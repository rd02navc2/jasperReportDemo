package com.beyond.surrounding.pss.service;

import com.beyond.surrounding.pss.entity.LpjFile;

/**
 * 會員資料核心業務邏輯介面
 * 升級目標：相容 Spring Boot 3 / Java 17+ 規格優化
 */
public interface MemberService {

    /**
     * 依據卡號（sCardID）取得用於停車折抵（PD, Parking Discount）的會員主檔資料
     * * @param sCardID 比漾會員卡號 / TS 卡號
     * @return LPJ_FILE 會員實體主檔
     * @throws Exception 
     */
	LpjFile getMemberData4PD(String cardID) throws Exception;

    //  提示：若未來需要啟用狀態更新方法，同樣不需宣告 throws Exception
    // void updateStatus(String sUserID);
}