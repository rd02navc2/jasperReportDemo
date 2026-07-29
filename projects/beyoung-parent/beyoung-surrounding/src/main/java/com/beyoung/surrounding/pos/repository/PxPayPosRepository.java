package com.beyoung.surrounding.pos.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.pos.entity.PX_TRANSACTION_LOG;


@Repository
public interface PxPayPosRepository extends JpaRepository<PX_TRANSACTION_LOG, String> {

	@Modifying
    @Query(value ="""
        UPDATE PX_TRANSACTION_LOG p 
        SET p.refundOrderId = :newOrderId, 
            p.refundTransactionId = :refundTranId, 
            p.transactionType = 'PAYMENT_REFUND', 
            p.refundTransactionDate = :refundDate, 
            p.invoiceNo = :invoiceNo, 
            p.accessDate = :accessDate 
        WHERE p.orderId = :orderId
    """, nativeQuery = true)
    int updateRefundLog(
        @Param("orderId") String orderId,
        @Param("newOrderId") String newOrderId,
        @Param("refundTranId") String refundTranId, // 改為 String 更具彈性
        @Param("refundDate") Date refundDate,
        @Param("invoiceNo") String invoiceNo,
        @Param("accessDate") Date accessDate
    );
	
	 
}