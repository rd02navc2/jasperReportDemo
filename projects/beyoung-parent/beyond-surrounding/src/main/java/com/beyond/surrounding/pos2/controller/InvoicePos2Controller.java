package com.beyond.surrounding.pos2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.util.ErrCodeConst;

@Slf4j
@RestController("InvoicePos2Controller")
@RequestMapping("/Surrounding/rest/pos2/Invoice")
@RequiredArgsConstructor
public class InvoicePos2Controller {

	@GetMapping(value = "/doRecoveryInvoice/{invoiceNO}/{cashierID}",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
					     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public ResponseBean doRecoveryInvoice( @PathVariable String invoiceNO, @PathVariable String cashierID) {
        try {
        	//2023-10-21 店長、朱經理取消
            log.info("POS2 doRecoveryInvoice(New) : invoiceNO -> {}", invoiceNO);

            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            //dc-
            // surroundingAccessLogService.save(request.getRemoteAddr(), "pos", request.getPathInfo());
            
            return bean;
        } catch (Exception e) {
            log.error("查詢優惠券狀態失敗: {}", e.getMessage(), e);
            
            // 對齊專案一致的 HTTP 417 例外包裝規格
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }

	}
	
}
        
        
        
        
	