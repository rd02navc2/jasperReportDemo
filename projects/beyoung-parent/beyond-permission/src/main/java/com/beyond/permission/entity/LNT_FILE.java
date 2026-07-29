package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LNT_FILE")
public class LNT_FILE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String LNT01;
	private String LNT02;
	private Date LNT03;
	private String LNT04;
	private String LNT05;
	private String LNT06;
	private String LNT07;
	private String LNT08;
	private String LNT09;
	private Double LNT10;
	private Double LNT11;
	private String LNT12;
	private String LNT13;
	private Double LNT14;
	private String LNT15;
	private String LNT16;
	private Date LNT17;
	private Date LNT18;
	private Date LNT19;
	private Date LNT20;
	private Date LNT21;
	private Date LNT22;
	private Date LNT23;
	private String LNT24;
	private String LNT25;
	private String LNT26;
	private String LNT27;
	private Date LNT28;
	private String LNT29;
	private String LNT30;
	private String LNT31;
	private String LNT32;
	private String LNT33;
	private String LNT34;
	private String LNT35;
	private Double LNT36;
	private String LNT37;
	private String LNT38;
	private Integer LNT39;
	private String LNT40;
	private String LNT41;
	private Double LNT42;
	private String LNT43;
	private String LNT44;
	private String LNT45;
	private Date LNT46;
	private String LNT47;
	private String LNT48;
	private String LNT49;
	private String LNT50;
	private Integer LNT51;
	private Date LNT52;
	private Date LNT53;
	private String LNTACTI;
	private Date LNTCRAT;
	private Date LNTDATE;
	private String LNTGRUP;
	private String LNTLEGAL;
	private String LNTMODU;
	private String LNTUSER;
	private String LNTORIU;
	private String LNTORIG;
	private String LNTPLANT;
	private String LNT54;
	private String LNTPOS;
	private String LNT55;
	private String LNT56;
	private String LNT57;
	private String LNT58;
	private String LNT59;
	private String LNT60;
	private Double LNT61;
	private String LNT62;
	private String LNT63;
	private Double LNT64;
	private Double LNT65;
	private Double LNT66;
	private Double LNT67;
	private Double LNT68;
	private Double LNT69;
	private String LNT70;
	private String LNT71;
	private Integer LNT72;
	private String LNT73;
	private Double TA_LNT01;
	private Integer TA_LNT02;
	private Integer TA_LNT03;
	private Double TA_LNT04;
	private String TA_LNT05;
	private String TA_LNT06;
	private String TA_LNT07;
	private String TA_LNT08;
	private Double TA_LNT09;
	private String TA_LNT10;
	private String TA_LNT11;
	private String TA_LNT12;
	private String TA_LNT13;
	private Integer TA_LNT14;
	private String TA_LNT15;
	
	private String TQA02;
	private Double TC_PSA12;
	private Double TC_PSA40;
	private Date TC_PSA04;
	private String TC_PSA05;
	private String OBA01;
	private String OBA02;
	
	public String getOBA01() {
		return OBA01;
	}
	public void setOBA01(String oBA01) {
		OBA01 = oBA01;
	}
	public String getOBA02() {
		return OBA02;
	}
	public void setOBA02(String oBA02) {
		OBA02 = oBA02;
	}
	public Double getTC_PSA40() {
		return TC_PSA40;
	}
	public void setTC_PSA40(Double tC_PSA40) {
		TC_PSA40 = tC_PSA40;
	}
	public String getTC_PSA05() {
		return TC_PSA05;
	}
	public void setTC_PSA05(String tC_PSA05) {
		TC_PSA05 = tC_PSA05;
	}
	public Date getTC_PSA04() {
		return TC_PSA04;
	}
	public void setTC_PSA04(Date tC_PSA04) {
		TC_PSA04 = tC_PSA04;
	}
	public Double getTC_PSA12() {
		return TC_PSA12;
	}
	public void setTC_PSA12(Double tC_PSA12) {
		TC_PSA12 = tC_PSA12;
	}
	public String getTQA02() {
		return TQA02;
	}
	public void setTQA02(String tQA02) {
		TQA02 = tQA02;
	}
	public String getLNT01() {
		return LNT01;
	}
	public void setLNT01(String lNT01) {
		LNT01 = lNT01;
	}
	public String getLNT02() {
		return LNT02;
	}
	public void setLNT02(String lNT02) {
		LNT02 = lNT02;
	}
	public Date getLNT03() {
		return LNT03;
	}
	public void setLNT03(Date lNT03) {
		LNT03 = lNT03;
	}
	public String getLNT04() {
		return LNT04;
	}
	public void setLNT04(String lNT04) {
		LNT04 = lNT04;
	}
	public String getLNT05() {
		return LNT05;
	}
	public void setLNT05(String lNT05) {
		LNT05 = lNT05;
	}
	public String getLNT06() {
		return LNT06;
	}
	public void setLNT06(String lNT06) {
		LNT06 = lNT06;
	}
	public String getLNT07() {
		return LNT07;
	}
	public void setLNT07(String lNT07) {
		LNT07 = lNT07;
	}
	public String getLNT08() {
		return LNT08;
	}
	public void setLNT08(String lNT08) {
		LNT08 = lNT08;
	}
	public String getLNT09() {
		return LNT09;
	}
	public void setLNT09(String lNT09) {
		LNT09 = lNT09;
	}
	public Double getLNT10() {
		return LNT10;
	}
	public void setLNT10(Double lNT10) {
		LNT10 = lNT10;
	}
	public Double getLNT11() {
		return LNT11;
	}
	public void setLNT11(Double lNT11) {
		LNT11 = lNT11;
	}
	public String getLNT12() {
		return LNT12;
	}
	public void setLNT12(String lNT12) {
		LNT12 = lNT12;
	}
	public String getLNT13() {
		return LNT13;
	}
	public void setLNT13(String lNT13) {
		LNT13 = lNT13;
	}
	public Double getLNT14() {
		return LNT14;
	}
	public void setLNT14(Double lNT14) {
		LNT14 = lNT14;
	}
	public String getLNT15() {
		return LNT15;
	}
	public void setLNT15(String lNT15) {
		LNT15 = lNT15;
	}
	public String getLNT16() {
		return LNT16;
	}
	public void setLNT16(String lNT16) {
		LNT16 = lNT16;
	}
	public Date getLNT17() {
		return LNT17;
	}
	public void setLNT17(Date lNT17) {
		LNT17 = lNT17;
	}
	public Date getLNT18() {
		return LNT18;
	}
	public void setLNT18(Date lNT18) {
		LNT18 = lNT18;
	}
	public Date getLNT19() {
		return LNT19;
	}
	public void setLNT19(Date lNT19) {
		LNT19 = lNT19;
	}
	public Date getLNT20() {
		return LNT20;
	}
	public void setLNT20(Date lNT20) {
		LNT20 = lNT20;
	}
	public Date getLNT21() {
		return LNT21;
	}
	public void setLNT21(Date lNT21) {
		LNT21 = lNT21;
	}
	public Date getLNT22() {
		return LNT22;
	}
	public void setLNT22(Date lNT22) {
		LNT22 = lNT22;
	}
	public Date getLNT23() {
		return LNT23;
	}
	public void setLNT23(Date lNT23) {
		LNT23 = lNT23;
	}
	public String getLNT24() {
		return LNT24;
	}
	public void setLNT24(String lNT24) {
		LNT24 = lNT24;
	}
	public String getLNT25() {
		return LNT25;
	}
	public void setLNT25(String lNT25) {
		LNT25 = lNT25;
	}
	public String getLNT26() {
		return LNT26;
	}
	public void setLNT26(String lNT26) {
		LNT26 = lNT26;
	}
	public String getLNT27() {
		return LNT27;
	}
	public void setLNT27(String lNT27) {
		LNT27 = lNT27;
	}
	public Date getLNT28() {
		return LNT28;
	}
	public void setLNT28(Date lNT28) {
		LNT28 = lNT28;
	}
	public String getLNT29() {
		return LNT29;
	}
	public void setLNT29(String lNT29) {
		LNT29 = lNT29;
	}
	public String getLNT30() {
		return LNT30;
	}
	public void setLNT30(String lNT30) {
		LNT30 = lNT30;
	}
	public String getLNT31() {
		return LNT31;
	}
	public void setLNT31(String lNT31) {
		LNT31 = lNT31;
	}
	public String getLNT32() {
		return LNT32;
	}
	public void setLNT32(String lNT32) {
		LNT32 = lNT32;
	}
	public String getLNT33() {
		return LNT33;
	}
	public void setLNT33(String lNT33) {
		LNT33 = lNT33;
	}
	public String getLNT34() {
		return LNT34;
	}
	public void setLNT34(String lNT34) {
		LNT34 = lNT34;
	}
	public String getLNT35() {
		return LNT35;
	}
	public void setLNT35(String lNT35) {
		LNT35 = lNT35;
	}
	public Double getLNT36() {
		return LNT36;
	}
	public void setLNT36(Double lNT36) {
		LNT36 = lNT36;
	}
	public String getLNT37() {
		return LNT37;
	}
	public void setLNT37(String lNT37) {
		LNT37 = lNT37;
	}
	public String getLNT38() {
		return LNT38;
	}
	public void setLNT38(String lNT38) {
		LNT38 = lNT38;
	}
	public Integer getLNT39() {
		return LNT39;
	}
	public void setLNT39(Integer lNT39) {
		LNT39 = lNT39;
	}
	public String getLNT40() {
		return LNT40;
	}
	public void setLNT40(String lNT40) {
		LNT40 = lNT40;
	}
	public String getLNT41() {
		return LNT41;
	}
	public void setLNT41(String lNT41) {
		LNT41 = lNT41;
	}
	public Double getLNT42() {
		return LNT42;
	}
	public void setLNT42(Double lNT42) {
		LNT42 = lNT42;
	}
	public String getLNT43() {
		return LNT43;
	}
	public void setLNT43(String lNT43) {
		LNT43 = lNT43;
	}
	public String getLNT44() {
		return LNT44;
	}
	public void setLNT44(String lNT44) {
		LNT44 = lNT44;
	}
	public String getLNT45() {
		return LNT45;
	}
	public void setLNT45(String lNT45) {
		LNT45 = lNT45;
	}
	public Date getLNT46() {
		return LNT46;
	}
	public void setLNT46(Date lNT46) {
		LNT46 = lNT46;
	}
	public String getLNT47() {
		return LNT47;
	}
	public void setLNT47(String lNT47) {
		LNT47 = lNT47;
	}
	public String getLNT48() {
		return LNT48;
	}
	public void setLNT48(String lNT48) {
		LNT48 = lNT48;
	}
	public String getLNT49() {
		return LNT49;
	}
	public void setLNT49(String lNT49) {
		LNT49 = lNT49;
	}
	public String getLNT50() {
		return LNT50;
	}
	public void setLNT50(String lNT50) {
		LNT50 = lNT50;
	}
	public Integer getLNT51() {
		return LNT51;
	}
	public void setLNT51(Integer lNT51) {
		LNT51 = lNT51;
	}
	public Date getLNT52() {
		return LNT52;
	}
	public void setLNT52(Date lNT52) {
		LNT52 = lNT52;
	}
	public Date getLNT53() {
		return LNT53;
	}
	public void setLNT53(Date lNT53) {
		LNT53 = lNT53;
	}
	public String getLNTACTI() {
		return LNTACTI;
	}
	public void setLNTACTI(String lNTACTI) {
		LNTACTI = lNTACTI;
	}
	public Date getLNTCRAT() {
		return LNTCRAT;
	}
	public void setLNTCRAT(Date lNTCRAT) {
		LNTCRAT = lNTCRAT;
	}
	public Date getLNTDATE() {
		return LNTDATE;
	}
	public void setLNTDATE(Date lNTDATE) {
		LNTDATE = lNTDATE;
	}
	public String getLNTGRUP() {
		return LNTGRUP;
	}
	public void setLNTGRUP(String lNTGRUP) {
		LNTGRUP = lNTGRUP;
	}
	public String getLNTLEGAL() {
		return LNTLEGAL;
	}
	public void setLNTLEGAL(String lNTLEGAL) {
		LNTLEGAL = lNTLEGAL;
	}
	public String getLNTMODU() {
		return LNTMODU;
	}
	public void setLNTMODU(String lNTMODU) {
		LNTMODU = lNTMODU;
	}
	public String getLNTUSER() {
		return LNTUSER;
	}
	public void setLNTUSER(String lNTUSER) {
		LNTUSER = lNTUSER;
	}
	public String getLNTORIU() {
		return LNTORIU;
	}
	public void setLNTORIU(String lNTORIU) {
		LNTORIU = lNTORIU;
	}
	public String getLNTORIG() {
		return LNTORIG;
	}
	public void setLNTORIG(String lNTORIG) {
		LNTORIG = lNTORIG;
	}
	public String getLNTPLANT() {
		return LNTPLANT;
	}
	public void setLNTPLANT(String lNTPLANT) {
		LNTPLANT = lNTPLANT;
	}
	public String getLNT54() {
		return LNT54;
	}
	public void setLNT54(String lNT54) {
		LNT54 = lNT54;
	}
	public String getLNTPOS() {
		return LNTPOS;
	}
	public void setLNTPOS(String lNTPOS) {
		LNTPOS = lNTPOS;
	}
	public String getLNT55() {
		return LNT55;
	}
	public void setLNT55(String lNT55) {
		LNT55 = lNT55;
	}
	public String getLNT56() {
		return LNT56;
	}
	public void setLNT56(String lNT56) {
		LNT56 = lNT56;
	}
	public String getLNT57() {
		return LNT57;
	}
	public void setLNT57(String lNT57) {
		LNT57 = lNT57;
	}
	public String getLNT58() {
		return LNT58;
	}
	public void setLNT58(String lNT58) {
		LNT58 = lNT58;
	}
	public String getLNT59() {
		return LNT59;
	}
	public void setLNT59(String lNT59) {
		LNT59 = lNT59;
	}
	public String getLNT60() {
		return LNT60;
	}
	public void setLNT60(String lNT60) {
		LNT60 = lNT60;
	}
	public Double getLNT61() {
		return LNT61;
	}
	public void setLNT61(Double lNT61) {
		LNT61 = lNT61;
	}
	public String getLNT62() {
		return LNT62;
	}
	public void setLNT62(String lNT62) {
		LNT62 = lNT62;
	}
	public String getLNT63() {
		return LNT63;
	}
	public void setLNT63(String lNT63) {
		LNT63 = lNT63;
	}
	public Double getLNT64() {
		return LNT64;
	}
	public void setLNT64(Double lNT64) {
		LNT64 = lNT64;
	}
	public Double getLNT65() {
		return LNT65;
	}
	public void setLNT65(Double lNT65) {
		LNT65 = lNT65;
	}
	public Double getLNT66() {
		return LNT66;
	}
	public void setLNT66(Double lNT66) {
		LNT66 = lNT66;
	}
	public Double getLNT67() {
		return LNT67;
	}
	public void setLNT67(Double lNT67) {
		LNT67 = lNT67;
	}
	public Double getLNT68() {
		return LNT68;
	}
	public void setLNT68(Double lNT68) {
		LNT68 = lNT68;
	}
	public Double getLNT69() {
		return LNT69;
	}
	public void setLNT69(Double lNT69) {
		LNT69 = lNT69;
	}
	public String getLNT70() {
		return LNT70;
	}
	public void setLNT70(String lNT70) {
		LNT70 = lNT70;
	}
	public String getLNT71() {
		return LNT71;
	}
	public void setLNT71(String lNT71) {
		LNT71 = lNT71;
	}
	public Integer getLNT72() {
		return LNT72;
	}
	public void setLNT72(Integer lNT72) {
		LNT72 = lNT72;
	}
	public String getLNT73() {
		return LNT73;
	}
	public void setLNT73(String lNT73) {
		LNT73 = lNT73;
	}
	public Double getTA_LNT01() {
		return TA_LNT01;
	}
	public void setTA_LNT01(Double tA_LNT01) {
		TA_LNT01 = tA_LNT01;
	}
	public Integer getTA_LNT02() {
		return TA_LNT02;
	}
	public void setTA_LNT02(Integer tA_LNT02) {
		TA_LNT02 = tA_LNT02;
	}
	public Integer getTA_LNT03() {
		return TA_LNT03;
	}
	public void setTA_LNT03(Integer tA_LNT03) {
		TA_LNT03 = tA_LNT03;
	}
	public Double getTA_LNT04() {
		return TA_LNT04;
	}
	public void setTA_LNT04(Double tA_LNT04) {
		TA_LNT04 = tA_LNT04;
	}
	public String getTA_LNT05() {
		return TA_LNT05;
	}
	public void setTA_LNT05(String tA_LNT05) {
		TA_LNT05 = tA_LNT05;
	}
	public String getTA_LNT06() {
		return TA_LNT06;
	}
	public void setTA_LNT06(String tA_LNT06) {
		TA_LNT06 = tA_LNT06;
	}
	public String getTA_LNT07() {
		return TA_LNT07;
	}
	public void setTA_LNT07(String tA_LNT07) {
		TA_LNT07 = tA_LNT07;
	}
	public String getTA_LNT08() {
		return TA_LNT08;
	}
	public void setTA_LNT08(String tA_LNT08) {
		TA_LNT08 = tA_LNT08;
	}
	public Double getTA_LNT09() {
		return TA_LNT09;
	}
	public void setTA_LNT09(Double tA_LNT09) {
		TA_LNT09 = tA_LNT09;
	}
	public String getTA_LNT10() {
		return TA_LNT10;
	}
	public void setTA_LNT10(String tA_LNT10) {
		TA_LNT10 = tA_LNT10;
	}
	public String getTA_LNT11() {
		return TA_LNT11;
	}
	public void setTA_LNT11(String tA_LNT11) {
		TA_LNT11 = tA_LNT11;
	}
	public String getTA_LNT12() {
		return TA_LNT12;
	}
	public void setTA_LNT12(String tA_LNT12) {
		TA_LNT12 = tA_LNT12;
	}
	public String getTA_LNT13() {
		return TA_LNT13;
	}
	public void setTA_LNT13(String tA_LNT13) {
		TA_LNT13 = tA_LNT13;
	}
	public Integer getTA_LNT14() {
		return TA_LNT14;
	}
	public void setTA_LNT14(Integer tA_LNT14) {
		TA_LNT14 = tA_LNT14;
	}
	public String getTA_LNT15() {
		return TA_LNT15;
	}
	public void setTA_LNT15(String tA_LNT15) {
		TA_LNT15 = tA_LNT15;
	}
}
