package com.beyond.report.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "POINT_TRANSFER")
public class POINT_TRANSFER {
	
	@Id
	@GeneratedValue
	private Integer sn;
	private String old_card_id;
	private String old_mcard_id;
	private Double old_card_point1;
	private Double old_card_point2;
	private String new_card_id;
	private String new_mcard_id;
	private Double new_card_point1;
	private Double new_card_point2;
	private String is_delete;
	private String is_main_card;
	private String invoice_no;
	private Double promote_amt;
	private String access_id;
	private Date access_date;
	
	@Transient
	private int rec_cnt;

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getOld_card_id() {
		return old_card_id;
	}

	public void setOld_card_id(String old_card_id) {
		this.old_card_id = old_card_id;
	}

	public String getNew_card_id() {
		return new_card_id;
	}

	public void setNew_card_id(String new_card_id) {
		this.new_card_id = new_card_id;
	}

	public Double getNew_card_point1() {
		return new_card_point1;
	}

	public void setNew_card_point1(Double new_card_point1) {
		this.new_card_point1 = new_card_point1;
	}

	public Double getNew_card_point2() {
		return new_card_point2;
	}

	public void setNew_card_point2(Double new_card_point2) {
		this.new_card_point2 = new_card_point2;
	}

	public String getAccess_id() {
		return access_id;
	}

	public void setAccess_id(String access_id) {
		this.access_id = access_id;
	}

	public Date getAccess_date() {
		return access_date;
	}

	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}

	public int getRec_cnt() {
		return rec_cnt;
	}

	public void setRec_cnt(int rec_cnt) {
		this.rec_cnt = rec_cnt;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Double getOld_card_point1() {
		return old_card_point1;
	}

	public void setOld_card_point1(Double old_card_point1) {
		this.old_card_point1 = old_card_point1;
	}

	public Double getOld_card_point2() {
		return old_card_point2;
	}

	public void setOld_card_point2(Double old_card_point2) {
		this.old_card_point2 = old_card_point2;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public Double getPromote_amt() {
		return promote_amt;
	}

	public void setPromote_amt(Double promote_amt) {
		this.promote_amt = promote_amt;
	}

	public String getOld_mcard_id() {
		return old_mcard_id;
	}

	public void setOld_mcard_id(String old_mcard_id) {
		this.old_mcard_id = old_mcard_id;
	}

	public String getNew_mcard_id() {
		return new_mcard_id;
	}

	public void setNew_mcard_id(String new_mcard_id) {
		this.new_mcard_id = new_mcard_id;
	}

	public String getIs_main_card() {
		return is_main_card;
	}

	public void setIs_main_card(String is_main_card) {
		this.is_main_card = is_main_card;
	}
}

