package com.beyond.surrounding.pos.entity;

import jakarta.persistence.*; // 使用 Jakarta JPA (Spring Boot 3.x 以上)
import lombok.*;

@Entity(name = "POS_PAY_INFO")
@Table(name = "PAY_INFO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PAY_INFO {

    @Id
    @Column(name = "order_id") // 對應 SQL 的 order_id
    private String orderId;

    @Column(name = "method")
    private String method;

    @Column(name = "amount")
    private Integer amount;
}