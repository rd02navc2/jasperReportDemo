package com.beyoung.surrounding.pss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiscountDetailDTO {

    @JsonProperty("isRent")
    private String isRent;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("pNo")
    private Integer pNo;

    @JsonProperty("userNO")
    private String userNO;

    @JsonProperty("cardNO")
    private String cardNO;

    @JsonProperty("center")
    private String center;

    @JsonProperty("carNO")
    private String carNO;

    @JsonProperty("enterDT")
    private String enterDT;

    @JsonProperty("exitDT")
    private String exitDT;

    @JsonProperty("parkingHour")
    private Double parkingHour;

    @JsonProperty("parkingFee")
    private Double parkingFee;

    @JsonProperty("discFee")
    private Double discFee;

    @JsonProperty("payAmt")
    private Double payAmt;

    @JsonProperty("paidAmt")
    private Double paidAmt;

    @JsonProperty("totDiscHour")
    private Double totDiscHour;

    @JsonProperty("realDiscHour")
    private Double realDiscHour;

    @JsonProperty("otherDiscFee")
    private Double otherDiscFee;

    @JsonProperty("otherDiscHour")
    private Double otherDiscHour;

    @JsonProperty("discount")
    private List<ParkingDiscountExecDTO> discount;

    /**
     * 統一錯誤處理，加入訊息字段
     */
    public static DiscountDetailDTO error(String message) {
        return DiscountDetailDTO.builder()
                .userName("Error: " + message)
                .build();
    }
}