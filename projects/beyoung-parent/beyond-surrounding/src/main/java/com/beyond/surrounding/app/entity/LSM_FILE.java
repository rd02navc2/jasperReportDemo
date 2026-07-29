package com.beyond.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * LsmFile
 * 完全依照舊系統 LSM_FILE 規格、欄位順序與型態定義補齊版
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LSM_FILE")
@IdClass(LSM_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class LSM_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================================
    //  複合主鍵定義區 (完全還原 java.util.Date)
    // ==========================================
    @Id
    @Column(name = "LSM01", length = 50)
    private String lsm01;

    @Id
    @Column(name = "LSM02", length = 50)
    private String lsm02;

    @Id
    @Column(name = "LSM03", length = 50)
    private String lsm03;

    @Id
    @Temporal(TemporalType.DATE) //  還原為舊系統 java.util.Date
    @Column(name = "LSM05")
    private Date lsm05;

 // ==========================================
    //  手動關聯與一般商務欄位定義區
    // ==========================================
    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LSM01", referencedColumnName = "lpj03", insertable = false, updatable = false)
    private LPJ_FILE lpjFile;

    @Column(name = "LSM04")
    private Double lsm04;

    @Temporal(TemporalType.DATE) //  還原為 java.util.Date
    @Column(name = "LSM06")
    private Date lsm06;

    @Column(name = "LSM07", length = 255)
    private String lsm07;

    @Column(name = "LSM08")
    private Double lsm08;

    @Column(name = "LSMLEGAL", length = 50)
    private String lsmlegal;

    @Column(name = "LSMPLANT", length = 50)
    private String lsmplant;

    @Column(name = "LSM09")
    private Integer lsm09;

    @Column(name = "LSM10")
    private Double lsm10;

    @Column(name = "LSM11")
    private Double lsm11;

    @Column(name = "LSM12")
    private Double lsm12;

    @Column(name = "LSM13")
    private Double lsm13;

    @Temporal(TemporalType.DATE) //  還原為 java.util.Date
    @Column(name = "LSM14")
    private Date lsm14;

    @Column(name = "LSM15", length = 255)
    private String lsm15;

    @Column(name = "LSMSTORE", length = 50)
    private String lsmstore;

    @Column(name = "TQA02", length = 255)
    private String tqa02;

    // ==========================================
    //  客製擴充 TA_ 欄位區（完全對齊舊版型態）
    // ==========================================
    @Column(name = "TA_LSM09", length = 255)
    private String taLsm09;

    @Column(name = "TA_LSM10", length = 255)
    private String taLsm10;

    @Column(name = "TA_LSM01", length = 255)
    private String taLsm01;

    @Column(name = "TA_LSM02", length = 255)
    private String taLsm02;

    @Column(name = "TA_LSM03", length = 255)
    private String taLsm03;

    @Column(name = "TA_LSM04", length = 255)
    private String taLsm04;

    @Temporal(TemporalType.DATE) //  還原為 java.util.Date
    @Column(name = "TA_LSM05")
    private Date taLsm05;

    @Column(name = "TA_LSM06")
    private Double taLsm06;

    @Column(name = "TA_LSM07")
    private Double taLsm07;

    @Column(name = "TA_LSM08")
    private Double taLsm08;

    @Column(name = "TA_LSM11", length = 255)
    private String taLsm11;

    @Column(name = "TA_LSM12")
    private Double taLsm12;
    
    @Column(name = "TA_LSM13")
    private String taLsm13;
    
    @Transient
    private String extendTqa02;
    
    public String getExtendTqa02() { return extendTqa02; }
    public void setExtendTqa02(String extendTqa02) { this.extendTqa02 = extendTqa02; }
    
    public LSM_FILE() {
    }
    
}