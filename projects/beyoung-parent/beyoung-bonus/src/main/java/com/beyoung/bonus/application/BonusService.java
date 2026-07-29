package com.beyoung.bonus.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Lazy; // member 引入延遲載入
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beyoung.bonus.api.client.MemberServiceClient;
import com.beyoung.bonus.domain.bean.AppendInvoiceBean;
import com.beyoung.bonus.domain.bean.PointResponseBean;
import com.beyoung.bonus.domain.dto.BonusDTO;
import com.beyoung.bonus.domain.dto.LsmHistoryDTO;
import com.beyoung.bonus.domain.dto.MemberStatsDTO;
import com.beyoung.bonus.domain.entity.BonusLog;
import com.beyoung.bonus.domain.entity.ExcludeCounter;
import com.beyoung.bonus.infrastructure.BonusLogRepository;
import com.beyoung.bonus.infrastructure.ExcludeCounterRepository;
import com.beyoung.bonus.infrastructure.LrqFile;
import com.beyoung.bonus.infrastructure.LrqFileRepository;
import com.beyoung.bonus.infrastructure.LsmFile;
import com.beyoung.bonus.infrastructure.LsmFileRepository;
import com.beyoung.bonus.util.ERPWebService;
import com.beyoung.bonus.util.ErrCodeConst;
import com.beyoung.bonus.util.GetDateTime;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.beyoung.bonus.domain.event.PointChangedEvent;
import com.beyoung.bonus.util.Constants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.hibernate.query.NativeQuery;

/**
 * 點數與排除專櫃商業邏輯層
 */
@Slf4j
@Service
public class BonusService {

    private final LrqFileRepository lrqFileRepository;
    private final LsmFileRepository lsmFileRepository;
    private final ExcludeCounterRepository excludeCounterRepository;
    private final BonusLogRepository bonusLogRepository;  
    private final ERPWebService erpWebService;
    private final StreamBridge streamBridge;
    private final MemberServiceClient memberServiceClient;
    private final StringRedisTemplate redisTemplate;
    
    @PersistenceContext
    private final EntityManager em;

    @Value("${erp.ws.url}") 
    private String erpUrl;

    // member 核心修正：利用 @Lazy 注入自身代理，徹底破解同類別內呼叫導致 AOP/Transactional 失效的傳統 Java 盲點
    private final BonusService self;

    /**
     * 建構子注入 (配合 @Lazy 自身注入)
     */
    public BonusService(LrqFileRepository lrqFileRepository, LsmFileRepository lsmFileRepository,
                        ExcludeCounterRepository excludeCounterRepository, BonusLogRepository bonusLogRepository,  
                        ERPWebService erpWebService, StreamBridge streamBridge, MemberServiceClient memberServiceClient,
                        StringRedisTemplate redisTemplate, EntityManager em, @Lazy BonusService self) {
        this.lrqFileRepository = lrqFileRepository;
        this.lsmFileRepository = lsmFileRepository;
        this.excludeCounterRepository = excludeCounterRepository;
        this.bonusLogRepository = bonusLogRepository;
        this.erpWebService = erpWebService;
        this.streamBridge = streamBridge;
        this.memberServiceClient = memberServiceClient;
        this.redisTemplate = redisTemplate;
        this.em = em;
        this.self = self;
    }

    public void updateLsmCardId(String oldCardId, String newCardId) {
        // 這裡直接操作 Bonus MS 的 Repository
        // 如果您有 LsmFile 的 Repository，請在這裡呼叫
        lsmFileRepository.updateCardId(oldCardId, newCardId);
    }
    
