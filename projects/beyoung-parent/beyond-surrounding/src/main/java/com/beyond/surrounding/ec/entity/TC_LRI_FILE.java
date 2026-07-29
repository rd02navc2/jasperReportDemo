package com.beyond.surrounding.ec.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "EcTcLriFile")
@Table(name = "TC_LRI_FILE")
@IdClass(TC_LRI_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor  // JPA 必須要有無參構造函數
@AllArgsConstructor
@Builder
public class TC_LRI_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TC_LRI01")
    private String TC_LRI01;

    @Id
    @Column(name = "TC_LRI02")
    private String TC_LRI02;

    @Id
    @Column(name = "TC_LRI03")
    private Double TC_LRI03;

    @Id
    @Column(name = "TC_LRIPLANT")
    private String TC_LRIPLANT;

    @Column(name = "TC_LRI04")
    private String TC_LRI04;

    @Column(name = "TC_LRI05")
    private String TC_LRI05;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TC_LRI06")
    private Date TC_LRI06;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TC_LRI07")
    private Date TC_LRI07;

    @Column(name = "TC_LRI08")
    private String TC_LRI08;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TC_LRI09")
    private Date TC_LRI09;

    @Column(name = "TC_LRI10")
    private String TC_LRI10;

    @Column(name = "TC_LRICONF")
    private String TC_LRICONF; 

    @Column(name = "TC_LRICONU")
    private String TC_LRICONU; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TC_LRICOND")
    private Date LRICOND; 

    @Column(name = "TC_LRIACTI")
    private String TC_LRIACTI; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TC_LRICRAT")
    private Date TC_LRICRAT; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TC_LRIDATE")
    private Date TC_LRIDATE; 

    @Column(name = "TC_LRIGRUP")
    private String TC_LRIGRUP; 

    @Column(name = "TC_LRILEGAL")
    private String TC_LRILEGAL; 

    @Column(name = "TC_LRIMODU")
    private String TC_LRIMODU; 

    @Column(name = "TC_LRIORIG")
    private String TC_LRIORIG; 

    @Column(name = "TC_LRIORIU")
    private String TC_LRIORIU;

    @Column(name = "TC_LRIUSER")
    private String TC_LRIUSER;
    
}