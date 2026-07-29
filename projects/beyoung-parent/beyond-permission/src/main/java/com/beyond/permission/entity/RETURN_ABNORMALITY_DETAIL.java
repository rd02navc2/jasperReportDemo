package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Return_Abnormality_Detail")
@IdClass(RETURN_ABNORMALITY_DETAIL_ComposeKey.class)
public class RETURN_ABNORMALITY_DETAIL {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	@Id
	private String order_id;
	@Id
	private String return_id;
	@Id
	private String return_abnormality_id;
	
	private Date ReturnAbnormalityCreateDate;
	private String ReturnAbnormalityReason;
	private String ReturnAbnormalityRemark;
	private String ReturnAbnormalityStatus;
	private Date access_date;
	
	public Date getReturnAbnormalityCreateDate() {
		return ReturnAbnormalityCreateDate;
	}
	public void setReturnAbnormalityCreateDate(Date returnAbnormalityCreateDate) {
		ReturnAbnormalityCreateDate = returnAbnormalityCreateDate;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getReturn_id() {
		return return_id;
	}
	public void setReturn_id(String return_id) {
		this.return_id = return_id;
	}
	public String getReturn_abnormality_id() {
		return return_abnormality_id;
	}
	public void setReturn_abnormality_id(String return_abnormality_id) {
		this.return_abnormality_id = return_abnormality_id;
	}
	public String getReturnAbnormalityReason() {
		return ReturnAbnormalityReason;
	}
	public void setReturnAbnormalityReason(String returnAbnormalityReason) {
		ReturnAbnormalityReason = returnAbnormalityReason;
	}
	public String getReturnAbnormalityRemark() {
		return ReturnAbnormalityRemark;
	}
	public void setReturnAbnormalityRemark(String returnAbnormalityRemark) {
		ReturnAbnormalityRemark = returnAbnormalityRemark;
	}
	public String getReturnAbnormalityStatus() {
		return ReturnAbnormalityStatus;
	}
	public void setReturnAbnormalityStatus(String returnAbnormalityStatus) {
		ReturnAbnormalityStatus = returnAbnormalityStatus;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}	

class RETURN_ABNORMALITY_DETAIL_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String transaction_id;
	private String order_id;
	private String return_id;
	private String return_abnormality_id;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RETURN_ABNORMALITY_DETAIL_ComposeKey) {
			final RETURN_ABNORMALITY_DETAIL_ComposeKey other = (RETURN_ABNORMALITY_DETAIL_ComposeKey) obj;
			if (transaction_id == other.transaction_id && order_id == other.order_id && return_id == other.return_id && return_abnormality_id == other.return_abnormality_id)
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
