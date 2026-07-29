package com.beyond.surrounding.ts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// 調整為對應您 YAML 檔最上層的屬性名稱 TS_API_URL
@FeignClient(name = "tspgApiClient", url = "${TS_API_URL}")
public interface TspgFeignClient {

    @PostMapping("/auth.ashx")
    String auth(@RequestBody String jsonPayload);
    
    @PostMapping("/other.ashx")
    String other(@RequestBody String jsonPayload);
    
}