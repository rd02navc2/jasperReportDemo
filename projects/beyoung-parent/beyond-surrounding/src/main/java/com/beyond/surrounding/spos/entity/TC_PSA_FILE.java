package com.beyond.surrounding.spos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "SPOS_TC_PSA_FILE")
@Table(name = "TC_PSA_FILE")
@IdClass(TC_PSA_FILE_ComposeKey.class) // 綁定 5 欄位複合主鍵類別
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
public class TC_PSA_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 5 個複合主鍵欄位 ---
    @Id @Column(name = "TC_PSAPLANT") private String TC_PSAPLANT;
    @Id @Column(name = "TC_PSA01")    private String TC_PSA01;
    @Id @Column(name = "TC_PSA02")    private String TC_PSA02;
    @Id @Column(name = "TC_PSA03")    private String TC_PSA03;
    @Id @Column(name = "TC_PSA04")    private String TC_PSA04;

    // --- 一般資料欄位 ---
    @Column(name = "TC_PSA05")  private String TC_PSA05;
    @Column(name = "TC_PSA06")  private String TC_PSA06;
    @Column(name = "TC_PSA07")  private Integer TC_PSA07;
    @Column(name = "TC_PSA08")  private Double TC_PSA08;
    @Column(name = "TC_PSA09")  private Double TC_PSA09;
    @Column(name = "TC_PSA10")  private Double TC_PSA10;
    @Column(name = "TC_PSA11")  private Double TC_PSA11;
    @Column(name = "TC_PSA12")  private Double TC_PSA12;
    
    // 防呆機制：針對容易漏傳且 DB 設為 NOT NULL 的字串欄位強灌空字串預設值
    @Builder.Default @Column(name = "TC_PSA13")  private String TC_PSA13 = "";
    @Builder.Default @Column(name = "TC_PSA14")  private String TC_PSA14 = "";
    @Builder.Default @Column(name = "TC_PSA15")  private String TC_PSA15 = "";
    @Builder.Default @Column(name = "TC_PSA16")  private String TC_PSA16 = "";
    @Builder.Default @Column(name = "TC_PSA17")  private String TC_PSA17 = "";
    @Builder.Default @Column(name = "TC_PSA18")  private String TC_PSA18 = "";
    
    @Column(name = "TC_PSA19")  private Integer TC_PSA19;
    @Column(name = "TC_PSA20")  private Integer TC_PSA20;
    @Column(name = "TC_PSA21")  private String TC_PSA21;
    @Column(name = "TC_PSA22")  private String TC_PSA22;
    @Column(name = "TC_PSA23")  private String TC_PSA23;
    @Column(name = "TC_PSA24")  private String TC_PSA24;
    @Column(name = "TC_PSA25")  private String TC_PSA25;
    @Column(name = "TC_PSA26")  private String TC_PSA26;
    @Column(name = "TC_PSA27")  private String TC_PSA27;
    @Column(name = "TC_PSA28")  private Double TC_PSA28;
    @Column(name = "TC_PSA29")  private Double TC_PSA29;
    @Column(name = "TC_PSA30")  private String TC_PSA30;
    @Column(name = "TC_PSA31")  private String TC_PSA31;
    @Column(name = "TC_PSA32")  private String TC_PSA32;
    @Column(name = "TC_PSA33")  private String TC_PSA33;
    @Column(name = "TC_PSA34")  private String TC_PSA34;
    @Column(name = "TC_PSA35")  private String TC_PSA35;
    @Column(name = "TC_PSA36")  private String TC_PSA36;
    @Column(name = "TC_PSA37")  private String TC_PSA37;
    @Column(name = "TC_PSA38")  private String TC_PSA38;
    @Column(name = "TC_PSA39")  private Integer TC_PSA39;
    @Column(name = "TC_PSA40")  private Double TC_PSA40;
    @Column(name = "TC_PSA41")  private Integer TC_PSA41;
    @Column(name = "TC_PSA42")  private Double TC_PSA42;
    
    @Column(name = "TC_PSA09A") private Double TC_PSA09A;
    @Column(name = "TC_PSA09B") private Double TC_PSA09B;
    @Column(name = "TC_PSA12A") private Double TC_PSA12A;
    @Column(name = "TC_PSA12B") private Double TC_PSA12B;

    // --- 鼎新標準維護欄位 ---
    @Builder.Default @Column(name = "TC_PSAUSER")  private String TC_PSAUSER = "";
    @Builder.Default @Column(name = "TC_PSAMODU")  private String TC_PSAMODU = "";
    @Builder.Default @Column(name = "TC_PSAGRUP")  private String TC_PSAGRUP = "";
    @Builder.Default 
    @Column(name = "TC_PSADate") private String TC_PSADate = java.time.LocalDate.now().toString(); 
    @Builder.Default @Column(name = "TC_PSATIME")  private String TC_PSATIME = "";
    @Builder.Default @Column(name = "TC_PSAORIG")  private String TC_PSAORIG = "";
    @Builder.Default @Column(name = "TC_PSAORIU")  private String TC_PSAORIU = "";
    @Builder.Default @Column(name = "TC_PSALEGAL") private String TC_PSALEGAL = "";
    
    // --- 外部聯查虛擬或帶入欄位 ---
    @Column(name = "TQA02") private String TQA02;
    
}