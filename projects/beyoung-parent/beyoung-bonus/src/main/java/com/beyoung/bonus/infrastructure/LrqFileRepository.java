package com.beyoung.bonus.infrastructure;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Date; // 引用舊式 Date
import java.util.Optional;

public interface LrqFileRepository extends JpaRepository<LrqFile, LrqFileId> {

    @Query(value = "SELECT LRQ03 FROM LRQ_FILE " +
                   "WHERE LRQACTI = 'Y' " +
                   "AND :today BETWEEN LRQ10 AND LRQ11 " +
                   "AND LRQ02 = :lrq02 AND LRQPLANT = :center", nativeQuery = true)
    Optional<Integer> findLrq03(@Param("lrq02") String lrq02, 
                                @Param("center") String center, 
                                @Param("today") Date today); // 改回 Date
    
    @Cacheable(
        value = "bonus:project", 
        key = "#lrq01 + ':' + #lrq02 + ':' + #center", 
        unless = "#result == null || !#result.isPresent()"
    )
    @Query("SELECT l FROM LrqFile l " +
           "WHERE l.lrq01 = :lrq01 " +
           "  AND l.lrq02 = :lrq02 " +
           "  AND l.lrqplant = :center " +
           "  AND l.lrqacti = 'Y' " +
           "  AND :targetDate BETWEEN l.lrq10 AND l.lrq11")
    Optional<LrqFile> findValidProject(
            @Param("lrq01") String lrq01,
            @Param("lrq02") String lrq02,
            @Param("center") String center,
            @Param("targetDate") LocalDate todayDate
    );
}