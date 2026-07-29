package com.beyoung.bonus.infrastructure;

import com.beyoung.bonus.domain.event.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final StreamBridge streamBridge;

    /**
     * 發送訂單完成事件至 Kafka
     */
    public void sendOrderCompletedEvent(OrderCompletedEvent event) {
        // "order-completed-out-0" 必須與 yml 設定的 binding 名稱一致
        boolean success = streamBridge.send("order-completed-out-0", event);
        
        if (success) {
            log.info("事件中心訂單事件發送成功: {}", event.getOrderNo());
        } else {
            log.error("事件中心訂單事件發送失敗: {}", event.getOrderNo());
            throw new RuntimeException("Kafka 消息發送異常");
        }
    }
}