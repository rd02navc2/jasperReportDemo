package com.beyond.report.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TC_PSB_FILE")
@IdClass(TC_PSB_FILE_ComposeKey.class)
public class TC_PSB_FILE implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tc_psbplant", length = 50)
	private String TC_PSBPLANT;
	@Id
	@Column(name = "tc_psb01", length = 50)
	private String TC_PSB01;
	@Id
	@Column(name = "tc_psb02", length = 50)
	private String TC_PSB02;
	@Id
	@Column(name = "tc_psb03", length = 50)
	private String TC_PSB03;
	@Id
	@Column(name = "tc_psb04")
	private Date TC_PSB04;
	private String TC_PSB05;
	@Id
	@Column(name = "tc_psb06")
	private Integer TC_PSB06;
	private String TC_PSB07;
	private String TC_PSB08;
	private Integer TC_PSB09;
	private Double TC_PSB10;
	private Double TC_PSB11;
	private Double TC_PSB12;
	private Double TC_PSB13;
	private Double TC_PSB14;
	private String TC_PSB15;
	private String TC_PSB16;
	private String TC_PSB17;
	private String TC_PSB18;
	private String TC_PSB19;
	private Double TC_PSB20;
	private String TC_PSB21;
	private Integer TC_PSB22;
	private Double TC_PSB23;
	private Double TC_PSB13A;
	private Double TC_PSB13B;
	
	private String LNT04;
	private String IMA02;
	private String IMA25;
	
	@Transient
	private Integer rec_cnt;

	public String getIMA02() {
		return IMA02;
	}
	public void setIMA02(String iMA02) {
		IMA02 = iMA02;
	}
	public Integer getRec_cnt() {
		return rec_cnt;
	}
	public void setRec_cnt(Integer rec_cnt) {
		this.rec_cnt = rec_cnt;
	}
	public String getIMA25() {
		return IMA25;
	}
	public void setIMA25(String iMA25) {
		IMA25 = iMA25;
	}
	public String getLNT04() {
		return LNT04;
	}
	public void setLNT04(String lNT04) {
		LNT04 = lNT04;
	}
	public String getTC_PSBPLANT() {
		return TC_PSBPLANT;
	}
	public void setTC_PSBPLANT(String tC_PSBPLANT) {
		TC_PSBPLANT = tC_PSBPLANT;
	}
	public String getTC_PSB01() {
		return TC_PSB01;
	}
	public void setTC_PSB01(String tC_PSB01) {
		TC_PSB01 = tC_PSB01;
	}
	public String getTC_PSB02() {
		return TC_PSB02;
	}
	public void setTC_PSB02(String tC_PSB02) {
		TC_PSB02 = tC_PSB02;
	}
	public String getTC_PSB03() {
		return TC_PSB03;
	}
	public void setTC_PSB03(String tC_PSB03) {
		TC_PSB03 = tC_PSB03;
	}
	public Date getTC_PSB04() {
		return TC_PSB04;
	}
	public void setTC_PSB04(Date tC_PSB04) {
		TC_PSB04 = tC_PSB04;
	}
	public String getTC_PSB05() {
		return TC_PSB05;
	}
	public void setTC_PSB05(String tC_PSB05) {
		TC_PSB05 = tC_PSB05;
	}
	public Integer getTC_PSB06() {
		return TC_PSB06;
	}
	public void setTC_PSB06(Integer tC_PSB06) {
		TC_PSB06 = tC_PSB06;
	}
	public String getTC_PSB07() {
		return TC_PSB07;
	}
	public void setTC_PSB07(String tC_PSB07) {
		TC_PSB07 = tC_PSB07;
	}
	public String getTC_PSB08() {
		return TC_PSB08;
	}
	public void setTC_PSB08(String tC_PSB08) {
		TC_PSB08 = tC_PSB08;
	}
	public Integer getTC_PSB09() {
		return TC_PSB09;
	}
	public void setTC_PSB09(Integer tC_PSB09) {
		TC_PSB09 = tC_PSB09;
	}
	public Double getTC_PSB10() {
		return TC_PSB10;
	}
	public void setTC_PSB10(Double tC_PSB10) {
		TC_PSB10 = tC_PSB10;
	}
	public Double getTC_PSB11() {
		return TC_PSB11;
	}
	public void setTC_PSB11(Double tC_PSB11) {
		TC_PSB11 = tC_PSB11;
	}
	public Double getTC_PSB12() {
		return TC_PSB12;
	}
	public void setTC_PSB12(Double tC_PSB12) {
		TC_PSB12 = tC_PSB12;
	}
	public Double getTC_PSB13() {
		return TC_PSB13;
	}
	public void setTC_PSB13(Double tC_PSB13) {
		TC_PSB13 = tC_PSB13;
	}
	public Double getTC_PSB14() {
		return TC_PSB14;
	}
	public void setTC_PSB14(Double tC_PSB14) {
		TC_PSB14 = tC_PSB14;
	}
	public String getTC_PSB15() {
		return TC_PSB15;
	}
	public void setTC_PSB15(String tC_PSB15) {
		TC_PSB15 = tC_PSB15;
	}
	public String getTC_PSB16() {
		return TC_PSB16;
	}
	public void setTC_PSB16(String tC_PSB16) {
		TC_PSB16 = tC_PSB16;
	}
	public String getTC_PSB17() {
		return TC_PSB17;
	}
	public void setTC_PSB17(String tC_PSB17) {
		TC_PSB17 = tC_PSB17;
	}
	public String getTC_PSB18() {
		return TC_PSB18;
	}
	public void setTC_PSB18(String tC_PSB18) {
		TC_PSB18 = tC_PSB18;
	}
	public String getTC_PSB19() {
		return TC_PSB19;
	}
	public void setTC_PSB19(String tC_PSB19) {
		TC_PSB19 = tC_PSB19;
	}
	public Double getTC_PSB20() {
		return TC_PSB20;
	}
	public void setTC_PSB20(Double tC_PSB20) {
		TC_PSB20 = tC_PSB20;
	}
	public String getTC_PSB21() {
		return TC_PSB21;
	}
	public void setTC_PSB21(String tC_PSB21) {
		TC_PSB21 = tC_PSB21;
	}
	public Integer getTC_PSB22() {
		return TC_PSB22;
	}
	public void setTC_PSB22(Integer tC_PSB22) {
		TC_PSB22 = tC_PSB22;
	}
	public Double getTC_PSB23() {
		return TC_PSB23;
	}
	public void setTC_PSB23(Double tC_PSB23) {
		TC_PSB23 = tC_PSB23;
	}
	public Double getTC_PSB13A() {
		return TC_PSB13A;
	}
	public void setTC_PSB13A(Double tC_PSB13A) {
		TC_PSB13A = tC_PSB13A;
	}
	public Double getTC_PSB13B() {
		return TC_PSB13B;
	}
	public void setTC_PSB13B(Double tC_PSB13B) {
		TC_PSB13B = tC_PSB13B;
	}
}