package com.beyoung.surrounding.pss.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 停車折扣/發票補登 請求參數實體
 * 移除舊版 @XmlRootElement，全面落實標準 Java 小駝峰命名，並透過 Jackson 確保相容性
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRequestBody {

    /**
     * 據點/中心代碼
     * @JsonAlias 能同時接收 "center", "sCenter" 兩種 JSON 欄位名
     */
    @JsonAlias({"center", "sCenter"})
    private String center;

    /**
     * 會員卡號
     */
    @JsonAlias({"cardNo", "sCardNO", "cardNO"})
    private String cardNo;

    /**
     * 車牌號碼
     */
    @JsonAlias({"carNo", "sCarNO", "carNO"})
    private String carNo;

    /**
     * 流水號 ID
     */
    @JsonProperty("p_no") // 維持舊版資料庫/API 特定的底線命名規則
    @JsonAlias("pNo")
    private Integer pNo;

    /* // 預留舊系統註解的欄位：消費金額
    @JsonAlias({"saleAmt", "dSaleAMT"})
    private Double saleAmt; 
    */
}