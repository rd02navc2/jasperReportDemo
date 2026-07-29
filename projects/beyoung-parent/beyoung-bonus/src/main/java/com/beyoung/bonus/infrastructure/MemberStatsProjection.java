package com.beyoung.bonus.infrastructure;

/**
 * 用於承接原生 SQL 的統計查詢結果
 */
public interface MemberStatsProjection {
    // 方法名稱必須對應 SQL 查詢中的欄位別名 (As)
    // Spring Data 會自動將 totalLsm08 對應到 SQL 的 AS totalLsm08
    Double getTotalLsm08(); 
    Integer getVipLevel();
}
