package com.beyoung.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LpjFileRepository extends JpaRepository<LpjFile, LpjFileId> {

    /**
     * 更正優化版：移除高風險且低效能的同表子查詢，改用顯式關聯，安全防範 Multi-row 錯誤
     */
    @Query(value = "SELECT j.lpj03 as lpj03, k.lpk04 as lpk04, j.lpj12 as lpj12, " +
                   "j.ta_lpj01 as taLpj01, j.ta_lpj02 as taLpj02, j.ta_lpj03 as taLpj03 " +
                   "FROM lpk_file k, lpj_file j " +
                   "WHERE k.lpk01 = j.lpj01 " +
                   "AND j.lpj03 = :cardNo " + // member 核心優化：直接用 j.lpj03 匹配卡號，告別危險的 (SELECT sub.lpj01...)
                   "AND j.lpj09 = :lpj09 " +
                   "AND j.ta_lpj04 = 'Y'", nativeQuery = true)
    Optional<LpjFileProjection> findMemberCardInfo(@Param("cardNo") String cardNo, @Param("lpj09") String lpj09);

    /**
     * 已更正版：移除未定義的 j. 別名，修復 Unknown column 錯誤
     */
    /*
    @Transactional 
    @Modifying(clearAutomatically = true) 
    @Query(value = "UPDATE lpj_file " +
                   "SET lpj07 = lpj07 + 1, lpj08 = :ts, lpj12 = lpj12 + :point, " +
                   "lpj14 = lpj14 + :point, ta_lpj03 = ta_lpj03 + :point " +
                   "WHERE lpj03 = :cardNo " +
                   "AND lpj09 = :lpj09 " + // member 核心更正：已移除原先錯誤的 j.
                   "AND ta_lpj04 = 'Y'", nativeQuery = true)
    int updateMemberPoints(@Param("ts") Date ts, @Param("point") double point, 
                           @Param("cardNo") String cardNo, @Param("lpj09") String lpj09);
    */
    
    @Transactional 
    @Modifying(clearAutomatically = true) 
    @Query(value = "UPDATE lpj_file " +
                   "SET lpj07 = lpj07 + 1, " +
                   "lpj12 = COALESCE(lpj12, 0) + :point, " + // member 防範原本欄位為 NULL 的情況
                   "lpj14 = COALESCE(lpj14, 0) + :point, " +
                   "ta_lpj03 = COALESCE(ta_lpj03, 0) + :point " +
                   "WHERE lpj03 = :cardNo " +
                   "AND lpj09 = :lpj09 " +
                   "AND (ta_lpj04 = 'Y' OR ta_lpj04 IS NULL OR ta_lpj04 = '')", nativeQuery = true) // member 放寬主卡旗標校驗
    int updateMemberPoints(@Param("point") double point, 
                           @Param("cardNo") String cardNo, 
                           @Param("lpj09") String lpj09);
    
    /**
     * 供 Bonus 微服務經由 RPC 呼叫時，Member 端獲取該會員名下所有已綁定的動態卡號清單
     */
    @Query("SELECT l.lpj03 FROM LpjFile l WHERE l.lpj01 = :memberId")
    List<String> findCardNosByLpj01(@Param("memberId") String memberId);

    /**
     * 底層保留之業務邏輯：依據卡號與特定卡片等級狀態取得會員資訊
     */
    @Query("SELECT l FROM LpjFile l WHERE l.lpj03 = :cardId AND l.lpj02 = :lpj02")
    Optional<LpjFile> findByLpj03AndLpj02(@Param("cardId") String cardId, @Param("lpj02") String lpj02);

	Optional<LpjFile> findByLpj03(String cardNo);
}