package com.beyond.surrounding.erp.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.erp.bean.CouponBean;
import com.beyond.surrounding.erp.bean.RequestBonusBody;
import com.beyond.surrounding.erp.entity.TC_LRJ_FILE;
import com.beyond.surrounding.erp.repository.ErpBonusLogRepository;
import com.beyond.surrounding.erp.repository.ErpTcLrjFileRepository;
import com.beyond.surrounding.erp.repository.ErpTcLrjRuleProjection;
import com.beyond.surrounding.util.ERPWebService;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErpBonusService {

	private final ErpBonusLogRepository bonusLogRepository;
	private final ErpTcLrjFileRepository erpTcLrjFileRepository;
	private final Environment env;
	private final JdbcTemplate jdbcTemplate;
	private final ERPWebService erpWebService;

    @Transactional // 取代舊版 Session 事務，確保與 Spring Boot 的事務管理器綁定
    public void saveLog(String sCenter, String sCounterID, String sUserID, String sUserName, 
                        String sCardNO, int iPoint, String sLoginID) {
        try {
            log.info("開始寫入紅利變動日誌(BONUS_LOG)，卡號: {}, 點數: {}", sCardNO, iPoint);
            
            // 呼叫 Repository 執行 Java 15+ Text Block 原生 SQL 寫入
            bonusLogRepository.insertBonusLog(
                sCenter, 
                sCounterID, 
                sUserID, 
                sUserName, 
                sCardNO, 
                iPoint, 
                new Date(), // 現代化直接帶入目前時間
                sLoginID
            );
            
        } catch (Exception e) {
            log.error("寫入 BONUS_LOG 失敗: ", e);
            throw e; // 向上拋出以觸發事務回滾
        }
    }

    @Transactional(readOnly = true) //  唯讀事務，優化查詢效能
    public TC_LRJ_FILE getRule(String sCenter, String sCardType) {
        log.info("查詢點數核銷規則: sCenter={}, sCardType={}", sCenter, sCardType);
        
        // 呼叫 Spring Data JPA Native Query
        List<ErpTcLrjRuleProjection> results = erpTcLrjFileRepository.findActiveRules(sCenter, sCardType);
        
        TC_LRJ_FILE bean = new TC_LRJ_FILE();
        
        if (results != null && !results.isEmpty()) {
            // 忠實重現原系統邏輯：取 List 中的最後一個元素
        	ErpTcLrjRuleProjection lastRecord = results.get(results.size() - 1);
            
            bean.setTC_LRJ01(lastRecord.getTc_lrj01());
            bean.setTC_LRJ03(lastRecord.getTc_lrj03() != null ? lastRecord.getTc_lrj03() : 0.0);
            bean.setTC_LRJ04(lastRecord.getTc_lrj04() != null ? lastRecord.getTc_lrj04() : 0.0);
            bean.setTC_LRJ05(lastRecord.getTc_lrj05() != null ? lastRecord.getTc_lrj05() : 0.0);
        } else {
            log.warn("查無對應的點數核銷規則！sCenter={}, sCardType={}", sCenter, sCardType);
        }
        
        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public CouponBean processCouponExchange(RequestBonusBody requestBody) throws Exception {
        
        // 1. 呼叫 WebService
        JSONObject joResult = erpWebService.exchangeCoupon(
                env.getProperty("ERP_WS_URL"), 
                requestBody.getSDate(), 
                requestBody.getSCenter(), 
                requestBody.getSUserID(), 
                requestBody.getSCaseNO(), 
                requestBody.getSCouponNO(), 
                requestBody.getSCaseItem(), 
                requestBody.getIQty(), 
                requestBody.getIPoint()
        );
        
        log.info("ERP exchangeCoupon Response：{}", joResult.toString());
        
        CouponBean bean = new CouponBean();
        
        // 2. 驗證 ERP 回傳狀態碼是否成功
        if (!joResult.getString("code").equals("0")) {
        	bean.setCode(joResult.getString("code"));
            bean.setMessage(joResult.getString("message"));
            log.error("{}:{}", joResult.getString("code"), joResult.getString("message"));
            return bean;
        }

        // 3. 成功後直接執行原汁原味的 saveBonusCouponLog 邏輯
        saveBonusCouponLog(
                requestBody.getSCenter(), 
                requestBody.getSUserID(), 
                requestBody.getSCaseNO(), 
                requestBody.getSCouponNO(), 
                requestBody.getSCaseItem(), 
                requestBody.getIQty(), 
                requestBody.getIPoint(), 
                requestBody.getSLoginID()
        );

        // 4. 裝填回傳結果
        bean.setLqe20(joResult.getString("lqe20"));
        bean.setLqe21(joResult.getString("lqe21"));
        bean.setRxe04(joResult.getString("rxe04"));
        bean.setRxe05(joResult.getString("rxe05"));
        bean.setLrz02(joResult.getString("lrz02"));
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);        

        return bean;
    }

    /**
     * 改寫舊有 saveBonusCouponLog 方法，擺脫過時的 Hibernate Session 依賴
     */
    public void saveBonusCouponLog(String sCenter, String sUserID, String sCaseNO, String sCouponNO, 
                                   String sCaseItem, int iQty, int iPoint, String sLoginId) throws Exception {
        Calendar cal = Calendar.getInstance();
        
        // 使用 Java 15+ 文字塊 (Text Blocks) 讓 SQL 乾淨又漂亮
        String _sSQL = """
            INSERT INTO BONUS_COUPON_LOG (
                center, user_id, case_no, coupon_no, case_item, qty, point, access_date, access_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        // 透過 jdbcTemplate.update 綁定參數 (替代舊有的 sqlQuery.setString)
        jdbcTemplate.update(_sSQL, 
            sCenter,
            sUserID,
            sCaseNO,
            sCouponNO,
            sCaseItem,
            iQty,
            iPoint,
            new Timestamp(cal.getTimeInMillis()),
            sLoginId
        );
    }
    
}


