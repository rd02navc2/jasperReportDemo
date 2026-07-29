package com.beyond.surrounding.spos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.app.entity.LQE_FILE;
import java.util.List;

@Repository
public interface CouponSposRepository extends JpaRepository<LQE_FILE, String> {

	/**
     * 1. 批量失效優惠券 (對應原 doCouponInvalid)
     * 使用 @Modifying 標記為更新操作，並確保事務由調用方（Service）控制
     */
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE LQE_FILE 
        SET LQE11 = :center, 
            LQE12 = CURRENT_TIMESTAMP, 
            LQE17 = '3' 
        WHERE LQE01 IN :ids
        """, nativeQuery = true)
    void updateCouponInvalid(@Param("center") String center, @Param("ids") List<String> ids);
    
    /**
     * 2. 透過發票號碼查詢優惠券詳細資訊 (對應原 getCouponByInvoiceNO)
     * 使用 nativeQuery 處理複雜的 CASE 邏輯
     */
    @Query(value = """
        SELECT GC_NO AS gcNo, 
               CASE LEFT(GC_NO, 2) 
                   WHEN 'UK' THEN 100 WHEN 'UL' THEN 200 WHEN 'UM' THEN 300 
                   WHEN 'UN' THEN 500 WHEN 'TL' THEN 100 ELSE 0 END AS gcAmt,
               CASE LEFT(UPD_NAME, 1) WHEN '9' THEN 'Y' ELSE 'N' END AS isApp 
        FROM COUPON_DATA 
        WHERE PKEY IN (SELECT PKEY FROM COUPON_SALE WHERE RECEI_NO = :invoiceNo)
        """, nativeQuery = true)
    List<Object[]> findCouponInfoByInvoice(@Param("invoiceNo") String invoiceNo);
    
}