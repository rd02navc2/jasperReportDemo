package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.pos2.entity.LntFile;
import com.beyond.surrounding.pos2.entity.RYC_FILE;
import com.beyond.surrounding.pos2.repository.CounterPos2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CounterPos2Service {

    private final CounterPos2Repository counterPos2Repository;
            
    @Transactional(readOnly = true)
    public List<LntFile> getAllCounter() {
        log.info("透過 counterPos2Repository 執行全體櫃位原生查詢...");
        
        // 1. 從 Repository 拿到對接好的 Map 清單
        List<Map<String, Object>> rawList = counterPos2Repository.getAllCounter();
        List<LntFile> resultList = new ArrayList<>();
        
        if (rawList == null || rawList.isEmpty()) {
            return resultList;
        }

        com.fasterxml.jackson.databind.ObjectMapper mapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                // 1. 關閉未知屬性報錯
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 2. 啟用大小寫不敏感匹配 (完美取代原本的 .enable 方法)
                .configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
        
        
        // 2. 核心魔法：利用 Jackson 自動將 Map 內的大寫鍵值，轉換映射回 LNT_FILE 實體物件
        for (Map<String, Object> row : rawList) {
        	LntFile entity = mapper.convertValue(row, LntFile.class);
            resultList.add(entity);
        }
        
        log.info("櫃位清單由 Map 轉實體完成，總計筆數: {}", resultList.size());
        return resultList;
    }

	@Transactional(readOnly = true)
    public List<RYC_FILE> getPOSData() {
        log.info("透過 counterPos2Repository 執行 POS 異動設定檔查詢...");
        
        // 1. 呼叫 Repository 取得乾淨的 Map 結構
        List<Map<String, Object>> rawMaps = counterPos2Repository.getPOSDataRaw();
        List<RYC_FILE> resultList = new java.util.ArrayList<>();
        
        if (rawMaps == null || rawMaps.isEmpty()) {
            return resultList;
        }

        // 2. 初始化 Jackson 轉換器（修正：ACCEPT_CASE_INSENSITIVE_PROPERTIES 應屬於 MapperFeature）
        com.fasterxml.jackson.databind.ObjectMapper mapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                // 1. 關閉未知屬性報錯
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 2. 啟用大小寫不敏感匹配 (完美取代原本的 .enable 方法)
                .configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
        
        // 3. 核心轉換：將每一列 Map 轉為大寫的 RYC_FILE 實體
        for (Map<String, Object> row : rawMaps) {
            RYC_FILE entity = mapper.convertValue(row, RYC_FILE.class);
            resultList.add(entity);
        }
        
        log.info("POS 異動設定檔 Map 轉實體完成，總計筆數: {}", resultList.size());
        return resultList;
    }
    
    
}