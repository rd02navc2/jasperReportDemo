package com.beyond.surrounding.ts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "CUSTOMER_SERVICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CUSTOMER_SERVICE implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer sn;
	private String content;
	
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
