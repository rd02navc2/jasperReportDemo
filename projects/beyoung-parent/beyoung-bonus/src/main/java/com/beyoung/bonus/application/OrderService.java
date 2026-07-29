package com.beyoung.bonus.application;

import com.beyoung.bonus.domain.entity.Order; // 假設您的 Order Entity 路徑
import com.beyoung.bonus.infrastructure.OrderRepository; // 假設您的 Repository 路徑
import com.beyoung.bonus.infrastructure.OrderEventProducer; // 剛剛補齊的 Producer
import com.beyoung.bonus.domain.event.OrderCompletedEvent; // 事件 DTO
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Transactional(rollbackFor = Exception.class)
    public void completeOrder(Order order) {
        // 1. 執行核心結帳邏輯（寫入訂單資料庫）
        orderRepository.save(order);
        log.info("訂單系統訂單已儲存，訂單編號: {}", order.getOrderNo());
        
        // 2. 註冊交易後鉤子，確保交易成功才發送 Kafka 事件
        // 注意：需確認當前執行緒已開啟交易 (Transaction)
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("訂單系統交易已提交，準備發送 Kafka 事件...");
                    
                    // 事件發送邏輯
                    orderEventProducer.sendOrderCompletedEvent(
                        OrderCompletedEvent.builder()
                            .orderNo(order.getOrderNo())
                            .cardNo(order.getCardNo())
                            .vipLevel(order.getVipLevel())
                            .totalAmount(order.getAmount())
                            .center(order.getCenter())
                            .counterId(order.getCounterId())
                            .build()
                    );
                }
            });
        } else {
            log.warn("訂單系統當前無活躍事務，事件將不會於 afterCommit 觸發");
        }
    }
}