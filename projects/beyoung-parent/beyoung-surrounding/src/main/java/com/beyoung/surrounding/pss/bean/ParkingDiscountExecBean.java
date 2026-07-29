package com.beyoung.surrounding.pss.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 停車折扣執行明細實體 (DTO/Bean)
 * 移除舊版底線命名法，全面落實標準小駝峰命名，並透過 Lombok 精簡代碼
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDiscountExecBean {

    private Integer pNo;           // 修正 p_no -> pNo
    private String bookingDate;    // 修正 booking_date -> bookingDate
    private String userId;         // 修正 user_id -> userId
    private String cardId;         // 修正 card_id -> cardId
    private String carNo;          // 修正 car_no -> carNo
    private String discId;         // 修正 disc_id -> discId
    private String discName;       // 修正 disc_name -> discName
    private Double discHour;       // 修正 disc_hour -> discHour
    private String center;
    private String isUsed;         // 修正 is_used -> isUsed
    private String modify;
    private String enterDate;      // 修正 enter_date -> enterDate
    private String exitDate;       // 修正 exit_date -> exitDate
}