package com.beyoung.surrounding.pos.client;

import javax.net.ssl.SSLSocketFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.beyoung.surrounding.util.CryptUtil;

import feign.Client;

@Configuration
public class FeignConfig {
    @Bean
    public Client feignClient() {
        return new Client.Default(getSSLSocketFactory(), (hostname, session) -> true);
    }

    private SSLSocketFactory getSSLSocketFactory() {
        // 請確保這裡呼叫您專案中原本的 CryptUtil.getSSLContext()
        return CryptUtil.getSSLContext().getSocketFactory();
    }
}