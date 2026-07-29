package com.beyoung.bonus.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "orders") // 請依照您的 DB 表名調整
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String orderNo;
    private String cardNo;
    private String vipLevel;
    private Double amount;
    private String center;
    private String counterId;
}