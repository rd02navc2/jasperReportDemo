package com.beyond.surrounding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "chiefpay")
public class ChiefPayProperties {
    private Api api;
    private Parking parking;

    @Data
    public static class Api {
        private String url;
    }

    @Data
    public static class Parking {
        private String url;
    }
}