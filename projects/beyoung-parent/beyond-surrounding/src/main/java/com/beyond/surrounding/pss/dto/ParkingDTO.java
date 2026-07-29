package com.beyond.surrounding.pss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // 使用 @Data 涵蓋 @Getter, @Setter, @ToString, @EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDTO {
    
    @JsonProperty("invoiceDate")
    private String invoiceDate;

    @JsonProperty("invoiceNo")
    private String invoiceNo;

    @JsonProperty("randomNo")
    private String randomNo;

    @JsonProperty("invoiceTime")
    private String invoiceTime;

    @JsonProperty("channel")
    private String channel;

    @JsonProperty("tranXType")
    private Integer tranXType;

    @JsonProperty("center")
    private String center;

    @JsonProperty("counterId")
    private String counterId;

    @JsonProperty("cardNO")
    private String cardNO;

    @JsonProperty("carNO")
    private String carNO;

    @JsonProperty("promoteAmt")
    private Double promoteAmt;
}