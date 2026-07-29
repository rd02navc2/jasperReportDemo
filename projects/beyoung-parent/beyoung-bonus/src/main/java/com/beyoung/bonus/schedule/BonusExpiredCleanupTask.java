package com.beyoung.bonus.schedule;

import com.beyoung.bonus.domain.entity.BonusLog;
import com.beyoung.bonus.infrastructure.BonusLogRepository;
//.repository.BonusLogRepository;
import com.beyoung.bonus.application.BonusService;
//.service.BonusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BonusExpiredCleanupTask {

    private final BonusLogRepository bonusLogRepository;
    private final BonusService bonusService; 

    /**
     * 調整為測試環境每 30 秒自動執行一次
     * 調整重點：註解原本的凌晨 Cron，換上 0/30
     */
    // @Scheduled(cron = "0 1 0 * * ?") // 1. 生產環境凌晨執行 (暫時註解)
    @Scheduled(cron = "0/30 * * * * ?")  // 2. 測試環境每 30 秒執行
    public void executePointResetJob() {
    	System.out.println("==================== [測試] 排程真的有在跑！ ====================");
        log.info("[排程啟動]啟動凌晨點數過期失效歸零作業...");
        LocalDate today = LocalDate.now();

        try {
            // 1. 掃描資料庫中滿足條件的資料：
            // 條件：當前日期 > expiry_date (已過期) 且 剩餘點數 point > 0
            // 實務上這裡建議透過 Pageable 分頁撈取，防範海量資料導致記憶體溢出(OOM)
            List<BonusLog> expiredLogs = bonusLogRepository.findExpiredPoints(today);

            if (expiredLogs.isEmpty()) {
                log.info("排程結束未偵測到任何過期點數紀錄，無須處理。");
                return;
            }

            log.info("偵測到已過期且需歸零的紀錄共 {} 筆，開始執行批次自動扣減...", expiredLogs.size());

            for (BonusLog logItem : expiredLogs) {
                // 逐筆在獨立的事務中處理，避免單一會員異常導致整批失敗
                processSingleExpiredLog(logItem);
            }

            log.info("[排程結束]成功完成今日點數失效歸零批次作業。");

        } catch (Exception e) {
            log.error("[排程災情]排程執行期間發生未預期系統異常：", e);
        }
    }

    /**
     * 處理單筆點數歸零事務，確保審計日誌、流水台帳與 Kafka 異步事件同步發送
     */
    @Transactional(rollbackFor = Exception.class)
    public void processSingleExpiredLog(BonusLog expiredLog) {
        try {
            int pointsToDeduct = expiredLog.getPoint();
            String cardNo = expiredLog.getCardNo();
            
            log.info("正在歸零會員卡號: [{}], 過期點數: {} 點", cardNo, pointsToDeduct);

            // 安全防禦：若原本紀錄的到期日為空，動態給予 9999-12-31 預設值，防範 Builder 報錯
            LocalDate finalExpiryDate = expiredLog.getExpiryDate() != null ? 
                    expiredLog.getExpiryDate() : LocalDate.of(9999, 12, 31);

            // 建立一筆「點數過期失效」的反向扣減交易紀錄，寫入 BONUS_LOG 作為審計日誌
            BonusLog auditDeductLog = BonusLog.builder()
                    .center(expiredLog.getCenter())
                    .counterId(expiredLog.getCounterId())
                    .userId(expiredLog.getUserId())
                    .userName(expiredLog.getUserName())
                    .cardNo(cardNo)
                    .point(-pointsToDeduct) // 負值代表點數過期失效扣減
                    .accessDate(LocalDateTime.now())
                    .expiryDate(finalExpiryDate) // 傳入經 Null-Safe 校驗後的安全日期物件
                    .accessId("SYSTEM_CRON_JOB") // 標註為系統排程自動發起
                    .build();

            bonusLogRepository.save(auditDeductLog);

            // 呼叫現有核心 BonusService 發送 Kafka 事件
            bonusService.sendPointChangedEvent(
                    expiredLog.getCenter(),                // 1. center (據點代碼)
                    expiredLog.getCounterId(),             // 2. counterId (專櫃代碼)
                    cardNo,                                // 3. cardNo (會員卡號)
                    -pointsToDeduct,                       // 4. point (扣減負值點數)
                    "EXPIRED_BILL_" + expiredLog.getId()   // 5. sourceBillNo (對帳關聯單號)
            );
            
            // 將原本這筆過期紀錄的剩餘點數更新為 0，防範重複扣點
            expiredLog.setPoint(0);
            bonusLogRepository.save(expiredLog);

            log.info("會員 [{}] 的 {} 點過期點數已完成審計落帳與 Kafka 事件同步廣播。", cardNo, pointsToDeduct);

        } catch (Exception e) {
            log.error("會員卡號 [{}] 點數過期歸零失敗，錯誤原因: ", expiredLog.getCardNo(), e);
            throw e; 
        }
    }

    /**
     * 處理單筆點數歸零事務，確保審計日誌、流水台帳與 Kafka 異步事件同步發送
     */
    @Transactional(rollbackFor = Exception.class)
    public void processSingleExpiredLog1(BonusLog expiredLog) {
        try {
            int pointsToDeduct = expiredLog.getPoint();
            String cardNo = expiredLog.getCardNo();
            
            log.info("正在歸零會員卡號: [{}], 過期點數: {} 點", cardNo, pointsToDeduct);

            // 安全防禦：若原本紀錄的到期日為空，動態給予 9999-12-31 預設值，防範 Builder 報錯
            LocalDate finalExpiryDate = expiredLog.getExpiryDate() != null ? 
                    expiredLog.getExpiryDate() : LocalDate.of(9999, 12, 31);

            // 建立一筆點數過期失效的反向扣減交易紀錄，寫入 BONUS_LOG 作為審計日誌
            BonusLog auditDeductLog = BonusLog.builder()
                    .center(expiredLog.getCenter())
                    .counterId(expiredLog.getCounterId())
                    .userId(expiredLog.getUserId())
                    .userName(expiredLog.getUserName())
                    .cardNo(cardNo)
                    .point(-pointsToDeduct) // 負值代表點數過期失效扣減
                    .accessDate(LocalDateTime.now())
                    .expiryDate(finalExpiryDate) // 傳入經 Null-Safe 校驗後的安全日期物件
                    .accessId("SYSTEM_CRON_JOB") // 標註為系統排程自動發起
                    .build();

            bonusLogRepository.save(auditDeductLog);

            // 呼叫現有核心 BonusService 發送 Kafka 事件
            bonusService.sendPointChangedEvent(
                    expiredLog.getCenter(),                // 1. center (據點代碼)
                    expiredLog.getCounterId(),             // 2. counterId (專櫃代碼)
                    cardNo,                                // 3. cardNo (會員卡號)
                    -pointsToDeduct,                       // 4. point (扣減負值點數)
                    "EXPIRED_BILL_" + expiredLog.getId()   // 5. sourceBillNo (對帳關聯單號)
            );
            
            // 將原本這筆過期紀錄的剩餘點數更新為 0，防範重複扣點
            expiredLog.setPoint(0);
            bonusLogRepository.save(expiredLog);

            log.info("會員 [{}] 的 {} 點過期點數已完成審計落帳與 Kafka 事件同步廣播。", cardNo, pointsToDeduct);

        } catch (Exception e) {
            log.error("會員卡號 [{}] 點數過期歸零失敗，錯誤原因: ", expiredLog.getCardNo(), e);
            throw e; 
        }
    }
}