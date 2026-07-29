package com.beyoung.surrounding.bonus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.app.entity.LPJ_FILE;
import java.util.List;
import java.util.Optional;

@Repository("bonusLpjFileRepository")
public interface LpjFileRepository extends JpaRepository<LPJ_FILE, String> {

    /**
     * 更正優化最終文字塊版：
     * 1. 揚棄 Projection 介面，直接回傳 Optional<LpjFile>。
     * 2. 使用 Java Text Blocks (""") 保持原生 SQL 格式，大幅提升維護性。
     * 3. 別名（as）精準對齊 LpjFile 實體的屬性名稱，確保混合欄位 lpk04 自動填入。
     */
    @Query(value = """
        SELECT
            j.LPJ03 as lpj03,
            j.LPJ01 as lpj01,
            j.LPJ02 as lpj02,
            j.LPJ12 as lpj12,
            j.LPJ09 as lpj09,
            j.TA_LPJ01 as taLpj01,
            j.TA_LPJ02 as taLpj02,
            j.TA_LPJ03 as taLpj03,
            j.TA_LPJ04 as taLpj04,
            k.LPK04 as lpk04
        FROM LPJ_FILE j
        INNER JOIN LPK_FILE k ON k.LPK01 = j.LPJ01
        WHERE j.LPJ03 = :cardNo
          AND j.LPJ09 = :lpj09
          AND j.TA_LPJ04 = 'Y'
        """, nativeQuery = true)
    Optional<LPJ_FILE> findMemberCardInfo(@Param("cardNo") String cardNo, @Param("lpj09") String lpj09);
	
    /**
     * 更新會員點數
     */
    @Modifying(clearAutomatically = true) 
    @Query(value = """
        UPDATE lpj_file 
        SET lpj07 = lpj07 + 1, 
            lpj12 = COALESCE(lpj12, 0) + :point, 
            lpj14 = COALESCE(lpj14, 0) + :point, 
            ta_lpj03 = COALESCE(ta_lpj03, 0) + :point 
        WHERE lpj03 = :cardNo 
          AND lpj09 = :lpj09 
          AND (ta_lpj04 = 'Y' OR ta_lpj04 IS NULL)
        """, nativeQuery = true)
    int updateMemberPoints(@Param("point") double point, 
                           @Param("cardNo") String cardNo, 
                           @Param("lpj09") String lpj09);
    
    /**
     * 供 Bonus 微服務經由 RPC 呼叫時，Member 端獲取該會員名下所有已綁定的動態卡號清單
     */
    @Query(value = """
            SELECT lpj03 
            FROM lpj_file 
            WHERE lpj01 = :memberId
            """, nativeQuery = true)
    List<String> findCardNosByLpj01(@Param("memberId") String memberId);

    /**
     * 修正：全面改為原生 SQL，並修正原先 @Param("cardId") 與 :lpj03 命名不一致的問題
     */
    @Query(value = """
            SELECT * FROM lpj_file 
            WHERE lpj03 = :cardId 
              AND lpj02 = :lpj02
            """, nativeQuery = true)
    Optional<LPJ_FILE> findByLpj03AndLpj02(@Param("cardId") String cardId, @Param("lpj02") String lpj02);

    /**
     * 修正：全面改為原生 SQL 文字塊，排除 Spring 啟動時的衍生查詢推導風險
     */
    @Query(value = """
            SELECT * FROM lpj_file 
            WHERE lpj03 = :cardNo
            """, nativeQuery = true)
    Optional<LPJ_FILE> findByLpj03(@Param("cardNo") String cardNo);
    
}