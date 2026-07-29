package com.beyond.surrounding.pos2.repository;

import com.beyond.surrounding.pos2.entity.LntFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface CounterPos2Repository extends JpaRepository<LntFile, String> {

    // 透過 Map 接收，完美避開 Hibernate 6 對 Entity 欄位完整度的強制檢查
    @Query(value = """
            SELECT 
                t1.lntplant AS LNTPLANT, 
                t1.lnt06 AS LNT06, 
                t1.lnt09 AS LNT09, 
                t1.lnt30 AS LNT30, 
                t2.tqa02 AS TQA02
            FROM lnt_file t1, tqa_file t2 
            WHERE 1=1 
              AND CURRENT_DATE BETWEEN t1.lnt17 AND t1.lnt18 
              AND t1.lnt30 = t2.tqa01 
              AND t1.lnt26 = 'Y'
            """, nativeQuery = true)
    List<Map<String, Object>> getAllCounter();
    
    // 透過 Map 接收，完美避開複合主鍵欄位不齊全時的 Hibernate 映射報錯
    @Query(value = """
            SELECT 
                RYC01, 
                RYC02, 
                RYC04, 
                RYC06, 
                TA_RYC09, 
                DATE_FORMAT(TA_RYC11, '%Y-%m-%d') AS TA_RYC11, 
                DATE_FORMAT(TA_RYC12, '%Y-%m-%d') AS TA_RYC12, 
                RYCACTI
            FROM ryc_file
            WHERE 1=1
              AND CURRENT_DATE BETWEEN TA_RYC11 AND TA_RYC12
            """, nativeQuery = true)
    List<Map<String, Object>> getPOSDataRaw();
    
    
}