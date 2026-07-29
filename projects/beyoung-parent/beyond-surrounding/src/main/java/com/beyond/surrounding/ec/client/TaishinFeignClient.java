package com.beyond.surrounding.ec.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import feign.Client;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@FeignClient(name = "taishinApiClient", url = "${TS_API_URL}", configuration = TaishinFeignClient.Configuration.class)
public interface TaishinFeignClient {

    @PostMapping(value = "/auth.ashx", consumes = "application/json;charset=utf-8", produces = "application/json;charset=utf-8")
    String postBackToTaishin(@RequestBody String jsonBody);

    class Configuration {
        @Bean
        public Client feignClient() {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        @Override public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {}
                        @Override public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {}
                        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
                }, new java.security.SecureRandom());

                HostnameVerifier allHostsValid = (hostname, session) -> true;

                // 🌟 直接返回 Feign 的預設 Client，但掛載免簽 SSLSocketFactory
                return new Client.Default(sslContext.getSocketFactory(), allHostsValid);
            } catch (Exception e) {
                throw new RuntimeException("無法建立免認證的 Feign Client", e);
            }
        }
    }
    
}