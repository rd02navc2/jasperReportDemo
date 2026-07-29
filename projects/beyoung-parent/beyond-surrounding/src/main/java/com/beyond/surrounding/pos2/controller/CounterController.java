package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.pos2.entity.LntFile;
import com.beyond.surrounding.pos2.entity.RYC_FILE;
import com.beyond.surrounding.pos2.service.CounterPos2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("pos2CounterController")
@RequestMapping("/Surrounding/rest/pos2/Counter")
@RequiredArgsConstructor
public class CounterController {

	private final CounterPos2Service counterPos2Service;

	@GetMapping(value = "/getAllCounter",
			produces = {
				MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
				MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"
			})
	public List<LntFile> getAllCounter() {
		log.info("開始呼叫 getAllCounter 查詢全體櫃位資料");
		
		try {
			// 1. 呼叫櫃位 Service 取得全體櫃位 List (不需傳入任何 pNO 參數)
			List<LntFile> counterList = counterPos2Service.getAllCounter();
			
			// 2. 防呆判定，若為 null 則給予空集合避免前端報錯
			if (counterList == null) {
				counterList = new java.util.ArrayList<>();
			}
			
			log.info("櫃位查詢成功，總計筆數: {}", counterList.size());
			return counterList;

		} catch (Exception e) {
			log.error("查詢全體櫃位時發生錯誤: {}", e.getMessage(), e);
			
			// 3. 丟出符合新專案規範的 417 錯誤 JSON
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
			errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", "查詢櫃位清單時發生錯誤: " + e.getMessage());
			
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
					errorJson.toString()
			);
		}
	}

	@GetMapping(value = "/getPOSData",
			produces = { 
				MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
				MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" 
			})
	public List<RYC_FILE> getPOSData() {
		log.info("接收到外部請求：獲取 POS 異動設定檔清單 (getPOSData)");
		
		try {
			// 1. 呼叫 Service 取得剛剛翻新好複合主鍵的 RYC_FILE 清單
			List<RYC_FILE> posDataList = counterPos2Service.getPOSData();
			
			// 2. 防呆處理，避免前端拿到 null 報錯
			if (posDataList == null) {
				posDataList = new java.util.ArrayList<>();
			}
			
			log.info("POS 異動設定檔查詢成功，總計筆數: {}", posDataList.size());
			return posDataList;
			
		} catch (Exception e) {
			log.error("執行 POS 異動設定檔查詢時發生錯誤: {}", e.getMessage(), e);
			
			// 3. 封裝標準的 417 錯誤 JSON 訊息
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
			errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", "執行 POS 異動設定檔查詢時發生錯誤: " + e.getMessage());
			
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
					errorJson.toString()
			);
		}
	}
	
	
}