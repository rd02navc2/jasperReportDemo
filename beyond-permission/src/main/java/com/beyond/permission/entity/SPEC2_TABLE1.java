package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Spec2_Table1")
@IdClass(SPEC2_TABLE1_ComposeKey.class)
public class SPEC2_TABLE1 implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String c_no;
	@Id
	private String CustomizedMainProductId;
	@Id
	private String SpecDimension;
	private String MallSpecId;
	private String control;
	
	public String getControl() {
		return control;
	}
	public void setControl(String control) {
		this.control = control;
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
	
}

class SPEC2_TABLE1_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	private String c_no;
	private String CustomizedMainProductId;
	private String SpecDimension;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SPEC2_TABLE1_ComposeKey) {
			final SPEC2_TABLE1_ComposeKey other = (SPEC2_TABLE1_ComposeKey) obj;
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
