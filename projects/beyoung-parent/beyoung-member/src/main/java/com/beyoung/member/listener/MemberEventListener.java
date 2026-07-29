package com.beyoung.member.listener;

import com.beyoung.member.domain.dto.PointChangedEvent;
import com.beyoung.member.infrastructure.LpjFile;
import com.beyoung.member.infrastructure.LpjFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final LpjFileRepository lpjFileRepository;

    @Bean
    public Consumer<PointChangedEvent> syncMemberPoints() {
        return event -> {
            log.info("[Member服務]自 Kafka 接收到補點同步事件，卡號: {}, 點數: +{}", event.getCardNo(), event.getPoint());
            try {
                processMemberMasterUpdate(event);
            } catch (Exception e) {
                log.error("[Member服務]同步 LPJ_FILE 失敗，卡號: {}", event.getCardNo(), e);
                throw e; // 拋出異常以利 Kafka 重試或進 DLQ
            }
        };
    }

    @Transactional(rollbackFor = Exception.class)
    public void processMemberMasterUpdate(PointChangedEvent event) {
        String cardNo = event.getCardNo();
        double points = (event.getPoint() != null) ? event.getPoint().doubleValue() : 0.0;
        
        Optional<LpjFile> existingCard = lpjFileRepository.findByLpj03(cardNo);

        if (existingCard.isEmpty()) {
            log.info("[Member服務]檢測到全新卡號，執行自動初始化建檔: {}", cardNo);
            
            LpjFile newCard = LpjFile.builder()
                    .lpj01("MEMBER_AUTO_001")
                    .lpj02("000")
                    .lpj03(cardNo)
                    .lpj04(LocalDateTime.now())
                    .lpj06(0)
                    .lpj07(1)
                    .lpj09("01") 
                    .lpj12((int) points)
                    .lpj14((int) points)
                    .taLpj03((int) points)
                    .taLpj04("Y")
                    .build();
            
            lpjFileRepository.save(newCard);
            log.info("[Member服務]全新會員卡號紀錄初始化完成。");
            
        } else {
            // 核心修正改為 Java 物件操作，避開原生 SQL 的 WHERE 條件限制
            LpjFile card = existingCard.get();
            
            // 累加點數 (假設原本的點數欄位為 Integer)
            card.setLpj12(card.getLpj12() + (int) points);
            card.setLpj14(card.getLpj14() + (int) points);
            card.setTaLpj03(card.getTaLpj03() + (int) points);
            card.setLpj07(card.getLpj07() + 1); // 累加異動次數
            
            // 使用 Hibernate 的 save，它會自動將這些變更 UPDATE 回資料庫
            lpjFileRepository.save(card);
            
            log.info("[Member服務]會員主檔已成功透過物件更新累計點數，卡號: {}", cardNo);
        }
    }
}