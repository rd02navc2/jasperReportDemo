package com.beyond.surrounding.ts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREDIT_CARD_BIND_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CREDIT_CARD_BIND_LOG implements Serializable { //dc- created 
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "BANK_NO")
    private String bankNo;

    @Column(name = "CARD_NAME")
    private String cardName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "CARD_STATUS")
    private String cardStatus;

    @Column(name = "CARD_TYPE")
    private String cardType;

    @Column(name = "CARD_TOKEN")
    private String cardToken;

    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;
	
}