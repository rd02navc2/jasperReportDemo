package com.beyond.surrounding.exchange.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyond.surrounding.app.entity.LPQ_FILE;
import com.beyond.surrounding.exchange.service.ExchangeService;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/app/Exchange")
@RequiredArgsConstructor
public class ExchangeController {

	@Autowired
	private ExchangeService exchangeService; // 建議透過 Service 層呼叫 DAO


	@GetMapping(value = "/getExchangeSetting/{cardType}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPQ_FILE getExchangeSetting(
    		 @PathVariable String cardType,
             @RequestParam String plant) {
        
        try {
        	LPQ_FILE bean = exchangeService.getExchangeSetting(cardType, plant);
            
            if (bean != null) {
                return bean;
            } else {
                // 如果你想在查無資料時拋出 417，可以改寫為 throw new IllegalArgumentException("查無相關設定資料");
                return null; 
            }
            
        } catch (Exception e) {
            log.error("查詢點數兌換設定失敗: {}", e.getMessage(), e); //  修正 Log 錯誤文字
            
            // 對齊專案一致的 HTTP 417 例外包裝規格
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }	
    
}