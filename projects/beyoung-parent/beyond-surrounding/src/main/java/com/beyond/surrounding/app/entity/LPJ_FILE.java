package com.beyond.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.util.Date;

/**
 * LpjFile
 * 依照舊系統原始單一主鍵 (LPJ03) 定義補齊重構版
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity(name = "LPJ_FILE")
@Table(name = "LPJ_FILE")
@DynamicInsert
public class LPJ_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "LPJ01", length = 255)
    private String lpj01;

    @Column(name = "LPJ02", length = 255)
    private String lpj02;

    @Id
    @Column(name = "LPJ03", length = 50)
    private String lpj03;

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

    @Column(name = "TA_LPJ01")
    private Double taLpj01;

    @Column(name = "TA_LPJ02")
    private Double taLpj02;

    @Column(name = "TA_LPJ03")
    private Double taLpj03;

    @Column(name = "TA_LPJ04", length = 255)
    private String taLpj04;

    @Transient
    private String lpk03;

    @Transient
    private String lpk04;

    @Transient
    private String lpk05;

    public LPJ_FILE() {
    }
    
}