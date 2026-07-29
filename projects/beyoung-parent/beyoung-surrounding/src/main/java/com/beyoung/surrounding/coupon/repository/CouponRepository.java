package com.beyoung.surrounding.coupon.repository;

import com.beyoung.surrounding.app.entity.LQE_FILE;
import com.beyoung.surrounding.app.entity.TC_PSC_FILE;
import com.beyoung.surrounding.app.entity.LPX_FILE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<LQE_FILE, String> {

	public interface LqeStatusProjection {
	    String getLqe01();
	    String getLqe17();
	    Date getLqe20();
	    Date getLqe21();
	    String getTaLqe09();
	    Double getTaLqe02();
	}
	
    // 1. 優惠券狀態查詢
	@Query(value = """
	    SELECT lqe01, lqe17, lqe20, lqe21, 
	           CASE WHEN ta_lqe09 IS NULL THEN 'N' ELSE ta_lqe09 END AS taLqe09, 
	           ta_lqe02 AS taLqe02 
	    FROM LQE_FILE 
	    WHERE lqe01 IN :ids
	    """, nativeQuery = true)
	List<LQE_FILE> getCouponStatus(@Param("ids") List<String> ids);
	 
	@Query(value = """
	    SELECT 
	        t2.tc_pscplant, t2.tc_psc01, t3.tqa02, t2.tc_psc04, t1.tc_psa13, t2.tc_psc07, t2.tc_psc08
	    FROM tc_psa_file t1
	    JOIN tc_psc_file t2 ON TRIM(t1.tc_psa01) = TRIM(t2.tc_psc01)
	    LEFT JOIN lnt_file t4 ON TRIM(t1.tc_psa01) = TRIM(t4.lnt06)
	    LEFT JOIN tqa_file t3 ON t4.lnt30 = t3.tqa01
	    WHERE t2.tc_psc01 = 'PSC001'
	    """, nativeQuery = true)
	List<Object[]> getRawCouponData();

    // 2. 會員優惠券歷史投影介面
    interface CouponHistoryProjection {
        String getTc_pscplant();
        String getTc_psc01();
        String getTqa02();
        Date getTc_psc04();
        String getTc_psa13();
        String getTc_psc07();
        Double getTc_psc08();
    }

    //dc- 目前銷貨檔 tc_psa_file 中的 tc_psa13 (卡號)，在資料庫中是透過哪一張表與 member_id (會員 ID) 進行關聯的？
    // 2. 會員優惠券歷史查詢
    @Query(value = """
	    SELECT 
	        t2.tc_pscplant, t2.tc_psc01, t3.tqa02, t2.tc_psc04, t1.tc_psa13, t2.tc_psc07, t2.tc_psc08
	    FROM tc_psc_file t2
	    INNER JOIN tc_psa_file t1 
	        ON TRIM(t2.tc_psc01) = TRIM(t1.tc_psa01) 
	        AND t2.tc_pscplant = t1.tc_psaplant
	        AND t2.tc_psc04 = t1.tc_psa04
	    LEFT JOIN lnt_file t4 
	        ON TRIM(t2.tc_psc01) = TRIM(t4.lnt06) 
	        AND t2.tc_psc04 BETWEEN t4.lnt17 AND t4.lnt18
	    LEFT JOIN tqa_file t3 
	        ON t4.lnt30 = t3.tqa01
	    WHERE t2.tc_psc05 IN ('07', '38')
	    AND t2.tc_psc04 BETWEEN STR_TO_DATE(?1, '%Y-%m-%d') AND STR_TO_DATE(?2, '%Y-%m-%d')
	    AND t1.tc_psa13 IN (
	        SELECT TRIM(lpj03) FROM lpj_file WHERE TRIM(lpj01) = ?3
	    )
	    AND LENGTH(TRIM(t2.tc_psc07)) > 0
	    ORDER BY t2.tc_psc04
	    """, nativeQuery = true)
	List<TC_PSC_FILE> getCouponHistByMemberID(
	        String startDate, 
	        String endDate, 
	        String memberID);
    
    @Query(value = """
        SELECT lpx01, lpx02, lpx03, lpx04, lpx28 
        FROM lpx_file 
        WHERE lpx01 LIKE 'ZK%' 
        ORDER BY lpx01
        """, nativeQuery = true)
    List<LPX_FILE> getCouponType();
    
}