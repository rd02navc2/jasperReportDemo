package com.beyoung.surrounding.pss.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.beyoung.surrounding.pss.dto.MemberParkingProjection;
import com.beyoung.surrounding.pss.entity.LpjFile;

@Repository("pssMemberRepository")
// @Transactional(transactionManager = "ERP_TM") //  對齊原 ERP 事務管理器
@Transactional
public interface MemberRepository extends JpaRepository<LpjFile, String> {

	/**
     * 1. 依卡號取得停車折抵所需的會員與卡片相關資料
     * 修正：為所有欄位補上明確的表別名 (lpj. 或 lpk.) 以解決欄位歧義
     */
	@Query(value = """
	        SELECT 
	            lpk.LPK04 AS lpk04, lpk.LPK05 AS lpk05, lpk.LPK06 AS lpk06, 
	            lpk.LPK15 AS lpk15, lpk.LPK18 AS lpk18, lpk.LPKUD02 AS lpkud02, 
	            lpj.LPJ01 AS lpj01, lpj.LPJ02 AS lpj02, lpj.LPJ03 AS lpj03, 
	            lpj.LPJ12 AS lpj12, lpj.LPJ14 AS lpj14, 
	            lpj.TA_LPJ01 AS taLpj01, lpj.TA_LPJ02 AS taLpj02, lpj.TA_LPJ03 AS taLpj03 
	        FROM LPJ_FILE lpj 
	        LEFT JOIN LPK_FILE lpk ON lpk.LPK01 = lpj.LPJ01 
	        WHERE lpj.LPJ03 = :cardId 
	          AND lpj.LPJ09 = '2' 
	          AND lpj.TA_LPJ04 = 'Y'
	        """, nativeQuery = true)
	    LpjFile getMemberData4PD(@Param("cardId") String cardId);

    /**
     * 2. 更新會員/卡片狀態 (原本被註解掉的功能，此處一併轉為 JPA 語法備用)
     */
    @Modifying
    @Query(value = """
        UPDATE LPK_FILE 
        SET lpkud02 = :status 
        WHERE lpk01 = :userId
        """, nativeQuery = true)
    void updateLpkStatus(@Param("userId") String userId, @Param("status") String status);

    /**
     * 3. 查詢 LPKUD02 狀態用 (原本被註解掉的功能)
     */
    @Query(value = """
        SELECT lpkud02 FROM LPK_FILE WHERE lpk01 = :userId
        """, nativeQuery = true)
    Optional<String> getLpkud02ByUserId(@Param("userId") String userId);
}