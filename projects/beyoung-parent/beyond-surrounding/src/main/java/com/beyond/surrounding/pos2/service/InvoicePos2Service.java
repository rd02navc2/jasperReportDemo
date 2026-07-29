package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.pos2.repository.InvoicePos2Repository;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.bean.ResponseBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicePos2Service {

    private final InvoicePos2Repository invoicePos2Repository;

    @Transactional(readOnly = true)
    public ResponseBean validate(String invoiceNo, String randomNo) {
        log.info("開始驗證發票: invoiceNo={}, randomNo={}", invoiceNo, randomNo);
        
        ResponseBean bean = new ResponseBean();
        // 預設成功
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        try {
            // 執行資料庫查詢
            List<Object[]> resultList = invoicePos2Repository.validateInvoiceRaw(invoiceNo, randomNo);

            // 判斷是否查無資料 (對應舊寫法 _lBean.size() <= 0)
            if (resultList == null || resultList.isEmpty()) {
                bean.setCode(ErrCodeConst.erp_not_found);
                bean.setMessage(ErrCodeConst.erp_not_found_message);
                log.warn("發票驗證失敗，查無 ERP 資料: invoiceNo={}", invoiceNo);
            }
            
        } catch (Exception e) {
            log.error("Service 驗證發票發生異常: {}", e.getMessage(), e);
            throw e; // 丟出給 Controller 的 try-catch 捕獲
        }

        return bean;
    }
}