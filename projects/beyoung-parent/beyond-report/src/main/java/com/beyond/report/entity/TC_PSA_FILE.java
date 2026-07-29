package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TC_PSA_FILE")
@IdClass(TC_PSA_FILE_ComposeKey.class)
public class TC_PSA_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tc_psaplant", length = 50)
    private String TC_PSAPLANT;

    @Id
    @Column(name = "tc_psa01", length = 50)
    private String TC_PSA01;

    @Id
    @Column(name = "tc_psa02", length = 50)
    private String TC_PSA02;

    @Id
    @Column(name = "tc_psa03", length = 50)
    private String TC_PSA03;

    @Id
    @Column(name = "tc_psa04")
    private Date TC_PSA04;

    @Column(length = 50)
    private String TC_PSA05;

    @Column(length = 50)
    private String TC_PSA06;

    private Integer TC_PSA07;
    private Double TC_PSA08;
    private Double TC_PSA09;
    private Double TC_PSA10;
    private Double TC_PSA11;
    private Double TC_PSA12;

    @Column(length = 50)
    private String TC_PSA13;

    @Column(length = 50)
    private String TC_PSA14;

    @Column(length = 50)
    private String TC_PSA15;

    @Column(length = 50)
    private String TC_PSA16;

    @Column(length = 50)
    private String TC_PSA17;

    @Column(length = 50)
    private String TC_PSA18;

    private Integer TC_PSA19;
    private Integer TC_PSA20;

    @Column(length = 50)
    private String TC_PSA21;

    @Column(length = 50)
    private String TC_PSA22;

    @Column(length = 50)
    private String TC_PSA23;

    @Column(length = 50)
    private String TC_PSA24;

    @Column(length = 50)
    private String TC_PSA25;

    private Date TC_PSA26;

    @Column(length = 50)
    private String TC_PSA27;

    private Double TC_PSA28;
    private Double TC_PSA29;

    @Column(length = 50)
    private String TC_PSA30;

    @Column(length = 50)
    private String TC_PSA31;

    @Column(length = 50)
    private String TC_PSA32;

    @Column(length = 50)
    private String TC_PSA33;

    @Column(length = 50)
    private String TC_PSA34;

    @Column(length = 50)
    private String TC_PSA35;

    @Column(length = 50)
    private String TC_PSA36;

    @Column(length = 50)
    private String TC_PSAUSER;

    @Column(length = 50)
    private String TC_PSAMODU;

    @Column(length = 50)
    private String TC_PSAGRUP;

    private Date TC_PSADate;

    @Column(length = 20)
    private String TC_PSATIME;

    @Column(length = 50)
    private String TC_PSAORIG;

    @Column(length = 50)
    private String TC_PSAORIU;

    @Column(length = 50)
    private String TC_PSALEGAL;

    @Column(length = 50)
    private String TC_PSA37;

    @Column(length = 50)
    private String TC_PSA38;

    private Integer TC_PSA39;
    private Double TC_PSA40;
    private Integer TC_PSA41;
    private Double TC_PSA42;
    private Double TC_PSA09A;
    private Double TC_PSA09B;
    private Double TC_PSA12A;
    private Double TC_PSA12B;

    @Column(length = 50)
    private String TQA02;

    @Column(length = 50)
    private String TC_PSC07;

    @Column(length = 50)
    private String LPK04;

    @Column(length = 50)
    private String LNT33;

    @Column(length = 50)
    private String LNT09;

    @Column(length = 50)
    private String OBA02;

    private Double LSM04;

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

    // Getter & Setter 保持原樣...
    public String getAge_level() {
        return age_level;
    }
    public void setAge_level(String age_level) {
        this.age_level = age_level;
    }
    public Integer getM_avg_price() {
        return m_avg_price;
    }
    public void setM_avg_price(Integer m_avg_price) {
        this.m_avg_price = m_avg_price;
    }
    public String getLNT09() {
        return LNT09;
    }
    public void setLNT09(String lNT09) {
        LNT09 = lNT09;
    }
    public Integer getTC_PSA08_CNT1() {
        return TC_PSA08_CNT1;
    }
    public void setTC_PSA08_CNT1(Integer tC_PSA08_CNT1) {
        TC_PSA08_CNT1 = tC_PSA08_CNT1;
    }
    public Integer getTC_PSA08_CNT2() {
        return TC_PSA08_CNT2;
    }
    public void setTC_PSA08_CNT2(Integer tC_PSA08_CNT2) {
        TC_PSA08_CNT2 = tC_PSA08_CNT2;
    }
    public String getOBA02() {
        return OBA02;
    }
    public void setOBA02(String oBA02) {
        OBA02 = oBA02;
    }
    public String getLPK04() {
        return LPK04;
    }
    public void setLPK04(String lPK04) {
        LPK04 = lPK04;
    }
    public Double getTotal_point() {
        return total_point;
    }
    public void setTotal_point(Double total_point) {
        this.total_point = total_point;
    }
    public Double getPre_point() {
        return pre_point;
    }
    public void setPre_point(Double pre_point) {
        this.pre_point = pre_point;
    }
    public Double getLast_point() {
        return last_point;
    }
    public void setLast_point(Double last_point) {
        this.last_point = last_point;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public Double getPoint() {
        return point;
    }
    public void setPoint(Double point) {
        this.point = point;
    }
    public Integer getRec_cnt() {
        return rec_cnt;
    }
    public void setRec_cnt(Integer rec_cnt) {
        this.rec_cnt = rec_cnt;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPoint_base() {
        return point_base;
    }
    public void setPoint_base(Double point_base) {
        this.point_base = point_base;
    }
    public String getTC_PSC07() {
        return TC_PSC07;
    }
    public void setTC_PSC07(String tC_PSC07) {
        TC_PSC07 = tC_PSC07;
    }
    public String getTQA02() {
        return TQA02;
    }
    public void setTQA02(String tQA02) {
        TQA02 = tQA02;
    }
    public String getTC_PSAPLANT() {
        return TC_PSAPLANT;
    }
    public void setTC_PSAPLANT(String tC_PSAPLANT) {
        TC_PSAPLANT = tC_PSAPLANT;
    }
    public String getTC_PSA01() {
        return TC_PSA01;
    }
    public void setTC_PSA01(String tC_PSA01) {
        TC_PSA01 = tC_PSA01;
    }
    public String getTC_PSA02() {
        return TC_PSA02;
    }
    public void setTC_PSA02(String tC_PSA02) {
        TC_PSA02 = tC_PSA02;
    }
    public String getTC_PSA03() {
        return TC_PSA03;
    }
    public void setTC_PSA03(String tC_PSA03) {
        TC_PSA03 = tC_PSA03;
    }
    public Date getTC_PSA04() {
        return TC_PSA04;
    }
    public void setTC_PSA04(Date tC_PSA04) {
        TC_PSA04 = tC_PSA04;
    }
    public String getTC_PSA05() {
        return TC_PSA05;
    }
    public void setTC_PSA05(String tC_PSA05) {
        TC_PSA05 = tC_PSA05;
    }
    public String getTC_PSA06() {
        return TC_PSA06;
    }
    public void setTC_PSA06(String tC_PSA06) {
        TC_PSA06 = tC_PSA06;
    }
    public Integer getTC_PSA07() {
        return TC_PSA07;
    }
    public void setTC_PSA07(Integer tC_PSA07) {
        TC_PSA07 = tC_PSA07;
    }
    public Double getTC_PSA08() {
        return TC_PSA08;
    }
    public void setTC_PSA08(Double tC_PSA08) {
        TC_PSA08 = tC_PSA08;
    }
    public Double getTC_PSA09() {
        return TC_PSA09;
    }
    public void setTC_PSA09(Double tC_PSA09) {
        TC_PSA09 = tC_PSA09;
    }
    public Double getTC_PSA10() {
        return TC_PSA10;
    }
    public void setTC_PSA10(Double tC_PSA10) {
        TC_PSA10 = tC_PSA10;
    }
    public Double getTC_PSA11() {
        return TC_PSA11;
    }
    public void setTC_PSA11(Double tC_PSA11) {
        TC_PSA11 = tC_PSA11;
    }
    public Double getTC_PSA12() {
        return TC_PSA12;
    }
    public void setTC_PSA12(Double tC_PSA12) {
        TC_PSA12 = tC_PSA12;
    }
    public String getTC_PSA13() {
        return TC_PSA13;
    }
    public void setTC_PSA13(String tC_PSA13) {
        TC_PSA13 = tC_PSA13;
    }
    public String getTC_PSA14() {
        return TC_PSA14;
    }
    public void setTC_PSA14(String tC_PSA14) {
        TC_PSA14 = tC_PSA14;
    }
    public String getTC_PSA15() {
        return TC_PSA15;
    }
    public void setTC_PSA15(String tC_PSA15) {
        TC_PSA15 = tC_PSA15;
    }
    public String getTC_PSA16() {
        return TC_PSA16;
    }
    public void setTC_PSA16(String tC_PSA16) {
        TC_PSA16 = tC_PSA16;
    }
    public String getTC_PSA17() {
        return TC_PSA17;
    }
    public void setTC_PSA17(String tC_PSA17) {
        TC_PSA17 = tC_PSA17;
    }
    public String getTC_PSA18() {
        return TC_PSA18;
    }
    public void setTC_PSA18(String tC_PSA18) {
        TC_PSA18 = tC_PSA18;
    }
    public Integer getTC_PSA19() {
        return TC_PSA19;
    }
    public void setTC_PSA19(Integer tC_PSA19) {
        TC_PSA19 = tC_PSA19;
    }
    public Integer getTC_PSA20() {
        return TC_PSA20;
    }
    public void setTC_PSA20(Integer tC_PSA20) {
        TC_PSA20 = tC_PSA20;
    }
    public String getTC_PSA21() {
        return TC_PSA21;
    }
    public void setTC_PSA21(String tC_PSA21) {
        TC_PSA21 = tC_PSA21;
    }
    public String getTC_PSA22() {
        return TC_PSA22;
    }
    public void setTC_PSA22(String tC_PSA22) {
        TC_PSA22 = tC_PSA22;
    }
    public String getTC_PSA23() {
        return TC_PSA23;
    }
    public void setTC_PSA23(String tC_PSA23) {
        TC_PSA23 = tC_PSA23;
    }
    public String getTC_PSA24() {
        return TC_PSA24;
    }
    public void setTC_PSA24(String tC_PSA24) {
        TC_PSA24 = tC_PSA24;
    }
    public String getTC_PSA25() {
        return TC_PSA25;
    }
    public void setTC_PSA25(String tC_PSA25) {
        TC_PSA25 = tC_PSA25;
    }
    public Date getTC_PSA26() {
        return TC_PSA26;
    }
    public void setTC_PSA26(Date tC_PSA26) {
        TC_PSA26 = tC_PSA26;
    }
    public String getTC_PSA27() {
        return TC_PSA27;
    }
    public void setTC_PSA27(String tC_PSA27) {
        TC_PSA27 = tC_PSA27;
    }
    public Double getTC_PSA28() {
        return TC_PSA28;
    }
    public void setTC_PSA28(Double tC_PSA28) {
        TC_PSA28 = tC_PSA28;
    }
    public Double getTC_PSA29() {
        return TC_PSA29;
    }
    public void setTC_PSA29(Double tC_PSA29) {
        TC_PSA29 = tC_PSA29;
    }
    public String getTC_PSA30() {
        return TC_PSA30;
    }
    public void setTC_PSA30(String tC_PSA30) {
        TC_PSA30 = tC_PSA30;
    }
    public String getTC_PSA31() {
        return TC_PSA31;
    }
    public void setTC_PSA31(String tC_PSA31) {
        TC_PSA31 = tC_PSA31;
    }
    public String getTC_PSA32() {
        return TC_PSA32;
    }
    public void setTC_PSA32(String tC_PSA32) {
        TC_PSA32 = tC_PSA32;
    }
    public String getTC_PSA33() {
        return TC_PSA33;
    }
    public void setTC_PSA33(String tC_PSA33) {
        TC_PSA33 = tC_PSA33;
    }
    public String getTC_PSA34() {
        return TC_PSA34;
    }
    public void setTC_PSA34(String tC_PSA34) {
        TC_PSA34 = tC_PSA34;
    }
    public String getTC_PSA35() {
        return TC_PSA35;
    }
    public void setTC_PSA35(String tC_PSA35) {
        TC_PSA35 = tC_PSA35;
    }
    public String getTC_PSA36() {
        return TC_PSA36;
    }
    public void setTC_PSA36(String tC_PSA36) {
        TC_PSA36 = tC_PSA36;
    }
    public String getTC_PSAUSER() {
        return TC_PSAUSER;
    }
    public void setTC_PSAUSER(String tC_PSAUSER) {
        TC_PSAUSER = tC_PSAUSER;
    }
    public String getTC_PSAMODU() {
        return TC_PSAMODU;
    }
    public void setTC_PSAMODU(String tC_PSAMODU) {
        TC_PSAMODU = tC_PSAMODU;
    }
    public String getTC_PSAGRUP() {
        return TC_PSAGRUP;
    }
    public void setTC_PSAGRUP(String tC_PSAGRUP) {
        TC_PSAGRUP = tC_PSAGRUP;
    }
    public Date getTC_PSADate() {
        return TC_PSADate;
    }
    public void setTC_PSADate(Date tC_PSADate) {
        this.TC_PSADate = tC_PSADate;
    }
    public String getTC_PSATIME() {
        return TC_PSATIME;
    }
    public void setTC_PSATIME(String tC_PSATIME) {
        TC_PSATIME = tC_PSATIME;
    }
    public String getTC_PSAORIG() {
        return TC_PSAORIG;
    }
    public void setTC_PSAORIG(String tC_PSAORIG) {
        TC_PSAORIG = tC_PSAORIG;
    }
    public String getTC_PSAORIU() {
        return TC_PSAORIU;
    }
    public void setTC_PSAORIU(String tC_PSAORIU) {
        TC_PSAORIU = tC_PSAORIU;
    }
    public String getTC_PSALEGAL() {
        return TC_PSALEGAL;
    }
    public void setTC_PSALEGAL(String tC_PSALEGAL) {
        TC_PSALEGAL = tC_PSALEGAL;
    }
    public String getTC_PSA37() {
        return TC_PSA37;
    }
    public void setTC_PSA37(String tC_PSA37) {
        TC_PSA37 = tC_PSA37;
    }
    public String getTC_PSA38() {
        return TC_PSA38;
    }
    public void setTC_PSA38(String tC_PSA38) {
        TC_PSA38 = tC_PSA38;
    }
    public Integer getTC_PSA39() {
        return TC_PSA39;
    }
    public void setTC_PSA39(Integer tC_PSA39) {
        TC_PSA39 = tC_PSA39;
    }
    public Double getTC_PSA40() {
        return TC_PSA40;
    }
    public void setTC_PSA40(Double tC_PSA40) {
        TC_PSA40 = tC_PSA40;
    }
    public Integer getTC_PSA41() {
        return TC_PSA41;
    }
    public void setTC_PSA41(Integer tC_PSA41) {
        TC_PSA41 = tC_PSA41;
    }
    public Double getTC_PSA42() {
        return TC_PSA42;
    }
    public void setTC_PSA42(Double tC_PSA42) {
        TC_PSA42 = tC_PSA42;
    }
    public Double getTC_PSA09A() {
        return TC_PSA09A;
    }
    public void setTC_PSA09A(Double tC_PSA09A) {
        TC_PSA09A = tC_PSA09A;
    }
    public Double getTC_PSA09B() {
        return TC_PSA09B;
    }
    public void setTC_PSA09B(Double tC_PSA09B) {
        TC_PSA09B = tC_PSA09B;
    }
    public Double getTC_PSA12A() {
        return TC_PSA12A;
    }
    public void setTC_PSA12A(Double tC_PSA12A) {
        TC_PSA12A = tC_PSA12A;
    }
    public Double getTC_PSA12B() {
        return TC_PSA12B;
    }
    public void setTC_PSA12B(Double tC_PSA12B) {
        TC_PSA12B = tC_PSA12B;
    }
    public String getLNT33() {
        return LNT33;
    }
    public void setLNT33(String lNT33) {
        LNT33 = lNT33;
    }
    public Double getTC_PSA08_1() {
        return TC_PSA08_1;
    }
    public void setTC_PSA08_1(Double tC_PSA08_1) {
        TC_PSA08_1 = tC_PSA08_1;
    }
    public Double getTC_PSA08_2() {
        return TC_PSA08_2;
    }
    public void setTC_PSA08_2(Double tC_PSA08_2) {
        TC_PSA08_2 = tC_PSA08_2;
    }
    public Double getLSM04() {
        return LSM04;
    }
    public void setLSM04(Double lSM04) {
        LSM04 = lSM04;
    }
}