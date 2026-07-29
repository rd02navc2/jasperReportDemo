package com.beyond.surrounding.ts.repository;

import com.beyond.surrounding.ts.entity.TS_OOA_LOG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.Optional;

@Repository
public interface TsOoaLogRepository extends JpaRepository<TS_OOA_LOG, String> {
    
    // 1. Native Delete
    @Modifying
    @Query(value = """
        DELETE FROM TS_OOA_LOG 
        WHERE order_no = ?1
        """, nativeQuery = true)
    void deleteByOrderNo(String orderNo);
	
    // 2. Native Select
    @Query(value = """
        SELECT * FROM TS_OOA_LOG 
        WHERE order_no = ?1
        """, nativeQuery = true)
    Optional<TS_OOA_LOG> findByOrderNo(String orderNo);

    // 3. Native Update (用於 updateRefund)
    @Modifying
    @Query(value = """
        UPDATE TS_OOA_LOG 
        SET refund_order_no = ?2, 
            amt_refund = ?3, 
            refund_trade_no = ?4, 
            refund_status = ?5, 
            refund_date = ?6 
        WHERE order_no = ?1
        """, nativeQuery = true)
    void updateRefundInfo(String orderNo, String refundOrderNo, Double amtRefund, 
                          String refundTradeNo, String refundStatus, Date refundDate);
    
    @Modifying
    @Query(value = """
        UPDATE TS_OOA_LOG 
        SET confirm_date = ?2 
        WHERE order_no = ?1
        """, nativeQuery = true)
    void updateConfirmDate(String orderNo, Date confirmDate);

    @Modifying
    @Query(value = """
        UPDATE TS_OOA_LOG 
        SET ret_code = ?2, 
            ret_msg = ?3, 
            notify_date = ?4 
        WHERE order_no = ?1
        """, nativeQuery = true)
    void updateNotifyInfo(String orderNo, String retCode, String retMsg, Date notifyDate);
    
    
    
    
}