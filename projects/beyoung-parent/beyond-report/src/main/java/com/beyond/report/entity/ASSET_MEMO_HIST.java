package com.beyond.report.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "ASSET_MEMO_HIST")
public class ASSET_MEMO_HIST implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String self_no;
	private String memo_hist;
	private String access_id;
	private Date access_date;
	
	public String getSelf_no() {
		return self_no;
	}
	public void setSelf_no(String self_no) {
		this.self_no = self_no;
	}
	public String getMemo_hist() {
		return memo_hist;
	}
	public void setMemo_hist(String memo_hist) {
		this.memo_hist = memo_hist;
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
	
}
