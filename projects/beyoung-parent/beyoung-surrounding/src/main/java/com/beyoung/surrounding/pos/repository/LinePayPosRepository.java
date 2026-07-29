package com.beyoung.surrounding.pos.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.pos.entity.TRANSACTION_LOG;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LinePayPosRepository extends JpaRepository<TRANSACTION_LOG, Integer> {

	@Modifying
    @Transactional
    @Query(value = """
        UPDATE TRANSACTION_LOG 
        SET refund_transaction_id = :transactionId, 
            transaction_type = :type, 
            refund_transaction_date = :date, 
            invoice_no = :invoiceNo 
        WHERE order_id = :orderId
    """, nativeQuery = true)
    void updateRefundInfo(@Param("orderId") String orderId, 
                          @Param("transactionId") String transactionId, 
                          @Param("type") String type, 
                          @Param("date") Date date, 
                          @Param("invoiceNo") String invoiceNo);
	
	@Modifying
    @Transactional
    @Query(value = """
        INSERT INTO PAY_INFO (order_id, method, amount) 
        VALUES (:orderId, :method, :amount)
        """, nativeQuery = true)
    void savePayInfo(@Param("orderId") String orderId, 
                     @Param("method") String method, 
                     @Param("amount") int amount);
	
	
	@Query(value = """
	    SELECT t2.tc_psc22 
	    FROM tc_psa_file t1
	    JOIN tc_psc_file t2 ON t1.tc_psaplant = t2.tc_pscplant 
	                        AND t1.tc_psa01 = t2.tc_psc01 
	                        AND t1.tc_psa02 = t2.tc_psc02 
	                        AND t1.tc_psa03 = t2.tc_psc03 
	                        AND t1.tc_psa04 = t2.tc_psc04
	    WHERE t1.tc_psa16 || t1.tc_psa17 = :invoiceNo
	    """, nativeQuery = true)
	String findOrderIdByInvoiceNo(@Param("invoiceNo") String invoiceNo);
	 
}