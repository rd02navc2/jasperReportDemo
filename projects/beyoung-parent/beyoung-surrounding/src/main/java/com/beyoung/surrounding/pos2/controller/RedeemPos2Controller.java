package com.beyoung.surrounding.pos2.controller;

import com.beyoung.surrounding.pos2.entity.TC_LRJ_FILE;
import com.beyoung.surrounding.pos2.service.RedeemPos2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Slf4j
@RestController("RedeemPos2Controller")
@RequestMapping("/Surrounding/rest/pos2/Redeem")
@RequiredArgsConstructor
public class RedeemPos2Controller {

	private final RedeemPos2Service redeemPos2Service;

	
	@PostMapping(
            value = "/getRule", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8" 
            } 
    )
	public TC_LRJ_FILE getRule(@RequestBody String requestBody) {
        log.info("POS2 getRule 原始字串: " + requestBody);
        
        try {
            // 1. 解析原始 JSON 字串 (使用 Gson 靜態方法避免過時語法)
            JsonObject jo = JsonParser.parseString(requestBody).getAsJsonObject();
            
            // 2. 安全取出欄位（防禦性寫法：若欄位不存在或為 null，給予空字串避免 NPE）
            String center = (jo.has("center") && !jo.get("center").isJsonNull()) 
                             ? jo.get("center").getAsString() : "";
                             
            String cardType = (jo.has("cardType") && !jo.get("cardType").isJsonNull()) 
                               ? jo.get("cardType").getAsString() : "";

            // 3. 呼叫 Service 層處理業務邏輯，並直接回傳結果物件
            return redeemPos2Service.getRule(center, cardType);
		
        } catch (Exception e) {
            log.error("商品查詢時發生錯誤: {}", e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "商品查詢時發生錯誤");
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
	}

	
	
}		
			