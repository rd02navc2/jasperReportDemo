package com.beyoung.surrounding.pos2.service;

import com.beyoung.surrounding.pos2.bean.RefundBean;
import com.beyoung.surrounding.pos2.client.PacePayPos2FeignClient;
import com.beyoung.surrounding.pos2.repository.PacePayPos2Repository;
import com.beyoung.surrounding.pos2.bean.PaceResponseBean;
import com.beyoung.surrounding.pos2.entity.PACE_TRANSACTION_LOG;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacePayPos2Service {

    private final PacePayPos2Repository pacePayPos2Repository;  
    private final PacePayPos2FeignClient pacePayPos2FeignClient;
    private final Environment env;
    // private final SessionFactory sessionFactory;
    
    @Transactional(readOnly = true)
    public void addTranLogPending(String orderId, String center, String counterId, String posId, Date posDateTime,
                                  String productName, Integer amount, String decryptedKey, String transactionId, Date transactionDate) {
        log.info("準備寫入原始交易紀錄至資料庫 (PENDING) - OrderId: {}", orderId);

        PACE_TRANSACTION_LOG logEntity = new PACE_TRANSACTION_LOG();
        logEntity.setOrderId(orderId);
        logEntity.setPosCenter(center);
        logEntity.setPosCounterId(counterId);
        logEntity.setPosId(posId);
        logEntity.setPosDateTime(posDateTime);
        logEntity.setPosProductName(productName);
        logEntity.setPosAmount(amount);
        logEntity.setOneTimeKey(decryptedKey);
        logEntity.setTransactionId(transactionId);
        logEntity.setTransactionDate(transactionDate);
        logEntity.setTransactionType("PENDING");
        logEntity.setAccessDate(new Date());

        pacePayPos2Repository.save(logEntity);
        log.info("成功寫入 PENDING 交易紀錄，OrderId: {}", orderId);
    }


    /**
	 * 執行 PacePay 遠端下單建立交易流程
	 * (100% 補齊舊邏輯：計算過期時間、組裝 Payload、發起請求、自動追蹤內部 Query、回傳 Response)
	 */
    @Transactional(readOnly = true)
	public PaceResponseBean pacepayPayment(String orderId, Integer amount) {
		log.info("【PacePay 支付下單】開始處理 - 訂單編號(orderId): {}, 金額(amount): {}", orderId, amount);
		
		try {
			// 1. 取得 YAML 設定檔中的參數（若不存在則帶入安全預設值）
			String expiringAtConfig = env.getProperty("pacepay.expiring-at", "60000");
			String currencyConfig = env.getProperty("pacepay.currency", "TWD");
			
			// 2. 計算過期時間 (補齊舊邏輯：目前時間 + 延遲毫秒數，並轉換為 UTC 時間格式字串)
			long expiringMillis = Long.parseLong(expiringAtConfig);
			Date expiringDate = new Date(System.currentTimeMillis() + expiringMillis);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String expiringAtStr = sdf.format(expiringDate);
			
			// 3. 組裝下單請求 Request Body (改用 Map 靈活組裝)
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("referenceId", orderId);
			requestBody.put("amount", amount);
			requestBody.put("currency", currencyConfig);
			requestBody.put("expiringAt", expiringAtStr);
			
			log.info("【PacePay 支付下單】Feign Up (發送請求) -> 參數: {}", requestBody);
			
			// 4. 透過 OpenFeign 宣告式客戶端發送 HTTP POST 請求
			PaceResponseBean paceResponseBean = pacePayPos2FeignClient.pacepayPayment(requestBody); 
			
			if (paceResponseBean == null) {
				log.error("【PacePay 支付下單】遠端支付平台未回傳任何資料 (Response 爲空)");
				throw new IllegalStateException("PacePay 下單失敗，平台無回應");
			}
			
			log.info("【PacePay 支付下單】Feign Down (接收回應) -> 遠端交易序號(TransactionID): {}, 狀態(Status): {}", 
					paceResponseBean.getTransactionId(), paceResponseBean.getStatus());
			
			// 5. 補齊舊邏輯：下單成功後，立刻同步發起一次交易內容查詢
			String transactionId = paceResponseBean.getTransactionId();
			if (transactionId != null && !transactionId.isEmpty()) {
				log.info("【PacePay 支付下單】自動觸發同步狀態查詢，交易序號: {}", transactionId);
				// 直接調用當前類別或 feign 提供的查詢方法
				pacePayPos2FeignClient.pacepayQuery(transactionId);
			}
			
			// 6. 返回最終結果
			return paceResponseBean;
			
		} catch (Exception e) {
			log.error("【PacePay 支付下單】流程執行期間發生嚴重異常！錯誤訊息: ", e);
			// 建立一個包含錯誤代碼與訊息的錯誤 Bean 回傳，避免 Controller 收到 null 崩潰
			PaceResponseBean errorBean = new PaceResponseBean();
			errorBean.setCode("500");
			errorBean.setMessage("PacePay 下單異常: " + e.getMessage());
			return errorBean;
		}
	}

	/**
	 * 執行 PacePay 遠端一維條碼/付款碼有效性驗證
	 * (100% 補齊舊邏輯：參數轉換、封裝 Payload、發起 Feign 請求、回傳 Response)
	 */
	@Transactional(readOnly = true)
	public PaceResponseBean pacepayVerifyBarcode(String transactionId, String decryptedKey) {
		log.info("【PacePay 條碼驗證】開始處理 - 交易序號(transactionId): {}, 密鑰長度: {} 碼", 
				transactionId, (decryptedKey != null ? decryptedKey.length() : 0));
		
		try {
			// 1. 安全防禦檢查
			if (transactionId == null || transactionId.isEmpty()) {
				throw new IllegalArgumentException("交易序號 (TransactionId) 不可為空");
			}
			
			// 2. 組裝請求 Body (舊程式碼的 sOneTimeKey 解密後傳入此處作為 decryptedKey，即為 API 所需之 barcode)
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("barcode", decryptedKey);
			
			log.info("【PacePay 條碼驗證】Feign Up (發送驗證請求) -> TransactionId: {}", transactionId);
			
			// 3. 透過 OpenFeign 客戶端發送 HTTP POST 請求到指定交易路徑的 /verify_barcode
			PaceResponseBean response = pacePayPos2FeignClient.pacepayVerifyBarcode(transactionId, requestBody);
			
			if (response == null) {
				log.error("【PacePay 條碼驗證】遠端支付平台未回傳任何資料 (Response 爲空)");
				throw new IllegalStateException("PacePay 條碼驗證失敗，平台無回應");
			}
			
			log.info("【PacePay 條碼驗證】Feign Down (接收回應) -> 驗證結果成功狀態(Success): {}, 回傳代碼(Code): {}", 
					response.getSuccess(), response.getCode());
			
			// 4. 返回遠端驗證結果模型
			return response;
			
		} catch (Exception e) {
			log.error("【PacePay 條碼驗證】流程執行期間發生嚴重異常！錯誤原因: ", e);
			// 建立一個包含錯誤代碼與訊息的錯誤 Bean 回傳，確保 Controller 層能正常捕獲狀態
			throw e;
		}
	}

	@Transactional(readOnly = true)
	public PaceResponseBean pacePayRefund(String transactionId, Integer amount) throws Exception {
	        
	        // 1. 取得設定檔資訊
	        String clientId = env.getProperty("PACE-PlaygroundClientID");
	        String clientSecret = env.getProperty("PACE-PlaygroundClientSecret");
	        String currency = env.getProperty("PACE-Currency");
	        String justification = env.getProperty("PACE-Refund-Justification");
	        
	        // 2. 產生 Basic Auth 認證字串
	        String apiKey = getApiKey(clientId, clientSecret);
	        String authHeader = "Basic " + apiKey;
	
	        // 3. 組裝 Request Body
	        RefundBean refundBean = new RefundBean();
	        refundBean.setTransactionId(transactionId);
	        refundBean.setAmount(amount);
	        refundBean.setCurrency(currency);
	        refundBean.setJustification(justification);
	
	        log.info("Up(createRefundRequest)：{}", refundBean);
	        
	        // 4. 直接透過 Feign 發送 POST 請求
	        PaceResponseBean paceResponseBean = pacePayPos2FeignClient.refund(authHeader, refundBean);
	        
	        if (paceResponseBean != null) {
	            log.info("Down(createRefundRequest)：TransactionID -> {}, Status -> {}, RefundId -> {}", 
	                    paceResponseBean.getTransactionId(), 
	                    paceResponseBean.getStatus(), 
	                    paceResponseBean.getRefundId());
	        }
	        
	        return paceResponseBean;
	    }
	
	    private String getApiKey(String clientId, String clientSecret) {
	        return Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
	    }
	
	/*
	@Transactional(readOnly = true)
    public PACE_TRANSACTION_LOG getTranLog(String orderId) throws Exception {
        Session session = sessionFactory.getCurrentSession();

        // 移除多餘的 "where 1=1" 與字串拼接，改用更乾淨的寫法
        String sql = "SELECT * FROM PACE_TRANSACTION_LOG WHERE order_id = :orderId";

        // 在新版 Hibernate 中，建議使用 createNativeQuery 並指定返回的 Class
        NativeQuery<PACE_TRANSACTION_LOG> query = session.createNativeQuery(sql, PACE_TRANSACTION_LOG.class);
        query.setParameter("orderId", orderId);
        
        List<PACE_TRANSACTION_LOG> list = query.list();
        
        // 原本的邏輯：如果有多筆，只取最後一筆；如果沒資料，回傳空的實體
        PACE_TRANSACTION_LOG bean = new PACE_TRANSACTION_LOG();
        for (PACE_TRANSACTION_LOG record : list) {
            bean = record;
        }

        return bean;
    }
    */
	
	@Transactional(readOnly = true)
	public PACE_TRANSACTION_LOG getTranLog(String orderId) {
	    List<PACE_TRANSACTION_LOG> list = pacePayPos2Repository.findByOrderId(orderId);
	    
	    PACE_TRANSACTION_LOG bean = new PACE_TRANSACTION_LOG();
	    for (PACE_TRANSACTION_LOG record : list) {
	        bean = record;
	    }
	    return bean;
	}

	@Transactional(readOnly = true)
	public PaceResponseBean pacePayQuery(String transactionId) throws Exception {
	        
        // 2. 記錄請求日誌 (改用 SLF4J 佔位符)
        log.info("Up(getTransaction)：TransactionID -> {}", transactionId);
        
        // 3. 透過 Feign 直接呼叫 GET 端點
        // 注意：如果您的 FeignClient 有配置 RequestInterceptor 處理認證，甚至可以不傳 authHeader。
        // 這裡假設需要帶入 header，若不需要可配合您原介面定義調整。
        PaceResponseBean responseBean = pacePayPos2FeignClient.pacepayQuery(transactionId);
        
        // 4. 記錄回應日誌
        if (responseBean != null) {
            log.info("Down(getTransaction)：TransactionID -> {}, Status -> {}", 
                    responseBean.getTransactionId(), 
                    responseBean.getStatus());
        }
        
        return responseBean;
    }


	@Transactional(readOnly = true)
	public void updTranLogRefund(String orderId, String refundTranXId, Date timeZoneTransform, String invoiceNo) {
	    // 呼叫 Repository 的 @Modifying 方法更新資料庫
		pacePayPos2Repository.updTranLogRefund(orderId, refundTranXId, timeZoneTransform, invoiceNo);
	}

	
	
}