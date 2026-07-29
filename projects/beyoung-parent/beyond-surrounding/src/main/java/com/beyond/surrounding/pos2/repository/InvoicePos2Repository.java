package com.beyond.surrounding.pos2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.pos2.entity.TC_PSA_FILE;
import com.beyond.surrounding.pos2.entity.TC_PSA_FILE_ComposeKey;
import java.util.List;

@Repository
public interface InvoicePos2Repository extends JpaRepository<TC_PSA_FILE, TC_PSA_FILE_ComposeKey> {

    // 使用 Java Text Blocks (三引號)，針對 MySQL 的 CONCAT 語法優化
	// 修正點：將欄位名稱全改為大寫 (TC_PSA16, TC_PSA17, TC_PSA31)，符合 MySQL 的實際欄位
	// 使用 TRIM() 清除欄位可能存在的 CHAR 補白空格
    @Query(value = """
            SELECT TC_PSA16, TC_PSA17, TC_PSA31 
            FROM TC_PSA_FILE 
            WHERE CONCAT(TRIM(TC_PSA16), TRIM(TC_PSA17)) = :invoiceNo 
              AND TRIM(TC_PSA31) = :randomNo
            """, nativeQuery = true)
    List<Object[]> validateInvoiceRaw(
            @Param("invoiceNo") String invoiceNo, 
            @Param("randomNo") String randomNo
    );

}
