package com.beyoung.surrounding.pos2.repository;

import com.beyoung.surrounding.app.entity.LQE_FILE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LqeFile2Repository extends JpaRepository<LQE_FILE, String> {

    // 補齊原生 SQL：將 SELECT 的別名對齊新 LqeFile Entity 的小駝峰屬性名稱
    @Query(value = """
            SELECT 
                lqe01 AS lqe01, 
                lqe17 AS lqe17, 
                lqe20 AS lqe20, 
                lqe21 AS lqe21, 
                CASE WHEN ta_lqe09 IS NULL THEN 'N' ELSE ta_lqe09 END AS taLqe09, 
                ta_lqe02 AS taLqe02 
            FROM LQE_FILE 
            WHERE lqe01 IN (:couponIds)
            """, nativeQuery = true)
    List<LQE_FILE> findCouponStatusByList(@Param("couponIds") List<String> couponIds);
    
}