package com.beyond.surrounding.pos.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "POS_COUPON_SALE")
@Table(name = "COUPON_SALE")
@IdClass(COUPON_SALE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class COUPON_SALE implements Serializable {
	
	private static final long serialVersionUID = 1L; 

	@Id
	private String PKEY;
	
	@Id
	private Integer SNO;
	
	private String RECEI_DATE;
	private String RECEI_NO;
	private String BURUI_CD;
	private Integer AMT;
	private String STATUS;
	private String PC_NO;
	private Integer ADD_GIVE;
	private Integer ADD_GIVE2;
	private String BRAND_CD;
	private String DEPT_CD;
	private String ISRETURN;
	private String UPD_NAME;
	private Date UPD_DATE;
	private Integer ADD_GIVE3;
	private String MEMO;
}
	