    public MemberStatsDTO getMemberStats(String cardNo, String start, String end) {
        
    	LocalDateTime startDate = LocalDate.parse(start).atStartOfDay();
    	LocalDateTime endDate = LocalDate.parse(end).atTime(LocalTime.MAX);
        
        // 1. 呼叫單表 Repository 計算加總
        Double totalPoints = lsmFileRepository.calculatePointsByCardNo(cardNo, startDate, endDate);
        
        // 2. 回傳 DTO，VIP 等級設為預設值 0 (因 Bonus 服務無權查詢 Member 等級)
        return new MemberStatsDTO(
            totalPoints != null ? totalPoints : 0.0, 
            0 
        );
    }
    
    /**
     * 整合入口：與特定活動專案（如 603）的 lrq03 欄位永久綁定並動態計算門檻
     */
    @Transactional(rollbackFor = Exception.class)
    public void processOrderAndBonus(BonusDTO.Request req) throws Exception {
        
        // 1. 驗證專櫃是否屬於黑名單
        AppendInvoiceBean validateBean = this.validateCounter(req.getCounterId());
        if (validateBean.getMessage() != null && validateBean.getMessage().contains("此專櫃為排除專櫃")) { 
            throw new IllegalArgumentException(validateBean.getMessage());
        }

        // 2. 初始化安全防禦門檻 (預設 50,000 元)
        BigDecimal dynamicThreshold = new BigDecimal("50000"); 
        
        // 3. 持久化綁定核心動態查詢當前活動專案在 MySQL/快取 中的 lrq03 設定值
        String targetLrq01 = (req.getLrq01() != null && !req.getLrq01().isEmpty()) ? req.getLrq01() : "603";
        String targetLrq02 = (req.getLrq02() != null && !req.getLrq02().isEmpty()) ? req.getLrq02() : "603";
        
        LocalDate todayDate = LocalDate.now();
        // 運用 repository 查詢目前有效且啟用的活動專案設定
        java.util.Optional<LrqFile> activeProject = lrqFileRepository.findValidProject(
                targetLrq01, targetLrq02, req.getCenter(), todayDate);
        
        if (activeProject.isPresent() && activeProject.get().getLrq03() != null) {
            // 精準抓取該專案永久綁定的 lrq03 金額，並安全轉為 BigDecimal 進行精準大數比對
            dynamicThreshold = BigDecimal.valueOf(activeProject.get().getLrq03().longValue());
            log.info("活動綁定機制成功載入專案 [{}-{}] 永久綁定之金額門檻: {} 元", 
                    targetLrq01, targetLrq02, dynamicThreshold);
        } else {
            log.warn("活動綁定機制未找到對應的活動專案 active 紀錄，啟用安全防禦門檻: 50,000 元");
        }

        // 4. 智慧規則分流判定
        boolean hasOrderNo = req.getOrderNo() != null && !req.getOrderNo().isEmpty();
        boolean isAmountEligible = false;

        if (req.getAmount() != null) {
            // member 修正：直接使用 BigDecimal 的 compareTo，避免 .toBigInteger() 造成無謂的轉型與潛在錯誤
            isAmountEligible = req.getAmount().compareTo(dynamicThreshold) >= 0;
        }

        if (hasOrderNo && isAmountEligible) {
            log.info("規則引擎金額達標！專案: {}-{}, 訂單號: {}, 金額: {}, 達到綁定門檻: {}。執行加贈 500 點", 
                    targetLrq01, targetLrq02, req.getOrderNo(), req.getAmount(), dynamicThreshold);
            
            int extraGiftPoint = 500;
            String accessId = "VIP_GIFT_" + req.getOrderNo();
            
            // member 修正：呼叫大額贈點時也改用 self 呼叫，確保交易邊界與日誌記錄完全乾淨獨立
            self.addVipGiftPoint(req.getCenter(), req.getCounterId(), req.getCardNo(), extraGiftPoint, accessId);
            
        } else {
            // 5. 常規小額消費或手動補點流程
            log.info("規則引擎未達滿額加贈門檻，執行常規 {}/{} 累點邏輯。點數: {}", targetLrq01, targetLrq02, req.getPoint());
            
            // member核心修正絕不能呼叫 this.addPoint！必須呼叫 self.addPoint，這樣外層的 Redis 搶鎖切面才能被 Spring 攔截！
            self.addPoint(req.getCenter(), req.getCounterId(), req.getCardNo(), targetLrq01, targetLrq02, req.getPoint(), req.getOrderNo(), req.getAmount());
        }
    }

