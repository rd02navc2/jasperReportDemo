package com.beyoung.surrounding.invoice.repository;

import org.springframework.stereotype.Repository;
import com.beyoung.surrounding.app.entity.LSM_FILE;
import com.beyoung.surrounding.app.entity.LSM_FILE_ComposeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

@Repository("invoiceLsmFileRepository")
public interface LsmFileRepository extends JpaRepository<LSM_FILE, LSM_FILE_ComposeKey> {
	
    @Modifying
    @Query(value = """
          INSERT INTO LSM_FILE (
              lsm01, lsm02, lsm03, lsm04, lsm05,
              lsm06, lsm08, lsmlegal, lsmplant, lsm09,
              lsm10, lsm11, lsm12, lsm13, lsm15,
              lsmstore, ta_lsm09, ta_lsm10, ta_lsm01, ta_lsm02,
              ta_lsm03, ta_lsm04, ta_lsm05, ta_lsm06, ta_lsm07,
              ta_lsm08, ta_lsm12, ta_lsm13
          ) VALUES (
              :lsm01, :lsm02, :lsm03, :lsm04, :lsm05,
              :lsm06, :lsm08, :lsmlegal, :lsmplant, :lsm09,
              :lsm10, :lsm11, :lsm12, :lsm13, :lsm15,
              :lsmstore, :taLsm09, :taLsm10, :taLsm01, :taLsm02,
              :taLsm03, :taLsm04, :taLsm05, :taLsm06, :taLsm07,
              :taLsm08, :taLsm12, :taLsm13
          )
          """, nativeQuery = true)
    void insertLsmDetail(@Param("lsm01") String lsm01, @Param("lsm02") String lsm02, @Param("lsm03") String lsm03, @Param("lsm04") double lsm04, @Param("lsm05") Date lsm05,
                         @Param("lsm06") Date lsm06, @Param("lsm08") double lsm08, @Param("lsmlegal") String lsmlegal, @Param("lsmplant") String lsmplant, @Param("lsm09") Integer lsm09,
                         @Param("lsm10") double lsm10, @Param("lsm11") double lsm11, @Param("lsm12") double lsm12, @Param("lsm13") double lsm13, @Param("lsm15") String lsm15,
                         @Param("lsmstore") String lsmstore, @Param("taLsm09") String taLsm09, @Param("taLsm10") String taLsm10, @Param("taLsm01") String taLsm01, @Param("taLsm02") String taLsm02,
                         @Param("taLsm03") String taLsm03, @Param("taLsm04") String taLsm04, @Param("taLsm05") Date taLsm05, @Param("taLsm06") double taLsm06, @Param("taLsm07") double taLsm07,
                         @Param("taLsm08") double taLsm08, @Param("taLsm12") double taLsm12, @Param("taLsm13") String taLsm13);

    @Query(value = """
        SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END
        FROM lsm_file
        WHERE lsm02 IN ('2', '7')
          AND ta_lsm09 = :invoiceNo
        """, nativeQuery = true)
    int countLsmFileUsedNative(@Param("invoiceNo") String invoiceNo);

    default boolean isInvoiceUsedInErp(String invoiceNo) {
        return countLsmFileUsedNative(invoiceNo) > 0;
    }

    @Query(value = """
        SELECT CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END
        FROM lsm_file
        WHERE lsm03 = :invoiceNo
          AND ta_lsm01 = :taLsm01
        """, nativeQuery = true)
    int countByLsm03AndTaLsm01Native(@Param("invoiceNo") String invoiceNo, @Param("taLsm01") String taLsm01);

    default boolean existsByLsm03AndTaLsm01(String invoiceNo, String taLsm01) {
        return countByLsm03AndTaLsm01Native(invoiceNo, taLsm01) > 0;
    }

    @Query(value = """
        SELECT *
        FROM lsm_file
        WHERE lsm03 = :invoiceNo
          AND ta_lsm01 = :taLsm01
        FETCH FIRST 1 ROW ONLY
        """, nativeQuery = true)
    Optional<LSM_FILE> findTopByLsm03AndTaLsm01Native(@Param("invoiceNo") String invoiceNo, @Param("taLsm01") String taLsm01);
    
}