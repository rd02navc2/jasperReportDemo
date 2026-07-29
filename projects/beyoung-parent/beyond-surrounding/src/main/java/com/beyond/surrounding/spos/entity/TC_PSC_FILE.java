package com.beyond.surrounding.spos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "SPOS_TC_PSC_FILE")
@Table(name = "TC_PSC_FILE")
@IdClass(TC_PSC_FILE_ComposeKey.class) //  綁定 7 欄位複合主鍵類別
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@com.fasterxml.jackson.annotation.JsonAutoDetect(
	    fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY,
	    getterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE,
	    setterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE
	)
public class TC_PSC_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 7 個複合主鍵欄位 ---
    @Id @Column(name = "TC_PSCPLANT") private String TC_PSCPLANT;
    @Id @Column(name = "TC_PSC01")    private String TC_PSC01;
    @Id @Column(name = "TC_PSC02")    private String TC_PSC02;
    @Id @Column(name = "TC_PSC03")    private String TC_PSC03;
    @Id @Column(name = "TC_PSC04")    private String TC_PSC04; //  修正與鍵值類別型態相依問題
    @Id @Column(name = "TC_PSC05")    private String TC_PSC05;
    @Id @Column(name = "TC_PSC07")    private String TC_PSC07;

    // --- 一般資料欄位 ---
    @Column(name = "TC_PSC06") private String TC_PSC06;
    @Column(name = "TC_PSC08") private Double TC_PSC08;
    @Column(name = "TC_PSC09") private String TC_PSC09;
    @Column(name = "TC_PSC10") private Integer TC_PSC10;
    @Column(name = "TC_PSC11") private Integer TC_PSC11;
    @Column(name = "TC_PSC13") private String TC_PSC13;
    @Column(name = "TC_PSC14") private Integer TC_PSC14;
    @Column(name = "TC_PSC15") private String TC_PSC15;
    @Column(name = "TC_PSC16") private String TC_PSC16;
    @Column(name = "TC_PSC17") private String TC_PSC17;
    @Column(name = "TC_PSC18") private String TC_PSC18;
    @Column(name = "TC_PSC19") private String TC_PSC19;
    @Column(name = "TC_PSC20") private String TC_PSC20;
    @Column(name = "TC_PSC21") private String TC_PSC21;
    @Column(name = "TC_PSC22") private String TC_PSC22;

    // --- 外部聯查或攜帶欄位 ---
    @Column(name = "TQA02")    private String TQA02;
    @Column(name = "TC_PSA13") private String TC_PSA13;
}