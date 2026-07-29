package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.pos.entity.PX_TRANSACTION_LOG;
import com.beyond.surrounding.pos.service.PurchaseService;
import com.beyond.surrounding.pos.service.PxPayPosService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Hex;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyond.surrounding.bean.ResponseBean;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos2/PXPay")
@RequiredArgsConstructor
public class PXPayPos2Controller {

	private final PurchaseService purchaseService; 
	private final PxPayPosService pxPayPosService;

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
	        @RequestParam String orderID) throws JSONException {
	
	    ResponseBean bean = new ResponseBean(); // 移到外面
	
	    try {
	
	        String decryptedKey =
	                new String(Hex.decodeHex(oneTimeKey.toCharArray()));
	
	        log.info("Purchase started: orderID={}", orderID);
	
	        // 1. 呼叫支付 API
	        String paymentRes = purchaseService.pxpayPayment(
	                orderID,
	                center,
	                counterID,
	                posID,
	                posDateTime,
	                productName,
	                amt,
	                decryptedKey);
	
	        JsonNode ret = new ObjectMapper().readTree(paymentRes);
	
	        // 支付失敗
	        if (!"0000".equals(ret.path("status_code").asText())) {
	
	            bean.setCode(ret.path("status_code").asText());
	            bean.setMessage(
	                    ret.path("status_message").asText("支付失敗"));
	
	            log.error("orderID -> {}, {} {}",
	                    orderID,
	                    bean.getCode(),
	                    bean.getMessage());
	
	            return bean;
	        }
	
	        // 2. 查詢交易狀態
	        String transactionId = ret.path("px_trade_no").asText();
	        String carrier = ret.path("invo_carrier").asText();
	
	        log.info(
	                "Payment Detail : orderId -> {}, transactionId -> {}, carrier -> {}",
	                orderID, transactionId, carrier);
	
	        String queryStatus =
	                purchaseService.pxpayQuery(orderID);
	
	        JsonNode retQuery =
	                new ObjectMapper().readTree(queryStatus);
	
	        if (!"0000".equals(retQuery.path("status_code").asText())) {
	
	            bean.setCode(retQuery.path("status_code").asText());
	            bean.setMessage(
	                    retQuery.path("status_message").asText());
	
	            return bean;
	        }
	
	        // trade_info 檢查
	        JsonNode tradeInfo = retQuery.path("trade_info");
	
	        if (!"0000".equals(tradeInfo.path("status_code").asText())) {
	
	            bean.setCode(tradeInfo.path("status_code").asText());
	            bean.setMessage(
	                    tradeInfo.path("status_message").asText());
	
	            return bean;
	        }
	
	        // 3. 寫入 DB
	        JsonNode payToolInfo = ret.path("pay_tool_info");
	
	        SimpleDateFormat sdf =
	                new SimpleDateFormat("yyyyMMddHHmmss");
	
	        pxPayPosService.addTranLog(
	                orderID,
	                center,
	                counterID,
	                posID,
	                sdf.parse(posDateTime),
	                productName,
	                amt,
	                decryptedKey,
	                transactionId,
	                sdf.parse(ret.path("px_trade_time").asText()),
	                payToolInfo.path("pay_tool").asText(),
	                payToolInfo.path("tool_name").asText(),
	                payToolInfo.path("identity").asText(),
	                carrier);
	
	        // 成功
	        bean.setInvo_carrier(carrier);
	        bean.setCode(ErrCodeConst.finished);
	        bean.setMessage(ErrCodeConst.finished_message);
	
	        return bean;
	
	    } catch (Exception e) {
	
	        log.error("Purchase failed: orderID={}", orderID, e);
	
	        try {
	
	            String queryStatus =
	                    purchaseService.pxpayQuery(orderID);
	
	            JsonObject retQuery =
	                    JsonParser.parseString(queryStatus)
	                            .getAsJsonObject();
	
	            JsonObject tradeInfo =
	                    retQuery.getAsJsonObject("trade_info");
	
	            if (tradeInfo != null &&
	                    "0000".equals(
	                            tradeInfo.get("status_code")
	                                    .getAsString())) {
	
	                String refundResult =
	                        purchaseService.pxpayRefund(
	                                tradeInfo.get("px_trade_no")
	                                        .getAsString(),
	                                orderID,
	                                center,
	                                posID,
	                                productName,
	                                amt,
	                                "R" + orderID,
	                                posDateTime);
	
	                log.info("refund result: {}", refundResult);
	            }
	
			} catch (Exception ee) {
				log.error("Reading Space 連線測試作業失敗: {}", ee.getMessage(), ee);
				
				// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
				ResponseBean errorBean = new ResponseBean();
				errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
				errorBean.setMessage("Reading Space 連線測試作業失敗 " + ee.getMessage());
				return errorBean;	
				
			}
		}

		return bean;
    }
	    
	    
    @GetMapping(value = "/refund/{center}/{invoiceNo}/{orderId}/{newOrderId}/{posDateTime}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean refund(
	        @PathVariable String center,
	        @PathVariable String invoiceNo,
	        @PathVariable String orderId,
	        @PathVariable String newOrderId,
	        @PathVariable String posDateTime) throws JSONException {
	
	    ResponseBean bean = new ResponseBean(); // ← 移到 try 外面
	
	    try {
	
	        log.info("開始執行退款流程: orderId={}, newOrderId={}",
	                orderId, newOrderId);
	
	        // 1. 取得原始交易資料
	        PX_TRANSACTION_LOG entity =
	                pxPayPosService.getTranLog(orderId);
	
	        if (entity == null) {
	
	            bean.setCode("404");
	            bean.setMessage("找不到原始交易紀錄");
	
	            log.error("orderID -> {}, 找不到原始交易紀錄",
	                    orderId);
	
	            return bean;
	        }
	
	        // 2. 呼叫退款 API
	        String refundRes = purchaseService.pxpayRefund(
	                entity.getTransactionId(),
	                orderId,
	                center,
	                entity.getPosId(),
	                entity.getPosProductName(),
	                entity.getPosAmount(),
	                newOrderId,
	                posDateTime);
	
	        JsonNode ret =
	                new ObjectMapper().readTree(refundRes);
	
	        if (!"0000".equals(ret.path("status_code").asText())) {
	
	            bean.setCode(ret.path("status_code").asText());
	            bean.setMessage(
	                    ret.path("status_message")
	                            .asText("退款請求失敗"));
	
	            return bean;
	        }
	
	        // 3. 查詢退款結果
	        String refundTranId =
	                ret.path("px_trade_no").asText();
	
	        String queryRes =
	                purchaseService.pxpayQuery(newOrderId);
	
	        JsonNode retQuery =
	                new ObjectMapper().readTree(queryRes);
	
	        if (!"0000".equals(retQuery.path("status_code").asText())) {
	
	            bean.setCode(retQuery.path("status_code").asText());
	            bean.setMessage(
	                    retQuery.path("status_message").asText());
	
	            return bean;
	        }
	
	        JsonNode tradeInfo = retQuery.path("trade_info");
	
	        if (!"0000".equals(tradeInfo.path("status_code").asText())) {
	
	            bean.setCode(tradeInfo.path("status_code").asText());
	            bean.setMessage(
	                    tradeInfo.path("status_message").asText());
	
	            return bean;
	        }
	
	        // 4. 更新 DB
	        SimpleDateFormat sdf =
	                new SimpleDateFormat("yyyyMMddHHmmss");
	
	        Date pxTradeTime =
	                sdf.parse(ret.path("px_trade_time").asText());
	
	        pxPayPosService.updTranLogRefund(
	                orderId,
	                newOrderId,
	                refundTranId,
	                pxTradeTime,
	                invoiceNo);
	
	        // 成功
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

}