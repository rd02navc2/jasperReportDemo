package com.beyond.surrounding.bonus.repository;

import java.util.Date; // 統一使用舊式 java.util.Date

public interface LsmHistoryProjection {
    String getLsmstore();
    String getLsm01();
    String getLsm02();
    Double getLsm04();
    
    // 統一更正為 java.util.Date，百分之百還原舊定義
    Date getLsm05(); 
    
    Double getLsm08();
    
    // 建議維持小駝峰，或配合底線（視前述 SQL 別名而定，以下為最安全的小駝峰對齊寫法）
    String getTaLsm02();
    String getTaLsm09();
    String getTqa02();      // 對應 CASE WHEN 的別名
    String getTaLsm04();
}