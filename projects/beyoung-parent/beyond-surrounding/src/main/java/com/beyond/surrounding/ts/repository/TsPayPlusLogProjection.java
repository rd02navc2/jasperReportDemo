package com.beyond.surrounding.ts.repository;

public interface TsPayPlusLogProjection {
    String getMemberId();  // 對應 SQL 中的 memberId
    String getBarcode();   // 對應 SQL 中的 barcode
    String getOrderNo();   // 對應 SQL 中的 orderNo
    String getHppUrl();    // 對應 SQL 中的 hppUrl
}