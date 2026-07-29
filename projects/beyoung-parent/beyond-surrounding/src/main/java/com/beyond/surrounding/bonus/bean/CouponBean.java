package com.beyond.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "CouponBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class CouponBean {

    @XmlElement(name = "sCouponNO")
    @JsonProperty("couponNo")
    private String couponNo;

    @XmlElement(name = "sStartDate")
    @JsonProperty("startDate")
    private String startDate;

    @XmlElement(name = "sEndDate")
    @JsonProperty("endDate")
    private String endDate;

    @XmlElement(name = "dValue")
    @JsonProperty("value")
    private Double value;
}