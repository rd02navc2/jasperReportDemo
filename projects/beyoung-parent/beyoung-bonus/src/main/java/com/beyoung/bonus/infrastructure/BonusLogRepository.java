package com.beyoung.bonus.infrastructure;

import com.beyoung.bonus.domain.entity.BonusLog;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BonusLogRepository extends JpaRepository<BonusLog, Long> {
	/**
     * 尋找「當前日期 > 點數截止日期」且「剩餘點數 > 0」的紀錄
     * 明確宣告此方法，透過 JPQL 對齊實體欄位，消除與 Service 對接時的編譯錯誤
     */
    @Query("SELECT b FROM BonusLog b WHERE b.expiryDate < :today AND b.point > 0")
    List<BonusLog> findExpiredPoints(@Param("today") LocalDate today);
    
    // SELECT COUNT(1) FROM bonus_log WHERE access_id = ? > 0
    boolean existsByAccessId(String accessId);
}