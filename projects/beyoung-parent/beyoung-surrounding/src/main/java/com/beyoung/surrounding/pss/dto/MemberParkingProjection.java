package com.beyoung.surrounding.pss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberParkingProjection {

    @JsonProperty("lpk04") private String lpk04;
    @JsonProperty("lpk05") private String lpk05;
    @JsonProperty("lpk06") private String lpk06;
    @JsonProperty("lpk15") private String lpk15;
    @JsonProperty("lpk18") private String lpk18;
    @JsonProperty("lpkud02") private String lpkud02;
    @JsonProperty("lpj01") private String lpj01;
    @JsonProperty("lpj03") private String lpj03;
    @JsonProperty("lpj02") private String lpj02;
    @JsonProperty("lpj12") private Double lpj12;
    @JsonProperty("lpj14") private Double lpj14;
    @JsonProperty("ta_lpj01") private Double taLpj01;
    @JsonProperty("ta_lpj02") private Double taLpj02;
    @JsonProperty("ta_lpj03") private Double taLpj03;
}