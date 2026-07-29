package com.beyond.surrounding.ts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.ts.entity.TS_PAYPLUS_LOG;
import java.util.Optional;

@Repository
public interface TsPayPlusLogRepository extends JpaRepository<TS_PAYPLUS_LOG, String> {

    /**
     * 使用 Java 15+ Text Blocks 語法與 Spring Data JPA 投影查詢 3D 頁面資訊
     */
	@Query(value = """
            SELECT 
                member_id AS memberId, 
                barcode AS barcode, 
                order_no AS orderNo, 
                hpp_url AS hppUrl
            FROM TS_PAYPLUS_LOG
            WHERE TRIM(member_id) = TRIM(:memberId) 
              AND TRIM(barcode) = TRIM(:barcode)
            LIMIT 1
            """, nativeQuery = true)
    Optional<TsPayPlusLogProjection> find3DPageProjection(
            @Param("memberId") String memberId, 
            @Param("barcode") String barcode);
    
}