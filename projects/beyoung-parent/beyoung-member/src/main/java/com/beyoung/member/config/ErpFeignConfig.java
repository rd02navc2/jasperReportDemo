package com.beyoung.member.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class ErpFeignConfig {

    @Bean
    public RequestInterceptor soapRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "text/xml; charset=UTF-8");
            requestTemplate.header("soapaction", "\"\"");
        };
    }
    
    // 移除自訂的 StringEncoder / StringDecoder。
    // Spring Cloud Feign 預設的 HttpMessageConverters 就已經完美支援 String 的輸入與輸出。
}