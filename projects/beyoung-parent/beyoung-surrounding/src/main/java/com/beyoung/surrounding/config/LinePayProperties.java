package com.beyoung.surrounding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "linepay")
public class LinePayProperties {
    private String channelId;
    private String authorizationKey; // 對應 YAML 中的 authorization-key
    private String paymentTarget;    // 對應 YAML 中的 payment-target
}