package com.beyond.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TC_PSB_FILE")
@IdClass(TC_PSB_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class TC_PSB_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================================
    //  複合主鍵定義區 (6 個 Primary Keys)
    // ==========================================
    @Id
    @Column(name = "TC_PSBPLANT", length = 50)
    private String tcPsbplant;

    @Id
    @Column(name = "TC_PSB01", length = 50)
    private String tcPsb01;

    @Id
    @Column(name = "TC_PSB02", length = 50)
    private String tcPsb02;

    @Id
    @Column(name = "TC_PSB03", length = 50)
    private String tcPsb03;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSB04")
    private Date tcPsb04;

    @Id
    @Column(name = "TC_PSB06")
    private Integer tcPsb06;

    // ==========================================
    //  一般商務欄位定義區
    // ==========================================
    @Column(name = "TC_PSB05", length = 255)
    private String tcPsb05;

    @Column(name = "TC_PSB07", length = 255)
    private String tcPsb07;

    @Column(name = "TC_PSB08", length = 255)
    private String tcPsb08;

    @Column(name = "TC_PSB09")
    private Integer tcPsb09;

    @Column(name = "TC_PSB10")
    private Double tcPsb10;

    @Column(name = "TC_PSB11")
    private Double tcPsb11;

    @Column(name = "TC_PSB12")
    private Double tcPsb12;

    @Column(name = "TC_PSB13")
    private Double tcPsb13;

    @Column(name = "TC_PSB14")
    private Double tcPsb14;

    @Column(name = "TC_PSB15", length = 255)
    private String tcPsb15;

    @Column(name = "TC_PSB16", length = 255)
    private String tcPsb16;

    @Column(name = "TC_PSB17", length = 255)
    private String tcPsb17;

    @Column(name = "TC_PSB18", length = 255)
    private String tcPsb18;

    @Column(name = "TC_PSB19", length = 255)
    private String tcPsb19;

    @Column(name = "TC_PSB20")
    private Double tcPsb20;

    @Column(name = "TC_PSB21", length = 255)
    private String tcPsb21;

    @Column(name = "TC_PSB22")
    private Integer tcPsb22;

    @Column(name = "TC_PSB23")
    private Double tcPsb23;

    @Column(name = "TC_PSB13A")
    private Double tcPsb13A;

    @Column(name = "TC_PSB13B")
    private Double tcPsb13B;

    // ==========================================
    //  外部關聯/擴充自訂欄位
    // ==========================================
    @Column(name = "LNT04", length = 255)
    private String lnt04;

    @Column(name = "IMA25", length = 255)
    private String ima25;
    
}