package com.beyond.surrounding.pos.entity;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data // 自動包含 equals() 和 hashCode()
public class TD_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L; 
	
    private String salDate;
    private String storeNo;
    private String posNo;
    private String trnNo;
    private String tentNo;
}