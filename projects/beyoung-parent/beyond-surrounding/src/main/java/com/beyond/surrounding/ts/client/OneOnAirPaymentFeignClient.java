package com.beyond.surrounding.ts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.net.URI;

@FeignClient(name = "OneOnAirPaymentFeignClient", url = "http://placeholder")
public interface OneOnAirPaymentFeignClient {

    // 透過傳入 URI 物件，可以完美動態決定要打哪一種支付通道 (paymentType) 的完整路徑
    @PostMapping(consumes = "application/json;charset=UTF-8")
    String getPaymentUrl(URI baseUri, @RequestBody String jsonPayload);
}