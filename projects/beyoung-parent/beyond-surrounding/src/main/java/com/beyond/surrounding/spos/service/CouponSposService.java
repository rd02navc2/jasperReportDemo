package com.beyond.surrounding.spos.service;

import com.beyond.surrounding.pos.repository.CouponPosRepository;
import com.beyond.surrounding.pos.dto.CouponPosDTO;
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
public class CouponSposService {

    private final CouponPosRepository couponRepository;

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
    @Transactional("ERP_TM")
    public void doCouponInvalid(String sCenter, String sCouponID) {
        log.info("執行優惠券失效, Center: {}, IDs: {}", sCenter, sCouponID);
        
        List<String> idList = List.of(sCouponID.split(",")).stream()
                                  .map(String::trim)
                                  .collect(Collectors.toList());
        
        couponRepository.updateCouponInvalid(sCenter, idList);
    }
}