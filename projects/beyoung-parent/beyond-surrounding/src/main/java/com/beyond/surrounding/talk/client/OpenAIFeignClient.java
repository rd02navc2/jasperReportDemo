package com.beyond.surrounding.talk.client;

import com.beyond.surrounding.talk.config.FeignDisableSSLConfiguration;
import com.beyond.surrounding.talk.dto.OpenAIRequest;
import com.beyond.surrounding.talk.dto.OpenAIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.net.URI;

// 加上 url = "http://placeholder" 作為佔位符
// 在方法傳入 URI baseUrl 時，這個 placeholder 會被動態覆蓋，但它能欺騙 Spring 讓專案順利啟動！
@FeignClient(
    name = "openaiFeignClient", 
    url = "http://placeholder", 
    configuration = FeignDisableSSLConfiguration.class
)
public interface OpenAIFeignClient {

    @PostMapping(consumes = "application/json;charset=UTF-8")
    OpenAIResponse sendAIMessage(
        URI baseUrl, // 實際發送時，會以這個傳入的 URI 為準
        @RequestHeader("Authorization") String authorization, 
        @RequestBody OpenAIRequest request
    );
    
}