package com.beyoung.surrounding.pos2.controller;

import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.pos2.bean.POS2Bean;
import com.beyoung.surrounding.pos2.service.InvoicePos2Service;
import com.beyoung.surrounding.pos2.service.TranXService;
import com.beyoung.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController("pos2TranXController")
@RequestMapping("/Surrounding/rest/pos2/Transaction")
@RequiredArgsConstructor
public class TranXController {

	private final InvoicePos2Service invoicePos2Service;
	private final TranXService tranXService;
	
	@GetMapping(value = "/validate/{invoiceNo}/{randomNo}",
			produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
						MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public ResponseBean validate(
            @PathVariable String invoiceNo, 
            @PathVariable String randomNo) {
        try {
            log.info("POS2 validate : invoiceNo -> {}, randomNo -> {}", invoiceNo, randomNo);
            ResponseBean bean = invoicePos2Service.validate(invoiceNo, randomNo);
            // surroundingAccessLogDao.save(request.getRemoteAddr(), "app", request.getPathInfo());
            
            return bean;
            
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
	
	@PostMapping(
            value = "/upload", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8" 
            } 
    )
	public ResponseBean upload(
            @RequestBody List<POS2Bean> requestBody, // 修正：加上 @RequestBody
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authString) { // 修正：@HeaderParam 改為 @RequestHeader
        
        try {
            // 1. 身分驗證
            if (authString == null || !isUserAuthenticated(authString)) {
                throw new IllegalArgumentException("User not authenticated");
            }
            
            // 2. 記錄 Log (對照舊系統 getTC_PSA_FILE() 與小駝峰轉換)
            if (requestBody != null && !requestBody.isEmpty() && requestBody.get(0).getTcPsaFile() != null) {
                var firstPsa = requestBody.get(0).getTcPsaFile();
                log.info("{} / {} ： 接收到 {} 筆優惠券/交易資料", 
                        firstPsa.getTcPsaPlant(), 
                        firstPsa.getTcPsa01(), 
                        requestBody.size());
            }

            // 3. 執行業務邏輯存檔/處理
            tranXService.save(requestBody);

            // 4. 回傳成功狀態
            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);            
            return bean;

        } catch (Exception e) {
            log.error("處理 getCouponStatus2 發生錯誤: {}", e.getMessage(), e);

            // 取得最底層的錯誤訊息
            String errorMsg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            
            // 構建錯誤的 JSON 結構
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", errorMsg);
            
            // 拋出 Spring Boot 的 417 異常 (Expectation Failed)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    private boolean isUserAuthenticated(String authString) {
        return authString != null && !authString.isBlank();
    }
	
}
            
            
            
            
            
            
            
            
            
            
            
            