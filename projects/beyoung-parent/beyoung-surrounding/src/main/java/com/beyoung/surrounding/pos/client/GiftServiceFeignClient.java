package com.beyoung.surrounding.pos.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.beyoung.surrounding.pos.dto.GiftResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "giftPosServiceClient", url = "${GIFT_WS_URL}")
public interface GiftServiceFeignClient {

    // 宣告一個 POST 請求方法，直接接收 JsonNode 格式的請求體，並回傳 JsonNode
    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    JsonNode fetchCoupons(@RequestBody JsonNode requestBody);
    
    @PostMapping(value = "/gift/recovery", consumes = MediaType.APPLICATION_JSON_VALUE)
    GiftResponseDTO recoveryInvoice(@RequestBody Map<String, Object> request);
}