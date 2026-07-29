package com.beyoung.surrounding.pos2.service;

import com.beyoung.surrounding.pos2.entity.ONE_TRANSACTION_LOG;
import com.beyoung.surrounding.pos2.entity.TAIWAN_TRANSACTION_LOG;
import com.beyoung.surrounding.pos2.repository.OnePayPos2Repository;
import lombok.RequiredArgsConstructor;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnePayPos2Service {

    private final OnePayPos2Repository onePayPos2Repository;

    @Transactional
    public void addTranLog(String orderId, String center, String counterId, String posId, 
                           Date posDateTime, String productName, int amount, String oneTimeKey, 
                           String transactionId, Date transactionDate, String walletProvider) {
        
        TAIWAN_TRANSACTION_LOG log = new TAIWAN_TRANSACTION_LOG();
        log.setOrderId(orderId);
        log.setPosCenter(center);
        log.setPosCounterId(counterId);
        log.setPosProductName(productName);
        log.setPosAmount(amount);
        log.setPosId(posId);
        log.setPosDateTime(posDateTime);
        log.setOneTimeKey(oneTimeKey);
        log.setAccessDate(new Timestamp(System.currentTimeMillis()));
        log.setTransactionType("PAYMENT");
        log.setTransactionId(transactionId);
        log.setTransactionDate(transactionDate);
        log.setWalletProvider(walletProvider);

        onePayPos2Repository.save(log);
    }
    
	@Transactional(readOnly = true)
    public ONE_TRANSACTION_LOG getTranLog(String orderId) {
        // 使用 Spring Data JPA 的 findById
        // 如果找不到，直接回傳 null 或丟出例外
        return onePayPos2Repository.findById(orderId).orElse(null);
    }

	@Transactional(readOnly = true)
	public void updTranLogRefund(String orderId, String newOrderId, String refundTranXId, Date posDateTime, String invoiceNo) {
	    // 直接呼叫 Repository，將現在時間轉換為 Timestamp 傳入
		onePayPos2Repository.updateRefundInfo(
	        orderId, 
	        newOrderId, 
	        refundTranXId, 
	        posDateTime, 
	        invoiceNo, 
	        new Date() // 自動獲取當前時間
	    );
	}
	
	
	
	
	
	
	
	
    
}