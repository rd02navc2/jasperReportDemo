package com.beyond.surrounding.pos.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.pos2.entity.PACE_TRANSACTION_LOG;


@Repository
public interface PacePayRepository extends JpaRepository<PACE_TRANSACTION_LOG, String> {
	
	
	
	List<PACE_TRANSACTION_LOG> findByOrderId(String orderId);
		
	@Modifying
    @Transactional("RMS_TM")
    @Query(value = """
        UPDATE PACE_TRANSACTION_LOG 
        SET refund_transaction_id = :refundTranXId, 
            transaction_type = 'PAYMENT_REFUND', 
            refund_transaction_date = :posDateTime, 
            invoice_no = :invoiceNo, 
            access_date = CURRENT_TIMESTAMP 
        WHERE order_id = :orderId
        """, nativeQuery = true)
    int updTranLogRefund(
        @Param("orderId") String orderId, 
        @Param("refundTranXId") String refundTranXId, // 修正命名為小駝峰
        @Param("posDateTime") Date posDateTime, 
        @Param("invoiceNo") String invoiceNo
    );
	
}