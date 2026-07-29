package com.beyoung.surrounding.app.entity;

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
@Table(name = "TC_PSC_FILE")
@IdClass(TC_PSC_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class TC_PSC_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==========================================
    //  複合主鍵定義區 (7 個 Primary Keys)
    // ==========================================
    @Id
    @Column(name = "TC_PSCPLANT", length = 50)
    private String tcPscplant;

    @Id
    @Column(name = "TC_PSC01", length = 50)
    private String tcPsc01;

    @Id
    @Column(name = "TC_PSC02", length = 50)
    private String tcPsc02;

    @Id
    @Column(name = "TC_PSC03", length = 50)
    private String tcPsc03;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSC04")
    private Date tcPsc04;

    @Id
    @Column(name = "TC_PSC05", length = 50)
    private String tcPsc05;

    @Id
    @Column(name = "TC_PSC07", length = 50)
    private String tcPsc07;

    // ==========================================
    //  一般商務欄位定義區 (TC_PSC06 ~ TC_PSC22)
    // ==========================================
    @Column(name = "TC_PSC06", length = 255)
    private String tcPsc06;

    @Column(name = "TC_PSC08")
    private Double tcPsc08;

    @Column(name = "TC_PSC09", length = 255)
    private String tcPsc09;

    @Column(name = "TC_PSC10")
    private Integer tcPsc10;

    @Column(name = "TC_PSC11")
    private Integer tcPsc11;

    @Column(name = "TC_PSC13", length = 255)
    private String tcPsc13;

    @Column(name = "TC_PSC14")
    private Integer tcPsc14;

    @Column(name = "TC_PSC15", length = 255)
    private String tcPsc15;

    @Column(name = "TC_PSC16", length = 255)
    private String tcPsc16;

    @Column(name = "TC_PSC17", length = 255)
    private String tcPsc17;

    @Column(name = "TC_PSC18", length = 255)
    private String tcPsc18;

    @Column(name = "TC_PSC19", length = 255)
    private String tcPsc19;

    @Column(name = "TC_PSC20", length = 255)
    private String tcPsc20;

    @Column(name = "TC_PSC21", length = 255)
    private String tcPsc21;

    @Column(name = "TC_PSC22", length = 255)
    private String tcPsc22;

    // ==========================================
    //  外部參考或附加欄位還原
    // ==========================================
    @Column(name = "TQA02", length = 255)
    private String tqa02;

    @Column(name = "TC_PSA13", length = 255)
    private String tcPsa13;
    
}