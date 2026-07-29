package com.beyoung.surrounding.spos.entity;

import lombok.*;
import java.io.Serializable;

@Data // 自動幫你生成完美且正確的 equals()、hashCode()、Getter、Setter 與 toString()
@NoArgsConstructor
@AllArgsConstructor
public class RYD_FILE_ComposeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String RYD01;
	private String RYD10;
	 
}

