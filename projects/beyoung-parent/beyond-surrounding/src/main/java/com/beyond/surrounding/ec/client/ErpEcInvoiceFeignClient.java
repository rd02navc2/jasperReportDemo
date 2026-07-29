package com.beyond.surrounding.ec.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// value 可以是任意名稱，url 則動態讀取環境變數 (對應原本的 env.getProperty("ERP_WS_URL"))
@FeignClient(contextId = "ErpEcInvoiceServiceClient", name = "erpServiceClient", url = "${ERP_WS_URL}")
public interface ErpEcInvoiceFeignClient {

    /**
     * 調用 TIPTOP ERP Web Service 取得發票號碼
     * 舊版中使用了 text/xml 且帶有自定義的 soapaction
     */
    @PostMapping(
        value = "", // URL 已在類別上方定義，此處為空或填特定 Path
        consumes = "text/xml; charset=UTF-8",
        produces = "text/xml; charset=UTF-8"
    )
    String getInvoiceNoSoap(@RequestBody String soapEnvelopeBody);

        
}