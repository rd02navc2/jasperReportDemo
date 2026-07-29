package com.beyond.surrounding.ts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.ts.entity.TS_OOA_LOG;
import java.util.Optional;

@Repository
public interface OneOnAirRepository extends JpaRepository<TS_OOA_LOG, String> {

    // 1. 根據 order_no 與 payment_type 查詢
    @Query(value = """
        SELECT * FROM TS_OOA_LOG 
        WHERE ORDER_NO = :orderNo 
          AND PAYMENT_TYPE = :paymentType
        """, nativeQuery = true)
    Optional<TS_OOA_LOG> findByOrderNoAndPaymentType(
        @Param("orderNo") String orderNo, 
        @Param("paymentType") String paymentType
    );

    // 2. 根據 order_no 查詢
    @Query(value = """
        SELECT * FROM TS_OOA_LOG 
        WHERE ORDER_NO = :orderNo
        """, nativeQuery = true)
    Optional<TS_OOA_LOG> findByOrderNo(@Param("orderNo") String orderNo);

    // 3. 更新確認時間 (以資料庫語法 CURRENT_TIMESTAMP 為例)
    @Modifying
    @Query(value = """
        UPDATE TS_OOA_LOG 
        SET CONFIRM_DATE = CURRENT_TIMESTAMP 
        WHERE ORDER_NO = :orderNo
        """, nativeQuery = true)
    int updateConfirm(@Param("orderNo") String orderNo);

    // 4. 更新通知狀態
    @Modifying
    @Query(value = """
        UPDATE TS_OOA_LOG 
        SET RET_CODE = :rtnCode, 
            RET_MSG = :rtnMsg, 
            NOTIFY_DATE = CURRENT_TIMESTAMP 
        WHERE ORDER_NO = :orderNo
        """, nativeQuery = true)
    int updateNotify(
        @Param("orderNo") String orderNo, 
        @Param("rtnCode") String rtnCode, 
        @Param("rtnMsg") String rtnMsg
    );
    
}