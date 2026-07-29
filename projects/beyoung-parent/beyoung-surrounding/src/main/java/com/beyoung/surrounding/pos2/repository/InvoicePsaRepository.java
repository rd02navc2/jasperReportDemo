package com.beyoung.surrounding.pos2.repository;

import com.beyoung.surrounding.pos2.entity.TC_PSA_FILE;
import com.beyoung.surrounding.pos2.entity.TC_PSA_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePsaRepository extends JpaRepository<TC_PSA_FILE, TC_PSA_FILE_ComposeKey> {

    // 檢查主鍵是否重複 (JPA 原生提供此方法，或我們可以用 existsById，不過為了契合舊代碼的日期 parse 比對，保留字串比對)
    @Query(value = """
            SELECT TC_PSAPLANT 
            FROM TC_PSA_FILE 
            WHERE TC_PSAPLANT = :plant 
              AND TC_PSA01 = :psa01 
              AND TC_PSA02 = :psa02 
              AND TC_PSA03 = :psa03 
              AND TC_PSA04 = :psa04
            """, nativeQuery = true)
    List<String> checkDuplicate(
            @Param("plant") String plant,
            @Param("psa01") String psa01,
            @Param("psa02") String psa02,
            @Param("psa03") String psa03,
            @Param("psa04") String psa04
    );

    // 當 02、03 狀態時，回頭查找 01 狀態的發票關聯
    @Query(value = """
            SELECT * FROM TC_PSA_FILE 
            WHERE TC_PSAPLANT = :plant 
              AND TC_PSA01 = :psa01 
              AND TC_PSA16 = :psa16 
              AND TC_PSA17 = :psa17 
              AND TC_PSA06 = '01' 
            ORDER BY TC_PSA04 DESC
            """, nativeQuery = true)
    List<TC_PSA_FILE> findOriginalInvoice(
            @Param("plant") String plant,
            @Param("psa01") String psa01,
            @Param("psa16") String psa16,
            @Param("psa17") String psa17
    );
}