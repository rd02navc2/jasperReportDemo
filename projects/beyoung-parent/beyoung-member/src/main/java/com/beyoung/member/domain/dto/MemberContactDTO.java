package com.beyoung.member.domain.dto;

import com.beyoung.member.infrastructure.LpkFile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class MemberContactDTO {

  
	private LpkFile lpkFile;
	
	
    private Double totalLsm08;
    
    
    private Integer vipLevel;
    
}