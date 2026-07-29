package com.beyoung.parking.domain.bean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 點數處理回應 Bean
 * 適用於 Java 21 / Spring Boot 3.4.3
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointResponseBean {

    /**
     * 回傳狀態碼 (例如: ErrCodeConst.finished)
     */
    private String code;

    /**
     * 回傳訊息說明 (例如: ErrCodeConst.finished_message)
     */
    private String message;
    
    /* * 如果未來此 Bean 需要封裝其他 ERP 回傳的欄位，
     * 可以直接在下方擴充，例如：
     * private Integer currentPoint; 
     */
}