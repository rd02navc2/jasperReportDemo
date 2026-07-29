package com.beyond.surrounding.exchange.service;

import com.beyond.surrounding.app.entity.LPQ_FILE;
import com.beyond.surrounding.app.entity.LPR_FILE;
import com.beyond.surrounding.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Transactional(readOnly = true)
    public LPQ_FILE getExchangeSetting(String cardType, String plant) throws Exception {
        // 1. 取得主檔
        List<LPQ_FILE> list = exchangeRepository.findExchangeSetting(cardType, plant);
        if (list.isEmpty()) {
            throw new Exception("Cannot find Exchange Setting");
        }
        LPQ_FILE lpq = list.get(0); // 取得第一筆
        
        // 2. 取得子檔
        List<LPR_FILE> details = exchangeRepository.findExchangeDetails(lpq.getLpq01(), plant, cardType);
        
        // 3. 封裝回傳
        lpq.setLprFile(details);
        return lpq;
    }
}