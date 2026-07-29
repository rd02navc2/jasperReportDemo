package com.beyond.surrounding.spos.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.bean.ProductBean;
import com.beyond.surrounding.spos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("sPosProductController")
@RequestMapping("/Surrounding/rest/spos/Product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@GetMapping(
            value = "/getProductByPNO/{pNO}", // 修正：路徑參數變數化
            produces = { MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
	                	 MediaType.APPLICATION_XML_VALUE + ";charset=utf-8"})
    public ProductBean getProductByPNO(@PathVariable String pNO) {
        try {
            log.info("接收到商品條碼查詢請求，pNO: {}", pNO);
            
            // 直接呼叫封裝好商業邏輯的 Service
            return productService.getProductByPNO(pNO);
            
        } catch (Exception e) {
            log.error("商品查詢時發生例外錯誤: {}", e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "商品查詢時發生錯誤: " + e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }
	
}