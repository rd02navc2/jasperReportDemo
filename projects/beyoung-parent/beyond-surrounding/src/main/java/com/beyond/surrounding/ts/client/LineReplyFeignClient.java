package com.beyond.surrounding.ts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "lineReplyClient", url = "${api-config.line.reply-url}")
public interface LineReplyFeignClient {

    @PostMapping(consumes = "application/json;charset=UTF-8")
    String replyToLine(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody String jsonBody
    );
}