package com.beyoung.surrounding.pos2.controller;

import com.beyoung.surrounding.pos2.service.ActionPos2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyoung.surrounding.bean.ActionResponseBean;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos2/Action")
@RequiredArgsConstructor
public class ActionPos2Controller {

	private final ActionPos2Service actionPos2Service;

	@GetMapping(value = "/checkBarcode",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ActionResponseBean checkBarcode(
            @RequestParam String center,
            @RequestParam String counterId,
            @RequestParam String posId,
            @RequestParam String barcode) {
        
        log.info("POS2 Controller checkBarcode 收到請求 : center -> {}, counterId -> {}", center, counterId);
        
        try {
            // 呼叫更新駝峰命名後的 Service 
            return actionPos2Service.checkBarcode(center, counterId, posId, barcode);
            
        } catch (Exception e) {
			log.error("查詢全體櫃位時發生錯誤: {}", e.getMessage(), e);
			
			// 3. 丟出符合新專案規範的 417 錯誤 JSON
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
			errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", "查詢櫃位清單時發生錯誤: " + e.getMessage());
			
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
					errorJson.toString()
			);
		}
	}
	
	@GetMapping(value = "/loginAD",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ActionResponseBean loginAd(
            @RequestParam String center,
            @RequestParam String userId,
            @RequestParam String password) {
        
        log.info("POS2 Controller loginAD 收到請求 : center -> {}, userId -> {}", center, userId);
        
        try {
            // 呼叫 Service 層處理 AD 登入驗證
            return actionPos2Service.loginAd(center, userId, password);
            
        } catch (Exception e) {
			log.error("AD登入驗證時發生錯誤: {}", e.getMessage(), e);
			
			// 3. 丟出符合新專案規範的 417 錯誤 JSON
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
			errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", "AD登入驗證時發生錯誤: " + e.getMessage());
			
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
					errorJson.toString()
			);
		}
	}
	
}