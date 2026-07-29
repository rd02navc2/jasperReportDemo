package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity(name = "POS2_TC_PSB_FILE")
@Table(name = "TC_PSB_FILE")
@IdClass(TC_PSB_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_PSB_FILE implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TC_PSBPLANT")
    private String tcPsbPlant;

    @Id
    @Column(name = "TC_PSB01")
    private String tcPsb01;

    @Id
    @Column(name = "TC_PSB02")
    private String tcPsb02;

    @Id
    @Column(name = "TC_PSB03")
    private String tcPsb03;

    @Id
    @Column(name = "TC_PSB04")
    private String tcPsb04;

    @Id
    @Column(name = "TC_PSB06")
    private Integer tcPsb06;

    @Column(name = "TC_PSB05") private String tcPsb05;
    @Column(name = "TC_PSB07") private String tcPsb07;
    @Column(name = "TC_PSB08") private String tcPsb08;
    @Column(name = "TC_PSB09") private Integer tcPsb09;
    @Column(name = "TC_PSB10") private Double tcPsb10;
    @Column(name = "TC_PSB11") private Double tcPsb11;
    @Column(name = "TC_PSB12") private Double tcPsb12;
    @Column(name = "TC_PSB13") private Double tcPsb13;
    @Column(name = "TC_PSB14") private Double tcPsb14;
    @Column(name = "TC_PSB15") private String tcPsb15;
    @Column(name = "TC_PSB16") private String tcPsb16;
    @Column(name = "TC_PSB17") private String tcPsb17;
    @Column(name = "TC_PSB18") private String tcPsb18;
    @Column(name = "TC_PSB19") private String tcPsb19;
    @Column(name = "TC_PSB20") private Double tcPsb20;
    @Column(name = "TC_PSB21") private String tcPsb21;
    @Column(name = "TC_PSB22") private Double tcPsb22;
    @Column(name = "TC_PSB23") private Double tcPsb23;
    @Column(name = "TC_PSB13A") private Double tcPsb13A;
    @Column(name = "TC_PSB13B") private Double tcPsb13B;
    
    @Column(name = "LNT04") private String lnt04;
    @Column(name = "IMA25") private String ima25;
}