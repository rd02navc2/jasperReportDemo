package com.beyond.surrounding.erp.service;

import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.erp.entity.GET_COUPON_LOG;
import com.beyond.surrounding.erp.repository.ErpCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErpCouponService {
	
	private final ErpCouponRepository erpCouponRepository;
	
	@Transactional // 確保與 Spring Boot 的事務管理器咬合，取代舊版 Session 事務
    public void save(String sCenter, String sSaleNO, String sTradeType, String sClientSystem, 
                     String sCardNO, int iPieces, String sCouponFrom, String sCouponTo) {
        
		// 利用 Lombok 的 Builder 模式，優雅且乾淨地建構實體物件
        GET_COUPON_LOG logEntity = GET_COUPON_LOG.builder()
                .center(sCenter)
                .sale_no(sSaleNO)
                .trade_type(sTradeType)
                .client_system(sClientSystem)
                .card_no(sCardNO)
                .pieces(iPieces)
                .coupon_from(sCouponFrom)
                .coupon_to(sCouponTo)
                .access_date(new Date()) // 直接帶入目前時間
                .build();

        // 呼叫 JPA Repository 執行存檔
        erpCouponRepository.save(logEntity);
    }
	
}
