package com.beyond.surrounding.erp.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "ErpTcLriFile")
@Table(name = "TC_LRI_FILE")
@IdClass(TC_LRI_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor  // JPA 必須要有無參構造函數
@AllArgsConstructor
@Builder
public class TC_LRI_FILE implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String TC_LRI01;
	@Id
	private String TC_LRI02;
	@Id
	private Double TC_LRI03;
	@Id
	private String TC_LRIPLANT;
	private String TC_LRI04;
	private String TC_LRI05;
	private Date TC_LRI06;
	private Date TC_LRI07;
	private String TC_LRI08;
	private Date TC_LRI09;
	private String TC_LRI10;
	private String TC_LRICONF; 
	private String TC_LRICONU; 
	private Date TC_LRICOND; 
	private String TC_LRIACTI; 
	private Date TC_LRICRAT; 
	private Date TC_LRIDATE; 
	private String TC_LRIGRUP; 
	private String TC_LRILEGAL; 
	private String TC_LRIMODU; 
	private String TC_LRIORIG; 
	private String TC_LRIORIU;
	private String TC_LRIUSER;
	
	public String getTC_LRI01() {
		return TC_LRI01;
	}
	public void setTC_LRI01(String tC_LRI01) {
		TC_LRI01 = tC_LRI01;
	}
	public String getTC_LRI02() {
		return TC_LRI02;
	}
	public void setTC_LRI02(String tC_LRI02) {
		TC_LRI02 = tC_LRI02;
	}
	public Double getTC_LRI03() {
		return TC_LRI03;
	}
	public void setTC_LRI03(Double tC_LRI03) {
		TC_LRI03 = tC_LRI03;
	}
	public String getTC_LRIPLANT() {
		return TC_LRIPLANT;
	}
	public void setTC_LRIPLANT(String tC_LRIPLANT) {
		TC_LRIPLANT = tC_LRIPLANT;
	}
	public String getTC_LRI04() {
		return TC_LRI04;
	}
	public void setTC_LRI04(String tC_LRI04) {
		TC_LRI04 = tC_LRI04;
	}
	public String getTC_LRI05() {
		return TC_LRI05;
	}
	public void setTC_LRI05(String tC_LRI05) {
		TC_LRI05 = tC_LRI05;
	}
	public Date getTC_LRI06() {
		return TC_LRI06;
	}
	public void setTC_LRI06(Date tC_LRI06) {
		TC_LRI06 = tC_LRI06;
	}
	public Date getTC_LRI07() {
		return TC_LRI07;
	}
	public void setTC_LRI07(Date tC_LRI07) {
		TC_LRI07 = tC_LRI07;
	}
	public String getTC_LRI08() {
		return TC_LRI08;
	}
	public void setTC_LRI08(String tC_LRI08) {
		TC_LRI08 = tC_LRI08;
	}
	public Date getTC_LRI09() {
		return TC_LRI09;
	}
	public void setTC_LRI09(Date tC_LRI09) {
		TC_LRI09 = tC_LRI09;
	}
	public String getTC_LRI10() {
		return TC_LRI10;
	}
	public void setTC_LRI10(String tC_LRI10) {
		TC_LRI10 = tC_LRI10;
	}
	public String getTC_LRICONF() {
		return TC_LRICONF;
	}
	public void setTC_LRICONF(String tC_LRICONF) {
		TC_LRICONF = tC_LRICONF;
	}
	public String getTC_LRICONU() {
		return TC_LRICONU;
	}
	public void setTC_LRICONU(String tC_LRICONU) {
		TC_LRICONU = tC_LRICONU;
	}
	public Date getTC_LRICOND() {
		return TC_LRICOND;
	}
	public void setTC_LRICOND(Date tC_LRICOND) {
		TC_LRICOND = tC_LRICOND;
	}
	public String getTC_LRIACTI() {
		return TC_LRIACTI;
	}
	public void setTC_LRIACTI(String tC_LRIACTI) {
		TC_LRIACTI = tC_LRIACTI;
	}
	public Date getTC_LRICRAT() {
		return TC_LRICRAT;
	}
	public void setTC_LRICRAT(Date tC_LRICRAT) {
		TC_LRICRAT = tC_LRICRAT;
	}
	public Date getTC_LRIDATE() {
		return TC_LRIDATE;
	}
	public void setTC_LRIDATE(Date tC_LRIDATE) {
		TC_LRIDATE = tC_LRIDATE;
	}
	public String getTC_LRIGRUP() {
		return TC_LRIGRUP;
	}
	public void setTC_LRIGRUP(String tC_LRIGRUP) {
		TC_LRIGRUP = tC_LRIGRUP;
	}
	public String getTC_LRILEGAL() {
		return TC_LRILEGAL;
	}
	public void setTC_LRILEGAL(String tC_LRILEGAL) {
		TC_LRILEGAL = tC_LRILEGAL;
	}
	public String getTC_LRIMODU() {
		return TC_LRIMODU;
	}
	public void setTC_LRIMODU(String tC_LRIMODU) {
		TC_LRIMODU = tC_LRIMODU;
	}
	public String getTC_LRIORIG() {
		return TC_LRIORIG;
	}
	public void setTC_LRIORIG(String tC_LRIORIG) {
		TC_LRIORIG = tC_LRIORIG;
	}
	public String getTC_LRIORIU() {
		return TC_LRIORIU;
	}
	public void setTC_LRIORIU(String tC_LRIORIU) {
		TC_LRIORIU = tC_LRIORIU;
	}
	public String getTC_LRIUSER() {
		return TC_LRIUSER;
	}
	public void setTC_LRIUSER(String tC_LRIUSER) {
		TC_LRIUSER = tC_LRIUSER;
	} 
	
}