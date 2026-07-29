package com.beyoung.surrounding.pos2.controller;

import com.beyoung.surrounding.pos.entity.TAIWAN_TRANSACTION_LOG;
import com.beyoung.surrounding.pos.service.PurchaseService;
import com.beyoung.surrounding.pos.service.TaiwanPayPosService;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.beyoung.surrounding.bean.ResponseBean;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos2/TaiwanPay")
@RequiredArgsConstructor
public class TaiwanPayPos2Controller {

	private final PurchaseService purchaseService;
	private final TaiwanPayPosService taiwanPayPosService;

	@GetMapping(value = "/purchase",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean purchase(
	        @RequestParam String center,
	        @RequestParam String counterID,
	        @RequestParam String posID,
	        @RequestParam String posDateTime,
	        @RequestParam String productName,
	        @RequestParam Integer amt,
	        @RequestParam String oneTimeKey,
	        @RequestParam String orderID) {
		
		ResponseBean bean = new ResponseBean();
	
		try {
		    // 解密 key
		    String decryptedKey = new String(Hex.decodeHex(oneTimeKey.toCharArray()));
		    
		    // 呼叫 Service
		    String paymentRes = purchaseService.taiwanpayPayment(orderID, posID, amt, decryptedKey);
		    
		    JsonNode ret = new ObjectMapper().readTree(paymentRes);
	        String code = ret.path("ResponseCode").asText("");
	        String msg = ret.path("ResponseMsg").asText("");
	        
	        if (!"000000".equals(code)) {
	            bean.setCode(code);
	            bean.setMessage(msg);
	            log.error("orderID -> {}, {} {}", orderID, code, msg);
	            return bean; // 符合您要求的 return _bean
	        }
	
	     // 1. 執行查詢交易詳情
	        String queryRes = purchaseService.taiwanpayQuery(orderID, posID);
	        JsonNode retQuery = new ObjectMapper().readTree(queryRes);

	        // 2. 檢查查詢結果 (這部分的邏輯與您原始邏輯完全對應)
	        if (!"000000".equals(retQuery.path("ResponseCode").asText())) {
	        	bean.setCode(retQuery.path("ResponseCode").asText());
	        	bean.setMessage(retQuery.path("ResponseMsg").asText());
	            log.error("sOrderID -> {}, {} {}", orderID, bean.getCode(), bean.getMessage());
	            return bean; // 嚴格保持舊版的 return _bean 行為
	        }

	        // 3. 執行資料庫寫入
	        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        // SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
	        //dc-
	        /*        
	        taiwanPayPosService.addTranLog(
	            orderID, center, counterID, posID, sdf1.parse(posDateTime),
	            "比漾商品", amt, decryptedKey, ret.get("TradeNo").asText(), 
	            sdf.parse(retQuery.get("TransTime").asText())
	        );
	 		*/
	        // 4. 設定成功狀態
	        bean.setCode(ErrCodeConst.finished);
	        bean.setMessage(ErrCodeConst.finished_message);
	        
	        // 5. 嚴格保持 return _bean
	        return bean;
		}catch(Exception e){
		    log.error("付款失敗，orderID={}", orderID, e);

		    try{
		        String queryStatus =
		                purchaseService.taiwanpayQuery(orderID, posID);

		        log.info("Checking purchase result");

		        JsonNode retQuery =
		                new ObjectMapper().readTree(queryStatus);

		        if ("000000".equals(
		                retQuery.path("ResponseCode").asText())) {

		            String refundResult =
		                    purchaseService.taiwanpayRefund(
		                            amt, orderID);

		            log.info("refund result: {}", refundResult);
		        }

			} catch (Exception ee) {
				log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
				
				// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
				ResponseBean errorBean = new ResponseBean();
				errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
				errorBean.setMessage("Reading Space 連線測試作業失敗 " + ee.getMessage());
				return errorBean;	
				
			}
		}
		return bean;

	    		
	}
	
	@GetMapping(value = "/refund/{center}/{invoiceNO}/{orderID}/{posID}/{posDateTime}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean refund(
	        @PathVariable String center,
	        @PathVariable String invoiceNO,
	        @PathVariable String orderID,
	        @PathVariable String posID,
	        @PathVariable String posDateTime) throws JSONException {
		
		ResponseBean bean = new ResponseBean();
	    
	    try {
	        log.info("接收到退款請求: orderID={}, invoiceNO={}", orderID, invoiceNO);
	        
	        // 核心邏輯交由 Service 處理，回傳執行狀態或處理結果
	        taiwanPayPosService.processRefund(orderID, invoiceNO, posID);       
       
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
           
            return bean;      
	               
		} catch (Exception e) {
			log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
			
			// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
			errorBean.setMessage("Reading Space 連線測試作業失敗 " + e.getMessage());
			return errorBean;	
			
		}
	}


	@GetMapping(value = "/query/{orderID}/{posID}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public TAIWAN_TRANSACTION_LOG queryTransaction(
	        @PathVariable String orderID,
	        @PathVariable String posID) throws JSONException {
	
		TAIWAN_TRANSACTION_LOG bean = new TAIWAN_TRANSACTION_LOG();
	
	    try {
	
	        log.info("發起查詢請求: orderID={}", orderID);
	
	        String queryRes =
	                purchaseService.taiwanpayQuery(orderID, posID);
	
	        JsonObject retQuery =
	                JsonParser.parseString(queryRes).getAsJsonObject();
	
	        bean.setCode(retQuery.get("ResponseCode").getAsString());
	        bean.setMessage(retQuery.get("ResponseMsg").getAsString());
	        bean.setTransactionType(
	                retQuery.get("TransType").getAsString());
	        bean.setTransTime(
	                retQuery.get("TransTime").getAsString());
	        
	        return bean;
	
	    } catch (Exception e) {
            log.error("usePoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
	
}