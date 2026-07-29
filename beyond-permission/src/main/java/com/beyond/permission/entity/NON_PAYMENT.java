package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Non_Payment")
@IdClass(NON_PAYMENT_ComposeKey.class)
public class NON_PAYMENT {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	@Id
	private String order_id;
	private String c_no;
	private String pay_type;
	private String shipping_type;
	private String order_status;
	private Date order_date;
	private String ProductId;
	private String MallSpecId;
	private String CustomizedProductId;
	private String ProductName;
	private String SaleType;
	private String Spec;
	private Integer Amount;
	private Integer Subtotal;
	private String BuyerName;
	private String OrderStatusDesc;
	private Date access_date;
	
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	private String c_name;
	
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public String getMallSpecId() {
		return MallSpecId;
	}
	public void setMallSpecId(String mallSpecId) {
		MallSpecId = mallSpecId;
	}
	public String getBuyerName() {
		return BuyerName;
	}
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	public String getOrderStatusDesc() {
		return OrderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		OrderStatusDesc = orderStatusDesc;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getShipping_type() {
		return shipping_type;
	}
	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
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
	public String getSaleType() {
		return SaleType;
	}
	public void setSaleType(String saleType) {
		SaleType = saleType;
	}
	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	public Integer getAmount() {
		return Amount;
	}
	public void setAmount(Integer amount) {
		Amount = amount;
	}
	public Integer getSubtotal() {
		return Subtotal;
	}
	public void setSubtotal(Integer subtotal) {
		Subtotal = subtotal;
	}
}

class NON_PAYMENT_ComposeKey implements Serializable {
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
		if (obj instanceof NON_PAYMENT_ComposeKey) {
			final NON_PAYMENT_ComposeKey other = (NON_PAYMENT_ComposeKey) obj;
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
