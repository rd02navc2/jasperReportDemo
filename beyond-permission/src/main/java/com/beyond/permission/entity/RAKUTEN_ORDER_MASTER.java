package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rakuten_Order_Master")
@IdClass(RAKUTEN_ORDER_MASTER_ComposeKey.class)
public class RAKUTEN_ORDER_MASTER {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	@Id
	private String order_id;
	private String c_no;
	private String BuyerName;
	private String BuyerPhone;
	private String IsActivity;
	private String IsUseCoupon;
	private String pay_type;
	private Integer Installment;
	private String shipping_type;
	private String StoreType;
	private String StoreShippingType;
	private String TransactionRemark;
	private Integer TransactionPrice;
	private String order_status;
	private String OrderStatusDesc;
	private Date OrderCloseDate;
	private Date OrderPackageDate;
	private Date create_date;
	private Date access_date;
	
	private String ReceiverName;
	private Date TransferDate;
	private String ProductType;
	private String CustomizedProductId;
	private String ProductName;
	private Integer Amount;
	private Integer Subtotal;	
	private Date OrderShippingDate;	
	private Date BuyerConfirmDate;	
	private String InvoiceNo;
	private Date InvoiveDate;
	private Date EntryAccountDate;
	
	private Integer refundTotal;
	private Integer itemTotalBeforeDiscount;
	private Integer discountTotal;
	
	private String orderPackageId;
	private String orderPaymentId;
	
	public String getOrderPackageId() {
		return orderPackageId;
	}
	public void setOrderPackageId(String orderPackageId) {
		this.orderPackageId = orderPackageId;
	}
	public String getOrderPaymentId() {
		return orderPaymentId;
	}
	public void setOrderPaymentId(String orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}
	public Integer getRefundTotal() {
		return refundTotal;
	}
	public void setRefundTotal(Integer refundTotal) {
		this.refundTotal = refundTotal;
	}
	public Integer getItemTotalBeforeDiscount() {
		return itemTotalBeforeDiscount;
	}
	public void setItemTotalBeforeDiscount(Integer itemTotalBeforeDiscount) {
		this.itemTotalBeforeDiscount = itemTotalBeforeDiscount;
	}
	public Integer getDiscountTotal() {
		return discountTotal;
	}
	public void setDiscountTotal(Integer discountTotal) {
		this.discountTotal = discountTotal;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getReceiverName() {
		return ReceiverName;
	}
	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}
	public Date getTransferDate() {
		return TransferDate;
	}
	public void setTransferDate(Date transferDate) {
		TransferDate = transferDate;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
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
	public Date getOrderShippingDate() {
		return OrderShippingDate;
	}
	public void setOrderShippingDate(Date orderShippingDate) {
		OrderShippingDate = orderShippingDate;
	}
	public Date getBuyerConfirmDate() {
		return BuyerConfirmDate;
	}
	public void setBuyerConfirmDate(Date buyerConfirmDate) {
		BuyerConfirmDate = buyerConfirmDate;
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
	public Date getEntryAccountDate() {
		return EntryAccountDate;
	}
	public void setEntryAccountDate(Date entryAccountDate) {
		EntryAccountDate = entryAccountDate;
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
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getBuyerName() {
		return BuyerName;
	}
	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}
	public String getBuyerPhone() {
		return BuyerPhone;
	}
	public void setBuyerPhone(String buyerPhone) {
		BuyerPhone = buyerPhone;
	}
	public String getIsActivity() {
		return IsActivity;
	}
	public void setIsActivity(String isActivity) {
		IsActivity = isActivity;
	}
	public String getIsUseCoupon() {
		return IsUseCoupon;
	}
	public void setIsUseCoupon(String isUseCoupon) {
		IsUseCoupon = isUseCoupon;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public Integer getInstallment() {
		return Installment;
	}
	public void setInstallment(Integer installment) {
		Installment = installment;
	}
	public String getShipping_type() {
		return shipping_type;
	}
	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}
	public String getStoreType() {
		return StoreType;
	}
	public void setStoreType(String storeType) {
		StoreType = storeType;
	}
	public String getStoreShippingType() {
		return StoreShippingType;
	}
	public void setStoreShippingType(String storeShippingType) {
		StoreShippingType = storeShippingType;
	}
	public String getTransactionRemark() {
		return TransactionRemark;
	}
	public void setTransactionRemark(String transactionRemark) {
		TransactionRemark = transactionRemark;
	}
	public Integer getTransactionPrice() {
		return TransactionPrice;
	}
	public void setTransactionPrice(Integer transactionPrice) {
		TransactionPrice = transactionPrice;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getOrderStatusDesc() {
		return OrderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		OrderStatusDesc = orderStatusDesc;
	}
	public Date getOrderCloseDate() {
		return OrderCloseDate;
	}
	public void setOrderCloseDate(Date orderCloseDate) {
		OrderCloseDate = orderCloseDate;
	}
	public Date getOrderPackageDate() {
		return OrderPackageDate;
	}
	public void setOrderPackageDate(Date orderPackageDate) {
		OrderPackageDate = orderPackageDate;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}

class RAKUTEN_ORDER_MASTER_ComposeKey implements Serializable {
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
		if (obj instanceof RAKUTEN_ORDER_MASTER_ComposeKey) {
			final RAKUTEN_ORDER_MASTER_ComposeKey other = (RAKUTEN_ORDER_MASTER_ComposeKey) obj;
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
