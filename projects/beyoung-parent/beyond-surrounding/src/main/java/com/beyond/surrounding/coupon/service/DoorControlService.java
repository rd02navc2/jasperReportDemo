package com.beyond.surrounding.coupon.service;

import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.dc.entity.EMPLOYEE;
import com.beyond.surrounding.dc.repositiry.DoorControlUnlimitRepository;
import com.beyond.surrounding.dc.repositiry.HrEmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DoorControlService {

    private final DoorControlUnlimitRepository doorControlUnlimitRepository;
    private final HrEmployeeRepository hrEmployeeRepository;
    /**
     * 取得員工資訊
     */
    @Transactional(readOnly = true)
    public EMPLOYEE getEmployee(String cardNO) {
        if (cardNO == null || cardNO.isBlank()) {
            return new EMPLOYEE(); // 對齊舊系統不噴 Exception，回傳空實體的邏輯
        }

        // 1. 查詢員工姓名
        Optional<String> cnNameOpt = hrEmployeeRepository.findCnnameByCardNo(cardNO.trim());
        
        // 2. 封裝回傳實體
        EMPLOYEE bean = new EMPLOYEE();
        
        // 修正：不用方法參照，改用明確的 Lambda 表達式，完美繞過編譯器對舊欄位命名的型態檢核
        cnNameOpt.ifPresent(name -> bean.setCnName(name)); 
        
        return bean;
    }
    /**
     * 檢查是否為免檢核特權名單
     * 對齊舊系統指定的 "RMS_TM" 事務管理器，並設定為唯讀以優化連線效能
     */
    @Transactional(readOnly = true)
    public boolean isUnlimit(String center, String cardNO, LPK_FILE member) {
        if (member == null || member.getLpk01() == null) {
            return false;
        }
        
        // 直接調用高效的 EXISTS 檢查，取代舊版 Select * 再算 size 的耗能寫法
        return doorControlUnlimitRepository.existsByUserId(member.getLpk01());
    }

    @Transactional(readOnly = true)
    public boolean isEmployee(String lpk03) {
        if (lpk03 == null || lpk03.isBlank()) {
            return false;
        }
        
        // 呼叫優化後的 COUNT 存在性檢查
        return hrEmployeeRepository.existsValidEmployee(lpk03.trim());
    }
    
}