package com.beyoung.bonus.domain.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCompletedEvent {
    private String orderNo;
    private String cardNo;
    private String vipLevel;
    private Double totalAmount;
    private String center;
    private String counterId;
}