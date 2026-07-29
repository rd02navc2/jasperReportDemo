package com.beyoung.member.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "Request") // 支援 XML 格式請求根節點
public class MemberStatsDTO {
	
	@JsonProperty("totalLsm08")
	private Double totalLsm08;
	
	@JsonProperty("vipLevel")
    private Integer vipLevel;

}
