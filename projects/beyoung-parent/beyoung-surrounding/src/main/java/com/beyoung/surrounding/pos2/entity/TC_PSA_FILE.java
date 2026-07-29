package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity(name = "POS2_TC_PSA_FILE")
@Table(name = "TC_PSA_FILE")
@IdClass(TC_PSA_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_PSA_FILE implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TC_PSAPLANT")
    private String tcPsaPlant;

    @Id
    @Column(name = "TC_PSA01")
    private String tcPsa01;

    @Id
    @Column(name = "TC_PSA02")
    private String tcPsa02;

    @Id
    @Column(name = "TC_PSA03")
    private String tcPsa03;

    @Id
    @Column(name = "TC_PSA04")
    private String tcPsa04;

    @Column(name = "TC_PSA05") private String tcPsa05;
    @Column(name = "TC_PSA06") private String tcPsa06;
    @Column(name = "TC_PSA07") private Integer tcPsa07;
    @Column(name = "TC_PSA08") private Double tcPsa08;
    @Column(name = "TC_PSA09") private Double tcPsa09;
    @Column(name = "TC_PSA10") private Double tcPsa10;
    @Column(name = "TC_PSA11") private Double tcPsa11;
    @Column(name = "TC_PSA12") private Double tcPsa12;
    @Column(name = "TC_PSA13") private String tcPsa13;
    @Column(name = "TC_PSA14") private String tcPsa14;
    @Column(name = "TC_PSA15") private String tcPsa15;
    @Column(name = "TC_PSA16") private String tcPsa16;
    @Column(name = "TC_PSA17") private String tcPsa17;
    @Column(name = "TC_PSA18") private String tcPsa18;
    @Column(name = "TC_PSA19") private Integer tcPsa19;
    @Column(name = "TC_PSA20") private Integer tcPsa20;
    @Column(name = "TC_PSA21") private String tcPsa21;
    @Column(name = "TC_PSA22") private String tcPsa22;
    @Column(name = "TC_PSA23") private String tcPsa23;
    @Column(name = "TC_PSA24") private String tcPsa24;
    @Column(name = "TC_PSA25") private String tcPsa25;
    @Column(name = "TC_PSA26") private String tcPsa26;
    @Column(name = "TC_PSA27") private String tcPsa27;
    @Column(name = "TC_PSA28") private Double tcPsa28;
    @Column(name = "TC_PSA29") private Double tcPsa29;
    @Column(name = "TC_PSA30") private String tcPsa30;
    @Column(name = "TC_PSA31") private String tcPsa31;
    @Column(name = "TC_PSA32") private String tcPsa32;
    @Column(name = "TC_PSA33") private String tcPsa33;
    @Column(name = "TC_PSA34") private String tcPsa34;
    @Column(name = "TC_PSA35") private String tcPsa35;
    @Column(name = "TC_PSA36") private String tcPsa36;
    @Column(name = "TC_PSAUSER") private String tcPsaUser;
    @Column(name = "TC_PSAMODU") private String tcPsaModu;
    @Column(name = "TC_PSAGRUP") private String tcPsaGrup;
    @Column(name = "TC_PSADate") private String tcPsaDate;
    @Column(name = "TC_PSATIME") private String tcPsaTime;
    @Column(name = "TC_PSAORIG") private String tcPsaOrig;
    @Column(name = "TC_PSAORIU") private String tcPsaOriu;
    @Column(name = "TC_PSALEGAL") private String tcPsaLegal;
    @Column(name = "TC_PSA37") private String tcPsa37;
    @Column(name = "TC_PSA38") private String tcPsa38;
    @Column(name = "TC_PSA39") private Integer tcPsa39;
    @Column(name = "TC_PSA40") private Double tcPsa40;
    @Column(name = "TC_PSA41") private Integer tcPsa41;
    @Column(name = "TC_PSA42") private Double tcPsa42;
    @Column(name = "TC_PSA09A") private Double tcPsa09A;
    @Column(name = "TC_PSA09B") private Double tcPsa09B;
    @Column(name = "TC_PSA12A") private Double tcPsa12A;
    @Column(name = "TC_PSA12B") private Double tcPsa12B;
    
    @Column(name = "TQA02") private String tqa02;
}