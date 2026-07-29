package com.beyond.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Data
@XmlRootElement(name = "RequestBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class BonusRequestBody {

    @XmlElement(name = "sCenter")
    @JsonProperty("center")
    private String center;

    @XmlElement(name = "sLoginId")
    @JsonProperty("loginId")
    private String loginId;

    @XmlElement(name = "sounterId")
    @JsonProperty("counterId")
    private String counterId;

    @XmlElement(name = "sUserId")
    @JsonProperty("userId")
    private String userId;

    @XmlElement(name = "sUserName")
    @JsonProperty("userName")
    private String userName;

    @XmlElement(name = "sCardNo")
    @JsonProperty("cardNo")
    private String cardNo;

    @XmlElement(name = "iPoint")
    @JsonProperty("point")
    private Integer point;

    @XmlElement(name = "sInvoiceB")
    @JsonProperty("invoiceB")
    private String invoiceB;

    @XmlElement(name = "sInvoiceE")
    @JsonProperty("invoiceE")
    private String invoiceE;

	
}