package com.beyond.surrounding.invoice.repository;

import com.beyond.surrounding.app.entity.TC_PSA_FILE;
import com.beyond.surrounding.app.entity.TC_PSA_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<TC_PSA_FILE, TC_PSA_FILE_ComposeKey> {

    /**
     * 1. 讀取使用者發票紀錄（改為純 Native SQL，對應真實資料表名稱）
     */
    @Query(value = """
            SELECT t.* FROM tc_psa_file t
            WHERE t.tc_psa13 IN (
                SELECT l.lpj03 FROM lpj_file l WHERE l.lpj01 = :memberID
            )
            AND t.tc_psa04 = :date
            AND t.tc_psaplant = 'BY001'
            ORDER BY t.tc_psa05
            """, nativeQuery = true)
    List<TC_PSA_FILE> getUserInvoice(
            @Param("memberID") String sMemberID, 
            @Param("date") Date sDate
    );
    
    /**
     * 隨機碼為 "uncheck" 時，只比對發票號碼與隨機碼串接 (修正為 nativeQuery = true)
     */
    @Query(value = """
            SELECT * FROM tc_psa_file 
            WHERE CONCAT(tc_psa16, tc_psa17) = :invoiceNo
            """, nativeQuery = true)
    List<TC_PSA_FILE> validateInvoiceUncheck(@Param("invoiceNo") String sInvoiceNo);

    /**
     * 一般情境下，比對發票隨機碼串接，以及消費總金額 (修正為 nativeQuery = true)
     */
    @Query(value = """
            SELECT * FROM tc_psa_file
            WHERE CONCAT(tc_psa16, tc_psa17) = :invoiceNo 
              AND tc_psa31 = :totalPrice
            """, nativeQuery = true)
    List<TC_PSA_FILE> validateInvoiceCheck(@Param("invoiceNo") String sInvoiceNo, @Param("totalPrice") Double dTotalPrice);

    /**
     * 3. 檢查 ERP (lsm_file) 是否已經使用過此發票 (修正為 nativeQuery = true)
     */
    @Query(value = """
            SELECT COUNT(*) FROM lsm_file 
            WHERE lsm02 IN ('2', '7') 
              AND ta_lsm09 = :invoiceNo
            """, nativeQuery = true)
    long countLsmFileUsed(@Param("invoiceNo") String sInvoiceNo);
    
    /**
     * 4. 對應原 TheaterAppend 邏輯
     */
    @Query(value = """
            SELECT * FROM THEATER_APPEND_TABLE 
            WHERE MEMBER_ID = :memberID 
              AND CARD_ID = :cardID 
              AND INVOICE_NO = :invoiceNo
            """, nativeQuery = true)
    Optional<Map<String, Object>> theaterAppend(
            @Param("memberID") String memberID, 
            @Param("cardID") String cardID, 
            @Param("invoiceNo") String invoiceNo, 
            @Param("totalPrice") String totalPrice, 
            @Param("invoiceDate") String invoiceDate, 
            @Param("pointType") String pointType
    );

    /**
     * 5. 查詢已使用發票
     */
    @Query(value = """
            SELECT * FROM INVOICE_USED_TABLE 
            WHERE INVOICE_NO = :invoiceNo
            """, nativeQuery = true)
    List<Map<String, Object>> getInvoiceUsed(@Param("invoiceNo") String invoiceNo);

    /**
     * 6. 檢查是否存在補登退貨記錄
     */
    @Query(value = """
            SELECT * FROM APPEND_RETURN_TABLE 
            WHERE MEMBER_ID = :memberID 
              AND INVOICE_NO = :invoiceNo
            """, nativeQuery = true)
    Optional<Map<String, Object>> findAppendReturnRecord(
            @Param("memberID") String memberID, 
            @Param("invoiceNo") String invoiceNo
    );
    
    /**
     * 6. 退貨補登：刪除補登記錄 (修正 Param 與 SQL 變數命名不一致問題)
     */
    @Modifying
    @Query(value = """
            DELETE FROM LSM_FILE 
            WHERE ta_lsm09 = :invoiceNo 
              AND lsm02 = '2'
            """, nativeQuery = true)
    int deleteLsmRecord(@Param("invoiceNo") String invoiceNo);

    /**
     * 7. 退貨補登：更新會員點數 (LPJ_FILE)
     */
    @Modifying
    @Query(value = """
            UPDATE LPJ_FILE 
            SET lpj07 = lpj07 - 1, 
                lpj08 = :date, 
                lpj12 = lpj12 - :point, 
                lpj14 = lpj14 - :point, 
                ta_lpj03 = ta_lpj03 - :point 
            WHERE lpj01 = :memberID 
              AND lpj09 = '2' 
              AND ta_lpj04 = 'Y'
            """, nativeQuery = true)
    int updateMemberPoints(@Param("memberID") String memberID, 
                           @Param("date") Date date, 
                           @Param("point") double point);

    /**
     * 7. 新增臨時會員邏輯
     */
    @Modifying
    @Query(value = """
            INSERT INTO LSM_FILE (CENTER_ID, MEMBER_ID, CREATE_TIME) 
            VALUES (:center, :memberID, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void addTempMember(@Param("center") String center, @Param("memberID") String memberID);

    /**
     * 補齊：發票防偽與重複性校驗
     */
    @Query(value = """
            SELECT 
                j.tc_psa01 as "TC_PSA01", 
                j.tc_psa04 as "TC_PSA04", 
                j.tc_psa13 as "TC_PSA13", 
                j.tc_psa06 as "TC_PSA06"
            FROM tc_psa_file j
            WHERE CONCAT(j.tc_psa16, j.tc_psa17) = :invoiceNo
              AND (:randomNo = 'uncheck' OR CAST(j.tc_psa31 AS CHAR) = :randomNo)
            """, nativeQuery = true)
    List<Map<String, Object>> validateInvoice(
            @Param("invoiceNo") String invoiceNo, 
            @Param("randomNo") String randomNo
    );
    
    /**
     * 8. 查詢會員最新點數資訊
     */
    @Query(value = """
            SELECT 
                j.lpj03,    
                k.lpk04, 
                j.lpj12, 
                j.ta_lpj01, 
                j.ta_lpj02, 
                j.ta_lpj03 
            FROM lpj_file j
            INNER JOIN lpk_file k ON k.lpk01 = j.lpj01
            WHERE j.lpj01 = (SELECT lpj01 FROM lpj_file WHERE lpj03 = :memberId)
              AND j.lpj09 = '2' 
              AND j.ta_lpj04 = 'Y'
            """, nativeQuery = true)
    Map<String, Object> findMemberPoints(@Param("memberId") String memberId);

    /**
     * 補齊：發票補登/綁定作業 - 取得專櫃消費金額與點數基底
     */
    @Query(value = """
            SELECT 
                j.tc_psaplant AS "TC_PSAPLANT", 
                j.tc_psa01    AS "TC_PSA01", 
                j.tc_psa02    AS "TC_PSA02", 
                j.tc_psa03    AS "TC_PSA03", 
                j.tc_psa04    AS "TC_PSA04", 
                j.tc_psa05    AS "TC_PSA05", 
                j.tc_psa12    AS "TC_PSA12", 
                j.tc_psa16    AS "TC_PSA16", 
                j.tc_psa17    AS "TC_PSA17", 
                j.tc_psa18    AS "TC_PSA18", 
                j.tc_psa40    AS "TC_PSA40", 
                j.tc_psalegal AS "TC_PSALEGAL", 
                l.lnt04       AS "LNT04_EXTRA" 
            FROM tc_psa_file j
            INNER JOIN lnt_file l ON j.tc_psa01 = l.lnt06
            WHERE j.tc_psa04 BETWEEN l.lnt17 AND l.lnt18
              AND CONCAT(j.tc_psa16, j.tc_psa17) = :sInvoiceNo
              AND (:sRandomNo = 'uncheck' OR CAST(j.tc_psa31 AS CHAR) = :sRandomNo)
            """, nativeQuery = true)
    List<Map<String, Object>> appendInvoice(
            @Param("sInvoiceNo") String sInvoiceNo, 
            @Param("sRandomNo") String sRandomNo
    );
 
    /**
     * Step 1-A：sRandomNo = "uncheck" 時（不帶 tc_psa31 條件）
     */
    @Query(value = """
            SELECT p.*
            FROM tc_psa_file p
            INNER JOIN lnt_file l ON p.tc_psa01 = l.lnt06
            WHERE p.tc_psa04 BETWEEN l.lnt17 AND l.lnt18
              AND CONCAT(p.tc_psa16, p.tc_psa17) = :invoiceNo
            """, nativeQuery = true)
    List<TC_PSA_FILE> findInvoiceUncheck(@Param("invoiceNo") String invoiceNo);
 
    /**
     * Step 1-B：sRandomNo 有值時（帶 tc_psa31 條件）
     */
    @Query(value = """
            SELECT p.*
            FROM tc_psa_file p
            INNER JOIN lnt_file l ON p.tc_psa01 = l.lnt06
            WHERE p.tc_psa04 BETWEEN l.lnt17 AND l.lnt18
              AND CONCAT(p.tc_psa16, p.tc_psa17) = :invoiceNo
              AND p.tc_psa31 = CAST(:randomNo AS DOUBLE)
            """, nativeQuery = true)
    List<TC_PSA_FILE> findInvoiceWithRandom(
            @Param("invoiceNo") String invoiceNo,
            @Param("randomNo") String randomNo);
 
    /**
     * 統一入口：Service 呼叫此方法
     */
    default List<TC_PSA_FILE> findInvoice(String invoiceNo, String randomNo) {
        if ("uncheck".equals(randomNo)) {
            return findInvoiceUncheck(invoiceNo);
        }
        return findInvoiceWithRandom(invoiceNo, randomNo);
    }
 
    /**
     * Step 10-A：更新 TC_PSA13（不帶 tc_psa31 條件，對應 uncheck）
     */
    @Modifying
    @Query(value = """
            UPDATE tc_psa_file
            SET tc_psa13 = :memberId
            WHERE tc_psa04 = :invoiceDate
              AND tc_psaplant = :plant
              AND CONCAT(tc_psa16, tc_psa17) = :invoiceNo
            """, nativeQuery = true)
    void updatePsa13Uncheck(
            @Param("memberId")    String memberId,
            @Param("invoiceDate") Date   invoiceDate,
            @Param("plant")       String plant,
            @Param("invoiceNo")   String invoiceNo);
 
    /**
     * Step 10-B：更新 TC_PSA13（帶 tc_psa31 條件）
     */
    @Modifying
    @Query(value = """
            UPDATE tc_psa_file
            SET tc_psa13 = :memberId
            WHERE tc_psa04 = :invoiceDate
              AND tc_psaplant = :plant
              AND CONCAT(tc_psa16, tc_psa17) = :invoiceNo
              AND CAST(tc_psa31 AS CHAR) = :randomNo
            """, nativeQuery = true)
    void updatePsa13WithRandom(
            @Param("memberId")    String memberId,
            @Param("invoiceDate") Date   invoiceDate,
            @Param("plant")       String plant,
            @Param("invoiceNo")   String invoiceNo,
            @Param("randomNo")    String randomNo);
 
    /**
     * 統一入口
     */
    default void updatePsa13(String memberId, Date invoiceDate, String plant,
                             String invoiceNo, String randomNo) {
        if (randomNo == null || "uncheck".equals(randomNo)) {
            updatePsa13Uncheck(memberId, invoiceDate, plant, invoiceNo);
        } else {
            updatePsa13WithRandom(memberId, invoiceDate, plant, invoiceNo, randomNo);
        }
    }

    /**
     * 批量查詢已使用發票明細
     */
    @Query(value = """
            SELECT * FROM INVOICE_USED_TABLE 
            WHERE INVOICE_NO IN (:invoiceNos)
            """, nativeQuery = true)
    List<Map<String, Object>> findInvoiceUsedDetails(@Param("invoiceNos") List<String> invoiceNos);
    
}