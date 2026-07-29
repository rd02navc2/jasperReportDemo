package com.beyoung.surrounding.member.dto;

import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointChangedEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private String cardNo;       // lsm01 / lpj03 (會員卡號)
    private String memberId;     // lpj01 (會員內部ID)
    
    // 改用 Object 或由 Spring Boot 自動適應，或確保名稱型態與 Bonus 拋出的型態相容
    @JsonProperty("point")
    private Double point;// 本次異動點數
    
    private String center;       // lsmplant / lsmlegal
    private String counterId;    // ta_lsm02
    private Double beforeTaLpj01;
    private Double beforeTaLpj02;
    private Double beforeTaLpj03;

    private String sourceBillNo; 
}