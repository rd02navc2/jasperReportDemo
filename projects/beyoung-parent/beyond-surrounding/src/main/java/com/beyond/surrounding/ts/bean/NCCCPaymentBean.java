package com.beyond.surrounding.ts.bean;

import java.util.List;

import com.beyond.surrounding.bean.ResponseBean;

public class NCCCPaymentBean extends ResponseBean{
	private String mid;
	private String send_date;
	private String serial_no;
	private Integer total_cnt;
	private String amt_sign;
	private Integer amt;
	private List<NCCCPaymentDetailBean> detail;
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getSerial_no() {
		return serial_no;
	}
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}
	public Integer getTotal_cnt() {
		return total_cnt;
	}
	public void setTotal_cnt(Integer total_cnt) {
		this.total_cnt = total_cnt;
	}
	public String getAmt_sign() {
		return amt_sign;
	}
	public void setAmt_sign(String amt_sign) {
		this.amt_sign = amt_sign;
	}
	public Integer getAmt() {
		return amt;
	}
	public void setAmt(Integer amt) {
		this.amt = amt;
	}
	public List<NCCCPaymentDetailBean> getDetail() {
		return detail;
	}
	public void setDetail(List<NCCCPaymentDetailBean> detail) {
		this.detail = detail;
	}
}
