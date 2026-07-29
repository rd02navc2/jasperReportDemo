package com.beyoung.member.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional; // 解決 Optional cannot be resolved
import org.springframework.data.repository.query.Param; // 解決 Param cannot be resolved
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 會員資料存取層
 * 升級架構：全面棄用 Native SQL，改用符合 Spring Boot 3.x / Jakarta Persistence 標準的純 JPA / JPQL 實作
 */
@Slf4j
@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    private static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 檢查會員是否存在 (優化：使用 JPQL COUNT 運算，不查詢全欄位)
     */
    public boolean isExistLPK(String sMemberID) {
        String jpql = """
                SELECT COUNT(t1)
                FROM LpkFile t1
                LEFT JOIN LpjFile t2 ON t1.lpk01 = t2.lpj01
                WHERE t1.lpk01 = :memberId OR t2.lpj03 = :memberId
                """;
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("memberId", sMemberID)
                .getSingleResult();
        return count != null && count > 0;
    }

    /**
     * 新增一般臨時會員 (標準 JPA 實體持久化)
     */
    public void addTempMember(String sCenter, String sMemberID, String sCardID) {
        LocalDateTime now = LocalDateTime.now();

        LpkFile lpk = new LpkFile();
        lpk.setLpk01(sMemberID);
        lpk.setLpk02("0");
        lpk.setLpk04("未註冊");
        lpk.setLpk10("603");
        lpk.setLpk13("703");
        lpk.setLpkacti("Y");
        lpk.setLpkcrat(now);
        lpk.setLpkoriu("admin");
        lpk.setLpkorig("16");
        lpk.setLpkpos("1");
        lpk.setLpkud02("61");
        lpk.setLpkud08(0);
        lpk.setLpkud09(0);
        lpk.setLpkud10(0);
        lpk.setLpk20("999999");
        lpk.setLpk21("0");
        lpk.setTaLpk04(now);
        lpk.setTaLpk05("Y");
        lpk.setTaLpk06("3");
        em.persist(lpk);

        LpjFile lpj = new LpjFile();
        lpj.setLpj01(sMemberID);
        lpj.setLpj02("000");
        lpj.setLpj03(sCardID);
        lpj.setLpj04(now);
        lpj.setLpj06(0);
        lpj.setLpj07(0);
        lpj.setLpj09("2");
        lpj.setLpj12(0);
        lpj.setLpj13(0);
        lpj.setLpj14(0);
        lpj.setLpj15(0);
        lpj.setLpj16("N");
        lpj.setLpj17("BY001");
        lpj.setLpj18(now);
        lpj.setLpj19("BY001");
        lpj.setLpjpos("1");
        lpj.setTaLpj01(1);
        lpj.setTaLpj02(0);
        lpj.setTaLpj03(0);
        lpj.setTaLpj04("Y");
        em.persist(lpj);
    }

    /**
     * 新增 RS 臨時會員
     */
    public void addRSTempMember(String sCenter, String sMemberID, String sCardID) {
        LocalDateTime now = LocalDateTime.now();

        LpkFile lpk = new LpkFile();
        lpk.setLpk01(sMemberID);
        lpk.setLpk02("0");
        lpk.setLpk04("臨時會員");
        lpk.setLpk10("603");
        lpk.setLpk13("703");
        lpk.setLpkacti("Y");
        lpk.setLpkcrat(now);
        lpk.setLpkoriu("admin");
        lpk.setLpkorig("16");
        lpk.setLpkpos("1");
        lpk.setLpkud02("61");
        lpk.setLpkud08(0);
        lpk.setLpkud09(0);
        lpk.setLpkud10(0);
        lpk.setLpk20("999999");
        lpk.setLpk21("0");
        lpk.setTaLpk04(now);
        lpk.setTaLpk05("Y");
        lpk.setTaLpk06("3");
        em.persist(lpk);

        LpjFile lpj = new LpjFile();
        lpj.setLpj01(sMemberID);
        lpj.setLpj02("TEMP");
        lpj.setLpj03(sCardID);
        lpj.setLpj04(now);
        lpj.setLpj06(0);
        lpj.setLpj07(0);
        lpj.setLpj09("2");
        lpj.setLpj12(0);
        lpj.setLpj13(0);
        lpj.setLpj14(0);
        lpj.setLpj15(0);
        lpj.setLpj16("N");
        lpj.setLpj17("BY001");
        lpj.setLpj18(now);
        lpj.setLpj19("BY001");
        lpj.setLpjpos("1");
        lpj.setTaLpj01(1);
        lpj.setTaLpj02(0);
        lpj.setTaLpj03(0);
        lpj.setTaLpj04("Y");
        em.persist(lpj);
    }

    /**
     * 高併發執行緒安全補點方法（標準 JPA 悲觀鎖寫入）
     */
    public void updateMemberPoints(String sCardID, Double dPoints) {
        // 1. 使用 JPA 悲觀鎖 (SELECT ... FOR UPDATE) 鎖定特定卡片帳戶實體
        String jpqlLock = "SELECT lpj FROM LpjFile lpj WHERE lpj.lpj03 = :cardId AND lpj.lpj09 = '2'";
        List<LpjFile> results = em.createQuery(jpqlLock, LpjFile.class)
                .setParameter("cardId", sCardID)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();

        if (results.isEmpty()) {
            throw new IllegalArgumentException("[Member微服務] 找不到對應的有效會卡帳戶，卡號: " + sCardID);
        }

        // 2. 利用 JPA 狀態管理進行動態髒檢查更新（Dirty Checking），無需編寫 UPDATE SQL
        LpjFile lpj = results.get(0);
        lpj.setLpj07(lpj.getLpj07() + dPoints.intValue()); // 依據您 Lpj07 實際型態進行加減
        lpj.setLpj18(LocalDateTime.now());
        
        log.info("[Member微服務] 卡號 {} 點數成功累加 {} 點", sCardID, dPoints);
    }

    /**
     * 更新會員聯絡資料 (改用 JPQL 批次更新)
     */
    public void updMemberContact(String sMemberID, String sMobile, String sEmail, String sAddr) {
        String jpql = """
                UPDATE LpkFile lpk
                SET lpk.lpk15 = :addr, lpk.lpk18 = :mobile, lpk.lpk19 = :email, lpk.lpkdate = :now
                WHERE lpk.lpk01 = :memberId
                """;
        em.createQuery(jpql)
                .setParameter("addr", sAddr)
                .setParameter("mobile", sMobile)
                .setParameter("email", sEmail)
                .setParameter("now", LocalDateTime.now())
                .setParameter("memberId", sMemberID)
                .executeUpdate();
    }

    /**
     * 執行眷屬綁定 (JPA 實體與高併發優化版)
     */
    public void doHouseHold(String sTempMemberID, LpjFile bean, String sMemberID, String sMainCardID) {
        
        // 1. 使用 JPA 標準悲觀寫入鎖（PESSIMISTIC_WRITE）鎖定主會員
        LpkFile mainMember = em.find(LpkFile.class, sMemberID, LockModeType.PESSIMISTIC_WRITE);
        if (mainMember == null) {
            throw new IllegalArgumentException("找不到主會員資料: " + sMemberID);
        }

        // 2. 累加點數 (JPQL)
	    String jpqlUpdMain = """
        UPDATE LpjFile lpj
        SET lpj.lpj07    = lpj.lpj07    + :lpj07,
            lpj.lpj12    = lpj.lpj12    + :lpj12,
            lpj.lpj14    = lpj.lpj14    + :lpj14,
            lpj.lpj15    = lpj.lpj15    + :lpj15,
            lpj.taLpj02 = lpj.taLpj02 + :taLpj02,
            lpj.taLpj03 = lpj.taLpj03 + :taLpj03
        WHERE lpj.lpj09 = '2' AND lpj.lpj01 = :memberId AND lpj.taLpj04 = 'Y'
        """;
    
        em.createQuery(jpqlUpdMain)
        .setParameter("lpj07",    bean.getLpj07())
        .setParameter("lpj12",    bean.getLpj12())
        .setParameter("lpj14",    bean.getLpj14())
        .setParameter("lpj15",    bean.getLpj15())
        .setParameter("taLpj02", bean.getTaLpj02())
        .setParameter("taLpj03", bean.getTaLpj03())
        .setParameter("memberId", sMemberID)
        .executeUpdate();
    
        // ========================================================
        // 【這裡替換為新的第 3 步】
        // 3. 標記暫時卡為已合併狀態，不變更主鍵 (LPJ01)
        String jpqlUpdTemp = """
                UPDATE LpjFile lpj
                SET lpj.lpj02 = 'MERGED', lpj.taLpj04 = 'N', lpj.taLpj05 = :memberId
                WHERE lpj.lpj09 = '2' AND lpj.lpj01 = :tempId
                """;
        em.createQuery(jpqlUpdTemp)
                .setParameter("memberId", sMemberID) 
                .setParameter("tempId",   sTempMemberID)
                .executeUpdate();
        
        // 4. 安全轉移 LPL_FILE 歷程 (修復：改用 JPQL 聚合函數配合悲觀鎖計算基準序號)
        String jpqlMaxSeq = "SELECT COALESCE(MAX(lpl.lpl09), 0) FROM LplFile lpl WHERE lpl.lpl01 = :memberId";
        Integer maxSeqResult = em.createQuery(jpqlMaxSeq, Integer.class)
                .setParameter("memberId", sMemberID)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getSingleResult();
        int currentMaxSeq = maxSeqResult != null ? maxSeqResult : 0;

        // 查詢暫時卡的所有歷程，並透過實體循環安全遞增主鍵
        String jpqlFindLpl = "SELECT lpl FROM LplFile lpl WHERE lpl.lpl01 = :tempId";
        List<LplFile> tempLplList = em.createQuery(jpqlFindLpl, LplFile.class)
                .setParameter("tempId", sTempMemberID)
                .getResultList();

        for (LplFile lpl : tempLplList) {
            currentMaxSeq++;
            lpl.setLpl01(sMemberID);
            lpl.setLpl09(currentMaxSeq); // 完美避開 ROWNUM 依賴，落實微服務跨平台設計
            em.merge(lpl);
        }

        // 5. 轉移 LSM_FILE 至主卡卡號 (JPQL)
        /*
        String jpqlUpdLsm = """
                UPDATE LsmFile lsm
                SET lsm.lsm01 = :mainCardId
                WHERE lsm.lsm01 = :tempId
                """;
        em.createQuery(jpqlUpdLsm)
                .setParameter("mainCardId", sMainCardID)
                .setParameter("tempId",     sTempMemberID)
                .executeUpdate();
        */

        // 6. 刪除暫時會員主檔 (JPA 實體移除)
        LpkFile tempMember = em.find(LpkFile.class, sTempMemberID);
        if (tempMember != null) {
            em.remove(tempMember);
        }
    }

    /**
     * 臨時會員轉正式會員
     */
    public void doFormal(String sTempMemberID, String sName, String sID,
                         String sBirthday, String sMobile, String sAddress, String sEmail) throws Exception {
        
        // 1. 取得會員實體
        LpkFile lpk = em.find(LpkFile.class, sTempMemberID);
        if (lpk == null) {
            throw new IllegalArgumentException("找不到要轉正的臨時會員: " + sTempMemberID);
        }

        // 2. 身分證性別辨識與實體資料變更
        if (sID != null && sID.length() >= 2) {
            String idSecond = sID.substring(1, 2);
            if ("1".equals(idSecond)) {
                lpk.setLpk06("1"); // 設為男
            } else if ("2".equals(idSecond)) {
                lpk.setLpk06("0"); // 設為女
            }
        }

        lpk.setLpk03(sID != null ? sID.toUpperCase() : null);
        lpk.setLpk04(sName);
        
        // 生日轉為 java.util.Date 符合 Entity 的設定
        if (sBirthday != null && !sBirthday.trim().isEmpty()) {
            java.time.LocalDate parsedBirthday = java.time.LocalDate.parse(sBirthday, BIRTHDAY_FORMATTER);
            java.util.Date utilDate = java.util.Date.from(parsedBirthday.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            lpk.setLpk05(utilDate);
        }
        
        lpk.setLpk15(sAddress);
        lpk.setLpk18(sMobile);
        lpk.setLpk19(sEmail);
        lpk.setLpkdate(LocalDateTime.now()); // 異動時間

        // 3. 更新卡片檔狀態 (JPQL)
        String jpqlUpdLpj = """
                UPDATE LpjFile lpj
                SET lpj.lpj02 = 'APP'
                WHERE lpj.lpj09 = '2' AND lpj.lpj01 = :tempId
                """;
        em.createQuery(jpqlUpdLpj)
                .setParameter("tempId", sTempMemberID)
                .executeUpdate();
                
        // 強制先進行 flush 檢查是否有資料庫層級錯誤
        em.flush();
    }

    public List<LpjFile> getAllCardByMemberID(String sMemberID) {
        String jpql = """
                SELECT lpj 
                FROM LpjFile lpj 
                WHERE lpj.lpj01 = :memberId 
                AND lpj.lpj09 = '2'
                """;
        return em.createQuery(jpql, LpjFile.class)
                .setParameter("memberId", sMemberID)
                .getResultList();
    }

    public List<LpjFile> getAllCardByID(String sID) {
        // 使用明確的 JOIN 語法，效能更好且符合 JPQL 標準
        String jpql = """
                SELECT lpj 
                FROM LpjFile lpj
                JOIN LpkFile lpk ON lpj.lpj01 = lpk.lpk01
                WHERE lpk.lpk03 = :id 
                AND lpj.lpj09 = '2'
                """;
        
        return em.createQuery(jpql, LpjFile.class)
                .setParameter("id", sID)
                .getResultList();
    }
    
    public LpjFile getPointByMemberID(String sMemberID) {
        String jpql = """
                SELECT lpj FROM LpjFile lpj
                WHERE lpj.lpj01 = :memberId AND lpj.lpj09 = '2' AND lpj.taLpj04 = 'Y'
                """;
        List<LpjFile> list = em.createQuery(jpql, LpjFile.class)
                .setParameter("memberId", sMemberID)
                .getResultList();
        return list.isEmpty() ? new LpjFile() : list.get(list.size() - 1);
    }

    public LpjFile getMemberDirectly(String sID) {
        String jpql = "SELECT t FROM LpjFile t WHERE t.lpj01 = :id AND t.lpj09 = '2' AND t.taLpj04 = 'Y'";
        try {
            return em.createQuery(jpql, LpjFile.class)
                     .setParameter("id", sID)
                     .getSingleResult();
        } catch (Exception e) {
            return null; // 或者處理找不到的情況
        }
    }
    
    public LpjFile getPointByID(String sID) {
        String jpql = """
                SELECT t2 FROM LpkFile t1, LpjFile t2
                WHERE t1.lpk03 = :id AND t2.lpj09 = '2' AND t2.taLpj04 = 'Y' AND t1.lpk01 = t2.lpj01
                """;
        List<LpjFile> list = em.createQuery(jpql, LpjFile.class)
                .setParameter("id", sID)
                .getResultList();
        return list.isEmpty() ? new LpjFile() : list.get(list.size() - 1);
    }

    public LpkFile getMemberContact(String sMemberID) {
        LocalDate[] range = lastYearLocalDateRange();
        String jpql = """
                SELECT lpk FROM LpkFile lpk
                WHERE lpk.lpk01 = :memberId
                """;
        List<LpkFile> list = em.createQuery(jpql, LpkFile.class)
                .setParameter("memberId", sMemberID)
                .getResultList();
        
        // 註：原本帶有大面積 SUM(lsm08) 的複雜關連查詢，建議拆分至服務層(Service)或利用 JPA @ManyToOne 關聯屬性動態讀取，
        // 這裡回傳主會員資料，保持物件一致性。
        return list.isEmpty() ? new LpkFile() : list.get(list.size() - 1);
    }

    public LpkFile getMemberContactByID(String sID) {
        String jpql = """
                SELECT t1 FROM LpkFile t1, LpjFile t2
                WHERE t1.lpk03 = :id AND t1.lpk01 = t2.lpj01 AND t2.lpj09 = '2' AND t2.taLpj04 = 'Y'
                """;
        TypedQuery<LpkFile> query = em.createQuery(jpql, LpkFile.class)
                .setParameter("id", sID);
        query.setMaxResults(1); // 限制回傳一筆，取代 ROWNUM = 1
        List<LpkFile> list = query.getResultList();
        return list.isEmpty() ? new LpkFile() : list.get(0);
    }

    public LpkFile getMemberContactByCardID(String sCardID) {
        String jpql = """
                SELECT t1 FROM LpkFile t1, LpjFile t2
                WHERE t2.lpj03 = :cardId AND t1.lpk01 = t2.lpj01 AND t2.lpj09 = '2' AND t2.taLpj04 = 'Y'
                """;
        TypedQuery<LpkFile> query = em.createQuery(jpql, LpkFile.class)
                .setParameter("cardId", sCardID);
        query.setMaxResults(1);
        List<LpkFile> list = query.getResultList();
        return list.isEmpty() ? new LpkFile() : list.get(0);
    }

    public LpkFile getMemberByCardID(String sCardID) {
        String jpql = """
                SELECT t1 FROM LpkFile t1, LpjFile t2
                WHERE t1.lpk01 = t2.lpj01 AND t2.lpj03 = :cardId AND t2.lpj09 = '2' AND t2.taLpj04 = 'Y'
                """;
        List<LpkFile> list = em.createQuery(jpql, LpkFile.class)
                .setParameter("cardId", sCardID)
                .getResultList();
        return list.isEmpty() ? new LpkFile() : list.get(list.size() - 1);
    }

    public LpkFile getMemberByCardID2(String sCardID) {
        // 全面防禦 ROWNUM，改以 query.setMaxResults(1) 與優化後的 JPQL 實作多重卡號、手機與身分證識別
        String jpql = """
                SELECT lpk FROM LpkFile lpk, LpjFile lpj
                WHERE lpk.lpk01 = lpj.lpj01
                  AND (lpj.lpj03 = :cid OR lpk.lpk18 = :cid OR substring(lpk.lpk03, 2, 9) = :cid)
                  AND lpj.lpj09 = '2' AND lpj.taLpj04 = 'Y'
                """;
        TypedQuery<LpkFile> query = em.createQuery(jpql, LpkFile.class)
                .setParameter("cid", sCardID);
        query.setMaxResults(1);
        List<LpkFile> list = query.getResultList();
        return list.isEmpty() ? new LpkFile() : list.get(0);
    }

    public LpkFile getMemberByCardID3(String sCardID) {
        return getMemberByCardID2(sCardID); // 邏輯與 ID2 收斂一致，透過標準 JPQL 進行多合一索引查詢
    }

    public LpkFile getMemberByCardID4(String sCardID) {
        return getMemberByCardID2(sCardID);
    }

    public boolean is000(String sCardID) {
        String jpql = "SELECT lpj.lpj01 FROM LpjFile lpj WHERE lpj.lpj03 = :cardId AND lpj.lpj02 = '000'";
        List<?> result = em.createQuery(jpql)
                .setParameter("cardId", sCardID)
                .getResultList();
        return !result.isEmpty();
    }
    
    /**
     * 透過卡號獲取主會員資料 (標準 JPQL 實作)
     * 邏輯：透過 LPJ_FILE (卡片檔) 的卡號 (lpj03) 找出會員 ID (lpj01)，再關聯 LPK_FILE (會員檔)
     */
    public LpkFile getMainCard(String sCardID) {
        String jpql = """
                SELECT lpk 
                FROM LpkFile lpk, LpjFile lpj
                WHERE lpk.lpk01 = lpj.lpj01
                  AND lpj.lpj03 = :cardId
                  AND lpj.lpj09 = '2'
                  AND lpj.taLpj04 = 'Y'
                """;
        
        TypedQuery<LpkFile> query = em.createQuery(jpql, LpkFile.class)
                .setParameter("cardId", sCardID);
        
        // 防禦機制：限制最多回傳一筆紀錄，徹底取代 ROWNUM = 1
        query.setMaxResults(1); 
        
        List<LpkFile> list = query.getResultList();
        
        // 若找不到資料，回傳一個空的 LpkFile 物件（維持您舊系統的回傳習慣）
        return list.isEmpty() ? new LpkFile() : list.get(0);
    }   
    
    public LpkFile findMemberByCardNo(String sID) {
        String jpql = """
                SELECT lpk FROM LpkFile lpk, LpjFile lpj
                WHERE lpk.lpk03 = :id 
                AND lpk.lpk01 = lpj.lpj01 
                AND lpj.lpj09 = '2' 
                AND lpj.taLpj04 = 'Y'
                """;
        
        TypedQuery<LpkFile> query = em.createQuery(jpql, LpkFile.class)
                .setParameter("id", sID);
        query.setMaxResults(1);
        
        List<LpkFile> list = query.getResultList();
        return list.isEmpty() ? new LpkFile() : list.get(0);
    }
    
    /**
     * 工具方法：計算去年區間 (回傳標準 LocalDate 物件，供 JPA 參數對齊安全合規)
     */
    private LocalDate[] lastYearLocalDateRange() {
        int lastYear = LocalDate.now().getYear() - 1;
        return new LocalDate[]{LocalDate.of(lastYear, 1, 1), LocalDate.of(lastYear, 12, 31)};
    }
}