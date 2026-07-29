package com.beyond.surrounding.card.repository;

import com.beyond.surrounding.app.entity.LPH_FILE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<LPH_FILE, String> {

    /**
     * 使用 Java Text Blocks 與 Native Query
     * 定義一個 Interface Projection 來接收欄位，Spring 會自動幫您處理映射
     */
    interface CardTypeProjection {
        String getLph01();
        String getLph02();
    }

    @Query(value = """
            SELECT 
                LPH01 AS lph01, 
                LPH02 AS lph02 
            FROM LPH_FILE 
            WHERE LPH09 = '0' 
               OR (LPH09 = '1' AND CURRENT_DATE() < LPH10)
            """, nativeQuery = true)
    List<LPH_FILE> getCardType();
    
    
    
}