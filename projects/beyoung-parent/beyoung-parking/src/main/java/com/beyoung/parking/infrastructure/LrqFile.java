package com.beyoung.parking.infrastructure;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "lrq_file") // 會員基本資料擴充檔 : 這張表通常用來存放會員的附加資訊（例如個人資料、聯絡管道等）
@IdClass(LrqFileId.class)
public class LrqFile {

    @Id
    @Column(name = "lrq02") // String  活動代號 / 規則代號 程式中固定查 "603"（可能代表「人工補點規則」）
    private String lrq02;

    @Id
    @Column(name = "lrqplant") // String  營運據點 / 店別代號 對應前端傳入的 center（據點/百貨店別）
    private String lrqplant;

    @Column(name = "lrq03") // Integer 活動贈點基數 / 點數倍率   雖然舊程式有查出這個設定值，但後續被覆蓋未直接使用
    private Integer lrq03;

    @Column(name = "lrqacti") // String  有效註記    必須為 'Y'，代表該活動規則目前處於啟用狀態
    private String lrqacti;

    @Column(name = "lrq10") // Date    活動開始日期  系統當前日期（sysdate）必須介於 lrq10 與 lrq11 之間
    @Temporal(TemporalType.DATE)
    private Date lrq10;

    @Column(name = "lrq11") // Date    活動結束日期  用來確保該點數活動目前沒有過期
    @Temporal(TemporalType.DATE)
    private Date lrq11;
}