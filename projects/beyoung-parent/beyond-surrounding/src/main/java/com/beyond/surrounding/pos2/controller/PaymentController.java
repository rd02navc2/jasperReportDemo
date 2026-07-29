package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.pos2.entity.RYD_FILE;
import com.beyond.surrounding.pos2.entity.TC_XMA_FILE;
import com.beyond.surrounding.pos2.service.PaymentPos2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/pos2/Payment") //  建議加上基礎路徑，更貼近舊系統網址
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentPos2Service paymentPos2Service;

    @GetMapping(value = "/getPaymentType", 
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            			MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public List<RYD_FILE> getPaymentType() {
        log.info("接收到外部請求：獲取付款種類設定清單 (getPaymentType)");
        
        try {
            // 1. 調用服務層取得付款種類
            List<RYD_FILE> list = paymentPos2Service.getPaymentType();
            
            if (list == null) {
                list = new ArrayList<>();
            }
            
            log.info("付款種類查詢成功，總計筆數: {}", list.size());
            return list;
            
        } catch (Exception e) {
            log.error("執行 getPaymentType 查詢時發生錯誤: {}", e.getMessage(), e);
            
            // 2. 封裝 417 錯誤 JSON 並拋出
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "查詢付款種類時發生錯誤: " + e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
    @GetMapping(value = "/getBinCode", 
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            			MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public List<TC_XMA_FILE> getBinCode() {
        log.info("接收到外部請求：獲取卡號 BinCode 設定清單 (getBinCode)");
        
        try {
            // 1. 呼叫服務層取得卡號設定清單
            List<TC_XMA_FILE> list = paymentPos2Service.getBinCode();
            
            if (list == null) {
                list = new java.util.ArrayList<>();
            }
            
            log.info("卡號 BinCode 清單查詢成功，總計筆數: {}", list.size());
            return list;
            
        } catch (Exception e) {
            log.error("執行 getBinCode 查詢時發生錯誤: {}", e.getMessage(), e);
            
            // 2. 封裝符合 Spring Boot 規範的 417 異常訊息
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "查詢卡號 BinCode 設定時發生錯誤: " + e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }
    
    
    
}