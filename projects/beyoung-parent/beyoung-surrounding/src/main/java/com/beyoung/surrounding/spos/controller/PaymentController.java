package com.beyoung.surrounding.spos.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyoung.surrounding.spos.entity.RYD_FILE;
import com.beyoung.surrounding.spos.repository.RYDFILERepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("sPosPaymentController")
@RequestMapping("/Surrounding/rest/spos/Payment")
@RequiredArgsConstructor
public class PaymentController {

	private final RYDFILERepository rydFileRepository;
	
	@GetMapping(value = "/getPaymentType",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public List<RYD_FILE> getPaymentType() {
        try {
            log.info("開始獲取 RYD_FILE 付款方式列表...");
            
            // 直接由 JPA 從 MySQL 資料表撈取完整資料
            List<RYD_FILE> paymentList = rydFileRepository.findAll();
            
            log.info("成功獲取付款方式，共 {} 筆資料。", paymentList.size());
            return paymentList;
            
        } catch (Exception e) {
            log.error("支付方式查詢時發生錯誤: {}", e.getMessage(), e);
            
            // 建立標準的錯誤 Response 結構
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "支付方式查詢時發生錯誤: " + e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }
	
}