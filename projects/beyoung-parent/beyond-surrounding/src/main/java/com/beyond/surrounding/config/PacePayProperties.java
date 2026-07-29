package com.beyond.surrounding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pacepay")
public class PacePayProperties {
    private Integer timeout;
    private Long expiringAt;
    private String currency;
    private String refundJustification;
    private String paymentTargetBase;
    private String refundsTarget;
    private String playgroundClientId;
}