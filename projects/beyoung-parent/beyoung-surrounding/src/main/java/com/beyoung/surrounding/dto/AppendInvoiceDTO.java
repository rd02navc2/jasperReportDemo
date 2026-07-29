package com.beyoung.surrounding.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@SuperBuilder // 讓子類別能繼承建構器
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 自動隱藏 null 欄位，減少傳輸量
public class AppendInvoiceDTO extends BaseResponseDTO {
    
    // 將欄位名稱改為標準的小駝峰命名 (例如 Name -> name)
    private String name;
    private String counterId;
    private String counterName;
    private String invoiceSn;
    private Double amount;
    private Double point;
    private Double pointBase;
    private Date invoiceDate;
    private String invoiceTime;
    private Double totalPoint;
    private Double prePoint;
    private Double lastPoint;
    private String creditCard;
    private Double invAmt;
    private String posId;
    private Double creditCardAmt;
}