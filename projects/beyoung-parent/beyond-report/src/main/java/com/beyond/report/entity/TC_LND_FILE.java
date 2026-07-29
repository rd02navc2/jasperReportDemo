package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TC_LND_FILE")
@IdClass(TC_LND_FILE_ComposeKey.class)
public class TC_LND_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TC_LND01", length = 50)
    private String TC_LND01;

    @Id
    @Column(name = "TC_LND02")
    private Integer TC_LND02;

    @Column(length = 50)
    private String TC_LND03;

    @Column(length = 50)
    private String TC_LND04;

    private Double TC_LND07;
    private Date TC_LND10;
    private Date TC_LND11;

    @Column(length = 50)
    private String TC_LND12;

    public String getTC_LND01() {
        return TC_LND01;
    }
    public void setTC_LND01(String tC_LND01) {
        TC_LND01 = tC_LND01;
    }
    public Integer getTC_LND02() {
        return TC_LND02;
    }
    public void setTC_LND02(Integer tC_LND02) {
        TC_LND02 = tC_LND02;
    }
    public String getTC_LND03() {
        return TC_LND03;
    }
    public void setTC_LND03(String tC_LND03) {
        TC_LND03 = tC_LND03;
    }
    public String getTC_LND04() {
        return TC_LND04;
    }
    public void setTC_LND04(String tC_LND04) {
        TC_LND04 = tC_LND04;
    }
    public Double getTC_LND07() {
        return TC_LND07;
    }
    public void setTC_LND07(Double tC_LND07) {
        TC_LND07 = tC_LND07;
    }
    public Date getTC_LND10() {
        return TC_LND10;
    }
    public void setTC_LND10(Date tC_LND10) {
        TC_LND10 = tC_LND10;
    }
    public Date getTC_LND11() {
        return TC_LND11;
    }
    public void setTC_LND11(Date tC_LND11) {
        TC_LND11 = tC_LND11;
    }
    public String getTC_LND12() {
        return TC_LND12;
    }
    public void setTC_LND12(String tC_LND12) {
        TC_LND12 = tC_LND12;
    }
}