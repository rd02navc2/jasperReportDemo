package com.beyoung.surrounding.bonus.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    // ==========================================
    // member 擴充欄位：專門用來承載拋送給 Kafka 會員端的點數異動事件資料
    // ==========================================
    
    /**
     * 唯一識別單號 (客製化贈點字軌: VIP_GIFT_訂單號)
     */
    private String bonNo; // 配合您先前的命名習慣，或命名為 accessId / billNo 都可以
    
    /**
     * 會員卡號
     */
    private String cardNo;
    
    /**
     * 本次加贈點數
     */
    private Integer point;
    
    /**
     * 中心代碼
     */
    private String center;
    
    /**
     * 專櫃代碼
     */
    private String counterId;
}