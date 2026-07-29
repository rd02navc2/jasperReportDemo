package com.beyond.surrounding.spos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "SPOS_TC_PSB_FILE")
@Table(name = "TC_PSB_FILE")
@IdClass(TC_PSB_FILE_ComposeKey.class) //  綁定 6 欄位複合主鍵類別
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
public class TC_PSB_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 6 個複合主鍵欄位 ---
    @Id @Column(name = "TC_PSBPLANT") private String TC_PSBPLANT;
    @Id @Column(name = "TC_PSB01")    private String TC_PSB01;
    @Id @Column(name = "TC_PSB02")    private String TC_PSB02;
    @Id @Column(name = "TC_PSB03")    private String TC_PSB03;
    @Id @Column(name = "TC_PSB04")    private String TC_PSB04;
    @Id @Column(name = "TC_PSB06")    private Integer TC_PSB06; //  明細項次/序號

    // --- 一般資料欄位 ---
    @Column(name = "TC_PSB05")  private String TC_PSB05;
    @Column(name = "TC_PSB07")  private String TC_PSB07;
    @Column(name = "TC_PSB08")  private String TC_PSB08;
    @Column(name = "TC_PSB09")  private Integer TC_PSB09;
    @Column(name = "TC_PSB10")  private Double TC_PSB10;
    @Column(name = "TC_PSB11")  private Double TC_PSB11;
    @Column(name = "TC_PSB12")  private Double TC_PSB12;
    @Column(name = "TC_PSB13")  private Double TC_PSB13;
    @Column(name = "TC_PSB14")  private Double TC_PSB14;
    @Column(name = "TC_PSB15")  private String TC_PSB15;
    @Column(name = "TC_PSB16")  private String TC_PSB16;
    @Column(name = "TC_PSB17")  private String TC_PSB17;
    @Column(name = "TC_PSB18")  private String TC_PSB18;
    @Column(name = "TC_PSB19")  private String TC_PSB19;
    @Column(name = "TC_PSB20")  private Double TC_PSB20;
    @Column(name = "TC_PSB21")  private String TC_PSB21;
    @Column(name = "TC_PSB22")  private Double TC_PSB22;
    @Column(name = "TC_PSB23")  private Double TC_PSB23;
    
    @Column(name = "TC_PSB13A") private Double TC_PSB13A;
    @Column(name = "TC_PSB13B") private Double TC_PSB13B;

    // --- 外部聯查關聯欄位 (如 LNT_FILE 促銷/IMA_FILE 料件屬性) ---
    @Column(name = "LNT04") private String LNT04;
    @Column(name = "IMA25") private String IMA25;
}