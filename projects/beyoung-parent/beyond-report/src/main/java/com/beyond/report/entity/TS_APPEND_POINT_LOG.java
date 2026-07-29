package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TS_APPEND_POINT_LOG")
@IdClass(TS_APPEND_POINT_LOG_ComposeKey.class)
public class TS_APPEND_POINT_LOG implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "TC_PSAPLANT", length = 50)
	private String TC_PSAPLANT;
	@Id
	@Column(name = "TC_PSA01", length = 50)
	private String TC_PSA01;
	@Id
	@Column(name = "TC_PSA02", length = 50)
	private String TC_PSA02;
	@Id
	@Column(name = "TC_PSA03", length = 50)
	private String TC_PSA03;
	@Id
	private Date TC_PSA04;
	private String TC_PSA12;
	private String TC_PSA13;
	private String TC_PSA16;
	private String TC_PSA17;
	private Double TC_PSA40;
	private String TC_PSC07;
	private String USERID;
	private Double POINT_BASE;
	private Double POINT;
	private Date ACCESS_DATE;
	private String ACCESS_ID;
	private String COUNTER_NAME;
	private String USER_NAME;
	
	public String getCOUNTER_NAME() {
		return COUNTER_NAME;
	}

	public void setCOUNTER_NAME(String cOUNTER_NAME) {
		COUNTER_NAME = cOUNTER_NAME;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public Double getPOINT_BASE() {
		return POINT_BASE;
	}

	public void setPOINT_BASE(Double pOINT_BASE) {
		POINT_BASE = pOINT_BASE;
	}

	public String getTC_PSA16() {
		return TC_PSA16;
	}

	public void setTC_PSA16(String tC_PSA16) {
		TC_PSA16 = tC_PSA16;
	}

	public String getTC_PSA17() {
		return TC_PSA17;
	}

	public void setTC_PSA17(String tC_PSA17) {
		TC_PSA17 = tC_PSA17;
	}

	public String getTC_PSC07() {
		return TC_PSC07;
	}

	public void setTC_PSC07(String tC_PSC07) {
		TC_PSC07 = tC_PSC07;
	}

	public String getACCESS_ID() {
		return ACCESS_ID;
	}

	public void setACCESS_ID(String aCCESS_ID) {
		ACCESS_ID = aCCESS_ID;
	}

	@Transient
	private Integer rec_cnt;

	@Transient
	private String name;
	
	@Transient
	private String invoice_date;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(String invoice_date) {
		this.invoice_date = invoice_date;
	}

	public Date getTC_PSA04() {
		return TC_PSA04;
	}

	public void setTC_PSA04(Date tC_PSA04) {
		TC_PSA04 = tC_PSA04;
	}

	public String getTC_PSAPLANT() {
		return TC_PSAPLANT;
	}

	public void setTC_PSAPLANT(String tC_PSAPLANT) {
		TC_PSAPLANT = tC_PSAPLANT;
	}

	public String getTC_PSA01() {
		return TC_PSA01;
	}

	public void setTC_PSA01(String tC_PSA01) {
		TC_PSA01 = tC_PSA01;
	}

	public String getTC_PSA02() {
		return TC_PSA02;
	}

	public void setTC_PSA02(String tC_PSA02) {
		TC_PSA02 = tC_PSA02;
	}

	public String getTC_PSA03() {
		return TC_PSA03;
	}

	public void setTC_PSA03(String tC_PSA03) {
		TC_PSA03 = tC_PSA03;
	}


	public String getTC_PSA12() {
		return TC_PSA12;
	}

	public void setTC_PSA12(String tC_PSA12) {
		TC_PSA12 = tC_PSA12;
	}

	public String getTC_PSA13() {
		return TC_PSA13;
	}

	public void setTC_PSA13(String tC_PSA13) {
		TC_PSA13 = tC_PSA13;
	}

	public Double getTC_PSA40() {
		return TC_PSA40;
	}

	public void setTC_PSA40(Double tC_PSA40) {
		TC_PSA40 = tC_PSA40;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public Double getPOINT() {
		return POINT;
	}

	public void setPOINT(Double pOINT) {
		POINT = pOINT;
	}

	public Date getACCESS_DATE() {
		return ACCESS_DATE;
	}

	public void setACCESS_DATE(Date aCCESS_DATE) {
		ACCESS_DATE = aCCESS_DATE;
	}

	public Integer getRec_cnt() {
		return rec_cnt;
	}

	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
}