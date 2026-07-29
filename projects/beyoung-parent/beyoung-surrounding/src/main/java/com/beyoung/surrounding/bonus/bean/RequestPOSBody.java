package com.beyoung.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import java.util.List;

@Data
@XmlRootElement(name = "RequestPOSBody")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestPOSBody {

    @XmlElement(name = "sUserID")
    @JsonProperty("userId")
    private String userId;

    @XmlElement(name = "sCounterID")
    @JsonProperty("counterId")
    private String counterId;

    @XmlElement(name = "sCardNO")
    @JsonProperty("cardNo")
    private String cardNo;

    @XmlElement(name = "sCreditCard")
    @JsonProperty("creditCard")
    private String creditCard;

    @XmlElement(name = "iTradeAmt")
    @JsonProperty("tradeAmt")
    private Integer tradeAmt;

    @XmlElement(name = "iInvoiceAmt")
    @JsonProperty("invoiceAmt")
    private Integer invoiceAmt;

    @XmlElement(name = "iPromoteAmt")
    @JsonProperty("promoteAmt")
    private Integer promoteAmt;

    @XmlElement(name = "iCreditAmt")
    @JsonProperty("creditAmt")
    private Integer creditAmt;

    @XmlElement(name = "sDeviceID")
    @JsonProperty("deviceId")
    private String deviceId;

    @XmlElement(name = "sInvoiceSN")
    @JsonProperty("invoiceSN")
    private String invoiceSN;

    @XmlElement(name = "sInvoiceNO")
    @JsonProperty("invoiceNO")
    private String invoiceNO;

    @XmlElement(name = "sInvoiceDate")
    @JsonProperty("invoiceDate")
    private String invoiceDate;

    @XmlElement(name = "lPrizeBean")
    @JsonProperty("prizeList")
    private List<PrizeBean> prizeList;
    
}