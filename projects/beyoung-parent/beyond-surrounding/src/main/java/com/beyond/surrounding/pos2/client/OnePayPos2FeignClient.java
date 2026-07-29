package com.beyond.surrounding.pos2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

@FeignClient(name = "onePayPos2Client", url = "${onepay.payment-target}")
public interface OnePayPos2FeignClient {
    @GetMapping("/gwMerchantApiPay.ashx")
    String payment(@SpringQueryMap Map<String, String> queryMap);

    @GetMapping("/gwMerchantApiQuery.ashx")
    String query(@SpringQueryMap Map<String, String> queryMap);

    @GetMapping("/gwMerchantApiRefund.ashx")
    String refund(@SpringQueryMap Map<String, String> queryMap);
}