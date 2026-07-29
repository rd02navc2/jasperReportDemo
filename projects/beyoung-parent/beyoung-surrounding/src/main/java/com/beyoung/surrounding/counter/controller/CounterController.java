package com.beyoung.surrounding.counter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyoung.surrounding.counter.dto.CounterDTO;
import com.beyoung.surrounding.counter.service.CounterService;
import com.beyoung.surrounding.app.entity.LNT_FILE;
import java.util.List;
import com.beyoung.surrounding.bonus.bean.CounterRequestBody;

/**
 * 發票與會員點數 API 控制器
 * 已全面升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/app/Counter")
@RequiredArgsConstructor
public class CounterController {

	private final CounterService counterService;

	@PostMapping(
            value = "/getCounterByID", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
	public List<LNT_FILE> getCounterByID(@RequestBody CounterRequestBody requestBody) {
	    try {
	        // 假設 Request DTO 中有 sCounterID 欄位
	    	List<LNT_FILE> l = counterService.getCounterByID(requestBody);
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
	
    @GetMapping(value = "/getCounterByInvoice/{invoiceNo}/{randomNo}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public LNT_FILE getCounterByInvoice(
	        @PathVariable String invoiceNo, 
	        @PathVariable String randomNo) {
	    try {
	    	LNT_FILE bean = counterService.getCounterByInvoice(invoiceNo, randomNo);
	        return bean;
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

	@GetMapping(value = "/getCounterList",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public List<LNT_FILE> getCounterList() {
	    try {
	    	List<LNT_FILE> l = counterService.getCounterList();
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
		
	@GetMapping(value = "/getDeptList",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public List<LNT_FILE> getDeptList() {
	    try {
	        List<LNT_FILE> l = counterService.getDeptList();
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

    @GetMapping(value = "/getAllCounter",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LNT_FILE> getAllCounter() {
        try {
        	List<LNT_FILE> l = counterService.getAllCounter();
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
	
    /**
     * 新增排除專櫃
     */
    @PostMapping(
            value = "/excludeCounter/add", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public CounterDTO.Response<Void> addExcludeCounter(@RequestBody CounterDTO.Request request) {
        
        // 1. 檢查 request 物件或關鍵欄位是否為 null (防禦性檢核)
        if (request == null || request.getCounterId() == null || request.getCounterId().trim().isEmpty()) {
            log.warn("新增排除專櫃失敗: 接收到的專櫃代碼為空！");
            // 根據您的系統架構，可以拋出客製化異常，或是回傳帶有錯誤訊息的 Response
            return CounterDTO.Response.error("400", "專櫃代碼 (counterId) 不能為空");
        }

        log.info("新增排除專櫃: counterId -> {}, counterName -> {}", request.getCounterId(), request.getCounterName());

        // 2. 呼叫 Service 處理
        counterService.addExcludeCounter(request);

        return CounterDTO.Response.success(null);
    }
    
    /**
     * 移除排除專櫃
     */
    @PostMapping(
            value = "/excludeCounter/remove", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public CounterDTO.Response<Void> removeExcludeCounter(@RequestBody CounterDTO.Request request) {
        
        // 防禦性檢核：確保傳入的專櫃代碼不是空值
        if (request == null || request.getCounterId() == null || request.getCounterId().trim().isEmpty()) {
            log.warn("移除排除專櫃失敗: 接收到的專櫃代碼為空！");
            return CounterDTO.Response.error("400", "專櫃代碼 (counterId) 不能為空");
        }

        log.info("移除排除專櫃: sCounterID -> {}", request.getCounterId());
        
        counterService.removeExcludeCounter(request.getCounterId()); 
        return CounterDTO.Response.success(null); 
    }

    /**
     * 查詢排除清單
     */
    @GetMapping(value = "/excludeCounter/list",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public CounterDTO.Response<List<CounterDTO.ExcludeCounterResponse>> getExcludeCounterList() {
    	log.info("查詢排除專櫃清單");
        return CounterDTO.Response.success(
        		counterService.getExcludeCounterList()
        );
    }


    
    
    
    
}