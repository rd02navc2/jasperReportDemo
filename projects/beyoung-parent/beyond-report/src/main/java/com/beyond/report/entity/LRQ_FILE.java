package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "LRQ_FILE")
@IdClass(LRQ_FILE_ComposeKey.class)
public class LRQ_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 255)
    private String LRQ00;

    @Column(length = 255)
    private String LRQ01;

    // === 主鍵欄位加上長度限制 (例如 50)，解決 3072 bytes 超限問題 ===
    @Id
    @Column(length = 50, nullable = false)
    private String LRQ02;

    private Integer LRQ03;
    private Integer LRQ04;
    private Double LRQ05;
    private Double LRQ06;
    private Double LRQ07;
    private Double LRQ08;

    @Column(length = 255)
    private String LRQ09;

    private Date LRQ10;
    private Date LRQ11;

    @Column(length = 50)
    private String LRQACTI;

    @Id
    @Column(length = 50, nullable = false)
    private String LRQ12;

    @Id
    @Column(length = 50, nullable = false)
    private String LRQ13;

    @Column(length = 50)
    private String LRQLEGAL;

    @Id
    @Column(length = 50, nullable = false)
    private String LRQPLANT;

    // Getters and Setters
    public String getLRQ00() { return LRQ00; }
    public void setLRQ00(String lRQ00) { LRQ00 = lRQ00; }

    public String getLRQ01() { return LRQ01; }
    public void setLRQ01(String lRQ01) { LRQ01 = lRQ01; }

    public String getLRQ02() { return LRQ02; }
    public void setLRQ02(String lRQ02) { LRQ02 = lRQ02; }

    public Integer getLRQ03() { return LRQ03; }
    public void setLRQ03(Integer lRQ03) { LRQ03 = lRQ03; }

    public Integer getLRQ04() { return LRQ04; }
    public void setLRQ04(Integer lRQ04) { LRQ04 = lRQ04; }

    public Double getLRQ05() { return LRQ05; }
    public void setLRQ05(Double lRQ05) { LRQ05 = lRQ05; }

    public Double getLRQ06() { return LRQ06; }
    public void setLRQ06(Double lRQ06) { LRQ06 = lRQ06; }

    public Double getLRQ07() { return LRQ07; }
    public void setLRQ07(Double lRQ07) { LRQ07 = lRQ07; }

    public Double getLRQ08() { return LRQ08; }
    public void setLRQ08(Double lRQ08) { LRQ08 = lRQ08; }

    public String getLRQ09() { return LRQ09; }
    public void setLRQ09(String lRQ09) { LRQ09 = lRQ09; }

    public Date getLRQ10() { return LRQ10; }
    public void setLRQ10(Date lRQ10) { LRQ10 = lRQ10; }

    public Date getLRQ11() { return LRQ11; }
    public void setLRQ11(Date lRQ11) { LRQ11 = lRQ11; }

    public String getLRQACTI() { return LRQACTI; }
    public void setLRQACTI(String lRQACTI) { LRQACTI = lRQACTI; }

    public String getLRQ12() { return LRQ12; }
    public void setLRQ12(String lRQ12) { LRQ12 = lRQ12; }

    public String getLRQ13() { return LRQ13; }
    public void setLRQ13(String lRQ13) { LRQ13 = lRQ13; }

    public String getLRQLEGAL() { return LRQLEGAL; }
    public void setLRQLEGAL(String lRQLEGAL) { LRQLEGAL = lRQLEGAL; }

    public String getLRQPLANT() { return LRQPLANT; }
    public void setLRQPLANT(String lRQPLANT) { LRQPLANT = lRQPLANT; }

    public static long getSerialversionuid() { return serialVersionUID; }
}

/**
 * 複合主鍵類別
 */
class LRQ_FILE_ComposeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String LRQ12;
    private String LRQ13;
    private String LRQ02;
    private String LRQPLANT;

    public LRQ_FILE_ComposeKey() {}

    public LRQ_FILE_ComposeKey(String LRQ12, String LRQ13, String LRQ02, String LRQPLANT) {
        this.LRQ12 = LRQ12;
        this.LRQ13 = LRQ13;
        this.LRQ02 = LRQ02;
        this.LRQPLANT = LRQPLANT;
    }

    public String getLRQ12() { return LRQ12; }
    public void setLRQ12(String lRQ12) { LRQ12 = lRQ12; }

    public String getLRQ13() { return LRQ13; }
    public void setLRQ13(String lRQ13) { LRQ13 = lRQ13; }

    public String getLRQ02() { return LRQ02; }
    public void setLRQ02(String lRQ02) { LRQ02 = lRQ02; }

    public String getLRQPLANT() { return LRQPLANT; }
    public void setLRQPLANT(String lRQPLANT) { LRQPLANT = lRQPLANT; }

    // === 修正 equals 邏輯 (改用 Objects.equals 安全比較字串內容) ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRQ_FILE_ComposeKey that = (LRQ_FILE_ComposeKey) o;
        return Objects.equals(LRQ12, that.LRQ12) &&
               Objects.equals(LRQ13, that.LRQ13) &&
               Objects.equals(LRQ02, that.LRQ02) &&
               Objects.equals(LRQPLANT, that.LRQPLANT);
    }

    // === 正確計算 HashCode ===
    @Override
    public int hashCode() {
        return Objects.hash(LRQ12, LRQ13, LRQ02, LRQPLANT);
    }
}