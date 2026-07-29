package com.beyond.surrounding.ts.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.ts.bean.TSRequestBean;
import com.beyond.surrounding.ts.entity.TS_EC_LOG;
import com.beyond.surrounding.ts.repository.TspgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TSPGService {
	
    public final TspgRepository tspgRepository;

    /**
     * 儲存 TSPG 原始交易/請求紀錄
     * 
     * @param requestBody 接收舊有的 TSRequestBean 或對應請求模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(Object requestBody) throws Exception {
        log.info("【TSPG】開始紀錄交易請求資料: {}", requestBody);

        if (!(requestBody instanceof TSRequestBean)) {
            throw new IllegalArgumentException("不合法的請求參數類型，預期應為 TSRequestBean");
        }
        
        TSRequestBean bean = (TSRequestBean) requestBody;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        // 1. 先行刪除重複的單號紀錄 (比照舊系統覆寫邏輯)
        tspgRepository.deleteByOrderNo(bean.getOrder_no());
        log.debug("【TSPG】已清除可能重複的訂單紀錄, 單號: {}", bean.getOrder_no());

        // 2. 寫入全新的授權初始紀錄
        tspgRepository.insertInitialLog(
                bean.getOrder_no(),
                bean.getEc_order_no(),
                bean.getAmt(),
                bean.getOrder_desc(),
                bean.getCard_no(),
                bean.getInstall_period(),
                now
        );
        
        log.info("【TSPG】訂單授權紀錄儲存成功, 單號: {}", bean.getOrder_no());
    }
        
    /**
     * 更新 TSPG 交易回傳狀態（不含退款金額的多載方法）
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String orderNo, String txType, String retCode, String retMsg) throws Exception {
        // 直接導向含有退款金額的重載方法，代入 null
        this.update(orderNo, txType, retCode, retMsg, null);
    }

    /**
     * 更新 TSPG 交易回傳狀態（包含取消授權、退款、取消退款之主要實作）
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String orderNo, String txType, String retCode, String retMsg, Integer amtRefund) throws Exception {
        log.info("【TSPG】更新交易狀態 -> 訂單號: {}, 交易類型: {}, 回傳碼: {}, 訊息: {}, 退款金額: {}", 
                orderNo, txType, retCode, retMsg, amtRefund);

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        int updatedRows = 0;

        if ("8".equals(txType)) {
            updatedRows = tspgRepository.updateAuthCancel(orderNo, txType, retCode, retMsg, amtRefund, now);
        } else if ("5".equals(txType)) {
            updatedRows = tspgRepository.updateRefund(orderNo, txType, retCode, retMsg, amtRefund, now);
        } else if ("6".equals(txType)) {
            updatedRows = tspgRepository.updateRefundCancel(orderNo, txType, retCode, retMsg, now);
        } else if ("1".equals(txType)) {
            // 處理背景授權回傳更新
            updatedRows = tspgRepository.updateAuthPostBack(orderNo, txType, retCode, retMsg, now);
        } else {
            log.warn("【TSPG】未知的交易類型 (txType: {})，跳過資料庫更新。", txType);
            return;
        }

        if (updatedRows > 0) {
            log.info("【TSPG】訂單 {} 狀態更新成功，受影響行數: {}", orderNo, updatedRows);
        } else {
            log.warn("【TSPG】訂單 {} 狀態更新未影響任何資料 (請檢查單號是否正確存在於 TS_EC_LOG)", orderNo);
        }
    }

    @Transactional(readOnly = true)
    public TS_EC_LOG getStatus(String orderNo, String cardType) throws Exception {
        log.info("【TSPG Service】執行 getStatus 查詢，單號: {}, 卡別路由: {}", orderNo, cardType);
        
        java.util.Optional<TS_EC_LOG> logOptional;
        
        // 沿用舊系統商業路由：當卡別為 "1" 查 NCCC 帳，其餘查 TS 帳
        if ("1".equals(cardType)) {
            logOptional = tspgRepository.getStatusFromNccc(orderNo);
        } else {
            logOptional = tspgRepository.getStatusFromTs(orderNo);
        }
        
        // 如果找不到資料，為了相容舊 Controller 的邏輯（if (_entity.getOrder_no() == null)），回傳一個空物件
        return logOptional.orElseGet(() -> {
            log.warn("【TSPG Service】查無交易紀錄，單號: {}", orderNo);
            return new TS_EC_LOG();
        });
    }
    

    
}