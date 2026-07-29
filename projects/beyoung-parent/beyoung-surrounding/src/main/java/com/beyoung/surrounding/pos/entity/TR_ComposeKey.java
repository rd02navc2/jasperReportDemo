package com.beyoung.surrounding.pos.entity;

import lombok.*;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data // 自動包含 equals() 和 hashCode()
public class TR_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	
	@Id @Column(name = "SAL_DATE", length = 20) private String salDate;
	@Id @Column(name = "STORE_NO", length = 20) private String storeNo;
	@Id @Column(name = "POS_NO",   length = 20) private String posNo;
	@Id @Column(name = "TRN_NO",   length = 20) private String trnNo;
	@Id @Column(name = "ITEM_NO",  length = 20) private String itemNo;
}