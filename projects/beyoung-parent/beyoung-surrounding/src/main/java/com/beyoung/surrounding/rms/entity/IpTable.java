package com.beyoung.surrounding.rms.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * IP_TABLE 的複合主鍵類別 (對應 Jakarta EE 10 規範)
 */
public class IpTable implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String ip;
    private String c_no;

    // JPA 規範要求必須提供無參數建構子
    public IpTable() {
    }

    public IpTable(String ip, String c_no) {
        this.ip = ip;
        this.c_no = c_no;
    }

    // --- Getter and Setter ---
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getC_no() {
        return c_no;
    }
    public void setC_no(String c_no) {
        this.c_no = c_no;
    }

    //  修正：改用 Objects.equals 確保安全的字串內容比對，避免位址比對(==)帶來的併發與快取 Bug
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpTable that = (IpTable) o;
        return Objects.equals(ip, that.ip) && 
               Objects.equals(c_no, that.c_no);
    }

    //  修正：必須依據欄位內容計算雜湊值，以符合 JPA 實體快取與容器規範
    @Override
    public int hashCode() {
        return Objects.hash(ip, c_no);
    }
}