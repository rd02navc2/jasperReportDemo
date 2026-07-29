package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "BET_POINT_LOG")
@IdClass(BET_POINT_LOG_ComposeKey.class)
public class BET_POINT_LOG implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 50)
    private String tc_psaplant;

    @Id
    @Column(length = 50)
    private String tc_psa01;

    @Id
    @Column(length = 50)
    private String tc_psa02;

    @Id
    @Column(length = 50)
    private String tc_psa03;

    @Id
    private Date tc_psa04;

    private Double tc_psa12;
    private String tc_psa13;
    private String tc_psa16;
    private String tc_psa17;
    private Double tc_psa40;

    private String tc_psc07;
    private String userid;
    private Double point_base;
    private Double point;
    private Date access_date;
    private String access_id;
    private String counter_name;
    private String user_name;
    private String is_show;
    
    public String getTc_psaplant() {
        return tc_psaplant;
    }
    public void setTc_psaplant(String tc_psaplant) {
        this.tc_psaplant = tc_psaplant;
    }
    public String getTc_psa01() {
        return tc_psa01;
    }
    public void setTc_psa01(String tc_psa01) {
        this.tc_psa01 = tc_psa01;
    }
    public String getTc_psa02() {
        return tc_psa02;
    }
    public void setTc_psa02(String tc_psa02) {
        this.tc_psa02 = tc_psa02;
    }
    public String getTc_psa03() {
        return tc_psa03;
    }
    public void setTc_psa03(String tc_psa03) {
        this.tc_psa03 = tc_psa03;
    }
    public Date getTc_psa04() {
        return tc_psa04;
    }
    public void setTc_psa04(Date tc_psa04) {
        this.tc_psa04 = tc_psa04;
    }
    public Double getTc_psa12() {
        return tc_psa12;
    }
    public void setTc_psa12(Double tc_psa12) {
        this.tc_psa12 = tc_psa12;
    }
    public String getTc_psa13() {
        return tc_psa13;
    }
    public void setTc_psa13(String tc_psa13) {
        this.tc_psa13 = tc_psa13;
    }
    public String getTc_psa16() {
        return tc_psa16;
    }
    public void setTc_psa16(String tc_psa16) {
        this.tc_psa16 = tc_psa16;
    }
    public String getTc_psa17() {
        return tc_psa17;
    }
    public void setTc_psa17(String tc_psa17) {
        this.tc_psa17 = tc_psa17;
    }

    public String getTc_psc07() {
        return tc_psc07;
    }
    public void setTc_psc07(String tc_psc07) {
        this.tc_psc07 = tc_psc07;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public Double getPoint_base() {
        return point_base;
    }
    public void setPoint_base(Double point_base) {
        this.point_base = point_base;
    }
    public Double getPoint() {
        return point;
    }
    public void setPoint(Double point) {
        this.point = point;
    }
    public Date getAccess_date() {
        return access_date;
    }
    public void setAccess_date(Date access_date) {
        this.access_date = access_date;
    }
    public String getAccess_id() {
        return access_id;
    }
    public void setAccess_id(String access_id) {
        this.access_id = access_id;
    }
    public String getCounter_name() {
        return counter_name;
    }
    public void setCounter_name(String counter_name) {
        this.counter_name = counter_name;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getIs_show() {
        return is_show;
    }
    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }
    public Double getTc_psa40() {
        return tc_psa40;
    }
    public void setTc_psa40(Double tc_psa40) {
        this.tc_psa40 = tc_psa40;
    }
}