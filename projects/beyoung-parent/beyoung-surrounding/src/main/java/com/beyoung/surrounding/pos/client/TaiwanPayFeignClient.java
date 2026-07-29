package com.beyoung.surrounding.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "taiwanPayClient", url = "${taiwanpay.payment-target}")
public interface TaiwanPayFeignClient {

    @PostMapping(value = "/PayOff", consumes = "application/json")
    String payment(@RequestBody String jsonBody);

    @PostMapping(value = "/QueryOneOff", consumes = "application/json")
    String query(@RequestBody String jsonBody);

    @PostMapping(value = "/RefundOff", consumes = "application/json")
    String refund(@RequestBody String jsonBody);
}