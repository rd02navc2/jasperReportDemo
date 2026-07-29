package com.beyoung.surrounding.pos.client;

import feign.Request;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class PaceFeignConfig {

    // 對齊 YAML: pacepay.timeout
    @Value("${pacepay.timeout:30000}")
    private int timeout;

    // 對齊 YAML: pacepay.playground-client-id
    @Value("${pacepay.playground-client-id:dummy_id}")
    private String clientId;

    // 對齊 YAML: pacepay.playground-client-secret
    @Value("${pacepay.playground-client-secret:dummy_secret}")
    private String clientSecret;

    @Bean
    public Request.Options options() {
        return new Request.Options(timeout, TimeUnit.MILLISECONDS, timeout, TimeUnit.MILLISECONDS, true);
    }

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return requestTemplate -> {
            String credentials = clientId + ":" + clientSecret;
            String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
            requestTemplate.header("Authorization", "Basic " + encoded);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}