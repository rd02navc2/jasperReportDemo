package com.beyond.report.repository;

import com.beyond.report.entity.ASSET_INVENTORY;
import com.beyond.report.entity.ASSET_INVENTORY_ComposeKey;
import com.beyond.report.entity.ASSET_MAINTAIN_HIST;
import com.beyond.report.entity.ASSET_MEMO_HIST;
import com.beyond.report.entity.ASSET_SCRAPPED;
import com.beyond.report.entity.ASSET_TYPE;
import com.beyond.report.projection.AssetInventoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@org.springframework.stereotype.Repository
public interface AssetInventoryRepository extends JpaRepository<ASSET_INVENTORY, ASSET_INVENTORY_ComposeKey> {

    // ========================================================================
    // 1. 查詢方法 (Query Methods)
    // ========================================================================

    /**
     * 依照部門名稱排序時 (多加 owner_id 排序)
     */
    @Query(value = """
            SELECT 
                t1.inventory_date AS inventory_date,
                t1.self_no AS self_no,
                t1.model_no AS model_no,
                t1.type AS type,
                t1.prod_desc AS prod_desc,
                t1.owner_id AS owner_id,
                t1.owner_name AS owner_name,
                t1.title AS title,
                t1.dept_name AS dept_name,
                t1.confirm_id AS confirm_id,
                t1.confirm_date AS confirm_date,
                t2.maintain_hist AS maintain_hist,
                t4.memo_hist AS memo_hist,
                t3.type_name AS type_name
            FROM ASSET_INVENTORY t1
            LEFT JOIN ASSET_MAINTAIN_HIST t2 ON t1.self_no = t2.self_no
            LEFT JOIN ASSET_MEMO_HIST t4 ON t1.self_no = t4.self_no
            LEFT JOIN ASSET_TYPE t3 ON t1.type = t3.type_id
            WHERE t1.inventory_date = :inventoryDate
            ORDER BY 
              CASE WHEN :sord = 'ASC' THEN 
                CASE :sidx 
                  WHEN 'self_no' THEN t1.self_no
                  WHEN 'dept_name' THEN t1.dept_name
                  WHEN 'owner_name' THEN t1.owner_name
                  ELSE t1.inventory_date END
              END ASC,
              CASE WHEN :sord = 'DESC' THEN 
                CASE :sidx 
                  WHEN 'self_no' THEN t1.self_no
                  WHEN 'dept_name' THEN t1.dept_name
                  WHEN 'owner_name' THEN t1.owner_name
                  ELSE t1.inventory_date END
              END DESC,
              t1.owner_id ASC
            """, nativeQuery = true)
    List<AssetInventoryProjection> findInventoryOrderByDept(
            @Param("inventoryDate") String inventoryDate,
            @Param("sidx") String sidx,
            @Param("sord") String sord);

    /**
     * 一般排序
     */
    @Query(value = """
            SELECT 
                t1.inventory_date AS inventory_date,
                t1.self_no AS self_no,
                t1.model_no AS model_no,
                t1.type AS type,
                t1.prod_desc AS prod_desc,
                t1.owner_id AS owner_id,
                t1.owner_name AS owner_name,
                t1.title AS title,
                t1.dept_name AS dept_name,
                t1.confirm_id AS confirm_id,
                t1.confirm_date AS confirm_date,
                t2.maintain_hist AS maintain_hist,
                t4.memo_hist AS memo_hist,
                t3.type_name AS type_name
            FROM ASSET_INVENTORY t1
            LEFT JOIN ASSET_MAINTAIN_HIST t2 ON t1.self_no = t2.self_no
            LEFT JOIN ASSET_MEMO_HIST t4 ON t1.self_no = t4.self_no
            LEFT JOIN ASSET_TYPE t3 ON t1.type = t3.type_id
            WHERE t1.inventory_date = :inventoryDate
            ORDER BY 
              CASE WHEN :sord = 'ASC' THEN 
                CASE :sidx 
                  WHEN 'self_no' THEN t1.self_no
                  WHEN 'model_no' THEN t1.model_no
                  WHEN 'owner_name' THEN t1.owner_name
                  ELSE t1.inventory_date END
              END ASC,
              CASE WHEN :sord = 'DESC' THEN 
                CASE :sidx 
                  WHEN 'self_no' THEN t1.self_no
                  WHEN 'model_no' THEN t1.model_no
                  WHEN 'owner_name' THEN t1.owner_name
                  ELSE t1.inventory_date END
              END DESC
            """, nativeQuery = true)
    List<AssetInventoryProjection> findInventoryOrderGeneral(
            @Param("inventoryDate") String inventoryDate,
            @Param("sidx") String sidx,
            @Param("sord") String sord);

