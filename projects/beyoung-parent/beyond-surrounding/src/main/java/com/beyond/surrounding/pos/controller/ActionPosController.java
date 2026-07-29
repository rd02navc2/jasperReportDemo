package com.beyond.surrounding.pos.controller;

import com.beyond.surrounding.bean.ActionResponseBean;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos/Action")
@RequiredArgsConstructor
public class ActionPosController {
	
	@GetMapping(value = "/checkBarcode",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ActionResponseBean checkBarcode(
	        @RequestParam String center,
	        @RequestParam String counterID,
	        @RequestParam String posID,
	        @RequestParam String barcode) throws JSONException {
		
		ActionResponseBean bean = new ActionResponseBean();

	    try {
	        // 1. Hex 解碼 (確保有引入 commons-codec)
	        String decrypted = new String(org.apache.commons.codec.binary.Hex.decodeHex(barcode.toCharArray()));
	        log.info("checkBarcode: center={}, counterID={}, posID={}, barcode={}({})", 
	                  center, counterID, posID, decrypted, barcode);

	        if (decrypted == null || decrypted.isEmpty()) {
	            bean.setCode(ErrCodeConst.pos_action_check_barcode);
	            bean.setMessage(ErrCodeConst.pos_action_check_barcode_message);
	            return bean;
	        }

	        // 1. invoice carrier
	        if (decrypted.length() == 8 && decrypted.startsWith("/")) {
	            bean.setsActionType("invoice_carrier");
	        }

	        // 2. member card
	        else if (decrypted.length() == 11 &&
	                (decrypted.startsWith("7708")
	                || decrypted.startsWith("APP")
	                || decrypted.startsWith("TS")
	                || decrypted.startsWith("EC"))) {

	            bean.setsActionType("member_card");
	        }

	        // 3. LinePay (31~39)
	        else if (decrypted.length() == 18 &&
	                (decrypted.startsWith("31")
	                || decrypted.startsWith("32")
	                || decrypted.startsWith("33")
	                || decrypted.startsWith("34")
	                || decrypted.startsWith("35")
	                || decrypted.startsWith("36")
	                || decrypted.startsWith("37")
	                || decrypted.startsWith("38")
	                || decrypted.startsWith("39"))) {

	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("LinePay");
	        }

	        else if (decrypted.length() == 18 && decrypted.startsWith("PI")) {
	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("PiPay");
	        }

	        else if (decrypted.length() == 18 && decrypted.startsWith("P")) {
	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("PXPay");
	        }

	        else if ((decrypted.length() == 22 &&
	                 (decrypted.startsWith("95") || decrypted.startsWith("96")))
	              || (decrypted.length() == 144 &&
	                 decrypted.startsWith("YWhPCKAAAAFylQABwVwyNTQx"))) {

	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("TaiwanPay");
	        }

	        else if (decrypted.length() == 20 && decrypted.startsWith("99")) {
	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("EasyPay");
	        }

	        else if (decrypted.length() == 18 && decrypted.startsWith("22")) {
	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("JkosPay");
	        }

	        else if (decrypted.length() == 18 && decrypted.startsWith("TS")) {
	            bean.setsActionType("mobile_payment");
	            bean.setsMoPayType("TSPay");
	        }

	        else {
	            bean.setCode(ErrCodeConst.pos_action_check_barcode);
	            bean.setMessage(ErrCodeConst.pos_action_check_barcode_message);
	            return bean;
	        }

	        // success
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
}