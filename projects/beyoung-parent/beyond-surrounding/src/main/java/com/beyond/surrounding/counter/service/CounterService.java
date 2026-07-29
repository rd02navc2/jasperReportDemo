package com.beyond.surrounding.counter.service;

import com.beyond.surrounding.bonus.bean.CounterRequestBody;
import com.beyond.surrounding.counter.dto.CounterDTO;
import com.beyond.surrounding.counter.repository.CounterRepository;
import com.beyond.surrounding.counter.repository.CounterRepository.InvoiceProjection;
import com.beyond.surrounding.invoice.repository.ExcludeCounterRepository;
import com.beyond.surrounding.app.entity.ExcludeCounter;
import com.beyond.surrounding.app.entity.LNT_FILE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CounterService {
	
	private final ExcludeCounterRepository excludeCounterRepository;
	private final CounterRepository counterRepository;
	
	
	@Transactional(readOnly = true)
    public List<LNT_FILE> getCounterByID(CounterRequestBody requestBody) {
        
        // 1. 檢查 requestBody 物件本身，以及裡面的 counterId 字串是否為空
        if (requestBody == null || requestBody.getCounterId() == null || requestBody.getCounterId().isEmpty()) {
            log.warn("傳入的 counterID 為空");
            return Collections.emptyList();
        }

        // 修正點：從 requestBody 物件中取出 counterId 字串
        String counterIdStr = requestBody.getCounterId();

        // 2. 解析 ID 清單
        List<String> idList = Arrays.stream(counterIdStr.split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toList());
        
        // (保留你用來 Debug 的原生 Raw 查詢日誌)
        try {
            List<Object[]> results = counterRepository.getCounterByIDRaw(idList);
            log.info("資料庫查到的筆數: {}", results.size());
            for(Object[] row : results) {
                log.info("Row: {}, {}, {}, {}, {}", row[0], row[1], row[2], row[3], row[4]);
            }
        } catch (Exception e) {
            log.error("Raw 日誌查詢失敗: {}", e.getMessage());
        }

        // 3. 呼叫 Repository 並精準對應轉換為 List<LntFile> 回傳
        return counterRepository.getCounterByID(idList).stream()
                .map(p -> {
                	LNT_FILE file = new LNT_FILE();
                    file.setLntplant(p.getLntPlant()); //  提示：若拋錯，請檢查 Entity 的 Setter 大小寫，如 setLntPlant
                    file.setLnt06(p.getLnt06());
                    file.setLnt09(p.getLnt09());
                    file.setLnt30(p.getLnt30());
                    return file;
                })
                .collect(Collectors.toList());
    }
	
	@Transactional(readOnly = true)
    public LNT_FILE getCounterByInvoice(String sInvoiceNo, String sRandomNo) {
        List<InvoiceProjection> results = counterRepository.getCounterByInvoice(sInvoiceNo, sRandomNo);
        
        // 取最後一筆 (模擬原 DAO 的邏輯)
        if (results.isEmpty()) return null;
        InvoiceProjection p = results.get(results.size() - 1);
        
        // 修正點：改為建立 LntFile 實體並手動賦值
        LNT_FILE file = new LNT_FILE();
        file.setLnt06(p.getLnt06());
        file.setLnt09(p.getLnt09());
        
        // 如果你的 LntFile 內有擴充發票相關欄位（如 tcPsa04, tqa02 等），請將下面註解打開並對應：
        // file.setTcPsa04(p.getTcPsa04());
        // file.setTcPsa05(p.getTcPsa05());
        // file.setTqa02(p.getTqa02());
        // file.setTcPsa12(p.getTcPsa12());
        // file.setTcPsa40(p.getTcPsa40());
        
        return file;
    }
	
	@Transactional(readOnly = true)
    public List<LNT_FILE> getCounterList() {
        // 1. 取得資料庫投影並轉換為 LntFile 實體清單 (不包含 try-catch 攔截)
        return counterRepository.getCounterList().stream()
            .map(p -> {
            	LNT_FILE file = new LNT_FILE();
                file.setLnt06(p.getLnt06());
                file.setLnt09(p.getLnt09());
                // 如果你的 LntFile 內有擴充對應 oba01、oba02 與 tqa02 欄位，請將下面三行註解打開：
                // file.setOba01(p.getOba01());
                // file.setOba02(p.getOba02());
                // file.setTqa02(p.getTqa02());
                return file;
            })
            .collect(Collectors.toList());
    }
		
	@Transactional(readOnly = true)
    public List<LNT_FILE> getDeptList() {      
        // 1. 取得資料庫投影並轉換為 LntFile 實體清單
        return counterRepository.getDeptList().stream()
                .map(p -> {
                	LNT_FILE file = new LNT_FILE();
                    file.setLnt09(p.getLnt09());
                    // 如果你的 LntFile 內有擴充對應 oba01 與 oba02 欄位，請將下面兩行註解打開：
                    // file.setOba01(p.getOba01());
                    // file.setOba02(p.getOba02());
                    return file;
                })
                .collect(Collectors.toList());               
    }

    @Transactional(readOnly = true)
    public List<LNT_FILE> getAllCounter() {  
        // 1. 取得資料庫投影並轉換為 LntFile 實體清單
        List<LNT_FILE> allCounters = counterRepository.getAllCounter().stream()
                .map(p -> {
                	LNT_FILE file = new LNT_FILE();
                    file.setLntplant(p.getLntPlant()); // 依據 Entity 的屬性名稱，也可能是 setLNTPLANT
                    file.setLnt06(p.getLnt06());
                    file.setLnt09(p.getLnt09());
                    file.setLnt30(p.getLnt30());
                    return file;
                })
                .collect(Collectors.toList());
                
        // 2. (接著進行您的 exclude 邏輯過濾，例如：allCounters.removeIf(...))
        
        return allCounters;       
    }
	
    @Transactional(readOnly = true)
    public Set<String> getExcludeCounterListName() {
        return excludeCounterRepository.findAll().stream()
                .map(ExcludeCounter::getCounterName)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<CounterDTO.ExcludeCounterResponse> getExcludeCounterList() {
        return excludeCounterRepository.findAll().stream()
                .map(entity -> CounterDTO.ExcludeCounterResponse.builder()
                        .counterId(entity.getCounterId())
                        .counterName(entity.getCounterName())
                        .createUserId(entity.getCreateUserId())
                        .build())
                .toList();
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void addExcludeCounter(CounterDTO.Request request) {
       ExcludeCounter entity = ExcludeCounter.builder()
                .counterId(request.getCounterId())     
                .counterName(request.getCounterName())
                .createUserId(request.getCreateUserId())
                .build();
       excludeCounterRepository.save(entity);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void removeExcludeCounter(@NonNull String counterId) {
        if (excludeCounterRepository.existsById(counterId)) {
            excludeCounterRepository.deleteById(counterId);
            log.info("成功自資料庫移除排除專櫃: {}", counterId);
        } else {
            log.warn("欲移除的排除專櫃不存在: {}", counterId);
            throw new IllegalArgumentException("該專櫃代碼不存在");
        }
    }

}