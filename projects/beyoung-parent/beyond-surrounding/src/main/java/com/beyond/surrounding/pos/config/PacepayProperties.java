package com.beyond.surrounding.pos.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pacepay")
@Getter
@Setter
public class PacepayProperties {

    private Integer timeout;
    private Integer expiringAt;
    private String currency;
    private String refundJustification;
    private String paymentTargetBase;
    private String refundsTarget;
    private String playgroundClientId;
    private String playgroundClientSecret; // 補上這個，IDE 的警告就會消失！
}