package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.dto.ResponseDTO;
import com.beyond.surrounding.pos2.entity.TAIWAN_TRANSACTION_LOG;
import com.beyond.surrounding.pos2.repository.TaiwanPayPos2Repository;
import com.beyond.surrounding.util.ErrCodeConst;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaiwanPayPos2Service {

    private final TaiwanPayPos2Repository taiwanPayPos2Repository;
    private final PurchasePos2Service purchasePos2Service;
    
    /**
     * 儲存 TaiwanPay 交易日誌
     */
    @Transactional // 確保此操作在交易內執行
    public void addTranLog(String orderID, String center, String counterID, String posID, 
                           Date posDateTime, String productName, int amt, String oneTimeKey, 
                           String transactionID, Date transactionDate) {
        
        // 建議在 Service 層建立 Entity 物件
        TAIWAN_TRANSACTION_LOG log = TAIWAN_TRANSACTION_LOG.builder()
                .orderId(orderID)
                .posCenter(center)
                .posCounterId(counterID)
                .posProductName(productName)
                .posAmount(amt)
                .posId(posID)
                .posDateTime(posDateTime)
                .oneTimeKey(oneTimeKey)
                .accessDate(new Date()) // 使用 java.util.Date 取代 Timestamp
                .transactionType("PAYMENT")
                .transactionId(transactionID)
                .transactionDate(transactionDate)
                .build();

        taiwanPayPos2Repository.save(log);
    }

    @Transactional(rollbackOn = Exception.class) // 若發生任何 Exception，自動回滾交易
    public ResponseBean processRefund(String orderID, String invoiceNO, String posID) {

    	ResponseBean bean = new ResponseBean();
	
		try {		
			// 1. 取得原始交易紀錄
			TAIWAN_TRANSACTION_LOG logEntity =
			taiwanPayPos2Repository.findById(orderID)
			.orElseThrow(() ->
			      new Exception("找不到訂單紀錄: " + orderID));
			
			// 2. 執行退款 API
			String refundResult =
					purchasePos2Service.taiwanpayRefund(
			      logEntity.getPosAmount(),
			      orderID);
			
			JsonObject ret =
			JsonParser.parseString(refundResult)
			        .getAsJsonObject();
			
			if (!"000000".equals(
			ret.get("ResponseCode").getAsString())) {
			
			bean.setCode(
			  ret.get("ResponseCode").getAsString());
			
			bean.setMessage(
			  ret.get("ResponseMsg").getAsString());
			
			log.error("orderID -> {}, {} {}",
			  orderID,
			  bean.getCode(),
			  bean.getMessage());
			
			return bean;
			}
			
			// 3. 退款成功，取得退款交易編號
			String refundTranId =
			ret.get("TradeNo").getAsString();
			
			log.info(
			"Refund Detail : orderId -> {}, refundTranId -> {}",
			orderID, refundTranId);
			
			// 4. 查詢交易狀態
			String queryResult =
					purchasePos2Service.taiwanpayQuery(
			      orderID, posID);
			
			JsonObject retQuery =
			JsonParser.parseString(queryResult)
			        .getAsJsonObject();
			
			if (!"000000".equals(
			retQuery.get("ResponseCode").getAsString())) {
			
			bean.setCode(
			  retQuery.get("ResponseCode").getAsString());
			
			bean.setMessage(
			  retQuery.get("ResponseMsg").getAsString());
			
			log.error("orderID -> {}, {} {}",
			  orderID,
			  bean.getCode(),
			  bean.getMessage());
			
				return bean;
				}
			
				// 5. 更新 DB
				SimpleDateFormat sdf =
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Date transTime =
				sdf.parse(ret.get("TransTime").getAsString());
				
				taiwanPayPos2Repository.updateRefundInfo(
				orderID,
				refundTranId,
				transTime,
				invoiceNO);
				
				// 6. 成功
				bean.setCode(ErrCodeConst.finished);
				bean.setMessage(ErrCodeConst.finished_message);
				
				return bean;
			
			} catch (Exception e) {
			
			log.error("退款失敗", e);
			
			bean.setCode("999999");
			bean.setMessage(e.getMessage());
			
			return bean;
			}
	
    }

    public ResponseDTO<?> queryTransactionStatus(String orderID) throws Exception {
        String queryResult = purchasePos2Service.taiwanpayQuery(orderID, null);
        JsonObject retQuery = JsonParser.parseString(queryResult).getAsJsonObject();
        
        // 安全地取得欄位值，若不存在則給預設空字串
        String code = retQuery.has("ResponseCode") ? retQuery.get("ResponseCode").getAsString() : "9999";
        String msg = retQuery.has("ResponseMsg") ? retQuery.get("ResponseMsg").getAsString() : "查詢無回應";
        
        Map<String, String> data = new HashMap<>();
        if ("000000".equals(code)) {
            data.put("transactionType", retQuery.has("TransType") ? retQuery.get("TransType").getAsString() : "");
            data.put("transTime", retQuery.has("TransTime") ? retQuery.get("TransTime").getAsString() : "");
        }
        
        return ResponseDTO.success(code, msg);
    }
    
    
    
}