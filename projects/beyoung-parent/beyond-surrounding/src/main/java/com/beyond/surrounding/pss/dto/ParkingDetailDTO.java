package com.beyond.surrounding.pss.dto;

import com.beyond.surrounding.pss.entity.ParkingRent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDetailDTO {
    
    @JsonProperty("carNO")
    private String carNO;
    
    @JsonProperty("enterDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime enterDate;
    
    @JsonProperty("parkingHour")
    private Double parkingHour;
    
    @JsonProperty("parkingFee")
    private Double parkingFee;
    
    @JsonProperty("pNo")
    private Integer pNo;

	public ParkingRent orElse(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}