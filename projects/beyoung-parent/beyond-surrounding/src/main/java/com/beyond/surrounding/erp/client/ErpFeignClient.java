package com.beyond.surrounding.erp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.net.URI;

@FeignClient(contextId = "erpInvoiceServiceClient", name = "erpServiceClient", url = "${ERP_WS_URL}")
public interface ErpFeignClient {

    @PostMapping(produces = "text/xml; charset=UTF-8", consumes = "text/xml; charset=UTF-8")
    String sendSoapRequest(URI baseUrl, @RequestBody String soapEnvelope, @RequestHeader("soapaction") String soapAction);
}