package com.beyoung.surrounding.pos.service;

import com.beyoung.surrounding.pos.repository.CouponPosRepository;
import com.beyoung.surrounding.pos.repository.LqeFileRepository;
import com.beyoung.surrounding.app.entity.LQE_FILE;
import com.beyoung.surrounding.pos.dto.CouponPosDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponPosService {

    private final CouponPosRepository couponRepository; 
    private final LqeFileRepository lqeFileRepository;
    
    /**
     * 根據發票號碼查詢優惠券資訊
     */
    @Transactional(readOnly = true)
    public List<CouponPosDTO> getCouponByInvoiceNO(String invoiceNo) {
        log.info("查詢發票號碼: {}", invoiceNo);
        List<Object[]> results = couponRepository.findCouponInfoByInvoice(invoiceNo);
        
        return results.stream().map(obj -> {
            CouponPosDTO dto = new CouponPosDTO();
            dto.setCouponNo((String) obj[0]);
            dto.setPrice(((Number) obj[1]).intValue());
            dto.setIsApp((String) obj[2]);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 執行優惠券失效作業
     */
    @Transactional(rollbackFor = Exception.class)
    public void doCouponInvalid(String center, String couponID) {
        log.info("執行優惠券失效, Center: {}, IDs: {}", center, couponID);
        
        List<String> idList = List.of(couponID.split(",")).stream()
                                  .map(String::trim)
                                  .collect(Collectors.toList());
        
        couponRepository.updateCouponInvalid(center, idList);
    }

    @Transactional(readOnly = true)
    public List<LQE_FILE> getCouponStatus(String couponIdsStr) {
        
        // 1. 防空檢查
        if (couponIdsStr == null || couponIdsStr.isBlank()) {
            return java.util.Collections.emptyList();
        }

        // 2. 將字串切割，並去除空白、過濾空值，轉為 List<String>
        List<String> couponIdList = java.util.Arrays.stream(couponIdsStr.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .distinct() // 順便去除重複，優化 SQL 效能
                .toList();

        if (couponIdList.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        log.info("查詢優惠券狀態(Service)，總券數: {}", couponIdList.size());

        // 3. 使用 Spring Data JPA 的 nativeQuery。
        //    JPA/Hibernate 會自動幫你處理超過千筆的限制或分批（若底層為現代驅動），
        //    且原生欄位別名必須精準對齊新 LqeFile Entity 的小駝峰欄位名（lqe01, lqe17, taLqe09 等）！
        return lqeFileRepository.findCouponStatusByList(couponIdList);
    }
    
    
}