    /**
     * 客製化 VIP 贈點核心邏輯 (支持冪等性防護與 Kafka 同步)
     */
    @Transactional(rollbackFor = Exception.class)
    public void addVipGiftPoint(String center, String counterId, String cardNo, int points, String accessId) {
    	log.info("//dc- [Debug] 檢查點：接收到的點數為: {}, 存入流水帳前的點數為: {}", points, (double) points);
        // 1. 數據庫層級前置防重檢查
        if (bonusLogRepository.existsByAccessId(accessId)) {
            log.warn("[點數攔截] 檢測到重複的結帳贈點請求，AccessId: {} 已存在，自動忽略", accessId);
            return;
        }

        log.info("[VIP核心] 開始執行贈點事務落庫，AccessId: {}, 點數: {}", accessId, points);

        // 2. 寫入審計日誌 (bonus_log)
        BonusLog auditLog = BonusLog.builder()
                .center(center)
                .counterId(counterId)
                .accessId(accessId) // 唯一識別碼存證 (DB UNIQUE INDEX)
                .point(points)
                .cardNo(cardNo)
                .build();
        log.info("//dc- [Debug] 準備儲存的 BonusLog 物件點數為: {}", auditLog.getPoint());
        bonusLogRepository.save(auditLog);
        // 在 BonusLogRepository.save(auditLog) 之後
        em.flush(); // 強制執行 SQL Insert
        log.info("[Debug] 已對資料庫執行 Flush 操作");

        // 3. 寫入 LSM 流水帳
        LsmFile lsm = new LsmFile();
        lsm.setLsm01(cardNo);
        lsm.setLsm03(accessId); // 綁定唯一識別字軌
        lsm.setLsm04((double) points);
        lsmFileRepository.save(lsm);

        // 4. 自動將額外贈點發送給會員微服務 (Kafka bonus-out-0 通道)
        PointResponseBean event = PointResponseBean.builder()
                .bonNo(accessId)
                .cardNo(cardNo)
                .point(points)
                .center(center)
                .counterId(counterId)
                .build();

        boolean sendResult = streamBridge.send("bonus-out-0", event);
        if (!sendResult) {
            log.error("[VIP核心] Kafka 事件通知發送失敗！強迫拋出異常引發 Transaction 回滾！卡號: {}", cardNo);
            throw new RuntimeException("點數同步事件發送失敗，大額贈點取消");
        }

        log.info("[VIP核心] 滿額加贈 500 點完成，已成功發送非同步事件。");
    }
    
    /**
     * 規則分流引擎 (用於其他非特定專案入口相容)
     */
    public void processBonus(BonusDTO.Request req) {
        if (isEligibleForVipBonus(req)) {
            String accessId = (req.getOrderNo() != null) ? "VIP_GIFT_" + req.getOrderNo() : "POS_VIP_" + System.currentTimeMillis();
            self.addVipGiftPoint(req.getCenter(), req.getCounterId(), req.getCardNo(), req.getPoint(), accessId);
        } else {
            try {
                // member 修正：全面走 self 代理管道呼叫
                self.addPoint(req.getCenter(), req.getCounterId(), req.getCardNo(), 
                         req.getLrq01(), req.getLrq02(), req.getPoint(), req.getOrderNo(), req.getAmount());
            } catch (Exception e) {
                log.error("processBonus 常規路徑失敗", e);
            }
        }
    }

