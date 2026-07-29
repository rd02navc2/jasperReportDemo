package com.beyond.surrounding.pos.service;

import com.beyond.surrounding.pos.entity.PX_TRANSACTION_LOG;
import com.beyond.surrounding.pos.repository.PxPayPosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

@Service 
@RequiredArgsConstructor
public class PxPayPosService {

	private final PxPayPosRepository pxPayPosRepository;

    @Transactional // 使用預設的 TransactionManager
    public PX_TRANSACTION_LOG addTranLog(String orderID, String center, String counterID, String posID, Date posDateTime, 
                           String productName, int amt, String oneTimeKey, String transactionID, 
                           Date transactionDate, String payTool, String toolName, String identity, String carrier) {
        
        // 1. 使用 Builder 模式 (如果 Entity 有加 @Builder)
    	// 建議將 Entity 欄位統一為駝峰式 (例如 posCounterId)，並用 @Column 映射到資料庫
    	PX_TRANSACTION_LOG log = PX_TRANSACTION_LOG.builder()
    	        .orderId(orderID)           // 確保 Entity 是 orderId
    	        .posCenter(center)          // 確保 Entity 是 posCenter
    	        .posCounterId(counterID)    // 確保 Entity 是 posCounterId (注意對應)
    	        .posProductName(productName)
    	        .posAmount(amt)
    	        .posId(posID)
    	        .posDateTime(posDateTime)
    	        .oneTimeKey(oneTimeKey)
    	        .invoCarrier(carrier)       // 確保 Entity 是 invoCarrier
    	        .accessDate(new Timestamp(System.currentTimeMillis()))
    	        .transactionType("PAYMENT")
    	        .transactionId(transactionID)
    	        .transactionDate(transactionDate)
    	        .payTool(payTool)
    	        .toolName(toolName)
    	        .identity(identity)
    	        .build();

    	return pxPayPosRepository.save(log);
    }

    @Transactional
    public PX_TRANSACTION_LOG getTranLog(String orderId) {
        // 使用 findById 並透過 orElse(null) 處理，若找不到則回傳 null
        return pxPayPosRepository.findById(orderId).orElse(null);
    }
    
    @Transactional
    public void updTranLogRefund(String orderID, String newOrderID, String refundTranXID, Date posDateTime, String invoiceNO) {
        pxPayPosRepository.updateRefundLog(
            orderID,
            newOrderID,
            refundTranXID, // 直接傳入 String
            posDateTime,
            invoiceNO,
            new Timestamp(System.currentTimeMillis())
        );
    }
    
    
}