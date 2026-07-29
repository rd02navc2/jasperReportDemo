package com.beyond.surrounding.pos2.client;

import com.beyond.surrounding.bean.SingBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "appServerPos2Client", url = "${app.server.url:#{null}}")
public interface AppServerPos2Client {

    @PostMapping(value = "/transaction/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    String uploadTransaction(
            @RequestHeader("app-id") String appId,
            @RequestHeader("User-Agent") String userAgent,
            @RequestBody SingBean body
    );
    
    @PostMapping(value = "/transaction/upload/returned", consumes = MediaType.APPLICATION_JSON_VALUE)
    String sendReturnTransaction(
            @RequestHeader("app-id") String appId,
            @RequestHeader("User-Agent") String userAgent,
            SingBean body
    );
    
    @PostMapping(value = "/transaction/transactionCheck", consumes = MediaType.APPLICATION_JSON_VALUE)
    String checkTransaction(
            @RequestHeader("app-id") String appId,
            @RequestHeader("User-Agent") String userAgent,
            SingBean body
    );
    
}
