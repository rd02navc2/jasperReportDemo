package com.beyond.surrounding.pss.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 遠端停車系統 Feign 用戶端
 * 移除舊版 ClientBuilder，全面落實小駝峰命名
 */
@FeignClient(name = "ParkingServiceFeignClient", url = "${parking.url}")
public interface ParkingServiceFeignClient {

    @GetMapping(value = "/checkCarNO", produces = MediaType.APPLICATION_JSON_VALUE)
    String checkCarNo(
        @RequestHeader("Authorization") String authorization,
        @RequestParam("sCarNO") String carNo
    );
}