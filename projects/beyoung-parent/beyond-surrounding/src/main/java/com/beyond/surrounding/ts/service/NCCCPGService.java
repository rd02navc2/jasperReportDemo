package com.beyond.surrounding.ts.service;

import com.beyond.surrounding.ts.bean.NCCCPaymentBean;
import com.beyond.surrounding.ts.bean.NCCCPaymentDetailBean;
import com.beyond.surrounding.ts.bean.TSRequestBean;
import com.beyond.surrounding.ts.repository.NcccPgRepository;
import com.beyond.surrounding.util.GetDateTime;
import com.beyond.surrounding.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NCCCPGService {

    private final NcccPgRepository ncccPgRepository;

    /**
     * 儲存 NCCC 原始交易/請求紀錄 (由舊 DAO 的 delete + insert 組成)
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(TSRequestBean requestBody) throws Exception {
        log.info("【NCCC】開始紀錄交易請求資料, 單號: {}", requestBody.getOrder_no());
        
        // 1. 先行刪除可能重複的單號
        ncccPgRepository.deleteByOrderNoNative(requestBody.getOrder_no());
        
        // 2. 寫入新授權封包日誌
        LocalDateTime now = LocalDateTime.now();
        ncccPgRepository.insertNative(
                requestBody.getOrder_no(),
                requestBody.getEc_order_no(),
                requestBody.getAmt(),
                requestBody.getOrder_desc(),
                requestBody.getCard_no(),
                requestBody.getInstall_period(),
                now, // access_date
                now  // auth_date
        );
        log.info("【NCCC】交易請求資料寫入成功!");
    }

    /**
     * 更新 NCCC 交易回傳狀態（不含退款金額）
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String orderNo, String txType, String approveCode, String retCode, String retMsg) throws Exception {
        log.info("【NCCC】更新標準授權狀態 -> 訂單號: {}, 授權碼: {}, 回傳碼: {}", orderNo, approveCode, retCode);
        
        ncccPgRepository.updateNative(
                orderNo, 
                txType, 
                approveCode, 
                retCode, 
                retMsg, 
                LocalDateTime.now()
        );
    }

    /**
     * 更新 NCCC 交易回傳狀態（包含退款金額與取消日期處理）
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String orderNo, String txType, String approveCode, String retCode, String retMsg, Integer amtRefund) throws Exception {
        if (amtRefund == null) {
            // 安全導向無退款的更新
            this.update(orderNo, txType, approveCode, retCode, retMsg);
            return;
        }
        
        log.info("【NCCC】更新退款交易狀態 -> 訂單號: {}, 退款金額: {}, 回傳碼: {}", orderNo, amtRefund, retCode);
        
        LocalDateTime now = LocalDateTime.now();
        ncccPgRepository.updateRefundNative(
                orderNo,
                txType,
                approveCode,
                retCode,
                retMsg,
                now, // access_date
                amtRefund,
                now  // auth_cancel_date
        );
    }

    /**
     * 產生 NCCC 結帳/請款媒體檔 (.dat)
     */
    public void genDat(NCCCPaymentBean requestBody, String sDir) throws Exception {
    	if (sDir == null || sDir.trim().isEmpty()) {
            throw new IllegalArgumentException("系統組態異常：環境變數 [nccc_payment_dir] 未設定，請檢查 application.properties。");
        }
        // 1. 防禦性建立目錄
        File baseDir = new File(sDir);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        File bakDir = new File(baseDir, "bak");
        if (!bakDir.exists()) {
            bakDir.mkdirs();
        }

        File targetFile = new File(baseDir, requestBody.getMid() + ".dat");

        // 2. 使用 try-with-resources 自動關閉檔案串流，避免異常時檔案被作業系統鎖死
        try (java.io.FileWriter writer = new java.io.FileWriter(targetFile, false)) {
            
            // 寫入 Header (H)
            writer.append("H");
            writer.append(requestBody.getMid());
            writer.append(requestBody.getSend_date());
            writer.append(StringUtil.fillzero(requestBody.getSerial_no(), 10));
            writer.append(StringUtil.fillzero(requestBody.getTotal_cnt() + "", 12));
            
            //  防禦機制：若前端未傳送金額正負號，預設給予空字串或加號
            String amtSign = requestBody.getAmt_sign() != null ? requestBody.getAmt_sign() : " ";
            writer.append(amtSign);
            
            writer.append(StringUtil.fillzero(requestBody.getAmt() + "", 12));
            writer.append(String.join("", java.util.Collections.nCopies(216, " ")));
            writer.append("\n");

            // 3. 安全檢查：避免 Postman 測試未給 detail 陣列導致 NullPointerException
            if (requestBody.getDetail() != null) {
                for (NCCCPaymentDetailBean detailBean : requestBody.getDetail()) {
                    log.info("Upload NCCC EC2Api Detail : order_no -> {}, price -> {}, order_type -> {} (01退貨/02請款)",
                            detailBean.getOrder_no(), detailBean.getPrice(), detailBean.getOrder_type());
                    
                    writer.append(detailBean.getMid());
                    writer.append(detailBean.getTid());
                    writer.append(StringUtil.padR(detailBean.getOrder_no(), 40, " "));
                    writer.append(String.join("", java.util.Collections.nCopies(19, " ")));
                    writer.append(StringUtil.fillzero(detailBean.getPrice() + "", 8));
                    writer.append(StringUtil.padR(detailBean.getApprove_code(), 8, " "));
                    writer.append(detailBean.getOrder_type());
                    writer.append(detailBean.getOrder_date());
                    writer.append(String.join("", java.util.Collections.nCopies(167, " ")));
                    writer.append("\n");
                }
            } else {
                log.warn("警告：NCCCPaymentBean 中的 detail 列表為空，僅產出 Header 資料！");
            }
        } // 離開區塊時，writer 會被保證確實 Close 並寫入磁碟

        // 4. 備份檔案機制
        String timestamp = GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli("");
        File backupFile = new File(bakDir, requestBody.getMid() + "_" + timestamp + ".dat");
        
        org.apache.commons.io.FileUtils.copyFile(targetFile, backupFile);
        log.info("【NCCC】媒體檔請款檔產製成功！實體路徑: {}, 備份路徑: {}", targetFile.getAbsolutePath(), backupFile.getAbsolutePath());
    }

    /**
     * 讀取並解析 NCCC 回傳的媒體回應檔
     */
    public NCCCPaymentBean readRsp(NCCCPaymentBean bean, String fileName, String dir) throws Exception {
        log.info("【NCCC】開始讀取媒體回應檔 -> 檔名: {}, 目錄: {}", fileName, dir);
        
        File file = new File(dir, fileName);
        if (!file.exists()) {
            throw new java.io.FileNotFoundException("找不到 NCCC 回應媒體檔: " + file.getAbsolutePath());
        }

        List<NCCCPaymentDetailBean> detailList = new ArrayList<>();
        
        // 取得系統編碼，確保與當初產製時的環境編碼對齊
        String systemEncoding = System.getProperty("sun.jnu.encoding");
        Charset charset = systemEncoding != null ? Charset.forName(systemEncoding) : java.nio.charset.StandardCharsets.UTF_8;

        // 使用 try-with-resources 自動關閉檔案讀取串流
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, charset);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                // 略過 Header 行（通常 NCCC 媒體檔首行若以 'H' 開頭則不屬於明細）
                if (line.startsWith("H")) {
                    continue;
                }

                // 確保這一行的字元長度足夠，防止 IndexOutOfBoundsException
                if (line.length() < 184) {
                    log.warn("忽略格式長度不足的行: {}", line);
                    continue;
                }

                NCCCPaymentDetailBean detailBean = new NCCCPaymentDetailBean();

                // 捨棄舊有危險的 Arrays.copyOfRange(_s.getBytes()) 寫法，直接使用標準且對齊定長長度的 substring
                // 舊邏輯對照位元組區間: [18-58] 訂單號, [159-165] 請款日, [165-168] 回應碼, [168-184] 回應訊息
                detailBean.setOrder_no(line.substring(18, 58).trim());
                detailBean.setPayment_date(line.substring(159, 165).trim());
                detailBean.setResponse_code(line.substring(165, 168).trim());
                detailBean.setResponse_message(line.substring(168, 184).trim());

                log.info("解析 NCCC 回應明細 -> 訂單: {}, 結果碼: {}, 訊息: {}", 
                        detailBean.getOrder_no(), detailBean.getResponse_code(), detailBean.getResponse_message());

                detailList.add(detailBean);
            }
        }

        bean.setDetail(detailList);
        return bean;
    }
    
}