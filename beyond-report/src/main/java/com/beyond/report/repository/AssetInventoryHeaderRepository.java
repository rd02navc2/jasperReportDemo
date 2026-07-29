package com.beyond.report.repository;

import com.beyond.report.entity.ASSET_INVENTORY_HEADER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AssetInventoryHeaderRepository extends JpaRepository<ASSET_INVENTORY_HEADER, String> {

    // ==================== 1. 查詢方法 ====================

    /**
     * 取得盤點日期清單 (按盤點日期降冪排序)
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            ORDER BY inventory_date DESC
            """, nativeQuery = true)
    List<ASSET_INVENTORY_HEADER> findAllOrderByInventoryDateDesc();

    /**
     * 依盤點日期查詢 Header
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    ASSET_INVENTORY_HEADER findByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 查詢最新的盤點 Header (包含未結案和已結案)
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER
            )
            """, nativeQuery = true)
    ASSET_INVENTORY_HEADER findLatestInventoryHeader();

    /**
     * 查詢最新的盤點狀態 (包含 close_date)
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER
            )
            """, nativeQuery = true)
    ASSET_INVENTORY_HEADER findLatestInventoryStatus();

    /**
     * 查詢最新的已結案盤點日期
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NOT NULL 
            ORDER BY inventory_date DESC 
            LIMIT 1
            """, nativeQuery = true)
    ASSET_INVENTORY_HEADER findLatestClosedInventory();

    /**
     * 查詢進行中的盤點 (未結案)
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NULL 
            ORDER BY inventory_date DESC
            """, nativeQuery = true)
    List<ASSET_INVENTORY_HEADER> findActiveInventories();

    /**
     * 查詢是否有進行中的盤點
     */
    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NULL
            """, nativeQuery = true)
    boolean hasActiveInventory();

    /**
     * 查詢特定日期範圍的盤點
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date BETWEEN :startDate AND :endDate 
            ORDER BY inventory_date DESC
            """, nativeQuery = true)
    List<ASSET_INVENTORY_HEADER> findByInventoryDateBetween(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    /**
     * 查詢特定產生類型的盤點
     */
    @Query(value = """
            SELECT * 
            FROM ASSET_INVENTORY_HEADER 
            WHERE generate_type = :generateType 
            ORDER BY inventory_date DESC
            """, nativeQuery = true)
    List<ASSET_INVENTORY_HEADER> findByGenerateType(@Param("generateType") String generateType);

    /**
     * 統計盤點總數
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM ASSET_INVENTORY_HEADER
            """, nativeQuery = true)
    long countAllInventories();

    /**
     * 統計已結案的盤點總數
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NOT NULL
            """, nativeQuery = true)
    long countClosedInventories();

    /**
     * 統計未結案的盤點總數
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NULL
            """, nativeQuery = true)
    long countActiveInventories();

    // ==================== 2. 新增/更新方法 ====================

    /**
     * 新增盤點 Header
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_INVENTORY_HEADER (
                inventory_date, generate_type, close_date, access_id, access_date, remark
            ) VALUES (
                :inventoryDate, :generateType, :closeDate, :accessId, :accessDate, :remark
            )
            """, nativeQuery = true)
    void insertHeader(
            @Param("inventoryDate") String inventoryDate,
            @Param("generateType") String generateType,
            @Param("closeDate") Timestamp closeDate,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate,
            @Param("remark") String remark
    );

    /**
     * 更新 Header 備註
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY_HEADER 
            SET remark = :remark, 
                access_id = :accessId, 
                access_date = :accessDate 
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    void updateHeaderRemark(
            @Param("remark") String remark,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate,
            @Param("inventoryDate") String inventoryDate
    );

    /**
     * 更新 Header 關閉狀態
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY_HEADER 
            SET remark = :remark, 
                close_date = :closeDate 
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    void updateHeaderClose(
            @Param("remark") String remark,
            @Param("closeDate") Timestamp closeDate,
            @Param("inventoryDate") String inventoryDate
    );

    /**
     * 更新 Header 關閉狀態 (包含 access_id 和 access_date)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY_HEADER 
            SET remark = :remark, 
                close_date = :closeDate,
                access_id = :accessId,
                access_date = :accessDate
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    void updateHeaderCloseWithAccess(
            @Param("remark") String remark,
            @Param("closeDate") Timestamp closeDate,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate,
            @Param("inventoryDate") String inventoryDate
    );

    /**
     * 更新 Header 全部欄位 (除了主鍵)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY_HEADER 
            SET generate_type = :generateType,
                close_date = :closeDate,
                access_id = :accessId,
                access_date = :accessDate,
                remark = :remark
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    void updateHeader(
            @Param("generateType") String generateType,
            @Param("closeDate") Timestamp closeDate,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate,
            @Param("remark") String remark,
            @Param("inventoryDate") String inventoryDate
    );

    // ==================== 3. 刪除方法 ====================

    /**
     * 刪除特定盤點日期的 Header
     */
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    void deleteByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 刪除所有已結案的 Header
     */
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NOT NULL
            """, nativeQuery = true)
    void deleteAllClosed();

    /**
     * 刪除指定日期之前的 Header (已結案)
     */
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date < :date 
            AND close_date IS NOT NULL
            """, nativeQuery = true)
    void deleteClosedBeforeDate(@Param("date") String date);

    // ==================== 4. 檢查/驗證方法 ====================

    /**
     * 檢查特定日期是否已存在 Header
     */
    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date = :inventoryDate
            """, nativeQuery = true)
    boolean existsByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 檢查特定日期是否已結案
     */
    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM ASSET_INVENTORY_HEADER 
            WHERE inventory_date = :inventoryDate 
            AND close_date IS NOT NULL
            """, nativeQuery = true)
    boolean isClosed(@Param("inventoryDate") String inventoryDate);

    /**
     * 檢查是否有未結案的盤點 (排除特定日期)
     */
    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM ASSET_INVENTORY_HEADER 
            WHERE close_date IS NULL 
            AND inventory_date != :excludeDate
            """, nativeQuery = true)
    boolean hasActiveInventoryExcluding(@Param("excludeDate") String excludeDate);
}