package com.beyoung.surrounding.invoice.repository;

import com.beyoung.surrounding.app.entity.LPL_FILE;
import com.beyoung.surrounding.app.entity.LPL_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;

/**
 * LplFileRepository
 * 對應 LPL_FILE 的商品明細寫入與流水號（lpl09）查詢
 *
 * ── findNextSeq 對應舊系統 SQL：
 *   SELECT MAX(lpl09) + 1 AS lpl09
 *   FROM lpl_file
 *   WHERE lpl01    = ?   -- lpj03 (會員卡號)
 *     AND lpl02    = ?   -- tc_psa04 (發票日期)
 *     AND lplplant = ?   -- tc_psaplant
 *
 *   ※ 若無資料則 MAX() 回傳 NULL，Service 端已處理 null → 1
 *
 * ── save() 對應舊系統 INSERT INTO LPL_FILE (...)
 *   由 JpaRepository.save() 提供，無需自訂 Query。
 */
@Repository("invoiceLplFileRepository")
public interface LplFileRepository extends JpaRepository<LPL_FILE, LPL_FILE_ComposeKey> {

    @Query(value = """
            SELECT MAX(lpl09) + 1
            FROM lpl_file
            WHERE lpl01    = :lpl01
              AND lpl02    = :lpl02
              AND lplplant = :plant
            """, nativeQuery = true)
    Integer findNextSeq(
            @Param("lpl01")  String lpl01,
            @Param("lpl02")  Date   lpl02,
            @Param("plant")  String plant);
}
