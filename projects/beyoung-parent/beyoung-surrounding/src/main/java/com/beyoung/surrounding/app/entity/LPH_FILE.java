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
@Builder
@Entity
@Table(name = "LPH_FILE")
@DynamicInsert
@DynamicUpdate
public class LPH_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPH01", length = 50)
    private String lph01;

    @Column(name = "LPH02", length = 255)
    private String lph02;

    @Column(name = "LPH03", length = 255)
    private String lph03;

    @Column(name = "LPH04", length = 255)
    private String lph04;

    @Column(name = "LPH05", length = 255)
    private String lph05;

    @Column(name = "LPH06", length = 255)
    private String lph06;

    @Column(name = "LPH07", length = 255)
    private String lph07;

    @Column(name = "LPH08")
    private Double lph08;

    @Column(name = "LPH09", length = 255)
    private String lph09;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPH10")
    private Date lph10;

    @Column(name = "LPH11")
    private Double lph11;

    @Column(name = "LPH12", length = 255)
    private String lph12;

    @Column(name = "LPH13", length = 255)
    private String lph13;

    @Column(name = "LPH14")
    private Double lph14;

    @Column(name = "LPH15")
    private Double lph15;

    @Column(name = "LPH16")
    private Double lph16;

    @Column(name = "LPH17", length = 255)
    private String lph17;

    @Column(name = "LPH18")
    private Double lph18;

    @Column(name = "LPH19")
    private Double lph19;

    @Column(name = "LPH20")
    private Double lph20;

    @Column(name = "LPH21", length = 255)
    private String lph21;

    @Column(name = "LPH22")
    private Double lph22;

    @Column(name = "LPH23")
    private Double lph23;

    @Column(name = "LPH24", length = 255)
    private String lph24;

    @Column(name = "LPH25", length = 255)
    private String lph25;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPH26")
    private Date lph26;

    @Column(name = "LPH27", length = 255)
    private String lph27;

    @Column(name = "LPH28")
    private Double lph28;

    @Column(name = "LPH29")
    private Double lph29;

    @Column(name = "LPHACTI", length = 10)
    private String lphacti;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LPHCRAT")
    private Date lphcrat;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPHDATE")
    private Date lphdate;

    @Column(name = "LPHGRUP", length = 50)
    private String lphgrup;

    @Column(name = "LPHMODU", length = 50)
    private String lphmodu;

    @Column(name = "LPHUSER", length = 50)
    private String lphuser;

    @Column(name = "LPHORIU", length = 50)
    private String lphoriu;

    @Column(name = "LPHORIG", length = 50)
    private String lphorig;

    @Column(name = "LPH30")
    private Double lph30;

    @Column(name = "LPH31", length = 255)
    private String lph31;

    @Column(name = "LPH32")
    private Double lph32;

    @Column(name = "LPH33")
    private Double lph33;

    @Column(name = "LPH34", length = 255)
    private String lph34;

    @Column(name = "LPH35")
    private Double lph35;

    @Column(name = "LPH36", length = 255)
    private String lph36;

    @Column(name = "LPH311", length = 255)
    private String lph311;

    @Column(name = "LPHPOS", length = 50)
    private String lphpos;

    @Column(name = "LPH37", length = 255)
    private String lph37;

    @Column(name = "LPH38")
    private Double lph38;

    @Column(name = "LPH39")
    private Double lph39;

    @Column(name = "LPH40", length = 255)
    private String lph40;

    @Column(name = "LPH41")
    private Double lph41;

    @Column(name = "LPH42")
    private Double lph42;

    @Column(name = "LPH43")
    private Double lph43;

    @Column(name = "LPH44")
    private Double lph44;

    @Column(name = "LPH45")
    private Double lph45;

    @Column(name = "LPH46", length = 255)
    private String lph46;

    @Column(name = "LPH47", length = 255)
    private String lph47;

    @Column(name = "LPH48", length = 255)
    private String lph48;

    @Column(name = "TA_LPH01")
    private Double taLph01;

    @Column(name = "TA_LPH02")
    private Double taLph02;

    @Column(name = "TA_LPH03")
    private Double taLph03;

    @Column(name = "TA_LPH04")
    private Double taLph04;

    public LPH_FILE() {
    }
    
}