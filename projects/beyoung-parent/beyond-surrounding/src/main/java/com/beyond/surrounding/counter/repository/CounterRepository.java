package com.beyond.surrounding.counter.repository;

import com.beyond.surrounding.app.entity.LPX_FILE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface CounterRepository extends JpaRepository<LPX_FILE, String> {
	
	// 暫時改用 Object[] 來檢查資料是否真的被讀取出來
	@Query(value = """
	    SELECT 
	        t1.lntplant, t1.lnt06, t1.lnt09, t1.lnt30, t2.tqa02 
	    FROM LNT_FILE t1, TQA_FILE t2 
	    WHERE t1.lnt30 = t2.tqa01 
	    AND t1.lnt26 = 'Y' 
	    AND CURRENT_DATE() BETWEEN t1.lnt17 AND t1.lnt18 
	    AND t1.lnt06 IN :ids
	    """, nativeQuery = true)
	List<Object[]> getCounterByIDRaw(@Param("ids") List<String> ids);
	
	@Query(value = """
	    SELECT 
	        t1.lntplant AS lntPlant, 
	        t1.lnt06 AS lnt06, 
	        t1.lnt09 AS lnt09, 
	        t1.lnt30 AS lnt30, 
	        t2.tqa02 AS tqa02 
	    FROM LNT_FILE t1, TQA_FILE t2 
	    WHERE t1.lnt30 = t2.tqa01 
	    AND t1.lnt26 = 'Y' 
	    AND CURRENT_DATE() BETWEEN t1.lnt17 AND t1.lnt18 
	    AND t1.lnt06 IN :ids
	    """, nativeQuery = true)
	List<CounterInfoProjection> getCounterByID(@Param("ids") List<String> ids);
	
	// 在 CounterRepository 中加入
	@Query(value = """
	    SELECT 
	        t1.tc_psa04 AS tcPsa04, 
	        t1.tc_psa05 AS tcPsa05, 
	        t2.lnt06 AS lnt06, 
	        t2.lnt09 AS lnt09, 
	        t3.tqa02 AS tqa02, 
	        t1.tc_psa12 AS tcPsa12, 
	        t1.tc_psa40 AS tcPsa40 
	    FROM TC_PSA_FILE t1, LNT_FILE t2, TQA_FILE t3 
	    WHERE t1.tc_psa01 = t2.lnt06 
	    AND t2.lnt30 = t3.tqa01 
	    AND t1.tc_psa04 BETWEEN t2.lnt17 AND t2.lnt18 
	    AND CONCAT(t1.tc_psa16, t1.tc_psa17) = :invoiceNo 
	    AND (:randomNo = 'uncheck' OR t1.tc_psa31 = :randomNo)
	    """, nativeQuery = true)
	List<InvoiceProjection> getCounterByInvoice(
	    @Param("invoiceNo") String invoiceNo, 
	    @Param("randomNo") String randomNo
	);

	interface InvoiceProjection {
	    Date getTcPsa04();
	    String getTcPsa05();
	    String getLnt06();
	    String getLnt09();
	    String getTqa02();
	    Double getTcPsa12();
	    Double getTcPsa40();
	}
	
	// 在 CounterRepository 介面中新增
	@Query(value = """
	    SELECT DISTINCT 
	        t1.lnt06 AS lnt06, 
	        t1.lnt09 AS lnt09, 
	        t2.oba01 AS oba01, 
	        t2.oba02 AS oba02, 
	        t3.tqa02 AS tqa02 
	    FROM LNT_FILE t1, OBA_FILE t2, TQA_FILE t3 
	    WHERE t1.lnt33 = t2.oba01 
	    AND t1.lnt30 = t3.tqa01 
	    AND t1.lnt26 = 'Y' 
	    AND CURRENT_DATE() <= t1.lnt18 
	    ORDER BY t1.lnt09, t2.oba01, t1.lnt06
	    """, nativeQuery = true)
	List<CounterListProjection> getCounterList();

	interface CounterListProjection {
	    String getLnt06();
	    String getLnt09();
	    String getOba01();
	    String getOba02();
	    String getTqa02();
	}

	 // 1. 修正 getDeptList：使用 Projection 介面
	 @Query(value = """
	     SELECT DISTINCT 
	         t1.lnt09 AS lnt09, 
	         t2.oba01 AS oba01, 
	         t2.oba02 AS oba02 
	     FROM LNT_FILE t1, OBA_FILE t2 
	     WHERE t1.lnt33 = t2.oba01 
	     AND CURRENT_DATE() BETWEEN t1.lnt21 AND t1.lnt22 
	     AND t1.lnt26 = 'Y' 
	     ORDER BY t1.lnt09, t2.oba01
	     """, nativeQuery = true)
	 List<DeptProjection> getDeptList();
	
	 interface DeptProjection {
	     String getLnt09();
	     String getOba01();
	     String getOba02();
	 }
	
	 // 2. 修正 getAllCounter：原生 SQL 不能直接回傳 DTO，請回傳 Projection
	 @Query(value = """
	     SELECT 
	         t1.lntplant AS lntPlant, 
	         t1.lnt06 AS lnt06, 
	         t1.lnt09 AS lnt09, 
	         t1.lnt30 AS lnt30, 
	         t2.tqa02 AS tqa02 
	     FROM LNT_FILE t1, TQA_FILE t2 
	     WHERE t1.lnt30 = t2.tqa01 
	     AND t1.lnt26 = 'Y' 
	     AND CURRENT_DATE() BETWEEN t1.lnt17 AND t1.lnt18
	     """, nativeQuery = true)
	 List<CounterInfoProjection> getAllCounter();
	
	 interface CounterInfoProjection {
	     String getLntPlant();
	     String getLnt06();
	     String getLnt09();
	     String getLnt30();
	     String getTqa02();
	 }
}