package com.beyond.surrounding.pss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // 新增
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 允許 JSON 缺少某些欄位
public class ParkingRequestDTO {

    @JsonProperty("center")
    private String center;

    @JsonProperty("cardNO")
    private String cardNO;

    @JsonProperty("carNO")
    private String carNO;

    @JsonProperty("pNo")
    private Integer pNo;

    @JsonProperty("enterDate")
    private String enterDate;

    @JsonProperty("parkingHour")
    private Double parkingHour;

    @JsonProperty("parkingFee")
    private Double parkingFee;

    @JsonProperty("discount")
    private List<DiscountDTO> discount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiscountDTO {
        @JsonProperty("disc_id")
        private String discId;
        
        @JsonProperty("disc_name")
        private String discName;
        
        @JsonProperty("disc_hour")
        private Double discHour;
        
        @JsonProperty("is_used")
        private String isUsed;
    }
}