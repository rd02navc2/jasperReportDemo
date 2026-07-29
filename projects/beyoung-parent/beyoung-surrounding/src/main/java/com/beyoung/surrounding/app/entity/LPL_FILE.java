package com.beyoung.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Date;

/**
 * LplFile
 * 完全依照舊系統 LPL_FILE 規格與客製欄位定義補齊版
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LPL_FILE")
@IdClass(LPL_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class LPL_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPL01", length = 50)
    private String lpl01;

    @Id
    @Temporal(TemporalType.DATE) // 還原為舊系統 java.util.Date，避免 HQL 查詢與轉型異常
    @Column(name = "LPL02")
    private Date lpl02;

    @Id
    @Column(name = "LPL09")
    private Integer lpl09;

    @Id
    @Column(name = "LPLPLANT", length = 50)
    private String lplplant;

    @Column(name = "LPL03", length = 255)
    private String lpl03;

    @Column(name = "LPL04", length = 255)
    private String lpl04;

    @Column(name = "LPL05", length = 255)
    private String lpl05;

    @Column(name = "LPL06", length = 255)
    private String lpl06;

    @Column(name = "LPL07")
    private Double lpl07;

    @Column(name = "LPL08")
    private Double lpl08;

    @Column(name = "LPLLEGAL", length = 50)
    private String lpllegal;

    // ==========================================
    //  客製擴充 TA_ 欄位區（完全對齊舊版宣告順序與名稱）
    // ==========================================
    @Column(name = "TA_LPL10", length = 255)
    private String taLpl10;

    @Column(name = "TA_LPL11", length = 255)
    private String taLpl11; //  已精準修正為 taLpl11

    @Column(name = "TA_LPL01", length = 255)
    private String taLpl01;

    @Column(name = "TA_LPL02", length = 255)
    private String taLpl02;
}