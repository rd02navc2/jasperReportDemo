package com.beyoung.bonus.listener;

import com.beyoung.bonus.domain.dto.BonusDTO;
import com.beyoung.bonus.application.BonusService;
//.service.BonusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BonusPointListener {

    private final BonusService bonusService;

    /**
     * 監聽 Kafka 的 order-completed-topic
     * 當訂單系統拋出事件時，自動呼叫整合好的加點邏輯
     */
    @Bean
    public Consumer<BonusDTO.Request> addPointListener() {
        return request -> {
            log.info("[Kafka 觸發]監聽到訂單加點事件，訂單號: {}, 卡號: {}", 
                     request.getOrderNo(), request.getCardNo());
            
            try {
                // 直接呼叫整合後的 Service 邏輯
                // 這裡會自動進到我們剛才寫好的分流判斷 (因為帶有 orderNo)
                bonusService.addVipGiftPoint(
                    request.getCenter(),
                    request.getCounterId(),
                    request.getCardNo(),
                    request.getPoint(),
                    "VIP_GIFT_" + request.getOrderNo() // 唯一字軌防護
                );
                
                log.info("[Kafka 觸發]加點處理成功。");
                
            } catch (Exception e) {
                log.error("[Kafka 觸發]加點處理失敗: {}", e.getMessage(), e);
                // 這裡拋出例外會導致 Kafka 重試機制啟動
            }
        };
    }
}
