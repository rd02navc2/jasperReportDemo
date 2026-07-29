package com.beyoung.surrounding.pos.controller;

import com.beyoung.surrounding.pos.service.InvoicePosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyoung.surrounding.bean.ResponseBean;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos/Invoice")
@RequiredArgsConstructor
public class InvoicePosController {

	// private final PurchaseService purchaseService; 
	private final InvoicePosService invoicePosService;
	
	@GetMapping(value = "/doRecoveryInvoice/{invoiceNO}/{cashierID}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	 public ResponseBean doRecoveryInvoice(
	            @PathVariable String invoiceNO,
	            @PathVariable String cashierID) {

	        try {
	        	log.info("doRecoveryInvoice(New) : invoiceNO -> "+invoiceNO);
	            return invoicePosService.doRecoveryInvoice(invoiceNO, cashierID);

	        } catch (Exception e) {
				log.error("進行發票還原作業失敗: {}", e.getMessage(), e);
				
				// 不要拋出任何 Exception，也不要自己轉 JSON 字串。
				// 直接回傳 ResponseBean 物件，Spring Boot 會自動根據前端的要求轉成 XML 或 JSON！
				ResponseBean errorBean = new ResponseBean();
				errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value())); // "417"
				
				// 處理乾淨的錯誤訊息
				String clearMessage = e.getMessage();
				if (clearMessage != null && clearMessage.contains("No static resource")) {
					clearMessage = "後端贈品服務連線失敗(404路由錯誤)";
				}
				errorBean.setMessage("進行發票還原作業失敗: " + clearMessage);
				
				return errorBean; 
			}
		}

}