package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "POS2_RYC_FILE")
@Table(name = "RYC_FILE")
@IdClass(RYC_FILE_ComposeKey.class) 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RYC_FILE implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RYC00")
    private String RYC00;

    @Id
    @Column(name = "RYC01")
    private String RYC01;

    @Column(name = "RYC02")
    private String RYC02;

    @Column(name = "RYC04")
    private String RYC04;

    @Column(name = "RYC06")
    private String RYC06;

    @Column(name = "TA_RYC09")
    private String TA_RYC09;

    @Column(name = "TA_RYC11")
    private String TA_RYC11;

    @Column(name = "TA_RYC12")
    private String TA_RYC12;

    @Column(name = "RYCACTI")
    private String RYCACTI;
}