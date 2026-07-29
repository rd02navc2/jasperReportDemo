package com.beyoung.surrounding.coupon.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "RequestCouponBody") //  修正點：將 XML 根節點名稱改為與類別一致的 RequestCouponBody
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestCouponBody {

    // 補齊欄位 1：對齊舊 API 的 sCenter
    @XmlElement(name = "sCenter")
    @JsonProperty("center")
    private String center;

    // 補齊欄位 2：對齊舊 API 的 sCouponID
    @XmlElement(name = "sCouponID")
    @JsonProperty("couponID")
    private String couponID;

}