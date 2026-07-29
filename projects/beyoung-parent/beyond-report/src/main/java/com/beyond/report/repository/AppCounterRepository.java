package com.beyond.report.repository;

import com.beyond.report.entity.APP_COUNTER;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppCounterRepository extends JpaRepository<APP_COUNTER, Date> {

    /**
     * 查詢 APP 統計數據 (搭配 Pageable 處理動態分頁與排序)
     */
    @Query(value = """
            SELECT 
                lpj04 AS LPJ04,
                lpj04 AS access_date,
                counter_all AS COUNTER_ALL,
                counter_000 AS COUNTER_000,
                counter_app AS COUNTER_APP,
                counter_beyond AS COUNTER_BEYOND,
                counter_non_beyond AS COUNTER_NON_BEYOND
            FROM APP_COUNTER 
            WHERE lpj04 BETWEEN STR_TO_DATE(:sFromDate, '%Y-%m-%d') 
                            AND STR_TO_DATE(:sEndDate, '%Y-%m-%d')
            ORDER BY lpj04 DESC
            """, nativeQuery = true)
    List<APP_COUNTER> findAppCounterByDateRange(
            @Param("sFromDate") String sFromDate,
            @Param("sEndDate") String sEndDate,
            Pageable pageable
    );
    
    /**
     * 查詢指定日期區間內的總筆數
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM APP_COUNTER 
            WHERE lpj04 BETWEEN STR_TO_DATE(:sFromDate, '%Y-%m-%d') 
                            AND STR_TO_DATE(:sEndDate, '%Y-%m-%d')
            """, nativeQuery = true)
    int countByDateRange(
            @Param("sFromDate") String sFromDate, 
            @Param("sEndDate") String sEndDate
    );
    
    
}