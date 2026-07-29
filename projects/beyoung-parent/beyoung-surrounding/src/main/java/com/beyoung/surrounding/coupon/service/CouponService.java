package com.beyoung.surrounding.coupon.service;

import com.beyoung.surrounding.coupon.repository.CouponRepository;
import com.beyoung.surrounding.app.entity.LPX_FILE;
import com.beyoung.surrounding.app.entity.LQE_FILE;
import com.beyoung.surrounding.app.entity.TC_PSC_FILE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {
	
	private final CouponRepository couponRepository;

    public List<LPX_FILE> getCouponType() {
        return couponRepository.getCouponType();
    }

    public List<LQE_FILE> getCouponStatus(String couponID) {
    	       
        if (couponID == null || couponID.isBlank()) return Collections.emptyList();
        
        List<String> idList = Arrays.stream(couponID.split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .toList();
        return couponRepository.getCouponStatus(idList);
    }

    
    
    
    public List<TC_PSC_FILE> getCouponHistByMemberID(String memberID, String startDate, String endDate) {
    	
    	//dc-
        List<Object[]> results = couponRepository.getRawCouponData();
        System.out.println("查詢到的筆數: " + results.size());
        for (Object[] row : results) {
            System.out.println("資料欄位: " + Arrays.toString(row));
        }
        return couponRepository.getCouponHistByMemberID(memberID, startDate, endDate);
    }

}