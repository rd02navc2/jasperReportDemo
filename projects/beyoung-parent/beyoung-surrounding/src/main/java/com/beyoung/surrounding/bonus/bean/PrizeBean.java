package com.beyoung.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import java.util.List;

@Data
@XmlRootElement(name = "PrizeBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrizeBean {

    @XmlElement(name = "sActivityCode")
    @JsonProperty("activityCode")
    private String activityCode;

    @XmlElement(name = "sActivityName")
    @JsonProperty("activityName")
    private String activityName;

    @XmlElement(name = "sPrizeType")
    @JsonProperty("prizeType")
    private String prizeType;

    @XmlElement(name = "sPrizeCode")
    @JsonProperty("prizeCode")
    private String prizeCode;

    @XmlElement(name = "sPrizeName")
    @JsonProperty("prizeName")
    private String prizeName;

    @XmlElement(name = "sPointCode")
    @JsonProperty("pointCode")
    private String pointCode;

    @XmlElement(name = "dNeedPoint")
    @JsonProperty("needPoint")
    private Double needPoint;

    @XmlElement(name = "dRedeemableQty")
    @JsonProperty("redeemableQty")
    private Double redeemableQty;

    @XmlElement(name = "dPrice")
    @JsonProperty("price")
    private Double price;

    // 宣告改用介面 List，更符合 Java 的設計良好習慣（底層一樣可以使用 ArrayList）
    @XmlElement(name = "aCouponNO")
    @JsonProperty("couponNo")
    private List<String> couponNo;
}