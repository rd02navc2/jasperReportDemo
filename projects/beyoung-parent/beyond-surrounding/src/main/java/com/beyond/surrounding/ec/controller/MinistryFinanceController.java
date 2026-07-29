package com.beyond.surrounding.ec.controller;

import com.beyond.surrounding.ec.dto.VerifyLPRequest;
import com.beyond.surrounding.ec.service.MinistryFinanceService;
import com.beyond.surrounding.ts.bean.TSResponseBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/ec/MF")
@RequiredArgsConstructor
public class MinistryFinanceController {

    private final MinistryFinanceService ministryFinanceService;

    @PostMapping(value = "/verifyLP", 
            consumes = MediaType.APPLICATION_JSON_VALUE, // 1. 改為接收 JSON
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public TSResponseBean verifyLP(@RequestBody VerifyLPRequest request) { // 2. 使用 @RequestBody 接收 DTO
	   try {
	       // 直接將 DTO 的屬性拆解傳給 Service（或是直接把整個 DTO 傳過去）
	       return ministryFinanceService.verifyLPProcess(
	               request.getSMid(),
	               request.getRetCode(),
	               request.getTxType(),
	               request.getOrderNo(),
	               request.getRetMsg(),
	               request.getAuthIdResp(),
	               request.getCarrierId2()
	       );
	       
	   } catch (Exception e) {
	       log.error("verifyLP Controller 層捕捉異常: ", e);
	       
	       JSONObject errorJson = new JSONObject();
	       errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
	       errorJson.put("message", e.getMessage());
	       
	       throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
	   }
	}
    
}