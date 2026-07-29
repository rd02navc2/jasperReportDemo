package com.beyond.surrounding.pos.controller;

import com.beyond.surrounding.pos2.entity.PACE_TRANSACTION_LOG;
import com.beyond.surrounding.pos.repository.PacePayRepository;
import com.beyond.surrounding.pos.service.PacePayPosService;
import com.beyond.surrounding.pos2.bean.PaceResponseBean;
import com.beyond.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos/PacePay")
@RequiredArgsConstructor
public class PacePayPosController {

	// private final PurchaseService purchaseService;
	private final PacePayPosService pacePayPosService;
	private final PacePayRepository pacePayRepository;
   
	@GetMapping(value = "/purchase",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public PaceResponseBean purchase(
            @RequestParam String center,
            @RequestParam String counterId,
            @RequestParam String posId,
            @RequestParam String posDateTimeStr,
            @RequestParam String productName,
            @RequestParam Integer amount,
            @RequestParam String oneTimeKey,
            @RequestParam String orderId) {

        long startTime = System.currentTimeMillis();
        String transactionId = "";
        PaceResponseBean bean = new PaceResponseBean();

        try {
            // 1. 解密條碼支付碼
            String decryptedKey = new String(Hex.decodeHex(oneTimeKey.toCharArray()));
            log.info("purchase : center -> {}, counterId -> {}, posId -> {}, posDateTime -> {}, productName -> {}, amount -> {}, oneTimeKey -> {} ({}), orderId -> {}",
                    center, counterId, posId, posDateTimeStr, productName, amount, decryptedKey, oneTimeKey, orderId);

            // 2. 呼叫第三方支付發起交易
            PaceResponseBean response = pacePayPosService.pacepayPayment(orderId, amount);
            transactionId = response.getTransactionId();
            log.info("Payment Detail : orderId -> {}, transactionId -> {}", orderId, transactionId);
            String transactionDateStr = response.getCreationDate();

            // 3. 驗證條碼有效性
            PaceResponseBean verifyResponse = pacePayPosService.pacepayVerifyBarcode(transactionId, decryptedKey);
            if (!"true".equals(verifyResponse.getSuccess())) {
                bean.setCode(ErrCodeConst.pos_pace_barcode_fail);
                bean.setMessage(ErrCodeConst.pos_pace_barcode_fail_message);
                log.error("orderId -> {}, transactionId -> {}, errorCode -> {}, errorMessage -> {}",
                        orderId, transactionId, ErrCodeConst.pos_pace_barcode_fail, ErrCodeConst.pos_pace_barcode_fail_message);
                return bean;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            
            pacePayPosService.addTranLogPending(
                    orderId, 
                    center, 
                    counterId, 
                    posId, 
                    sdf.parse(posDateTimeStr),
                    productName, 
                    amount, 
                    decryptedKey, 
                    transactionId, 
                    timeZoneTransform(transactionDateStr)
            );
           
            // 5. 輪詢查詢交易結果 (上限 53 秒，每 5 秒一次)
            while (true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= startTime + (53 * 1000)) {
                    break;
                }
                
                Thread.sleep(5000);
                
                String queryStatus = pacePayPosService.pacePayQuery(transactionId).getStatus();
                if ("approved".equals(queryStatus) || "processing".equals(queryStatus)) {
                    
                    // 交易成功，變更狀態
                    pacePayRepository.findById(orderId).ifPresent(logEntity -> {
                        logEntity.setTransactionType("APPROVED");
                        logEntity.setAccessDate(new Date());
                        pacePayRepository.save(logEntity);
                    });
                    
                    break;
                }
            }

            // 6. 最終狀態雙重校驗
            String finalStatus = pacePayPosService.pacePayQuery(transactionId).getStatus();
            if (!"approved".equals(finalStatus) && !"processing".equals(finalStatus)) {
                bean.setCode(ErrCodeConst.pos_pace_fail);
                bean.setMessage(ErrCodeConst.pos_pace_fail_message);
                log.error("transactionId -> {}, {}", transactionId, ErrCodeConst.pos_pace_fail_message);
                return bean;
            }

            bean.setCode(ErrCodeConst.pos_pace_finished);
            bean.setMessage(ErrCodeConst.pos_pace_finished_message);
            return bean;

        } catch (Exception e) {
        	// 增加對 FeignException 的捕捉
            if (e instanceof feign.FeignException) {
                feign.FeignException feignEx = (feign.FeignException) e;
                bean.setCode(String.valueOf(feignEx.status()));
                
                // 嘗試撈取 Feign 回傳的 Body 錯誤訊息
                String errorResponseBody = feignEx.contentUTF8();
                bean.setMessage("Feign 呼叫異常: " + errorResponseBody);
                
                log.error("Feign 交易失敗 -> orderId: {}, Status: {}, Body: {}", 
                        orderId, feignEx.status(), errorResponseBody, feignEx);
                return bean;
                
            } else if (e instanceof HttpStatusCodeException) {
                HttpStatusCodeException httpEx = (HttpStatusCodeException) e;
                String errorResponse = httpEx.getResponseBodyAsString();
                JsonObject resultObject = JsonParser.parseString(errorResponse).getAsJsonObject();
                JsonObject errorObject = resultObject.getAsJsonObject("error");
                
                String errorCode = httpEx.getStatusCode().toString();
                String errorMessage = errorObject.get("message").getAsString();
                
                bean.setCode(errorCode);
                bean.setMessage(errorMessage);
                log.error("orderId -> {}, errorCode -> {}, errorMessage -> {}", orderId, errorCode, errorMessage);
                return bean;
            } else {
                log.error("內部系統異常：", e);
                try {
                    if (transactionId != null && !transactionId.isEmpty()) {
                        String queryStatus = pacePayPosService.pacePayQuery(transactionId).getStatus();
                        log.error("Checking purchase result due to exception. Status: {}", queryStatus);
                        if ("approved".equals(queryStatus) || "processing".equals(queryStatus)) {
                            log.warn("交易已扣款但發生程式異常，啟動安全防禦性自動沖正退款。TransactionID: {}", transactionId);
                            pacePayPosService.pacePayRefund(transactionId, amount);
                        }
                    }
                } catch (Exception ex) {
                    log.error("安全自動沖正機制執行失敗！需人工介入檢核：", ex);
                }

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode errJson = mapper.createObjectNode();
                errJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
                errJson.put("message", e.getMessage());

                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errJson.toString(), e);
            }
        }
    }

