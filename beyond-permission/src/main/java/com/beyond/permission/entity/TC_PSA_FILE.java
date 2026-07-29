package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TC_PSA_FILE")
@IdClass(TC_PSA_FILE_ComposeKey.class)
public class TC_PSA_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 複合主鍵 (Composite Key) ====================
    // 加入 length = 50 避免 utf8mb4 下主鍵總長度超過 3072 bytes
    @Id
    @Column(name = "TC_PSAPLANT", length = 50)
    private String TC_PSAPLANT;

    @Id
    @Column(name = "TC_PSA01", length = 50)
    private String TC_PSA01;

    @Id
    @Column(name = "TC_PSA02", length = 50)
    private String TC_PSA02;

    @Id
    @Column(name = "TC_PSA03", length = 50)
    private String TC_PSA03;

    @Id
    @Temporal(TemporalType.DATE)
    @Column(name = "TC_PSA04")
    private Date TC_PSA04;

    // ==================== 資料庫欄位 (Persistent Fields) ====================
    private String TC_PSA05;
    private String TC_PSA06;
    private Integer TC_PSA07;
    private Double TC_PSA08;
    private Double TC_PSA09;
    private Double TC_PSA10;
    private Double TC_PSA11;
    private Double TC_PSA12;
    private String TC_PSA13;
    private String TC_PSA14;
    private String TC_PSA15;
    private String TC_PSA16;
    private String TC_PSA17;
    private String TC_PSA18;
    private Integer TC_PSA19;
    private Integer TC_PSA20;
    private String TC_PSA21;
    private String TC_PSA22;
    private String TC_PSA23;
    private String TC_PSA24;
    private String TC_PSA25;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date TC_PSA26;
    
    private String TC_PSA27;
    private Double TC_PSA28;
    private Double TC_PSA29;
    private String TC_PSA30;
    private String TC_PSA31;
    private String TC_PSA32;
    private String TC_PSA33;
    private String TC_PSA34;
    private String TC_PSA35;
    private String TC_PSA36;
    private String TC_PSAUSER;
    private String TC_PSAMODU;
    private String TC_PSAGRUP;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date TC_PSADate;
    
    private String TC_PSATIME;
    private String TC_PSAORIG;
    private String TC_PSAORIU;
    private String TC_PSALEGAL;
    private String TC_PSA37;
    private String TC_PSA38;
    private Integer TC_PSA39;
    private Double TC_PSA40;
    private Integer TC_PSA41;
    private Double TC_PSA42;
    private Double TC_PSA09A;
    private Double TC_PSA09B;
    private Double TC_PSA12A;
    private Double TC_PSA12B;

    private String TQA02;
    private String TC_PSC07;
    private String LPK04;
    private String LNT33;
    private String LNT09;
    private String OBA02;
    private String LNT06;
    private String LNT10;

    // ==================== 非持久化欄位 (Transient Fields) ====================
    @Transient
    private Double period_tc_psa09a;

    @Transient
    private Double cont_tc_psa09a;

    @Transient
    private Double period_gross_profit;

    @Transient
    private Double cont_gross_profit;

    @Transient
    private Double period_tc_psa07;

    @Transient
    private Double cont_tc_psa07;

    @Transient
    private Integer m_avg_price;

    @Transient
    private String age_level;

    @Transient
    private Integer rec_cnt;

    @Transient
    private Double TC_PSA08_1;

    @Transient
    private Double TC_PSA08_2;

    @Transient
    private Integer TC_PSA08_CNT1;

    @Transient
    private Integer TC_PSA08_CNT2;

    @Transient
    private String name;

    @Transient
    private Double point_base;

    @Transient
    private Double point;

    @Transient
    private Double total_point;

    @Transient
    private Double pre_point;

    @Transient
    private Double last_point;

    @Transient
    private String userid;

    @Transient
    private Double tc_psa09a_month_accu;

    @Transient
    private String floor;

    @Transient
    private Double tc_psa09a_last_year_month_accu;

    @Transient
    private Double tc_psa09a_year_accu;

    @Transient
    private Double tc_psa09a_last_year_year_accu;

    @Transient
    private Double tc_psa07_month_accu;

    @Transient
    private Double tc_psa07_last_year_month_accu;

    @Transient
    private Double tc_psa07_year_accu;

    @Transient
    private Double tc_psa07_last_year_year_accu;

    @Transient
    private Double day_tc_psa09a;

    @Transient
    private Double day_gross_profit;

    @Transient
    private Double day_ly_tc_psa09a;

    @Transient
    private Double day_tc_psa07;

    @Transient
    private Double month_accu_gross_profit;

    @Transient
    private Double month_accu_ly_tc_psa09a;

    @Transient
    private Double month_accu_tc_psa07;

    @Transient
    private Double year_accu_gross_profit;

    @Transient
    private Double year_accu_ly_tc_psa09a;

    @Transient
    private Double year_accu_tc_psa07;

    @Transient
    private String org_name;
}