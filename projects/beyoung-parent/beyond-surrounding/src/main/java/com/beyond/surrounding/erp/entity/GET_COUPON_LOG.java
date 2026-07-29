package com.beyond.surrounding.erp.entity;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GET_COUPON_LOG")
@Getter
@Setter
@NoArgsConstructor  // JPA 必須要有無參構造函數
@AllArgsConstructor
@Builder
public class GET_COUPON_LOG implements Serializable {

    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sn;
	private String center;
	private String sale_no;
	private String trade_type;
	private String client_system;
	private String card_no;
	private String coupon_from;
	private String coupon_to;
	private Integer pieces;
	private Date access_date;
	
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getSale_no() {
		return sale_no;
	}
	public void setSale_no(String sale_no) {
		this.sale_no = sale_no;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getClient_system() {
		return client_system;
	}
	public void setClient_system(String client_system) {
		this.client_system = client_system;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getCoupon_from() {
		return coupon_from;
	}
	public void setCoupon_from(String coupon_from) {
		this.coupon_from = coupon_from;
	}
	public String getCoupon_to() {
		return coupon_to;
	}
	public void setCoupon_to(String coupon_to) {
		this.coupon_to = coupon_to;
	}
	public Integer getPieces() {
		return pieces;
	}
	public void setPieces(Integer pieces) {
		this.pieces = pieces;
	}
	public Date getAccess_date() {
		return access_date;
	}
	public void setAccess_date(Date access_date) {
		this.access_date = access_date;
	}
}
