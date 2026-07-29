package com.beyoung.parking.domain.dto;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointChangedEvent implements Serializable {
    private String cardNo;       // lsm01 / lpj03 (會員卡號)
    private String memberId;     // lpj01 (會員內部ID)
    private Double changedPoints;// lsm04 (本次異動點數)
    private String center;       // lsmplant / lsmlegal
    private String counterId;    // ta_lsm02
    private Double beforeTaLpj01;// 備份異動前點數
    private Double beforeTaLpj02;
    private Double beforeTaLpj03;
}