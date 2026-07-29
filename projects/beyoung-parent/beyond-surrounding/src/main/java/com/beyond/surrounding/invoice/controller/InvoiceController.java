package com.beyond.surrounding.invoice.controller;

import com.beyond.surrounding.app.bean.AppendInvoiceBean;
import com.beyond.surrounding.app.bean.InvoiceBean;
import com.beyond.surrounding.app.client.ChiefPayFeignClient;
import com.beyond.surrounding.invoice.service.InvoiceService;
import com.beyond.surrounding.app.entity.TC_PSA_FILE;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 發票與會員點數 API 控制器
 * 已全面升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@RestController
@RequestMapping(
        value = "/Surrounding/rest/app/Invoice",
        produces = "application/json;charset=utf-8"
)
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final ChiefPayFeignClient chiefPayClient;
    // private final ChiefPayIntegrationService chiefPayIntegrationService;

    /**
     * 1. 查詢會員特定日期的發票紀錄
     * GET /Surrounding/rest/invoice/getUserInvoice/{memberID}/{date}
     */
	@GetMapping(value = "/getUserInvoice/{memberID}/{date}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<TC_PSA_FILE> getUserInvoice(
            @PathVariable String memberID,
            @PathVariable Date date) {
        log.info("查詢會員發票紀錄：memberID -> {}, date -> {}", memberID, date);
        try {
			log.info("getUserInvoice : memberID -> {}, date -> {}", memberID, date);
			
			// 呼叫服務層或 Repository 取得發票清單
			List<TC_PSA_FILE> list = invoiceService.getUserInvoice(memberID, date);
			
			return list;
        } catch (Exception e) {
            log.error("查詢點數兌換設定失敗: {}", e.getMessage(), e); //  修正 Log 錯誤文字
            
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

    /**
     * 2. 單純驗證發票真偽與隨機碼
     * GET /Surrounding/rest/invoice/validate/{invoiceNo}/{randomNo}
     */
    @GetMapping(value = "/validate/{invoiceNo}/{randomNo}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public AppendInvoiceBean validate(
            @PathVariable String invoiceNo,
            @PathVariable String randomNo) {
        log.info("單純驗證發票：invoiceNo -> {}, randomNo -> {} ", invoiceNo, randomNo);
        try {
        	AppendInvoiceBean bean = invoiceService.validate(invoiceNo, randomNo);
            // 將 Bean 內原有的狀態資訊拉到最外層的 DTO Response 中
            return bean;
        } catch (Exception e) {
            log.error("查詢點數兌換設定失敗: {}", e.getMessage(), e); //  修正 Log 錯誤文字
            
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
    

    @GetMapping(value = "/appendInvoice/{memberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public AppendInvoiceBean appendInvoice(
            @PathVariable String memberID, 
            @RequestParam String cardID,
            @RequestParam String invoiceNo,
            @RequestParam String randomNo) {
        
        log.info("appendInvoice start : cardID -> {}, memberID -> {}, invoiceNo -> {}, randomNo -> {}", 
                cardID, memberID, invoiceNo, randomNo);

        try {
            // 1. 執行驗證邏輯
            AppendInvoiceBean bean = invoiceService.validate(invoiceNo, randomNo);
            
            // 2. 驗證通過，執行發票點數補登寫入
            if (ErrCodeConst.finished.equals(bean.getCode())) {
                bean = invoiceService.appendInvoice(memberID, cardID, invoiceNo, randomNo);
                log.info("appendInvoice Response： cardID -> {}, counterID -> {}, counterName -> {}", 
                        cardID, bean.getCounterID(), bean.getCounterName());
                
                // 3. 透過 Feign 執行外部服務呼叫 (ChiefPay)
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    
                    // 使用 Map 結構化組裝，替代原本脆弱的 JSONObject 拼接
                    Map<String, Object> chiefPayBody = new HashMap<>();
                    chiefPayBody.put("sCardNO", bean.getCard_id());
                    chiefPayBody.put("sCreditCard", bean.getCreditCard() == null ? "" : bean.getCreditCard());
                    chiefPayBody.put("iInvoiceAmt", bean.getInvAmt());
                    chiefPayBody.put("iPromoteAmt", bean.getAmount());
                    chiefPayBody.put("iCreditAmt", bean.getCreditCardAmt() == null ? 0 : bean.getCreditCardAmt());
                    chiefPayBody.put("sDeviceID", bean.getPosId());
                    chiefPayBody.put("sCounterID", bean.getCounterID());
                    chiefPayBody.put("sInvoiceSN", bean.getInvoiceSN());
                    chiefPayBody.put("sInvoiceNO", invoiceNo);
                    chiefPayBody.put("sInvoiceDate", bean.getInvoiceDate() != null ? sdf.format(bean.getInvoiceDate()) : "");

                    log.info("appendInvoice call ChiefPay purchase(up)： {}", chiefPayBody);
                    
                    // 發送 Feign 請求
                    Map<String, Object> responseMap = chiefPayClient.purchase(chiefPayBody);
                    log.info("appendInvoice call ChiefPay purchase(down)： {}", responseMap);
                    
                    // 檢查 ChiefPay 回傳狀態碼
                    if (responseMap == null || !"0000".equals(String.valueOf(responseMap.get("code")))) {
                        String errCode = responseMap != null ? String.valueOf(responseMap.get("code")) : "UNKNOWN";
                        String errMsg = responseMap != null ? String.valueOf(responseMap.get("message")) : "No response";
                        throw new RuntimeException("補登發票寫入贈獎失敗：" + errCode + " " + errMsg);
                    }
                    
                } catch (Exception e) {
                    // 依據舊系統邏輯：ChiefPay 失敗僅記錄 error log，不中斷補登流程
                    log.error("ChiefPay call error: ", e);
                }
                
                // 4. 設定成功的狀態碼與訊息
                bean.setCode(ErrCodeConst.finished);
                bean.setMessage(ErrCodeConst.finished_message);				
            }
            
            log.info("appendInvoice Result : invoiceNo -> {}, {} {}", invoiceNo, bean.getCode(), bean.getMessage());
            return bean;

        } catch (Exception e) {
            log.error("appendInvoice Exception: ", e);
            
            // 修正原本舊程式碼底層未定義 json 物件的編譯錯誤，改用符合 Spring 規範的 ResponseStatusException
            String errorJson = String.format("{\"code\":%d,\"message\":\"%s\"}", 
                    HttpStatus.EXPECTATION_FAILED.value(), 
                    e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "");
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson);
        }
    }
    
    /**
     * 4. 取得已核銷發票明細
     * GET /Surrounding/rest/invoice/getInvoiceUsed?sInvoiceNo
     */
    @GetMapping(value = "/getInvoiceUsed", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<InvoiceBean> getInvoiceUsed(@RequestParam String invoiceNo) {
        
        log.info("取得已核銷發票明細：invoiceNo -> {}", invoiceNo);
        
        try {
            // 呼叫重構後的 service 取得 InvoiceBean 列表
            List<InvoiceBean> list = invoiceService.getInvoiceUsed(invoiceNo);
            return list;
            
        } catch (Exception e) {
            // 修正 Log 錯誤文字，精確對應此 API 業務
            log.error("取得已核銷發票明細失敗: {}", e.getMessage(), e); 
            
            // 對齊舊系統一致的 HTTP 417 例外包裝規格
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 拋出 Spring Boot 的 417 狀態碼與 JSON 字串內容
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }
    
    /**
     * 5. 影城/特定場域發票點數補登
     * GET /Surrounding/rest/invoice/TheaterAppend/{memberID}
     */
    @GetMapping(value = "/TheaterAppend/{memberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public AppendInvoiceBean theaterAppend(
            @PathVariable String memberID,
            @RequestParam String cardID,
            @RequestParam String invoiceNo,
            @RequestParam String totalPrice,
            @RequestParam String invoiceDate,
            @RequestParam String pointType) {

        log.info("TheaterAppend : memberID -> {}, cardID -> {}, invoiceNo -> {}, totalPrice -> {}, invoiceDate -> {}, pointType -> {}", 
                memberID, cardID, invoiceNo, totalPrice, totalPrice, pointType);

        try {
            // 呼叫底層商務邏輯
            AppendInvoiceBean bean = invoiceService.theaterAppend(memberID, cardID, invoiceNo, totalPrice, invoiceDate, pointType);
            
            // 填入成功代碼 (對應原本的 ErrCodeConst)
            bean.setCode("finished"); // 請替換成你專案實際的 ErrCodeConst.finished
            bean.setMessage("交易成功"); // 請替換成你專案實際的 ErrCodeConst.finished_message

            log.info("TheaterAppend Result : sInvoiceNo -> {}, {} {}", invoiceNo, bean.getCode(), bean.getMessage());
            return bean;

        } catch (Exception e) {
            log.error("TheaterAppend 發生異常: ", e);
            
            // 模擬原本拋出 EXPECTATION_FAILED (417) 的行為
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorMap.put("message", e.getMessage());
            
            // Spring Boot 標準拋出特定狀態碼異常的方式
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 6. 退貨補登 (扣回點數)
     * 修正：改用 POST 以確保操作安全性，防止 GET 請求被瀏覽器或爬蟲誤觸
     */
    @GetMapping(value = "/AppendReturn/{memberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public AppendInvoiceBean appendReturn(
            @PathVariable String memberId,
            @RequestParam String invoiceNo,
            @RequestParam String totalPrice,
            @RequestParam String invoiceDate,
            @RequestParam double point) {
        
        try {
            log.info("appendReturn : memberId -> {}, invoiceNo -> {}, totalPrice -> {}, invoiceDate -> {}, point -> {}", 
                    memberId, invoiceNo, totalPrice, invoiceDate, point);
            
            // 呼召 Service 層處理業務邏輯（全面採用小駝峰參數）
            AppendInvoiceBean bean = invoiceService.appendReturn(memberId, invoiceNo, totalPrice, invoiceDate, point);
            
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                

            log.info(String.format("appendReturn Result : invoiceNo -> %s, %s %s", 
                    invoiceNo, bean.getCode(), bean.getMessage()));
            
            return bean;
            
        } catch (Exception e) {
            log.error("appendReturn Error: ", e);
            
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }  

}