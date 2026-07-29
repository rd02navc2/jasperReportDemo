package com.beyoung.surrounding.bonus.service;

import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Lazy; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyoung.surrounding.dto.AppendInvoiceDTO;
import com.beyoung.surrounding.bonus.bean.PointResponseBean;
import com.beyoung.surrounding.bonus.dto.BonusDTO;
import com.beyoung.surrounding.bonus.dto.MemberStatsDTO;
import com.beyoung.surrounding.bonus.repository.ExcludeCounterRepository;
import com.beyoung.surrounding.app.entity.LRQ_FILE;
import com.beyoung.surrounding.bonus.repository.LrqFileRepository;
import com.beyoung.surrounding.app.entity.LSM_FILE;
import com.beyoung.surrounding.bonus.repository.LsmFileRepository;
import com.beyoung.surrounding.bonus.repository.LpjFileRepository;
import com.beyoung.surrounding.util.ERPWebService;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.beyoung.surrounding.util.GetDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;


/**
 * 點數與排除專櫃商業邏輯層
 */
@Slf4j
@Service
public class BonusService {

    private final LrqFileRepository lrqFileRepository;
    private final LsmFileRepository lsmFileRepository;
    private final LpjFileRepository lpjFileRepository;
    private final ExcludeCounterRepository excludeCounterRepository;
    private final ERPWebService erpWebService;
    
    @PersistenceContext
    private final EntityManager em;

    @Value("${erp.ws.url:http://127.0.0.1/erp/ws}")
    private String erpUrl;

    /**
     * 建構子注入 (配合 @Lazy 自身注入)
     */
    public BonusService(LrqFileRepository lrqFileRepository, LsmFileRepository lsmFileRepository, LpjFileRepository lpjFileRepository, 
                        ExcludeCounterRepository excludeCounterRepository, // BonusLogRepository bonusLogRepository,  
                        ERPWebService erpWebService, EntityManager em, @Lazy BonusService self) 
                        {
        this.lrqFileRepository = lrqFileRepository;
        this.lsmFileRepository = lsmFileRepository;
		this.lpjFileRepository = lpjFileRepository;
        this.excludeCounterRepository = excludeCounterRepository;
        this.erpWebService = erpWebService;
        this.em = em;
    }

    public void updateLsmCardId(String oldCardId, String newCardId) {
        lsmFileRepository.updateCardId(oldCardId, newCardId);
    }
    
