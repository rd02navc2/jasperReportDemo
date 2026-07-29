package com.beyond.surrounding.erp.controller;

import com.beyond.surrounding.erp.bean.InvoiceBean;
import com.beyond.surrounding.erp.bean.RequestInvoiceBody;
import com.beyond.surrounding.util.ERPWebService;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController("ErpInvoiceController")
@RequestMapping("/Surrounding/rest/erp/Invoice") // 依實際需求調整路徑
public class ErpInvoiceController {

	private final ERPWebService erpWebService;
	private final Environment env; // 1. 宣告 Environment 物件

    // 2. 透過建構子注入 ERPWebService 與 Environment
    public ErpInvoiceController(ERPWebService erpWebService, Environment env) {
        this.erpWebService = erpWebService;
        this.env = env;
    }
    
    /**
     * 取得發票號碼 API
     * @throws JSONException 
     */
    @PostMapping(value = "/getInvoiceNO", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public InvoiceBean getInvoiceNO(@RequestBody RequestInvoiceBody requestBody) {
        try {
            log.info("ERP getInvoiceNO : sCenter -> {}, sPOSID -> {}, sMonth -> {}", 
                     requestBody.getsCenter(), requestBody.getsPOSID(), requestBody.getsMonth());

            InvoiceBean bean = new InvoiceBean();
            
            // 3. 完美還原舊版寫法，從 env 取得 "ERP_WS_URL"
            String erpWsUrl = env.getProperty("ERP_WS_URL");
            
            // 呼叫商業邏輯
            JSONObject joResult = erpWebService.getInvoiceNo(erpWsUrl, requestBody.getsCenter(), requestBody.getsPOSID(), requestBody.getsMonth());
            
            log.info("ERP getInvoiceNO({}) Response：{}", requestBody.getsPOSID(), joResult.toString());

            if (!"0".equals(joResult.getString("code"))) {
                bean.setCode(joResult.getString("code"));
                bean.setMessage(joResult.getString("message"));
                log.error("ERP：getInvoiceNO：{}:{}", joResult.getString("code"), joResult.getString("message"));
                return bean;
            }
            
            bean.setOom02(joResult.getString("oom02"));
            bean.setOom07a(joResult.getString("oom07a"));
            bean.setOom071a(joResult.getString("oom071a"));
            bean.setOom081a(joResult.getString("oom081a"));
            bean.setOom07b(joResult.getString("oom07b"));
            bean.setOom071b(joResult.getString("oom071b"));
            bean.setOom081b(joResult.getString("oom081b"));
            
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);               
            
            return bean;

        } catch (Exception e) {
            log.error("處理發票號碼取得失敗", e);
            
            JSONObject errorJson = new JSONObject();
            try {
                errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
                errorJson.put("message", e.getMessage());
            } catch (JSONException ex) {
                log.error("建構錯誤 JSON 失敗", ex);
            }
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }

    
    
    
    
    
}