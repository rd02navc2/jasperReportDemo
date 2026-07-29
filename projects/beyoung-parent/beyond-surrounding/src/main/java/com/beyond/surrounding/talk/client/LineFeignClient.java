package com.beyond.surrounding.talk.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Map;

@FeignClient(name = "lineMessagingClient", url = "https://api.line.me")
public interface LineFeignClient {

    @PostMapping(value = "/v2/bot/message/push", consumes = "application/json")
    String pushMessage(
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> requestBody
    );
    
    
}