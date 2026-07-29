package com.beyoung.surrounding.bonus.repository;

import java.util.Date; // 統一使用舊式 java.util.Date
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.beyoung.surrounding.app.entity.LSM_FILE;
import com.beyoung.surrounding.app.entity.LSM_FILE_ComposeKey;

@Repository("bonusLsmFileRepository")
public interface LsmFileRepository extends JpaRepository<LSM_FILE, LSM_FILE_ComposeKey> {
	
    /**
     * 1. 舊卡點數紀錄移轉至新卡
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE lsm_file 
            SET lsm01 = :newCardId 
            WHERE lsm01 = :oldCardId
            """, nativeQuery = true)
    int updateCardId(@Param("oldCardId") String oldCardId, @Param("newCardId") String newCardId);
	
    /**
     * 2. 查詢點數歷史明細
     * 說明：日期引數已由 String 統一改為 java.util.Date，並移除資料庫方言函數（STR_TO_DATE）以利完全還原舊定義。
     */
    @Query(value = """
            SELECT 
                lsmstore, 
                lsm01, 
                lsm02, 
                lsm04, 
                lsm05, 
                lsm08,
                ta_lsm02, 
                ta_lsm09,
                CASE WHEN ta_lsm02 = 'EC' THEN 'beyond beyond' ELSE tqa02 END AS tqa02,
                ta_lsm04 AS taLsm04
            FROM lsm_file
            LEFT JOIN lnt_file ON ta_lsm02 = lnt06 AND lsm05 BETWEEN lnt17 AND lnt18
            LEFT JOIN tqa_file ON lnt30 = tqa01
            WHERE lsm01 IN (:cardNos)
              AND lsm05 BETWEEN :startDate AND :endDate
              AND lsm02 IN ('2','5','7','8','9','B')
            """, nativeQuery = true)
    List<LsmHistoryProjection> findPointHistory(
            @Param("cardNos")   List<String> cardNos, 
            @Param("startDate") Date startDate, 
            @Param("endDate")   Date endDate
    );
	
    /**
     * 3. 根據卡號與日期範圍進行點數加總
     * 說明：已將 LocalDateTime 統一更正為 java.util.Date，百分之百與實體及資料庫型態對齊。
     */
    @Query(value = """
            SELECT COALESCE(SUM(lsm08), 0.0) 
            FROM lsm_file 
            WHERE lsm01 = :cardNo 
              AND lsm05 >= :start 
              AND lsm05 <= :end
            """, nativeQuery = true)
    Double calculatePointsByCardNo(
            @Param("cardNo") String cardNo, 
            @Param("start")  Date start, 
            @Param("end")    Date end
    );
    
}