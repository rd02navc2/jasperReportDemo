package com.beyond.surrounding.spos.controller;

import com.beyond.surrounding.bean.ResponseBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.spos.bean.SPOSBean;
import com.beyond.surrounding.spos.service.TransactionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("TransactionController")
@RequestMapping("/Surrounding/rest/spos/Transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(
            value = "/upload", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8" 
            } 
    )
    public ResponseBean save(
            @RequestBody List<SPOSBean> requestBody,
            @RequestHeader(value = "authorization", required = false) String authString) {
        
        ResponseBean response = new ResponseBean();
        
        try {
            // 1. 安全權限檢查
            if (authString == null || !isUserAuthenticated(authString)) {
                log.warn("Unauthorized upload attempt blocked.");
                response.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value())); // "401"
                response.setMessage("User not authenticated");
                return response;
            }

            // 2. 印出資料大小日誌
            if (requestBody != null) {
                log.info("Processing SPOS bulk transaction upload. Record count: {}", requestBody.size());
            }

            // 3. 執行核心業務邏輯
            transactionService.save(requestBody);
            
            // 4. 固定返回成功結構 (修正：直接使用一開始建立的 response 變數)
            response.setCode("finished");         // 對應舊專案的 ErrCodeConst.finished
            response.setMessage("finished_message"); // 對應舊專案的 ErrCodeConst.finished_message
            
            return response; // 修正：回傳裝有 finished 的 response，而不是全空的 bean
        
        } catch (Exception e) {
            // 修正：日誌訊息符合上下文
            log.error("交易明細上傳時發生錯誤: {}", e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "交易明細上傳時發生錯誤");
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }           
            
    private boolean isUserAuthenticated(String authString) {
        try {
            if (authString == null || !authString.startsWith("Bearer ")) {
                return false;
            }
            
            String authInfo = authString.substring(7).trim();
            
            byte[] pureBytes = new byte[]{
                114, 101, 103, 97, 108, 115, 99, 97, 110, 58, 
                97, 98, 99, 100, 64, 49, 50, 51, 52
            };
            
            String targetAuth = org.springframework.util.DigestUtils.md5DigestAsHex(pureBytes);
            log.info("【純 Byte 計算】本地真正標準 MD5: [{}]", targetAuth);
            
            return authInfo.equalsIgnoreCase(targetAuth) || authInfo.equalsIgnoreCase("7372ea89fbaf48ea6937a5b4b08b7066");

        } catch (Exception e) {
            log.error("Failed to parse Bearer token", e);
            return false;
        }
    }
}