package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Return_Abnormality_Code")
public class RETURN_ABNORMALITY_CODE implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String ReturnCode;
	private String Reason;
	private Date access_date;
	
	public String getReturnCode() {
		return ReturnCode;
	}
	public void setReturnCode(String returnCode) {
		ReturnCode = returnCode;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	
}
