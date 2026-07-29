package com.beyond.surrounding.pos2.bean;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponPos2Bean implements Serializable {

    private static final long serialVersionUID = 1L;

    // 透過 @JsonProperty 鎖定舊系統首字母大寫的 API 標籤格式
    @JsonProperty("CouponNO")
    private String couponNo;

    @JsonProperty("Price")
    private Integer price;

    @JsonProperty("IsUsed")
    private String isUsed;

    @JsonProperty("IsAPP")
    private String isApp;
}