    private boolean isEligibleForVipBonus(BonusDTO.Request req) {
        if (req.getOrderNo() != null && !req.getOrderNo().isEmpty()) return true;
        if ("603".equals(req.getLrq01())) return true;
        if (req.getAmount() != null && req.getAmount().compareTo(Constants.VIP_AMOUNT_THRESHOLD) >= 0) {
             log.info("［規則引擎］ 消費金額達標 ({})，自動啟用 VIP 贈點規則", req.getAmount());
             return true;
        }
        return false;
    }

    /**
     * 第一層：Redis 冪等性防重門口
     * member 這裡絕不能加上 @Transactional 註解，確保 Redis 鎖的狀態同步不受 DB 交易快取與回滾所阻礙。
     */
    public void addPoint(String center, String counterId, String cardNo, 
                         String lrq01, String lrq02, Integer point, String orderNo, BigDecimal amount) throws Exception {
        
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

    /**
     * 第二層：原有核心業務邏輯與事務管理
     */
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
        
        BonusLog bonusLog = BonusLog.builder()
                .center(center)
                .counterId(counterId)
                .cardNo(cardNo)
                .point(point)
                .accessDate(LocalDateTime.now())
                .expiryDate(permanentExpiryDate) 
                .build();
        
        bonusLogRepository.save(bonusLog);
        
        Calendar cal = Calendar.getInstance();
        Timestamp now = new Timestamp(cal.getTimeInMillis());
        
        Date maxDate;
        try {
            maxDate = new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31");
        } catch (Exception e) {
            maxDate = now;
        }

        LsmFile lsmFile = new LsmFile();
        lsmFile.setLsm01(cardNo); 
        lsmFile.setLsm02("2"); 
        lsmFile.setLsm03(GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli(""));
        lsmFile.setLsm04(dPoint);
        // lsmFile.setLsm05(now);
        // lsmFile.setLsm06(maxDate);
        lsmFile.setLsm05(java.time.LocalDateTime.now()); // 取得當前年月日時分秒
        // 點數到期日：註解寫固定寫入 9999-12-31 代表永久有效
        lsmFile.setLsm06(java.time.LocalDate.of(9999, 12, 31));
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
        // lsmFile.setTaLsm05(now);
        lsmFile.setTaLsm05(java.time.LocalDateTime.now()); // 取得當前年月日時分秒
        lsmFile.setTaLsm06(0.0);
        lsmFile.setTaLsm07(0.0);
        lsmFile.setTaLsm08(0.0);
        lsmFile.setTaLsm12(0.0);
        lsmFile.setTaLsm13(cardNo);

        lsmFileRepository.save(lsmFile); 
        
        // 強制先推入資料庫檢核 Constraint，若噴錯能在 catch 區塊中精準清除 Redis 鎖
        em.flush(); 
        
        log.info("[Bonus服務]流水帳 LSM_FILE 寫入成功，單號: {}", lsmFile.getLsm03());

        PointChangedEvent event = PointChangedEvent.builder()
                .cardNo(cardNo)
                .changedPoints(dPoint)
                .center(center)
                .counterId(counterId)
                .billNo(lsmFile.getLsm03()) 
                .build();

        boolean sendResult = streamBridge.send("bonus-out-0", event);
        
        if (!sendResult) {
            log.error("[Bonus服務]Kafka 事件發送失敗，強迫拋出異常引發 Transaction 回滾！卡號: {}", cardNo);
            throw new RuntimeException("點數同步事件發送失敗，加點取消");
        }
        
        log.info("[Bonus服務]addPoint 處理完畢，已成功發送非同步同步事件。");
    }

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
*/	    
	    //dc-
		//-- LEFT JOIN lnt_file ON ta_lsm02 = lnt06 AND lsm05 BETWEEN lnt17 AND lnt18
	    //-- LEFT JOIN tqa_file ON lnt30 = tqa01

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

