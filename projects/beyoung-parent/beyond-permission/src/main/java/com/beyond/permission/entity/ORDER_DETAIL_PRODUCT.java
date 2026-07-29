package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Order_Detail_Product")
@IdClass(ORDER_DETAIL_PRODUCT_ComposeKey.class)
public class ORDER_DETAIL_PRODUCT {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String transaction_id;
	@Id
	private String order_id;
	@Id
	private String ProductId;
	@Id
	private String c_no;
	private String MallSpecId;
	private Integer Amount;
	private Integer OriginalPrice;
	private Integer ListPrice;
	private Integer UsedPoint;
	private Integer BasicPointDiscount;
	private Integer Subtotal;
	private String TaxType;
	private String ProductType;
	private String CustomizedProductId;
	private String ProductName;
	private String Spec;
	private Date create_date;
	private Date access_date;
	
//	@Transient
//	private String c_no;
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public Integer getAmount() {
		return Amount;
	}
	public void setAmount(Integer amount) {
		Amount = amount;
	}
	public Integer getOriginalPrice() {
		return OriginalPrice;
	}
	public void setOriginalPrice(Integer originalPrice) {
		OriginalPrice = originalPrice;
	}
	public Integer getListPrice() {
		return ListPrice;
	}
	public void setListPrice(Integer listPrice) {
		ListPrice = listPrice;
	}
	public Integer getUsedPoint() {
		return UsedPoint;
	}
	public void setUsedPoint(Integer usedPoint) {
		UsedPoint = usedPoint;
	}
	public Integer getBasicPointDiscount() {
		return BasicPointDiscount;
	}
	public void setBasicPointDiscount(Integer basicPointDiscount) {
		BasicPointDiscount = basicPointDiscount;
	}
	public Integer getSubtotal() {
		return Subtotal;
	}
	public void setSubtotal(Integer subtotal) {
		Subtotal = subtotal;
	}
	public String getTaxType() {
		return TaxType;
	}
	public void setTaxType(String taxType) {
		TaxType = taxType;
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

class ORDER_DETAIL_PRODUCT_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String transaction_id;
	private String order_id;
	private String ProductId;
	private String c_no;

	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
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
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ORDER_DETAIL_PRODUCT_ComposeKey) {
			final ORDER_DETAIL_PRODUCT_ComposeKey other = (ORDER_DETAIL_PRODUCT_ComposeKey) obj;
			if (transaction_id == other.transaction_id && order_id == other.order_id && ProductId == other.ProductId && c_no == other.c_no)
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
