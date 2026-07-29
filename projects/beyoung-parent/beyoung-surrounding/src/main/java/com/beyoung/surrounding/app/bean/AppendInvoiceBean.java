package com.beyoung.surrounding.app.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import com.beyoung.surrounding.bean.ResponseBean;

@Getter // 自動生成所有 Getter
@Setter // 自動生成所有 Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppendInvoiceBean extends ResponseBean {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("CounterID")
    private String counterID;

    @JsonProperty("CounterName")
    private String counterName;

    @JsonProperty("InvoiceSN")
    private String invoiceSN;

    @JsonProperty("Amount")
    private Double amount;

    @JsonProperty("Point")
    private Double point;

    @JsonProperty("PointBase")
    private Double pointBase;

    @JsonProperty("InvoiceDate")
    private Date invoiceDate;

    @JsonProperty("InvoiceTime")
    private String invoiceTime;

    @JsonProperty("TotalPoint")
    private Double totalPoint;

    @JsonProperty("PrePoint")
    private Double prePoint;

    @JsonProperty("LastPoint")
    private Double lastPoint;

    @JsonProperty("CreditCard")
    private String creditCard;

    @JsonProperty("InvAmt")
    private Double invAmt;

    @JsonProperty("PosId")
    private String posId;

    @JsonProperty("CreditCardAmt")
    private Double creditCardAmt;
}