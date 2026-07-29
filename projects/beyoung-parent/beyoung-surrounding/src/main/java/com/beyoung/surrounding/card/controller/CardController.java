package com.beyoung.surrounding.card.controller;

import com.beyoung.surrounding.card.repository.CardRepository;
import com.beyoung.surrounding.app.entity.LPH_FILE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 發票與會員點數 API 控制器
 * 已全面升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/app/Card")
@RequiredArgsConstructor
public class CardController {

    // 依據您的需求，已更名為 Repository
    private final CardRepository cardRepository;
    
	@GetMapping(value = "/getCardType",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LPH_FILE> getCardType() {
        try {
            // 使用更名後的 repository 呼叫對應方法
            List<LPH_FILE> l = cardRepository.getCardType();
            
            return l;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            
            // 1. 建立 Jackson 的 ObjectNode 來代替舊專案的 JSONObject json
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode jsonNode = mapper.createObjectNode();
            
            // 2. 對應原本的 EXPECTATION_FAILED (HTTP 417) 狀態碼與錯誤訊息
            jsonNode.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            jsonNode.put("message", e.getMessage());
            
            // 3. 拋出 Spring Boot 的狀態異常，並帶入 JSON 字串
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    jsonNode.toString()
            );
        }
    } 
}