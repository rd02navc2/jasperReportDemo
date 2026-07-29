package com.beyoung.surrounding.invoice.repository;

import com.beyoung.surrounding.app.entity.LPJ_FILE;
import com.beyoung.surrounding.app.entity.LpjProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

/**
 * LpjFileRepository
 * 對應 LPJ_FILE 的會員資料查詢與積點累加更新
 *
 * ── findActiveMember 對應舊系統 SQL：
 *   SELECT lpj03, lpk04, lpj12, ta_lpj01, ta_lpj02, ta_lpj03
 *   FROM lpj_file, lpk_file
 *   WHERE lpk01 = lpj01
 *     AND lpj01 = ?           -- sMemberID
 *     AND lpj09 = ?           -- "2"
 *     AND ta_lpj04 = 'Y'
 *
 * ── addPoints 對應舊系統 SQL：
 *   UPDATE LPJ_FILE
 *   SET lpj07   = lpj07 + 1,
 *       lpj08   = ?,           -- now (timestamp)
 *       lpj12   = lpj12 + ?,   -- earnedPoint
 *       lpj14   = lpj14 + ?,   -- earnedPoint
 *       lpj15   = lpj15 + ?,   -- consumeAmt
 *       ta_lpj03= ta_lpj03 + ? -- earnedPoint
 *   WHERE lpj01 = ?
 *     AND lpj09 = ?
 *     AND ta_lpj04 = 'Y'
 */
@Repository("invoiceLpjFileRepository")
public interface LpjFileRepository extends JpaRepository<LPJ_FILE, String> {

	// 1. 保留原本的查詢會員卡 SQL
    @Query(value = """
          SELECT lpj03, lpk04, lpj12, ta_lpj01, ta_lpj02, ta_lpj03
          FROM lpk_file, lpj_file
          WHERE lpk01 = lpj01
            AND lpj01 = (SELECT lpj01 FROM lpj_file WHERE LPJ03 = :cardNo)
            AND lpj09 = :lpj09
            AND ta_lpj04 = 'Y'
          """, nativeQuery = true)
    List<Object[]> findLpjInfo(@Param("cardNo") String cardNo, @Param("lpj09") String lpj09);

    // 4. 保留原本的更新累計點數 SQL
    @Modifying // 告知 JPA 這是 DML (Update) 操作
    @Query(value = """
          UPDATE LPJ_FILE
          SET lpj07 = lpj07 + 1,
              lpj08 = :timestamp,
              lpj12 = lpj12 + :point,
              lpj14 = lpj14 + :point,
              ta_lpj03 = ta_lpj03 + :point
          WHERE lpj01 = (SELECT lpj01 FROM lpj_file WHERE LPJ03 = :cardNo)
            AND lpj09 = :lpj09
            AND ta_lpj04 = 'Y'
          """, nativeQuery = true)
    void updateLpjPoint(@Param("timestamp") Date timestamp, 
                        @Param("point") double point, 
                        @Param("cardNo") String cardNo, 
                        @Param("lpj09") String lpj09);
    
    // ── 查詢有效會員資料（JOIN lpk_file 取得姓名與積點資訊）
	@Query(value = """
	        SELECT 
	            j.LPJ01 AS lpj01, 
	            j.LPJ02 AS lpj02, 
	            j.LPJ03 AS lpj03, 
	            j.LPJ07 AS lpj07,
	            j.TA_LPJ01 AS taLpj01,
	            j.TA_LPJ02 AS taLpj02,
	            k.LPK04 AS lpk04
	        FROM lpj_file j
	        JOIN lpk_file k ON k.lpk01 = j.lpj01
	        WHERE j.lpj01    = :memberId
	          AND j.lpj09    = :lpj09
	          AND j.ta_lpj04 = 'Y'
	        """, nativeQuery = true)
	List<LpjProjection> findActiveMember(@Param("memberId") String memberId, @Param("lpj09") String lpj09);

    // ── 更新會員積點（對應舊系統 UPDATE LPJ_FILE）
    @Modifying
    @Query(value = """
            UPDATE lpj_file
            SET lpj07    = lpj07 + 1,
                lpj08    = :now,
                lpj12    = lpj12 + :earnedPoint,
                lpj14    = lpj14 + :earnedPoint,
                lpj15    = lpj15 + :consumeAmt,
                ta_lpj03 = ta_lpj03 + :earnedPoint
            WHERE lpj01    = :memberId
              AND lpj09    = :lpj09
              AND ta_lpj04 = 'Y'
            """, nativeQuery = true)
    void addPoints(
            @Param("memberId")    String memberId,
            @Param("lpj09")       String lpj09,
            @Param("earnedPoint") double earnedPoint,
            @Param("consumeAmt")  double consumeAmt,
            @Param("now")         Date   now);
}
