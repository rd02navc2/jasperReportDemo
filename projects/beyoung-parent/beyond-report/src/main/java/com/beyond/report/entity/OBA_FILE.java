package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "OBA_FILE")
public class OBA_FILE implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String OBA01;
	private String OBA02;
	
	private String LNT09;
	private String LNT06;
	private String TQA02;

	private Date LNT21;
	private Date LNT22;

	@Transient
	private String EXCEL_LNT09;
	
	@Transient
	private String EXCEL_OBA01;

	@Transient
	private Integer rec_cnt;

	public String getEXCEL_LNT09() {
		return EXCEL_LNT09;
	}
	public void setEXCEL_LNT09(String eXCEL_LNT09) {
		EXCEL_LNT09 = eXCEL_LNT09;
	}
	public String getEXCEL_OBA01() {
		return EXCEL_OBA01;
	}
	public void setEXCEL_OBA01(String eXCEL_OBA01) {
		EXCEL_OBA01 = eXCEL_OBA01;
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
	public String getLNT06() {
		return LNT06;
	}
	public void setLNT06(String lNT06) {
		LNT06 = lNT06;
	}
	public String getTQA02() {
		return TQA02;
	}
	public void setTQA02(String tQA02) {
		TQA02 = tQA02;
	}
	public String getLNT09() {
		return LNT09;
	}
	public void setLNT09(String lNT09) {
		LNT09 = lNT09;
	}
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
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}

}