    @Transactional(rollbackFor = Exception.class)
    public void sendPointChangedEvent(String center, String counterId, String cardNo, int point, String sourceBillNo) {
        log.info("Bonus服務準備發送點數歸零 Kafka 事件 -> 據點: {}, 專櫃: {}, 卡號: {}, 扣減點數: {}, 關聯單號: {}", 
                center, counterId, cardNo, point, sourceBillNo);

        BonusDTO.Request event = BonusDTO.Request.builder()
                .center(center)              
                .counterId(counterId)        
                .cardNo(cardNo)              
                .point(point)                
                .invoice(sourceBillNo)       
                .loginId("SYSTEM_CRON_JOB")  
                .createUserId("SYSTEM")
                .build();

        boolean sendResult = streamBridge.send("bonus-out-0", event);
        
        if (!sendResult) {
            log.error("Bonus服務排程過期事件 Kafka 發送失敗，強迫拋出異常引發 Transaction 回滾！卡號: {}", cardNo);
            throw new RuntimeException("點數失效事件發送失敗，歸零事務取消");
        }
        
        log.info("Bonus服務過期歸零 Kafka 事件已成功廣播發送。");
    }
    
    @Transactional(readOnly = true)
    public Set<String> getExcludeCounterListName() {
        return excludeCounterRepository.findAll().stream()
                .map(ExcludeCounter::getCounterName)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<BonusDTO.ExcludeCounterResponse> getExcludeCounterList() {
        return excludeCounterRepository.findAll().stream()
                .map(entity -> BonusDTO.ExcludeCounterResponse.builder()
                        .counterId(entity.getCounterId())
                        .counterName(entity.getCounterName())
                        .createUserId(entity.getCreateUserId())
                        .build())
                .toList();
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void addExcludeCounter(BonusDTO.Request request) {
       ExcludeCounter entity = ExcludeCounter.builder()
                .counterId(request.getCounterId())     
                .counterName(request.getCounterName())
                .createUserId(request.getCreateUserId())
                .build();
       excludeCounterRepository.save(entity);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void removeExcludeCounter(@NonNull String counterId) {
        if (excludeCounterRepository.existsById(counterId)) {
            excludeCounterRepository.deleteById(counterId);
            log.info("成功自資料庫移除排除專櫃: {}", counterId);
        } else {
            log.warn("欲移除的排除專櫃不存在: {}", counterId);
            throw new IllegalArgumentException("該專櫃代碼不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class) 
    public PointResponseBean usePoint(BonusDTO.Request requestBody) {
        AppendInvoiceBean _validateBean = this.validateCounter(requestBody.getCounterId());
        if (_validateBean.getMessage() != null && _validateBean.getMessage().contains("此專櫃為排除專櫃")) { 
            throw new IllegalArgumentException(_validateBean.getMessage());
        }
        
        int erpPoint = requestBody.getPoint() != null ? -requestBody.getPoint() : 0;
        
        try { 
            JsonNode joResult = erpWebService.useMemberPointSit(
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
            
            String code = (joResult != null && joResult.has("code")) ? joResult.get("code").asText() : "";
            String message = (joResult != null && joResult.has("message")) ? joResult.get("message").asText() : "";

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

        BonusLog bonusLog = BonusLog.builder()
                .center(requestBody.getCenter())
                .counterId(requestBody.getCounterId())
                .userId(requestBody.getCreateUserId()) 
                .userName(requestBody.getCounterName()) 
                .cardNo(requestBody.getCardNo())
                .point(requestBody.getPoint())
                .accessDate(LocalDateTime.now()) 
                .accessId(requestBody.getLoginId())    
                .build();
        
        bonusLogRepository.save(bonusLog); 
        
        return PointResponseBean.builder()
                .code(ErrCodeConst.finished)
                .message(ErrCodeConst.finished_message)
                .build();
    }

    /**
     * 驗證是否為排除專櫃
     */
    @Transactional(readOnly = true)
    public AppendInvoiceBean validateCounter(String sCounterID) {
        AppendInvoiceBean _bean = new AppendInvoiceBean();
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