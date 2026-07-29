package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Product_Main")
@IdClass(PRODUCT_MAIN_ComposeKey.class)
public class PRODUCT_MAIN implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String c_no;
	@Id
	private String CustomizedMainProductId;
	private String ProductId;
	private String SaleType;
	private String SaleTypeInfo;
	private String ProductName;
	private String type0_no;
	private String type1_no;
	private String type2_no;
	private String type3_no;
	private String type4_no;
	private String type5_no;
	private Integer MarketPrice;
	private Integer SalePrice;
	private Integer CostPrice;
	private String SpecTypeDimension;
	private String Stock;
	private String Stock1;
	private String SaftyStock;
	private String MaxBuyNum;
	private String ShortDescription;
	private String VideoPath;
	private String LongDescription;
	private String SpecDimension1;
	private String SpecDimension2;
	private String a00_file;
	private String a01_file;
	private String a02_file;
	private String a03_file;
	private String a04_file;
	private String a05_file;
	private String a06_file;
	private String a07_file;
	private String a08_file;
	private String a09_file;
	private String status_id;
	private Date access_date;
	private String access_id;
	private String CustomizedProductId;
	private String BarCode;
	private Date approve_date;
	private String approve_id;
	
	private String c_name;
	
	private String status_name;
	
	@Transient
	private Integer rec_cnt;
	
	public String getStock1() {
		return Stock1;
	}
	public void setStock1(String stock1) {
		Stock1 = stock1;
	}
	public String getStatus_id() {
		return status_id;
	}
	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}
	public String getStatus_name() {
		return status_name;
	}
	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}
	public Integer getMarketPrice() {
		return MarketPrice;
	}
	public void setMarketPrice(Integer marketPrice) {
		MarketPrice = marketPrice;
	}
	public Integer getSalePrice() {
		return SalePrice;
	}
	public void setSalePrice(Integer salePrice) {
		SalePrice = salePrice;
	}
	public Integer getCostPrice() {
		return CostPrice;
	}
	public void setCostPrice(Integer costPrice) {
		CostPrice = costPrice;
	}
	public Date getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
	}
	public String getApprove_id() {
		return approve_id;
	}
	public void setApprove_id(String approve_id) {
		this.approve_id = approve_id;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getC_no() {
		return c_no;
	}
	public void setC_no(String c_no) {
		this.c_no = c_no;
	}
	public String getCustomizedMainProductId() {
		return CustomizedMainProductId;
	}
	public void setCustomizedMainProductId(String customizedMainProductId) {
		CustomizedMainProductId = customizedMainProductId;
	}
	public String getProductId() {
		return ProductId;
	}
	public void setProductId(String productId) {
		ProductId = productId;
	}
	public String getSaleType() {
		return SaleType;
	}
	public void setSaleType(String saleType) {
		SaleType = saleType;
	}
	public String getSaleTypeInfo() {
		return SaleTypeInfo;
	}
	public void setSaleTypeInfo(String saleTypeInfo) {
		SaleTypeInfo = saleTypeInfo;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getType0_no() {
		return type0_no;
	}
	public void setType0_no(String type0_no) {
		this.type0_no = type0_no;
	}
	public String getType1_no() {
		return type1_no;
	}
	public void setType1_no(String type1_no) {
		this.type1_no = type1_no;
	}
	public String getType2_no() {
		return type2_no;
	}
	public void setType2_no(String type2_no) {
		this.type2_no = type2_no;
	}
	public String getType3_no() {
		return type3_no;
	}
	public void setType3_no(String type3_no) {
		this.type3_no = type3_no;
	}
	public String getType4_no() {
		return type4_no;
	}
	public void setType4_no(String type4_no) {
		this.type4_no = type4_no;
	}
	public String getType5_no() {
		return type5_no;
	}
	public void setType5_no(String type5_no) {
		this.type5_no = type5_no;
	}
	public String getSpecTypeDimension() {
		return SpecTypeDimension;
	}
	public void setSpecTypeDimension(String specTypeDimension) {
		SpecTypeDimension = specTypeDimension;
	}
	public String getStock() {
		return Stock;
	}
	public void setStock(String stock) {
		Stock = stock;
	}
	public String getSaftyStock() {
		return SaftyStock;
	}
	public void setSaftyStock(String saftyStock) {
		SaftyStock = saftyStock;
	}
	public String getMaxBuyNum() {
		return MaxBuyNum;
	}
	public void setMaxBuyNum(String maxBuyNum) {
		MaxBuyNum = maxBuyNum;
	}
	public String getShortDescription() {
		return ShortDescription;
	}
	public void setShortDescription(String shortDescription) {
		ShortDescription = shortDescription;
	}
	public String getVideoPath() {
		return VideoPath;
	}
	public void setVideoPath(String videoPath) {
		VideoPath = videoPath;
	}
	public String getLongDescription() {
		return LongDescription;
	}
	public void setLongDescription(String longDescription) {
		LongDescription = longDescription;
	}
	public String getSpecDimension1() {
		return SpecDimension1;
	}
	public void setSpecDimension1(String specDimension1) {
		SpecDimension1 = specDimension1;
	}
	public String getSpecDimension2() {
		return SpecDimension2;
	}
	public void setSpecDimension2(String specDimension2) {
		SpecDimension2 = specDimension2;
	}
	public String getA00_file() {
		return a00_file;
	}
	public void setA00_file(String a00_file) {
		this.a00_file = a00_file;
	}
	public String getA01_file() {
		return a01_file;
	}
	public void setA01_file(String a01_file) {
		this.a01_file = a01_file;
	}
	public String getA02_file() {
		return a02_file;
	}
	public void setA02_file(String a02_file) {
		this.a02_file = a02_file;
	}
	public String getA03_file() {
		return a03_file;
	}
	public void setA03_file(String a03_file) {
		this.a03_file = a03_file;
	}
	public String getA04_file() {
		return a04_file;
	}
	public void setA04_file(String a04_file) {
		this.a04_file = a04_file;
	}
	public String getA05_file() {
		return a05_file;
	}
	public void setA05_file(String a05_file) {
		this.a05_file = a05_file;
	}
	public String getA06_file() {
		return a06_file;
	}
	public void setA06_file(String a06_file) {
		this.a06_file = a06_file;
	}
	public String getA07_file() {
		return a07_file;
	}
	public void setA07_file(String a07_file) {
		this.a07_file = a07_file;
	}
	public String getA08_file() {
		return a08_file;
	}
	public void setA08_file(String a08_file) {
		this.a08_file = a08_file;
	}
	public String getA09_file() {
		return a09_file;
	}
	public void setA09_file(String a09_file) {
		this.a09_file = a09_file;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
	public String getAccess_id() {
		return access_id;
	}
	public void setAccess_id(String access_id) {
		this.access_id = access_id;
	}
	public String getCustomizedProductId() {
		return CustomizedProductId;
	}
	public void setCustomizedProductId(String customizedProductId) {
		CustomizedProductId = customizedProductId;
	}
	public String getBarCode() {
		return BarCode;
	}
	public void setBarCode(String barCode) {
		BarCode = barCode;
	}
}

class PRODUCT_MAIN_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String c_no;
	private String CustomizedMainProductId;

	public String getC_no() {
		return c_no;
	}

	public void setC_no(String c_no) {
		this.c_no = c_no;
	}

	public String getCustomizedMainProductId() {
		return CustomizedMainProductId;
	}

	public void setCustomizedMainProductId(String customizedMainProductId) {
		CustomizedMainProductId = customizedMainProductId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PRODUCT_MAIN_ComposeKey) {
			final PRODUCT_MAIN_ComposeKey other = (PRODUCT_MAIN_ComposeKey) obj;
			if (c_no == other.c_no && CustomizedMainProductId == other.CustomizedMainProductId)
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
