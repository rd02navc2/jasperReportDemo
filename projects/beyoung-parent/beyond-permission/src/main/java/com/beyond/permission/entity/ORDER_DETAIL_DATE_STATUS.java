package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Order_Detail_Date_Status")
@IdClass(ORDER_DETAIL_DATE_STATUS_ComposeKey.class)
public class ORDER_DETAIL_DATE_STATUS {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	@Id
	private String order_id;
	private Date TransferDate;
	private Date LastShippingDate;
	private Date OrderShippingDate;
	private Date OrderCloseDate;
	private Date BuyerConfirmDate;
	private Date EntryAccountDate;
	private Date PickingDate;
	private Date OrderPackageDate;
	private String InvoiceNo;
	private Date InvoiveDate;
	private Date LastDeliveryDate;
	private String order_status;
	private String DeliverType;
	private String OrderNote;
	private String OrderStatusDesc;
	private String OrderShippingId;
	private Date create_date;
	private Date access_date;
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}	
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getDeliverType() {
		return DeliverType;
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
	public Date getTransferDate() {
		return TransferDate;
	}
	public void setTransferDate(Date transferDate) {
		TransferDate = transferDate;
	}
	public Date getLastShippingDate() {
		return LastShippingDate;
	}
	public void setLastShippingDate(Date lastShippingDate) {
		LastShippingDate = lastShippingDate;
	}
	public Date getOrderShippingDate() {
		return OrderShippingDate;
	}
	public void setOrderShippingDate(Date orderShippingDate) {
		OrderShippingDate = orderShippingDate;
	}
	public Date getOrderCloseDate() {
		return OrderCloseDate;
	}
	public void setOrderCloseDate(Date orderCloseDate) {
		OrderCloseDate = orderCloseDate;
	}
	public Date getBuyerConfirmDate() {
		return BuyerConfirmDate;
	}
	public void setBuyerConfirmDate(Date buyerConfirmDate) {
		BuyerConfirmDate = buyerConfirmDate;
	}
	public Date getEntryAccountDate() {
		return EntryAccountDate;
	}
	public void setEntryAccountDate(Date entryAccountDate) {
		EntryAccountDate = entryAccountDate;
	}
	public Date getPickingDate() {
		return PickingDate;
	}
	public void setPickingDate(Date pickingDate) {
		PickingDate = pickingDate;
	}
	public Date getOrderPackageDate() {
		return OrderPackageDate;
	}
	public void setOrderPackageDate(Date orderPackageDate) {
		OrderPackageDate = orderPackageDate;
	}
	public String getInvoiceNo() {
		return InvoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}
	public Date getInvoiveDate() {
		return InvoiveDate;
	}
	public void setInvoiveDate(Date invoiveDate) {
		InvoiveDate = invoiveDate;
	}
	public Date getLastDeliveryDate() {
		return LastDeliveryDate;
	}
	public void setLastDeliveryDate(Date lastDeliveryDate) {
		LastDeliveryDate = lastDeliveryDate;
	}
	public void setDeliverType(String deliverType) {
		DeliverType = deliverType;
	}
	public String getOrderNote() {
		return OrderNote;
	}
	public void setOrderNote(String orderNote) {
		OrderNote = orderNote;
	}
	public String getOrderStatusDesc() {
		return OrderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		OrderStatusDesc = orderStatusDesc;
	}
	public String getOrderShippingId() {
		return OrderShippingId;
	}
	public void setOrderShippingId(String orderShippingId) {
		OrderShippingId = orderShippingId;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}	
}

class ORDER_DETAIL_DATE_STATUS_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String transaction_id;
	private String order_id;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ORDER_DETAIL_DATE_STATUS_ComposeKey) {
			final ORDER_DETAIL_DATE_STATUS_ComposeKey other = (ORDER_DETAIL_DATE_STATUS_ComposeKey) obj;
			if (transaction_id == other.transaction_id && order_id == other.order_id)
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
