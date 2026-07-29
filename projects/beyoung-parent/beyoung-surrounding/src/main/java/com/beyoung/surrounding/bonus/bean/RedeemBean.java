package com.beyoung.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import java.util.List;

@Data
@XmlRootElement(name = "RedeemBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class RedeemBean {

    @XmlElement(name = "sCode") //  對齊原本 XML 的大寫開頭
    @JsonProperty("code")      //  支援現代 JSON 的小駝峰
    private String code;

    @XmlElement(name = "sMessage")
    @JsonProperty("message")
    private String message;

    @XmlElement(name = "lPointBean") //  對齊原系統 XML 標籤名稱
    @JsonProperty("pointList")       //  改用更具可讀性的 JSON 屬性名
    private List<PointBean> pointList;

    @XmlElement(name = "lPrizeBean")
    @JsonProperty("prizeList")
    private List<PrizeBean> prizeList;

    @XmlElement(name = "lCouponBean")
    @JsonProperty("couponList")
    private List<CouponBean> couponList;

}