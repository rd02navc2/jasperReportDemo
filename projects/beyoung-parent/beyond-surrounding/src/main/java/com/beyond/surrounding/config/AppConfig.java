package com.beyond.surrounding.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class AppConfig {

    /**
     * 手動註冊 RestTemplate Bean，解決 InvoiceService 的注入崩潰問題
     * 同時為舊 ERP 系統對接中台（ChiefPay）設定防禦性的超時時間（Timeout），防止連線掛死
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // 設定連線逾時（5秒）
        factory.setConnectTimeout(5000);
        // 設定讀取逾時（10秒）
        factory.setReadTimeout(10000);
        
        return new RestTemplate(factory);
    }
}