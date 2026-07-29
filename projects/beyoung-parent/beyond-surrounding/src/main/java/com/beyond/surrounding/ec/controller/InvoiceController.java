package com.beyond.surrounding.ec.controller;

import com.beyond.surrounding.ec.bean.ECRequestBody;
import com.beyond.surrounding.bean.InvResponseBean;
// import com.beyond.surrounding.ec.service.InvoiceService;
import com.beyond.surrounding.util.ERPWebService;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController("ECInvoiceController")
@RequestMapping("/Surrounding/rest/ec") // 依實際需求調整路徑
@RequiredArgsConstructor
public class InvoiceController {

	private final ERPWebService erpWebService;
    // private final InvoiceService invoiceService;
    private final Environment env;
    
    /**
     * 取得發票號碼 API
     * @throws JSONException 
     */
    @PostMapping(value = "/getInvoiceNO", 
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public InvResponseBean getInvoiceNo(@RequestBody ECRequestBody requestBody) throws JSONException {
    	
    	try {
            // 1. 變數全面全駝峰化，消滅底線與 s/i 前綴
            log.info("EC：getInvoiceNo : center -> {}, posId -> {}, month -> {}", 
                    requestBody.getCenter(), requestBody.getPosId(), requestBody.getMonth());
            
            InvResponseBean bean = new InvResponseBean();
            
            // 2. 呼叫實例化（注入後）的 erpWebService (傳入全駝峰變數)
            JSONObject joResult = null;
            try {
                // 🛠️ 修正：改用小寫注入變數 erpWebService
                joResult = erpWebService.getInvoiceNo(
                        env.getProperty("ERP_WS_URL"), 
                        requestBody.getCenter(), 
                        requestBody.getPosId(), 
                        requestBody.getMonth()
                );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            log.info("Response：{}", joResult.toString());
            
            // 3. 檢查 ERP 回傳代碼
            if (!"0".equals(joResult.getString("code"))) {
                bean.setCode(joResult.getString("code"));
                bean.setMessage(joResult.getString("message"));
                log.error("EC：getInvoiceNo 失敗：{}:{}", joResult.getString("code"), joResult.getString("message"));
                return bean;
            }
            
            // 4. 封裝發票欄位資料
            bean.setOom02(joResult.getString("oom02"));
            bean.setOom07a(joResult.getString("oom07a"));
            bean.setOom071a(joResult.getString("oom071a"));
            bean.setOom081a(joResult.getString("oom081a"));
            bean.setOom07b(joResult.getString("oom07b"));
            bean.setOom071b(joResult.getString("oom071b"));
            bean.setOom081b(joResult.getString("oom081b"));
            
            // 5. 設定完成狀態 (對齊全駝峰常數)
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            
            return bean;
			
        } catch (Exception e) {
            log.error("usePoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
    
    
    
}