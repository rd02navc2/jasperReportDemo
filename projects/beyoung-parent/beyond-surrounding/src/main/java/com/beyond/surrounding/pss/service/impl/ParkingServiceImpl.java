package com.beyond.surrounding.pss.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beyond.surrounding.pss.bean.DiscountDetailBean;
import com.beyond.surrounding.pss.bean.ParkingDiscountExecBean;
import com.beyond.surrounding.pss.entity.TcPsaFile;
import com.beyond.surrounding.pss.entity.ParkingDiscountExec;
import com.beyond.surrounding.pss.entity.ParkingDiscountExecHeader;
import com.beyond.surrounding.pss.entity.ParkingDiscountSet;
import com.beyond.surrounding.pss.entity.ParkingRent;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.pss.repository.ParkingRepository;
import com.beyond.surrounding.pss.service.ParkingService;
import com.beyond.surrounding.util.ErrCodeConst; // 請根據您專案實際套件路徑調整
import com.beyond.surrounding.util.GetDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    /**
     * 1. 核心功能：停車發票補登入口實作
     */
    @Override
    public ResponseBean appendInvoice4Parking(String invoiceDate, String invoiceNo, String randomNo, String invoiceTime, 
                                             String center, String channel, Integer tranXType, String counterId, 
                                             String cardNo, String carNo, Double promoteAmt) throws Exception {
    
        ResponseBean responseBean = new ResponseBean();
        try {
            if (tranXType == 0) {
                parkingRepository.executeParkingAppend(invoiceDate, invoiceNo, randomNo, invoiceTime, center, 
                                                       channel, tranXType, counterId, cardNo, carNo, promoteAmt);
            } else {
                parkingRepository.executeParkingAppend(invoiceDate, invoiceNo, randomNo, invoiceTime, center, 
                                                       channel, tranXType, counterId, cardNo, carNo, -promoteAmt);
            }
            
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage("停車發票補登寫入成功");
            responseBean.setYn("Y");
        } catch (Exception e) {
            log.error("資料庫寫入停車折抵失敗: ", e);
            responseBean.setCode(ErrCodeConst.error);
            responseBean.setMessage("寫入折抵失敗: " + e.getMessage());
            responseBean.setYn("N");
        }
        return responseBean;
    }

    /**
     * 2. 核心功能：檢查發票於停車場域狀態
     */
    @Override
    @Transactional(readOnly = true)
    public TcPsaFile check4Parking(String invoiceNo, String randomNo) throws Exception {
        log.info("發票停車狀態校驗: {}, 隨機碼: {}", invoiceNo, randomNo);
        
        String todayStr = GetDateTime.getTodayDateW("-");
        Optional<TcPsaFile> optionalTcPsa;

        if ("uncheck".equals(randomNo)) {
            optionalTcPsa = parkingRepository.check4ParkingUncheck(invoiceNo, todayStr);
        } else {
            optionalTcPsa = parkingRepository.check4ParkingWithRandom(invoiceNo, randomNo, todayStr);
        }

        TcPsaFile responseBean = new TcPsaFile();

        if (optionalTcPsa.isPresent()) {
            TcPsaFile psaFile = optionalTcPsa.get();
            if ("02".equals(psaFile.getTcPsa06()) || "03".equals(psaFile.getTcPsa06())) {
                responseBean.setCode(ErrCodeConst.pos_refund);
                responseBean.setMessage(ErrCodeConst.pos_refund_message);
                return responseBean;
            }
            psaFile.setCode(ErrCodeConst.finished);
            return psaFile;
        } else {
            responseBean.setCode(ErrCodeConst.pos_parking_not_found);
            responseBean.setMessage(ErrCodeConst.pos_parking_not_found_message);
            return responseBean;
        }
    }

    /**
     * 3. 核心功能：針對自動繳費機日誌前置校驗
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseBean checklog4Parking(String invoiceNo, String randomNo) throws Exception {
        log.info("APS_UN 專用日誌校驗發票: {}", invoiceNo);
        ResponseBean responseBean = new ResponseBean();
        
        boolean isLogValid = parkingRepository.existsParkingLog(invoiceNo, randomNo);
        if (isLogValid) {
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage("日誌校驗成功");
            responseBean.setYn("Y");
        } else {
            responseBean.setCode(ErrCodeConst.error);
            responseBean.setMessage("此發票無法於無人機進行折抵或驗證碼不符");
            responseBean.setYn("N");
        }
        return responseBean;
    }

    /**
     * 【修正】若找不到資料則必須回傳 null，以防止外層業務中的 if (execCheck != null) 誤判失效
     */
    @Override
    @Transactional(readOnly = true)
    public ParkingDiscountExec getCardUsed(String carNo, String cardNo) throws Exception {
        return parkingRepository.getCardUsed(carNo, cardNo).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ParkingDiscountExec getVIPUsed(String carNo, String memberId) throws Exception {
        return parkingRepository.getVIPUsed(carNo, memberId).orElse(null);
    }

    /**
     * 【修正】將最後一個參數名從 dDiscHour 校正為 discHour，確保跟 Interface 的簽章完全重合
     */
    @Override
    public Integer insertCard(String loginId, String userId, String carNo, String cardNo, 
                              String discId, String discName, Double discHour) throws Exception {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        ParkingDiscountExecHeader header = parkingRepository.findTodayHeaderByCarNo(carNo, todayStr)
                                                            .orElseGet(() -> {
                                                                ParkingDiscountExecHeader newHeader = new ParkingDiscountExecHeader();
                                                                newHeader.setCarNo(carNo);
                                                                newHeader.setAccessDate(ts); 
                                                                return parkingRepository.saveHeader(newHeader);
                                                            });

        ParkingDiscountExec exec = new ParkingDiscountExec();
        exec.setPNo(header.getPNo());
        exec.setDiscId(discId);
        exec.setDiscName(discName);
        exec.setDiscHour(discHour);
        exec.setCardId(cardNo); 
        exec.setIsUsed("N");

        parkingRepository.saveExec(exec);
        return header.getPNo();
    }
  
    @Override
    @Transactional(readOnly = true)
    public List<ParkingDiscountSet> getDiscExec2(String carNo) throws Exception {
        List<Object[]> rows = parkingRepository.getDiscExec2Native(carNo);
        log.info("【getDiscExec2】查詢結果筆數: {}", (rows != null ? rows.size() : "null"));
        List<ParkingDiscountSet> list = new ArrayList<>();
        
        if (rows != null) {
            for (Object[] row : rows) {
                ParkingDiscountSet bean = new ParkingDiscountSet();
                if (row[0] != null) bean.setPNo(((Number) row[0]).intValue());
                if (row[1] != null) bean.setDiscId(row[1].toString());
                if (row[2] != null) bean.setDiscName(row[2].toString());
                if (row[3] != null) bean.setIsUsed(row[3].toString());
                list.add(bean);
            }
        }
        return list;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ParkingDiscountSet> getDiscExec(String carNo) throws Exception {
        List<Object[]> results = parkingRepository.getDiscExec2Native(carNo);
        List<ParkingDiscountSet> list = new ArrayList<>();
        
        if (results != null) {
            for (Object[] row : results) {
                ParkingDiscountSet set = new ParkingDiscountSet();
                set.setDiscId((String) row[0]); 
                set.setDiscName((String) row[1]);
                if (row[2] != null) {
                    set.setPNo(Integer.valueOf(row[2].toString()));
                }
                set.setIsUsed(row[3] != null ? (String) row[3] : "N");
                list.add(set);
            }
        }
        return list;
    } 

    @Override
    public void delCardNo(String center, String cardNo, Integer pNo) throws Exception {
        parkingRepository.deleteCardNo(pNo, cardNo);
    }
    
    /**
     * 【修正】恢復原本舊系統核心的穿透查詢，避免回傳 null 造成後續邏輯中斷
     */
    @Override
    @Transactional(readOnly = true)
    public ParkingRent getParkingRent(String carNo) throws Exception {
        return parkingRepository.getParkingRent(carNo).orElse(null);
    }

    /**
     * 【修正】補齊原本舊系統的資料庫同步邏輯
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 涉及大量寫入、更新，必須確保交易完整性
    public void sync(DiscountDetailBean requestBody) throws Exception {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 宣告用來暫存的主鍵 PNo
        Integer finalPNo = requestBody.getPNo();
        
        // =========================================================================
        // 1. 處理 PARKING_DISCOUNT_EXEC_HEADER (主表更新或新增)
        // =========================================================================
        // 舊註解：-1表示沒有用折抵，但可能有消費折抵(存在PARKING_POS_LOG)
        if (requestBody.getPNo() == null || requestBody.getPNo() == -1) {
            
            // 建立新版的 Header 實體並透過實體映射直接 save，免寫原生的 Insert SQL 語句
            ParkingDiscountExecHeader header = new ParkingDiscountExecHeader();
            header.setUserId(requestBody.getUserId());
            header.setCenter(requestBody.getCenter());
            header.setCardId(requestBody.getCardNo());
            header.setCarNo(requestBody.getCarNo());
            
            // 處理時間解析
            if (requestBody.getEnterDt() != null && !"".equals(requestBody.getEnterDt())) {
                header.setEnterDate(sdf.parse(requestBody.getEnterDt()));
            }
            if (requestBody.getExitDt() != null && !"".equals(requestBody.getExitDt())) {
                header.setExitDate(sdf.parse(requestBody.getExitDt()));
            }
            
            header.setParkingHour(requestBody.getParkingHour());
            header.setParkingFee(requestBody.getParkingFee());
            header.setDiscFee(requestBody.getDiscFee());
            header.setPayAmt(requestBody.getPayAmt());
            header.setPaidAmt(requestBody.getPaidAmt());
            header.setTotDiscHour(requestBody.getTotDiscHour());
            header.setRealDiscHour(requestBody.getRealDiscHour());
            header.setOtherDiscFee(requestBody.getOtherDiscFee());
            header.setOtherDiscHour(requestBody.getOtherDiscHour());
            header.setIsUsed("Y");
            header.setAccessDate(ts);
            header.setAccessId("PSS_Sync");
            
            // 呼叫 JPA Repository 儲存實體，JPA 會自動將自增產生的 pNo 回填至 header 物件中
            ParkingDiscountExecHeader savedHeader = parkingRepository.saveHeader(header);
            finalPNo = savedHeader.getPNo(); 
            
        } else {
            // 舊版 SQL 涉及多欄位 Update，這裏優雅地穿透到 Repository 執行帶有 @Modifying 的宣告式 JPQL/Native SQL
            parkingRepository.updateParkingHeader(
                    requestBody.getEnterDt(), requestBody.getExitDt(), requestBody.getCarNo(),
                    requestBody.getParkingHour(), requestBody.getParkingFee(), requestBody.getDiscFee(),
                    requestBody.getPayAmt(), requestBody.getPaidAmt(), requestBody.getTotDiscHour(),
                    requestBody.getRealDiscHour(), requestBody.getOtherDiscFee(), requestBody.getOtherDiscHour(),
                    "Y", ts, "PSS_Sync", requestBody.getPNo()
            );
        }

        // =========================================================================
        // 2. 處理明細更新迴圈 (PARKING_DISCOUNT_EXEC 與 PARKING_POS_LOG)
        // =========================================================================
        if (requestBody.getDiscount() != null) {
            for (ParkingDiscountExecBean execBean : requestBody.getDiscount()) {
                
                // 狀況 A：消費折抵
                if ("sale_hour".equals(execBean.getDiscId())) {
                    if ("Y".equals(execBean.getModify())) {
                        String todayStr = GetDateTime.getTodayDateW("-");
                        
                        // 計算最終要綁定的 PNo（若上面是新創的，就用 _head 剛生成的自增主鍵）
                        Integer bindingPNo = (requestBody.getPNo() == null || requestBody.getPNo() == -1) ? finalPNo : requestBody.getPNo();
                        
                        // 呼叫 Repository 更新停車發票日誌關聯
                        parkingRepository.updateParkingPosLog(
                                bindingPNo, requestBody.getUserId(), requestBody.getCardNo(), todayStr, requestBody.getCarNo()
                        );
                    }
                } 
                // 狀況 B：其餘會員卡、VIP、黑卡等一般折扣變更
                else {
                    if ("Y".equals(execBean.getModify())) {
                        // 呼叫 Repository 更新折扣執行表的狀態
                        parkingRepository.updateParkingDiscountExecStatus(
                                "Y", ts, "PSS_Sync", requestBody.getPNo(), requestBody.getCarNo(), execBean.getDiscId()
                        );
                    }
                }
            }
        }
    }
}