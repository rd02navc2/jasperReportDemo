package com.beyond.surrounding.pss.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.beyond.surrounding.pss.service.ParkingService;
import com.beyond.surrounding.pss.bean.ParkingInvoiceRequestBody;
import com.beyond.surrounding.pss.client.ParkingServiceFeignClient;
import com.beyond.surrounding.pss.entity.TcPsaFile;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * 停車發票補登 API 資源控制器 (JAX-RS + Jakarta + Jackson 版)
 */
@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pss/InvoiceParking")
@RequiredArgsConstructor
public class InvoiceParkingController {

    private final ParkingService parkingService; 
    private final ParkingServiceFeignClient parkingServiceClient;
    private final ObjectMapper objectMapper;
    
    
    /**
     * 停車發票補登核心 API (Controller 層)
     */
    @PostMapping(
            value = "/appendInvoice4Parking", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
	public ResponseBean appendInvoice4Parking(ParkingInvoiceRequestBody requestBody) {
        
        log.info("停車發票補登 : {}, {}, {}, {}, {}, {}, {}, {}", 
                requestBody.getInvoiceDate(), requestBody.getInvoiceNo(), requestBody.getCenter(), 
                requestBody.getChannel(), requestBody.getTranXType(), requestBody.getCounterId(), 
                requestBody.getCarNo(), requestBody.getPromoteAmt());
        
        try {
            // 1. 若通路為 Service，直接呼叫 Feign Client 進行外部車牌驗證
            if ("Service".equals(requestBody.getChannel())) {
                
                String jsonResponse = parkingServiceClient.checkCarNo(
                        "322b514d7a347849583731744a495072447a454e6f773d3d", 
                        requestBody.getCarNo()
                );
                
                log.info("Feign 遠端車牌驗證回傳結果: {}", jsonResponse);
                
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                if (rootNode.has("code") && !"0000".equals(rootNode.get("code").asText())) {
                    throw new Exception(rootNode.get("code").asText() + " " + rootNode.get("message").asText());
                }
            }
            
            ResponseBean responseBean = new ResponseBean();
            String channel = requestBody.getChannel();
            
            // 2. 核心業務邏輯分支 (全數轉換為小駝峰方法名)
            if ("POS".equals(channel)) {
                responseBean = parkingService.appendInvoice4Parking(
                        requestBody.getInvoiceDate(), requestBody.getInvoiceNo(), requestBody.getRandomNo(), 
                        requestBody.getInvoiceTime(), requestBody.getCenter(), requestBody.getChannel(), 
                        requestBody.getTranXType(), requestBody.getCounterId(), requestBody.getCardNo(), 
                        requestBody.getCarNo(), requestBody.getPromoteAmt());
                
            } else if ("Service".equals(channel)) {
            	TcPsaFile psaFile = parkingService.check4Parking(requestBody.getInvoiceNo(), "uncheck");
            	// 【合理修正 1】：校驗失敗時，將錯誤資訊轉移給 ResponseBean 格式再回傳
                if (!ErrCodeConst.finished.equals(psaFile.getCode())) {
                    responseBean.setCode(psaFile.getCode());
                    responseBean.setMessage(psaFile.getMessage());
                    responseBean.setYn("N");
                    return responseBean; // 永遠維持回傳 ResponseBean
                }

                responseBean = parkingService.appendInvoice4Parking(
                        GetDateTime.getTodayDateW("-"), requestBody.getInvoiceNo(), "uncheck", 
                        psaFile.getTcPsa05(), psaFile.getTcPsaplant(), requestBody.getChannel(), 
                        0, psaFile.getTcPsa01(), psaFile.getTcPsa13(), requestBody.getCarNo(), 
                        psaFile.getTcPsa40());
                
            } else if ("APS_UN".equals(channel)) {
                responseBean = parkingService.checklog4Parking(requestBody.getInvoiceNo(), requestBody.getRandomNo());
                if (!"finished".equals(responseBean.getCode())) {
                    return responseBean;
                }
                
                responseBean = parkingService.appendInvoice4Parking(
                        requestBody.getInvoiceDate(), requestBody.getInvoiceNo(), requestBody.getRandomNo(), 
                        null, requestBody.getCenter(), requestBody.getChannel(), requestBody.getTranXType(), 
                        null, null, requestBody.getCarNo(), requestBody.getPromoteAmt());
                
            } else {
                TcPsaFile psaFile = parkingService.check4Parking(requestBody.getInvoiceNo(), requestBody.getRandomNo());
             // 【合理修正 2】：同上，統一回傳規格，不隨意洩漏 Entity 結構
                if (!ErrCodeConst.finished.equals(psaFile.getCode())) {
                    responseBean.setCode(psaFile.getCode());
                    responseBean.setMessage(psaFile.getMessage());
                    responseBean.setYn("N");
                    return responseBean;
                }

                responseBean = parkingService.appendInvoice4Parking(
                        requestBody.getInvoiceDate(), requestBody.getInvoiceNo(), requestBody.getRandomNo(), 
                        psaFile.getTcPsa05(), psaFile.getTcPsaplant(), requestBody.getChannel(), 
                        requestBody.getTranXType(), psaFile.getTcPsa01(), psaFile.getTcPsa13(), 
                        requestBody.getCarNo(), psaFile.getTcPsa40());
            }
            
            // 備留原註解 log 行為參考：
            // surroundingAccessLogDao.save(request.getRemoteAddr(), "app", request.getRequestURI());
            
            return responseBean;
            
        } catch (Exception e) {
            log.error("執行 appendInvoice4Parking 發生異常", e);
            
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value()); // 417
            jsonError.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }
	
}

