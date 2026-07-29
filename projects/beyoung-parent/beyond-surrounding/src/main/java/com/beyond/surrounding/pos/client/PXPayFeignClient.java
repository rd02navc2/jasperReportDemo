package com.beyond.surrounding.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "pxPayClient", url = "${pxpay.payment-target}")
public interface PXPayFeignClient {
    @PostMapping(value = "/Payment", consumes = "application/json")
    String payment(@RequestHeader Map<String, String> headers, @RequestBody String jsonBody);

    @GetMapping(value = "/OrderStatus/1/{orderId}/{reqTime}")
    String query(@RequestHeader Map<String, String> headers, 
                 @PathVariable String orderId, 
                 @PathVariable String reqTime);

    @PostMapping(value = "/Refund", consumes = "application/json")
    String refund(@RequestHeader Map<String, String> headers, @RequestBody String jsonBody);
}