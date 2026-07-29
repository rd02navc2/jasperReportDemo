package com.beyoung.bonus.infrastructure;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LSM_FILE") // 調整為全大寫
@IdClass(LsmFileId.class)
@DynamicInsert
public class LsmFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LSM01", length = 50)
    private String lsm01;

    @Id
    @Column(name = "LSM02", length = 10)
    @Builder.Default
    private String lsm02 = "2";

    @Id
    @Column(name = "LSM03", length = 50)
    private String lsm03;

    @Column(name = "LSM04")
    private Double lsm04;

    @Column(name = "LSM05")
    private LocalDateTime lsm05;

    @Column(name = "LSM06")
    private LocalDate lsm06;

    @Column(name = "LSM08")
    private Double lsm08;	// 消費金額

    @Column(name = "LSMLEGAL", length = 10)
    private String lsmlegal;

    @Column(name = "LSMPLANT", length = 10)
    private String lsmplant;

    @Column(name = "LSM09")
    private Integer lsm09;

    @Column(name = "LSM10")
    private Double lsm10;

    @Column(name = "LSM11")
    private Double lsm11;

    @Column(name = "LSM12")
    private Double lsm12;

    @Column(name = "LSM13")
    private Double lsm13;

    @Column(name = "LSM15", length = 5)
    private String lsm15;

    @Column(name = "LSMSTORE", length = 20)
    private String lsmstore;
  
    @Column(name = "TA_LSM01", length = 255)
    private String taLsm01;

    @Column(name = "TA_LSM02", length = 50)
    private String taLsm02;

    @Column(name = "TA_LSM03", length = 50)
    private String taLsm03;

    @Column(name = "TA_LSM04", length = 50)
    private String taLsm04;

    @Column(name = "TA_LSM05")
    private LocalDateTime taLsm05;

    @Column(name = "TA_LSM06")
    private Double taLsm06;

    @Column(name = "TA_LSM07")
    private Double taLsm07;

    @Column(name = "TA_LSM08")
    private Double taLsm08;
    
    @Column(name = "TA_LSM09")
    private Double taLsm09;

    @Column(name = "TA_LSM12")
    private Double taLsm12;

    @Column(name = "TA_LSM13", length = 50)
    private String taLsm13;
}