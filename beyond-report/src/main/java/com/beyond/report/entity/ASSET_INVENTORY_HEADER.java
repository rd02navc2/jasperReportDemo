package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "ASSET_INVENTORY_HEADER")
public class ASSET_INVENTORY_HEADER implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String inventory_date;
	private Date close_date;
	
	private String generate_type;
	private String access_id;
	private Date access_date;
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGenerate_type() {
		return generate_type;
	}
	public void setGenerate_type(String generate_type) {
		this.generate_type = generate_type;
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
	public String getInventory_date() {
		return inventory_date;
	}
	public void setInventory_date(String inventory_date) {
		this.inventory_date = inventory_date;
	}
	public Date getClose_date() {
		return close_date;
	}
	public void setClose_date(Date close_date) {
		this.close_date = close_date;
	}
}
