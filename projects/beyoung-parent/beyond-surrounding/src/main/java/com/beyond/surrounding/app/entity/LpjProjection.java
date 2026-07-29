package com.beyond.surrounding.app.entity;

/**
 * 用於接收 LpjFileRepository 複雜查詢的投影介面
 * 確保 get 方法名稱與 Repository 中的 AS 別名一一對應
 */
public interface LpjProjection {
    String getLpj01();    // 對應 AS lpj01
    String getLpj02();    // 對應 AS lpj02
    String getLpj03();      // 對應 SQL AS lpj03
    Double getLpj07();    // 對應 AS lpj07
    Double getLpj08();    // 對應 AS lpj08
    Double getLpj12();    // 對應 AS lpj12
    Double getLpj14();    // 對應 AS lpj14
    Double getLpj15();    // 對應 AS lpj15
    Double getTaLpj01();   // 對應 SQL AS taLpj01
    Double getTaLpj02();   // 對應 SQL AS taLpj02 (新增)
    Double getTaLpj03();  // 對應 AS taLpj03
    String getTaLpj04();  // 對應 AS taLpj04
    String getLpk04();    // 對應 AS lpk04 (來自 lpk_file)
}