package com.beyond.surrounding.pos2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 贈品系統 Feign 客戶端
 * 網址動態讀取環境變數 GIFT_WS_URL
 */
@FeignClient(name = "giftPos2ServiceClient", url = "${GIFT_WS_URL}")
public interface GiftPos2ServiceFeignClient {

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    String callGiftApi(@RequestBody String jsonPayload);
}