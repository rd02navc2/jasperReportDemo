package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rakuten_Return_Detail")
@IdClass(RAKUTEN_RETURN_DETAIL_ComposeKey.class)
public class RAKUTEN_RETURN_DETAIL {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	@Id
	private String order_id;
	@Id
	private String CustomizedProductId;
	private String c_no;
	private Date ReturnCreateDate;
	private String ReturnReason;
	private String ReturnReasonRemark;
	private String ReturnPickupName;
	private String ReturnPickupMobile;
	private String ReturnPickupPhone;
	private String ReturnPickupZipcode;
	private String ReturnPickupAddress;
	private Integer ReturnPrice;
	private String ReturnStatus;
	private Date ReturnCloseDate;
	private Date ReturnDebitDate;
	private String ProductId;
	private String MallSpecId;
	private String ProductName;
	private String Spec;
	private Date access_date;
	private String return_id; //requestId
	private String ReturnStatusCode;
	private String ReturnStatusDesc;
	
	public String getReturnStatusCode() {
		return ReturnStatusCode;
	}
	public void setReturnStatusCode(String returnStatusCode) {
		ReturnStatusCode = returnStatusCode;
	}
	public String getReturnStatusDesc() {
		return ReturnStatusDesc;
	}
	public void setReturnStatusDesc(String returnStatusDesc) {
		ReturnStatusDesc = returnStatusDesc;
	}
	public String getReturn_id() {
		return return_id;
	}
	public void setReturn_id(String return_id) {
		this.return_id = return_id;
	}
	public Date getReturnCreateDate() {
		return ReturnCreateDate;
	}
	public void setReturnCreateDate(Date returnCreateDate) {
		ReturnCreateDate = returnCreateDate;
	}
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getMallSpecId() {
		return MallSpecId;
	}
	public void setMallSpecId(String mallSpecId) {
		MallSpecId = mallSpecId;
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
	public String getReturnReason() {
		return ReturnReason;
	}
	public void setReturnReason(String returnReason) {
		ReturnReason = returnReason;
	}
	public String getReturnReasonRemark() {
		return ReturnReasonRemark;
	}
	public void setReturnReasonRemark(String returnReasonRemark) {
		ReturnReasonRemark = returnReasonRemark;
	}
	public String getReturnPickupName() {
		return ReturnPickupName;
	}
	public void setReturnPickupName(String returnPickupName) {
		ReturnPickupName = returnPickupName;
	}
	public String getReturnPickupMobile() {
		return ReturnPickupMobile;
	}
	public void setReturnPickupMobile(String returnPickupMobile) {
		ReturnPickupMobile = returnPickupMobile;
	}
	public String getReturnPickupPhone() {
		return ReturnPickupPhone;
	}
	public void setReturnPickupPhone(String returnPickupPhone) {
		ReturnPickupPhone = returnPickupPhone;
	}
	public String getReturnPickupZipcode() {
		return ReturnPickupZipcode;
	}
	public void setReturnPickupZipcode(String returnPickupZipcode) {
		ReturnPickupZipcode = returnPickupZipcode;
	}
	public String getReturnPickupAddress() {
		return ReturnPickupAddress;
	}
	public void setReturnPickupAddress(String returnPickupAddress) {
		ReturnPickupAddress = returnPickupAddress;
	}
	public Integer getReturnPrice() {
		return ReturnPrice;
	}
	public void setReturnPrice(Integer returnPrice) {
		ReturnPrice = returnPrice;
	}
	public String getReturnStatus() {
		return ReturnStatus;
	}
	public void setReturnStatus(String returnStatus) {
		ReturnStatus = returnStatus;
	}
	public Date getReturnCloseDate() {
		return ReturnCloseDate;
	}
	public void setReturnCloseDate(Date returnCloseDate) {
		ReturnCloseDate = returnCloseDate;
	}
	public Date getReturnDebitDate() {
		return ReturnDebitDate;
	}
	public void setReturnDebitDate(Date returnDebitDate) {
		ReturnDebitDate = returnDebitDate;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getCustomizedProductId() {
		return CustomizedProductId;
	}
	public void setCustomizedProductId(String customizedProductId) {
		CustomizedProductId = customizedProductId;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}	

class RAKUTEN_RETURN_DETAIL_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String transaction_id;
	private String order_id;
	private String CustomizedProductId;

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

	public String getCustomizedProductId() {
		return CustomizedProductId;
	}

	public void setCustomizedProductId(String customizedProductId) {
		CustomizedProductId = customizedProductId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RAKUTEN_RETURN_DETAIL_ComposeKey) {
			final RAKUTEN_RETURN_DETAIL_ComposeKey other = (RAKUTEN_RETURN_DETAIL_ComposeKey) obj;
			if (transaction_id == other.transaction_id && order_id == other.order_id && CustomizedProductId == other.CustomizedProductId)
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
