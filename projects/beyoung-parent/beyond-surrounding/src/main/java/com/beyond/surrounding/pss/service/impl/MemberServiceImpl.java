package com.beyond.surrounding.pss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.pss.repository.MemberRepository;
import com.beyond.surrounding.pss.entity.LpjFile;
import com.beyond.surrounding.pss.service.MemberService;

/**
 * 會員資料業務邏輯層實作 (Spring Boot 3 / Jakarta EE 10 規格)
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;
    
    /**
     * 依據卡號取得用於停車折抵的會員主檔
     *  優化：
     * 1. 移除了不必要的 throws Exception，交由 Spring Data JPA 拋出 Unchecked Exception。
     * 2. 加上 @Transactional(readOnly = true) 標記唯讀事務，這能讓 Hibernate 內部跳過髒檢查 (Dirty Checking)，優化 ERP 大數據連線查詢的效能。
     */
    @Override
    @Transactional(readOnly = true)
    // 100% 遵循既定簽章與舊版業務行為，並完美轉換為新版小駝峰 LpjFile 實體
    public LpjFile getMemberData4PD(String cardId) throws Exception {
        
        // 直接透過 Repository 查詢並回傳 LpjFile 實體，省去 Projection 手動組裝的隱患
        LpjFile memberBean = memberRepository.getMemberData4PD(cardId);
        
        return memberBean;
    }
}
    
