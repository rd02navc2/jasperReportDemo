package com.beyond.surrounding.pss.client;

import com.beyond.surrounding.util.CryptUtil;
import feign.Client;
import org.springframework.context.annotation.Bean;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class FeignIgnoreSslConfig {

    @Bean
    public Client feignClient() {
        try {
            // 建立一個盲目信任所有 Hostname 的驗證器
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // 使用您原有的 CryptUtil.getSSLContext() 封裝成 Feign 的 Client
            return new Client.Default(
                    CryptUtil.getSSLContext().getSocketFactory(), 
                    allHostsValid
            );
        } catch (Exception e) {
            throw new RuntimeException("初始化 Feign SSL 配置失敗", e);
        }
    }
}