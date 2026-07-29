package com.beyond.report.repository;

import com.beyond.report.entity.BUDGET_COUNTER_EXCEPT;
import com.beyond.report.entity.BUDGET_DAY_DETAIL;
import com.beyond.report.entity.BUDGET_DAY_DETAIL_ComposeKey;
import com.beyond.report.entity.BUDGET_DAY_HEADER;
import com.beyond.report.entity.EMAIL_ADDRESS;
import com.beyond.report.entity.LNT_FILE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<BUDGET_DAY_DETAIL, BUDGET_DAY_DETAIL_ComposeKey> {                             

    // ========================================================================
    // 1. EMAIL_ADDRESS 相關 (保留字 `function` 與 `TO`/`CC` 已加上反引號包裹)
    // ========================================================================

    @Query(value = """
            SELECT * FROM PermissionDB.email_address 
            WHERE `function` = :function
            """, nativeQuery = true)
    EMAIL_ADDRESS getBGParam(@Param("function") String function);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE PermissionDB.email_address 
            SET `TO` = :toMail 
            WHERE `function` = :function
            """, nativeQuery = true)
    void updateBudgetApplyTo(@Param("toMail") String toMail, @Param("function") String function);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE PermissionDB.email_address 
            SET `CC` = :ccMail 
            WHERE `function` = :function
            """, nativeQuery = true)
    void updateBudgetApproveCC(@Param("ccMail") String ccMail, @Param("function") String function);

    // ========================================================================
    // 2. BUDGET_DAY_HEADER 相關
    // ========================================================================

    @Query(value = """
            SELECT * FROM BUDGET_DAY_HEADER 
            WHERE b_month = :month
            """, nativeQuery = true)
    BUDGET_DAY_HEADER findHeaderByMonth(@Param("month") String month);

    @Query(value = """
            SELECT * FROM BUDGET_DAY_HEADER 
            ORDER BY b_month DESC
            """, nativeQuery = true)
    List<BUDGET_DAY_HEADER> findAllHeaders();

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BUDGET_DAY_HEADER 
            SET status = :status 
            WHERE b_month = :month
            """, nativeQuery = true)
    void updateStatus(@Param("month") String month, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BUDGET_DAY_HEADER 
            SET status = :status, 
                approve_id = :approveId, 
                approve_date = :approveDate 
            WHERE b_month = :month
            """, nativeQuery = true)
    void approveHeader(
            @Param("month") String month,
            @Param("status") String status,
            @Param("approveId") String approveId,
            @Param("approveDate") Timestamp approveDate
    );

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BUDGET_DAY_HEADER 
            SET status = :status, 
                ReturnReason = :returnReason, 
                reject_id = :rejectId, 
                reject_date = :rejectDate 
            WHERE b_month = :month
            """, nativeQuery = true)
    void rejectHeader(
            @Param("month") String month,
            @Param("status") String status,
            @Param("returnReason") String returnReason,
            @Param("rejectId") String rejectId,
            @Param("rejectDate") Timestamp rejectDate
    );

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BUDGET_DAY_HEADER 
            SET status = :status, 
                access_id = :accessId, 
                access_date = :accessDate 
            WHERE b_month = :month
            """, nativeQuery = true)
    void updateHeaderDraft(
            @Param("month") String month,
            @Param("status") String status,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate
    );

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO BUDGET_DAY_HEADER (b_month) 
            VALUES (:month)
            """, nativeQuery = true)
    void insertHeader(@Param("month") String month);

    // ========================================================================
    // 3. BUDGET_DAY_DETAIL 相關
    // ========================================================================

    @Query(value = """
            SELECT t1.* 
            FROM BUDGET_DAY_DETAIL t1 
            LEFT JOIN BUDGET_COUNTER_EXCEPT t2 ON t1.counter_id = t2.counter_id 
            WHERE t1.b_month = :month 
            AND t2.counter_id IS NULL 
            ORDER BY t1.b_month DESC, t1.counter_id ASC
            """, nativeQuery = true)
    List<BUDGET_DAY_DETAIL> findDetailsByMonth(
            @Param("month") String month
    );

    @Query(value = """
            SELECT COUNT(*) 
            FROM BUDGET_DAY_DETAIL t1 
            LEFT JOIN BUDGET_COUNTER_EXCEPT t2 ON t1.counter_id = t2.counter_id 
            WHERE t1.b_month = :month 
            AND t2.counter_id IS NULL
            """, nativeQuery = true)
    int countDetailsByMonth(@Param("month") String month);

    @Query(value = """
            SELECT * FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND dept_id = :deptId 
            AND counter_id = :counterId
            """, nativeQuery = true)
    BUDGET_DAY_DETAIL findDetailByMonthDeptCounter(
            @Param("month") String month,
            @Param("deptId") String deptId,
            @Param("counterId") String counterId
    );

    @Query(value = """
            SELECT * FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND floor = :floor 
            AND dept_id = :deptId 
            AND counter_id = :counterId
            """, nativeQuery = true)
    BUDGET_DAY_DETAIL findDetailByMonthFloorDeptCounter(
            @Param("month") String month,
            @Param("floor") String floor,
            @Param("deptId") String deptId,
            @Param("counterId") String counterId
    );

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO BUDGET_DAY_DETAIL (
                b_01, b_02, b_03, b_04, b_05,
                b_06, b_07, b_08, b_09, b_10,
                b_11, b_12, b_13, b_14, b_15,
                b_16, b_17, b_18, b_19, b_20,
                b_21, b_22, b_23, b_24, b_25,
                b_26, b_27, b_28, b_29, b_30,
                b_31, b_month, floor, dept_id, dept_name,
                counter_id, counter_name, org_name
            ) VALUES (
                :b01, :b02, :b03, :b04, :b05,
                :b06, :b07, :b08, :b09, :b10,
                :b11, :b12, :b13, :b14, :b15,
                :b16, :b17, :b18, :b19, :b20,
                :b21, :b22, :b23, :b24, :b25,
                :b26, :b27, :b28, :b29, :b30,
                :b31, :month, :floor, :deptId, :deptName,
                :counterId, :counterName, :orgName
            )
            """, nativeQuery = true)
    void insertDetail(
            @Param("b01") String b01,
            @Param("b02") String b02,
            @Param("b03") String b03,
            @Param("b04") String b04,
            @Param("b05") String b05,
            @Param("b06") String b06,
            @Param("b07") String b07,
            @Param("b08") String b08,
            @Param("b09") String b09,
            @Param("b10") String b10,
            @Param("b11") String b11,
            @Param("b12") String b12,
            @Param("b13") String b13,
            @Param("b14") String b14,
            @Param("b15") String b15,
            @Param("b16") String b16,
            @Param("b17") String b17,
            @Param("b18") String b18,
            @Param("b19") String b19,
            @Param("b20") String b20,
            @Param("b21") String b21,
            @Param("b22") String b22,
            @Param("b23") String b23,
            @Param("b24") String b24,
            @Param("b25") String b25,
            @Param("b26") String b26,
            @Param("b27") String b27,
            @Param("b28") String b28,
            @Param("b29") String b29,
            @Param("b30") String b30,
            @Param("b31") String b31,
            @Param("month") String month,
            @Param("floor") String floor,
            @Param("deptId") String deptId,
            @Param("deptName") String deptName,
            @Param("counterId") String counterId,
            @Param("counterName") String counterName,
            @Param("orgName") String orgName
    );

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BUDGET_DAY_DETAIL SET
                b_01 = :b01, b_02 = :b02, b_03 = :b03, b_04 = :b04, b_05 = :b05,
                b_06 = :b06, b_07 = :b07, b_08 = :b08, b_09 = :b09, b_10 = :b10,
                b_11 = :b11, b_12 = :b12, b_13 = :b13, b_14 = :b14, b_15 = :b15,
                b_16 = :b16, b_17 = :b17, b_18 = :b18, b_19 = :b19, b_20 = :b20,
                b_21 = :b21, b_22 = :b22, b_23 = :b23, b_24 = :b24, b_25 = :b25,
                b_26 = :b26, b_27 = :b27, b_28 = :b28, b_29 = :b29, b_30 = :b30,
                b_31 = :b31, org_name = :orgName
            WHERE b_month = :month 
            AND floor = :floor 
            AND dept_id = :deptId 
            AND counter_id = :counterId
            """, nativeQuery = true)
    void updateDetail(
            @Param("b01") String b01,
            @Param("b02") String b02,
            @Param("b03") String b03,
            @Param("b04") String b04,
            @Param("b05") String b05,
            @Param("b06") String b06,
            @Param("b07") String b07,
            @Param("b08") String b08,
            @Param("b09") String b09,
            @Param("b10") String b10,
            @Param("b11") String b11,
            @Param("b12") String b12,
            @Param("b13") String b13,
            @Param("b14") String b14,
            @Param("b15") String b15,
            @Param("b16") String b16,
            @Param("b17") String b17,
            @Param("b18") String b18,
            @Param("b19") String b19,
            @Param("b20") String b20,
            @Param("b21") String b21,
            @Param("b22") String b22,
            @Param("b23") String b23,
            @Param("b24") String b24,
            @Param("b25") String b25,
            @Param("b26") String b26,
            @Param("b27") String b27,
            @Param("b28") String b28,
            @Param("b29") String b29,
            @Param("b30") String b30,
            @Param("b31") String b31,
            @Param("orgName") String orgName,
            @Param("month") String month,
            @Param("floor") String floor,
            @Param("deptId") String deptId,
            @Param("counterId") String counterId
    );

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE BUDGET_DAY_DETAIL 
            SET floor = :floor 
            WHERE b_month = :month 
            AND counter_id = :counterId
            """, nativeQuery = true)
    void updateFloorByCounter(
            @Param("month") String month,
            @Param("floor") String floor,
            @Param("counterId") String counterId
    );

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND dept_id = :deptId 
            AND counter_id = :counterId
            """, nativeQuery = true)
    void deleteDetailByMonthDeptCounter(
            @Param("month") String month,
            @Param("deptId") String deptId,
            @Param("counterId") String counterId
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BUDGET_DAY_DETAIL WHERE b_month = :month", nativeQuery = true)
    void deleteDetailsByMonth(@Param("month") String month);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND counter_id = :counterId
            """, nativeQuery = true)
    void deleteDetailByMonthAndCounter(
            @Param("month") String month,
            @Param("counterId") String counterId
    );

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND (
                b_01 IS NULL OR b_01 = 0 OR
                b_02 IS NULL OR b_02 = 0 OR
                b_03 IS NULL OR b_03 = 0 OR
                b_04 IS NULL OR b_04 = 0 OR
                b_05 IS NULL OR b_05 = 0 OR
                b_06 IS NULL OR b_06 = 0 OR
                b_07 IS NULL OR b_07 = 0 OR
                b_08 IS NULL OR b_08 = 0 OR
                b_09 IS NULL OR b_09 = 0 OR
                b_10 IS NULL OR b_10 = 0 OR
                b_11 IS NULL OR b_11 = 0 OR
                b_12 IS NULL OR b_12 = 0 OR
                b_13 IS NULL OR b_13 = 0 OR
                b_14 IS NULL OR b_14 = 0 OR
                b_15 IS NULL OR b_15 = 0 OR
                b_16 IS NULL OR b_16 = 0 OR
                b_17 IS NULL OR b_17 = 0 OR
                b_18 IS NULL OR b_18 = 0 OR
                b_19 IS NULL OR b_19 = 0 OR
                b_20 IS NULL OR b_20 = 0 OR
                b_21 IS NULL OR b_21 = 0 OR
                b_22 IS NULL OR b_22 = 0 OR
                b_23 IS NULL OR b_23 = 0 OR
                b_24 IS NULL OR b_24 = 0 OR
                b_25 IS NULL OR b_25 = 0 OR
                b_26 IS NULL OR b_26 = 0 OR
                b_27 IS NULL OR b_27 = 0 OR
                b_28 IS NULL OR b_28 = 0 OR
                b_29 IS NULL OR b_29 = 0 OR
                b_30 IS NULL OR b_30 = 0 OR
                b_31 IS NULL OR b_31 = 0
            )
            """, nativeQuery = true)
    void deleteZeroDetails(@Param("month") String month);

    @Query(value = """
            SELECT t1.* 
            FROM BUDGET_DAY_DETAIL t1 
            LEFT JOIN BUDGET_COUNTER_EXCEPT t2 ON t1.counter_id = t2.counter_id 
            WHERE t1.b_month = :month 
            AND t2.counter_id IS NULL
            AND (
                b_01 IS NULL OR b_01 = 0 OR
                b_02 IS NULL OR b_02 = 0 OR
                b_03 IS NULL OR b_03 = 0 OR
                b_04 IS NULL OR b_04 = 0 OR
                b_05 IS NULL OR b_05 = 0 OR
                b_06 IS NULL OR b_06 = 0 OR
                b_07 IS NULL OR b_07 = 0 OR
                b_08 IS NULL OR b_08 = 0 OR
                b_09 IS NULL OR b_09 = 0 OR
                b_10 IS NULL OR b_10 = 0 OR
                b_11 IS NULL OR b_11 = 0 OR
                b_12 IS NULL OR b_12 = 0 OR
                b_13 IS NULL OR b_13 = 0 OR
                b_14 IS NULL OR b_14 = 0 OR
                b_15 IS NULL OR b_15 = 0 OR
                b_16 IS NULL OR b_16 = 0 OR
                b_17 IS NULL OR b_17 = 0 OR
                b_18 IS NULL OR b_18 = 0 OR
                b_19 IS NULL OR b_19 = 0 OR
                b_20 IS NULL OR b_20 = 0 OR
                b_21 IS NULL OR b_21 = 0 OR
                b_22 IS NULL OR b_22 = 0 OR
                b_23 IS NULL OR b_23 = 0 OR
                b_24 IS NULL OR b_24 = 0 OR
                b_25 IS NULL OR b_25 = 0 OR
                b_26 IS NULL OR b_26 = 0 OR
                b_27 IS NULL OR b_27 = 0 OR
                b_28 IS NULL OR b_28 = 0 OR
                b_29 IS NULL OR b_29 = 0 OR
                b_30 IS NULL OR b_30 = 0 OR
                b_31 IS NULL OR b_31 = 0
            )
            """, nativeQuery = true)
    List<BUDGET_DAY_DETAIL> findIncompleteDetails(@Param("month") String month);

    // ========================================================================
    // 4. BUDGET_COUNTER_EXCEPT 相關
    // ========================================================================

    @Query(value = "SELECT * FROM BUDGET_COUNTER_EXCEPT ORDER BY counter_id", nativeQuery = true)
    List<BUDGET_COUNTER_EXCEPT> findAllExceptCounters();

    @Query(value = "SELECT * FROM BUDGET_COUNTER_EXCEPT WHERE counter_id = :counterId", nativeQuery = true)
    BUDGET_COUNTER_EXCEPT findExceptCounterById(@Param("counterId") String counterId);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO BUDGET_COUNTER_EXCEPT (counter_id, counter_name) 
            VALUES (:counterId, :counterName)
            """, nativeQuery = true)
    void insertExceptCounter(
            @Param("counterId") String counterId,
            @Param("counterName") String counterName
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BUDGET_COUNTER_EXCEPT WHERE counter_id = :counterId", nativeQuery = true)
    void deleteExceptCounter(@Param("counterId") String counterId);

    // ========================================================================
    // 5. BUDGET_DAY_APPLY 相關
    // ========================================================================

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM BUDGET_DAY_APPLY 
            WHERE b_month = :month 
            AND apply_id = :applyId
            """, nativeQuery = true)
    void deleteApply(
            @Param("month") String month,
            @Param("applyId") String applyId
    );

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO BUDGET_DAY_APPLY (b_month, apply_id, apply_date) 
            VALUES (:month, :applyId, :applyDate)
            """, nativeQuery = true)
    void insertApply(
            @Param("month") String month,
            @Param("applyId") String applyId,
            @Param("applyDate") Timestamp applyDate
    );

    @Query(value = "SELECT apply_id FROM BUDGET_DAY_APPLY WHERE b_month = :month", nativeQuery = true)
    List<String> findApplyIdsByMonth(@Param("month") String month);

    // ========================================================================
    // 6. LNT_FILE 相關 (Oracle ERP)
    // ========================================================================

    @Query(value = """
            SELECT lntplant, lnt06, lnt09, lnt30, tqa02 
            FROM lnt_file t1, tqa_file t2 
            WHERE t1.lnt30 = t2.tqa01 
            AND lnt26 = 'Y' 
            AND lnt06 = :counterId
            """, nativeQuery = true)
    LNT_FILE findLntFileByCounterId(@Param("counterId") String counterId);

    // ========================================================================
    // 7. 匯入/更新 Counter Data (BUDGET_DAY_DETAIL)
    // ========================================================================

    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND dept_id = :deptId 
            AND counter_id = :counterId
            """, nativeQuery = true)
    boolean existsDetailByMonthDeptCounter(
            @Param("month") String month,
            @Param("deptId") String deptId,
            @Param("counterId") String counterId
    );

    @Query(value = """
            SELECT * FROM BUDGET_DAY_DETAIL 
            WHERE b_month = :month 
            AND org_name IS NOT NULL
            """, nativeQuery = true)
    List<BUDGET_DAY_DETAIL> findDetailsWithOrgName(@Param("month") String month);

    // ========================================================================
    // 8. 統計方法
    // ========================================================================

    @Query(value = "SELECT COUNT(*) FROM BUDGET_DAY_DETAIL WHERE b_month = :month", nativeQuery = true)
    long countDetailsByMonthAll(@Param("month") String month);
}