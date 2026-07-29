package com.beyoung.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "PointBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class PointBean {

    @XmlElement(name = "sPointCode")
    @JsonProperty("pointCode")
    private String pointCode;

    @XmlElement(name = "dPoint")
    @JsonProperty("point")
    private Double point;
}