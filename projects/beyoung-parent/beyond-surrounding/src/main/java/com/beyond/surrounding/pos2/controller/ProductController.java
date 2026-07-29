package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.pos2.bean.ProductBean;
import com.beyond.surrounding.pos2.entity.IMA_FILE;
import com.beyond.surrounding.pos2.service.ProductPos2Service;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("pos2ProductController")
@RequestMapping("/Surrounding/rest/pos2/Product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductPos2Service productPos2Service;

	@GetMapping(value = "/getProductByPNO/{pNO}",
			produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
						MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public ProductBean getProductByPNO(@PathVariable String pNO) {
		log.info("開始呼叫 getProductByPNO 查詢商品，參數 pNO: {}", pNO);
		
		try {
			ProductBean bean = new ProductBean();
			bean.setCode(ErrCodeConst.finished_message);
			bean.setMessage(ErrCodeConst.finished_message);		
			
			// 1. 呼叫 Service 取得商品 Entity (注意新舊版類別命名的轉變，此處以新版 ImaFile 示範)
			IMA_FILE entity = productPos2Service.getProductByPNO(pNO);
			
			// 2. 判斷是否查無資料
			if (entity == null || entity.getIMA01() == null) {
				log.warn("查無此商品代碼，pNO: {}", pNO);
				bean.setCode(ErrCodeConst.not_found);
				bean.setMessage(ErrCodeConst.not_found);
				return bean;
			}

			// 3. 欄位數據映射至 ProductBean
			bean.setP_no(entity.getIMA01());
			bean.setP_name(entity.getIMA02());		
			bean.setCounter_no(entity.getTA_IMA01());
			bean.setType_no(entity.getIMA131());
			
			// 日期轉換處理 (若原欄位為 Date 型態，建議轉成 .toString() 或格式化字串)
			bean.setAccess_date(entity.getIMADATE() != null ? entity.getIMADATE().toString() : null);
			bean.setIs_tax(entity.getIMA15());
			
			// 金額與價格轉換 (Double 型態防空判定)
			bean.setPrice1(entity.getIMA127() != null ? Double.valueOf(entity.getIMA127().toString()) : 0.0);
			bean.setPrice2(entity.getIMA128() != null ? Double.valueOf(entity.getIMA128().toString()) : 0.0);
			
			log.info("商品查詢成功，pNo: {}, 商品名稱: {}", bean.getP_no(), bean.getP_name());
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

	@GetMapping(value = "/getProductByDate",
			produces = { 
				MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
				MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" 
			})
	public List<IMA_FILE> getProductByDate(
			@RequestParam String fromDate, 
			@RequestParam String toDate) {
		
		log.info("收到 getProductByDate 請求, 條件: {} 至 {}", fromDate, toDate);
		
		try {
			// 呼叫 Service 取得合併後的結果
			return productPos2Service.getProductByDateCombined(fromDate, toDate);
			
		} catch (Exception e) {
			log.error("執行商品異動查詢時發生錯誤: " + e.getMessage(), e);
			
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "商品查詢執行商品異動查詢時發生錯誤錯誤");
			
			// 拋出符合 Spring Boot 規範的 417 異常
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, e.getMessage());
		}
	}
	
	
}