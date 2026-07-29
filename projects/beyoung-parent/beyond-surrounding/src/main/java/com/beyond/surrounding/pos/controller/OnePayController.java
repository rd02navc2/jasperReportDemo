package com.beyond.surrounding.pos.controller;

import com.beyond.surrounding.pos.validate.OnePayValidator;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.ParseUtil;
import com.beyond.surrounding.pos.entity.ONE_TRANSACTION_LOG;
import com.beyond.surrounding.pos.service.OnePayService;
import com.beyond.surrounding.pos.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.beyond.surrounding.bean.ResponseBean;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos/OnePay")
@RequiredArgsConstructor
public class OnePayController {

	private final PurchaseService purchaseService;
	private final OnePayService onePayService;
	private final OnePayValidator onePayValidator; // 建議將驗證邏輯拆分出來

	@GetMapping(value = "/purchase",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean purchase(
	        @RequestParam("center") String center,
	        @RequestParam("counterID") String counterId,
	        @RequestParam("posID") String posId,
	        @RequestParam("posDateTime") String posDateTime,
	        @RequestParam("productName") String productName,
	        @RequestParam("amt") Integer amount,
	        @RequestParam("oneTimeKey") String oneTimeKey,
	        @RequestParam("orderID") String orderId,
	        @RequestParam("moPayType") String moPayType) throws JSONException {
	
	    log.info("OnePay 交易請求: orderID={}, moPayType={}", orderId, moPayType);
	    ResponseBean bean = new ResponseBean();
	    
	    try {
	        // 1. 執行驗證與解碼
	        String decodedKey = onePayValidator.validateAndDecode(moPayType, oneTimeKey, orderId);
	
	        // 2. 呼叫 Service 執行支付 (這裡將 log 與 env 視為環境變數或自動注入)
	        String paymentXml = purchaseService.onepayPayment(orderId, posId, posDateTime, amount, decodedKey);
	        
	        // 3. XML 解析結果
	        String rCode = ParseUtil.getTagValue(paymentXml, "RtnCode", 0);
	        String rPosCode = ParseUtil.getTagValue(paymentXml, "RtnPOSActionCode", 0);
	        String rMsg = ParseUtil.getTagValue(paymentXml, "RtnMsg", 0);
	
	        if (!"000".equals(rCode) || !"0".equals(rPosCode)) {
	            bean.setCode(rCode);
	            bean.setMessage(rMsg);
	            log.error("交易失敗: orderID={}, 錯誤碼={}, 訊息={}", orderId, rCode, rMsg);
	            return bean;
	        }
	
	        // 4. 記錄日誌
	        String transactionId = ParseUtil.getTagValue(paymentXml, "ServiceTradeNo", 0);
	        log.info("Payment Detail : orderId={}, transactionId={}", orderId, transactionId);
	
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
	        onePayService.addTranLog(
	            orderId, center, counterId, posId, sdf1.parse(posDateTime), 
	            productName, amount, decodedKey, transactionId, 
	            sdf1.parse(ParseUtil.getTagValue(paymentXml, "ServiceTradeDate", 0) + ParseUtil.getTagValue(paymentXml, "ServiceTradeTime", 0)),
	            ParseUtil.getTagValue(paymentXml, "WalletProvider", 0)
	        );
	
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

	@GetMapping(value = "/refund/{center}/{invoiceNo}/{orderId}/{newOrderId}/{posId}/{posDateTime}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean refund(
	        @PathVariable String center,
	        @PathVariable String invoiceNo,
	        @PathVariable String orderId,
	        @PathVariable String newOrderId,
	        @PathVariable String posId,
	        @PathVariable String posDateTime) throws JSONException {
	    
		ResponseBean bean = new ResponseBean();
		
		try {
		    log.info("Refund request received: center={}, invoiceNo={}, orderId={}, newOrderId={}, posDateTime={}", 
		             center, invoiceNo, orderId, newOrderId, posDateTime);
		    
		    // 1. 取得交易紀錄 (使用正確的類別名稱 OneTransactionLog)
		    ONE_TRANSACTION_LOG entity = onePayService.getTranLog(orderId);
		    
		    if (entity == null) {
		        log.error("Transaction not found for orderId: {}", orderId);
		        bean.setCode(ErrCodeConst.not_found);
		        bean.setMessage("訂單不存在");
		        return bean; // <--- 確保這裡有回傳，程式會在此結束並返回錯誤訊息
		    }
		    
		    log.info("Found transaction, amount: {}", entity.getPosAmount());
		    
		    String _sRefund = purchaseService.onepayRefund(orderId, posId, posDateTime, entity.getPosAmount(), entity.getOneTimeKey(), entity.getTransactionId());
			String _sCode = ParseUtil.getTagValue(_sRefund, "RtnCode", 0);
			String _sPosCode = ParseUtil.getTagValue(_sRefund, "RtnPOSActionCode", 0);
			String _sMessage = ParseUtil.getTagValue(_sRefund, "RtnMsg", 0);
			if (!"000".equals(_sCode)||!"0".equals(_sPosCode)){
				bean.setCode(_sCode);
				bean.setMessage(_sMessage);	
				log.error("orderID -> "+ orderId+", "+_sCode+" "+_sMessage);
				return bean;
			}
			
			    
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		    
		    // 3. 更新交易紀錄
		    onePayService.updTranLogRefund(orderId, newOrderId, _sRefund, sdf1.parse(ParseUtil.getTagValue(_sRefund, "ServiceTradeDate", 0)+ParseUtil.getTagValue(_sRefund, "ServiceTradeTime", 0)), invoiceNo);

		    // 4. 設定回應
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
	
	
	@GetMapping(value = "/query",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ONE_TRANSACTION_LOG query(
            @RequestParam(value = "center",      required = false) String sCenter,
            @RequestParam(value = "posID",       required = false) String sPosID,
            @RequestParam(value = "posDateTime", required = false) String sPosDateTime,
            @RequestParam(value = "oneTimeKey",  required = false) String sOneTimeKey,
            @RequestParam(value = "orderID",     required = false) String sOrderID,
            @RequestParam(value = "tradeType",   required = false) String sTradeType) throws JSONException {

        try {
            log.info("query : center -> {}, orderID -> {}", sCenter, sOrderID);

            ONE_TRANSACTION_LOG _bean = new ONE_TRANSACTION_LOG();

            String _sQueryStatus = purchaseService.onepayQuery(
                    sOrderID, sPosID, sPosDateTime, sOneTimeKey, sTradeType);

            String _sCode    = ParseUtil.getTagValue(_sQueryStatus, "RtnPOSActionCode",    0);
            String _sMessage = ParseUtil.getTagValue(_sQueryStatus, "RtnPOSActionCodeMsg", 0);

            if (!"0".equals(_sCode)) {
                _bean.setCode(_sCode);
                _bean.setMessage(_sMessage);
                log.error("orderID -> {}, {} {}", sOrderID, _sCode, _sMessage);
                return _bean;
            }

            SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

            _bean.setCode(ParseUtil.getTagValue(_sQueryStatus, "RtnPOSActionCode",    0));
            _bean.setMessage(ParseUtil.getTagValue(_sQueryStatus, "RtnPOSActionCodeMsg", 0));
            _bean.setTransTime(sdf.format(sdf1.parse(
                    ParseUtil.getTagValue(_sQueryStatus, "ServiceTradeDate", 0) +
                    ParseUtil.getTagValue(_sQueryStatus, "ServiceTradeTime", 0))));

            return _bean;

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