package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "POS2_IMA_FILE")
@Table(name = "IMA_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IMA_FILE implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "IMA01")
	private String IMA01;

	@Column(name = "IMA02")
	private String IMA02;

	@Column(name = "TA_IMA01")
	private String TA_IMA01;

	@Column(name = "IMA15")
	private String IMA15;

	@Column(name = "IMA127")
	private Double IMA127;

	@Column(name = "IMA128")
	private Double IMA128;

	@Column(name = "IMA131")
	private String IMA131;

	@Column(name = "IMADATE")
	private String IMADATE;

	@Column(name = "OBA02")
	private String OBA02;

	@Column(name = "IMA1005")
	private String IMA1005;

	@Column(name = "TQA02")
	private String TQA02;

	@Column(name = "IMA25")
	private String IMA25;

	@Column(name = "IMA54")
	private String IMA54;

	@Column(name = "RTG05")
	private Double RTG05;
}