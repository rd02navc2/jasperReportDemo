package com.beyoung.parking.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beyoung.parking.api.client.MemberServiceClient;
import com.beyoung.parking.domain.bean.AppendInvoiceBean;
import com.beyoung.parking.domain.bean.PointResponseBean;
import com.beyoung.parking.domain.dto.ParkingDTO;
import com.beyoung.parking.domain.dto.PointChangedEvent;
import com.beyoung.parking.domain.entity.ExcludeCounter;
import com.beyoung.parking.infrastructure.ParkingLogRepository;
import com.beyoung.parking.infrastructure.ExcludeCounterRepository;
import com.beyoung.parking.infrastructure.LrqFileRepository;
import com.beyoung.parking.infrastructure.LsmFile;
import com.beyoung.parking.infrastructure.LsmFileRepository;
import com.beyoung.parking.util.ERPWebService;
import com.beyoung.parking.util.ErrCodeConst;
import com.beyoung.parking.util.GetDateTime;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 點數與排除專櫃商業邏輯層
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingService {

    private final LrqFileRepository lrqFileRepository;
    private final LsmFileRepository lsmFileRepository;
    
    private final ExcludeCounterRepository excludeCounterRepository;
    private final ParkingLogRepository parkingLogRepository;  
    private final ERPWebService erpWebService;

    @Value("${erp.ws.url}") 
    private String erpUrl;
    
    //dc-
    private final StreamBridge streamBridge;
    @PersistenceContext
    private final EntityManager em;
    
    // 2. 注入 Feign Client 工具
    private final MemberServiceClient memberServiceClient;
 
 /*
    @Transactional(readOnly = true)
    public List<LsmFile> getPointHistByMemberID(String sMemberID, String sStartDate, String sEndDate) {
        // 1. 先向 member 服務索取該會員的卡號清單 (cardNos)
        // 2. 執行本地 SQL 查詢 lsm_file
        String sql = "SELECT ... FROM lsm_file WHERE lsm01 IN (:cardNos) ...";
        return (List<LsmFile>) em.createNativeQuery(sql, "LsmFile_Hist")
                .setParameter("cardNos", cardNos)
                .setParameter("startDate", sStartDate)
                .setParameter("endDate", sEndDate)
                .getResultList();
    }
 */   
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<LsmFile> getPointHistByMemberID(String sMemberID, String sStartDate, String sEndDate) {
        
        log.info("parking服務開始查詢點數歷程，會員ID: {}", sMemberID);

        // 3. 真實實作透過 FeignClient 向 member 微服務請求卡號清單
        List<String> cardNos;
        try {
            cardNos = memberServiceClient.getCardNumbersByMemberId(sMemberID);
        } catch (Exception e) {
            log.error("parking服務遠端呼叫 Member 服務失敗，會員ID: {}", sMemberID, e);
            // 依業務邏輯決定是拋出異常還是回傳空清單，此處選擇容錯回傳空清單
            return Collections.emptyList();
        }
        
        // 如果該會員沒有綁定任何卡號，直接返回空陣列，避免 SQL 語法錯誤 (IN 空集合會報錯)
        if (cardNos == null || cardNos.isEmpty()) {
            log.warn("parking服務該會員 ID : {} 查無任何關聯卡號", sMemberID);
            return Collections.emptyList();
        }

        // 4. 帶入查出來的 cardNos 執行本地點數流水帳 Native SQL 查詢
        String sql = """
                SELECT lsmstore, lsm01, lsm02, lsm04, lsm05, lsm08,
                       ta_lsm02, ta_lsm09,
                       CASE WHEN ta_lsm02 = 'EC' THEN 'beyond beyond' ELSE tqa02 END AS tqa02,
                       ta_lsm04
                FROM lsm_file
                LEFT JOIN lnt_file ON ta_lsm02 = lnt06
                  AND lsm05 BETWEEN lnt17 AND lnt18
                LEFT JOIN tqa_file ON lnt30 = tqa01
                WHERE 1=1
                  AND lsm01 IN (:cardNos) -- 綁定動態卡號清單
                  AND lsm05 BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD')
                                AND TO_DATE(:endDate,   'YYYY-MM-DD')
                  AND lsm02 IN ('2','5','7','8','9','B')
                ORDER BY (TO_CHAR(lsm05,'yyyy-MM-dd') || RPAD(ta_lsm04, 20, '0')) DESC
                """;

        return (List<LsmFile>) em.createNativeQuery(sql, "LsmFile_Hist")
                .setParameter("cardNos",   cardNos) // 傳入 List<String>，JPA 會自動轉成 Oracle 的 IN ('卡號1', '卡號2')
                .setParameter("startDate", sStartDate)
                .setParameter("endDate",   sEndDate)
                .getResultList();
    }

    /**
     * 查詢排除清單
     */
    @Transactional(readOnly = true)
    public Set<String> getExcludeCounterListName() {
        return excludeCounterRepository.findAll().stream()
                .map(ExcludeCounter::getCounterName)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<ParkingDTO.ExcludeCounterResponse> getExcludeCounterList() {

        return excludeCounterRepository.findAll().stream()
                .map(entity -> ParkingDTO.ExcludeCounterResponse.builder()
                        .counterId(entity.getCounterId())
                        .counterName(entity.getCounterName())
                        .createUserId(entity.getCreateUserId())
                        .build())
                .toList();
    }
    
    /**
     * 新增排除專櫃
     */
    @Transactional(rollbackFor = Exception.class)
    public void addExcludeCounter(ParkingDTO.Request request) {
       
        ExcludeCounter entity = ExcludeCounter.builder()
                .counterId(request.getCounterId())     
                .counterName(request.getCounterName())
                .createUserId(request.getCreateUserId())
                .build();

       excludeCounterRepository.save(entity);
    }
    
    /**
     * 移除排除專櫃
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeExcludeCounter(@NonNull String counterId) {
        // 檢查資料庫是否存在該專櫃代碼，存在才刪除（或是直接 deleteById）
        if (excludeCounterRepository.existsById(counterId)) {
            excludeCounterRepository.deleteById(counterId);
            log.info("成功自資料庫移除排除專櫃: {}", counterId);
        } else {
            log.warn("欲移除的排除專櫃不存在: {}", counterId);
            // 視專案需求，也可以選擇拋出客製化 Exception 讓 Controller 捕捉
            throw new IllegalArgumentException("該專櫃代碼不存在");
        }
    }

    
    @Transactional(rollbackFor = Exception.class) // 若 ERP 成功但 DB 失敗，可依業務需求決定是否回滾
    public PointResponseBean usePoint(ParkingDTO.Request requestBody) {
        
    	AppendInvoiceBean _validateBean = this.validateCounter(requestBody.getCounterId());
        
        // 檢查是否包含排除專櫃的錯誤訊息（比對邏輯完全對齊舊系統）
        // 備註：若新系統 ErrCodeConst 已經轉型，包含比對也可以直接改成 _validateBean.getCode().equals("E001")
        if (_validateBean.getMessage() != null 
                && _validateBean.getMessage().contains("此專櫃為排除專櫃")) { // 或使用舊的 ErrCodeConst.append_exclude_counter_message
            
            // 拋出 Exception 以便觸發 @Transactional 的 rollback
            throw new IllegalArgumentException(_validateBean.getMessage());
        }
        
        // 1. 呼叫遠端 ERP WebService (點數負號轉置)
        int erpPoint = requestBody.getPoint() != null ? -requestBody.getPoint() : 0;
        
        try { //dc- ERP 連線或解析異常 5/29 資安因素只能連 SIT 測試環境
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
            
            log.info("parking：usePoint Response：{}", joResult != null ? joResult.toString() : "null");
            
            // 2. 判斷 ERP 回傳代碼是否成功
            String code = (joResult != null && joResult.has("code")) ? joResult.get("code").asText() : "";
            String message = (joResult != null && joResult.has("message")) ? joResult.get("message").asText() : "";

            if (!"0".equals(code)) {
                log.error("卡號：{}，遠端 ERP 錯誤 {}:{}", requestBody.getCardNo(), code, message);
                return PointResponseBean.builder()
                        .code(ErrCodeConst.pos_rs_erp_ws)
                        .message(ErrCodeConst.pos_rs_erp_ws_message)
                        .build();
            }
            
        } catch (Exception e) {
            log.error("ERP 連線或解析異常", e);
            // 這裡視業務決定要直接中斷丟出 Exception，還是回傳特定錯誤代碼
            throw new RuntimeException("ERP WebService 呼叫失敗", e);
        }

        // 3. 寫入操作日誌 (JPA Repository)
        com.beyoung.parking.domain.entity.ParkingLog parkingLog = com.beyoung.parking.domain.entity.ParkingLog.builder()
                .center(requestBody.getCenter())
                .counterId(requestBody.getCounterId())
                .userId(requestBody.getCreateUserId()) 
                .userName(requestBody.getCounterName()) 
                .cardNo(requestBody.getCardNo())
                .point(requestBody.getPoint())
                .accessDate(LocalDateTime.now()) 
                .accessId(requestBody.getLoginId())    
                .build();
        
        parkingLogRepository.save(parkingLog); 
        
        // 4. 回傳成功狀態
        return PointResponseBean.builder()
                .code(ErrCodeConst.finished)
                .message(ErrCodeConst.finished_message)
                .build();
    }

    @Transactional(rollbackFor = Exception.class) // 事務管理，發生異常時全數回滾
    public synchronized void addPoint(String center, String counterId, String cardNo, Integer point) {
        
        // [驗證專櫃邏輯] 完全對齊您原有的防錯機制
        AppendInvoiceBean _validateBean = this.validateCounter(counterId);
        if (_validateBean.getMessage() != null 
                && _validateBean.getMessage().contains("此專櫃為排除專櫃")) { 
            throw new IllegalArgumentException(_validateBean.getMessage());
        }
        
        // 查詢 LRQ 活動設定（屬於 parking 本地領域）
        Date today = new Date();      

        // 1. 查詢 LRQ 活動設定上限
        Integer lrq03 = lrqFileRepository.findLrq03("603", center, today).orElse(100);

        // 2. 轉換前端傳入點數
        double dPoint = point != null ? point.doubleValue() : 0.0;

        // 3. 核心修正：安全閥攔截邏輯
        /* 「專櫃人員可以根據客訴狀況彈性補點（例如發票少累積 20 點就補 20 點），但是為了防弊，手動補點最高不能超過行銷活動設定的上限（例如 100 點）。」 */
        if (dPoint > lrq03.doubleValue()) {
            log.error("parking服務補點失敗！輸入點數 {} 超過目前活動限制上限 {}", dPoint, lrq03);
            throw new IllegalArgumentException("補點點數已超過當前行銷活動上限（最高 " + lrq03 + " 點）");
        }
        
        // 準備時間參數
        Calendar cal = Calendar.getInstance();
        Timestamp now = new Timestamp(cal.getTimeInMillis());
        
        Date maxDate;
        try {
            maxDate = new SimpleDateFormat("yyyy-MM-dd").parse("9999-12-31");
        } catch (Exception e) {
            maxDate = now;
        }

        // 建立並儲存流水帳 (LSM_FILE) - 權責歸屬 parking
        LsmFile lsmFile = new LsmFile();
        lsmFile.setLsm01(cardNo); // 直接使用傳入的會員卡號
        lsmFile.setLsm02("2");
        lsmFile.setLsm03(GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli(""));
        lsmFile.setLsm04(dPoint);
        lsmFile.setLsm05(now);
        lsmFile.setLsm06(maxDate);
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
        lsmFile.setTaLsm05(now);
        
        // 因為此處不查 LPJ_FILE，異動前快照點數設為 0.0（後續對帳由 member 端事件或對帳檔為主）
        lsmFile.setTaLsm06(0.0);
        lsmFile.setTaLsm07(0.0);
        lsmFile.setTaLsm08(0.0);
        
        lsmFile.setTaLsm12(0.0);
        lsmFile.setTaLsm13(cardNo);

        lsmFileRepository.save(lsmFile); // JPA 自動生成 Insert SQL
        log.info("parking服務流水帳 LSM_FILE 寫入成功，單號: {}", lsmFile.getLsm03());

        // 5. 改造取代不再呼叫 lpjFileRepository.updateMemberPoints
        // 改為包裝事件發送給 Kafka
        PointChangedEvent event = PointChangedEvent.builder()
                .cardNo(cardNo)
                .changedPoints(dPoint)
                .center(center)
                .counterId(counterId)
                .build();

        // 發送訊息至 yml 設定中的 parking-out-0 通道 (對應 member-point-topic)
        boolean sendResult = streamBridge.send("parking-out-0", event);
        
        if (!sendResult) {
            log.error("parking服務Kafka 事件發送失敗，強迫拋出異常引發 Transaction 回滾！卡號: {}", cardNo);
            throw new RuntimeException("點數同步事件發送失敗，加點取消");
        }
        
        log.info("parking服務addPoint 處理完畢，已成功發送非同步同步事件。");
    }

    
    @Transactional(rollbackFor = Exception.class)
    public AppendInvoiceBean validateCounter(String sCounterID) {
        AppendInvoiceBean _bean = new AppendInvoiceBean();
        
        // 1. 預設為成功狀態 (對應 ErrCodeConst.finished -> "0")
        _bean.setCode("0");
        _bean.setMessage("finished");

        try {
            // 2. 直接利用 JPA 的 existsById 檢查該專櫃代碼是否存在於排除名單中
            // 效能極高，等同於執行 SELECT COUNT(1) FROM exclude_counter_file WHERE s_counter_id = ?
            boolean isExclude = excludeCounterRepository.existsById(sCounterID);

            if (isExclude) {
                // 3. 若存在，代表該專櫃被排除，設定對應的失敗代碼與訊息
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