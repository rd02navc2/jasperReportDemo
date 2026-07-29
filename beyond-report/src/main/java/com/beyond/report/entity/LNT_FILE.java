package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "LNT_FILE")
public class LNT_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 30)
    private String LNT01;

    @Column(length = 100)
    private String LNE06; // 廠商名稱

    @Column(length = 30)
    private String LNT02;

    private Date LNT03;

    @Column(length = 30)
    private String LNT04;

    @Column(length = 30)
    private String LNT05;

    @Column(length = 30)
    private String LNT06;

    @Column(length = 30)
    private String LNT07;

    @Column(length = 30)
    private String LNT08;

    @Column(length = 30)
    private String LNT09;

    private Double LNT10;
    private Double LNT11;

    @Column(length = 30)
    private String LNT12;

    @Column(length = 30)
    private String LNT13;

    private Double LNT14;

    @Column(length = 30)
    private String LNT15;

    @Column(length = 30)
    private String LNT16;

    private Date LNT17;
    private Date LNT18;
    private Date LNT19;
    private Date LNT20;
    private Date LNT21;
    private Date LNT22;
    private Date LNT23;

    @Column(length = 30)
    private String LNT24;

    @Column(length = 30)
    private String LNT25;

    @Column(length = 30)
    private String LNT26;

    @Column(length = 30)
    private String LNT27;

    private Date LNT28;

    @Column(length = 30)
    private String LNT29;

    @Column(length = 30)
    private String LNT30;

    @Column(length = 30)
    private String LNT31;

    @Column(length = 30)
    private String LNT32;

    @Column(length = 30)
    private String LNT33;

    @Column(length = 30)
    private String LNT34;

    @Column(length = 30)
    private String LNT35;

    private Double LNT36;

    @Column(length = 30)
    private String LNT37;

    @Column(length = 30)
    private String LNT38;

    private Integer LNT39;

    @Column(length = 30)
    private String LNT40;

    @Column(length = 30)
    private String LNT41;

    private Double LNT42;

    @Column(length = 30)
    private String LNT43;

    @Column(length = 30)
    private String LNT44;

    @Column(length = 30)
    private String LNT45;

    private Date LNT46;

    @Column(length = 30)
    private String LNT47;

    @Column(length = 30)
    private String LNT48;

    @Column(length = 30)
    private String LNT49;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String LNT50;

    private Integer LNT51;
    private Date LNT52;
    private Date LNT53;

    @Column(length = 10)
    private String LNTACTI;

    private Date LNTCRAT;
    private Date LNTDATE;

    @Column(length = 20)
    private String LNTGRUP;

    @Column(length = 20)
    private String LNTLEGAL;

    @Column(length = 20)
    private String LNTMODU;

    @Column(length = 20)
    private String LNTUSER;

    @Column(length = 20)
    private String LNTORIU;

    @Column(length = 20)
    private String LNTORIG;

    @Column(length = 20)
    private String LNTPLANT;

    @Column(length = 30)
    private String LNT54;

    @Column(length = 20)
    private String LNTPOS;

    @Column(length = 30)
    private String LNT55;

    @Column(length = 30)
    private String LNT56;

    @Column(length = 30)
    private String LNT57;

    @Column(length = 30)
    private String LNT58;

    @Column(length = 30)
    private String LNT59;

    @Column(length = 30)
    private String LNT60;

    private Double LNT61;

    @Column(length = 30)
    private String LNT62;

    @Column(length = 30)
    private String LNT63;

    private Double LNT64;
    private Double LNT65;
    private Double LNT66;
    private Double LNT67;
    private Double LNT68;
    private Double LNT69;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String LNT70;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String LNT71;

    private Integer LNT72;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String LNT73;

    private Double TA_LNT01;
    private Integer TA_LNT02;
    private Integer TA_LNT03;
    private Double TA_LNT04;

    @Column(length = 30)
    private String TA_LNT05;

    @Column(length = 30)
    private String TA_LNT06;

    @Column(length = 30)
    private String TA_LNT07;

    @Column(length = 30)
    private String TA_LNT08;

    private Double TA_LNT09;

    @Column(length = 30)
    private String TA_LNT10;

    @Column(length = 30)
    private String TA_LNT11;

    @Column(length = 30)
    private String TA_LNT12;

    @Column(length = 30)
    private String TA_LNT13;

    private Integer TA_LNT14;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String TA_LNT15;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String TQA02;

    private Double TC_PSA12;
    private Double TC_PSA40;
    private Date TC_PSA04;

    @Column(length = 30)
    private String TC_PSA05;

    @Column(length = 30)
    private String OBA01;

    @Column(length = 30)
    private String OBA02;

    private Double LNU04;

    @Column(length = 30)
    private String LNV01;

    // ==================== Getter & Setter ====================

    public String getLNT01() {
        return LNT01;
    }

    public void setLNT01(String LNT01) {
        this.LNT01 = LNT01;
    }

    public String getLNE06() {
        return LNE06;
    }

    public void setLNE06(String LNE06) {
        this.LNE06 = LNE06;
    }

    public String getLNT02() {
        return LNT02;
    }

    public void setLNT02(String LNT02) {
        this.LNT02 = LNT02;
    }

    public Date getLNT03() {
        return LNT03;
    }

    public void setLNT03(Date LNT03) {
        this.LNT03 = LNT03;
    }

    public String getLNT04() {
        return LNT04;
    }

    public void setLNT04(String LNT04) {
        this.LNT04 = LNT04;
    }

    public String getLNT05() {
        return LNT05;
    }

    public void setLNT05(String LNT05) {
        this.LNT05 = LNT05;
    }

    public String getLNT06() {
        return LNT06;
    }

    public void setLNT06(String LNT06) {
        this.LNT06 = LNT06;
    }

    public String getLNT07() {
        return LNT07;
    }

    public void setLNT07(String LNT07) {
        this.LNT07 = LNT07;
    }

    public String getLNT08() {
        return LNT08;
    }

    public void setLNT08(String LNT08) {
        this.LNT08 = LNT08;
    }

    public String getLNT09() {
        return LNT09;
    }

    public void setLNT09(String LNT09) {
        this.LNT09 = LNT09;
    }

    public Double getLNT10() {
        return LNT10;
    }

    public void setLNT10(Double LNT10) {
        this.LNT10 = LNT10;
    }

    public Double getLNT11() {
        return LNT11;
    }

    public void setLNT11(Double LNT11) {
        this.LNT11 = LNT11;
    }

    public String getLNT12() {
        return LNT12;
    }

    public void setLNT12(String LNT12) {
        this.LNT12 = LNT12;
    }

    public String getLNT13() {
        return LNT13;
    }

    public void setLNT13(String LNT13) {
        this.LNT13 = LNT13;
    }

    public Double getLNT14() {
        return LNT14;
    }

    public void setLNT14(Double LNT14) {
        this.LNT14 = LNT14;
    }

    public String getLNT15() {
        return LNT15;
    }

    public void setLNT15(String LNT15) {
        this.LNT15 = LNT15;
    }

    public String getLNT16() {
        return LNT16;
    }

    public void setLNT16(String LNT16) {
        this.LNT16 = LNT16;
    }

    public Date getLNT17() {
        return LNT17;
    }

    public void setLNT17(Date LNT17) {
        this.LNT17 = LNT17;
    }

    public Date getLNT18() {
        return LNT18;
    }

    public void setLNT18(Date LNT18) {
        this.LNT18 = LNT18;
    }

    public Date getLNT19() {
        return LNT19;
    }

    public void setLNT19(Date LNT19) {
        this.LNT19 = LNT19;
    }

    public Date getLNT20() {
        return LNT20;
    }

    public void setLNT20(Date LNT20) {
        this.LNT20 = LNT20;
    }

    public Date getLNT21() {
        return LNT21;
    }

    public void setLNT21(Date LNT21) {
        this.LNT21 = LNT21;
    }

    public Date getLNT22() {
        return LNT22;
    }

    public void setLNT22(Date LNT22) {
        this.LNT22 = LNT22;
    }

    public Date getLNT23() {
        return LNT23;
    }

    public void setLNT23(Date LNT23) {
        this.LNT23 = LNT23;
    }

    public String getLNT24() {
        return LNT24;
    }

    public void setLNT24(String LNT24) {
        this.LNT24 = LNT24;
    }

    public String getLNT25() {
        return LNT25;
    }

    public void setLNT25(String LNT25) {
        this.LNT25 = LNT25;
    }

    public String getLNT26() {
        return LNT26;
    }

    public void setLNT26(String LNT26) {
        this.LNT26 = LNT26;
    }

    public String getLNT27() {
        return LNT27;
    }

    public void setLNT27(String LNT27) {
        this.LNT27 = LNT27;
    }

    public Date getLNT28() {
        return LNT28;
    }

    public void setLNT28(Date LNT28) {
        this.LNT28 = LNT28;
    }

    public String getLNT29() {
        return LNT29;
    }

    public void setLNT29(String LNT29) {
        this.LNT29 = LNT29;
    }

    public String getLNT30() {
        return LNT30;
    }

    public void setLNT30(String LNT30) {
        this.LNT30 = LNT30;
    }

    public String getLNT31() {
        return LNT31;
    }

    public void setLNT31(String LNT31) {
        this.LNT31 = LNT31;
    }

    public String getLNT32() {
        return LNT32;
    }

    public void setLNT32(String LNT32) {
        this.LNT32 = LNT32;
    }

    public String getLNT33() {
        return LNT33;
    }

    public void setLNT33(String LNT33) {
        this.LNT33 = LNT33;
    }

    public String getLNT34() {
        return LNT34;
    }

    public void setLNT34(String LNT34) {
        this.LNT34 = LNT34;
    }

    public String getLNT35() {
        return LNT35;
    }

    public void setLNT35(String LNT35) {
        this.LNT35 = LNT35;
    }

    public Double getLNT36() {
        return LNT36;
    }

    public void setLNT36(Double LNT36) {
        this.LNT36 = LNT36;
    }

    public String getLNT37() {
        return LNT37;
    }

    public void setLNT37(String LNT37) {
        this.LNT37 = LNT37;
    }

    public String getLNT38() {
        return LNT38;
    }

    public void setLNT38(String LNT38) {
        this.LNT38 = LNT38;
    }

    public Integer getLNT39() {
        return LNT39;
    }

    public void setLNT39(Integer LNT39) {
        this.LNT39 = LNT39;
    }

    public String getLNT40() {
        return LNT40;
    }

    public void setLNT40(String LNT40) {
        this.LNT40 = LNT40;
    }

    public String getLNT41() {
        return LNT41;
    }

    public void setLNT41(String LNT41) {
        this.LNT41 = LNT41;
    }

    public Double getLNT42() {
        return LNT42;
    }

    public void setLNT42(Double LNT42) {
        this.LNT42 = LNT42;
    }

    public String getLNT43() {
        return LNT43;
    }

    public void setLNT43(String LNT43) {
        this.LNT43 = LNT43;
    }

    public String getLNT44() {
        return LNT44;
    }

    public void setLNT44(String LNT44) {
        this.LNT44 = LNT44;
    }

    public String getLNT45() {
        return LNT45;
    }

    public void setLNT45(String LNT45) {
        this.LNT45 = LNT45;
    }

    public Date getLNT46() {
        return LNT46;
    }

    public void setLNT46(Date LNT46) {
        this.LNT46 = LNT46;
    }

    public String getLNT47() {
        return LNT47;
    }

    public void setLNT47(String LNT47) {
        this.LNT47 = LNT47;
    }

    public String getLNT48() {
        return LNT48;
    }

    public void setLNT48(String LNT48) {
        this.LNT48 = LNT48;
    }

    public String getLNT49() {
        return LNT49;
    }

    public void setLNT49(String LNT49) {
        this.LNT49 = LNT49;
    }

    public String getLNT50() {
        return LNT50;
    }

    public void setLNT50(String LNT50) {
        this.LNT50 = LNT50;
    }

    public Integer getLNT51() {
        return LNT51;
    }

    public void setLNT51(Integer LNT51) {
        this.LNT51 = LNT51;
    }

    public Date getLNT52() {
        return LNT52;
    }

    public void setLNT52(Date LNT52) {
        this.LNT52 = LNT52;
    }

    public Date getLNT53() {
        return LNT53;
    }

    public void setLNT53(Date LNT53) {
        this.LNT53 = LNT53;
    }

    public String getLNTACTI() {
        return LNTACTI;
    }

    public void setLNTACTI(String LNTACTI) {
        this.LNTACTI = LNTACTI;
    }

    public Date getLNTCRAT() {
        return LNTCRAT;
    }

    public void setLNTCRAT(Date LNTCRAT) {
        this.LNTCRAT = LNTCRAT;
    }

    public Date getLNTDATE() {
        return LNTDATE;
    }

    public void setLNTDATE(Date LNTDATE) {
        this.LNTDATE = LNTDATE;
    }

    public String getLNTGRUP() {
        return LNTGRUP;
    }

    public void setLNTGRUP(String LNTGRUP) {
        this.LNTGRUP = LNTGRUP;
    }

    public String getLNTLEGAL() {
        return LNTLEGAL;
    }

    public void setLNTLEGAL(String LNTLEGAL) {
        this.LNTLEGAL = LNTLEGAL;
    }

    public String getLNTMODU() {
        return LNTMODU;
    }

    public void setLNTMODU(String LNTMODU) {
        this.LNTMODU = LNTMODU;
    }

    public String getLNTUSER() {
        return LNTUSER;
    }

    public void setLNTUSER(String LNTUSER) {
        this.LNTUSER = LNTUSER;
    }

    public String getLNTORIU() {
        return LNTORIU;
    }

    public void setLNTORIU(String LNTORIU) {
        this.LNTORIU = LNTORIU;
    }

    public String getLNTORIG() {
        return LNTORIG;
    }

    public void setLNTORIG(String LNTORIG) {
        this.LNTORIG = LNTORIG;
    }

    public String getLNTPLANT() {
        return LNTPLANT;
    }

    public void setLNTPLANT(String LNTPLANT) {
        this.LNTPLANT = LNTPLANT;
    }

    public String getLNT54() {
        return LNT54;
    }

    public void setLNT54(String LNT54) {
        this.LNT54 = LNT54;
    }

    public String getLNTPOS() {
        return LNTPOS;
    }

    public void setLNTPOS(String LNTPOS) {
        this.LNTPOS = LNTPOS;
    }

    public String getLNT55() {
        return LNT55;
    }

    public void setLNT55(String LNT55) {
        this.LNT55 = LNT55;
    }

    public String getLNT56() {
        return LNT56;
    }

    public void setLNT56(String LNT56) {
        this.LNT56 = LNT56;
    }

    public String getLNT57() {
        return LNT57;
    }

    public void setLNT57(String LNT57) {
        this.LNT57 = LNT57;
    }

    public String getLNT58() {
        return LNT58;
    }

    public void setLNT58(String LNT58) {
        this.LNT58 = LNT58;
    }

    public String getLNT59() {
        return LNT59;
    }

    public void setLNT59(String LNT59) {
        this.LNT59 = LNT59;
    }

    public String getLNT60() {
        return LNT60;
    }

    public void setLNT60(String LNT60) {
        this.LNT60 = LNT60;
    }

    public Double getLNT61() {
        return LNT61;
    }

    public void setLNT61(Double LNT61) {
        this.LNT61 = LNT61;
    }

    public String getLNT62() {
        return LNT62;
    }

    public void setLNT62(String LNT62) {
        this.LNT62 = LNT62;
    }

    public String getLNT63() {
        return LNT63;
    }

    public void setLNT63(String LNT63) {
        this.LNT63 = LNT63;
    }

    public Double getLNT64() {
        return LNT64;
    }

    public void setLNT64(Double LNT64) {
        this.LNT64 = LNT64;
    }

    public Double getLNT65() {
        return LNT65;
    }

    public void setLNT65(Double LNT65) {
        this.LNT65 = LNT65;
    }

    public Double getLNT66() {
        return LNT66;
    }

    public void setLNT66(Double LNT66) {
        this.LNT66 = LNT66;
    }

    public Double getLNT67() {
        return LNT67;
    }

    public void setLNT67(Double LNT67) {
        this.LNT67 = LNT67;
    }

    public Double getLNT68() {
        return LNT68;
    }

    public void setLNT68(Double LNT68) {
        this.LNT68 = LNT68;
    }

    public Double getLNT69() {
        return LNT69;
    }

    public void setLNT69(Double LNT69) {
        this.LNT69 = LNT69;
    }

    public String getLNT70() {
        return LNT70;
    }

    public void setLNT70(String LNT70) {
        this.LNT70 = LNT70;
    }

    public String getLNT71() {
        return LNT71;
    }

    public void setLNT71(String LNT71) {
        this.LNT71 = LNT71;
    }

    public Integer getLNT72() {
        return LNT72;
    }

    public void setLNT72(Integer LNT72) {
        this.LNT72 = LNT72;
    }

    public String getLNT73() {
        return LNT73;
    }

    public void setLNT73(String LNT73) {
        this.LNT73 = LNT73;
    }

    public Double getTA_LNT01() {
        return TA_LNT01;
    }

    public void setTA_LNT01(Double TA_LNT01) {
        this.TA_LNT01 = TA_LNT01;
    }

    public Integer getTA_LNT02() {
        return TA_LNT02;
    }

    public void setTA_LNT02(Integer TA_LNT02) {
        this.TA_LNT02 = TA_LNT02;
    }

    public Integer getTA_LNT03() {
        return TA_LNT03;
    }

    public void setTA_LNT03(Integer TA_LNT03) {
        this.TA_LNT03 = TA_LNT03;
    }

    public Double getTA_LNT04() {
        return TA_LNT04;
    }

    public void setTA_LNT04(Double TA_LNT04) {
        this.TA_LNT04 = TA_LNT04;
    }

    public String getTA_LNT05() {
        return TA_LNT05;
    }

    public void setTA_LNT05(String TA_LNT05) {
        this.TA_LNT05 = TA_LNT05;
    }

    public String getTA_LNT06() {
        return TA_LNT06;
    }

    public void setTA_LNT06(String TA_LNT06) {
        this.TA_LNT06 = TA_LNT06;
    }

    public String getTA_LNT07() {
        return TA_LNT07;
    }

    public void setTA_LNT07(String TA_LNT07) {
        this.TA_LNT07 = TA_LNT07;
    }

    public String getTA_LNT08() {
        return TA_LNT08;
    }

    public void setTA_LNT08(String TA_LNT08) {
        this.TA_LNT08 = TA_LNT08;
    }

    public Double getTA_LNT09() {
        return TA_LNT09;
    }

    public void setTA_LNT09(Double TA_LNT09) {
        this.TA_LNT09 = TA_LNT09;
    }

    public String getTA_LNT10() {
        return TA_LNT10;
    }

    public void setTA_LNT10(String TA_LNT10) {
        this.TA_LNT10 = TA_LNT10;
    }

    public String getTA_LNT11() {
        return TA_LNT11;
    }

    public void setTA_LNT11(String TA_LNT11) {
        this.TA_LNT11 = TA_LNT11;
    }

    public String getTA_LNT12() {
        return TA_LNT12;
    }

    public void setTA_LNT12(String TA_LNT12) {
        this.TA_LNT12 = TA_LNT12;
    }

    public String getTA_LNT13() {
        return TA_LNT13;
    }

    public void setTA_LNT13(String TA_LNT13) {
        this.TA_LNT13 = TA_LNT13;
    }

    public Integer getTA_LNT14() {
        return TA_LNT14;
    }

    public void setTA_LNT14(Integer TA_LNT14) {
        this.TA_LNT14 = TA_LNT14;
    }

    public String getTA_LNT15() {
        return TA_LNT15;
    }

    public void setTA_LNT15(String TA_LNT15) {
        this.TA_LNT15 = TA_LNT15;
    }

    /**
     * 取得櫃位名稱 (TQA02)
     * 修正：原本寫死回傳 null，現在正確回傳欄位值
     */
    public String getTQA02() {
        return TQA02;
    }

    public void setTQA02(String TQA02) {
        this.TQA02 = TQA02;
    }

    public Double getTC_PSA12() {
        return TC_PSA12;
    }

    public void setTC_PSA12(Double TC_PSA12) {
        this.TC_PSA12 = TC_PSA12;
    }

    public Double getTC_PSA40() {
        return TC_PSA40;
    }

    public void setTC_PSA40(Double TC_PSA40) {
        this.TC_PSA40 = TC_PSA40;
    }

    public Date getTC_PSA04() {
        return TC_PSA04;
    }

    public void setTC_PSA04(Date TC_PSA04) {
        this.TC_PSA04 = TC_PSA04;
    }

    public String getTC_PSA05() {
        return TC_PSA05;
    }

    public void setTC_PSA05(String TC_PSA05) {
        this.TC_PSA05 = TC_PSA05;
    }

    public String getOBA01() {
        return OBA01;
    }

    public void setOBA01(String OBA01) {
        this.OBA01 = OBA01;
    }

    public String getOBA02() {
        return OBA02;
    }

    public void setOBA02(String OBA02) {
        this.OBA02 = OBA02;
    }

    public Double getLNU04() {
        return LNU04;
    }

    public void setLNU04(Double LNU04) {
        this.LNU04 = LNU04;
    }

    public String getLNV01() {
        return LNV01;
    }

    public void setLNV01(String LNV01) {
        this.LNV01 = LNV01;
    }
}