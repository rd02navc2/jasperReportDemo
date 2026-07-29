package com.beyoung.surrounding.pos.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.beyoung.surrounding.pos.entity.TAIWAN_TRANSACTION_LOG;

@Repository
public interface TaiwanPayRepository extends JpaRepository<TAIWAN_TRANSACTION_LOG, String> {
    // 您不需要在這裡寫 @Query，直接使用 save() 即可完成插入操作

	@Modifying
	@Transactional
	@Query(value = """
	    UPDATE TAIWAN_TRANSACTION_LOG 
	    SET refund_transaction_id = :refundId, 
	        transaction_type = 'PAYMENT_REFUND', 
	        refund_transaction_date = :refundDate, 
	        invoice_no = :invoiceNo, 
	        access_date = CURRENT_TIMESTAMP 
	    WHERE order_id = :orderId
	    """, nativeQuery = true)
	int updateRefundInfo(@Param("orderId") String orderId, 
	                     @Param("refundId") String refundId, 
	                     @Param("refundDate") Date refundDate, 
	                     @Param("invoiceNo") String invoiceNo);
	
	
	
	
	
	
	
	
	
	
	
}