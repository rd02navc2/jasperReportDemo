package com.beyond.report.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IT_REQUEST")
public class IT_REQUEST {
	
	@Id
	private String request_no;
	private Date request_date;
	private String apply_id;
	private String apply_name;
	private String apply_email;
	private String apply_ext;
	private String apply_dept;
	private String apply_dept_manager1;
	private String apply_dept_manager1_email;
	private String apply_dept_manager2;
	private String apply_dept_manager2_email;
	private Date expect_date;
	private String status;
	private String request_brief;
	private String request_detail;
	private String it_memo;
	private String is_pos;
	private String counter_no;
	private String counter_name;
	private Date pos_in_date;
	private Date pos_out_date;
	private String pos_desk;
	private String pos_public;
	private String pos_floor;
	private String pos_quick_key;
	private String pos_inv;
	private String pos_easypay;
	private String pos_pay_type1;
	private String pos_pay_type2;
	private String pos_pay_type3;
	private Date approve_dept_manager_date;
	private String approve_dept_manager_id;
	private Date reject_dept_manager_date;
	private String reject_dept_manager_id;
	private String reject_dept_manager_reason;
	private Date approve_it_manager1_date;
	private String approve_it_manager1_id;
	private Date reject_it_manager1_date;
	private String reject_it_manager1_id;
	private String reject_it_manager1_reason;
	private Date approve_it_manager2_date;
	private String approve_it_manager2_id;
	private Date reject_it_manager2_date;
	private String reject_it_manager2_id;
	private String reject_it_manager2_reason;
	private Date receive_it_date;
	private String receive_it_id;
	private Date complete_it_date;
	private String complete_it_id;

	private String a00_file;
	private String a01_file;
	private String a02_file;
	private String a03_file;
	private String is_closed;
	private String mtn_h_pos;
	private String mtn_h_receipt;
	private String mtn_h_creditcard;
	private String mtn_s_3;
	private String mtn_s_pos;
	private String mtn_s_receipt;
	private String mtn_s_windows;
	private String mtn_o_artificial;
	private String mtn_o_network;
	private String mtn_o_power;
	private String mtn_o_pc;
	private String mtn_o_daily;
	private String mtn_o;
	private String mtn_o_reason;

	@Transient
	private java.math.BigInteger iMtn_h_pos;
	@Transient
	private java.math.BigInteger iMtn_h_receipt;
	@Transient
	private java.math.BigInteger iMtn_h_creditcard;
	@Transient
	private java.math.BigInteger iMtn_s_3;
	@Transient
	private java.math.BigInteger iMtn_s_pos;
	@Transient
	private java.math.BigInteger iMtn_s_receipt;
	@Transient
	private java.math.BigInteger iMtn_s_windows;
	@Transient
	private java.math.BigInteger iMtn_o_artificial;
	@Transient
	private java.math.BigInteger iMtn_o_network;
	@Transient
	private java.math.BigInteger iMtn_o_power;
	@Transient
	private java.math.BigInteger iMtn_o_pc;
	@Transient
	private java.math.BigInteger iMtn_o_daily;
	@Transient
	private java.math.BigInteger iMtn_o;
	@Transient
	private java.math.BigInteger iIs_pos;
	@Transient
	private java.math.BigInteger iMtn_total;

	@Transient
	private String status_name;

	@Transient
	private java.math.BigInteger sno;
	
	@Transient
	private Integer rec_cnt;

	public String getRequest_no() {
		return request_no;
	}

	public void setRequest_no(String request_no) {
		this.request_no = request_no;
	}

	public Date getRequest_date() {
		return request_date;
	}

	public void setRequest_date(Date request_date) {
		this.request_date = request_date;
	}

	public String getApply_id() {
		return apply_id;
	}

	public void setApply_id(String apply_id) {
		this.apply_id = apply_id;
	}

	public String getApply_name() {
		return apply_name;
	}

	public void setApply_name(String apply_name) {
		this.apply_name = apply_name;
	}

	public String getApply_email() {
		return apply_email;
	}

