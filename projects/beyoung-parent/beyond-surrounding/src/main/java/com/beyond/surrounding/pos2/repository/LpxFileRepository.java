package com.beyond.surrounding.pos2.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.beyond.surrounding.pos2.entity.LpxFile;

@Repository("pos2LpxFileRepository")
public interface LpxFileRepository extends JpaRepository<LpxFile, String> {

	// 維持 List<LpxFile> 回傳
    // 將實體中存在但 SQL 沒查的欄位 (lpx01, lpx03, lpx04) 全部用 NULL + 別名補齊，滿足 Hibernate 6 的檢查
    @Query(value = """
        SELECT 
            LPX01 as lpx01, -- 必須包含 @Id 欄位
            LPX02 as lpx02, 
            NULL as lpx03,  -- 補齊實體中的 lpx03 欄位
            NULL as lpx04,  -- 補齊實體中的 lpx04 欄位
            DATE_FORMAT(LPX17, '%Y-%m-%d') as lpx17, 
            LPX23 as lpx23, 
            LPX28 as lpx28 
        FROM lpx_file 
        WHERE LPXACTI = 'Y'
        """, nativeQuery = true)
    List<LpxFile> findActiveCouponTypes();
}

