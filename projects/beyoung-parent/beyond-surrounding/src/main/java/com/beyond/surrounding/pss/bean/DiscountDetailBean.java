package com.beyond.surrounding.pss.bean;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 折扣明細請求與傳輸實體 (DTO/Bean)
 * 移除舊版匈牙利命名法，全面落實標準小駝峰命名，並透過 Lombok 精簡代碼
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDetailBean {

    private String isRent;
    private String userName;
    private Integer pNo; // 修正 p_no -> pNo
    private String userId;
    private String userNo;
    private String cardNo;
    private String center;
    private String carNo;
    private String enterDt;
    private String exitDt;
    private Double parkingHour;
    private Double parkingFee;
    private Double discFee;
    private Double payAmt;
    private Double paidAmt;
    private Double totDiscHour;
    private Double realDiscHour;
    private Double otherDiscFee;
    private Double otherDiscHour;  

    // 明細清單，對齊新系統的命名規範
    private List<ParkingDiscountExecBean> discount;

	
}