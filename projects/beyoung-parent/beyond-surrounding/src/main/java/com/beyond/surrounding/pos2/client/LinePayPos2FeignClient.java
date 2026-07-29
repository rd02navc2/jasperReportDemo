package com.beyond.surrounding.pos2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "linePayPos2Client", url = "${linepay.payment-target}")
public interface LinePayPos2FeignClient {

    @PostMapping(value = "/v3/payments/request")
    String payment(
        @RequestHeader("X-LINE-ChannelId") String channelId,
        @RequestHeader("X-LINE-Authorization") String auth,
        @RequestHeader("X-LINE-Authorization-Nonce") String nonce,
        @RequestBody String jsonRequest
    );

    @GetMapping(value = "/v3/payments/{transactionId}")
    String paymentDetail(
        @RequestHeader("X-LINE-ChannelId") String channelId,
        @RequestHeader("X-LINE-Authorization") String auth,
        @RequestHeader("X-LINE-Authorization-Nonce") String nonce,
        @PathVariable String transactionId
    );
    
    @PostMapping(value = "/v3/payments/{orderId}/refund")
    String refund(
        @RequestHeader("X-LINE-ChannelId") String channelId,
        @RequestHeader("X-LINE-Authorization") String auth,
        @RequestHeader("X-LINE-Authorization-Nonce") String nonce,
        @PathVariable String orderId,
        @RequestBody String jsonRequest
    );
}