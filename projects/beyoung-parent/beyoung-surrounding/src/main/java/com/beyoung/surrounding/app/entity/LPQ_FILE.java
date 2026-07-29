package com.beyoung.surrounding.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LPQ_FILE")
@IdClass(LPQ_FILE_ComposeKey.class)
@DynamicInsert
@DynamicUpdate
public class LPQ_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LPQ01", length = 50)
    private String lpq01;

    @Column(name = "LPQ02", length = 255)
    private String lpq02;

    @Id
    @Column(name = "LPQ03", length = 50)
    private String lpq03;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPQ04")
    private Date lpq04;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPQ05")
    private Date lpq05;

    @Column(name = "LPQ06", length = 255)
    private String lpq06;

    @Column(name = "LPQ07", length = 255)
    private String lpq07;

    @Column(name = "LPQ08", length = 255)
    private String lpq08;

    @Column(name = "LPQ09", length = 255)
    private String lpq09;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPQ10")
    private Date lpq10;

    @Column(name = "LPQ11", length = 255)
    private String lpq11;

    @Column(name = "LPQACTI", length = 10)
    private String lpqacti;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LPQCRAT")
    private Date lpqcrat;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPQDATE")
    private Date lpqdate;

    @Column(name = "LPQGRUP", length = 50)
    private String lpqgrup;

    @Column(name = "LPQMODU", length = 50)
    private String lpqmodu;

    @Column(name = "LPQUSER", length = 50)
    private String lpquser;

    @Column(name = "LPQORIU", length = 50)
    private String lpqoriu;

    @Column(name = "LPQORIG", length = 50)
    private String lpqorig;

    @Column(name = "LPQLEGAL", length = 50)
    private String lpqlegal;

    @Id
    @Column(name = "LPQPLANT", length = 50)
    private String lpqplant;

    @Column(name = "LPQPOS", length = 50)
    private String lpqpos;

    @Id
    @Column(name = "LPQ00", length = 50)
    private String lpq00;

    @Column(name = "LPQ12", length = 255)
    private String lpq12;

    @Id
    @Column(name = "LPQ13", length = 50)
    private String lpq13;

    @Column(name = "LPQ14")
    private Integer lpq14;

    @Column(name = "LPQ15", length = 255)
    private String lpq15;

    @Temporal(TemporalType.DATE)
    @Column(name = "LPQ16")
    private Date lpq16;

    @Column(name = "LPQ17", length = 255)
    private String lpq17;

    @Column(name = "LPQ18")
    private Integer lpq18;

    @Column(name = "LPQ19", length = 255)
    private String lpq19;

    @Column(name = "LPQ20", length = 255)
    private String lpq20;

    @Column(name = "TA_LPQ01", length = 255)
    private String taLpq01;

    @Transient
    private List<LPR_FILE> lprFile;
    
}