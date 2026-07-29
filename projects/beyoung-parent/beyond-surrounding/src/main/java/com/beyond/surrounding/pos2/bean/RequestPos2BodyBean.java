package com.beyond.surrounding.pos2.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 點數與排除專櫃 請求參數實體
 * 使用 Lombok 處理建構子，並手動寫出標準駝峰式 Getter / Setter
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JacksonXmlRootElement(localName = "RequestPos2Body")
public class RequestPos2BodyBean {

	@JsonProperty("center")
    @JacksonXmlProperty(localName = "center")
    private String center;

	@JsonProperty("couponId")
    @JacksonXmlProperty(localName = "couponId")
    private String couponId;

    // --- 手動 Getter & Setter (全面駝峰式) ---

    public String getCenter() {
        return this.center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getCouponId() {
        return this.couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
