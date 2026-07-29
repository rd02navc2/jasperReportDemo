package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.bean.ActionResponseBean;
import com.beyond.surrounding.pos2.service.SubscriptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/pos2/Subscript")
@RequiredArgsConstructor
public class SubscriptController {

	private final SubscriptService subscriptService;

	@GetMapping(value = "/search",
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ActionResponseBean search(
            @RequestParam String counterNo, 
            @RequestParam String center,
            @RequestParam String posId, 
            @RequestParam Integer quantity) {
        
        log.info("search Controller 收到請求 : counterNo -> {}, center -> {}, posId -> {}, quantity -> {}", 
                counterNo, center, posId, quantity);
        
        try {
            // 呼叫改用 Feign 重構後的 Service，並直接返回結果
            return subscriptService.search(counterNo, center, posId, quantity);
            
        } catch (Exception e) {
            log.error("庫存訂閱查詢時發生錯誤: {}", e.getMessage(), e);
            
            // 3. 丟出符合新專案規範的 417 錯誤 JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "庫存訂閱查詢時發生錯誤: " + e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }
	
}