package com.beyoung.surrounding.pss.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
	 * 停車發票補登請求參數實體
	 * 移除舊版 @XmlRootElement，採用現代化 Jackson 註解並全面落實小駝峰命名
	 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInvoiceRequestBody {

    @JsonProperty("sInvoiceDate")
    private String invoiceDate;

    @JsonProperty("sInvoiceNo")
    private String invoiceNo;

    @JsonProperty("sRandomNo")
    private String randomNo;

    @JsonProperty("sInvoiceTime")
    private String invoiceTime;

    @JsonProperty("sChannel")
    private String channel;

    @JsonProperty("iTranXType")
    private Integer tranXType;

    @JsonProperty("sCenter")
    private String center;

    @JsonProperty("sCounterId")
    private String counterId;

    @JsonProperty("sCardNO")
    private String cardNo;

    @JsonProperty("sCarNO")
    private String carNo;

    @JsonProperty("dPromoteAmt")
    private Double promoteAmt;
}
