package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "LQE_FILE")
public class LQE_FILE implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String LQE01;
	private String LQE02;
	private String LQE03;
	private String LQE04;
	private Date LQE05;
	private String LQE06;
	private Date LQE07;
	private Double LQE08;
	private String LQE09;
	private Date LQE10;
	private String LQE11;
	private Date LQE12;
	private String LQE13;
	private Date LQE14;
	private String LQE15;
	private Date LQE16;
	private String LQE17;
	private String LQE18;
	private Date LQE19;
	private Date LQE20;
	private Date LQE21;
	private String LQEPOS;
	private String LQE22;
	private Double LQE23;
	private String LQE24;
	private Date LQE25;
	private Double TA_LQE01;
	private Double TA_LQE02;
	private String TA_LQE03;
	private String TA_LQE04;
	private Date TA_LQE05;
	private String TA_LQE06;
	private String TA_LQE07;
	private String TA_LQE09;
	
	private String TQA02;
	
	@Transient
	private int rec_cnt;
	
	public String getTA_LQE09() {
		return TA_LQE09;
	}
	public void setTA_LQE09(String tA_LQE09) {
		TA_LQE09 = tA_LQE09;
	}
	public String getLQE01() {
		return LQE01;
	}
	public void setLQE01(String lQE01) {
		LQE01 = lQE01;
	}
	public String getLQE02() {
		return LQE02;
	}
	public void setLQE02(String lQE02) {
		LQE02 = lQE02;
	}
	public String getLQE03() {
		return LQE03;
	}
	public void setLQE03(String lQE03) {
		LQE03 = lQE03;
	}
	public String getLQE04() {
		return LQE04;
	}
	public void setLQE04(String lQE04) {
		LQE04 = lQE04;
	}
	public Date getLQE05() {
		return LQE05;
	}
	public void setLQE05(Date lQE05) {
		LQE05 = lQE05;
	}
	public String getLQE06() {
		return LQE06;
	}
	public void setLQE06(String lQE06) {
		LQE06 = lQE06;
	}
	public Date getLQE07() {
		return LQE07;
	}
	public void setLQE07(Date lQE07) {
		LQE07 = lQE07;
	}
	public Double getLQE08() {
		return LQE08;
	}
	public void setLQE08(Double lQE08) {
		LQE08 = lQE08;
	}
	public String getLQE09() {
		return LQE09;
	}
	public void setLQE09(String lQE09) {
		LQE09 = lQE09;
	}
	public Date getLQE10() {
		return LQE10;
	}
	public void setLQE10(Date lQE10) {
		LQE10 = lQE10;
	}
	public String getLQE11() {
		return LQE11;
	}
	public void setLQE11(String lQE11) {
		LQE11 = lQE11;
	}
	public Date getLQE12() {
		return LQE12;
	}
	public void setLQE12(Date lQE12) {
		LQE12 = lQE12;
	}
	public String getLQE13() {
		return LQE13;
	}
	public void setLQE13(String lQE13) {
		LQE13 = lQE13;
	}
	public Date getLQE14() {
		return LQE14;
	}
	public void setLQE14(Date lQE14) {
		LQE14 = lQE14;
	}
	public String getLQE15() {
		return LQE15;
	}
	public void setLQE15(String lQE15) {
		LQE15 = lQE15;
	}
	public Date getLQE16() {
		return LQE16;
	}
	public void setLQE16(Date lQE16) {
		LQE16 = lQE16;
	}
	public String getLQE17() {
		return LQE17;
	}
	public void setLQE17(String lQE17) {
		LQE17 = lQE17;
	}
	public String getLQE18() {
		return LQE18;
	}
	public void setLQE18(String lQE18) {
		LQE18 = lQE18;
	}
	public Date getLQE19() {
		return LQE19;
	}
	public void setLQE19(Date lQE19) {
		LQE19 = lQE19;
	}
	public Date getLQE20() {
		return LQE20;
	}
	public void setLQE20(Date lQE20) {
		LQE20 = lQE20;
	}
	public Date getLQE21() {
		return LQE21;
	}
	public void setLQE21(Date lQE21) {
		LQE21 = lQE21;
	}
	public String getLQEPOS() {
		return LQEPOS;
	}
	public void setLQEPOS(String lQEPOS) {
		LQEPOS = lQEPOS;
	}
	public String getLQE22() {
		return LQE22;
	}
	public void setLQE22(String lQE22) {
		LQE22 = lQE22;
	}
	public Double getLQE23() {
		return LQE23;
	}
	public void setLQE23(Double lQE23) {
		LQE23 = lQE23;
	}
	public String getLQE24() {
		return LQE24;
	}
	public void setLQE24(String lQE24) {
		LQE24 = lQE24;
	}
	public Date getLQE25() {
		return LQE25;
	}
	public void setLQE25(Date lQE25) {
		LQE25 = lQE25;
	}
	public Double getTA_LQE01() {
		return TA_LQE01;
	}
	public void setTA_LQE01(Double tA_LQE01) {
		TA_LQE01 = tA_LQE01;
	}
	public Double getTA_LQE02() {
		return TA_LQE02;
	}
	public void setTA_LQE02(Double tA_LQE02) {
		TA_LQE02 = tA_LQE02;
	}
	public String getTA_LQE03() {
		return TA_LQE03;
	}
	public void setTA_LQE03(String tA_LQE03) {
		TA_LQE03 = tA_LQE03;
	}
	public String getTA_LQE04() {
		return TA_LQE04;
	}
	public void setTA_LQE04(String tA_LQE04) {
		TA_LQE04 = tA_LQE04;
	}
	public Date getTA_LQE05() {
		return TA_LQE05;
	}
	public void setTA_LQE05(Date tA_LQE05) {
		TA_LQE05 = tA_LQE05;
	}
	public String getTA_LQE06() {
		return TA_LQE06;
	}
	public void setTA_LQE06(String tA_LQE06) {
		TA_LQE06 = tA_LQE06;
	}
	public String getTA_LQE07() {
		return TA_LQE07;
	}
	public void setTA_LQE07(String tA_LQE07) {
		TA_LQE07 = tA_LQE07;
	}
	public int getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(int rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getTQA02() {
		return TQA02;
	}
	public void setTQA02(String tQA02) {
		TQA02 = tQA02;
	}
}
