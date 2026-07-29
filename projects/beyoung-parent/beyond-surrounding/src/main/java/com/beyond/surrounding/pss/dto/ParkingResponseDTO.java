package com.beyond.surrounding.pss.dto;

import com.beyond.surrounding.pss.entity.ParkingDiscountSet;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingResponseDTO {

    @JsonProperty("carNO")
    private String carNO;

    @JsonProperty("enterDate")
    private LocalDateTime enterDate; // 建議改用 LocalDateTime

    @JsonProperty("parkingHour")
    private Double parkingHour;

    @JsonProperty("parkingFee")
    private Double parkingFee;

    @JsonProperty("pNo")
    private Integer pNo;

    @JsonProperty("discount")
    private List<ParkingDiscountSet> discount;

    @JsonProperty("isRent")
    private String isRent;

    @JsonProperty("userName")
    private String userName;
}