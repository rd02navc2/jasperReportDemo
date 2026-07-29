package com.beyoung.bonus.infrastructure;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LsmFileRepository extends JpaRepository<LsmFile, LsmFileId> {
	
	@Modifying
    @Transactional
    @Query(value = """
            UPDATE lsm_file 
            SET lsm01 = :newCardId 
            WHERE lsm01 = :oldCardId
            """, nativeQuery = true)
    int updateCardId(@Param("oldCardId") String oldCardId, @Param("newCardId") String newCardId);
	
	@Query(value = """
            SELECT lsmstore, lsm01, lsm02, lsm04, lsm05, lsm08,
                   ta_lsm02, ta_lsm09,
                   CASE WHEN ta_lsm02 = 'EC' THEN 'beyond beyond' ELSE tqa02 END AS tqa02,
                   ta_lsm04
            FROM lsm_file
            LEFT JOIN lnt_file ON ta_lsm02 = lnt06 AND lsm05 BETWEEN lnt17 AND lnt18
            LEFT JOIN tqa_file ON lnt30 = tqa01
            WHERE lsm01 IN :cardNos
              AND lsm05 BETWEEN STR_TO_DATE(:startDate, '%Y-%m-%d') 
                            AND STR_TO_DATE(:endDate, '%Y-%m-%d')
              AND lsm02 IN ('2','5','7','8','9','B')
            """, nativeQuery = true)
    List<LsmHistoryProjection> findPointHistory(@Param("cardNos") List<String> cardNos, 
                                                @Param("startDate") String startDate, 
                                                @Param("endDate") String endDate);
	
	// 只需要根據卡號 (lsm01) 與日期範圍進行加總
	@Query(value = """
		    SELECT COALESCE(SUM(lsm08), 0.0) 
		    FROM lsm_file 
		    WHERE lsm01 = :cardNo 
		      AND lsm05 >= :start 
		      AND lsm05 <= :end
		    """, nativeQuery = true)
		Double calculatePointsByCardNo(@Param("cardNo") String cardNo, 
		                               @Param("start") LocalDateTime start, 
		                               @Param("end") LocalDateTime end);
	
}