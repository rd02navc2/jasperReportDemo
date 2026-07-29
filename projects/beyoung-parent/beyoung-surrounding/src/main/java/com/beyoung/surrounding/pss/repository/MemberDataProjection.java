package com.beyoung.surrounding.pss.repository;

import java.util.Date;

/**
 * 用於接收 ERP 會員車牌折抵原生 SQL 的資料投影介面
 *  Spring Data JPA 會依據別名（不分大小寫）自動將欄位結果注入
 */
public interface MemberDataProjection {
    String getLPK04();
    Date getLPK05();
    String getLPK06();
    String getLPK15();
    String getLPK18();
    String getLPKUD02();
    String getLPJ01();
    String getLPJ03();
    Double getLPJ12();
    Double getLPJ14();
    Double getTA_LPJ01();
    Double getTA_LPJ02();
    Double getTA_LPJ03();
}