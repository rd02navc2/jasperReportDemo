package com.beyond.surrounding.app.entity;

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
@Table(name = "LQE_FILE")
@DynamicInsert
@DynamicUpdate
public class LQE_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LQE01", length = 50)
    private String lqe01;

    @Column(name = "LQE02", length = 255)
    private String lqe02;

    @Column(name = "LQE03", length = 255)
    private String lqe03;

    @Column(name = "LQE04", length = 255)
    private String lqe04;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE05")
    private Date lqe05;

    @Column(name = "LQE06", length = 255)
    private String lqe06;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE07")
    private Date lqe07;

    @Column(name = "LQE08")
    private Double lqe08;

    @Column(name = "LQE09", length = 255)
    private String lqe09;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE10")
    private Date lqe10;

    @Column(name = "LQE11", length = 255)
    private String lqe11;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE12")
    private Date lqe12;

    @Column(name = "LQE13", length = 255)
    private String lqe13;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE14")
    private Date lqe14;

    @Column(name = "LQE15", length = 255)
    private String lqe15;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE16")
    private Date lqe16;

    @Column(name = "LQE17", length = 255)
    private String lqe17;

    @Column(name = "LQE18", length = 255)
    private String lqe18;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE19")
    private Date lqe19;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE20")
    private Date lqe20;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE21")
    private Date lqe21;

    @Column(name = "LQEPOS", length = 50)
    private String lqepos;

    @Column(name = "LQE22", length = 255)
    private String lqe22;

    @Column(name = "LQE23")
    private Double lqe23;

    @Column(name = "LQE24", length = 255)
    private String lqe24;

    @Temporal(TemporalType.DATE)
    @Column(name = "LQE25")
    private Date lqe25;

    // ==========================================
    //  客製擴充 TA_ 欄位區
    // ==========================================
    @Column(name = "TA_LQE01")
    private Double taLqe01;

    @Column(name = "TA_LQE02")
    private Double taLqe02;

    @Column(name = "TA_LQE03", length = 255)
    private String taLqe03; // 已修正拼字：taLpq03 -> taLqe03

    @Column(name = "TA_LQE04", length = 255)
    private String taLqe04;

    @Temporal(TemporalType.DATE)
    @Column(name = "TA_LQE05")
    private Date taLqe05;

    @Column(name = "TA_LQE06", length = 255)
    private String taLqe06;

    @Column(name = "TA_LQE07", length = 255)
    private String taLqe07;

    @Column(name = "TA_LQE09", length = 255)
    private String taLqe09;
    
}