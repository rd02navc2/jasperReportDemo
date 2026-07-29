package com.beyond.surrounding.pos.repository;

import com.beyond.surrounding.pos.entity.TD;
import com.beyond.surrounding.pos.entity.TD_ComposeKey;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PosDetailRepository extends JpaRepository<TD, TD_ComposeKey> {
	
	public interface InvoiceProjection {
	    String getSalDate();
	    String getStoreNo();
	    String getPosNo();
	    String getTrnNo();
	    String getInvNo();
	}
	
	/**
     * JPQL 版本 (推薦)
     * 利用 JOIN FETCH 一次性撈取主檔與關聯的 TR/TP 列表
     */
	// 直接查詢 TD，移除 fetch 關聯
	@Query(value = """ 	
			SELECT * FROM TD_TABLE_NAME WHERE INV_NO = :invNo
			""", nativeQuery = true) // 修正為 3 個雙引號，並使用實際表名與欄位
	Optional<TD> findByInvNo(@Param("invNo") String invNo);
    
    /**
     * 原生 SQL 版本 (使用 Projection 介面)
     * 適合需要執行特定複雜 SQL 邏輯時使用
     */
    @Query(value = """
        SELECT t.SAL_DATE as salDate, t.STORE_NO as storeNo, t.POS_NO as posNo, t.TRN_NO as trnNo, t.INV_NO as invNo
        FROM TD t
        LEFT JOIN TR tr ON t.SAL_DATE = tr.SAL_DATE AND t.STORE_NO = tr.STORE_NO 
                          AND t.POS_NO = tr.POS_NO AND t.TRN_NO = tr.TRN_NO
        LEFT JOIN TP tp ON t.SAL_DATE = tp.SAL_DATE AND t.STORE_NO = tp.STORE_NO 
                          AND t.POS_NO = tp.POS_NO AND t.TRN_NO = tp.TRN_NO
        WHERE t.INV_NO = :invNo
        """, nativeQuery = true)
    List<InvoiceProjection> findByInvNoNative(@Param("invNo") String invNo);
    
    /**
     * 根據會員 VIP 號碼查詢
     */
    @Query(value = """
            SELECT * FROM TD_FILE WHERE VIP_NO = :vipNo
            """, nativeQuery = true) //  請將 TD_FILE 與 VIP_NO 換成實際的資料表與欄位名稱
    List<TD> findByVipNo(@Param("vipNo") String vipNo);
    
}