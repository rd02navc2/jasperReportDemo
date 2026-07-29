package com.beyoung.surrounding.pos2.service;

import com.beyoung.surrounding.app.entity.LPK_FILE;
import com.beyoung.surrounding.pos2.repository.LpjFileRepository;
import com.beyoung.surrounding.pos2.repository.MemberPos2Repository;
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
public class MemberPos2Service {
	
	private final MemberPos2Repository memberPos2Repository;
	private final LpjFileRepository lpjFileRepository; 
	
	@Transactional(readOnly = true)
    public boolean isValidate(String cardID) {
        log.info("驗證會員卡有效性，Card ID: {}", cardID);
        try {
            // 改為接收 String
            String lpj09 = lpjFileRepository.findLpj09ByCardID(cardID);
            
            // 查無資料 (null) 或格式不符，回傳 false
            if (lpj09 == null || "".equals(lpj09.trim()) || !"2".equals(lpj09)) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("驗證會員卡時發生異常: " + e.getMessage(), e);
            return false;
        }
    }

	@Transactional(readOnly = true)
	public LPK_FILE getMemberByCardID(String cardID) {
		log.info("透過 memberPos2Repository 查詢會員資料，Card ID: {}", cardID);
		
		try {
			// 1. 直接呼叫 Repository 封裝的原生查詢
			List<Object[]> resultList = memberPos2Repository.findMemberByCardIDRaw(cardID);
			
			if (resultList == null || resultList.isEmpty()) {
				log.warn("Repository 查無對應會員資料，cardID: {}", cardID);
				return new LPK_FILE();
			}

			// 2. 承襲原舊系統邏輯，抓取查詢結果的最後一筆
			Object[] row = resultList.get(resultList.size() - 1);
			
			// 3. 映射至 LpkFile 物件
			LPK_FILE bean = new LPK_FILE();
			bean.setLpk01(row[0] != null ? row[0].toString() : null);
			bean.setLpk03(row[1] != null ? row[1].toString() : null);
			bean.setLpk04(row[2] != null ? row[2].toString() : null);
			bean.setLpk18(row[3] != null ? row[3].toString() : null);
			bean.setLpj03(row[4] != null ? row[4].toString() : null);
			
			// 點數轉換 (Double 型態)
			bean.setLpj12(row[5] != null ? Double.valueOf(row[5].toString()) : null);
			bean.setTaLpj01(row[6] != null ? Double.valueOf(row[6].toString()) : null);
			bean.setTaLpj02(row[7] != null ? Double.valueOf(row[7].toString()) : null);
			bean.setTaLpj03(row[8] != null ? Double.valueOf(row[8].toString()) : null);
			
			bean.setLpkud02(row[9] != null ? row[9].toString() : null);
			bean.setLpk14(row[10] != null ? row[10].toString() : null);

			return bean;
			
		} catch (Exception e) {
			log.error("Service 處理 getMemberByCardID 異常: " + e.getMessage(), e);
			return new LPK_FILE();
		}
	}
	
	
	
	
	
	
}