    /**
     * 取得所有資產類型
     */
    @Query(value = "SELECT * FROM ASSET_TYPE ORDER BY type_id", nativeQuery = true)
    List<ASSET_TYPE> getAssetType();

    /**
     * 依資產編號查詢最新的盤點資料
     */
    @Query(value = """
            SELECT 
                t1.*, 
                t2.maintain_hist, 
                t4.memo_hist, 
                t3.type_name 
            FROM ASSET_INVENTORY t1
            LEFT JOIN ASSET_MAINTAIN_HIST t2 ON t1.self_no = t2.self_no
            LEFT JOIN ASSET_MEMO_HIST t4 ON t1.self_no = t4.self_no
            LEFT JOIN ASSET_TYPE t3 ON t1.type = t3.type_id
            WHERE t1.inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER
            ) 
            AND t1.self_no = :selfNo
            """, nativeQuery = true)
    ASSET_INVENTORY findLatestBySelfNo(@Param("selfNo") String selfNo);

    /**
     * 依員工編號查詢資產清單 (最新盤點)
     */
    @Query(value = """
            SELECT 
                t1.generate_type, 
                t1.close_date, 
                t2.*, 
                t4.memo_hist, 
                t3.type_name 
            FROM ASSET_INVENTORY_HEADER t1
            INNER JOIN ASSET_INVENTORY t2 ON t1.inventory_date = t2.inventory_date
            LEFT JOIN ASSET_MEMO_HIST t4 ON t2.self_no = t4.self_no
            LEFT JOIN ASSET_TYPE t3 ON t2.type = t3.type_id
            WHERE t1.inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER
            ) 
            AND t2.owner_id = :ownerId
            """, nativeQuery = true)
    List<ASSET_INVENTORY> findByOwnerId(@Param("ownerId") String ownerId);

