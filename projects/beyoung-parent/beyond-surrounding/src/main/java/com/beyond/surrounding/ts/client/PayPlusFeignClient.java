package com.beyond.surrounding.ts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payPlusClient", url = "${api-config.one-on-air.api-url}")
public interface PayPlusFeignClient {

    // 遠端對接端點：會自動組合成 https://api.example.com/GetPaymentBarcode
    @PostMapping(value = "/GetPaymentBarcode", consumes = "application/json;charset=UTF-8")
    String getPaymentBarcode(@RequestBody String jsonString);
    
    // 取得信用卡綁定狀態的遠端端點
    @PostMapping(value = "/GetCreditCardStatus", consumes = "application/json;charset=UTF-8")
    String getCreditCardStatus(@RequestBody String jsonString);
    
    @PostMapping(value = "/DeleteCreditCardAuth", consumes = "application/json;charset=UTF-8")
    String deleteCreditCardAuth(@RequestBody String requestBodyJson);
    
    @PostMapping(value = "/GetCreditCardList", consumes = "application/json;charset=UTF-8")
    String getCreditCardList(@RequestBody String requestBodyJson);
    
    @PostMapping(value = "/GetCardPage", consumes = "application/json;charset=UTF-8")
    String getCardPage(@RequestBody String requestBodyJson);
    
    
    
}