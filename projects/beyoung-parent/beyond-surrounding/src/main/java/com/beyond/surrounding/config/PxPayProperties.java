package com.beyond.surrounding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pxpay")
public class PxPayProperties {
    private String paymentTarget;
    private Config config; // 巢狀對應 config 區塊

    @Data
    public static class Config {
        private String contentType;  // 對應 content-type
        private String merCode;      // 對應 mer-code (特店代碼)
        private String merEnName;    // 對應 mer-en-name (特店英文名稱)
        private String key;          // 雜湊或加解密金鑰
    }
}