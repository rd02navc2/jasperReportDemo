package com.beyoung.surrounding.pos2.service;

import com.beyoung.surrounding.bean.APPRequestBean;
import com.beyoung.surrounding.bean.APPResponseBean;
import com.beyoung.surrounding.bean.MemberIdentityBean;
import com.beyoung.surrounding.bean.Rcrm;
import com.beyoung.surrounding.bean.ReturnedBean;
import com.beyoung.surrounding.bean.SingBean;
import com.beyoung.surrounding.bean.TransactionCheckBean;
import com.beyoung.surrounding.bean.UploadBean;
import com.beyoung.surrounding.pos2.client.AppServerPos2Client;
import com.beyoung.surrounding.util.CryptUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service // 關鍵：必須補上這行，Spring 才能抓到這個 Bean 並注入給 Controller！
@RequiredArgsConstructor
public class UploadTransactionService {

    private final AppServerPos2Client appServerPos2Client;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public APPResponseBean<String> upload(
            String transactionId, 
            String identity, 
            String storeCode,
            String sourceUuid, 
            String invoiceNumber, 
            String invoiceRandomNumber, 
            Integer invoiceAmount,        // 補回對應 Controller 的金額欄位
            String transactionDatetime,  // 補回對應 Controller 的交易時間欄位
            String type, 
            Environment env) throws Exception {

        // 1. 組裝 會員身分 Bean
        MemberIdentityBean memberIdentityBean = new MemberIdentityBean();
        memberIdentityBean.setType(MemberIdentityBean.Type.cardno);
        
        if (MemberIdentityBean.Type.mobile.equalsIgnoreCase(type)) {
            memberIdentityBean.setType(MemberIdentityBean.Type.mobile);
        }
        memberIdentityBean.setIdentity(identity);

        // 2. 組裝 交易內容 Bean
        UploadBean uploadBean = new UploadBean();
        uploadBean.setTransactionId(transactionId); 
        uploadBean.setTransactionType("normal");
        uploadBean.setMemberIdentity(memberIdentityBean);
        uploadBean.setBrandCode("BYP");
        uploadBean.setStoreCode(storeCode);
        uploadBean.setSourceUuid(sourceUuid);
        uploadBean.setInvoiceNumber(invoiceNumber);
        uploadBean.setInvoiceRandomNumber(invoiceRandomNumber);
        
        // 依據原邏輯綁定：將數值轉為字串塞入原欄位
        if (invoiceAmount != null) {
            uploadBean.setInvoiceAmount(String.valueOf(invoiceAmount)); 
        }
        
        // 日期格式轉換：從傳入的 transactionDatetime (yyyyMMddHHmmss) 轉為 yyyy/MM/dd HH:mm:ss
        String formattedDatetime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new SimpleDateFormat("yyyyMMddHHmmss").parse(transactionDatetime));
        uploadBean.setTransactionDatetime(formattedDatetime);

        // 3. 打包外層 Request
        APPRequestBean<UploadBean> requestBean = new APPRequestBean<>();
        requestBean.setRequest_parameter(uploadBean);
        requestBean.setTimestamp(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        // 4. 進行 Base64 與 HmacSHA256 加密簽章
        String uploadJson = objectMapper.writeValueAsString(requestBean);
        String uploadJson64 = Base64.getEncoder().encodeToString(uploadJson.getBytes(StandardCharsets.UTF_8));
        
        String signature = CryptUtil.toHmacSHA256(uploadJson64, env.getProperty("SubscriptKey"));
        String signedToken = String.format("%s.%s", uploadJson64, signature);

        log.info("Start 上傳交易，參數 => {} api加密後的參數 => {\"sign\" : \"{}\"}", uploadJson, signedToken);

        // 5. 使用 Feign Client 發送請求
        String responseString = appServerPos2Client.uploadTransaction(
                env.getProperty("SubscriptAppId"),
                "POSAPI/1.0.0",
                new SingBean(signedToken)
        );

        log.info("Start 上傳交易 ， 回傳訊息 => {}", responseString);
        
        // 6. 解析回傳結果
        @SuppressWarnings("unchecked")
        APPResponseBean<String> appResponseBean = objectMapper.readValue(responseString, APPResponseBean.class);
        log.info("End 上傳交易");

        return appResponseBean;
    }

