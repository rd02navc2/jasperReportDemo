package com.beyond.surrounding.pss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingDiscountExecDTO {

    @JsonProperty("pNo")
    private Integer pNo;

    @JsonProperty("bookingDate")
    private String bookingDate;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("cardId")
    private String cardId;

    @JsonProperty("carNo")
    private String carNo;

    @JsonProperty("discId")
    private String discId;

    @JsonProperty("discName")
    private String discName;

    @JsonProperty("discHour")
    private Double discHour;

    @JsonProperty("center")
    private String center;

    @JsonProperty("isUsed")
    private String isUsed;

    @JsonProperty("modify")
    private String modify;

    @JsonProperty("enterDate")
    private String enterDate;

    @JsonProperty("exitDate")
    private String exitDate;
}