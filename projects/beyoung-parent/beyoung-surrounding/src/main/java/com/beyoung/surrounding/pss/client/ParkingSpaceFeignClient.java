package com.beyoung.surrounding.pss.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

// url 直接設定為目標 API 的基礎路徑，並掛上我們自訂的 SSL 信任配置
@FeignClient(
    name = "parkingSpaceClient", 
    url = "https://10.200.0.3", 
    configuration = FeignIgnoreSslConfig.class
)
public interface ParkingSpaceFeignClient {

    @GetMapping(value = "/space/api/get_remain_space_info", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getRemainSpaceInfo(@RequestHeader("Authorization") String authorization);
}