    private Date timeZoneTransform(String dateStr) throws Exception {
        // 保持你原本的時區轉 Date 邏輯
        return new Date(); 
    }

    @GetMapping(value = "/query/{center}/{orderId}", 
    produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public PaceResponseBean getTransaction(@PathVariable String center,
	                                       @PathVariable String orderId) {
	    PaceResponseBean bean = new PaceResponseBean();
	
	    try {
	        String transactionId = pacePayPosService.getTranLog(orderId).getTransactionId();
	        if (null == transactionId || "".equals(transactionId)) {
	            bean.setCode(ErrCodeConst.pos_pace_not_found);
	            bean.setMessage(ErrCodeConst.pos_pace_not_found_message);
	            log.error("errorCode ->" + ErrCodeConst.pos_pace_not_found + ", errorMessage ->"
	                    + ErrCodeConst.pos_pace_not_found_message);
	            return bean;
	        }
	        
	        log.info("query : center -> " + center + ", orderId -> " + orderId + ", transactionId -> " + transactionId);
	        PaceResponseBean queryResponse = pacePayPosService.pacePayQuery(transactionId);
	
	        bean.setCode(ErrCodeConst.finished);
	        bean.setMessage(ErrCodeConst.finished_message);
	        bean.setStatus(queryResponse.getStatus());
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        Date date = timeZoneTransform(queryResponse.getCreationDate());
	        bean.setCreationDate(sdf.format(date));
	        return bean;
	        
	    } catch (Exception e) {
	        if (e instanceof HttpStatusCodeException) {
	            HttpStatusCodeException httpExc = (HttpStatusCodeException) e;
	            String errorResponse = httpExc.getResponseBodyAsString();
	            JsonObject resultObject = JsonParser.parseString(errorResponse).getAsJsonObject();
	            JsonObject errorObject = (JsonObject) resultObject.get("error");
	            String errorCode = httpExc.getStatusCode().toString();
	            String errorMessage = errorObject.get("message").getAsString();
	            
	            bean.setCode(errorCode);
	            bean.setMessage(errorMessage);
	            log.error("orderId -> " + orderId + ", errorCode ->" + errorCode + ", errorMessage ->" + errorMessage);
	            return bean;
	        } else {
	            // 1. 修正原本編譯錯誤的 Logger
	            log.error("System exception occurred while processing orderId: {}", orderId, e);
	            
	            // 2. 改用 Spring Boot 的 HttpStatus 與 ResponseStatusException
	            // 註：原本的 EXPECTATION_FAILED (417) 在 Spring 中對應的是 HttpStatus.EXPECTATION_FAILED
	            throw new org.springframework.web.server.ResponseStatusException(
	                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
	                    e.getMessage(), 
	                    e
	            		);
	        	}
	        }
	}
	
    @GetMapping(value = "/refund/{center}/{invoiceNo}/{orderId}/{posId}/{posDateTime}", 
    produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public PaceResponseBean refund(@PathVariable String center,
                               @PathVariable String invoiceNo,
                               @PathVariable String orderId,
                               @PathVariable String posId,
                               @PathVariable String posDateTime) {

    	PaceResponseBean bean = new PaceResponseBean();
	    try {
	        log.info("refund : center -> " + center + ", invoiceNo -> " + invoiceNo + ", orderId -> " + orderId
	                + ", posId -> " + posId + ", posDateTime -> " + posDateTime);
	
	        // 1. 取得交易日誌
	        PACE_TRANSACTION_LOG entity = pacePayPosService.getTranLog(orderId);
	        String transactionId = entity.getTransactionId();
	        if (null == transactionId || "".equals(transactionId)) {
	            bean.setCode(ErrCodeConst.pos_pace_not_found);
	            bean.setMessage(ErrCodeConst.pos_pace_not_found_message);
	            log.error("errorCode ->" + ErrCodeConst.pos_pace_not_found + ", errorMessage ->"
	                    + ErrCodeConst.pos_pace_not_found_message);
	            return bean;
	        }
	
	        // 2. 發起退款請求
	        PaceResponseBean refundResponse = pacePayPosService.pacePayRefund(transactionId, entity.getPosAmount());
	        String refundStatus = refundResponse.getStatus();
	        if (!"approved".equals(refundStatus) && !"completed".equals(refundStatus)) {
	            bean.setCode(ErrCodeConst.pos_pace_refund_fail);
	            bean.setMessage(refundStatus);
	            log.error("orderId -> " + orderId + ", errorCode ->" + ErrCodeConst.pos_pace_refund_fail
	                    + ", errorMessage ->" + ErrCodeConst.pos_pace_refund_fail_message + ", refundStatus ->"
	                    + refundStatus);
	            return bean;
	        }
	
	        String refundTranXId = refundResponse.getRefundId();
	        log.info("Refund Detail : orderId -> " + orderId + ", refundTranXId -> " + refundTranXId
	                + ", refundStatus -> " + refundStatus);
	
	        // 3. 查詢退款狀態確認
	        PaceResponseBean queryResponse = pacePayPosService.pacePayQuery(transactionId);
	        if (!"refunded".equals(queryResponse.getStatus())) {
	            bean.setCode(ErrCodeConst.pos_pace_refund_fail);
	            bean.setMessage(ErrCodeConst.pos_pace_refund_fail_message);
	            log.error("orderId -> " + orderId + ", errorCode ->" + ErrCodeConst.pos_pace_refund_fail
	                    + ", errorMessage ->" + ErrCodeConst.pos_pace_refund_fail_message + ", transactionStatus ->"
	                    + queryResponse.getStatus());
	            return bean;
	        }
	
	        // 4. 更新本地交易日誌狀態
	        pacePayPosService.updTranLogRefund(orderId, refundTranXId,
	                timeZoneTransform(refundResponse.getCreationDate()), invoiceNo);
	                
	        bean.setCode(ErrCodeConst.finished);
	        bean.setMessage(ErrCodeConst.finished_message);
	
	        return bean;
	        
	    } catch (Exception e) {
	        if (e instanceof HttpStatusCodeException) {
	            HttpStatusCodeException httpExc = (HttpStatusCodeException) e;
	            String errorResponse = httpExc.getResponseBodyAsString();
	            
	            // 修正為新版 Gson 靜態方法
	            JsonObject resultObject = JsonParser.parseString(errorResponse).getAsJsonObject();
	            JsonObject errorObject = (JsonObject) resultObject.get("error");
	            String errorCode = String.valueOf(httpExc.getStatusCode().value());
	            String errorMessage = errorObject.get("message").getAsString();
	            
	            bean.setCode(errorCode);
	            bean.setMessage(errorMessage);
	            log.error("orderId -> " + orderId + ", errorCode ->" + errorCode + ", errorMessage ->" + errorMessage);
	            return bean;
	        } else {
	            // 修正 Logger 錯誤與改用 Spring Boot 異常處理機制
	            log.error("System exception occurred during refund processing for orderId: {}", orderId, e);
	            
	            throw new org.springframework.web.server.ResponseStatusException(
	                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
	                    e.getMessage(), 
	                    e
	            );
	        }
	    }
	}
	
	
	
}