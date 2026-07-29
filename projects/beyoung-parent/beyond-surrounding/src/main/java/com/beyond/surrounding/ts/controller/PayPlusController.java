package com.beyond.surrounding.ts.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.ts.bean.PayPlusRequestBean;
import com.beyond.surrounding.ts.bean.PayPlusResponseBean;
import com.beyond.surrounding.ts.bean.Request3DBody;
import com.beyond.surrounding.ts.bean.ResultData;
import com.beyond.surrounding.ts.entity.TS_PAYPLUS_LOG;
import com.beyond.surrounding.ts.service.PayPlusService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/ts/PP")
@RequiredArgsConstructor
public class PayPlusController {

	private final PayPlusService payPlusService; 
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private ResponseStatusException createLegacyException(Exception e) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
        errorJson.put("message", e.getMessage());
        return new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
    }
	
	@GetMapping(value = "/connectTest",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean connectTest() {
        try {
            log.info("Connect Test");
            
            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            return bean;
		} catch (Exception e) {
			log.error("Connect Test 異常: ", e);
		    throw createLegacyException(e);
		}	
	}
	
	@PostMapping(value = "/getPaymentBarcode", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResultData getPaymentBarcode(@RequestBody PayPlusRequestBean requestBody) {
        try {
            log.info("getPaymentBarcode API 進入: CardToken -> {}, MemberId -> {}", 
                     requestBody.getCard_token(), requestBody.getMember_id());

            // 呼叫 Service 執行核心邏輯，直接拿到 ResultData 物件
            ResultData result = payPlusService.processPaymentBarcode(requestBody);
            
            // 成功時直接返回物件，Spring Boot 預設為 HTTP 200 OK
            return result;
        } catch (Exception e) {
            log.error("getPaymentBarcode 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }

	@PostMapping(value = "/getCreditCardStatus", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResultData getCreditCardStatus(@RequestBody PayPlusRequestBean requestBody) {
        try {
            log.info("getCreditCardStatus API 進入: OrderNo -> {}, MemberId -> {}", 
                     requestBody.getOrder_no(), requestBody.getMember_id());

            // 呼叫 Service 執行核心邏輯，直接拿到 ResultData 物件
            ResultData result = payPlusService.getCreditCardStatus(requestBody);
            
            // 成功時直接返回物件，Spring Boot 預設會以 HTTP 200 OK 回傳
            return result;
        } catch (Exception e) {
            log.error("getCreditCardStatus 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
    
	@PostMapping(value = "/deleteCreditCardAuth", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public PayPlusResponseBean deleteCreditCardAuth(@RequestBody PayPlusRequestBean requestBody) {
        try {
            // 直接交由 Service 處理完畢並回傳
            return payPlusService.deleteCreditCardAuth(requestBody);
        } catch (Exception e) {
            log.error("deleteCreditCardAuth 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }	
		
	}
	
	@PostMapping(value = "/getCreditCardList", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public PayPlusResponseBean getCreditCardList(@RequestBody PayPlusRequestBean requestBody) {
        try {
            return payPlusService.getCreditCardList(requestBody);
	
        } catch (Exception e) {
            log.error("getCreditCardList 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }	           		
	}
	
	@PostMapping(value = "/getCardPage", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public PayPlusResponseBean getCardPage(@RequestBody PayPlusRequestBean requestBody) {
        try {
            return payPlusService.getCardPage(requestBody);
	
        } catch (Exception e) {
            log.error("getCardPage 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }	           		
	}
	
	@PostMapping(value = "/get3DResult", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseEntity<String> get3DResult(@RequestBody String requestBody) {
		try {
			// 1. 呼叫 Service 處理 3D 驗證結果更新，並取得加上 CheckSum 簽章後的回傳 JSON
			String responseJson = payPlusService.save3DResult(requestBody);
			
			// 2. 回傳 200 OK 與處理好的 JSON 結果
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(responseJson);
		} catch (Exception e) {
            log.error("get3DResult 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }	           		
	}
	
	@PostMapping(value = "/get3DPage", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public TS_PAYPLUS_LOG get3DPage(@RequestBody Request3DBody requestBody) {
		try {
			log.info(String.format("get3DPage(Query DB) : member_id -> %s, barcode -> %s", 
					requestBody.getMemberId(), requestBody.getBarcode()));
			
			// 1. 呼叫 Service
			TS_PAYPLUS_LOG entity = payPlusService.get3DPage(requestBody.getMemberId(), requestBody.getBarcode());
			
			// 2. 判斷是否查無資料
			if (entity.getMember_id() == null) {
				entity.setCode(ErrCodeConst.not_found);
				entity.setMessage("會員交易資料尚未產生");

				return entity;
			}
			
			// 3. 查有資料，填入成功代碼
			entity.setOrder_no(entity.getOrder_no());
			entity.setHpp_url(entity.getHpp_url());
			entity.setCode(ErrCodeConst.finished);
			entity.setMessage(ErrCodeConst.finished_message);
			
			// 使用 Jackson 將 Entity 轉成 JSON String 記錄 log
			log.info("get3DPage(Query DB) Response : " + objectMapper.writeValueAsString(entity));
			return entity;	
		} catch (Exception e) {
            log.error("get3DPage 異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }	           		
	}
    
}