package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "LPJ_FILE")
public class LPJ_FILE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String LPJ01;
	private String LPJ02;
	@Id
	private String LPJ03;
	private Date LPJ04;
	private Date LPJ05;
	private Double LPJ06;
	private Integer LPJ07;
	private Date LPJ08;
	private String LPJ09;
	private Date LPJ10;
	private Double LPJ11;
	private Double LPJ12;
	private Double LPJ13;
	private Double LPJ14;
	private Double LPJ15;
	private String LPJ16;
	private String LPJ17;
	private Date LPJ18;
	private String LPJ19;
	private String LPJ20;
	private Date LPJ21;
	private String LPJ22;
	private Date LPJ23;
	private String LPJ24;
	private Date LPJ25;
	private String LPJPOS;
	private String LPJ26;
	private Double TA_LPJ01;
	private Double TA_LPJ02;
	private Double TA_LPJ03;
	private String TA_LPJ04;
	
	private String LPK03; //身分證
	private String LPK04; 
	private String LPK05; //生日
	private String LPK06;
	private String LPK14;
	private String LPK15;
	private String LPK18;
	private String LPKUD02;
	
	public String getLPKUD02() {
		return LPKUD02;
	}
	public void setLPKUD02(String lPKUD02) {
		LPKUD02 = lPKUD02;
	}
	public String getLPK06() {
		return LPK06;
	}
	public void setLPK06(String lPK06) {
		LPK06 = lPK06;
	}
	public String getLPK15() {
		return LPK15;
	}
	public void setLPK15(String lPK15) {
		LPK15 = lPK15;
	}
	public String getLPK18() {
		return LPK18;
	}
	public void setLPK18(String lPK18) {
		LPK18 = lPK18;
	}

	public String getLPK05() {
		return LPK05;
	}
	public void setLPK05(String lPK05) {
		LPK05 = lPK05;
	}
	public String getLPK04() {
		return LPK04;
	}
	public void setLPK04(String lPK04) {
		LPK04 = lPK04;
	}
	public String getLPK03() {
		return LPK03;
	}
	public void setLPK03(String lPK03) {
		LPK03 = lPK03;
	}
	public String getLPJ01() {
		return LPJ01;
	}
	public void setLPJ01(String lPJ01) {
		LPJ01 = lPJ01;
	}
	public String getLPJ02() {
		return LPJ02;
	}
	public void setLPJ02(String lPJ02) {
		LPJ02 = lPJ02;
	}
	public String getLPJ03() {
		return LPJ03;
	}
	public void setLPJ03(String lPJ03) {
		LPJ03 = lPJ03;
	}
	public Date getLPJ04() {
		return LPJ04;
	}
	public void setLPJ04(Date lPJ04) {
		LPJ04 = lPJ04;
	}
	public Date getLPJ05() {
		return LPJ05;
	}
	public void setLPJ05(Date lPJ05) {
		LPJ05 = lPJ05;
	}
	public Double getLPJ06() {
		return LPJ06;
	}
	public void setLPJ06(Double lPJ06) {
		LPJ06 = lPJ06;
	}
	public Integer getLPJ07() {
		return LPJ07;
	}
	public void setLPJ07(Integer lPJ07) {
		LPJ07 = lPJ07;
	}
	public Date getLPJ08() {
		return LPJ08;
	}
	public void setLPJ08(Date lPJ08) {
		LPJ08 = lPJ08;
	}
	public String getLPJ09() {
		return LPJ09;
	}
	public void setLPJ09(String lPJ09) {
		LPJ09 = lPJ09;
	}
	public Date getLPJ10() {
		return LPJ10;
	}
	public void setLPJ10(Date lPJ10) {
		LPJ10 = lPJ10;
	}
	public Double getLPJ11() {
		return LPJ11;
	}
	public void setLPJ11(Double lPJ11) {
		LPJ11 = lPJ11;
	}
	public Double getLPJ12() {
		return LPJ12;
	}
	public void setLPJ12(Double lPJ12) {
		LPJ12 = lPJ12;
	}
	public Double getLPJ13() {
		return LPJ13;
	}
	public void setLPJ13(Double lPJ13) {
		LPJ13 = lPJ13;
	}
	public Double getLPJ14() {
		return LPJ14;
	}
	public void setLPJ14(Double lPJ14) {
		LPJ14 = lPJ14;
	}
	public Double getLPJ15() {
		return LPJ15;
	}
	public void setLPJ15(Double lPJ15) {
		LPJ15 = lPJ15;
	}
	public String getLPJ16() {
		return LPJ16;
	}
	public void setLPJ16(String lPJ16) {
		LPJ16 = lPJ16;
	}
	public String getLPJ17() {
		return LPJ17;
	}
	public void setLPJ17(String lPJ17) {
		LPJ17 = lPJ17;
	}
	public Date getLPJ18() {
		return LPJ18;
	}
	public void setLPJ18(Date lPJ18) {
		LPJ18 = lPJ18;
	}
	public String getLPJ19() {
		return LPJ19;
	}
	public void setLPJ19(String lPJ19) {
		LPJ19 = lPJ19;
	}
	public String getLPJ20() {
		return LPJ20;
	}
	public void setLPJ20(String lPJ20) {
		LPJ20 = lPJ20;
	}
	public Date getLPJ21() {
		return LPJ21;
	}
	public void setLPJ21(Date lPJ21) {
		LPJ21 = lPJ21;
	}
	public String getLPJ22() {
		return LPJ22;
	}
	public void setLPJ22(String lPJ22) {
		LPJ22 = lPJ22;
	}
	public Date getLPJ23() {
		return LPJ23;
	}
	public void setLPJ23(Date lPJ23) {
		LPJ23 = lPJ23;
	}
	public String getLPJ24() {
		return LPJ24;
	}
	public void setLPJ24(String lPJ24) {
		LPJ24 = lPJ24;
	}
	public Date getLPJ25() {
		return LPJ25;
	}
	public void setLPJ25(Date lPJ25) {
		LPJ25 = lPJ25;
	}
	public String getLPJPOS() {
		return LPJPOS;
	}
	public void setLPJPOS(String lPJPOS) {
		LPJPOS = lPJPOS;
	}
	public String getLPJ26() {
		return LPJ26;
	}
	public void setLPJ26(String lPJ26) {
		LPJ26 = lPJ26;
	}
	public Double getTA_LPJ01() {
		return TA_LPJ01;
	}
	public void setTA_LPJ01(Double tA_LPJ01) {
		TA_LPJ01 = tA_LPJ01;
	}
	public Double getTA_LPJ02() {
		return TA_LPJ02;
	}
	public void setTA_LPJ02(Double tA_LPJ02) {
		TA_LPJ02 = tA_LPJ02;
	}
	public Double getTA_LPJ03() {
		return TA_LPJ03;
	}
	public void setTA_LPJ03(Double tA_LPJ03) {
		TA_LPJ03 = tA_LPJ03;
	}
	public String getTA_LPJ04() {
		return TA_LPJ04;
	}
	public void setTA_LPJ04(String tA_LPJ04) {
		TA_LPJ04 = tA_LPJ04;
	}
	public String getLPK14() {
		return LPK14;
	}
	public void setLPK14(String lPK14) {
		LPK14 = lPK14;
	}
}

