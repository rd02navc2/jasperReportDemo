package com.beyoung.bonus.infrastructure;

import com.beyoung.bonus.domain.entity.ExcludeCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 排除專櫃資料存取層
 * 繼承 JpaRepository 以獲得標準的 CRUD 操作 (save, delete, find, etc.)
 * 對應實體: ExcludeCounter, 主鍵類型: String (sCounterID)
 */
@Repository
public interface ExcludeCounterRepository extends JpaRepository<ExcludeCounter, String> {
    
    // JpaRepository 已經內建了以下方法，不需額外實作：
    // - save(ExcludeCounter entity): 對應原邏輯的新增/更新 
    // - deleteById(String id): 對應原邏輯的移除排除專櫃 
    // - findAll(): 取得所有資料以產生排除清單
    // - existsById(String id): 用於檢查專櫃是否在排除清單中
	
	// 檢查專櫃 ID 是否存在
    boolean existsByCounterId(String counterId);
}