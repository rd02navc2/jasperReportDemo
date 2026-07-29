package com.beyond.surrounding.pos2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "taiwanPayPos2Client", url = "${taiwanpay.payment-target}")
public interface TaiwanPayPos2FeignClient {

    @PostMapping(value = "/PayOff", consumes = "application/json")
    String payment(@RequestBody String jsonBody);

    @PostMapping(value = "/QueryOneOff", consumes = "application/json")
    String query(@RequestBody String jsonBody);

    @PostMapping(value = "/RefundOff", consumes = "application/json")
    String refund(@RequestBody String jsonBody);
}