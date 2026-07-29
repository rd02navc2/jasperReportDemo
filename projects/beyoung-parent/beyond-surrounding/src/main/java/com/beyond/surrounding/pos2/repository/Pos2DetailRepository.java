package com.beyond.surrounding.pos2.repository;

import com.beyond.surrounding.pos2.entity.TD;
import com.beyond.surrounding.pos2.entity.TD_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Pos2DetailRepository extends JpaRepository<TD, TD_ComposeKey> {

	@Query(value = """
            SELECT 
                t1.SAL_DATE, t1.STORE_NO, t1.POS_NO, t1.TRN_NO, t1.VIP_NO, t1.INV_AMT, t1.TOT_SALES, 
                t1.PROMOT_AMT, t1.SAL_TIME, t1.SEQ_NO, t1.INV_NO, t1.SAL_TYPE,
                tr.ITEM_NO, tr.QTY, tr.SAL_PRICE, tr.GRD_AMT, tr.TENT_NO,
                tp.MEMO3, tp.PAY_AMT, tp.INSTALLMENT_PERIOD
            FROM (
                SELECT 
                    t1.SAL_DATE, t1.STORE_NO, t1.POS_NO, t1.TRN_NO, t1.VIP_NO, t1.INV_AMT, t1.TOT_SALES, 
                    t1.T_PROMOT_AMT AS PROMOT_AMT, t1.SAL_TIME, t1.SEQ_NO, 
                    t1.INV_NO AS INV_NO, --  修正：直接讀取 INV_NO，不再使用 CONCAT
                    CASE 
                        WHEN t3.SAL_TYPE = 'D' THEN 'D' 
                        WHEN t2.SAL_TYPE = 'D' OR t2.SAL_TYPE = 'R' THEN 'D' 
                        ELSE t1.SAL_TYPE 
                    END AS SAL_TYPE
                FROM 
                    --  修正：內部子查詢的條件通通改為 INV_NO = :invoiceNo
                    (SELECT * FROM TD WHERE Store_No = :center AND SAL_DATE BETWEEN :startDate AND :endDate AND INV_NO = :invoiceNo AND SAL_TYPE = 'P') t1
                LEFT JOIN 
                    (SELECT * FROM TD WHERE Store_No = :center AND SAL_DATE BETWEEN :startDate AND :endDate AND INV_NO = :invoiceNo AND SAL_TYPE = 'D') t3 
                    ON t1.Store_No = t3.Store_No AND t1.SAL_DATE = t3.SAL_DATE AND t1.POS_NO = t3.POS_NO AND t1.SEQ_NO = t3.SEQ_NO 
                LEFT JOIN 
                    (SELECT * FROM TD WHERE Store_No = :center AND OTRN_DATE BETWEEN :startDate AND :endDate AND TD_NOTE = :invoiceNo AND (SAL_TYPE = 'D' OR SAL_TYPE = 'R')) t2 
                    ON t1.Store_No = t2.Store_No AND t1.SAL_DATE = t2.OTRN_DATE AND t1.POS_NO = t2.OPOS_NO AND t1.SEQ_NO = t2.OTRN_NO
            ) t1, TP, TR	
            WHERE t1.Store_No = tr.Store_No AND t1.Sal_Date = tr.Sal_Date AND t1.Pos_No = tr.Pos_No AND t1.Trn_No = tr.Trn_No 
              AND t1.Store_No = tp.Store_No AND t1.Sal_Date = tp.Sal_Date AND t1.Pos_No = tp.Pos_No AND t1.Trn_No = tp.Trn_No  
              AND tp.Store_No = :center AND tp.SAL_DATE BETWEEN :startDate AND :endDate 
            ORDER BY INV_NO
            """, nativeQuery = true)
    List<TD> findTDDetails(
            @Param("center") String center, 
            @Param("startDate") String startDate, 
            @Param("endDate") String endDate, 
            @Param("invoiceNo") String invoiceNo);
    
    
    
}