    @Transactional(readOnly = true)
    public APPResponseBean<String> returned(Environment env, String transactionId, String identity, String storeCode,
            String invoiceAmount, String transactionDatetime, String sourceTransactionId, String type) {
		log.info("Start 退貨交易");
		try {
			// 3. 會員身分類型判斷 (使用簡潔的三元運算子)
			MemberIdentityBean memberIdentityBean = new MemberIdentityBean();
			if (MemberIdentityBean.Type.mobile.equalsIgnoreCase(type)) {
	            memberIdentityBean.setType(MemberIdentityBean.Type.mobile);
	        }
	        memberIdentityBean.setIdentity(identity);
			
			// 4. 組裝退貨 Bean
			ReturnedBean returnedBean = new ReturnedBean();
			returnedBean.setTransactionId(transactionId);
			returnedBean.setTransactionType("cancel");
			returnedBean.setSource_transaction_id(sourceTransactionId);
			returnedBean.setMemberIdentity(memberIdentityBean);
			returnedBean.setBrandCode("BYP");
			returnedBean.setStoreCode(storeCode);
			returnedBean.setInvoiceAmount(invoiceAmount);
			
			// 使用執行緒安全的 Java 8 DateTimeFormatter 代替 SimpleDateFormat
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			String formattedTxDate = LocalDateTime.parse(transactionDatetime, inputFormatter).format(outputFormatter);
			returnedBean.setTransactionDatetime(formattedTxDate);
			
			// 5. 準備請求外殼與現在時間戳記
			APPRequestBean<ReturnedBean> requestBean = new APPRequestBean<>();
			requestBean.setRequest_parameter(returnedBean);
			requestBean.setTimestamp(LocalDateTime.now().format(outputFormatter));
			
			// 6. 轉換 JSON 與加密
			String uploadJson = objectMapper.writeValueAsString(requestBean);
			String uploadJson64 = Base64.getEncoder().encodeToString(uploadJson.getBytes(StandardCharsets.UTF_8));
			String sSignature = CryptUtil.toHmacSHA256(uploadJson64, env.getProperty("SubscriptKey"));
			String combinedPayload = String.format("%s.%s", uploadJson64, sSignature);
			
			log.info("退貨交易發送，參數 => {}，api加密後的參數 => {\"sign\" : \"{}\"}", uploadJson, combinedPayload);
			
			// 7. 直接透過 Feign 呼叫 API，乾淨俐落
			SingBean appReq = new SingBean(combinedPayload);
			String responseString = appServerPos2Client.sendReturnTransaction(
			env.getProperty("SubscriptAppId"),
			"POSAPI/1.0.0",
			appReq
			);
			
			log.info("退貨交易接收，回傳訊息 => {}", responseString);
			
			// 8. 使用 TypeReference 確保泛型轉型安全，不留 Raw Type 警告
			APPResponseBean<String> appResponseBean = objectMapper.readValue(
			responseString, 
			new TypeReference<APPResponseBean<String>>() {}
			);
			
			log.info("End 退貨交易");
			return appResponseBean;
		
		} catch (Exception e) {
			log.error("退貨交易處理失敗: {}", e.getMessage(), e);
			throw new RuntimeException("退貨交易處理異常", e);
			}
    }

    @Transactional(readOnly = true)
    public APPResponseBean<JsonObject> transactionCheck(Environment env, String type, String id) {
        log.info("Start 查詢交易");
        try {
            // 1. 組裝查詢 Request Bean
            TransactionCheckBean transactionCheckBean = new TransactionCheckBean();
            transactionCheckBean.setId(id);
            
            if ("tid".equalsIgnoreCase(type)) {
                transactionCheckBean.setType(TransactionCheckBean.Type.transactionId);
            } else if ("mmrm".equalsIgnoreCase(type)) {
                transactionCheckBean.setType(TransactionCheckBean.Type.mmrmTid);
            }

            APPRequestBean<TransactionCheckBean> requestBean = new APPRequestBean<>();
            requestBean.setRequest_parameter(transactionCheckBean);
            
            // 使用執行緒安全的時間格式化
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            requestBean.setTimestamp(LocalDateTime.now().format(outputFormatter));

            // 2. 物件序列化與加密處理
            String uploadJson = objectMapper.writeValueAsString(requestBean);
            String uploadJson64 = Base64.getEncoder().encodeToString(uploadJson.getBytes(StandardCharsets.UTF_8));
            String sSignature = CryptUtil.toHmacSHA256(uploadJson64, env.getProperty("SubscriptKey"));
            String combinedPayload = String.format("%s.%s", uploadJson64, sSignature);

            log.info("查詢交易，參數 => {}，api加密後的參數 => {\"sign\":\"{}\"}", uploadJson, combinedPayload);

            // 3. 透過 Feign Client 發送請求
            SingBean appReq = new SingBean(combinedPayload);
            String responseString = appServerPos2Client.checkTransaction(
                    env.getProperty("SubscriptAppId"),
                    "POSAPI/1.0.0",
                    appReq
            );

            log.info("查詢交易，回傳訊息 => {}", responseString);

            // 4. 解析回傳的 JSON 結構 (使用 Gson 新版推薦的靜態方法避免過時警告)
            JsonObject retM = JsonParser.parseString(responseString).getAsJsonObject();
            
            APPResponseBean<JsonObject> aPPResponseBean = new APPResponseBean<>();
            Rcrm rcrm = new Rcrm();
            
            JsonObject rcrmJson = retM.get("rcrm").getAsJsonObject();
            rcrm.setRC(rcrmJson.get("RC").getAsString());
            rcrm.setRM(rcrmJson.get("RM").getAsString());
            aPPResponseBean.setRcrm(rcrm);

            // 5. 判斷回傳狀態代碼
            if (!"C01".equals(rcrm.getRC())) {
                return aPPResponseBean;
            }

            // 成功則將 results (JsonObject) 塞入 Bean 回傳
            JsonObject retD = retM.get("results").getAsJsonObject();
            aPPResponseBean.setResults(retD);

            log.info("End 查詢交易");
            return aPPResponseBean;

        } catch (Exception e) {
            log.error("查詢交易處理異常: {}", e.getMessage(), e);
            throw new RuntimeException("查詢交易時發生錯誤", e);
        }
    }
    
    
}