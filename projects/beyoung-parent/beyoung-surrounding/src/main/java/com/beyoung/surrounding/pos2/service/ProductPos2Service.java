package com.beyoung.surrounding.pos2.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyoung.surrounding.pos2.entity.IMA_FILE;
import com.beyoung.surrounding.pos2.repository.ProductPos2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductPos2Service {
	
	private final ProductPos2Repository productPos2Repository;

	@Transactional(readOnly = true)
	public IMA_FILE getProductByPNO(String pNO) {
		log.info("透過 ProductRepository 查詢商品資料，商品代碼 pNO: {}", pNO);
		
		// 呼叫 Repository 並在查無資料時直接回傳空實體（承襲舊系統不管查到與否都 new 一個物件的邏輯）
		return productPos2Repository.findProductByPnoRaw(pNO)
				.orElseGet(() -> {
					log.warn("Repository 查無此商品代碼: {}", pNO);
					return new IMA_FILE();
				});
	}

	@Transactional(readOnly = true)
    public List<IMA_FILE> getProductByDateCombined(String sFromDate, String sToDate) {
        log.info("開始執行商品日期區間查詢與變價合併，區間: {} ~ {}", sFromDate, sToDate);
        
        // 1. 取得基礎商品清單 (注意：若回傳是 Immutable 陣列，需用 new ArrayList 包起來才能 addAll)
        List<IMA_FILE> baseList = new java.util.ArrayList<>(productPos2Repository.getProductByDate(sFromDate, sToDate));
        
        // 2. 取得變價商品清單
        List<IMA_FILE> changePriceList = productPos2Repository.getChangePriceByDate(sFromDate, sToDate);
        
        // 3. 合併清單
        if (changePriceList != null && !changePriceList.isEmpty()) {
            baseList.addAll(changePriceList);
        }
        
        log.info("商品清單合併完成，總計件數: {}", baseList.size());
        return baseList;
    }

}
