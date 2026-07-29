package com.beyond.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TRANSACTION_LOG")
public class TRANSACTION_LOG {
	
	@Id
	private String ORDER_ID;
	private String POS_CENTER;
	private String POS_COUNTER_ID;
	private String POS_PRODUCT_NAME;
	private Integer POS_AMOUNT;
	private String ACCESS_DATE;
	private String TRANSACTION_TYPE;
	private String TRANSACTION_ID;
	private String TRANSACTION_DATE;
	private String CURRENCY;
	private String ONE_TIME_KEY;
	private String NEED_CHECK;
	private String REFUND_TRANSACTION_ID;
	private String REFUND_TRANSACTION_DATE;
	private String INVOICE_NO;

	@Transient
	private String accessdate;

	@Transient
	private Integer counter;
	
	@Transient
	private Integer rec_cnt;
	
	public String getAccessdate() {
		return accessdate;
	}
	public void setAccessdate(String accessdate) {
		this.accessdate = accessdate;
	}
	public Integer getCounter() {
		return counter;
	}
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public String getPOS_CENTER() {
		return POS_CENTER;
	}
	public void setPOS_CENTER(String pOS_CENTER) {
		POS_CENTER = pOS_CENTER;
	}
	public String getPOS_COUNTER_ID() {
		return POS_COUNTER_ID;
	}
	public void setPOS_COUNTER_ID(String pOS_COUNTER_ID) {
		POS_COUNTER_ID = pOS_COUNTER_ID;
	}
	public String getPOS_PRODUCT_NAME() {
		return POS_PRODUCT_NAME;
	}
	public void setPOS_PRODUCT_NAME(String pOS_PRODUCT_NAME) {
		POS_PRODUCT_NAME = pOS_PRODUCT_NAME;
	}
	public Integer getPOS_AMOUNT() {
		return POS_AMOUNT;
	}
	public void setPOS_AMOUNT(Integer pOS_AMOUNT) {
		POS_AMOUNT = pOS_AMOUNT;
	}
	public String getACCESS_DATE() {
		return ACCESS_DATE;
	}
	public void setACCESS_DATE(String aCCESS_DATE) {
		ACCESS_DATE = aCCESS_DATE;
	}
	public String getTRANSACTION_TYPE() {
		return TRANSACTION_TYPE;
	}
	public void setTRANSACTION_TYPE(String tRANSACTION_TYPE) {
		TRANSACTION_TYPE = tRANSACTION_TYPE;
	}
	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}
	public void setTRANSACTION_ID(String tRANSACTION_ID) {
		TRANSACTION_ID = tRANSACTION_ID;
	}
	public String getTRANSACTION_DATE() {
		return TRANSACTION_DATE;
	}
	public void setTRANSACTION_DATE(String tRANSACTION_DATE) {
		TRANSACTION_DATE = tRANSACTION_DATE;
	}
	public String getCURRENCY() {
		return CURRENCY;
	}
	public void setCURRENCY(String cURRENCY) {
		CURRENCY = cURRENCY;
	}
	public String getONE_TIME_KEY() {
		return ONE_TIME_KEY;
	}
	public void setONE_TIME_KEY(String oNE_TIME_KEY) {
		ONE_TIME_KEY = oNE_TIME_KEY;
	}
	public String getNEED_CHECK() {
		return NEED_CHECK;
	}
	public void setNEED_CHECK(String nEED_CHECK) {
		NEED_CHECK = nEED_CHECK;
	}
	public String getREFUND_TRANSACTION_ID() {
		return REFUND_TRANSACTION_ID;
	}
	public void setREFUND_TRANSACTION_ID(String rEFUND_TRANSACTION_ID) {
		REFUND_TRANSACTION_ID = rEFUND_TRANSACTION_ID;
	}
	public String getREFUND_TRANSACTION_DATE() {
		return REFUND_TRANSACTION_DATE;
	}
	public void setREFUND_TRANSACTION_DATE(String rEFUND_TRANSACTION_DATE) {
		REFUND_TRANSACTION_DATE = rEFUND_TRANSACTION_DATE;
	}
	public String getINVOICE_NO() {
		return INVOICE_NO;
	}
	public void setINVOICE_NO(String iNVOICE_NO) {
		INVOICE_NO = iNVOICE_NO;
	}
}
