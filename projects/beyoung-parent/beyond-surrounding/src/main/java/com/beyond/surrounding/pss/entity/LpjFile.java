package com.beyond.surrounding.pss.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;

/**
 * LpjFile
 * 完全沿用舊 ERP 規格定義與自訂擴充欄位重構版
 * 已同步轉換為 Jakarta Persistence 規範、導入 Lombok 簡化，並補齊欄位映射
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "pssLpjFile")
@Table(name = "LPJ_FILE")
@DynamicInsert
public class LpjFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPJ03", length = 50)
    private String lpj03;

    @Column(name = "LPJ01", length = 255)
    private String lpj01;

    @Column(name = "LPJ02", length = 255)
    private String lpj02;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ04")
    private Date lpj04;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ05")
    private Date lpj05;

    @Column(name = "LPJ06")
    private Double lpj06;

    @Column(name = "LPJ07")
    private Integer lpj07;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ08")
    private Date lpj08;

    @Column(name = "LPJ09", length = 255)
    private String lpj09;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ10")
    private Date lpj10;

    @Column(name = "LPJ11")
    private Double lpj11;

    @Column(name = "LPJ12")
    private Double lpj12;

    @Column(name = "LPJ13")
    private Double lpj13;

    @Column(name = "LPJ14")
    private Double lpj14;

    @Column(name = "LPJ15")
    private Double lpj15;

    @Column(name = "LPJ16", length = 255)
    private String lpj16;

    @Column(name = "LPJ17", length = 255)
    private String lpj17;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ18")
    private Date lpj18;

    @Column(name = "LPJ19", length = 255)
    private String lpj19;

    @Column(name = "LPJ20", length = 255)
    private String lpj20;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ21")
    private Date lpj21;

    @Column(name = "LPJ22", length = 255)
    private String lpj22;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ23")
    private Date lpj23;

    @Column(name = "LPJ24", length = 255)
    private String lpj24;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPJ25")
    private Date lpj25;

    @Column(name = "LPJPOS", length = 50)
    private String lpjpos;

    @Column(name = "LPJ26", length = 255)
    private String lpj26;

    // ==========================================
    //  客製擴充 TA_ 欄位區
    // ==========================================
    @Column(name = "TA_LPJ01")
    private Double taLpj01;

    @Column(name = "TA_LPJ02")
    private Double taLpj02;

    @Column(name = "TA_LPJ03")
    private Double taLpj03;

    @Column(name = "TA_LPJ04", length = 255)
    private String taLpj04;

    // ==========================================
    //  核心補齊：其他單據關聯/外部自訂欄位 (原 LPK 欄位)
    // ==========================================
    @Column(name = "LPK03", length = 50) // 身分證
    private String lpk03; 

    @Column(name = "LPK04", length = 255)
    private String lpk04; 

    @Temporal(TemporalType.DATE)
    @Column(name = "LPK05") // 生日
    private Date lpk05; 

    @Column(name = "LPK06", length = 255)
    private String lpk06;

    @Column(name = "LPK15", length = 255)
    private String lpk15;

    @Column(name = "LPK18", length = 255)
    private String lpk18;

    @Column(name = "LPKUD02", length = 255)
    private String lpkud02;
}