package com.beyond.surrounding.pos2.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.pos2.entity.ONE_TRANSACTION_LOG;
import com.beyond.surrounding.pos2.entity.TAIWAN_TRANSACTION_LOG;

@Repository
public interface OnePayPos2Repository extends JpaRepository<ONE_TRANSACTION_LOG, String> {

	void save(TAIWAN_TRANSACTION_LOG log);
    
	@Modifying
	@Query(value = """
	    UPDATE ONE_TRANSACTION_LOG 
	    SET refund_transaction_id = :refundId, 
	        refund_order_id = :newOrderId, 
	        transaction_type = 'PAYMENT_REFUND', 
	        refund_transaction_date = :refundDate, 
	        invoice_no = :invoiceNo, 
	        access_date = :now 
	    WHERE order_id = :orderId
	    """, nativeQuery = true)
	void updateRefundInfo(
	    @Param("orderId") String orderId, 
	    @Param("newOrderId") String newOrderId, 
	    @Param("refundId") String refundId, 
	    @Param("refundDate") Date refundDate, 
	    @Param("invoiceNo") String invoiceNo, 
	    @Param("now") Date now
	);
	
	
	
}