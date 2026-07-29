package com.beyond.surrounding.erp.bean;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty; //  引入 Jackson 註解
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "RequestBonusBody")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class RequestBonusBody {

    @XmlElement(name = "sCenter")
    @JsonProperty("sCenter") //  確保 JSON 綁定成功
    private String sCenter;

    @XmlElement(name = "sDate")
    @JsonProperty("sDate")
    private String sDate;

    @XmlElement(name = "sLoginID")
    @JsonProperty("sLoginID")
    private String sLoginID;

    @XmlElement(name = "sCounterID")
    @JsonProperty("sCounterID")
    private String sCounterID;

    @XmlElement(name = "sUserID")
    @JsonProperty("sUserID")
    private String sUserID;

    @XmlElement(name = "sUserName")
    @JsonProperty("sUserName")
    private String sUserName;

    @XmlElement(name = "sCardNO")
    @JsonProperty("sCardNO")
    private String sCardNO;

    @XmlElement(name = "sCardType")
    @JsonProperty("sCardType")
    private String sCardType;

    @XmlElement(name = "sCaseNO")
    @JsonProperty("sCaseNO")
    private String sCaseNO;

    @XmlElement(name = "sCaseItem")
    @JsonProperty("sCaseItem")
    private String sCaseItem;

    @XmlElement(name = "sCouponNO")
    @JsonProperty("sCouponNO")
    private String sCouponNO;

    @XmlElement(name = "iPoint")
    @JsonProperty("iPoint")
    private Integer iPoint;

    @XmlElement(name = "iAmt")
    @JsonProperty("iAmt")
    private Integer iAmt;

    @XmlElement(name = "iQty")
    @JsonProperty("iQty")
    private Integer iQty;

    @XmlElement(name = "sInvoiceB")
    @JsonProperty("sInvoiceB")
    private String sInvoiceB;

    @XmlElement(name = "sInvoiceE")
    @JsonProperty("sInvoiceE")
    private String sInvoiceE;

    @XmlElement(name = "sPosID")
    @JsonProperty("sPosID")
    private String sPosID;

    @XmlElement(name = "sSerialNO")
    @JsonProperty("sSerialNO")
    private String sSerialNO;
    
}