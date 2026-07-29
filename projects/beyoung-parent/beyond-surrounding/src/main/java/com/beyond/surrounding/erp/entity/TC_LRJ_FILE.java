package com.beyond.surrounding.erp.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "ErpTcLrjFile")
@Table(name = "TC_LRJ_FILE")
@IdClass(TC_LRJ_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor  // JPA 必須要有無參構造函數
@AllArgsConstructor
@Builder
public class TC_LRJ_FILE implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String TC_LRJ01;
	@Id
	private String TC_LRJ02;
	@Id
	private String TC_LRJ09;
	private Double TC_LRJ03;
	@Id
	private String TC_LRJPLANT;
	private Double TC_LRJ04;
	private Double TC_LRJ05;
	private String TC_LRJ06;
	private String TC_LRJ07;
	private String TC_LRJ08;
	private String TC_LRJACTI;
	
	public String getTC_LRJ01() {
		return TC_LRJ01;
	}
	public void setTC_LRJ01(String tC_LRJ01) {
		TC_LRJ01 = tC_LRJ01;
	}
	public String getTC_LRJ02() {
		return TC_LRJ02;
	}
	public void setTC_LRJ02(String tC_LRJ02) {
		TC_LRJ02 = tC_LRJ02;
	}
	public String getTC_LRJ09() {
		return TC_LRJ09;
	}
	public void setTC_LRJ09(String tC_LRJ09) {
		TC_LRJ09 = tC_LRJ09;
	}
	public Double getTC_LRJ03() {
		return TC_LRJ03;
	}
	public void setTC_LRJ03(Double tC_LRJ03) {
		TC_LRJ03 = tC_LRJ03;
	}
	public String getTC_LRJPLANT() {
		return TC_LRJPLANT;
	}
	public void setTC_LRJPLANT(String tC_LRJPLANT) {
		TC_LRJPLANT = tC_LRJPLANT;
	}
	public Double getTC_LRJ04() {
		return TC_LRJ04;
	}
	public void setTC_LRJ04(Double tC_LRJ04) {
		TC_LRJ04 = tC_LRJ04;
	}
	public Double getTC_LRJ05() {
		return TC_LRJ05;
	}
	public void setTC_LRJ05(Double tC_LRJ05) {
		TC_LRJ05 = tC_LRJ05;
	}
	public String getTC_LRJ06() {
		return TC_LRJ06;
	}
	public void setTC_LRJ06(String tC_LRJ06) {
		TC_LRJ06 = tC_LRJ06;
	}
	public String getTC_LRJ07() {
		return TC_LRJ07;
	}
	public void setTC_LRJ07(String tC_LRJ07) {
		TC_LRJ07 = tC_LRJ07;
	}
	public String getTC_LRJ08() {
		return TC_LRJ08;
	}
	public void setTC_LRJ08(String tC_LRJ08) {
		TC_LRJ08 = tC_LRJ08;
	}
	public String getTC_LRJACTI() {
		return TC_LRJACTI;
	}
	public void setTC_LRJACTI(String tC_LRJACTI) {
		TC_LRJACTI = tC_LRJACTI;
	}
}
