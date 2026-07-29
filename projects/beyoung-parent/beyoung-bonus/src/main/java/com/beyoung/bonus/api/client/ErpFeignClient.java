package com.beyoung.bonus.api.client;

import com.beyoung.bonus.config.ErpFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import java.net.URI;

/**
 * TIPTOP ERP Web Service (SOAP) 的 Feign 宣告式客戶端
 * 配置類別 ErpFeignConfig 會自動加上 SOAP 必要的 Content-Type 與 soapaction Headers
 */
@FeignClient(
	    name = "erpFeignClient", 
	    url = "http://placeholder", // 加上隨便一個 url 字串，強制關閉負載平衡器
	    configuration = ErpFeignConfig.class
	)
public interface ErpFeignClient {

    /**
     * 發送 SOAP XML 請求至指定的 ERP Endpoint
     * * @param baseUri      動態傳入的 ERP 服務網址 (例如：URI.create(sUrl))
     * @param soapEnvelope 拼接好的 SOAP XML 字串 Body
     * @return ERP 回傳的 XML 封包字串
     */
    @PostMapping
    String sendSoapRequest(URI baseUri, String soapEnvelope);
}