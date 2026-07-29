package com.beyond.surrounding.ec.bean;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ECRequestBody {

    @JsonProperty("center")
    private String center;

    @JsonProperty("loginId")
    private String loginId;

    @JsonProperty("counterId")
    private String counterId;

    @JsonProperty("posId")
    private String posId;

    @JsonProperty("month")
    private String month;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("cardNo")
    private String cardNo;

    @JsonProperty("point")
    private Integer point;
    
}