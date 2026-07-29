package com.beyoung.surrounding.member.repository;

import com.beyoung.surrounding.app.entity.LPJ_FILE;
import com.beyoung.surrounding.app.entity.LPK_FILE;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository("memMemberRepository")
public interface MemberRepository extends JpaRepository<LPK_FILE, String> {

    @Query(value = """
            SELECT COUNT(*)
            FROM LPK_FILE t1
            LEFT JOIN LPJ_FILE t2 ON t1.LPK01 = t2.LPJ01
            WHERE t1.LPK01 = :memberId OR t2.LPJ03 = :memberId
            """, nativeQuery = true)
    long countByMemberIdOrCardId(@Param("memberId") String memberId);

    default boolean isExistLpk(String memberId) {
        return countByMemberIdOrCardId(memberId) > 0;
    }

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO LPK_FILE (
                LPK01, LPK02, LPK04, LPK10, LPK13, LPKACTI, LPKCRAT, 
                LPKORIU, LPKORIG, LPKPOS, LPKUD02, LPKUD08, LPKUD09, 
                LPKUD10, LPK20, LPK21, TA_LPK04, TA_LPK05, TA_LPK06
            ) VALUES (
                :memberId, '0', :lpk04, '603', '703', 'Y', :now, 
                'admin', '16', '1', '61', 0.0, 0.0, 
                0, '999999', '0', :now, 'Y', '3'
            )
            """, nativeQuery = true)
    void insertLpkTempMember(@Param("memberId") String memberId, @Param("lpk04") String lpk04, @Param("now") Date now);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO LPJ_FILE (
                LPJ01, LPJ02, LPJ03, LPJ04, LPJ06, LPJ07, LPJ09, 
                LPJ12, LPJ13, LPJ14, LPJ15, LPJ16, LPJ17, LPJ18, 
                LPJ19, LPJPOS, TA_LPJ01, TA_LPJ02, TA_LPJ03, TA_LPJ04
            ) VALUES (
                :memberId, :lpj02, :cardId, :now, 0.0, 0, '2', 
                0.0, 0.0, 0.0, 0.0, 'N', 'BY001', :now, 
                'BY001', '1', 1.0, 0.0, 0.0, 'Y'
            )
            """, nativeQuery = true)
    void insertLpjTempMember(@Param("memberId") String memberId, @Param("lpj02") String lpj02, @Param("cardId") String cardId, @Param("now") Date now);

    @Transactional
    default void addTempMember(String center, String memberId, String cardId) {
        Date now = new Date();
        insertLpkTempMember(memberId, "未註冊", now);
        insertLpjTempMember(memberId, "000", cardId, now);
    }

    @Transactional
    default void addRsTempMember(String center, String memberId, String cardId) {
        Date now = new Date();
        insertLpkTempMember(memberId, "臨時會員", now);
        insertLpjTempMember(memberId, "TEMP", cardId, now);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            SELECT * FROM LPJ_FILE lpj 
            WHERE lpj.LPJ03 = :cardId AND lpj.LPJ09 = '2'
            """, nativeQuery = true)
    List<LPJ_FILE> findLpjByCardIdForUpdate(@Param("cardId") String cardId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPJ_FILE 
            SET LPJ07 = LPJ07 + :points, LPJ18 = CURRENT_TIMESTAMP 
            WHERE LPJ03 = :cardId AND LPJ09 = '2'
            """, nativeQuery = true)
    int updatePointsNative(@Param("cardId") String cardId, @Param("points") int points);

    @Transactional
    default void updateMemberPoints(String cardId, Double points) {
        List<LPJ_FILE> results = findLpjByCardIdForUpdate(cardId);
        if (results.isEmpty()) {
            throw new IllegalArgumentException("[Member微服務] 找不到對應的有效會卡帳戶，卡號: " + cardId);
        }
        updatePointsNative(cardId, points.intValue());
    }

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPK_FILE 
            SET LPK15 = :addr, LPK18 = :mobile, LPK19 = :email, LPKDATE = CURRENT_TIMESTAMP 
            WHERE LPK01 = :memberId
            """, nativeQuery = true)
    void updateMemberContact(@Param("memberId") String memberId, @Param("mobile") String mobile, @Param("email") String email, @Param("addr") String addr);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            SELECT * FROM LPK_FILE WHERE LPK01 = :memberId
            """, nativeQuery = true)
    Optional<LPK_FILE> findLpkByIdForUpdate(@Param("memberId") String memberId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPJ_FILE 
            SET LPJ07    = LPJ07    + :lpj07,
                LPJ12    = LPJ12    + :lpj12,
                LPJ14    = LPJ14    + :lpj14,
                LPJ15    = LPJ15    + :lpj15,
                TA_LPJ02 = TA_LPJ02 + :taLpj02,
                TA_LPJ03 = TA_LPJ03 + :taLpj03
            WHERE LPJ09 = '2' AND LPJ01 = :memberId AND TA_LPJ04 = 'Y'
            """, nativeQuery = true)
    void updateMainMemberPoints(@Param("memberId") String memberId, @Param("lpj07") Integer lpj07, @Param("lpj12") Double lpj12, 
                                @Param("lpj14") Double lpj14, @Param("lpj15") Double lpj15, @Param("taLpj02") Double taLpj02, @Param("taLpj03") Double taLpj03);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPJ_FILE 
            SET LPJ02 = 'MERGED', TA_LPJ04 = 'N', TA_LPJ05 = :memberId 
            WHERE LPJ09 = '2' AND LPJ01 = :tempId
            """, nativeQuery = true)
    void mergeTempLpj(@Param("memberId") String memberId, @Param("tempId") String tempId);

    @Query(value = """
            SELECT COALESCE(MAX(LPL09), 0) FROM LPL_FILE WHERE LPL01 = :memberId
            """, nativeQuery = true)
    Integer getMaxLplSeq(@Param("memberId") String memberId);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPL_FILE 
            SET LPL01 = :memberId, LPL09 = :newSeq 
            WHERE LPL01 = :tempId AND LPL02 = :lpl02 AND LPL09 = :oldSeq AND LPLPLANT = :lplplant
            """, nativeQuery = true)
    void transferLplRecord(@Param("memberId") String memberId, @Param("newSeq") int newSeq, @Param("tempId") String tempId, 
                            @Param("lpl02") Date lpl02, @Param("oldSeq") int oldSeq, @Param("lplplant") String lplplant);

    @Query(value = """
            SELECT * FROM LPL_FILE WHERE LPL01 = :tempId
            """, nativeQuery = true)
    List<Object[]> findLplRecordsByTempId(@Param("tempId") String tempId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM LPK_FILE WHERE LPK01 = :tempId
            """, nativeQuery = true)
    void deleteLpkById(@Param("tempId") String tempId);

    @Transactional
    default void doHousehold(String tempMemberId, LPJ_FILE bean, String memberId, String mainCardId) {
        findLpkByIdForUpdate(memberId).orElseThrow(() -> new IllegalArgumentException("找不到主會員資料: " + memberId));

        updateMainMemberPoints(memberId, bean.getLpj07(), bean.getLpj12(), bean.getLpj14(), bean.getLpj15(), bean.getTaLpj02(), bean.getTaLpj03());
        mergeTempLpj(memberId, tempMemberId);

        Integer maxSeqResult = getMaxLplSeq(memberId);
        int currentMaxSeq = maxSeqResult != null ? maxSeqResult : 0;

        List<Object[]> tempLplList = findLplRecordsByTempId(tempMemberId);
        for (Object[] row : tempLplList) {
            currentMaxSeq++;
            String oldLpl01 = (String) row[0];
            Date lpl02 = (Date) row[1];
            int oldSeq = ((Number) row[8]).intValue();
            String lplplant = (String) row[10];
            transferLplRecord(memberId, currentMaxSeq, oldLpl01, lpl02, oldSeq, lplplant);
        }

        deleteLpkById(tempMemberId);
    }

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPK_FILE 
            SET LPK03 = :id, LPK04 = :name, LPK05 = :birthday, LPK06 = :gender, 
                LPK15 = :address, LPK18 = :mobile, LPK19 = :email, LPKDATE = CURRENT_TIMESTAMP 
            WHERE LPK01 = :tempMemberId
            """, nativeQuery = true)
    void updateLpkToFormal(@Param("tempMemberId") String tempMemberId, @Param("name") String name, @Param("id") String id, 
                            @Param("birthday") String birthday, @Param("gender") String gender, @Param("address") String address, 
                            @Param("mobile") String mobile, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE LPJ_FILE SET LPJ02 = 'APP' WHERE LPJ09 = '2' AND LPJ01 = :tempId
            """, nativeQuery = true)
    void updateLpjToFormal(@Param("tempId") String tempId);

    @Transactional
    default void doFormal(String tempMemberId, String name, String id,
                          String birthday, String mobile, String address, String email) throws Exception {
        Optional<LPK_FILE> lpkOpt = findLpkByIdForUpdate(tempMemberId);
        if (lpkOpt.isEmpty()) {
            throw new IllegalArgumentException("找不到要轉正的臨時會員: " + tempMemberId);
        }

        String gender = null;
        if (id != null && id.length() >= 2) {
            String idSecond = id.substring(1, 2);
            if ("1".equals(idSecond)) gender = "1";
            else if ("2".equals(idSecond)) gender = "0";
        }

        String uppercaseId = (id != null) ? id.toUpperCase() : null;
        updateLpkToFormal(tempMemberId, name, uppercaseId, birthday, gender, address, mobile, email);
        updateLpjToFormal(tempMemberId);
    }

    @Query(value = """
            SELECT * FROM LPJ_FILE 
            WHERE LPJ01 = :memberId AND LPJ09 = '2' AND LPJ03 IS NOT NULL AND TRIM(LPJ03) != ''
            """, nativeQuery = true)
    List<LPJ_FILE> getAllCardByMemberId(@Param("memberId") String memberId);

    @Query(value = """
            SELECT lpj.* FROM LPJ_FILE lpj 
            JOIN LPK_FILE lpk ON lpj.LPJ01 = lpk.LPK01 
            WHERE lpk.LPK03 = :id AND lpj.LPJ09 = '2'
            """, nativeQuery = true)
    List<LPJ_FILE> getAllCardById(@Param("id") String id);

    @Query(value = """
            SELECT * FROM LPJ_FILE 
            WHERE LPJ01 = :memberId AND LPJ09 = '2' AND TA_LPJ04 = 'Y'
            """, nativeQuery = true)
    List<LPJ_FILE> getPointByMemberIdNative(@Param("memberId") String memberId);

    default LPJ_FILE getPointByMemberId(String memberId) {
        List<LPJ_FILE> list = getPointByMemberIdNative(memberId);
        return list.isEmpty() ? new LPJ_FILE() : list.get(list.size() - 1);
    }

    @Query(value = """
            SELECT * FROM LPJ_FILE 
            WHERE LPJ01 = :id AND LPJ09 = '2' AND TA_LPJ04 = 'Y'
            """, nativeQuery = true)
    Optional<LPJ_FILE> getMemberDirectlyNative(@Param("id") String id);

    default LPJ_FILE getMemberDirectly(String id) {
        return getMemberDirectlyNative(id).orElse(null);
    }

    @Query(value = """
            SELECT t2.* FROM LPK_FILE t1 
            JOIN LPJ_FILE t2 ON t1.LPK01 = t2.LPJ01 
            WHERE t1.LPK03 = :id AND t2.LPJ09 = '2' AND t2.TA_LPJ04 = 'Y'
            """, nativeQuery = true)
    List<LPJ_FILE> getPointByIdNative(@Param("id") String id);

    default LPJ_FILE getPointById(String id) {
        List<LPJ_FILE> list = getPointByIdNative(id);
        return list.isEmpty() ? new LPJ_FILE() : list.get(list.size() - 1);
    }

    @Query(value = """
            SELECT * FROM LPK_FILE WHERE LPK01 = :memberId
            """, nativeQuery = true)
    List<LPK_FILE> getMemberContactNative(@Param("memberId") String memberId);

    default LPK_FILE getMemberContact(String memberId) {
        List<LPK_FILE> list = getMemberContactNative(memberId);
        return list.isEmpty() ? new LPK_FILE() : list.get(list.size() - 1);
    }

    @Query(value = """
            SELECT t1.* FROM LPK_FILE t1 
            JOIN LPJ_FILE t2 ON t1.LPK01 = t2.LPJ01 
            WHERE t1.LPK03 = :id AND t2.LPJ09 = '2' AND t2.TA_LPJ04 = 'Y' 
            LIMIT 1
            """, nativeQuery = true)
    List<LPK_FILE> getMemberContactByIdNative(@Param("id") String id);

    default LPK_FILE getMemberContactById(String id) {
        List<LPK_FILE> list = getMemberContactByIdNative(id);
        return list.isEmpty() ? new LPK_FILE() : list.get(0);
    }

    @Query(value = """
            SELECT t1.* FROM LPK_FILE t1 
            JOIN LPJ_FILE t2 ON t1.LPK01 = t2.LPJ01 
            WHERE t2.LPJ03 = :cardId AND t2.LPJ09 = '2' AND t2.TA_LPJ04 = 'Y' 
            LIMIT 1
            """, nativeQuery = true)
    List<LPK_FILE> getMemberContactByCardIdNative(@Param("cardId") String cardId);

    default LPK_FILE getMemberContactByCardId(String cardId) {
        List<LPK_FILE> list = getMemberContactByCardIdNative(cardId);
        return list.isEmpty() ? new LPK_FILE() : list.get(0);
    }

    @Query(value = """
            SELECT t1.* FROM LPK_FILE t1 
            JOIN LPJ_FILE t2 ON t1.LPK01 = t2.LPJ01 
            WHERE t2.LPJ03 = :cardId AND t2.LPJ09 = '2' AND t2.TA_LPJ04 = 'Y'
            """, nativeQuery = true)
    List<LPK_FILE> getMemberByCardIdNative(@Param("cardId") String cardId);

    default LPK_FILE getMemberByCardId(String cardId) {
        List<LPK_FILE> list = getMemberByCardIdNative(cardId);
        return list.isEmpty() ? new LPK_FILE() : list.get(list.size() - 1);
    }

    @Query(value = """
            SELECT lpk.* FROM LPK_FILE lpk 
            JOIN LPJ_FILE lpj ON lpk.LPK01 = lpj.LPJ01 
            WHERE (lpj.LPJ03 = :cid OR lpk.LPK18 = :cid OR SUBSTRING(lpk.LPK03, 2, 9) = :cid) 
              AND lpj.LPJ09 = '2' AND lpj.TA_LPJ04 = 'Y' 
            LIMIT 1
            """, nativeQuery = true)
    List<LPK_FILE> getMemberByCardId2Native(@Param("cid") String cardId);

    default LPK_FILE getMemberByCardId2(String cardId) {
        List<LPK_FILE> list = getMemberByCardId2Native(cardId);
        return list.isEmpty() ? new LPK_FILE() : list.get(0);
    }

   @Query(value = """
            SELECT 
                lpk.lpk01, 
                lpk.lpk03,   -- 🛠️ 關鍵修正：明確指定 lpk. 防止欄位 ambiguous 衝突
                lpk.lpk04, 
                lpk.lpkud02, 
                lpk.lpk18
            FROM lpk_file lpk, lpj_file lpj  -- 🛠️ 幫這兩張表加上別名 lpk 與 lpj
            WHERE lpk.lpk01 = lpj.lpj01
              AND (
                   lpj.lpj03 = (SELECT lpj03 FROM lpj_file WHERE lpj01 = (SELECT lpj01 FROM lpj_file WHERE lpj03 = :cardId LIMIT 1) AND lpj09 = '2' AND ta_lpj04 = 'Y' LIMIT 1)
                OR lpj.lpj03 = (SELECT lpj03 FROM lpk_file, lpj_file WHERE lpk01 = lpj01 AND substr(lpk03, 2, 9) = :cardId AND lpj09 = '2' AND ta_lpj04 = 'Y' LIMIT 1)
                OR lpj.lpj03 = (SELECT lpj03 FROM lpk_file, lpj_file WHERE lpk01 = lpj01 AND lpk18 = :cardId AND lpj09 = '2' AND ta_lpj04 = 'Y' LIMIT 1)
              )
            """, nativeQuery = true)
    LPK_FILE getMemberByCardID3(@Param("cardId") String cardId);

    default LPK_FILE getMemberByCardId4(String cardId) {
        return getMemberByCardId2(cardId);
    }

    @Query(value = """
            SELECT LPJ01 FROM LPJ_FILE WHERE LPJ03 = :cardId AND LPJ02 = '000'
            """, nativeQuery = true)
    List<String> checkIs000(@Param("cardId") String cardId);

    default boolean is000(String cardId) {
        return !checkIs000(cardId).isEmpty();
    }

    @Query(value = """
            SELECT lpk.* FROM LPK_FILE lpk 
            JOIN LPJ_FILE lpj ON lpk.LPK01 = lpj.LPJ01 
            WHERE lpj.LPJ03 = :cardId AND lpj.LPJ09 = '2' AND lpj.TA_LPJ04 = 'Y' 
            LIMIT 1
            """, nativeQuery = true)
    List<LPK_FILE> getMainCardNative(@Param("cardId") String cardId);

    default LPK_FILE getMainCard(String cardId) {
        List<LPK_FILE> list = getMainCardNative(cardId);
        return list.isEmpty() ? new LPK_FILE() : list.get(0);
    }

    @Query(value = """
            SELECT lpk.* FROM LPK_FILE lpk 
            JOIN LPJ_FILE lpj ON lpk.LPK01 = lpj.LPJ01 
            WHERE lpk.LPK03 = :id AND lpj.LPJ09 = '2' AND lpj.TA_LPJ04 = 'Y' 
            LIMIT 1
            """, nativeQuery = true)
    List<LPK_FILE> findMemberByCardNoNative(@Param("id") String id);

    default LPK_FILE findMemberByCardNo(String id) {
        List<LPK_FILE> list = findMemberByCardNoNative(id);
        return list.isEmpty() ? new LPK_FILE() : list.get(0);
    }
    
    @Query(value = """
            SELECT 
                lpk.lpk01   AS lpk01, 
                lpk.lpk03   AS lpk03, 
                lpk.lpk04   AS lpk04, 
                lpk.lpkud02 AS lpkud02, 
                lpk.lpk18   AS lpk18
            FROM 
                lpk_file lpk, 
                lpj_file lpj 
            WHERE 
                lpk.lpk01 = lpj.lpj01   
                AND (
                    -- 第一個條件：單表查詢不衝突，明確指定 lpj03 
                    lpj.lpj03 = (
                        SELECT a.lpj03 FROM lpj_file a
                        WHERE a.lpj01 = (SELECT b.lpj01 FROM lpj_file b WHERE b.lpj03 = :cardId LIMIT 1) 
                          AND a.lpj09 = '2' AND a.ta_lpj04 = 'Y' LIMIT 1
                    )     
                    -- 第二個條件：修正！子查詢內指定 sub_lpj.lpj03 防止 ambiguous
                    OR lpj.lpj03 = (
                        SELECT sub_lpj.lpj03 
                        FROM lpk_file sub_lpk, lpj_file sub_lpj
                        WHERE sub_lpk.lpk01 = sub_lpj.lpj01 
                          AND substr(sub_lpk.lpk03, 2, 9) = :cardId 
                          AND sub_lpj.lpj09 = '2' AND sub_lpj.ta_lpj04 = 'Y' 
                        LIMIT 1
                    )     
                    -- 第三個條件：修正！子查詢內指定 sub_lpj.lpj03 防止 ambiguous
                    OR lpj.lpj03 = (
                        SELECT sub_lpj.lpj03 
                        FROM lpk_file sub_lpk, lpj_file sub_lpj
                        WHERE sub_lpk.lpk01 = sub_lpj.lpj01 
                          AND sub_lpk.lpk18 = :cardId 
                          AND sub_lpj.lpj09 = '2' AND sub_lpj.ta_lpj04 = 'Y' 
                        LIMIT 1
                    )   
                )
            """, nativeQuery = true)
        List<MemberCustomProjection> findMemberByCardID3Raw(@Param("cardId") String cardId);
    
    
}