	public void setApply_email(String apply_email) {
		this.apply_email = apply_email;
	}

	public String getApply_ext() {
		return apply_ext;
	}

	public void setApply_ext(String apply_ext) {
		this.apply_ext = apply_ext;
	}

	public String getApply_dept() {
		return apply_dept;
	}

	public void setApply_dept(String apply_dept) {
		this.apply_dept = apply_dept;
	}

	public String getApply_dept_manager1() {
		return apply_dept_manager1;
	}

	public void setApply_dept_manager1(String apply_dept_manager1) {
		this.apply_dept_manager1 = apply_dept_manager1;
	}

	public String getApply_dept_manager1_email() {
		return apply_dept_manager1_email;
	}

	public void setApply_dept_manager1_email(String apply_dept_manager1_email) {
		this.apply_dept_manager1_email = apply_dept_manager1_email;
	}

	public String getApply_dept_manager2() {
		return apply_dept_manager2;
	}

	public void setApply_dept_manager2(String apply_dept_manager2) {
		this.apply_dept_manager2 = apply_dept_manager2;
	}

	public String getApply_dept_manager2_email() {
		return apply_dept_manager2_email;
	}

	public void setApply_dept_manager2_email(String apply_dept_manager2_email) {
		this.apply_dept_manager2_email = apply_dept_manager2_email;
	}

	public Date getExpect_date() {
		return expect_date;
	}

