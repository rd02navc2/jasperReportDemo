package com.beyoung.surrounding.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

// url 會從 application.properties 中的 APPServer 讀取
@FeignClient(name = "privilegeServiceClient", url = "${APPServer}")
public interface PrivilegeServiceFeignClient {

    @PostMapping(value = "/privilege/search", consumes = MediaType.APPLICATION_JSON_VALUE)
    String executeSearch(
            @RequestHeader("app-id") String appId,
            @RequestBody String body
    );
    
    @PostMapping(value = "/privilege/redeem", consumes = MediaType.APPLICATION_JSON_VALUE)
    String executeRedeem(
            @RequestHeader("app-id") String appId,
            @RequestBody String body
    );
    
    @PostMapping(value = "/privilege/transaction_check", consumes = MediaType.APPLICATION_JSON_VALUE)
    String executeTransactionCheck(
            @RequestHeader("app-id") String appId,
            @RequestBody String body
    );
    
    @PostMapping(value = "/privilege/available_list", consumes = MediaType.APPLICATION_JSON_VALUE)
    String executeAvailableList(
            @RequestHeader("app-id") String appId,
            @RequestBody String body
    );
    
}