    public MemberStatsDTO getMemberStats(String cardNo, String start, String end) {
        // 取得系統預設時區，用於 LocalDate / LocalDateTime 轉 java.util.Date
        ZoneId zoneId = ZoneId.systemDefault();
        
        // 1. 精準轉換時間：起日 00:00:00
        Date startDate = Date.from(
            LocalDate.parse(start).atStartOfDay(zoneId).toInstant()
        );
        
        // 2. 精準轉換時間：迄日 23:59:59.999999999
        Date endDate = Date.from(
            LocalDate.parse(end).atTime(LocalTime.MAX).atZone(zoneId).toInstant()
        );
        
        // 3. 呼叫全面原生 SQL 化的 Repository 計算點數加總
        Double totalPoints = lsmFileRepository.calculatePointsByCardNo(cardNo, startDate, endDate);
        
        // 4. 回傳 DTO，VIP 等級設為預設值 0 (因 Bonus 服務無權查詢 Member 等級)
        return new MemberStatsDTO(
            totalPoints != null ? totalPoints : 0.0, 
            0 
        );
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void addPoint(String center, String counterId, String cardNo, 
                         String lrq01, String lrq02, Integer point) throws Exception {

        log.info("執行單純補點程序：卡號 -> {}, 點數 -> {}, 專案 -> [{}-{}]", cardNo, point, lrq01, lrq02);
        
        // 1. 會員檢查
        if (lpjFileRepository.findByLpj03(cardNo).isEmpty()) {
            throw new IllegalArgumentException("無效的會員卡號：" + cardNo);
        }

        // 2. 檢查專櫃權限
        if (this.validateCounter(counterId).getMessage().contains("排除")) {
            throw new IllegalArgumentException("此專櫃為排除專櫃，無法補點。");
        }

	     // 3. 檢查活動政策 (保留 LRQ03 點數上限檢查)
	     // 說明：配合 Repository 規格，將 LocalDate.now() 更正為 java.util.Date (new Date())
	     LRQ_FILE activeProject = lrqFileRepository.findValidProject(lrq01, lrq02, center, new java.util.Date())
	             .orElseThrow(() -> new IllegalArgumentException("活動政策未生效或已過期"));

        // 4. [移除金額檢核] -- 直接進入點數上限檢核
        int maxLimitPoints = activeProject.getLrq03() != null ? activeProject.getLrq03() : 100;
        if (point > maxLimitPoints) {
            throw new IllegalArgumentException("補點點數已超過活動限制上限！");
        }

        // 5. 寫入流水帳 (保持不變)
        LSM_FILE lsmFile = new LSM_FILE();
        lsmFile.setLsm01(cardNo);
        lsmFile.setLsm02("2");
        lsmFile.setLsm03(GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli(""));
        lsmFile.setLsm04(point.doubleValue());
        lsmFile.setLsm05(new java.util.Date());
        // ... (設定其他欄位)
        
        lsmFileRepository.save(lsmFile);
        em.flush();
        log.info("[Bonus服務] 單純補點寫入成功");
    }
    
    //dc-
    /**
     * 第一層：Redis 冪等性防重門口
     * member 這裡絕不能加上 @Transactional 註解，確保 Redis 鎖的狀態同步不受 DB 交易快取與回滾所阻礙。
     */
    /*
    public void addPoint(String center, String counterId, String cardNo, 
                         String lrq01, String lrq02, Integer point, String orderNo, BigDecimal amount) throws Exception {
       
    	
    	self.executeAddPointTransaction(center, counterId, cardNo, lrq01, lrq02, point, amount); 
    	
    	/*
        // 1. 組裝唯一的防重 Key
        String redisKey = "bonus:idempotent:" + (orderNo != null ? orderNo : (cardNo + ":" + lrq01 + "-" + lrq02));
        
        // 2. 透過 SETNX 搶鎖，並設定 5 分鐘快取效期，防止高併發重複點擊
        Boolean isLockCaptured = redisTemplate.opsForValue()
                .setIfAbsent(redisKey, "PROCESSING", Duration.ofMinutes(5));

        if (Boolean.FALSE.equals(isLockCaptured)) {
            log.warn("[Bonus服務] 偵測到重複請求！防重鎖已存在: {}", redisKey);
            throw new IllegalStateException("該筆加點請求正在處理中，請勿重複送單！");
        }

        try {
            log.info("[Redis防護] 搶鎖成功。Key: {}，轉入內層交易處理...", redisKey);
            
            // 3. member 修正：利用 self 呼叫第二層帶有 @Transactional 事務的落庫邏輯，保證事務被 Spring 代理託管
            self.executeAddPointTransaction(center, counterId, cardNo, lrq01, lrq02, point, amount); 
            
            // 4. 執行成功：將 Redis 狀態更新為 SUCCESS，保留 1 天防止重複送單
            redisTemplate.opsForValue().set(redisKey, "SUCCESS", Duration.ofDays(1));
            log.info("[Redis防護] 業務處理落庫成功。防重鎖狀態變更為 SUCCESS，Key: {}", redisKey);
            
        } catch (Exception e) {
            // 5. 異常安全閥：如果原邏輯內拋出異常，立刻把 Redis 的鎖刪掉，容許修正資料後立即重試
            redisTemplate.delete(redisKey);
            log.error("[Bonus服務] 業務流程或資料庫交易失敗，已自動清除釋放 Redis 防重鎖。原因: {}", e.getMessage());
            throw e; 
        }
        
    }

    @Transactional(rollbackFor = Exception.class) 
    public void executeAddPointTransaction(String center, String counterId, String cardNo, 
                                           String lrq01, String lrq02, Integer point, BigDecimal amount) throws Exception { 
        
        AppendInvoiceBean _validateBean = this.validateCounter(counterId);
        if (_validateBean.getMessage() != null 
                && _validateBean.getMessage().contains("此專櫃為排除專櫃")) { 
            throw new IllegalArgumentException(_validateBean.getMessage());
        }
 
        log.info("發動加點程序：卡號 -> {}, 點數 -> {}, 專案 -> [{}-{}]", cardNo, point, lrq01, lrq02);
        
        LocalDate todayDate = LocalDate.now();     

        LrqFile activeProject = lrqFileRepository.findValidProject(lrq01, lrq02, center, todayDate)
                .orElseThrow(() -> {
                    log.error("[政策攔截]現場補點不符政策！專案 [{}-{}] 在據點 [{}] 未生效、已過期或已被關閉", lrq01, lrq02, center);
                    return new IllegalArgumentException(
                        String.format("現場補點不符政策：專案 [%s-%s] 在此據點未生效、已過期或被關閉！", lrq01, lrq02));
                });

        double reqAmount = amount != null ? amount.doubleValue() : 0.0;
        double minAmountThreshold = activeProject.getLrq04() != null ? activeProject.getLrq04() : 0.0; 

        if (reqAmount < minAmountThreshold) {
            log.error("政策攔截補點失敗！消費金額 {} 未達該專案設定的最低門檻 {}", reqAmount, minAmountThreshold);
            throw new IllegalArgumentException(
                String.format("加點失敗：消費金額未達該活動門檻！（輸入金額: %s, 活動門檻: %s）", reqAmount, minAmountThreshold));
        }
     
        int maxLimitPoints = activeProject.getLrq03() != null ? activeProject.getLrq03() : 100;
        double dPoint = point != null ? point.doubleValue() : 0.0;

        if (dPoint > maxLimitPoints) {
            log.error("Bonus服務補點失敗！輸入點數 {} 超過該活動限制上限 {}", dPoint, maxLimitPoints);
            throw new IllegalArgumentException(
                String.format("補點點數已超過活動限制上限！（輸入: %s 點, 該活動上限: %s 點）", point, maxLimitPoints));
        }

        LocalDate permanentExpiryDate = LocalDate.of(9999, 12, 31);
           
        LsmFile lsmFile = new LsmFile();
        lsmFile.setLsm01(cardNo); 
        lsmFile.setLsm02("2"); 
        lsmFile.setLsm03(GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli(""));
        lsmFile.setLsm04(dPoint);
        
        //  調整：完全契合重構後的 java.util.Date 欄位指派
        lsmFile.setLsm05(new java.util.Date()); // 取得當前年月日時分秒
        lsmFile.setLsm06(java.sql.Date.valueOf("9999-12-31")); // 點數到期日：固定寫入 9999-12-31 代表永久有效
        
        lsmFile.setLsm08(0.0);
        lsmFile.setLsmlegal(center);
        lsmFile.setLsmplant(center);
        lsmFile.setLsm09(0);
        lsmFile.setLsm10(0.0);
        lsmFile.setLsm11(0.0);
        lsmFile.setLsm12(0.0);
        lsmFile.setLsm13(0.0);
        lsmFile.setLsm15("1");
        lsmFile.setLsmstore(center);
        lsmFile.setTaLsm01("補贈點");
        lsmFile.setTaLsm02(counterId);
        lsmFile.setTaLsm03("");
        lsmFile.setTaLsm04(GetDateTime.getTimeMilli());
        
        lsmFile.setTaLsm05(new java.util.Date()); //  調整：取得當前年月日時分秒
        lsmFile.setTaLsm06(0.0);
        lsmFile.setTaLsm07(0.0);
        lsmFile.setTaLsm08(0.0);
        lsmFile.setTaLsm12(0.0);
        
        //  修正：確認舊系統 LSM_FILE 規格中無 TA_LSM13 欄位，直接將該行拔除，不留死碼

        lsmFileRepository.save(lsmFile); 
        
        // 強制先推入資料庫檢核 Constraint，若噴錯能在 catch 區塊中精準清除 Redis 鎖
        em.flush(); 
        
        log.info("[Bonus服務]流水帳 LSM_FILE 寫入成功，單號: {}", lsmFile.getLsm03());
    }
*/    
    /*
	@Transactional(readOnly = true)
	public List<LsmHistoryDTO> getPointHistByMemberID(String sMemberID, String sStartDate, String sEndDate) {
	    // 確保這裡有宣告並賦值 cardNos
	    List<String> cardNos = memberServiceClient.getCardNumbersByMemberId(sMemberID);
	    
	    if (cardNos == null || cardNos.isEmpty()) {
	        return Collections.emptyList();
	    }
	
	    String sql = """
	            SELECT lsmstore, lsm01, lsm02, lsm04, lsm05, lsm08,
	                   ta_lsm02
	            FROM lsm_file
	            WHERE lsm01 IN (:cardNos)
	                 """;
/*
        String sql = """
                SELECT lsmstore, lsm01, lsm02, lsm04, lsm05, lsm08,
                       ta_lsm02, ta_lsm09,
                       CASE WHEN ta_lsm02 = 'EC' THEN 'beyond beyond' ELSE tqa02 END AS tqa02,
                       ta_lsm04
                FROM lsm_file
                LEFT JOIN lnt_file ON ta_lsm02 = lnt06 AND lsm05 BETWEEN lnt17 AND lnt18
                LEFT JOIN tqa_file ON lnt30 = tqa01
                WHERE lsm01 IN (:cardNos)
                  AND lsm05 BETWEEN STR_TO_DATE(:startDate, '%Y-%m-%d') 
                                AND STR_TO_DATE(:endDate, '%Y-%m-%d')
                  AND lsm02 IN ('2','5','7','8','9','B')
                ORDER BY CONCAT(DATE_FORMAT(lsm05, '%Y-%m-%d'), RPAD(ta_lsm04, 20, '0')) DESC
                """;

	    return em.createNativeQuery(sql)
	            .unwrap(NativeQuery.class)
	            .setTupleTransformer((tuple, aliases) -> {
	                return new LsmHistoryDTO(
	                    (String) tuple[0],
	                    (String) tuple[1],
	                    (String) tuple[2],
	                    (Double) tuple[3],
	                    (LocalDateTime) tuple[4],
	                    (Double) tuple[5],
	                    (String) tuple[6],
	                    (String) tuple[7],
	                    (String) tuple[8],
	                    (String) tuple[9]
	                );
	            })
	            .setParameter("cardNos", cardNos) // 現在這裡一定能存取到變數
	            .setParameter("startDate", sStartDate)
	            .setParameter("endDate", sEndDate)
	            .getResultList();
	}
	*/
    
    @Transactional(rollbackFor = Exception.class)
    public void sendPointChangedEvent(String center, String counterId, String cardNo, int point, String sourceBillNo) {
        log.info("Bonus服務準備發送點數歸零 Kafka 事件 -> 據點: {}, 專櫃: {}, 卡號: {}, 扣減點數: {}, 關聯單號: {}", 
                center, counterId, cardNo, point, sourceBillNo);

        BonusDTO.Request.builder()
                .center(center)              
                .counterId(counterId)        
                .cardNo(cardNo)              
                .point(point)                
                .invoice(sourceBillNo)       
                .loginId("SYSTEM_CRON_JOB")  
                .createUserId("SYSTEM")
                .build();
    }
    

    @Transactional(rollbackFor = Exception.class) 
    public PointResponseBean usePoint(BonusDTO.Request requestBody) {
        AppendInvoiceDTO _validateBean = this.validateCounter(requestBody.getCounterId());
        if (_validateBean.getMessage() != null && _validateBean.getMessage().contains("此專櫃為排除專櫃")) { 
            throw new IllegalArgumentException(_validateBean.getMessage());
        }
        
        int erpPoint = requestBody.getPoint() != null ? -requestBody.getPoint() : 0;
        
        try { 
            JSONObject joResult = erpWebService.useMemberPointSit(
                    erpUrl, 
                    requestBody.getCenter(), 
                    requestBody.getCounterId(), 
                    requestBody.getCardNo(), 
                    erpPoint, 
                    requestBody.getInvoice(), 
                    requestBody.getInvoice(), 
                    "GC", 
                    GetDateTime.getTimeMilli("")                
            );
            
            log.info("Bonus：usePoint Response：{}", joResult != null ? joResult.toString() : "null");
            
            String code = (joResult != null && joResult.has("code")) ? ((JsonNode) joResult.get("code")).asText() : "";
            String message = (joResult != null && joResult.has("message")) ? ((JsonNode) joResult.get("message")).asText() : "";

            if (!"0".equals(code)) {
                if (message.contains("餘額") || message.contains("不足")) {
                    log.error("[ERP 扣點失敗 - 餘額不足]卡號: {}, 扣減點數: {}, ERP 回傳訊息: {}", requestBody.getCardNo(), requestBody.getPoint(), message);
                } else if (message.contains("卡號") || message.contains("無此")) {
                    log.error("[ERP 扣點失敗 - 卡號有誤]輸入卡號: {}, ERP 回傳訊息: {}", requestBody.getCardNo(), message);
                } else {
                    log.error("[ERP 扣點失敗 - 其他未知異常]卡號: {}, 代碼: {}, 訊息: {}", requestBody.getCardNo(), code, message);
                }
                throw new RuntimeException("遠端 ERP 檢核失敗 [" + code + "]：" + message);
            }
            
        } catch (Exception e) {
            log.error("ERP 連線或解析異常", e);
            throw new RuntimeException("ERP WebService 呼叫失敗", e);
        }
       
        return PointResponseBean.builder()
                .code(ErrCodeConst.finished)
                .message(ErrCodeConst.finished_message)
                .build();
    }

    /**
     * 驗證是否為排除專櫃
     */
    @Transactional(readOnly = true)
    public AppendInvoiceDTO validateCounter(String sCounterID) {
        AppendInvoiceDTO _bean = new AppendInvoiceDTO();
        _bean.setCode("0");
        _bean.setMessage("finished");

        try {
            boolean isExclude = excludeCounterRepository.existsById(sCounterID);
            if (isExclude) {
                _bean.setCode("E001"); 
                _bean.setMessage("此專櫃為排除專櫃，不可參與活動，專櫃代碼：" + sCounterID);
            }
        } catch (Exception e) {
            log.error("validateCounter 發生錯誤: sCounterID = {}", sCounterID, e);
            _bean.setCode("9999");
            _bean.setMessage("系統發生錯誤：" + e.getMessage());
        }
        return _bean;
    }
}