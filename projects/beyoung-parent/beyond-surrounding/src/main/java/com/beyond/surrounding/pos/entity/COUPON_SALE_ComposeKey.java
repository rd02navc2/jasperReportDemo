package com.beyond.surrounding.pos.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class COUPON_SALE_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String PKEY;
	private Integer SNO;
}