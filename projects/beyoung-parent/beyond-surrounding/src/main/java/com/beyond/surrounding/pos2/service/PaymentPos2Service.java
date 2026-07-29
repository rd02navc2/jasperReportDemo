package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.pos2.entity.RYD_FILE;
import com.beyond.surrounding.pos2.entity.TC_XMA_FILE;
import com.beyond.surrounding.pos2.repository.PaymentPos2Repository;
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
public class PaymentPos2Service {

    private final PaymentPos2Repository paymentPos2Repository;

    @Transactional(readOnly = true)
    public List<RYD_FILE> getPaymentType() {
        log.info("開始執行付款種類原生查詢 (對接舊表 tc_ryd_file)...");
        
        // 1. 從 Repository 撈出 Raw Data Map
        List<Map<String, Object>> rawMaps = paymentPos2Repository.getPaymentTypeRaw();
        List<RYD_FILE> resultList = new ArrayList<>();
        
        if (rawMaps == null || rawMaps.isEmpty()) {
            return resultList;
        }

        // 2. 使用現代化 Builder 初始化 Jackson 轉換器
        // 關閉未知屬性錯誤，並開啟大小寫不敏感匹配，通殺所有大寫 Entity 欄位
        com.fasterxml.jackson.databind.ObjectMapper mapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();

        // 3. 核心轉換：將 Map 裡面的 RYD01, RYD02 等大寫欄位精準揉入 RYD_FILE 實體中
        for (Map<String, Object> row : rawMaps) {
            RYD_FILE entity = mapper.convertValue(row, RYD_FILE.class);
            resultList.add(entity);
        }
        
        log.info("付款種類資料轉換完成，總計筆數: {}", resultList.size());
        return resultList;
    }

    @Transactional(readOnly = true)
    public List<TC_XMA_FILE> getBinCode() {
        log.info("開始執行卡號 BinCode 跨表原生查詢...");
        
        // 1. 從 Repository 取得帶有關聯欄位 (TC_XMB02) 的原始 Map 清單
        List<Map<String, Object>> rawMaps = paymentPos2Repository.getBinCodeRaw();
        List<TC_XMA_FILE> resultList = new ArrayList<>();
        
        if (rawMaps == null || rawMaps.isEmpty()) {
            return resultList;
        }

        // 2. 使用現代化 Builder 配置大寫防禦轉換器
        // 關閉未知欄位報錯（因為 SQL 只查了 3 個欄位，其餘 10 多個欄位會自動安全忽略並設為 null）
        com.fasterxml.jackson.databind.ObjectMapper mapper = com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();

        // 3. 核心轉換：大寫 Map 轉為實體，包含 @Transient 的 TC_XMB02 也會被完美揉入
        for (Map<String, Object> row : rawMaps) {
            TC_XMA_FILE entity = mapper.convertValue(row, TC_XMA_FILE.class);
            resultList.add(entity);
        }
        
        log.info("卡號 BinCode 資料轉換完成，總計筆數: {}", resultList.size());
        return resultList;
    }
    
    
    
    
    
}