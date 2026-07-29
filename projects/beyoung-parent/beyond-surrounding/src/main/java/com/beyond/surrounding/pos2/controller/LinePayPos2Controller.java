package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.pos2.service.LinePayPos2Service;
import com.beyond.surrounding.pos2.service.PurchasePos2Service;
import com.beyond.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.beyond.surrounding.bean.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos2/LinePay")
@RequiredArgsConstructor
public class LinePayPos2Controller {

	private final PurchasePos2Service purchasePos2Service; 
	private final LinePayPos2Service linePayPos2Service;
	
	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping(value = "/purchase",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean purchase(
	        @RequestParam String center,
	        @RequestParam String counterID,
	        @RequestParam String productName,
	        @RequestParam Integer amt,
	        @RequestParam String oneTimeKey,
	        @RequestParam String orderID) throws JSONException {
		
		ResponseBean bean = new ResponseBean();
	
	    try {
	        log.info("purchase: center={}, counterID={}, orderID={}", center, counterID, orderID);
	
	        // 1. 呼叫 LinePay 支付 API
	        String paymentResponse = purchasePos2Service.linePayment(
	                orderID,
	                center,
	                counterID,
	                "比漾商品",
	                amt,
	                oneTimeKey);

	        JsonNode ret = objectMapper.readTree(paymentResponse);

	        if (!"0000".equals(ret.path("returnCode").asText())) {

	            bean.setCode(ErrCodeConst.pos_linepay_payment);

	            bean.setMessage(
	                    ErrCodeConst.pos_linepay_payment_message
	                    + " : "
	                    + ret.path("returnMessage").asText());

	            log.error("orderID -> {}, {}",
	                    orderID,
	                    ret.path("returnMessage").asText());

	            return bean;
	        }
	
	     // 1. 取得 transactionId
	        String transactionId = ret.path("info").path("transactionId").asText();

	        log.info("Payment Detail : orderId -> {}, transactionId -> {}",
	                orderID, transactionId);

	        // 2. 查詢 payment detail
	        String detailResponse = purchasePos2Service.linePaymentDetail(transactionId);

	        JsonNode retDetail = objectMapper.readTree(detailResponse);

	        // 失敗處理
	        if (!"0000".equals(retDetail.path("returnCode").asText())) {

	            bean.setCode(ErrCodeConst.pos_linepay_payment_detail);
	            bean.setMessage(
	                    ErrCodeConst.pos_linepay_payment_detail_message
	                    + " : "
	                    + retDetail.path("returnMessage").asText());

	            log.error("orderID -> {}, {}",
	                    orderID,
	                    retDetail.path("returnMessage").asText());

	            return bean;
	        }

	        // 3. info array
	        JsonNode infoArray = retDetail.path("info");

	        if (!infoArray.isArray() || infoArray.size() == 0) {
	            bean.setCode("9999");
	            bean.setMessage("payment detail info empty");
	            return bean;
	        }

	        JsonNode info = infoArray.get(0);

	        // 4. parse date
	        SimpleDateFormat sdf =
	                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

	        // 5. write DB
	        linePayPos2Service.addTranLog(
	                orderID,
	                center,
	                counterID,
	                "比漾商品",
	                amt,
	                oneTimeKey,
	                transactionId,
	                info.path("transactionType").asText(),
	                sdf.parse(info.path("transactionDate").asText()),
	                info.path("currency").asText()
	        );

	        // 6. success
	        bean.setCode(ErrCodeConst.finished);
	        bean.setMessage(ErrCodeConst.finished_message);

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
    
	@GetMapping(value = "/refund/{center}/{invoiceNO}/{orderID}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean refund(
	        @PathVariable String center,
	        @PathVariable String invoiceNO,
	        @PathVariable String orderID) throws JSONException {
		
		ResponseBean bean = new ResponseBean();
	    
	    try {
	        log.info("開始執行 LinePay 退款流程: orderID={}, invoiceNO={}", orderID, invoiceNO);
	
	     // 1. 呼叫退款 API
	        String refundRes = purchasePos2Service.linePayRefund(orderID);

	        JsonNode ret = objectMapper.readTree(refundRes);

	        // 失敗
	        if (!"0000".equals(ret.path("returnCode").asText())) {

	            String errorMsg = ret.path("returnMessage").asText("退款失敗");

	            log.error("LinePay Refund API 失敗: {}", errorMsg);

	            bean.setCode(ErrCodeConst.pos_linepay_refund);
	            bean.setMessage(ErrCodeConst.pos_linepay_refund_message + " : " + errorMsg);

	            return bean;
	        }

	        // 2. refundTransactionId
	        String refundTransactionId =
	                ret.path("info").path("refundTransactionId").asText();

	        log.info("Refund Detail : orderId -> {}, refundTransactionId -> {}",
	                orderID, refundTransactionId);

	        // 3. 查詢 detail
	        String detailRes =
	        		purchasePos2Service.linePaymentDetail(refundTransactionId);

	        JsonNode retDetail =
	                objectMapper.readTree(detailRes);

	        // 失敗
	        if (!"0000".equals(retDetail.path("returnCode").asText())) {

	            bean.setCode(ErrCodeConst.pos_linepay_refund_detail);
	            bean.setMessage(
	                    ErrCodeConst.pos_linepay_refund_detail_message
	                    + " : "
	                    + retDetail.path("returnMessage").asText());

	            return bean;
	        }

	        // 4. info 是 array（關鍵修正）
	        JsonNode infoArray = retDetail.path("info");

	        if (!infoArray.isArray() || infoArray.size() == 0) {
	            bean.setCode("9999");
	            bean.setMessage("refund detail info empty");
	            return bean;
	        }

	        JsonNode info = infoArray.get(0);

	        // 5. GMT time parse
	        SimpleDateFormat sdf =
	                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

	        // 6. update DB
	        linePayPos2Service.updateRefundLog(
	                orderID,
	                refundTransactionId,
	                info.path("transactionType").asText(""),
	                sdf.parse(info.path("transactionDate").asText()),
	                invoiceNO
	        );

	        // 7. success
	        bean.setCode(ErrCodeConst.finished);
	        bean.setMessage(ErrCodeConst.finished_message);

	        log.info("退款流程成功完成: orderID={}", orderID);

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