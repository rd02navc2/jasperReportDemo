package com.beyond.surrounding.ts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openaiClient", url = "${api-config.openai.url}")
public interface OpenaiFeignClient {

    @PostMapping(consumes = "application/json;charset=UTF-8")
    String askOpenai(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody String jsonBody
    );
}