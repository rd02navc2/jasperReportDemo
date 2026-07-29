package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Spec_Dimension_All")
@IdClass(SPEC_DIMENSION_ALL_ComposeKey.class)
public class SPEC_DIMENSION_ALL implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String c_no;
	@Id
	private String CustomizedMainProductId;
	@Id
	private String SpecDimension;
	private String Stock;
	private String Stock1;
	private String SaftyStock;
	private String CustomizedProductId;
	private String BarCode;
	private String MallSpecId;
	private String control;
	private String PChome_ProductId;
	
	private String yahoo_deleted;
	private String rakuten_deleted;
	private String pchome_deleted;
	private String momo_deleted;
	
	public String getMomo_deleted() {
		return momo_deleted;
	}
	public void setMomo_deleted(String momo_deleted) {
		this.momo_deleted = momo_deleted;
	}
	public String getYahoo_deleted() {
		return yahoo_deleted;
	}
	public void setYahoo_deleted(String yahoo_deleted) {
		this.yahoo_deleted = yahoo_deleted;
	}
	public String getRakuten_deleted() {
		return rakuten_deleted;
	}
	public void setRakuten_deleted(String rakuten_deleted) {
		this.rakuten_deleted = rakuten_deleted;
	}
	public String getPchome_deleted() {
		return pchome_deleted;
	}
	public void setPchome_deleted(String pchome_deleted) {
		this.pchome_deleted = pchome_deleted;
	}
	public String getPChome_ProductId() {
		return PChome_ProductId;
	}
	public void setPChome_ProductId(String pChome_ProductId) {
		PChome_ProductId = pChome_ProductId;
	}
	public String getControl() {
		return control;
	}
	public void setControl(String control) {
		this.control = control;
	}
	public String getStock1() {
		return Stock1;
	}
	public void setStock1(String stock1) {
		Stock1 = stock1;
	}
	public String getMallSpecId() {
		return MallSpecId;
	}
	public void setMallSpecId(String mallSpecId) {
		MallSpecId = mallSpecId;
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
	public String getSpecDimension() {
		return SpecDimension;
	}
	public void setSpecDimension(String specDimension) {
		SpecDimension = specDimension;
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

class SPEC_DIMENSION_ALL_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String c_no;
	private String CustomizedMainProductId;
	private String SpecDimension;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SPEC_DIMENSION_ALL_ComposeKey) {
			final SPEC_DIMENSION_ALL_ComposeKey other = (SPEC_DIMENSION_ALL_ComposeKey) obj;
			if (c_no == other.c_no && CustomizedMainProductId == other.CustomizedMainProductId && SpecDimension == other.SpecDimension)
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
