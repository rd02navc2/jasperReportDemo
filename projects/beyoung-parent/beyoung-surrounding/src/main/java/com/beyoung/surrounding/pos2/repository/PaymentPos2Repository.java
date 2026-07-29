package com.beyoung.surrounding.pos2.repository;

import com.beyoung.surrounding.pos2.entity.RYD_FILE;
import com.beyoung.surrounding.pos2.entity.RYD_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface PaymentPos2Repository extends JpaRepository<RYD_FILE, RYD_FILE_ComposeKey> {

    // 沿用舊系統邏輯：查詢 tc_ryd_file 並透過別名對齊 RYD_FILE 的欄位
    @Query(value = """
            SELECT 
                tc_ryd01 AS RYD01, 
                tc_ryd03 AS RYD02, 
                tc_ryd02 AS RYD10 
            FROM tc_ryd_file 
            WHERE tc_rydacti = 'Y' 
              AND tc_ryd06 = 'Y'
            """, nativeQuery = true)
    List<Map<String, Object>> getPaymentTypeRaw();
    
    // 沿用舊系統關聯邏輯，並透過別名精準對齊大寫欄位
    @Query(value = """
            SELECT 
                t1.tc_xma01 AS TC_XMA01, 
                t2.tc_xmb02 AS TC_XMB02, 
                t1.tc_xma07 AS TC_XMA07 
            FROM tc_xma_file t1 
            LEFT JOIN tc_xmb_file t2 ON t1.tc_xma01 = t2.tc_xmb01
            """, nativeQuery = true)
    List<Map<String, Object>> getBinCodeRaw();
    
}