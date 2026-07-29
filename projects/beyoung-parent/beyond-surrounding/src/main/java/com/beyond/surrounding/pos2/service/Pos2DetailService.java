package com.beyond.surrounding.pos2.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.pos2.entity.TD;
import com.beyond.surrounding.pos2.repository.Pos2DetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class Pos2DetailService {
	
	private final Pos2DetailRepository pos2DetailRepository;

	/**
     * 對應 Controller 呼叫接口，補齊日期並調用 Repository 撈取 ERP 發票資料
     */
    @Transactional(readOnly = true)
    public TD getTDByInvoiceNO(Environment env, String center, String invoiceNo) {
        try {
            // 1. 補齊日期：舊 Controller 沒傳日期，此處預設取系統當天日期 (格式: yyyyMMdd，請依資料庫規範微調)
            String todayStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            
            log.info("開始查詢發票明細 -> center: {}, invoiceNo: {}, 自動動態代入日期範圍: {}", center, invoiceNo, todayStr);

            // 2. 調用 Repository 的原生 MySQL 查詢
            List<TD> tdList = pos2DetailRepository.findTDDetails(center, todayStr, todayStr, invoiceNo);
            
            // 3. 依據舊 Controller 邏輯：只預期接收單一 Bean，並檢查 getPOS_NO() 是否為 null
            if (tdList != null && !tdList.isEmpty()) {
                return tdList.get(0);
            }
            
        } catch (Exception e) {
            log.error("執行 getTDByInvoiceNO 發生異常錯誤", e);
        }
        
        // 若找不到資料或拋錯，回傳一個空的 TD 物件，以利 Controller 觸發 "ERP 找不到發票資料" 的 Exception 判斷
        return new TD();
    }


}