	public void setExpect_date(Date expect_date) {
		this.expect_date = expect_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequest_brief() {
		return request_brief;
	}

	public void setRequest_brief(String request_brief) {
		this.request_brief = request_brief;
	}

	public String getRequest_detail() {
		return request_detail;
	}

	public void setRequest_detail(String request_detail) {
		this.request_detail = request_detail;
	}

	public String getIt_memo() {
		return it_memo;
	}

	public void setIt_memo(String it_memo) {
		this.it_memo = it_memo;
	}

	public String getIs_pos() {
		return is_pos;
	}

	public void setIs_pos(String is_pos) {
		this.is_pos = is_pos;
	}

	public String getCounter_no() {
		return counter_no;
	}

	public void setCounter_no(String counter_no) {
		this.counter_no = counter_no;
	}

	public String getCounter_name() {
		return counter_name;
	}

	public void setCounter_name(String counter_name) {
		this.counter_name = counter_name;
	}

	public Date getPos_in_date() {
		return pos_in_date;
	}

	public void setPos_in_date(Date pos_in_date) {
		this.pos_in_date = pos_in_date;
	}

	public Date getPos_out_date() {
		return pos_out_date;
	}

	public void setPos_out_date(Date pos_out_date) {
		this.pos_out_date = pos_out_date;
	}

	public String getPos_desk() {
		return pos_desk;
	}

	public void setPos_desk(String pos_desk) {
		this.pos_desk = pos_desk;
	}

	public String getPos_public() {
		return pos_public;
	}

	public void setPos_public(String pos_public) {
		this.pos_public = pos_public;
	}

	public String getPos_floor() {
		return pos_floor;
	}

	public void setPos_floor(String pos_floor) {
		this.pos_floor = pos_floor;
	}

	public String getPos_quick_key() {
		return pos_quick_key;
	}

	public void setPos_quick_key(String pos_quick_key) {
		this.pos_quick_key = pos_quick_key;
	}

	public String getPos_inv() {
		return pos_inv;
	}

	public void setPos_inv(String pos_inv) {
		this.pos_inv = pos_inv;
	}

	public String getPos_easypay() {
		return pos_easypay;
	}

	public void setPos_easypay(String pos_easypay) {
		this.pos_easypay = pos_easypay;
	}

	public String getPos_pay_type1() {
		return pos_pay_type1;
	}

	public void setPos_pay_type1(String pos_pay_type1) {
		this.pos_pay_type1 = pos_pay_type1;
	}

	public String getPos_pay_type2() {
		return pos_pay_type2;
	}

	public void setPos_pay_type2(String pos_pay_type2) {
		this.pos_pay_type2 = pos_pay_type2;
	}

	public String getPos_pay_type3() {
		return pos_pay_type3;
	}

	public void setPos_pay_type3(String pos_pay_type3) {
		this.pos_pay_type3 = pos_pay_type3;
	}

	public Date getApprove_dept_manager_date() {
		return approve_dept_manager_date;
	}

	public void setApprove_dept_manager_date(Date approve_dept_manager_date) {
		this.approve_dept_manager_date = approve_dept_manager_date;
	}

	public String getApprove_dept_manager_id() {
		return approve_dept_manager_id;
	}

	public void setApprove_dept_manager_id(String approve_dept_manager_id) {
		this.approve_dept_manager_id = approve_dept_manager_id;
	}

	public Date getReject_dept_manager_date() {
		return reject_dept_manager_date;
	}

	public void setReject_dept_manager_date(Date reject_dept_manager_date) {
		this.reject_dept_manager_date = reject_dept_manager_date;
	}

	public String getReject_dept_manager_id() {
		return reject_dept_manager_id;
	}

	public void setReject_dept_manager_id(String reject_dept_manager_id) {
		this.reject_dept_manager_id = reject_dept_manager_id;
	}

	public String getReject_dept_manager_reason() {
		return reject_dept_manager_reason;
	}

	public void setReject_dept_manager_reason(String reject_dept_manager_reason) {
		this.reject_dept_manager_reason = reject_dept_manager_reason;
	}

	public Date getApprove_it_manager1_date() {
		return approve_it_manager1_date;
	}

	public void setApprove_it_manager1_date(Date approve_it_manager1_date) {
		this.approve_it_manager1_date = approve_it_manager1_date;
	}

	public String getApprove_it_manager1_id() {
		return approve_it_manager1_id;
	}

	public void setApprove_it_manager1_id(String approve_it_manager1_id) {
		this.approve_it_manager1_id = approve_it_manager1_id;
	}

	public Date getReject_it_manager1_date() {
		return reject_it_manager1_date;
	}

	public void setReject_it_manager1_date(Date reject_it_manager1_date) {
		this.reject_it_manager1_date = reject_it_manager1_date;
	}

	public String getReject_it_manager1_id() {
		return reject_it_manager1_id;
	}

	public void setReject_it_manager1_id(String reject_it_manager1_id) {
		this.reject_it_manager1_id = reject_it_manager1_id;
	}

	public String getReject_it_manager1_reason() {
		return reject_it_manager1_reason;
	}

	public void setReject_it_manager1_reason(String reject_it_manager1_reason) {
		this.reject_it_manager1_reason = reject_it_manager1_reason;
	}

	public Date getApprove_it_manager2_date() {
		return approve_it_manager2_date;
	}

	public void setApprove_it_manager2_date(Date approve_it_manager2_date) {
		this.approve_it_manager2_date = approve_it_manager2_date;
	}

	public String getApprove_it_manager2_id() {
		return approve_it_manager2_id;
	}

	public void setApprove_it_manager2_id(String approve_it_manager2_id) {
		this.approve_it_manager2_id = approve_it_manager2_id;
	}

	public Date getReject_it_manager2_date() {
		return reject_it_manager2_date;
	}

	public void setReject_it_manager2_date(Date reject_it_manager2_date) {
		this.reject_it_manager2_date = reject_it_manager2_date;
	}

	public String getReject_it_manager2_id() {
		return reject_it_manager2_id;
	}

	public void setReject_it_manager2_id(String reject_it_manager2_id) {
		this.reject_it_manager2_id = reject_it_manager2_id;
	}

	public String getReject_it_manager2_reason() {
		return reject_it_manager2_reason;
	}

	public void setReject_it_manager2_reason(String reject_it_manager2_reason) {
		this.reject_it_manager2_reason = reject_it_manager2_reason;
	}

	public Date getReceive_it_date() {
		return receive_it_date;
	}

	public void setReceive_it_date(Date receive_it_date) {
		this.receive_it_date = receive_it_date;
	}

	public String getReceive_it_id() {
		return receive_it_id;
	}

	public void setReceive_it_id(String receive_it_id) {
		this.receive_it_id = receive_it_id;
	}

	public Date getComplete_it_date() {
		return complete_it_date;
	}

	public void setComplete_it_date(Date complete_it_date) {
		this.complete_it_date = complete_it_date;
	}

	public String getComplete_it_id() {
		return complete_it_id;
	}

	public void setComplete_it_id(String complete_it_id) {
		this.complete_it_id = complete_it_id;
	}

	public String getA00_file() {
		return a00_file;
	}

	public void setA00_file(String a00_file) {
		this.a00_file = a00_file;
	}

	public String getA01_file() {
		return a01_file;
	}

	public void setA01_file(String a01_file) {
		this.a01_file = a01_file;
	}

	public String getA02_file() {
		return a02_file;
	}

	public void setA02_file(String a02_file) {
		this.a02_file = a02_file;
	}

	public String getA03_file() {
		return a03_file;
	}

	public void setA03_file(String a03_file) {
		this.a03_file = a03_file;
	}

	public String getIs_closed() {
		return is_closed;
	}

	public void setIs_closed(String is_closed) {
		this.is_closed = is_closed;
	}

	public String getMtn_h_pos() {
		return mtn_h_pos;
	}

	public void setMtn_h_pos(String mtn_h_pos) {
		this.mtn_h_pos = mtn_h_pos;
	}

	public String getMtn_h_receipt() {
		return mtn_h_receipt;
	}

	public void setMtn_h_receipt(String mtn_h_receipt) {
		this.mtn_h_receipt = mtn_h_receipt;
	}

	public String getMtn_h_creditcard() {
		return mtn_h_creditcard;
	}

	public void setMtn_h_creditcard(String mtn_h_creditcard) {
		this.mtn_h_creditcard = mtn_h_creditcard;
	}

	public String getMtn_s_3() {
		return mtn_s_3;
	}

	public void setMtn_s_3(String mtn_s_3) {
		this.mtn_s_3 = mtn_s_3;
	}

	public String getMtn_s_pos() {
		return mtn_s_pos;
	}

	public void setMtn_s_pos(String mtn_s_pos) {
		this.mtn_s_pos = mtn_s_pos;
	}

	public String getMtn_s_receipt() {
		return mtn_s_receipt;
	}

	public void setMtn_s_receipt(String mtn_s_receipt) {
		this.mtn_s_receipt = mtn_s_receipt;
	}

	public String getMtn_s_windows() {
		return mtn_s_windows;
	}

	public void setMtn_s_windows(String mtn_s_windows) {
		this.mtn_s_windows = mtn_s_windows;
	}

	public String getMtn_o_artificial() {
		return mtn_o_artificial;
	}

	public void setMtn_o_artificial(String mtn_o_artificial) {
		this.mtn_o_artificial = mtn_o_artificial;
	}

	public String getMtn_o_network() {
		return mtn_o_network;
	}

	public void setMtn_o_network(String mtn_o_network) {
		this.mtn_o_network = mtn_o_network;
	}

	public String getMtn_o_power() {
		return mtn_o_power;
	}

	public void setMtn_o_power(String mtn_o_power) {
		this.mtn_o_power = mtn_o_power;
	}

	public String getMtn_o_pc() {
		return mtn_o_pc;
	}

	public void setMtn_o_pc(String mtn_o_pc) {
		this.mtn_o_pc = mtn_o_pc;
	}

	public String getMtn_o_daily() {
		return mtn_o_daily;
	}

	public void setMtn_o_daily(String mtn_o_daily) {
		this.mtn_o_daily = mtn_o_daily;
	}

	public String getMtn_o() {
		return mtn_o;
	}

	public void setMtn_o(String mtn_o) {
		this.mtn_o = mtn_o;
	}

	public String getMtn_o_reason() {
		return mtn_o_reason;
	}

	public void setMtn_o_reason(String mtn_o_reason) {
		this.mtn_o_reason = mtn_o_reason;
	}

	public java.math.BigInteger getiMtn_h_pos() {
		return iMtn_h_pos;
	}

	public void setiMtn_h_pos(java.math.BigInteger iMtn_h_pos) {
		this.iMtn_h_pos = iMtn_h_pos;
	}

	public java.math.BigInteger getiMtn_h_receipt() {
		return iMtn_h_receipt;
	}

	public void setiMtn_h_receipt(java.math.BigInteger iMtn_h_receipt) {
		this.iMtn_h_receipt = iMtn_h_receipt;
	}

	public java.math.BigInteger getiMtn_h_creditcard() {
		return iMtn_h_creditcard;
	}

	public void setiMtn_h_creditcard(java.math.BigInteger iMtn_h_creditcard) {
		this.iMtn_h_creditcard = iMtn_h_creditcard;
	}

	public java.math.BigInteger getiMtn_s_3() {
		return iMtn_s_3;
	}

	public void setiMtn_s_3(java.math.BigInteger iMtn_s_3) {
		this.iMtn_s_3 = iMtn_s_3;
	}

	public java.math.BigInteger getiMtn_s_pos() {
		return iMtn_s_pos;
	}

	public void setiMtn_s_pos(java.math.BigInteger iMtn_s_pos) {
		this.iMtn_s_pos = iMtn_s_pos;
	}

	public java.math.BigInteger getiMtn_s_receipt() {
		return iMtn_s_receipt;
	}

	public void setiMtn_s_receipt(java.math.BigInteger iMtn_s_receipt) {
		this.iMtn_s_receipt = iMtn_s_receipt;
	}

	public java.math.BigInteger getiMtn_s_windows() {
		return iMtn_s_windows;
	}

	public void setiMtn_s_windows(java.math.BigInteger iMtn_s_windows) {
		this.iMtn_s_windows = iMtn_s_windows;
	}

	public java.math.BigInteger getiMtn_o_artificial() {
		return iMtn_o_artificial;
	}

	public void setiMtn_o_artificial(java.math.BigInteger iMtn_o_artificial) {
		this.iMtn_o_artificial = iMtn_o_artificial;
	}

	public java.math.BigInteger getiMtn_o_network() {
		return iMtn_o_network;
	}

	public void setiMtn_o_network(java.math.BigInteger iMtn_o_network) {
		this.iMtn_o_network = iMtn_o_network;
	}

	public java.math.BigInteger getiMtn_o_power() {
		return iMtn_o_power;
	}

	public void setiMtn_o_power(java.math.BigInteger iMtn_o_power) {
		this.iMtn_o_power = iMtn_o_power;
	}

	public java.math.BigInteger getiMtn_o_pc() {
		return iMtn_o_pc;
	}

	public void setiMtn_o_pc(java.math.BigInteger iMtn_o_pc) {
		this.iMtn_o_pc = iMtn_o_pc;
	}

	public java.math.BigInteger getiMtn_o_daily() {
		return iMtn_o_daily;
	}

	public void setiMtn_o_daily(java.math.BigInteger iMtn_o_daily) {
		this.iMtn_o_daily = iMtn_o_daily;
	}

	public java.math.BigInteger getiMtn_o() {
		return iMtn_o;
	}

	public void setiMtn_o(java.math.BigInteger iMtn_o) {
		this.iMtn_o = iMtn_o;
	}

	public java.math.BigInteger getiMtn_total() {
		return iMtn_total;
	}

	public void setiMtn_total(java.math.BigInteger iMtn_total) {
		this.iMtn_total = iMtn_total;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public java.math.BigInteger getSno() {
		return sno;
	}

	public void setSno(java.math.BigInteger sno) {
		this.sno = sno;
	}

	public Integer getRec_cnt() {
		return rec_cnt;
	}

	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}

	public java.math.BigInteger getiIs_pos() {
		return iIs_pos;
	}

	public void setiIs_pos(java.math.BigInteger iIs_pos) {
		this.iIs_pos = iIs_pos;
	}

}
