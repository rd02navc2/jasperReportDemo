package com.beyond.report.repository;

import com.beyond.report.entity.LOGDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogDbRepository extends JpaRepository<LOGDB, Long> {

    /**
     * 相容 MySQL 的 Native SQL 查詢
     */
    @Query(value = """
            SELECT 
                t1.LogNum AS logNum,
                t1.UserID AS userID,
                DATE_FORMAT(t1.LogTime, '%Y-%m-%d') AS logTime,
                t1.LogTime AS logTime2,
                t1.WorkingStatus AS workingStatus,
                t2.WSName AS wsName
            FROM LogDB t1 
            INNER JOIN WorkingStatusDB t2 ON t1.WorkingStatus = t2.WSNum
            WHERE t1.LogTime BETWEEN CONCAT(:sFromDate, ' 00:00:00') 
                                 AND CONCAT(:sEndDate, ' 23:59:59')
            ORDER BY t1.LogTime ASC, t1.UserID ASC
            """, nativeQuery = true)
    List<LOGDB> findLoginLogsByDateRange(
            @Param("sFromDate") String sFromDate, 
            @Param("sEndDate") String sEndDate
    );
}