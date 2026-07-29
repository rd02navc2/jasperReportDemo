package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "ASSET_SCRAPPED")
public class ASSET_SCRAPPED implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String self_no;
	private String model_no;
	private String type;
	private String prod_desc;
	private String owner_id;
	private String owner_name;
	private String title;
	private String dept_name;
	private String maintain_hist;
	private String scrapped_id;
	private Date scrapped_date;
	private String memo_hist;
	
	public String getMemo_hist() {
		return memo_hist;
	}
	public void setMemo_hist(String memo_hist) {
		this.memo_hist = memo_hist;
	}
	private String type_name;
	
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getSelf_no() {
		return self_no;
	}
	public void setSelf_no(String self_no) {
		this.self_no = self_no;
	}
	public String getModel_no() {
		return model_no;
	}
	public void setModel_no(String model_no) {
		this.model_no = model_no;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProd_desc() {
		return prod_desc;
	}
	public void setProd_desc(String prod_desc) {
		this.prod_desc = prod_desc;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getOwner_name() {
		return owner_name;
	}
	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getMaintain_hist() {
		return maintain_hist;
	}
	public void setMaintain_hist(String maintain_hist) {
		this.maintain_hist = maintain_hist;
	}
	public String getScrapped_id() {
		return scrapped_id;
	}
	public void setScrapped_id(String scrapped_id) {
		this.scrapped_id = scrapped_id;
	}
	public Date getScrapped_date() {
		return scrapped_date;
	}
	public void setScrapped_date(Date scrapped_date) {
		this.scrapped_date = scrapped_date;
	}
}

