package com.beyoung.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;


/**
 * TcPsaFile
 * T100/舊 ERP 客製規格對應實體類別（現代化重構版）
 * 已同步轉換為 Jakarta 規範、導入 Lombok 簡化，並保留 ResponseBean 繼承鏈
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity(name = "pssTcPsaFile")
@Table(name = "TC_PSA_FILE")
@IdClass(TcPsaFileId.class)
@DynamicInsert
public class TcPsaFile implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================================
    // 舊系統的 5 個複合主鍵宣告 (Primary Keys)
    // ==========================================
    @Id
    @Column(name = "TC_PSAPLANT", length = 10)
    private String tcPsaplant;

    @Id
    @Column(name = "TC_PSA01", length = 20)
    private String tcPsa01;

    @Id
    @Column(name = "TC_PSA02", length = 20)
    private String tcPsa02;

    @Id
    @Column(name = "TC_PSA03", length = 20)
    private String tcPsa03;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSA04")
    private Date tcPsa04; // 發票單據日期

    // ==========================================
    //  核心與延伸舊欄位完整還原區 (TC_PSA05 ~ TC_PSA42)
    // ==========================================
    @Column(name = "TC_PSA05", length = 10)
    private String tcPsa05; // 項次

    @Column(name = "TC_PSA06", length = 10)
    private String tcPsa06; // 狀態 / 單據類型 (validate 檢核用)

    @Column(name = "TC_PSA07")
    private Integer tcPsa07;

    @Column(name = "TC_PSA08")
    private Double tcPsa08;

    @Column(name = "TC_PSA09")
    private Double tcPsa09;

    @Column(name = "TC_PSA10")
    private Double tcPsa10;

    @Column(name = "TC_PSA11")
    private Double tcPsa11;

    @Column(name = "TC_PSA12")
    private Double tcPsa12;

    @Column(name = "TC_PSA13", length = 20)
    private String tcPsa13; // 會員關聯鍵 (對應 lpj03)

    @Column(name = "TC_PSA14")
    private String tcPsa14;

    @Column(name = "TC_PSA15")
    private String tcPsa15;

    @Column(name = "TC_PSA16", length = 20, nullable = false)
    private String tcPsa16; // 發票號碼 (validate 檢核用)

    @Column(name = "TC_PSA17", length = 10)
    private String tcPsa17; // 隨機碼 (validate 檢核用)

    @Column(name = "TC_PSA18")
    private String tcPsa18;

    @Column(name = "TC_PSA19")
    private Integer tcPsa19;

    @Column(name = "TC_PSA20")
    private Integer tcPsa20;

    @Column(name = "TC_PSA21")
    private String tcPsa21;

    @Column(name = "TC_PSA22")
    private String tcPsa22;

    @Column(name = "TC_PSA23")
    private String tcPsa23;

    @Column(name = "TC_PSA24")
    private String tcPsa24;

    @Column(name = "TC_PSA25")
    private String tcPsa25;

    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSA26")
    private Date tcPsa26;

    @Column(name = "TC_PSA27")
    private String tcPsa27;

    @Column(name = "TC_PSA28")
    private Double tcPsa28;

    @Column(name = "TC_PSA29")
    private Double tcPsa29;

    @Column(name = "TC_PSA30")
    private String tcPsa30;

    @Column(name = "TC_PSA31")
    private Double tcPsa31; // 消費總金額 ( 配合舊邏輯宣告為 Double 型態)

    @Column(name = "TC_PSA32")
    private String tcPsa32;

    @Column(name = "TC_PSA33")
    private String tcPsa33;

    @Column(name = "TC_PSA34")
    private String tcPsa34;

    @Column(name = "TC_PSA35")
    private String tcPsa35;

    @Column(name = "TC_PSA36")
    private String tcPsa36;

    @Column(name = "TC_PSA37")
    private String tcPsa37;

    @Column(name = "TC_PSA38")
    private String tcPsa38;

    @Column(name = "TC_PSA39")
    private Integer tcPsa39;

    @Column(name = "TC_PSA40")
    private Double tcPsa40;

    @Column(name = "TC_PSA41")
    private Integer tcPsa41;

    @Column(name = "TC_PSA42")
    private Double tcPsa42;

    // ==========================================
    // 舊系統 A/B 後綴特殊欄位
    // ==========================================
    @Column(name = "TC_PSA09A")
    private Double tcPsa09A;

    @Column(name = "TC_PSA09B")
    private Double tcPsa09B;

    @Column(name = "TC_PSA12A")
    private Double tcPsa12A;

    @Column(name = "TC_PSA12B")
    private Double tcPsa12B;

    // ==========================================
    // Tiptop 標準審核與日誌追蹤欄位 (Audit Fields)
    // ==========================================
    @Column(name = "TC_PSAUSER")
    private String tcPsauser; // 建檔人員

    @Column(name = "TC_PSAMODU")
    private String tcPsamodu; // 修改人員

    @Column(name = "TC_PSAGRUP")
    private String tcPsagrup; // 建檔部門

    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSADATE")
    private Date tcPsadate;   // 最近修改日

    @Column(name = "TC_PSATIME")
    private String tcPsatime; // 註記時間

    @Column(name = "TC_PSAORIG")
    private String tcPsaorig; // 建立部門

    @Column(name = "TC_PSAORIU")
    private String tcPsaoriu; // 建立者

    @Column(name = "TC_PSALEGAL")
    private String tcPsalegal; // 法人

    // ==========================================
    // 外部關聯/暫時欄位 (保留原本舊系統設計)
    // ==========================================
    @Column(name = "TQA02")
    private String tqa02;

    // ==========================================
    // 系統新版審計欄位 (選留，可根據資料庫實際狀況增減)
    // ==========================================
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", updatable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    @Transient
    private String code; // 僅供 API 狀態流轉使用的非資料庫欄位

    @Transient
    private String message; // 僅供 API 錯誤訊息提示使用的非資料庫欄位
}