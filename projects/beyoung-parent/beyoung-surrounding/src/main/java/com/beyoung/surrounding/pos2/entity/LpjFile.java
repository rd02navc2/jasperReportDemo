package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "POS2_LPJ_FILE")
@Table(name = "LPJ_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LpjFile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "LPJ01")
    private String lpj01;

    @Column(name = "LPJ02")
    private String lpj02;

    @Id
    @Column(name = "LPJ03")
    private String lpj03;

    @Column(name = "LPJ04")
    private Date lpj04;

    @Column(name = "LPJ05")
    private Date lpj05;

    @Column(name = "LPJ06")
    private Double lpj06;

    @Column(name = "LPJ07")
    private Integer lpj07;

    @Column(name = "LPJ08")
    private Date lpj08;

    @Column(name = "LPJ09")
    private String lpj09;

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

    @Column(name = "LPJ16")
    private String lpj16;

    @Column(name = "LPJ17")
    private String lpj17;

    @Column(name = "LPJ18")
    private Date lpj18;

    @Column(name = "LPJ19")
    private String lpj19;

    @Column(name = "LPJ20")
    private String lpj20;

    @Column(name = "LPJ21")
    private Date lpj21;

    @Column(name = "LPJ22")
    private String lpj22;

    @Column(name = "LPJ23")
    private Date lpj23;

    @Column(name = "LPJ24")
    private String lpj24;

    @Column(name = "LPJ25")
    private Date lpj25;

    @Column(name = "LPJPOS")
    private String lpjpos;

    @Column(name = "LPJ26")
    private String lpj26;

    @Column(name = "TA_LPJ01")
    private Double taLpj01;

    @Column(name = "TA_LPJ02")
    private Double taLpj02;

    @Column(name = "TA_LPJ03")
    private Double taLpj03;

    @Column(name = "TA_LPJ04")
    private String taLpj04;

    // 以下為原實體內附帶的 LPK 關聯欄位（可能用於 Native SQL 的 View 或多表 Join 投影）
    @Column(name = "LPK03")
    private String lpk03; // 身分證

    @Column(name = "LPK04")
    private String lpk04; // 姓名

    @Column(name = "LPK05")
    private String lpk05; // 生日
}