    /**
     * 依盤點日期和資產編號查詢單筆資產
     */
    @Query(value = """
            SELECT * FROM ASSET_INVENTORY 
            WHERE inventory_date = :inventoryDate 
            AND self_no = :selfNo
            """, nativeQuery = true)
    ASSET_INVENTORY findByInventoryDateAndSelfNo(
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNo") String selfNo);

    /**
     * 查詢某盤點日期下尚未確認的資產
     */
    @Query(value = """
            SELECT * FROM ASSET_INVENTORY 
            WHERE inventory_date = :inventoryDate 
            AND confirm_date IS NULL
            """, nativeQuery = true)
    List<ASSET_INVENTORY> findUnconfirmedByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 查詢尚未確認的員工清單 (最新盤點且未結案)
     */
    @Query(value = """
            SELECT 
                t1.inventory_date,
                t1.self_no,
                t1.model_no,
                t1.type,
                t1.prod_desc,
                t1.owner_id,
                t1.owner_name,
                t1.title,
                t1.dept_name,
                t1.confirm_id,
                t1.confirm_date
            FROM ASSET_INVENTORY t1
            WHERE t1.inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER 
                WHERE close_date IS NULL
            )
            """, nativeQuery = true)
    List<ASSET_INVENTORY> findUnconfirmedStaff();

    /**
     * 查詢尚未確認的員工 Owner ID 清單
     */
    @Query(value = """
            SELECT DISTINCT owner_id 
            FROM ASSET_INVENTORY 
            WHERE inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER 
                WHERE close_date IS NULL
            )
            AND confirm_date IS NULL
            """, nativeQuery = true)
    List<String> findUnconfirmedOwnerIds();

    /**
     * 查詢尚未確認的員工 Owner ID 清單 (指定盤點日期)
     */
    @Query(value = """
            SELECT DISTINCT owner_id 
            FROM ASSET_INVENTORY 
            WHERE inventory_date = :inventoryDate 
            AND confirm_date IS NULL
            """, nativeQuery = true)
    List<String> findUnconfirmedOwnerIdsByDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 查詢報廢資產 (日期區間 + 模糊搜尋)
     */
    @Query(value = """
    	    SELECT 
    	        t1.self_no,
    	        t1.model_no,
    	        t1.type,
    	        t1.prod_desc,
    	        t1.owner_id,
    	        t1.owner_name,
    	        t1.title,
    	        t1.dept_name,
    	        t1.scrapped_id,
    	        t1.scrapped_date,
    	        t2.maintain_hist, 
    	        t4.memo_hist, 
    	        t3.type_name 
    	    FROM ASSET_SCRAPPED t1
    	    LEFT JOIN ASSET_MAINTAIN_HIST t2 ON t1.self_no = t2.self_no
    	    LEFT JOIN ASSET_MEMO_HIST t4 ON t1.self_no = t4.self_no
    	    LEFT JOIN ASSET_TYPE t3 ON t1.type = t3.type_id
    	    WHERE (:fromDate IS NULL OR :fromDate = '' OR DATE(t1.scrapped_date) >= STR_TO_DATE(:fromDate, '%Y-%m-%d'))
    	      AND (:endDate IS NULL OR :endDate = '' OR DATE(t1.scrapped_date) <= STR_TO_DATE(:endDate, '%Y-%m-%d'))
    	      AND (:selfNo IS NULL OR :selfNo = '' OR t1.self_no LIKE CONCAT('%', :selfNo, '%'))
    	    ORDER BY 
    	      CASE WHEN :sord = 'ASC' THEN 
    	        CASE :sidx 
    	          WHEN 'self_no' THEN t1.self_no
    	          WHEN 'dept_name' THEN t1.dept_name
    	          WHEN 'owner_name' THEN t1.owner_name
    	          ELSE t1.scrapped_date END
    	      END ASC,
    	      CASE WHEN :sord = 'DESC' THEN 
    	        CASE :sidx 
    	          WHEN 'self_no' THEN t1.self_no
    	          WHEN 'dept_name' THEN t1.dept_name
    	          WHEN 'owner_name' THEN t1.owner_name
    	          ELSE t1.scrapped_date END
    	      END DESC
    	    """, nativeQuery = true)
    	List<ASSET_SCRAPPED> findScrappedAssets(
    	        @Param("fromDate") String fromDate,
    	        @Param("endDate") String endDate,
    	        @Param("selfNo") String selfNo,
    	        @Param("sidx") String sidx,
    	        @Param("sord") String sord);

    /**
     * 查詢單筆報廢紀錄
     */
    @Query(value = "SELECT * FROM ASSET_SCRAPPED WHERE self_no = :selfNo", nativeQuery = true)
    ASSET_SCRAPPED findScrappedBySelfNo(@Param("selfNo") String selfNo);

    /**
     * 查詢報廢紀錄 (依報廢人員)
     */
    @Query(value = "SELECT * FROM ASSET_SCRAPPED WHERE scrapped_id = :scrappedId ORDER BY scrapped_date DESC", nativeQuery = true)
    List<ASSET_SCRAPPED> findScrappedByScrappedId(@Param("scrappedId") String scrappedId);

    /**
     * 查詢報廢紀錄 (依日期範圍)
     */
    @Query(value = """
            SELECT * FROM ASSET_SCRAPPED 
            WHERE DATE(scrapped_date) BETWEEN STR_TO_DATE(:fromDate, '%Y-%m-%d') 
                AND STR_TO_DATE(:endDate, '%Y-%m-%d')
            ORDER BY scrapped_date DESC
            """, nativeQuery = true)
    List<ASSET_SCRAPPED> findScrappedByDateRange(
            @Param("fromDate") String fromDate,
            @Param("endDate") String endDate);

    // ========================================================================
    // 2. 維護紀錄 (Maintain History) - 支援 Entity 物件
    // ========================================================================

    /**
     * 查詢維護紀錄
     */
    @Query(value = "SELECT * FROM ASSET_MAINTAIN_HIST WHERE self_no = :selfNo", nativeQuery = true)
    ASSET_MAINTAIN_HIST findMaintainHistBySelfNo(@Param("selfNo") String selfNo);

    /**
     * 查詢多筆維護紀錄
     */
    @Query(value = "SELECT * FROM ASSET_MAINTAIN_HIST WHERE self_no IN (:selfNos)", nativeQuery = true)
    List<ASSET_MAINTAIN_HIST> findMaintainHistBySelfNos(@Param("selfNos") List<String> selfNos);

    /**
     * 新增維護紀錄 (使用個別參數)
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_MAINTAIN_HIST (
                self_no, maintain_hist, access_id, access_date
            ) VALUES (
                :selfNo, :maintainHist, :accessId, :accessDate
            )
            """, nativeQuery = true)
    void saveMaintainHist(
            @Param("selfNo") String selfNo,
            @Param("maintainHist") String maintainHist,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate);

    /**
     * 新增維護紀錄 (使用 Entity 物件)
     * 直接使用 JPA 的 save 方法 (需繼承 JpaRepository)
     */
    // 直接使用繼承的 save(ASSET_MAINTAIN_HIST entity) 方法
    // 但因為 ASSET_MAINTAIN_HIST 可能沒有 @Id 標註，所以需要額外定義

    /**
     * 新增維護紀錄 (使用 Entity 物件) - 透過 @Param 傳入 Entity
     * 注意：此方法需要 ASSET_MAINTAIN_HIST 有對應的欄位名稱
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_MAINTAIN_HIST (
                self_no, maintain_hist, access_id, access_date
            ) VALUES (
                :#{#hist.selfNo}, 
                :#{#hist.maintainHist}, 
                :#{#hist.accessId}, 
                :#{#hist.accessDate}
            )
            """, nativeQuery = true)
    void insertMaintainHist(@Param("hist") ASSET_MAINTAIN_HIST hist);

    /**
     * 更新維護紀錄 (使用個別參數)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_MAINTAIN_HIST 
            SET maintain_hist = :maintainHist, 
                access_id = :accessId, 
                access_date = :accessDate 
            WHERE self_no = :selfNo
            """, nativeQuery = true)
    void updateMaintainHist(
            @Param("maintainHist") String maintainHist,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate,
            @Param("selfNo") String selfNo);

    /**
     * 更新維護紀錄 (使用 Entity 物件)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_MAINTAIN_HIST 
            SET maintain_hist = :#{#hist.maintainHist}, 
                access_id = :#{#hist.accessId}, 
                access_date = :#{#hist.accessDate} 
            WHERE self_no = :#{#hist.selfNo}
            """, nativeQuery = true)
    void updateMaintainHist(@Param("hist") ASSET_MAINTAIN_HIST hist);

    /**
     * 刪除維護紀錄
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_MAINTAIN_HIST WHERE self_no = :selfNo", nativeQuery = true)
    void deleteMaintainHist(@Param("selfNo") String selfNo);

    /**
     * 檢查維護紀錄是否存在
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM ASSET_MAINTAIN_HIST WHERE self_no = :selfNo", nativeQuery = true)
    boolean existsMaintainHist(@Param("selfNo") String selfNo);

    // ========================================================================
    // 3. 備註紀錄 (Memo History) - 支援 Entity 物件
    // ========================================================================

    /**
     * 查詢備註紀錄
     */
    @Query(value = "SELECT * FROM ASSET_MEMO_HIST WHERE self_no = :selfNo", nativeQuery = true)
    ASSET_MEMO_HIST findMemoHistBySelfNo(@Param("selfNo") String selfNo);

    /**
     * 查詢多筆備註紀錄
     */
    @Query(value = "SELECT * FROM ASSET_MEMO_HIST WHERE self_no IN (:selfNos)", nativeQuery = true)
    List<ASSET_MEMO_HIST> findMemoHistBySelfNos(@Param("selfNos") List<String> selfNos);

    /**
     * 新增備註紀錄 (使用個別參數)
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_MEMO_HIST (
                self_no, memo_hist, access_id, access_date
            ) VALUES (
                :selfNo, :memoHist, :accessId, :accessDate
            )
            """, nativeQuery = true)
    void saveMemoHist(
            @Param("selfNo") String selfNo,
            @Param("memoHist") String memoHist,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate);

    /**
     * 新增備註紀錄 (使用 Entity 物件)
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_MEMO_HIST (
                self_no, memo_hist, access_id, access_date
            ) VALUES (
                :#{#hist.selfNo}, 
                :#{#hist.memoHist}, 
                :#{#hist.accessId}, 
                :#{#hist.accessDate}
            )
            """, nativeQuery = true)
    void insertMemoHist(@Param("hist") ASSET_MEMO_HIST hist);

    /**
     * 更新備註紀錄 (使用個別參數)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_MEMO_HIST 
            SET memo_hist = :memoHist, 
                access_id = :accessId, 
                access_date = :accessDate 
            WHERE self_no = :selfNo
            """, nativeQuery = true)
    void updateMemoHist(
            @Param("memoHist") String memoHist,
            @Param("accessId") String accessId,
            @Param("accessDate") Timestamp accessDate,
            @Param("selfNo") String selfNo);

    /**
     * 更新備註紀錄 (使用 Entity 物件)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_MEMO_HIST 
            SET memo_hist = :#{#hist.memoHist}, 
                access_id = :#{#hist.accessId}, 
                access_date = :#{#hist.accessDate} 
            WHERE self_no = :#{#hist.selfNo}
            """, nativeQuery = true)
    void updateMemoHist(@Param("hist") ASSET_MEMO_HIST hist);

    /**
     * 刪除備註紀錄
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_MEMO_HIST WHERE self_no = :selfNo", nativeQuery = true)
    void deleteMemoHist(@Param("selfNo") String selfNo);

    /**
     * 檢查備註紀錄是否存在
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM ASSET_MEMO_HIST WHERE self_no = :selfNo", nativeQuery = true)
    boolean existsMemoHist(@Param("selfNo") String selfNo);

    // ========================================================================
    // 4. 報廢 (Scrapped) - 支援 Entity 物件
    // ========================================================================

    /**
     * 新增報廢紀錄 (使用個別參數)
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_SCRAPPED (
                self_no, model_no, type, prod_desc, owner_id, 
                owner_name, title, dept_name, scrapped_id, scrapped_date
            ) VALUES (
                :selfNo, :modelNo, :type, :prodDesc, :ownerId,
                :ownerName, :title, :deptName, :scrappedId, :scrappedDate
            )
            """, nativeQuery = true)
    void saveScrapped(
            @Param("selfNo") String selfNo,
            @Param("modelNo") String modelNo,
            @Param("type") String type,
            @Param("prodDesc") String prodDesc,
            @Param("ownerId") String ownerId,
            @Param("ownerName") String ownerName,
            @Param("title") String title,
            @Param("deptName") String deptName,
            @Param("scrappedId") String scrappedId,
            @Param("scrappedDate") Timestamp scrappedDate);

    /**
     * 新增報廢紀錄 (使用 Entity 物件)
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_SCRAPPED (
                self_no, model_no, type, prod_desc, owner_id, 
                owner_name, title, dept_name, scrapped_id, scrapped_date
            ) VALUES (
                :#{#scrapped.selfNo}, 
                :#{#scrapped.modelNo}, 
                :#{#scrapped.type}, 
                :#{#scrapped.prodDesc}, 
                :#{#scrapped.ownerId},
                :#{#scrapped.ownerName}, 
                :#{#scrapped.title}, 
                :#{#scrapped.deptName}, 
                :#{#scrapped.scrappedId}, 
                :#{#scrapped.scrappedDate}
            )
            """, nativeQuery = true)
    void insertScrapped(@Param("scrapped") ASSET_SCRAPPED scrapped);

    /**
     * 刪除報廢紀錄
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_SCRAPPED WHERE self_no = :selfNo", nativeQuery = true)
    void deleteScrapped(@Param("selfNo") String selfNo);

    /**
     * 批量刪除報廢紀錄
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_SCRAPPED WHERE self_no IN (:selfNos)", nativeQuery = true)
    void batchDeleteScrapped(@Param("selfNos") List<String> selfNos);

    /**
     * 檢查報廢紀錄是否存在
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM ASSET_SCRAPPED WHERE self_no = :selfNo", nativeQuery = true)
    boolean existsScrappedBySelfNo(@Param("selfNo") String selfNo);

    // ========================================================================
    // 5. 新增/更新/複製 (Asset Inventory)
    // ========================================================================

    /**
     * 複製上一期的資產資料到新盤點
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_INVENTORY (
                inventory_date, self_no, model_no, type, prod_desc,
                owner_id, owner_name, title, dept_name
            )
            SELECT :newDate, self_no, model_no, type, prod_desc,
                   owner_id, owner_name, title, dept_name
            FROM ASSET_INVENTORY
            WHERE inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER 
                WHERE close_date IS NOT NULL
            )
            """, nativeQuery = true)
    void copyAssetsFromPrevious(@Param("newDate") String newDate);

    /**
     * 複製特定盤點日期的資產資料
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_INVENTORY (
                inventory_date, self_no, model_no, type, prod_desc,
                owner_id, owner_name, title, dept_name
            )
            SELECT :newDate, self_no, model_no, type, prod_desc,
                   owner_id, owner_name, title, dept_name
            FROM ASSET_INVENTORY
            WHERE inventory_date = :sourceDate
            """, nativeQuery = true)
    void copyAssetsFromDate(
            @Param("newDate") String newDate,
            @Param("sourceDate") String sourceDate);

    /**
     * 確認資產 (員工簽核)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY 
            SET owner_id = :ownerId, 
                confirm_id = :confirmId, 
                confirm_date = :confirmDate 
            WHERE inventory_date = :inventoryDate 
            AND owner_id = :ownerId
            """, nativeQuery = true)
    void confirmAsset(
            @Param("ownerId") String ownerId,
            @Param("confirmId") String confirmId,
            @Param("confirmDate") Timestamp confirmDate,
            @Param("inventoryDate") String inventoryDate);

    /**
     * 批量確認資產
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY 
            SET confirm_id = :confirmId, 
                confirm_date = :confirmDate 
            WHERE inventory_date = :inventoryDate 
            AND owner_id IN (:ownerIds)
            """, nativeQuery = true)
    void batchConfirmAssets(
            @Param("ownerIds") List<String> ownerIds,
            @Param("confirmId") String confirmId,
            @Param("confirmDate") Timestamp confirmDate,
            @Param("inventoryDate") String inventoryDate);

    /**
     * 取消確認資產
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY 
            SET confirm_id = NULL, 
                confirm_date = NULL 
            WHERE inventory_date = :inventoryDate 
            AND owner_id = :ownerId
            """, nativeQuery = true)
    void unconfirmAsset(
            @Param("ownerId") String ownerId,
            @Param("inventoryDate") String inventoryDate);

    /**
     * 批量更新資產所有人
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY 
            SET owner_id = :newOwnerId,
                owner_name = :newOwnerName,
                title = :newTitle,
                dept_name = :newDeptName
            WHERE inventory_date = :inventoryDate 
            AND self_no IN (:selfNos)
            """, nativeQuery = true)
    void batchUpdateOwner(
            @Param("selfNos") List<String> selfNos,
            @Param("newOwnerId") String newOwnerId,
            @Param("newOwnerName") String newOwnerName,
            @Param("newTitle") String newTitle,
            @Param("newDeptName") String newDeptName,
            @Param("inventoryDate") String inventoryDate);

    /**
     * 單筆插入資產
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO ASSET_INVENTORY (
                inventory_date, self_no, model_no, type, prod_desc,
                owner_id, owner_name, title, dept_name
            ) VALUES (
                :inventoryDate, :selfNo, :modelNo, :type, :prodDesc,
                :ownerId, :ownerName, :title, :deptName
            )
            """, nativeQuery = true)
    void insertAsset(
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNo") String selfNo,
            @Param("modelNo") String modelNo,
            @Param("type") String type,
            @Param("prodDesc") String prodDesc,
            @Param("ownerId") String ownerId,
            @Param("ownerName") String ownerName,
            @Param("title") String title,
            @Param("deptName") String deptName);

    /**
     * 單筆更新資產
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE ASSET_INVENTORY 
            SET model_no = :modelNo,
                type = :type,
                prod_desc = :prodDesc,
                owner_id = :ownerId,
                owner_name = :ownerName,
                title = :title,
                dept_name = :deptName
            WHERE inventory_date = :inventoryDate 
            AND self_no = :selfNo
            """, nativeQuery = true)
    void updateAsset(
            @Param("modelNo") String modelNo,
            @Param("type") String type,
            @Param("prodDesc") String prodDesc,
            @Param("ownerId") String ownerId,
            @Param("ownerName") String ownerName,
            @Param("title") String title,
            @Param("deptName") String deptName,
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNo") String selfNo);

    // ========================================================================
    // 6. 刪除方法
    // ========================================================================

    /**
     * 刪除特定盤點日期的所有資產
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_INVENTORY WHERE inventory_date = :inventoryDate", nativeQuery = true)
    void deleteByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 刪除單一資產
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_INVENTORY WHERE inventory_date = :inventoryDate AND self_no = :selfNo", nativeQuery = true)
    void deleteByInventoryDateAndSelfNo(
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNo") String selfNo);

    /**
     * 批量刪除資產
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_INVENTORY WHERE inventory_date = :inventoryDate AND self_no IN (:selfNos)", nativeQuery = true)
    void batchDeleteAssets(
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNos") List<String> selfNos);

    /**
     * 刪除指定日期之前的資產
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ASSET_INVENTORY WHERE inventory_date < :date", nativeQuery = true)
    void deleteAssetsBeforeDate(@Param("date") String date);

    // ========================================================================
    // 7. 統計/計數方法
    // ========================================================================

    /**
     * 統計特定盤點日期的資產總數
     */
    @Query(value = "SELECT COUNT(*) FROM ASSET_INVENTORY WHERE inventory_date = :inventoryDate", nativeQuery = true)
    long countByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 統計特定盤點日期已確認的資產總數
     */
    @Query(value = "SELECT COUNT(*) FROM ASSET_INVENTORY WHERE inventory_date = :inventoryDate AND confirm_date IS NOT NULL", nativeQuery = true)
    long countConfirmedByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 統計特定盤點日期未確認的資產總數
     */
    @Query(value = "SELECT COUNT(*) FROM ASSET_INVENTORY WHERE inventory_date = :inventoryDate AND confirm_date IS NULL", nativeQuery = true)
    long countUnconfirmedByInventoryDate(@Param("inventoryDate") String inventoryDate);

    /**
     * 統計特定員工的資產總數 (最新盤點)
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM ASSET_INVENTORY 
            WHERE owner_id = :ownerId 
            AND inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER
            )
            """, nativeQuery = true)
    long countByOwnerId(@Param("ownerId") String ownerId);

    /**
     * 統計特定部門的資產總數 (最新盤點)
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM ASSET_INVENTORY 
            WHERE dept_name = :deptName 
            AND inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER
            )
            """, nativeQuery = true)
    long countByDeptName(@Param("deptName") String deptName);

    /**
     * 統計報廢資產總數 (依日期範圍)
     */
    @Query(value = """
            SELECT COUNT(*) 
            FROM ASSET_SCRAPPED 
            WHERE DATE(scrapped_date) BETWEEN STR_TO_DATE(:fromDate, '%Y-%m-%d') 
                AND STR_TO_DATE(:endDate, '%Y-%m-%d')
            """, nativeQuery = true)
    long countScrappedByDateRange(
            @Param("fromDate") String fromDate,
            @Param("endDate") String endDate);

    // ========================================================================
    // 8. 檢查/驗證方法
    // ========================================================================

    /**
     * 檢查資產是否存在
     */
    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM ASSET_INVENTORY 
            WHERE inventory_date = :inventoryDate 
            AND self_no = :selfNo
            """, nativeQuery = true)
    boolean existsByInventoryDateAndSelfNo(
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNo") String selfNo);

    /**
     * 檢查資產是否已確認
     */
    @Query(value = """
            SELECT COUNT(*) > 0 
            FROM ASSET_INVENTORY 
            WHERE inventory_date = :inventoryDate 
            AND self_no = :selfNo 
            AND confirm_date IS NOT NULL
            """, nativeQuery = true)
    boolean isConfirmed(
            @Param("inventoryDate") String inventoryDate,
            @Param("selfNo") String selfNo);

    /**
     * 檢查員工是否所有資產都已確認
     */
    @Query(value = """
            SELECT COUNT(*) = 0 
            FROM ASSET_INVENTORY 
            WHERE owner_id = :ownerId 
            AND inventory_date = :inventoryDate 
            AND confirm_date IS NULL
            """, nativeQuery = true)
    boolean isAllConfirmedByOwnerId(
            @Param("ownerId") String ownerId,
            @Param("inventoryDate") String inventoryDate);

    /**
     * 檢查特定盤點日期是否所有資產都已確認
     */
    @Query(value = """
            SELECT COUNT(*) = 0 
            FROM ASSET_INVENTORY 
            WHERE inventory_date = :inventoryDate 
            AND confirm_date IS NULL
            """, nativeQuery = true)
    boolean isAllConfirmedByInventoryDate(@Param("inventoryDate") String inventoryDate);
}