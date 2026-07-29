package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.pos2.entity.TC_LRJ_FILE;
import com.beyond.surrounding.pos2.repository.RedeemPos2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedeemPos2Service {

    private final RedeemPos2Repository redeemPos2Repository;

    @Transactional
    public TC_LRJ_FILE getRule(String center, String cardType) {
        // 1. 呼叫 Repository 拿取資料清單
        List<TC_LRJ_FILE> list = redeemPos2Repository.findRulesNative(center, cardType);
        
        // 2. 仿照你原本的 for 迴圈邏輯：若無資料回傳 new 物件，有資料則拿最後一筆
        if (list == null || list.isEmpty()) {
            return new TC_LRJ_FILE();
        }
        
        return list.get(list.size() - 1);
    }
    
}