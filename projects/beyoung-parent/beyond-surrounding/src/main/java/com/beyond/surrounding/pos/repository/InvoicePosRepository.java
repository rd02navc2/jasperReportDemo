package com.beyond.surrounding.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.app.entity.LQE_FILE;
import java.util.List;

@Repository
public interface InvoicePosRepository extends JpaRepository<LQE_FILE, String> {

	/**
     * 1. 批量失效優惠券 (對應原 doCouponInvalid)
     * 使用 @Modifying 標記為更新操作，並確保事務由調用方（Service）控制
     */
    @Modifying(clearAutomatically = true)
    @Query(value = """
        UPDATE LQE_FILE 
        SET LQE11 = :center, 
            LQE12 = CURRENT_TIMESTAMP, 
            LQE17 = '3' 
        WHERE LQE01 IN :ids
        """, nativeQuery = true)
    void updateCouponInvalid(@Param("center") String center, @Param("ids") List<String> ids);
    
    
}