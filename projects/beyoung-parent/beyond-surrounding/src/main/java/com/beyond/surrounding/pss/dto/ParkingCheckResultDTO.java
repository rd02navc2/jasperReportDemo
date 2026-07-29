package com.beyond.surrounding.pss.dto;

import com.beyond.surrounding.pss.entity.LpjFile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 確保回應格式清晰
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ParkingCheckResultDTO {
    
    @JsonProperty("code")
    private String sCode;       // 建議前端對接時統一使用 code
    
    @JsonProperty("message")
    private String sMessage;    // 建議統一使用 message
    
    @JsonProperty("data")
    private LpjFile lDdata;     // 建議統一使用 data

    // 1. 成功時的工廠方法
    public static ParkingCheckResultDTO success(LpjFile data) {
        return ParkingCheckResultDTO.builder()
                .sCode("0000")
                .sMessage("查詢成功")
                .lDdata(data)
                .build();
    }

    // 2. 錯誤處理的工廠方法
    public static ParkingCheckResultDTO error(String sCode, String sMessage) {
        return ParkingCheckResultDTO.builder()
                .sCode(sCode)
                .sMessage(sMessage)
                .lDdata(null)
                .build();
    }

    // 3. 檢查方法
    public boolean isSuccess() {
        return "0000".equals(this.sCode);
    }
}