package com.beyond.report.entity;

import java.util.Date;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LPK_FILE")
public class LPK_FILE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String LPK01;
	private String LPK02;
	private String LPK03;
	private String LPK04;
	private Date LPK05;
	private String LPK06;
	private String LPK07;
	private String LPK08;
	private String LPK09;
	private String LPK10;
	private String LPK11;
	private String LPK12;
	private String LPK13;
	private String LPK14;
	private String LPK15;
	private String LPK16;
	private String LPK17;
	private String LPK18;
	private String LPK19;
	private String LPKACTI;
	private Date LPKCRAT;
	private Date LPKDate;
	private String LPKGRUP;
	private String LPKMODU;
	private String LPKUSER;
	private String LPKORIU;
	private String LPKORIG;
	private String LPKPOS;
	private Integer LPK051;
	private String LPKUD01;
	private String LPKUD02;
	private String LPKUD03;
	private String LPKUD04;
	private String LPKUD05;
	private String LPKUD06;
	private Double LPKUD07;
	private Double LPKUD08;
	private Double LPKUD09;
	private Integer LPKUD10;
	private Integer LPKUD11;
	private Integer LPKUD12;
	private Date LPKUD13;
	private Date LPKUD14;
	private Date LPKUD15;
	private Integer LPK052;
	private Integer LPK053;
	private String LPK20;
	private String LPK21;
	private Integer TA_LPK01;
	private String TA_LPK02;
	private String TA_LPK03;
	private Date TA_LPK04;
	private String TA_LPK05;
	private String TA_LPK06;
	private String TA_LPK07;
	private String TA_LPK08;
	
	private Double LPJ15;
	private Double TC_PSA08;
	
	@Transient
	private Integer age;

	@Transient
	private String vip_level;
	
	@Transient
	private Double lsm08_1;

	@Transient
	private Integer lsm08_cnt_1;

	@Transient
	private Double lsm08_2;

	@Transient
	private Integer lsm08_cnt_2;

	@Transient
	private Integer rec_cnt;

	@Transient
	private Integer lpk01_cnt;

	public Integer getLpk01_cnt() {
		return lpk01_cnt;
	}
	public void setLpk01_cnt(Integer lpk01_cnt) {
		this.lpk01_cnt = lpk01_cnt;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public Double getTC_PSA08() {
		return TC_PSA08;
	}
	public void setTC_PSA08(Double tC_PSA08) {
		TC_PSA08 = tC_PSA08;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getLsm08_cnt_1() {
		return lsm08_cnt_1;
	}
	public void setLsm08_cnt_1(Integer lsm08_cnt_1) {
		this.lsm08_cnt_1 = lsm08_cnt_1;
	}
	public Integer getLsm08_cnt_2() {
		return lsm08_cnt_2;
	}
	public void setLsm08_cnt_2(Integer lsm08_cnt_2) {
		this.lsm08_cnt_2 = lsm08_cnt_2;
	}
	public Double getLsm08_1() {
		return lsm08_1;
	}
	public void setLsm08_1(Double lsm08_1) {
		this.lsm08_1 = lsm08_1;
	}
	public Double getLsm08_2() {
		return lsm08_2;
	}
	public void setLsm08_2(Double lsm08_2) {
		this.lsm08_2 = lsm08_2;
	}
	public String getVip_level() {
		return vip_level;
	}
	public void setVip_level(String vip_level) {
		this.vip_level = vip_level;
	}
	public Double getLPJ15() {
		return LPJ15;
	}
	public void setLPJ15(Double lPJ15) {
		LPJ15 = lPJ15;
	}
	public String getLPK01() {
		return LPK01;
	}
	public void setLPK01(String lPK01) {
		LPK01 = lPK01;
	}
	public String getLPK02() {
		return LPK02;
	}
	public void setLPK02(String lPK02) {
		LPK02 = lPK02;
	}
	public String getLPK03() {
		return LPK03;
	}
	public void setLPK03(String lPK03) {
		LPK03 = lPK03;
	}
	public String getLPK04() {
		return LPK04;
	}
	public void setLPK04(String lPK04) {
		LPK04 = lPK04;
	}
	public Date getLPK05() {
		return LPK05;
	}
	public void setLPK05(Date lPK05) {
		LPK05 = lPK05;
	}
	public String getLPK06() {
		return LPK06;
	}
	public void setLPK06(String lPK06) {
		LPK06 = lPK06;
	}
	public String getLPK07() {
		return LPK07;
	}
	public void setLPK07(String lPK07) {
		LPK07 = lPK07;
	}
	public String getLPK08() {
		return LPK08;
	}
	public void setLPK08(String lPK08) {
		LPK08 = lPK08;
	}
	public String getLPK09() {
		return LPK09;
	}
	public void setLPK09(String lPK09) {
		LPK09 = lPK09;
	}
	public String getLPK10() {
		return LPK10;
	}
	public void setLPK10(String lPK10) {
		LPK10 = lPK10;
	}
	public String getLPK11() {
		return LPK11;
	}
	public void setLPK11(String lPK11) {
		LPK11 = lPK11;
	}
	public String getLPK12() {
		return LPK12;
	}
	public void setLPK12(String lPK12) {
		LPK12 = lPK12;
	}
	public String getLPK13() {
		return LPK13;
	}
	public void setLPK13(String lPK13) {
		LPK13 = lPK13;
	}
	public String getLPK14() {
		return LPK14;
	}
	public void setLPK14(String lPK14) {
		LPK14 = lPK14;
	}
	public String getLPK15() {
		return LPK15;
	}
	public void setLPK15(String lPK15) {
		LPK15 = lPK15;
	}
	public String getLPK16() {
		return LPK16;
	}
	public void setLPK16(String lPK16) {
		LPK16 = lPK16;
	}
	public String getLPK17() {
		return LPK17;
	}
	public void setLPK17(String lPK17) {
		LPK17 = lPK17;
	}
	public String getLPK18() {
		return LPK18;
	}
	public void setLPK18(String lPK18) {
		LPK18 = lPK18;
	}
	public String getLPK19() {
		return LPK19;
	}
	public void setLPK19(String lPK19) {
		LPK19 = lPK19;
	}
	public String getLPKACTI() {
		return LPKACTI;
	}
	public void setLPKACTI(String lPKACTI) {
		LPKACTI = lPKACTI;
	}
	public Date getLPKCRAT() {
		return LPKCRAT;
	}
	public void setLPKCRAT(Date lPKCRAT) {
		LPKCRAT = lPKCRAT;
	}
	public Date getLPKDate() {
		return LPKDate;
	}
	public void setLPKDate(Date lPKDate) {
		LPKDate = lPKDate;
	}
	public String getLPKGRUP() {
		return LPKGRUP;
	}
	public void setLPKGRUP(String lPKGRUP) {
		LPKGRUP = lPKGRUP;
	}
	public String getLPKMODU() {
		return LPKMODU;
	}
	public void setLPKMODU(String lPKMODU) {
		LPKMODU = lPKMODU;
	}
	public String getLPKUSER() {
		return LPKUSER;
	}
	public void setLPKUSER(String lPKUSER) {
		LPKUSER = lPKUSER;
	}
	public String getLPKORIU() {
		return LPKORIU;
	}
	public void setLPKORIU(String lPKORIU) {
		LPKORIU = lPKORIU;
	}
	public String getLPKORIG() {
		return LPKORIG;
	}
	public void setLPKORIG(String lPKORIG) {
		LPKORIG = lPKORIG;
	}
	public String getLPKPOS() {
		return LPKPOS;
	}
	public void setLPKPOS(String lPKPOS) {
		LPKPOS = lPKPOS;
	}
	public Integer getLPK051() {
		return LPK051;
	}
	public void setLPK051(Integer lPK051) {
		LPK051 = lPK051;
	}
	public String getLPKUD01() {
		return LPKUD01;
	}
	public void setLPKUD01(String lPKUD01) {
		LPKUD01 = lPKUD01;
	}
	public String getLPKUD02() {
		return LPKUD02;
	}
	public void setLPKUD02(String lPKUD02) {
		LPKUD02 = lPKUD02;
	}
	public String getLPKUD03() {
		return LPKUD03;
	}
	public void setLPKUD03(String lPKUD03) {
		LPKUD03 = lPKUD03;
	}
	public String getLPKUD04() {
		return LPKUD04;
	}
	public void setLPKUD04(String lPKUD04) {
		LPKUD04 = lPKUD04;
	}
	public String getLPKUD05() {
		return LPKUD05;
	}
	public void setLPKUD05(String lPKUD05) {
		LPKUD05 = lPKUD05;
	}
	public String getLPKUD06() {
		return LPKUD06;
	}
	public void setLPKUD06(String lPKUD06) {
		LPKUD06 = lPKUD06;
	}
	public Double getLPKUD07() {
		return LPKUD07;
	}
	public void setLPKUD07(Double lPKUD07) {
		LPKUD07 = lPKUD07;
	}
	public Double getLPKUD08() {
		return LPKUD08;
	}
	public void setLPKUD08(Double lPKUD08) {
		LPKUD08 = lPKUD08;
	}
	public Double getLPKUD09() {
		return LPKUD09;
	}
	public void setLPKUD09(Double lPKUD09) {
		LPKUD09 = lPKUD09;
	}
	public Integer getLPKUD10() {
		return LPKUD10;
	}
	public void setLPKUD10(Integer lPKUD10) {
		LPKUD10 = lPKUD10;
	}
	public Integer getLPKUD11() {
		return LPKUD11;
	}
	public void setLPKUD11(Integer lPKUD11) {
		LPKUD11 = lPKUD11;
	}
	public Integer getLPKUD12() {
		return LPKUD12;
	}
	public void setLPKUD12(Integer lPKUD12) {
		LPKUD12 = lPKUD12;
	}
	public Date getLPKUD13() {
		return LPKUD13;
	}
	public void setLPKUD13(Date lPKUD13) {
		LPKUD13 = lPKUD13;
	}
	public Date getLPKUD14() {
		return LPKUD14;
	}
	public void setLPKUD14(Date lPKUD14) {
		LPKUD14 = lPKUD14;
	}
	public Date getLPKUD15() {
		return LPKUD15;
	}
	public void setLPKUD15(Date lPKUD15) {
		LPKUD15 = lPKUD15;
	}
	public Integer getLPK052() {
		return LPK052;
	}
	public void setLPK052(Integer lPK052) {
		LPK052 = lPK052;
	}
	public Integer getLPK053() {
		return LPK053;
	}
	public void setLPK053(Integer lPK053) {
		LPK053 = lPK053;
	}
	public String getLPK20() {
		return LPK20;
	}
	public void setLPK20(String lPK20) {
		LPK20 = lPK20;
	}
	public String getLPK21() {
		return LPK21;
	}
	public void setLPK21(String lPK21) {
		LPK21 = lPK21;
	}
	public Integer getTA_LPK01() {
		return TA_LPK01;
	}
	public void setTA_LPK01(Integer tA_LPK01) {
		TA_LPK01 = tA_LPK01;
	}
	public String getTA_LPK02() {
		return TA_LPK02;
	}
	public void setTA_LPK02(String tA_LPK02) {
		TA_LPK02 = tA_LPK02;
	}
	public String getTA_LPK03() {
		return TA_LPK03;
	}
	public void setTA_LPK03(String tA_LPK03) {
		TA_LPK03 = tA_LPK03;
	}
	public Date getTA_LPK04() {
		return TA_LPK04;
	}
	public void setTA_LPK04(Date tA_LPK04) {
		TA_LPK04 = tA_LPK04;
	}
	public String getTA_LPK05() {
		return TA_LPK05;
	}
	public void setTA_LPK05(String tA_LPK05) {
		TA_LPK05 = tA_LPK05;
	}
	public String getTA_LPK06() {
		return TA_LPK06;
	}
	public void setTA_LPK06(String tA_LPK06) {
		TA_LPK06 = tA_LPK06;
	}
	public String getTA_LPK07() {
		return TA_LPK07;
	}
	public void setTA_LPK07(String tA_LPK07) {
		TA_LPK07 = tA_LPK07;
	}
	public String getTA_LPK08() {
		return TA_LPK08;
	}
	public void setTA_LPK08(String tA_LPK08) {
		TA_LPK08 = tA_LPK08;
	}
}
