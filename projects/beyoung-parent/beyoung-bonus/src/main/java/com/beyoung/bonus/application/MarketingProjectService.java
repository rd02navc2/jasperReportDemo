package com.beyoung.bonus.application;

import com.beyoung.bonus.infrastructure.LrqFile;
//.domain.entity.LrqFile;
import com.beyoung.bonus.infrastructure.LrqFileId;
//.domain.entity.LrqFileId;
import com.beyoung.bonus.infrastructure.LrqFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingProjectService {

    private final LrqFileRepository lrqFileRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private static final String THRESHOLD_REDIS_KEY = "bonus:config:dynamicThreshold";

    /**
     * 變更全域大額贈點門檻，並手動精準刷新 Redis 緩存
     */
    public void updateDynamicThreshold(String newThreshold) {
        if (newThreshold == null || newThreshold.trim().isEmpty()) {
            throw new IllegalArgumentException("門檻值欄位 [threshold] 不得為空");
        }
        try {
            // 嚴謹度校驗：驗證傳入字串是否為合法的 BigInteger 格式
            new java.math.BigInteger(newThreshold.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("門檻值必須為純整數字串，例如: 50000");
        }

        // 使用純代碼精準打擊，秒級清除/重寫快取
        stringRedisTemplate.opsForValue().set(THRESHOLD_REDIS_KEY, newThreshold.trim());
        log.info("[快取強刷防線] 大額贈點門檻已同步寫入 Redis 快取 Key: [{}], 新值: {}", THRESHOLD_REDIS_KEY, newThreshold);
    }

    /**
     * 獲取當前生效的門檻值 (若 Redis 內尚未設定，則自動返回系統預設防禦值 50000)
     */
    public String getDynamicThreshold() {
        String current = stringRedisTemplate.opsForValue().get(THRESHOLD_REDIS_KEY);
        if (current == null || current.trim().isEmpty()) {
            return "50000"; // 預設防禦底線
        }
        return current;
    }

    /**
     * 新增專案規則 (POST)
     * 結合 Redis 快取清除機制，確保新活動上架時，前線 POS 能瞬間穿透載入最新規則
     * * 優化重點：明確呼叫 Getter，並透過 CacheEvict 確保抹除前線可能殘留的空值快取紀錄
     */
    @Transactional(rollbackFor = Exception.class)
    public LrqFile addProject(LrqFile project) {
        log.info("[營運後台]準備新增活動專案規則 -> {}:{}:{}", 
                project.getLrq01(), project.getLrq02(), project.getLrqplant());

        LrqFileId id = new LrqFileId(project.getLrq01(), project.getLrq02(), project.getLrqplant());
        if (lrqFileRepository.existsById(id)) {
            throw new IllegalArgumentException("活動設定已存在！主鍵衝突");
        }

        // 執行 MySQL 落庫
        LrqFile savedEntity = lrqFileRepository.save(project);
        
        // 2. 核心手工強刷防線：繞過 AOP 註解，直接對 Redis 發動打擊
        // 嚴格對齊 Spring Cache 預設的雙冒號規範 "快取名稱::鍵值"
        String redisKey = "bonus:project::" + project.getLrq01() + ":" + project.getLrq02() + ":" + project.getLrqplant();
        
        try {
            Boolean isDeleted = stringRedisTemplate.delete(redisKey);
            log.info("[快取強刷防線]MySQL 寫入完成！手動驅逐 Redis Key: [{}], 是否成功驅逐: {}", redisKey, isDeleted);
        } catch (Exception e) {
            // 唯讀快取異常不影響主體事務落庫，印出日誌供系統管理員追蹤
            log.error("[快取強刷失敗]Redis 連線或清除時發生異常，請檢查基礎設施狀態！", e);
        }

        return savedEntity;
    }
    

    /**
     * 更新專案規則 (PUT)
     * 改用 StringRedisTemplate 手動清除機制，繞過 Spring AOP 事務死角，確保 100% 執行 DEL
     */
    @Transactional(rollbackFor = Exception.class)
    public LrqFile updateProject(LrqFile project) {
        log.info("[營運後台]收到更新活動規則請求，主鍵結構 -> {}:{}:{}", 
                project.getLrq01(), project.getLrq02(), project.getLrqplant());
        
        LrqFileId id = new LrqFileId(project.getLrq01(), project.getLrq02(), project.getLrqplant());
        
        // 1. 查詢舊資料
        LrqFile existProject = lrqFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("找不到對應的活動設定，無法更新！主鍵: [%s-%s-%s]", 
                        project.getLrq01(), project.getLrq02(), project.getLrqplant())));
        
        // 2. 動態賦值防禦
        if (project.getLrq03() != null) existProject.setLrq03(project.getLrq03());
        if (project.getLrqacti() != null) existProject.setLrqacti(project.getLrqacti());
        if (project.getLrq10() != null) existProject.setLrq10(project.getLrq10());
        if (project.getLrq11() != null) existProject.setLrq11(project.getLrq11());

        // 3. 執行 MySQL 落庫
        LrqFile savedEntity = lrqFileRepository.save(existProject);
        
        // 4. 終極強刷防線用純代碼精準打擊，與前線 POS 查快取的 Key 格式完美對齊
        String redisKey = "bonus:project::" + project.getLrq01() + ":" + project.getLrq02() + ":" + project.getLrqplant();
        
        try {
            Boolean isDeleted = stringRedisTemplate.delete(redisKey);
            log.info("[快取強刷防線]MySQL 更新成功！手動驅逐 Redis Key: [{}], 是否成功驅逐: {}", redisKey, isDeleted);
        } catch (Exception e) {
            log.error("快取強刷失敗Redis 連線或清除時發生異常！", e);
        }

        return savedEntity;
    }
}