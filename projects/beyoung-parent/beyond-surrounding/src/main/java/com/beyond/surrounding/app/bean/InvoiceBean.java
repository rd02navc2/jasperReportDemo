package com.beyond.surrounding.app.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.beyond.surrounding.bean.ResponseBean;

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceBean extends ResponseBean {

    // 使用 @JsonProperty 確保與舊系統 API 欄位大小寫完全一致
    @JsonProperty("InvoiceNo")
    private String invoiceNo;

    @JsonProperty("IsUsed")
    private String isUsed;

    @JsonProperty("IsRefund")
    private String isRefund;
    
}