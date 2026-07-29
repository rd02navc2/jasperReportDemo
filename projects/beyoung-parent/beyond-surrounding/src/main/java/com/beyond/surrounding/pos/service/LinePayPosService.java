package com.beyond.surrounding.pos.service;

import com.beyond.surrounding.pos.entity.TRANSACTION_LOG;
import com.beyond.surrounding.pos.repository.LinePayPosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class LinePayPosService {

    @Autowired
    private LinePayPosRepository linePayPosRepository;

    @Transactional(readOnly = true)
    public void addTranLog(String orderId, String center, String counterId, String productName, 
                           int amt, String oneTimeKey, String transactionId, String transactionType, 
                           Date transactionDate, String currency) throws Exception {
        
        // 使用 Builder 模式建立 Entity
        TRANSACTION_LOG log = TRANSACTION_LOG.builder()
                .orderId(orderId)
                .posCenter(center)
                .posCounterId(counterId)
                .posProductName(productName)
                .posAmount(amt)
                .oneTimeKey(oneTimeKey)
                .transactionId(transactionId)
                .transactionType(transactionType)
                .transactionDate(transactionDate)
                .currency(currency)
                .accessDate(new Date()) // 寫入當下時間
                .needCheck("N")
                .build();

        // 呼叫 Repository 儲存
        linePayPosRepository.save(log);
    }

    @Transactional // 移除 readOnly=true，因為這是寫入操作
    public void updateRefundLog(String orderId, String transactionId, String transactionType, Date date, String invoiceNo) {
        // 呼叫您在 Repository 中定義的現代化方法
        linePayPosRepository.updateRefundInfo(orderId, transactionId, transactionType, date, invoiceNo);
    }
		
}
		