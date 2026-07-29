package com.beyond.surrounding.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "erpPointServiceClient", name = "erpServiceClient", url = "${ERP_WS_URL}")
public interface ErpPointFeignClient {

    /**
     * 呼叫 ERP 扣點/加點 WebService
     *  注意：舊系統通常是用 Form 表單參數 (POST) 傳遞給舊 WebService，故此處以 @RequestParam 對照
     */
    @PostMapping(value = "/path/to/erp/processPoint4EC", produces = "application/json;charset=utf-8")
    String processPoint4EC(
        @RequestParam String center,
        @RequestParam String counterID,
        @RequestParam String cardNO,
        @RequestParam Double amt,
        @RequestParam Integer point,
        @RequestParam String ruleId
    );
    
    @PostMapping(
            value = "/Surrounding/rest", // 依據你前面 log 顯示的 chiefpay.api.url 根路徑調整後綴
            consumes = MediaType.TEXT_XML_VALUE,
            produces = MediaType.TEXT_XML_VALUE
        )
        String sendSoapRequest(@RequestBody String soapEnvelope);
    
}