package com.beyoung.surrounding.bonus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "RefundBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class RefundBean {

    @XmlElement(name = "sInvoiceNO") // 對齊原系統 XML 標籤名稱
    @JsonProperty("invoiceNo")       // 支援現代 JSON 的小駝峰
    private String invoiceNo;

    @XmlElement(name = "sCode")       // 對齊原本 XML 的大寫開頭
    @JsonProperty("code")
    private String code;

    @XmlElement(name = "sMessage")
    @JsonProperty("message")
    private String message;
}