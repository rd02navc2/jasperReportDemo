package com.beyoung.surrounding.app.entity;

import java.util.Date;

/**
 * 用於接收 TcPsbRepository 複雜查詢的投影介面
 */
public interface TcPsbProjection {
    String getTcPsbplant();
    String getTcPsb01();
    String getTcPsb02();
    String getTcPsb03();
    Date   getTcPsb04();
    Integer getTcPsb06();
    String getTcPsb05();
    String getTcPsb07();
    String getTcPsb08();
    Integer getTcPsb09();
    Double getTcPsb10();
    Double getTcPsb11();
    Double getTcPsb12();
    Double getTcPsb13();
    Double getTcPsb13a();
    Double getTcPsb13b();
    Double getTcPsb14();
    String getTcPsb15();
    String getTcPsb16();
    String getTcPsb17();
    String getTcPsb18();
    String getTcPsb19();
    Double getTcPsb20();
    String getTcPsb21();
    Integer getTcPsb22();
    Double getTcPsb23();
    String getIma25();
    String getLnt04(); // 來自 lnt_file 的資料
    
}