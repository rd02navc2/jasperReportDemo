package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "POS2_LQE_FILE")
@Table(name = "LQE_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LqeFile implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LQE01")
    private String LQE01;

    @Column(name = "LQE02")
    private String LQE02;

    @Column(name = "LQE03")
    private String LQE03;

    @Column(name = "LQE04")
    private String LQE04;

    @Column(name = "LQE05")
    private Date LQE05;

    @Column(name = "LQE06")
    private String LQE06;

    @Column(name = "LQE07")
    private Date LQE07;

    @Column(name = "LQE08")
    private Double LQE08;

    @Column(name = "LQE09")
    private String LQE09;

    @Column(name = "LQE10")
    private Date LQE10;

    @Column(name = "LQE11")
    private String LQE11;

    @Column(name = "LQE12")
    private Date LQE12;

    @Column(name = "LQE13")
    private String LQE13;

    @Column(name = "LQE14")
    private Date LQE14;

    @Column(name = "LQE15")
    private String LQE15;

    @Column(name = "LQE16")
    private Date LQE16;

    @Column(name = "LQE17")
    private String LQE17;

    @Column(name = "LQE18")
    private String LQE18;

    @Column(name = "LQE19")
    private Date LQE19;

    @Column(name = "LQE20")
    private Date LQE20;

    @Column(name = "LQE21")
    private Date LQE21;

    @Column(name = "LQEPOS")
    private String LQEPOS;

    @Column(name = "LQE22")
    private String LQE22;

    @Column(name = "LQE23")
    private Double LQE23;

    @Column(name = "LQE24")
    private String LQE24;

    @Column(name = "LQE25")
    private Date LQE25;

    @Column(name = "TA_LQE01")
    private Double TA_LQE01;

    @Column(name = "TA_LQE02")
    private Double TA_LQE02;

    @Column(name = "TA_LQE03")
    private String TA_LQE03;

    @Column(name = "TA_LQE04")
    private String TA_LQE04;

    @Column(name = "TA_LQE05")
    private Date TA_LQE05;

    @Column(name = "TA_LQE06")
    private String TA_LQE06;

    @Column(name = "TA_LQE07")
    private String TA_LQE07;

    @Column(name = "TA_LQE09")
    private String TA_LQE09;
}