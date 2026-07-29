package com.beyond.surrounding.ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.app.entity.LPJ_FILE;
import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.app.entity.LPL_FILE;
import com.beyond.surrounding.app.entity.LSM_FILE;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface Member4ECRepository extends JpaRepository<LPK_FILE, String> {

    /**
     * 1. 檢查卡號是否存在於 LPK 或 LPJ 檔中
     */
    @Query(value = """
        SELECT COUNT(k.lpk01) 
        FROM lpk_file k 
        LEFT JOIN lpj_file j ON k.lpk01 = j.lpj01 
        WHERE k.lpk03 = :cardNo OR j.lpj03 = :cardNo
        """, nativeQuery = true)
    Long countLPKInternal(@Param("cardNo") String cardNo);

    default boolean isExistLPK(String cardNo) {
        Long count = countLPKInternal(cardNo);
        return count != null && count > 0;
    }

    /**
     * 2. 檢查身分證字號或會員編號是否存在
     * 修正點：改用對 MySQL 驅動最安全的 Long 計數查詢，配合 default 轉 Boolean
     */
    @Query(value = """
        SELECT COUNT(lpk01) 
        FROM lpk_file 
        WHERE lpk03 = :id OR lpk01 = :id
        """, nativeQuery = true)
    Long countByIDInternal(@Param("id") String id);

    default boolean isExistID(String id) {
        Long count = countByIDInternal(id);
        return count != null && count > 0;
    }

    /**
     * 3. 根據身分證號獲取會員主檔與 VIP 級別
     */
    @Query(value = """
        SELECT k.*, 
               j.lpj03 AS LPJ03,
               j.lpj12 AS LPJ12, 
               j.lpj15 AS LPJ15,
               j.ta_lpj01 AS TA_LPJ01, 
               j.ta_lpj02 AS TA_LPJ02, 
               j.ta_lpj03 AS TA_LPJ03, 
               (CASE WHEN (k.lpkud02='62' OR k.lpkud02='66') THEN 1 ELSE 0 END) AS VIP_LEVEL 
        FROM lpk_file k 
        INNER JOIN lpj_file j ON k.lpk01 = j.lpj01 
        WHERE k.lpk03 = :id AND j.lpj09 = '2' AND j.ta_lpj04 = 'Y'
        LIMIT 1
        """, nativeQuery = true)
    Optional<LPK_FILE> findMemberById(@Param("id") String id);

    /**
     * 4. 新增暫存會員基本資料 (LPK_FILE)
     */
    @Modifying
    @Query(value = """
        INSERT INTO LPK_FILE (
            lpk01, lpk02, lpk04, lpk10, lpk13, 
            lpkacti, lpkcrat, lpkoriu, lpkorig, lpkpos, 
            lpkud02, lpkud08, lpkud09, lpkud10, lpk20, 
            lpk21, ta_lpk04, ta_lpk05, ta_lpk06
        ) VALUES (
            :cardNo, '0', '未註冊', '606', '706', 
            'Y', :now, 'admin', '16', '1', 
            '61', 0, 0, 0, '999999', 
            '0', :now, 'Y', '3'
        )
        """, nativeQuery = true)
    void insertTempMember(@Param("cardNo") String cardNo, @Param("now") Date now);

    /**
     * 5. 新增暫存點數主檔資料 (LPJ_FILE)
     */
    @Modifying
    @Query(value = """
        INSERT INTO LPJ_FILE (
            lpj01, lpj02, lpj03, lpj04, lpj06, 
            lpj07, lpj09, lpj12, lpj13, lpj14, 
            lpj15, lpj16, lpj17, lpj18, lpj19, 
            lpjpos, ta_lpj01, ta_lpj02, ta_lpj03, ta_lpj04
        ) VALUES (
            :cardNo, '000', :cardNo, :now, 0, 
            0, '2', 0, 0, 0, 
            0, 'N', 'BY001', :now, 'BY001', 
            '1', 1, 0, 0, 'Y'
        )
        """, nativeQuery = true)
    void insertTempPoint(@Param("cardNo") String cardNo, @Param("now") Date now);

    /**
     * 6. 修改會員通訊資料
     * 修正點：加上 clearAutomatically = true 防止一級快取地雷
     */
    @Modifying(clearAutomatically = true) 
    @Query(value = """
        UPDATE LPK_FILE 
        SET lpk15 = :addr, lpk18 = :mobile, lpk19 = :email, lpkdate = :updateTime 
        WHERE lpk03 = :id
        """, nativeQuery = true)
    void updateMemberById(@Param("id") String id, @Param("mobile") String mobile, @Param("email") String email, @Param("addr") String addr, @Param("updateTime") Date updateTime);
    
    /**
     * 7. 根據身分證號獲取有效的點數主檔
     */
    @Query(value = """
            SELECT * FROM lpj_file 
            WHERE (lpj01 = :id OR lpj03 = :id) 
              AND lpj09 = '2' 
              AND ta_lpj04 = 'Y'
            LIMIT 1
            """, nativeQuery = true)
        Optional<LPJ_FILE> findPointById(@Param("id") String id);

    /**
     * 8. 根據暫存會員編號獲取點數主檔
     */
    @Query(value = """
        SELECT * FROM lpj_file 
        WHERE lpj01 = :memberId AND lpj09 = '2' AND ta_lpj04 = 'Y'
        """, nativeQuery = true)
    Optional<LPJ_FILE> findPointByMemberId(@Param("memberId") String memberId);

    /**
     * 9. 查詢某會員編號的所有歷史卡紀錄
     */
    @Query(value = """
        SELECT * FROM lpl_file 
        WHERE lpl01 = :memberId
        """, nativeQuery = true)
    List<LPL_FILE> findLplByMemberId(@Param("memberId") String memberId);

    /**
     * 10. 獲取該會員歷史卡紀錄的下一個可用序號 (MAX + 1)
     */
    @Query(value = """
        SELECT COALESCE(MAX(lpl09), 0) + 1 
        FROM lpl_file 
        WHERE lpl01 = :memberId
        """, nativeQuery = true)
    Integer getNextLplSeq(@Param("memberId") String memberId);

    /**
     * 11. 查詢點數交易歷史明細歷程
     */
    @Query(value = """
            SELECT lsm.*, 
                   CASE WHEN lsm.ta_lsm02 = 'EC' THEN 'beyond beyond' ELSE tqa.tqa02 END AS extendTqa02 
            FROM lsm_file lsm 
            LEFT JOIN lnt_file lnt ON lsm.ta_lsm02 = lnt.lnt06 AND lsm.lsm05 BETWEEN lnt.lnt17 AND lnt.lnt18 
            LEFT JOIN tqa_file tqa ON lnt.lnt30 = tqa.tqa01 
            WHERE lsm.lsm01 IN (
                SELECT j.lpj03 
                FROM lpk_file k 
                INNER JOIN lpj_file j ON k.lpk01 = j.lpj01 
                WHERE k.lpk03 = :id
            ) 
              AND lsm.lsm05 BETWEEN STR_TO_DATE(:startDate, '%Y-%m-%d') AND STR_TO_DATE(:endDate, '%Y-%m-%d') 
              AND lsm.lsm02 IN ('2','5','7','8','9','B') 
              AND lsm.lsm04 <> 0 
            ORDER BY CONCAT(lsm.lsm05, lsm.ta_lsm04) DESC
            """, nativeQuery = true)
        List<LSM_FILE> getPointHistById(@Param("id") String id, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 12. 歸戶：將暫存點數累加併入主會員卡
     */
    @Modifying
    @Query(value = """
        UPDATE LPJ_FILE 
        SET lpj07 = lpj07 + :lpj07, 
            lpj12 = lpj12 + :lpj12, 
            lpj14 = lpj14 + :lpj14, 
            lpj15 = lpj15 + :lpj15, 
            ta_lpj02 = ta_lpj02 + :taLpj02, 
            ta_lpj03 = ta_lpj03 + :taLpj03 
        WHERE lpj09 = '2' AND lpj01 = :memberId AND ta_lpj04 = 'Y'
        """, nativeQuery = true)
    void mergePointToMainCard(@Param("memberId") String memberId, @Param("lpj07") Integer lpj07, @Param("lpj12") Double lpj12, 
                              @Param("lpj14") Double lpj14, @Param("lpj15") Double lpj15, @Param("taLpj02") Double taLpj02, @Param("taLpj03") Double taLpj03);

    /**
     * 13. 歸戶：將被合併的暫存卡點數歸屬人改為主卡，並將效期標記 ta_lpj04 設為 'N' (失效)
     */
    @Modifying
    @Query(value = """
        UPDATE LPJ_FILE 
        SET lpj01 = :memberId, lpj02 = 'EC', ta_lpj04 = 'N' 
        WHERE lpj09 = '2' AND lpj01 = :tempMemberId
        """, nativeQuery = true)
    void disableTempPointCard(@Param("memberId") String memberId, @Param("tempMemberId") String tempMemberId);

    /**
     * 14. 歸戶：移轉舊卡歷史紀錄 (LPL_FILE) 的關聯
     */
    @Modifying
    @Query(value = """
        UPDATE LPL_FILE 
        SET lpl01 = :memberId, lpl09 = :newSeq 
        WHERE lpl01 = :tempMemberId AND lpl02 = :lpl02 AND lpl09 = :oldSeq
        """, nativeQuery = true)
    void updateLplHistory(@Param("memberId") String memberId, @Param("newSeq") Integer newSeq, @Param("tempMemberId") String tempMemberId, @Param("lpl02") Date lpl02, @Param("oldSeq") Integer oldSeq);

    /**
     * 15. 歸戶：將舊點數交易明細歷程 (LSM_FILE) 轉載至新主卡
     */
    @Modifying
    @Query(value = """
        UPDATE LSM_FILE 
        SET lsm01 = :mainCardId 
        WHERE lsm01 = :tempMemberId
        """, nativeQuery = true)
    void transferPointHistory(@Param("mainCardId") String mainCardId, @Param("tempMemberId") String tempMemberId);

    /**
     * 16. 正式化：升級正式會員並帶入性別註記
     */
    @Modifying
    @Query(value = """
        UPDATE LPK_FILE 
        SET lpk06 = :gender, lpk03 = :id, lpk04 = :name, lpk05 = :birthday, lpk15 = :address, lpk18 = :mobile, lpk19 = :email 
        WHERE lpk01 = :tempMemberId
        """, nativeQuery = true)
    void formalizeMemberWithGender(@Param("tempMemberId") String tempMemberId, @Param("id") String id, @Param("name") String name, @Param("birthday") Date birthday, @Param("address") String address, @Param("mobile") String mobile, @Param("email") String email, @Param("gender") String gender);

    /**
     * 17. 正式化：升級正式會員 (無法識別性別時的備用更新)
     */
    @Modifying
    @Query(value = """
        UPDATE LPK_FILE 
        SET lpk03 = :id, lpk04 = :name, lpk05 = :birthday, lpk15 = :address, lpk18 = :mobile, lpk19 = :email 
        WHERE lpk01 = :tempMemberId
        """, nativeQuery = true)
    void formalizeMemberWithoutGender(@Param("tempMemberId") String tempMemberId, @Param("id") String id, @Param("name") String name, @Param("birthday") Date birthday, @Param("address") String address, @Param("mobile") String mobile, @Param("email") String email);

    /**
     * 18. 正式化：更新點數主檔卡片狀態為 'EC'
     */
    @Modifying
    @Query(value = """
        UPDATE LPJ_FILE 
        SET lpj02 = 'EC' 
        WHERE lpj09 = '2' AND lpj01 = :tempMemberId
        """, nativeQuery = true)
    void formalizePointCard(@Param("tempMemberId") String tempMemberId);
    
}