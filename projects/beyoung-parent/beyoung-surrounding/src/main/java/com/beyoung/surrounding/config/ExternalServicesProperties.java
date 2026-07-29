package com.beyoung.surrounding.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties
public class ExternalServicesProperties {

    /**
     * 禮券 WebService 位置
     * 對應 YAML 中的 GIFT_WS_URL
     */
    private String giftWsUrl;

    /**
     * 中台特權應用伺服器位置
     * 對應 YAML 中的 APPServer (寬鬆綁定會自動對齊 appServer 或 APPServer)
     */
    private String appServer;

    /**
     * 停車場系統配置
     */
    private Parking parking = new Parking();

    @Data
    public static class Parking {
        private String url;
    }
}