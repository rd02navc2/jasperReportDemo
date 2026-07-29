package com.beyond.surrounding.pos.controller;

import com.beyond.surrounding.pos.bean.PosDetailBean;
import com.beyond.surrounding.pos.entity.TD;
import com.beyond.surrounding.pos.service.PosDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos/Detail")
@RequiredArgsConstructor
public class PosDetailController {

	 private final Environment env;
	 private final PosDetailService posDetailService;

	    @GetMapping(value = "getDetailByInvoiceNO/{center}/{invoiceDate}/{invoiceNo}",
		produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	    public List<PosDetailBean> getDetailByInvoiceNO(
	            @PathVariable String center,
	            @PathVariable String invoiceDate,
	            @PathVariable String invoiceNo) throws JSONException {
	    	
	    	// ResponseBean bean = new ResponseBean();

	        try {
	            log.info("getDetailByInvoiceNO : center -> {}, invoiceDate -> {}, invoiceNo -> {}",
	                    center, invoiceDate, invoiceNo);

	            List<TD> list = posDetailService.getTDByInvoiceNO(env, center, invoiceDate, invoiceNo);
	            List<PosDetailBean> l = posDetailService.combine(list);
				
	            return l;

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
    
    @GetMapping(value = "/getDetailByCreditCardNO/{center}/{invoiceDateS}/{invoiceDateE}/{preCardNo}/{endCardNo}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<PosDetailBean> getDetailByCreditCardNO(
            @PathVariable String center,
            @PathVariable String invoiceDateS,
            @PathVariable String invoiceDateE,
            @PathVariable String preCardNo,
            @PathVariable String endCardNo) throws JSONException {

        try {
            log.info("查詢信用卡明細: center={}, dates={}-{}, card={}-{}",
                    center, invoiceDateS, invoiceDateE, preCardNo, endCardNo);

            List<TD> data = posDetailService.getTDByCreditCardNO(env, center, invoiceDateS, invoiceDateE, preCardNo, endCardNo);
            List<PosDetailBean> list = posDetailService.combine(data);

            return list;

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
    
    @GetMapping(value = "/getDetailByCardNO/{center}/{invoiceDateS}/{invoiceDateE}/{cardNo}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<PosDetailBean> getDetailByCardNO(
            @PathVariable String center,
            @PathVariable String invoiceDateS,
            @PathVariable String invoiceDateE,
            @PathVariable String cardNo) throws JSONException {
        
    	try {
            log.info("getDetailByCardNO : center -> {}, dateS -> {}, dateE -> {}, cardNo -> {}", 
                      center, invoiceDateS, invoiceDateE, cardNo);

            // 呼叫 Service 執行業務邏輯
            List<TD> entityList = posDetailService.getTDByCardNO(center, invoiceDateS, invoiceDateE, cardNo);
            List<PosDetailBean> resultList = posDetailService.combine(entityList);

            return resultList;
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
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		