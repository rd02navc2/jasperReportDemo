package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "POS2_TC_XMA_FILE")
@Table(name = "TC_XMA_FILE")
@IdClass(TC_XMA_FILE_ComposeKey.class) 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_XMA_FILE implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "TC_XMA01")
	@JsonProperty("TC_XMA01") //  強制 XML/JSON 標籤輸出維持大寫
	private String TC_XMA01;

	@Column(name = "TC_XMA02")
	@JsonProperty("TC_XMA02")
	private String TC_XMA02;

	@Column(name = "TC_XMA03")
	@JsonProperty("TC_XMA03")
	private String TC_XMA03;

	@Column(name = "TC_XMA04")
	@JsonProperty("TC_XMA04")
	private String TC_XMA04;

	@Id
	@Column(name = "TC_XMA05")
	@JsonProperty("TC_XMA05")
	private Integer TC_XMA05;

	@Id
	@Column(name = "TC_XMA06")
	@JsonProperty("TC_XMA06")
	private Integer TC_XMA06;

	@Id
	@Column(name = "TC_XMA07")
	@JsonProperty("TC_XMA07")
	private String TC_XMA07;

	@Column(name = "TC_XMA08")
	@JsonProperty("TC_XMA08")
	private String TC_XMA08;

	@Column(name = "TC_XMA09")
	@JsonProperty("TC_XMA09")
	private String TC_XMA09;
	
	@Transient //  標記為非資料庫映射欄位，但可正常用於自訂 SQL 轉物件
	@JsonProperty("TC_XMB02")
	private String TC_XMB02;
}