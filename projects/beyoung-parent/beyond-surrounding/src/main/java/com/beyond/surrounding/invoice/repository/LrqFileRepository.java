package com.beyond.surrounding.invoice.repository;

import com.beyond.surrounding.app.entity.LRQ_FILE;
import com.beyond.surrounding.app.entity.LRQ_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * LrqFileRepository
 * 對應 LRQ_FILE 的積點換算率查詢
 *
 * 對應舊系統 SQL：
 *   SELECT lrq03
 *   FROM lrq_file
 *   WHERE lrqacti = 'Y'
 *     AND TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD') BETWEEN lrq10 AND lrq11
 *     AND lrq02    = ?     -- '601'（一般）; 603=APP, 604=TS, 606=EC
 *     AND lrqplant = ?     -- tc_psaplant
 *
 * 注意：SYSDATE 比對改由資料庫端 CURRENT_DATE 處理，確保時區一致
 */
@Repository("invoiceLrqFileRepository")
public interface LrqFileRepository extends JpaRepository<LRQ_FILE, LRQ_FILE_ComposeKey> {

	// 2. 保留原本的查詢 LRQ 設定點數 SQL
    @Query(value = """
          SELECT lrq03
          FROM lrq_file
          WHERE lrqacti = 'Y'
            AND TO_DATE(TO_CHAR(sysdate, 'YYYY-MM-DD'), 'YYYY-MM-DD') BETWEEN lrq10 AND lrq11
            AND lrq02 = :lrq02
            AND lrqplant = :center
          """, nativeQuery = true)
    List<Integer> findLrqPoint(@Param("lrq02") String lrq02, @Param("center") String center);
    
    @Query(value = """
            SELECT *
            FROM lrq_file
            WHERE lrqacti  = 'Y'
              AND CURRENT_DATE BETWEEN lrq10 AND lrq11
              AND lrq02    = :lrq02
              AND lrqplant = :plant
            """, nativeQuery = true)
    List<LRQ_FILE> findActiveRate(
            @Param("lrq02") String lrq02,
            @Param("plant") String plant);
    
}
