package com.beyond.permission.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CENTURY_UPLOAD")
@IdClass(CENTURY_UPLOAD_ComposeKey.class)
public class CENTURY_UPLOAD implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String CinemaClass,CinemaClass1,CinemaClass2,CinemaClass3,CinemaClass4,CinemaClass5;
	private String NOW_DATE;
	private double Cust,Cust_Ly;	//~~每日來客~~AmosW
	private double EDay,EDay_Ly;	//~~每日業績~~AmosW
	private double Food;
	private double MonthCust,MonthCust_Ly;
	private double MonthEDay,MonthEDay_Ly;
	private double MonthFood;
	private double YearCust,YearCust_Ly;
	private double YearEDay,YearEDay_Ly;
	private double YearFood;
	
	public String getCinemaClass5() {
		return CinemaClass5;
	}
	public void setCinemaClass5(String cinemaClass5) {
		CinemaClass5 = cinemaClass5;
	}
	public String getCinemaClass4() {
		return CinemaClass4;
	}
	public void setCinemaClass4(String cinemaClass4) {
		CinemaClass4 = cinemaClass4;
	}
	public String getCinemaClass3() {
		return CinemaClass3;
	}
	public void setCinemaClass3(String cinemaClass3) {
		CinemaClass3 = cinemaClass3;
	}
	public String getCinemaClass2() {
		return CinemaClass2;
	}
	public void setCinemaClass2(String cinemaClass2) {
		CinemaClass2 = cinemaClass2;
	}
	public String getCinemaClass1() {
		return CinemaClass1;
	}
	public void setCinemaClass1(String cinemaClass1) {
		CinemaClass1 = cinemaClass1;
	}
	public String getCinemaClass() {
		return CinemaClass;
	}
	public void setCinemaClass(String cinemaClass) {
		CinemaClass = cinemaClass;
	}
	public String getNOW_DATE() {
		return NOW_DATE;
	}
	public void setNOW_DATE(String nOW_DATE) {
		NOW_DATE = nOW_DATE;
	}
	public double getCUST() {
		return Cust;
	}
	public void setCUST(double cUST) {
		Cust = cUST;
	}
	public double getCUST_LY() {
		return Cust_Ly;
	}
	public void setCUST_LY(double cUST_LY) {
		Cust_Ly = cUST_LY;
	}
	public double getEDAY() {
		return EDay;
	}
	public void setEDAY(double eDAY) {
		EDay = eDAY;
	}
	public double getEDAY_LY() {
		return EDay_Ly;
	}
	public void setEDAY_LY(double eDAY_LY) {
		EDay_Ly = eDAY_LY;
	}
	public double getFOOD() {
		return Food;
	}
	public void setFOOD(double fOOD) {
		Food = fOOD;
	}
	public double getMONTHCUST() {
		return MonthCust;
	}
	public void setMONTHCUST(double mONTHCUST) {
		this.MonthCust = mONTHCUST;
	}
	public double getMONTHCUST_LY() {
		return MonthCust_Ly;
	}
	public void setMONTHCUST_LY(double mONTHCUST_LY) {
		this.MonthCust_Ly = mONTHCUST_LY;
	}
	public double getMONTHEDAY() {
		return MonthEDay;
	}
	public void setMONTHEDAY(double mONTHEDAY) {
		this.MonthEDay = mONTHEDAY;
	}
	public double getMONTHEDAY_LY() {
		return MonthEDay_Ly;
	}
	public void setMONTHEDAY_LY(double mONTHEDAY_LY) {
		this.MonthEDay_Ly = mONTHEDAY_LY;
	}
	public double getMONTHFOOD() {
		return MonthFood;
	}
	public void setMONTHFOOD(double mONTHFOOD) {
		this.MonthFood = mONTHFOOD;
	}	
	public double getYEARCUST() {
		return YearCust;
	}
	public void setYEARCUST(double yEARCUST) {
		this.YearCust = yEARCUST;
	}
	public double getYEARCUST_LY() {
		return YearCust_Ly;
	}
	public void setYEARCUST_LY(double yEARCUST_LY) {
		this.YearCust_Ly = yEARCUST_LY;
	}
	public double getYEAREDAY() {
		return YearEDay;
	}
	public void setYEAREDAY(double yEAREDAY) {
		this.YearEDay = yEAREDAY;
	}
	public double getYEAREDAY_LY() {
		return YearEDay_Ly;
	}
	public void setYEAREDAY_LY(double yEAREDAY_LY) {
		this.YearEDay_Ly = yEAREDAY_LY;
	}
	public double getYEARFOOD() {
		return YearFood;
	}
	public void setYEARFOOD(double yEARFOOD) {
		this.YearFood = yEARFOOD;
	}
}

class CENTURY_UPLOAD_ComposeKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String CinemaClass;
	private String CinemaClass1;
	private String CinemaClass2;
	private String CinemaClass3;
	private String CinemaClass4;
	private String CinemaClass5;
	
	public String getCinemaClass() {
		return CinemaClass;
	}
	public void setCinemaClass(String cinemaClass) {
		this.CinemaClass = cinemaClass;
	}
	
	public String getCinemaClass1() {
		return CinemaClass1;
	}
	public void setCinemaClass1(String cinemaClass1) {
		this.CinemaClass1 = cinemaClass1;
	}
	
	public String getCinemaClass2() {
		return CinemaClass2;
	}
	public void setCinemaClass2(String cinemaClass2) {
		this.CinemaClass2 = cinemaClass2;
	}
	
	public String getCinemaClass3() {
		return CinemaClass3;
	}
	public void setCinemaClass3(String cinemaClass3) {
		this.CinemaClass3 = cinemaClass3;
	}
	
	public String getCinemaClass4() {
		return CinemaClass4;
	}
	public void setCinemaClass4(String cinemaClass4) {
		this.CinemaClass4 = cinemaClass4;
	}
	
	public String getCinemaClass5() {
		return CinemaClass5;
	}
	public void setCinemaClass5(String cinemaClass5) {
		this.CinemaClass5 = cinemaClass5;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CENTURY_UPLOAD_ComposeKey) {
			final CENTURY_UPLOAD_ComposeKey other = (CENTURY_UPLOAD_ComposeKey) obj;
			if (CinemaClass == other.CinemaClass && CinemaClass1 == other.CinemaClass1 && CinemaClass2 == other.CinemaClass2 && CinemaClass3 == other.CinemaClass3 && CinemaClass4 == other.CinemaClass4 && CinemaClass5 == other.CinemaClass5)
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
