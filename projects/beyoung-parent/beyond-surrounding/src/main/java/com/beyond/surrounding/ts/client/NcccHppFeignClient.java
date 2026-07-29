package com.beyond.surrounding.ts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "ncccHppClient", url = "${NCCC_API_URL}")
public interface NcccHppFeignClient {

    /**
     * Feign 自動處理表單提交，並將回應解析為 Map 或專屬的 Response DTO
     */
    @PostMapping(
        value = "/merchant/HPPRequest", 
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    Map<String, String> postTransaction(@SpringQueryMap Map<String, ?> params);
    
    /**
     * 宣告式呼叫 NCCC 的取消授權機制
     */
    @PostMapping(
        value = "/merchant/HPPRequest", // 根據舊 HppApiClient，取消與授權路徑相同，皆由 apiClient.postCancel() 決定參數
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    Map<String, String> postCancel(@SpringQueryMap Map<String, ?> params);
    
    @PostMapping(
            value = "/merchant/HPPRequest", 
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
        )
        Map<String, String> postQuery(@SpringQueryMap Map<String, ?> params);
    
    @PostMapping(
            value = "/merchant/HPPRequest", // 根據原參數 env.getProperty("nccc_payment_url")
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        Map<String, String> uploadPaymentDat(
            @RequestPart("file") MultipartFile file,
            @RequestPart("mode") String mode,       // 對應原參數 "Internet"
            @RequestPart("mid") String merchantId  // 對應原參數 env.getProperty("nccc_mid")
        );
    
    @PostMapping(
            value = "/merchant/HPPRequest", // 依原 env.getProperty("nccc_payment_url")
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
        )
        String downloadPaymentResponse(
            @RequestParam String mode,     // "Internet"
            @RequestParam("mid") String merchantId // env.getProperty("nccc_mid")
        );
    
}