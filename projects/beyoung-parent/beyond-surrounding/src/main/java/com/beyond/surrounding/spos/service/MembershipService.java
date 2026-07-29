package com.beyond.surrounding.spos.service;

import com.beyond.surrounding.spos.entity.LPK_FILE;
import com.beyond.surrounding.spos.repository.LPKFILERepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service("sPosMembershipService")
@RequiredArgsConstructor
public class MembershipService {

    private final LPKFILERepository lpkFileRepository; // 注入新建立的 LPK Repository

    /**
     * 根據卡號或手機號碼查詢會員資料
     * 對齊舊專案的交易管理器，若有需要可指定交易名稱如 @Transactional("ERP_TM")
     */
    @Transactional(readOnly = true)
    public LPK_FILE getMemberByCardID(String sCardID) throws Exception {
        log.info("Executing getMemberByCardID for CardID/Mobile: {}", sCardID);
        
        LPK_FILE bean = new LPK_FILE();
        
        try {
            // 1. 執行原生 SQL 查詢 (回傳 List<Object[]>)
            List<Object[]> results = lpkFileRepository.findMemberByCardIDRaw(sCardID);
            
            // 2. 模擬舊專案邏輯：迭代結果，並將最後一筆的資料塞入 bean 中回傳
            for (Object[] row : results) {
                if (row != null && row.length >= 2) {
                    bean.setLPK04(row[0] != null ? row[0].toString() : null);
                    bean.setLPK18(row[1] != null ? row[1].toString() : null);
                }
            }
            
        } catch (Exception e) {
            log.error("Error querying member by card ID: {}", e.getMessage(), e);
            throw e;
        }
        
        return bean;
    }
}