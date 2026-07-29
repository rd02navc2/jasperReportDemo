package com.beyond.surrounding.bonus.controller;

import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.util.ERPWebService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.beyond.surrounding.bonus.bean.BonusRequestBody;
import com.beyond.surrounding.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * 點數相關 API 控制器
 * 將原本的 JAX-RS @Path 轉換為 Spring MVC @RequestMapping 
 */
@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/bonus/Point")
@RequiredArgsConstructor 
public class BonusController {

    private final InvoiceService invoiceService;
    private final ERPWebService erpWebService;
    private final Environment env;
    // private JSONObject json = new JSONObject();
    private final ObjectMapper mapper = new ObjectMapper();
     
    @PostMapping(
            value = "/usePoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public ResponseBean usePoint(@RequestBody BonusRequestBody requestBody) throws JSONException {
    	try {
            // 進入點 Log 紀錄 (AOP 以外的標準業務日誌)
            log.info("Bonus：usePoint : center -> {}, counterID -> {}, cardNO -> {}, point -> {}, invoiceB -> {}, invoiceE -> {}",
                    requestBody.getCenter(), 
                    requestBody.getCounterId(), 
                    requestBody.getCardNo(), 
                    requestBody.getPoint(), 
                    requestBody.getInvoiceB(), 
                    requestBody.getInvoiceE());
            
            ResponseBean bean = new ResponseBean();
            JSONObject _joResult = erpWebService.useMemberPoint(
            		env.getProperty("ERP_WS_URL"), 
            		requestBody.getCenter(), 
            		requestBody.getCounterId(), 
            		requestBody.getCardNo(), 
            		requestBody.getPoint(), 
            		requestBody.getInvoiceB(), 
            		requestBody.getInvoiceE(), "GC", 
            		GetDateTime.getTimeMilli(""));
            
			log.info("Bonus：usePoint Response："+_joResult.toString());
			
			
            		
            // 將 Service 的結果封裝回前端所需的 DTO 格式
            return bean;
            
    	} catch (Exception e) {
            log.error("usePoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }

	@PostMapping(
            value = "/addPoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
	public ResponseBean addPoint(@RequestBody BonusRequestBody requestBody) {
        try {
            log.info("Bonus：addPoint : sCenter -> " + requestBody.getCenter() 
                    + ", counterID -> " + requestBody.getCounterId() 
                    + ", cardNO -> " + requestBody.getCardNo() 
                    + ", point -> " + requestBody.getPoint());
            
            ResponseBean _bean = new ResponseBean();
            
            invoiceService.addPoint(requestBody.getCenter(), requestBody.getCounterId(), requestBody.getCardNo(), requestBody.getPoint());
            _bean.setCode(ErrCodeConst.finished);
            _bean.setMessage(ErrCodeConst.finished_message);
            
            return _bean;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            
            ObjectNode jsonNode = mapper.createObjectNode();
            jsonNode.put("code", HttpStatus.EXPECTATION_FAILED.value());
            jsonNode.put("message", e.getMessage());
            
            // 在 Spring Boot 中，拋出特定 HTTP 狀態碼與內容的標準做法之一
            throw new ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, 
                    jsonNode.toString()
            );
        }
    }

	
	
    /**
     * 測試介面
     */
    @GetMapping("/test")
    public String test() {
        return "Spring Boot 3 & Java 21 重構成功！"; 
    }
}