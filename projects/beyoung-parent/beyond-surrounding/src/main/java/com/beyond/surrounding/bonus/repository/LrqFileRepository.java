package com.beyond.surrounding.bonus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.app.entity.LRQ_FILE;
import com.beyond.surrounding.app.entity.LRQ_FILE_ComposeKey;
import java.util.Date; // 統一使用舊式 java.util.Date
import java.util.Optional;

@Repository("bonusLrqFileRepository")
public interface LrqFileRepository extends JpaRepository<LRQ_FILE, LRQ_FILE_ComposeKey> {

    /**
     * 1. 依據日期與專案代號，尋找對應的 LRQ03 點數基底
     */
    @Query(value = """
            SELECT LRQ03 
            FROM LRQ_FILE 
            WHERE LRQACTI = 'Y' 
              AND :today BETWEEN LRQ10 AND LRQ11 
              AND LRQ02 = :lrq02 
              AND LRQPLANT = :center
            """, nativeQuery = true)
    Optional<Integer> findLrq03(
            @Param("lrq02") String lrq02, 
            @Param("center") String center, 
            @Param("today")  Date today
    );
    
    /**
     * 2. 依據目標日期，尋找符合有效期間內的第一筆專案定義
     * 說明：已將 LocalDate 統一更正為 java.util.Date，並讓參數命名完全一致
     */
    @Query(value = """
            SELECT * FROM LRQ_FILE 
            WHERE LRQ01 = :lrq01 
              AND LRQ02 = :lrq02 
              AND LRQPLANT = :center 
              AND LRQACTI = 'Y' 
              AND :targetDate BETWEEN LRQ10 AND LRQ11
            LIMIT 1
            """, nativeQuery = true)
    Optional<LRQ_FILE> findValidProject(
            @Param("lrq01")      String lrq01,
            @Param("lrq02")      String lrq02,
            @Param("center")     String center,
            @Param("targetDate") Date targetDate
    );
    
}