package com.beyoung.bonus.domain.event; // 請依據您實際的 package 路徑調整

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointChangedEvent {
    private String cardNo;
    private Double changedPoints;
    private String center;
    private String counterId;
    
    // ==========================================
    // 新增：串聯跨微服務的唯一流水單號 (LSM03)
    // ==========================================
    private String billNo; 
}