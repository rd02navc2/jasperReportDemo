package com.beyoung.surrounding.rms.entity;

import java.io.Serializable;
import java.util.Date;

//  核心修正：全面改用 Spring Boot 3 的 jakarta.persistence 命名空間
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "SURROUNDING_ACCESS_LOG")
public class SurroundingAccessLog implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  明確宣告自動遞增策略 (根據 DB 實際狀況可選 IDENTITY 或 SEQUENCE)
    @Column(name = "SN")
    private Integer sn;

    @Column(name = "ACCESS_DATE")
    private Date access_date;

    @Column(name = "ACCESS_IP", length = 50)
    private String access_ip;

    @Column(name = "C_NO", length = 50)
    private String c_no;

    @Column(name = "URL", length = 500)
    private String url;

    //  自動化優化：在 Log 寫入資料庫前，如果時間為空，自動填入當前系統時間
    @PrePersist
    protected void onCreate() {
        if (this.access_date == null) {
            this.access_date = new Date();
        }
    }

    public SurroundingAccessLog() {
    }

    // --- Getter and Setter ---
    public Integer getSn() {
        return sn;
    }
    public void setSn(Integer sn) {
        this.sn = sn;
    }
    public Date getAccess_date() {
        return access_date;
    }
    public void setAccess_date(Date access_date) {
        this.access_date = access_date;
    }
    public String getAccess_ip() {
        return access_ip;
    }
    public void setAccess_ip(String access_ip) {
        this.access_ip = access_ip;
    }
    public String getC_no() {
        return c_no;
    }
    public void setC_no(String c_no) {
        this.c_no = c_no;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}