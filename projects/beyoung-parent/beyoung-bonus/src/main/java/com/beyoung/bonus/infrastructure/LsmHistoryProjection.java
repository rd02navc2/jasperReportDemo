package com.beyoung.bonus.infrastructure;

import java.time.LocalDateTime;

public interface LsmHistoryProjection {
    String getLsmstore();
    String getLsm01();
    String getLsm02();
    Double getLsm04();
    LocalDateTime getLsm05();
    Double getLsm08();
    String getTa_lsm02();
    String getTa_lsm09();
    String getTqa02();      // 對應 CASE WHEN 的別名
    String getTa_lsm04();
}