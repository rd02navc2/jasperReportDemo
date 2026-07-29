package com.beyond.surrounding.pos2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.net.URI;

// 宣告為 FeignClient，名稱可自訂
@FeignClient(name = "subscriptFeignClient", url = "http://placeholder")
public interface SubscriptFeignClient {

	@PostMapping(value = "/search", consumes = "application/json")
    String searchRemoteSystem(
            URI baseUri, 
            @RequestBody String jsonBody
    );
}