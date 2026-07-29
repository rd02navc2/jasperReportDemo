package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity(name = "POS2_TC_PSC_FILE")
@Table(name = "TC_PSC_FILE")
@IdClass(TC_PSC_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_PSC_FILE implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TC_PSCPLANT")
    private String tcPscPlant;

    @Id
    @Column(name = "TC_PSC01")
    private String tcPsc01;

    @Id
    @Column(name = "TC_PSC02")
    private String tcPsc02;

    @Id
    @Column(name = "TC_PSC03")
    private String tcPsc03;

    @Id
    @Column(name = "TC_PSC04")
    private String tcPsc04; // 修正：與主鍵類別維持一致型態

    @Id
    @Column(name = "TC_PSC05")
    private String tcPsc05;

    @Id
    @Column(name = "TC_PSC07")
    private String tcPsc07;

    @Column(name = "TC_PSC06") private String tcPsc06;
    @Column(name = "TC_PSC08") private Double tcPsc08;
    @Column(name = "TC_PSC09") private String tcPsc09;
    @Column(name = "TC_PSC10") private Integer tcPsc10;
    @Column(name = "TC_PSC11") private Integer tcPsc11;
    @Column(name = "TC_PSC13") private String tcPsc13;
    @Column(name = "TC_PSC14") private Integer tcPsc14;
    @Column(name = "TC_PSC15") private String tcPsc15;
    @Column(name = "TC_PSC16") private String tcPsc16;
    @Column(name = "TC_PSC17") private String tcPsc17;
    @Column(name = "TC_PSC18") private String tcPsc18;
    @Column(name = "TC_PSC19") private String tcPsc19;
    @Column(name = "TC_PSC20") private String tcPsc20;
    @Column(name = "TC_PSC21") private String tcPsc21;
    @Column(name = "TC_PSC22") private String tcPsc22;
    
    @Column(name = "TQA02") private String tqa02;
    @Column(name = "TC_PSA13") private String tcPsa13;
}