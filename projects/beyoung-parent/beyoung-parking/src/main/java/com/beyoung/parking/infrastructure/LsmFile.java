package com.beyoung.parking.infrastructure;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "lsm_file") // 點數異動流水帳明細 : 這張表是點數加減的歷史 log。每當點數有增加或減少，都必須寫入一筆不可修改的明細，用於日後對帳
@IdClass(LsmFileId.class)
public class LsmFile {

    @Id
    @Column(name = "lsm01") // String  會員卡號    寫入 lpj03（正式卡號）
    private String lsm01;

    @Id
    @Column(name = "lsm02") // String  異動類別    固定寫入 "2"（代表人工補點/線上加點）
    private String lsm02;

    @Id
    @Column(name = "lsm03") // String  流水帳單號 / 時間序號    今日日期 + 毫秒數，確保主鍵唯一不重複
    private String lsm03;

    @Column(name = "lsm04") // Double  異動點數    寫入本次增加的點數（例如 +50.0）
    private Double lsm04;

    @Column(name = "lsm05") // Date    異動日期時間  寫入當前系統時間（Timestamp）
    @Temporal(TemporalType.TIMESTAMP)
    private Date lsm05;

    @Column(name = "lsm06") // Date    點數到期日   固定寫入 9999-12-31（代表此補點永久有效、不過期）
    @Temporal(TemporalType.DATE)
    private Date lsm06;

    @Column(name = "lsm08") // Double  異動前金額/金額相關  固定寫入 0.0
    private Double lsm08;

    @Column(name = "lsmlegal") // String  法人代號    寫入 center（所屬公司/法人）
    private String lsmlegal;

    @Column(name = "lsmplant") // String  工廠/營運據點 寫入 center（發生加點的據點）
    private String lsmplant;

    @Column(name = "lsm09") // 
    private Integer lsm09;

    @Column(name = "lsm10")
    private Double lsm10;

    @Column(name = "lsm11")
    private Double lsm11;

    @Column(name = "lsm12")
    private Double lsm12;

    @Column(name = "lsm13")
    private Double lsm13;

    @Column(name = "lsm15") // String  帳款狀態 / 過帳註記 固定寫入 "1"（代表已即時入帳完成）
    private String lsm15;

    @Column(name = "lsmstore") 
    private String lsmstore;
  
    @Column(name = "ta_lsm01") // String  客製：異動原因說明   固定寫入 中文字串 "補贈點"
    private String taLsm01;

    @Column(name = "ta_lsm02") // String  客製：收銀機台 / 櫃位號   寫入 counterId（前端傳入的專櫃/機台號）
    private String taLsm02;

    @Column(name = "ta_lsm03") 
    private String taLsm03;

    @Column(name = "ta_lsm04") // String  客製：時間標記 寫入當前時間的毫秒字串
    private String taLsm04;

    @Column(name = "ta_lsm05") // Date    客製：確認日期 寫入當前系統時間
    @Temporal(TemporalType.TIMESTAMP)
    private Date taLsm05;

    @Column(name = "ta_lsm06") // Double  客製：異動前點數 A  備份加點前 lpj_file 的 ta_lpj01 餘額
    private Double taLsm06;

    @Column(name = "ta_lsm07") // Double  客製：異動前點數 B  備份加點前 lpj_file 的 ta_lpj02 餘額
    private Double taLsm07;

    @Column(name = "ta_lsm08") // Double  客製：異動前點數 C  備份加點前 lpj_file 的 ta_lpj03 餘額
    private Double taLsm08;

    @Column(name = "ta_lsm12") 
    private Double taLsm12;

    @Column(name = "ta_lsm13") // String  客製：原始參考卡號   再次寫入 cardNo 作為稽核對帳用
    private String taLsm13;
}