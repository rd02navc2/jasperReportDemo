package com.beyond.report.repository;

import com.beyond.report.entity.EMPLOYEE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EMPLOYEE, String> {

    /**
     * 依員工代碼 (CODE) 查詢員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE CODE = :code
            """, nativeQuery = true)
    EMPLOYEE findByCode(@Param("code") String code);

    /**
     * 依員工ID (EMPLOYEEID) 查詢員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE EMPLOYEEID = :employeeId
            """, nativeQuery = true)
    EMPLOYEE findByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * 依多個員工代碼查詢員工清單
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE CODE IN (:codes)
            """, nativeQuery = true)
    List<EMPLOYEE> findByCodes(@Param("codes") List<String> codes);

    /**
     * 依多個員工ID查詢員工清單
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE EMPLOYEEID IN (:employeeIds)
            """, nativeQuery = true)
    List<EMPLOYEE> findByEmployeeIds(@Param("employeeIds") List<String> employeeIds);

    /**
     * 查詢有電子郵件且啟用的員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE EMAIL IS NOT NULL 
            AND EMAIL != '' 
            AND EMPLOYEESTATEID = '1'
            ORDER BY CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findActiveEmployeesWithEmail();

    /**
     * 依部門查詢員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE DEPT_NAME = :deptName
            ORDER BY CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findByDeptName(@Param("deptName") String deptName);

    /**
     * 依多個部門查詢員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE DEPT_NAME IN (:deptNames)
            ORDER BY DEPT_NAME, CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findByDeptNames(@Param("deptNames") List<String> deptNames);

    /**
     * 依員工姓名模糊查詢
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE CNNAME LIKE CONCAT('%', :name, '%')
            ORDER BY CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findByNameLike(@Param("name") String name);

    /**
     * 查詢指定員工代碼清單的電子郵件 (用於發送通知)
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE CODE IN (:codes)
            AND EMAIL IS NOT NULL 
            AND EMAIL != ''
            """, nativeQuery = true)
    List<EMPLOYEE> findEmployeesWithEmailByCodes(@Param("codes") List<String> codes);

    /**
     * 查詢指定員工ID清單的電子郵件 (用於發送通知)
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE EMPLOYEEID IN (:employeeIds)
            AND EMAIL IS NOT NULL 
            AND EMAIL != ''
            """, nativeQuery = true)
    List<EMPLOYEE> findEmployeesWithEmailByEmployeeIds(@Param("employeeIds") List<String> employeeIds);

    /**
     * 查詢未確認資產的員工清單 (用於發送提醒通知)
     */
    @Query(value = """
            SELECT DISTINCT e.* 
            FROM EMPLOYEE e
            INNER JOIN ASSET_INVENTORY a ON e.CODE = a.owner_id
            WHERE a.inventory_date = (
                SELECT MAX(inventory_date) 
                FROM ASSET_INVENTORY_HEADER 
                WHERE close_date IS NULL
            )
            AND a.confirm_date IS NULL
            AND e.EMAIL IS NOT NULL 
            AND e.EMAIL != ''
            ORDER BY e.CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findUnconfirmedEmployeesWithEmail();

    /**
     * 查詢特定盤點日期未確認的員工
     */
    @Query(value = """
            SELECT DISTINCT e.* 
            FROM EMPLOYEE e
            INNER JOIN ASSET_INVENTORY a ON e.CODE = a.owner_id
            WHERE a.inventory_date = :inventoryDate
            AND a.confirm_date IS NULL
            AND e.EMAIL IS NOT NULL 
            AND e.EMAIL != ''
            ORDER BY e.CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findUnconfirmedEmployeesByDateWithEmail(@Param("inventoryDate") String inventoryDate);

    /**
     * 依員工代碼查詢員工 (使用 JPQL)
     */
    @Query("SELECT e FROM EMPLOYEE e WHERE e.CODE = :code")
    EMPLOYEE findEmployeeByCode(@Param("code") String code);

    /**
     * 依員工ID查詢員工 (使用 JPQL)
     */
    @Query("SELECT e FROM EMPLOYEE e WHERE e.EMPLOYEEID = :employeeId")
    EMPLOYEE findEmployeeByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * 檢查員工是否存在 (依 CODE)
     */
    @Query("SELECT COUNT(e) > 0 FROM EMPLOYEE e WHERE e.CODE = :code")
    boolean existsByCode(@Param("code") String code);

    /**
     * 檢查員工是否存在 (依 EMPLOYEEID)
     */
    @Query("SELECT COUNT(e) > 0 FROM EMPLOYEE e WHERE e.EMPLOYEEID = :employeeId")
    boolean existsByEmployeeId(@Param("employeeId") String employeeId);

    /**
     * 依多個員工ID (EmployeeId) 查詢員工清單 (使用 Native Query)
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE EMPLOYEEID IN (:employeeIds)
            """, nativeQuery = true)
    List<EMPLOYEE> findEmployeesByEmployeeIds(@Param("employeeIds") List<String> employeeIds);

    // ========== 以下是新增的實用方法 ==========

    /**
     * 依員工ID查詢 (使用 JPA 內建方法，因為主鍵是 EMPLOYEEID)
     * 可以直接使用繼承的 findById(String id)
     */

    /**
     * 查詢員工是否存在 (使用 JPA 內建方法)
     * 可以直接使用繼承的 existsById(String id)
     */

    /**
     * 依電子郵件查詢員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE EMAIL = :email
            """, nativeQuery = true)
    EMPLOYEE findByEmail(@Param("email") String email);

    /**
     * 查詢特定職稱的員工
     */
    @Query(value = """
            SELECT * FROM EMPLOYEE 
            WHERE TITLE = :title
            AND EMPLOYEESTATEID = '1'
            ORDER BY CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findByTitle(@Param("title") String title);

    /**
     * 查詢所有主管 (有管理權限的員工)
     */
    @Query(value = """
            SELECT DISTINCT e.* 
            FROM EMPLOYEE e
            WHERE e.MANAGER_CODE IS NOT NULL 
            AND e.MANAGER_CODE != ''
            AND e.EMPLOYEESTATEID = '1'
            ORDER BY e.CODE
            """, nativeQuery = true)
    List<EMPLOYEE> findAllManagers();
}