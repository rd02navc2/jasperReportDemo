package com.beyoung.bonus.domain.dto;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 點數歷史查詢專用 DTO
 * 對應 BonusService 中的 SQL 查詢結果集
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "Request") // 支援 XML 格式請求根節點
public class LsmHistoryDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @JsonProperty("lsmstore")
    private String lsmstore;
    
    @JsonProperty("lsm01")
    private String lsm01;
    
    @JsonProperty("lsm02")
    private String lsm02;
    
    @JsonProperty("lsm04")
    private Double lsm04;
    
    @JsonProperty("lsm05")
    private LocalDateTime lsm05;
    
    @JsonProperty("lsm08")
    private Double lsm08;
    
    @JsonProperty("taLsm02")
    private String taLsm02;
    
    @JsonProperty("taLsm09")
    private String taLsm09;
    
    @JsonProperty("tqa02")
    private String tqa02;   // 處理 CASE WHEN 的結果
    
    @JsonProperty("taLsm04")
    private String taLsm04;
    
}