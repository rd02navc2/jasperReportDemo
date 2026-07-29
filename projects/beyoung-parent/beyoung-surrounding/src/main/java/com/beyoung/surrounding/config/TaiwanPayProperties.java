package com.beyoung.surrounding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "taiwanpay")
public class TaiwanPayProperties {
    private String paymentTarget;
    private String merchantId;    // 對應 merchant-id
    private String terminalId;    // 對應 terminal-id
    private String key;
    private String contentType;   // 對應 content-type
}