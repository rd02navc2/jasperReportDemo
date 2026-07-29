package com.beyond.surrounding.erp.repository;

import com.beyond.surrounding.erp.entity.GET_COUPON_LOG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpCouponRepository extends JpaRepository<GET_COUPON_LOG, Long> {
    // 繼承後即自帶標準的 save() 方法
}