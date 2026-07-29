package com.beyond.surrounding.member.dto;

import com.beyond.surrounding.app.entity.LPK_FILE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class MemberContactDTO {

	private LPK_FILE lpkFile;
    private Double totalLsm08;
    private Integer vipLevel;
    
}