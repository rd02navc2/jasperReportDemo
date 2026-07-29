package com.beyond.surrounding.pos.repository;

import com.beyond.surrounding.pos.entity.TD;
import com.beyond.surrounding.pos.entity.TD_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TDRepository extends JpaRepository<TD, TD_ComposeKey> {
			    
   
			   @Query(value ="""
			    SELECT t
			    FROM TD t
			    WHERE t.invNo = :invoiceNo
			      AND t.storeNo = :center
			      AND t.salDate = :date
			   """, nativeQuery = true)
			    List<TD> findByInvNoCustom(
			            @Param("center") String center,
			            @Param("date") String date,
			            @Param("invoiceNo") String invoiceNo
			    );
       
	   			@Query(value = """
    		    SELECT 
    		        DATE_FORMAT(t1.tc_psa04, '%Y%m%d') as salDate, 
    		        t1.tc_psaplant as storeNo, 
    		        t1.tc_psa02 as posNo, 
    		        t1.tc_psa01 as tentNo, 
    		        t1.tc_psa13 as vipNo, 
    		        t1.tc_psa12 as invAmt, 
    		        t1.tc_psa08 as totSales, 
    		        t1.tc_psa40 as promotAmt, 
    		        t1.tc_psa05 as salTime, 
    		        t1.tc_psa03 as seqNo, 
    		        CONCAT(t1.tc_psa16, t1.tc_psa17) as invNo,
    		        CASE WHEN t2.tc_psa06 IN ('02','03') THEN 'D' ELSE 'P' END as salType,
    		        CASE WHEN tb.tc_psb19 IS NULL THEN tb.tc_psb01 ELSE tb.tc_psb19 END as itemNo,
    		        tb.tc_psb09 as qty, 
    		        tb.tc_psb11 as salPrice, 
    		        tb.tc_psb13 as grdAmt,
    		        tc.tc_psc07 as memo3, 
    		        tc.tc_psc08 as payAmt, 
    		        tc.tc_psc10 as installmentPeriod
    		    FROM tc_psa_file t1
    		    LEFT JOIN (SELECT * FROM tc_psa_file WHERE tc_psa06 IN ('02','03')) t2 
    		        ON t1.tc_psa01 = t2.tc_psa01 AND t1.tc_psa16 = t2.tc_psa16 AND t1.tc_psa17 = t2.tc_psa17
    		    LEFT JOIN tc_psb_file tb 
    		        ON t1.tc_psaplant = tb.tc_psbplant AND t1.tc_psa01 = tb.tc_psb01 AND t1.tc_psa02 = tb.tc_psb02 AND t1.tc_psa03 = tb.tc_psb03 AND t1.tc_psa04 = tb.tc_psb04
    		    LEFT JOIN tc_psc_file tc 
    		        ON t1.tc_psaplant = tc.tc_pscplant AND t1.tc_psa01 = tc.tc_psc01 AND t1.tc_psa02 = tc.tc_psc02 AND t1.tc_psa03 = tc.tc_psc03 AND t1.tc_psa04 = tc.tc_psc04 AND tc.tc_psc05 IN ('11','12','13','14')
    		    WHERE t1.tc_psa06 = '01'
    		      AND t1.tc_psaplant = :center
    		      AND DATE_FORMAT(t1.tc_psa04, '%Y%m%d') BETWEEN :start AND :end
    		      AND t1.tc_psa13 = (
    		          SELECT lpj03 FROM lpj_file 
    		          WHERE lpj01 = (SELECT lpj01 FROM lpj_file WHERE lpj03 = :cardNo) 
    		            AND lpj09 = '2' AND ta_lpj04 = 'Y'
    		      )
    		""", nativeQuery = true)
    		List<Object[]> findDetailsByCardNoNative(
    		        @Param("center") String center, 
    		        @Param("start") String start, 
    		        @Param("end") String end, 
    		        @Param("cardNo") String cardNo);
    		
    		
    		@Query(value = """
    		        SELECT 
    		            DATE_FORMAT(t1.tc_psa04, '%Y%m%d') as salDate,
    		            t1.tc_psaplant as storeNo,
    		            t1.tc_psa02 as posNo,
    		            t1.tc_psa01 as tentNo,
    		            t1.tc_psa13 as vipNo,
    		            t1.tc_psa12 as invAmt,
    		            t1.tc_psa08 as totSales,
    		            t1.tc_psa40 as promotAmt,
    		            t1.tc_psa05 as salTime,
    		            t1.tc_psa03 as seqNo,
    		            CONCAT(t1.tc_psa16, t1.tc_psa17) as invNo,
    		            CASE WHEN t1.tc_psa06 IN ('02','03') THEN 'D' ELSE 'P' END as salType,
    		            CASE WHEN tb.tc_psb19 IS NULL THEN tb.tc_psb01 ELSE tb.tc_psb19 END as itemNo,
    		            tb.tc_psb09 as qty,
    		            tb.tc_psb11 as salPrice,
    		            tb.tc_psb13 as grdAmt,
    		            tc.tc_psc07 as memo3,
    		            tc.tc_psc08 as payAmt,
    		            tc.tc_psc10 as installmentPeriod
    		        FROM tc_psa_file t1
    		        LEFT JOIN tc_psb_file tb ON t1.tc_psaplant = tb.tc_psbplant AND t1.tc_psa01 = tb.tc_psb01 AND t1.tc_psa02 = tb.tc_psb02 AND t1.tc_psa03 = tb.tc_psb03 AND t1.tc_psa04 = tb.tc_psb04
    		        LEFT JOIN tc_psc_file tc ON t1.tc_psaplant = tc.tc_pscplant AND t1.tc_psa01 = tc.tc_psc01 AND t1.tc_psa02 = tc.tc_psc02 AND t1.tc_psa03 = tc.tc_psc03 AND t1.tc_psa04 = tc.tc_psc04 AND tc.tc_psc05 IN ('11','12','13','14')
    		        WHERE t1.tc_psaplant = :center
    		          AND DATE_FORMAT(t1.tc_psa04, '%Y%m%d') BETWEEN :start AND :end
    		          AND SUBSTRING(tc.tc_psc07, 1, 4) = :preCardNo
    		          AND (
    		            (:preCardNo = '552003' AND SUBSTRING(tc.tc_psc07, 15, 2) = SUBSTRING(:endCardNo, 3, 2))
    		            OR
    		            (:preCardNo != '552003' AND SUBSTRING(tc.tc_psc07, 13, 4) = :endCardNo)
    		          )
    		    """, nativeQuery = true)
    		    List<Object[]> findDetailsByCreditCardNo(
    		            @Param("center") String center, 
    		            @Param("start") String start, 
    		            @Param("end") String end, 
    		            @Param("preCardNo") String preCardNo, 
    		            @Param("endCardNo") String endCardNo);

    		    
    		    
    		    @Query(value = """
    		            SELECT 
    		                DATE_FORMAT(tc_psa04, '%Y%m%d') AS sal_date, 
    		                tc_psaplant AS store_no, 
    		                tc_psa02 AS pos_no, 
    		                tc_psa14 AS trn_no, 
    		                tc_psa13 AS vip_no, 
    		                tc_psa08 AS tot_sales, 
    		                tc_psa12 AS inv_amt, 
    		                tc_psa40 AS promot_amt, 
    		                tc_psa05 AS sal_time, 
    		                tc_psa01 AS tent_no, 
    		                tc_psa03 AS seq_no, 
    		                CONCAT(tc_psa16, tc_psa17) AS inv_no,
    		                tc_psa06 AS sal_type,
    		                NULL AS item_no,
    		                0.0 AS qty,
    		                0.0 AS sal_price,
    		                0.0 AS grd_amt,
    		                NULL AS memo3,
    		                0.0 AS pay_amt,
    		                0 AS installment_period
    		            FROM tc_psa_file 
    		            WHERE tc_psaplant = :center 
    		              AND CONCAT(tc_psa16, tc_psa17) = :invoiceNO 
    		              AND tc_psa06 = '01'
    		            """, nativeQuery = true)
    		    List<TD> getTDByInvoiceNO(@Param("center") String center, @Param("invoiceNO") String invoiceNO);
    		    
    		    
}
    		

	    



