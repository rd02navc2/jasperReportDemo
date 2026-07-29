package com.beyond.surrounding.ec.controller;

import com.beyond.surrounding.ec.entity.TC_LRJ_FILE;
import com.beyond.surrounding.ec.service.RedeemService;
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
@RequestMapping("/Surrounding/rest/ec/Redeem") // 依實際需求調整路徑
@RequiredArgsConstructor
public class RedeemController {
	
	private final RedeemService redeemService;

    @GetMapping(value = "/getRule", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public TC_LRJ_FILE getRule() throws JSONException {
        try {
            return redeemService.getRule();
            
        } catch (Exception e) {
            log.error("getRule Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
    
}