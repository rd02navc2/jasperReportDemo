package com.beyoung.surrounding.pos.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "POS_COUPON_DATA")
@Table(name = "COUPON_DATA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class COUPON_DATA implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private String GC_NO;
	
	private String GC_FLAG;
	private Integer GC_AMT;
	private String PKEY;
	private String ACT_CD;
	private String UPD_NAME;
	private Date UPD_DATE;
	private String CHG_DATE;
	private String CHG_REG;
	private String CHG_STORE;
	private String LAST_NAME;
	private Date LAST_DATE;
	
	private String ISAPP;
}