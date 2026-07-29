package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.pos2.entity.LpxFile;
import com.beyond.surrounding.pos2.entity.LqeFile;
import com.beyond.surrounding.pos2.repository.CouponPos2Repository;
import com.beyond.surrounding.pos2.repository.LpxFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponPos2Service {

    private final CouponPos2Repository couponPos2Repository;
    private final LpxFileRepository lpxFileRepository;


    /**
     * 執行優惠券失效作業
     */
    @Transactional("ERP_TM")
    public void doCouponInvalid(String center, String couponId) {
        log.info("執行優惠券失效, Center: {}, IDs: {}", center, couponId);
        
        if (couponId == null || couponId.trim().isEmpty()) {
            return;
        }

        List<String> idList = Arrays.stream(couponId.split(","))
                                    .map(String::trim)
                                    .filter(id -> !id.isEmpty())
                                    .collect(Collectors.toList());
        
        couponPos2Repository.updateCouponInvalid(center, idList);
    }
    
    /**
     * 根據優惠券 ID 清單查詢優惠券狀態
     */
    @Transactional(readOnly = true)
    public List<LqeFile> getCouponStatus(String couponId) throws Exception {
        if (couponId == null || couponId.trim().isEmpty()) {
            return List.of();
        }

        // 把字串切成清單 (例如 "A,B,C" -> ["A", "B", "C"])
        List<String> couponIdList = Arrays.stream(couponId.split(","))
                                          .map(String::trim)
                                          .filter(id -> !id.isEmpty())
                                          .collect(Collectors.toList());

        // 直接查，直接回傳 List<LqeFile> (對應原 LQE_FILE)
        return couponPos2Repository.findCouponStatusRawLqeFile(couponIdList);
    }

    /**
     * 獲取優惠券即時狀態 (對應舊有 getCouponRealStatus 邏輯)
     * 指定特定的 TransactionManager: ERP_TM
     */
    @Transactional(readOnly = true)
    public List<LqeFile> getCouponRealStatus(String couponID) {
        if (couponID == null || couponID.trim().isEmpty()) {
            return List.of();
        }

        log.info("執行 getCouponRealStatus, 傳入原始參數: {}", couponID);

        // 1. 將逗號分隔的字串拆分為 List，並去除前後空白
        List<String> couponIdList = Arrays.stream(couponID.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                // 保留你原本對 ZK05 開頭的特殊判斷或過濾邏輯（若不需要過濾，此行可移除）
                .filter(id -> !id.startsWith("ZK05")) 
                .collect(Collectors.toList());

        if (couponIdList.isEmpty()) {
            return List.of();
        }

        // 2. 直接交給 Repository 的 IN 語法查詢，Hibernate 6 會自動安全綁定
        return couponPos2Repository.findCouponRealStatusList(couponIdList);
    }

    @Transactional(readOnly = true)
    public List<LpxFile> getCouponType() {
        try {
            log.info("開始撈取有效的券別型態資料...");
            return lpxFileRepository.findActiveCouponTypes();
        } catch (Exception e) {
            log.error("執行 getCouponType 發生異常: {}", e.getMessage(), e);
            throw e;
        }
    }
}