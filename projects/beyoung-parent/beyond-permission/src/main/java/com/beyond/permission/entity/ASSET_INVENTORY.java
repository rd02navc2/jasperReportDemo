package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ASSET_INVENTORY")
@IdClass(ASSET_INVENTORY_ComposeKey.class)
public class ASSET_INVENTORY implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String inventory_date;
	@Id
	private String self_no;
	private String model_no;
	private String type;
	private String prod_desc;
	private String owner_id;
	private String owner_name;
	private String title;
	private String dept_name;
	private String confirm_id;
	private Date confirm_date;
	private String maintain_hist;
	
	private String type_name;
	private String generate_type;
	private Date close_date;
	
	public Date getClose_date() {
		return close_date;
	}
	public void setClose_date(Date close_date) {
		this.close_date = close_date;
	}
	public String getGenerate_type() {
		return generate_type;
	}
	public void setGenerate_type(String generate_type) {
		this.generate_type = generate_type;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getInventory_date() {
		return inventory_date;
	}
	public void setInventory_date(String inventory_date) {
		this.inventory_date = inventory_date;
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
	public String getConfirm_id() {
		return confirm_id;
	}
	public void setConfirm_id(String confirm_id) {
		this.confirm_id = confirm_id;
	}
	public Date getConfirm_date() {
		return confirm_date;
	}
	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}
	public String getMaintain_hist() {
		return maintain_hist;
	}
	public void setMaintain_hist(String maintain_hist) {
		this.maintain_hist = maintain_hist;
	}

}

class ASSET_INVENTORY_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String inventory_date;
	private String self_no;

	public String getInventory_date() {
		return inventory_date;
	}

	public void setInventory_date(String inventory_date) {
		this.inventory_date = inventory_date;
	}

	public String getSelf_no() {
		return self_no;
	}

	public void setSelf_no(String self_no) {
		this.self_no = self_no;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ASSET_INVENTORY_ComposeKey) {
			final ASSET_INVENTORY_ComposeKey other = (ASSET_INVENTORY_ComposeKey) obj;
			if (inventory_date == other.inventory_date && self_no == other.self_no)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
