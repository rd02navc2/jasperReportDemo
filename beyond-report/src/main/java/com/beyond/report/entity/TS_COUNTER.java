package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TS_COUNTER")
public class TS_COUNTER implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Temporal(TemporalType.DATE) // 或 TemporalType.TIMESTAMP，視資料庫欄位類型而定
    @Column(name = "LPJ04")
    private Date LPJ04;

    private Integer COUNTER_ALL;
    private Integer COUNTER_BEYOND;
    private Integer COUNTER_NON_BEYOND;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ACCESS_DATE;

    @Transient
    private Integer rec_cnt;

    public Date getLPJ04() {
        return LPJ04;
    }
    public void setLPJ04(Date lPJ04) {
        LPJ04 = lPJ04;
    }
    public Integer getCOUNTER_ALL() {
        return COUNTER_ALL;
    }
    public void setCOUNTER_ALL(Integer cOUNTER_ALL) {
        COUNTER_ALL = cOUNTER_ALL;
    }
    public Integer getCOUNTER_BEYOND() {
        return COUNTER_BEYOND;
    }
    public void setCOUNTER_BEYOND(Integer cOUNTER_BEYOND) {
        COUNTER_BEYOND = cOUNTER_BEYOND;
    }
    public Integer getCOUNTER_NON_BEYOND() {
        return COUNTER_NON_BEYOND;
    }
    public void setCOUNTER_NON_BEYOND(Integer cOUNTER_NON_BEYOND) {
        COUNTER_NON_BEYOND = cOUNTER_NON_BEYOND;
    }
    public Date getACCESS_DATE() {
        return ACCESS_DATE;
    }
    public void setACCESS_DATE(Date aCCESS_DATE) {
        ACCESS_DATE = aCCESS_DATE;
    }
    public Integer getRec_cnt() {
        return rec_cnt;
    }
    public void setRec_cnt(Integer rec_cnt) {
        this.rec_cnt = rec_cnt;
    }
}