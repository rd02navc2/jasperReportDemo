package com.beyond.surrounding.invoice.service;

import com.beyond.surrounding.dto.AppendInvoiceDTO;
import com.beyond.surrounding.invoice.dto.InvoiceDTO;
import com.beyond.surrounding.app.bean.AppendInvoiceBean;
import com.beyond.surrounding.app.bean.InvoiceBean;
import com.beyond.surrounding.app.client.ChiefPayFeignClient;
import com.beyond.surrounding.bonus.service.HiefPayService;
import com.beyond.surrounding.app.entity.LPJ_FILE;
import com.beyond.surrounding.app.entity.LpjProjection;
import com.beyond.surrounding.app.entity.LPL_FILE;
import com.beyond.surrounding.app.entity.LRQ_FILE;
import com.beyond.surrounding.app.entity.LSM_FILE;
import com.beyond.surrounding.app.entity.TC_PSA_FILE;
import com.beyond.surrounding.app.entity.TC_PSB_FILE;
import com.beyond.surrounding.app.entity.TcPsbProjection;
import com.beyond.surrounding.app.entity.TC_PSC_FILE;
import com.beyond.surrounding.app.entity.TcPscProjection;
import com.beyond.surrounding.invoice.repository.ExcludeCounterRepository;
import com.beyond.surrounding.invoice.repository.InvoiceRepository;
import com.beyond.surrounding.invoice.repository.LsmFileRepository;
import com.beyond.surrounding.invoice.repository.LpjFileRepository;
import com.beyond.surrounding.invoice.repository.LplFileRepository;
import com.beyond.surrounding.invoice.repository.LrqFileRepository;
import com.beyond.surrounding.invoice.repository.TcPsbRepository;
import com.beyond.surrounding.invoice.repository.TcPscRepository;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 點數與排除專櫃商業邏輯層
 * 已全面重構升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final LsmFileRepository lsmFileRepository;
    private final LpjFileRepository lpjFileRepository;
    private final LplFileRepository lplFileRepository; 
    private final LrqFileRepository lrqFileRepository; 
    private final TcPsbRepository tcPsbRepository;  
    private final TcPscRepository tcPscRepository;  
    private final ExcludeCounterRepository excludeCounterRepository;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;
    private final ChiefPayFeignClient chiefPayClient;
    
    @Autowired 
    private HiefPayService hiefPayService;
    
   
    @Value("${app.chiefpay.url:http://localhost:8096}")
    private String chiefPayBaseUrl;
    
    
    @Transactional(rollbackFor = Exception.class)
    public void addPoint(String center, String counterId, String cardNo, double point) throws Exception {

        // 1. 查詢會員卡資訊
        List<Object[]> listBean1 = lpjFileRepository.findLpjInfo(cardNo, "2");
        if (listBean1.isEmpty()) {
            throw new Exception("無此會員卡號：" + cardNo);
        }
        
        // 解析 Native SQL 回傳的陣列欄位 (對齊你 select 的欄位順序)
        Object[] row = listBean1.get(listBean1.size() - 1);
        LPJ_FILE bean1 = new LPJ_FILE();
        bean1.setLpj03((String) row[0]);
        // row[1] 是 LPK04
        bean1.setLpj12(row[2] != null ? ((Number) row[2]).doubleValue() : 0.0);
        bean1.setTaLpj01(row[3] != null ? ((Number) row[3]).doubleValue() : 0.0);
        bean1.setTaLpj02(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0);
        bean1.setTaLpj03(row[5] != null ? ((Number) row[5]).doubleValue() : 0.0);

        // 2. 查詢 LRQ 設定點數
        int lrqPoint = 100;
        List<Integer> listLrq = lrqFileRepository.findLrqPoint("603", center);
        for (Integer recordPoint : listLrq) {
            if (recordPoint != null) {
                lrqPoint = recordPoint;
            }
        }

        // ==========================================================================================================
        Calendar cal = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 3. 新增 LSM_FILE (點數明細)
        lsmFileRepository.insertLsmDetail(
            bean1.getLpj03(),
            "2",
            GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli(""),
            point,
            timestamp,
            sdf.parse("9999-12-31"),
            0.0, center, center, 0,
            0.0, 0.0, 0.0, 0.0, "1",
            center, null, null, "補贈點", counterId,
            "", GetDateTime.getTimeMilli(), timestamp, bean1.getTaLpj01(), bean1.getTaLpj02(),
            bean1.getTaLpj03(), 0.0, cardNo
        );

        // 4. 更新 LPJ_FILE (累計點數)
        lpjFileRepository.updateLpjPoint(timestamp, point, cardNo, "2");
    }  

    /**
     * 新增臨時會員
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTempMember(String sCenter, String sMemberID) {
        log.info("Service 新增臨時會員: sCenter={}, sMemberID={}", sCenter, sMemberID);
        try {
            invoiceRepository.addTempMember(sCenter, sMemberID);
        } catch (Exception e) {
            log.error("addTempMember 發生錯誤: ", e);
            throw new RuntimeException("資料庫寫入臨時會員失敗: " + e.getMessage());
        }
    }

    /**
     * 1. 取得使用者發票清單
     */
    /**
     * 1. 取得使用者發票清單 (重構整合版)
     * @param sMemberID 會員 ID (對應舊系統的 lpj01 關聯鍵)
     * @param sDate 前端傳入的日期字串 (預期格式：yyyy-MM-dd)
     * @return List<TcPsaFile> 封裝好的 JPA 實體清單
     */
    @Transactional(readOnly = true)
    public List<TC_PSA_FILE> getUserInvoice(String sMemberID, Date sDate) {
        log.info("Service 讀取使用者發票: memberId={}, date(String)={}", sMemberID, sDate);
        
        // 防禦性程式碼：檢查傳入參數是否為空
        if (sMemberID == null || sMemberID.trim().isEmpty() || sDate == null ) {
            log.warn("getUserInvoice 拒絕請求：參數不可為空值");
            throw new IllegalArgumentException("查詢參數（會員ID與日期）不可為空");
        }

        try {
            // 在 Service 層將 String 安全解析為 LocalDate，完全與底層資料庫 Oracle to_date 函數脫鉤
            // 如果舊前端傳進來的是 yyyyMMdd (如 "20260611")，請改用 LocalDate.parse(sDate, DateTimeFormatter.ofPattern("yyyyMMdd"))
            // Date localDate = java.time.LocalDate.parse(sDate);
            
            log.debug("成功將字串轉換為 LocalDate: {}，開始調用 Repository", sDate);
            
            // 呼叫帶有 LocalDate 參數的 JPQL Repository 方法
            return invoiceRepository.getUserInvoice(sMemberID, sDate);
            
        } catch (java.time.format.DateTimeParseException e) {
            log.error("日期格式解析失敗，預期格式為 yyyy-MM-dd, 實際收到: {}", sDate);
            throw new IllegalArgumentException("日期格式錯誤，請使用正確的 yyyy-MM-dd 格式 (例如: 2026-06-11)");
        } catch (Exception e) {
            log.error("getUserInvoice 執行期間發生未預期錯誤: ", e);
            throw new RuntimeException("無法讀取發票清單: " + e.getMessage(), e);
        }
    }

    /**
     * 2. 驗證發票是否存在或重置 (100% 還原舊系統 Hibernate 檢核風控邏輯)
     * @param sInvoiceNo 發票號碼
     * @param sRandomNo  隨機碼 (若為 "uncheck" 則免檢金額，否則傳入金額字串對齊 tc_psa31)
     */
    @Transactional(readOnly = true)
    public AppendInvoiceBean validate(String sInvoiceNo, String sRandomNo) {
        AppendInvoiceBean bean = new AppendInvoiceBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        // 1. 查詢 TC_PSA_FILE (建議改用 Repository，這裡保留原生 SQL 概念)
        String sql = "SELECT tc_psa01, tc_psa04, tc_psa13, tc_psa06 FROM tc_psa_file " +
                     "WHERE tc_psa16||tc_psa17 = :invoiceNo " +
                     ("uncheck".equals(sRandomNo) ? "" : "AND tc_psa31 = :randomNo");

        var query = entityManager.createNativeQuery(sql, TC_PSA_FILE.class);
        query.setParameter("invoiceNo", sInvoiceNo);
        if (!"uncheck".equals(sRandomNo)) {
            query.setParameter("randomNo", sRandomNo);
        }

        @SuppressWarnings("unchecked")
        List<TC_PSA_FILE> results = query.getResultList();

        if (results.isEmpty()) {
        	bean.setCode(ErrCodeConst.pos_not_found);
        	bean.setMessage(ErrCodeConst.pos_not_found_message);
        	return bean;
        }

        // 2. 驗證邏輯
        TC_PSA_FILE record = results.get(0);
        
        if ("00002".equals(record.getTcPsa01())) {
        	bean.setCode(ErrCodeConst.append_exclude_counter);
        	bean.setMessage(ErrCodeConst.append_exclude_counter_message + "，專櫃代碼：" + record.getTcPsa01());
            return bean;
        }
        
        if ("02".equals(record.getTcPsa06()) || "03".equals(record.getTcPsa06())) {
        	bean.setCode(ErrCodeConst.pos_refund);
        	bean.setMessage(ErrCodeConst.pos_refund_message);
            return bean;
        }

        // 檢查是否已使用或過期
        if (record.getTcPsa13() == null || record.getTcPsa13().trim().isEmpty()) {
            if (new Date().after(GetDateTime.getNewDate(record.getTcPsa04(), 8))) {
            	bean.setCode(ErrCodeConst.expired);
            	bean.setMessage(ErrCodeConst.expired_message);
                return bean;
            }
        } else {
        	bean.setCode(ErrCodeConst.pos_used);
        	bean.setMessage(ErrCodeConst.pos_used_message);
            return bean;
        }

        // 3. 檢查 LSM_FILE
        String lsmSql = "SELECT ta_lsm09 FROM lsm_file WHERE lsm02 IN ('2','7') AND ta_lsm09 = :invoiceNo";
        List<?> lsmResults = entityManager.createNativeQuery(lsmSql)
                                         .setParameter("invoiceNo", sInvoiceNo)
                                         .getResultList();

        if (!lsmResults.isEmpty()) {
        	bean.setCode(ErrCodeConst.erp_used);
        	bean.setMessage(ErrCodeConst.erp_used_message + "(" + sInvoiceNo + ")");
        }

        return bean;
    }
     
    @Transactional(readOnly = true)
    public AppendInvoiceDTO validate2(String invoiceNo, String randomNo, String counterId) throws Exception {
        log.info("Service 驗證發票 (100% 舊 ERP 規格還原): invoiceNo={}, randomNo/Price={}", invoiceNo, randomNo);
        
        
        // 1. 先進行專櫃風控檢查
        AppendInvoiceDTO counterBean = this.validateCounter(counterId);
        
        // 2. 檢查專櫃檢查結果是否包含錯誤 (直接攔截)
        if (counterBean.getMessage() != null && counterBean.getMessage().contains("此專櫃為排除專櫃")) {
            log.warn("檢測到排除專櫃: {}", counterId);
            return counterBean; // 直接回傳錯誤結果，不進行後續動作
        }

        // 3. 只有通過專櫃檢查，才初始化正常的發票物件
        AppendInvoiceDTO _bean = new AppendInvoiceDTO();
        _bean.setCode(ErrCodeConst.finished);
        _bean.setMessage(ErrCodeConst.finished_message);
        
        try {
            List<TC_PSA_FILE> _lBean;
            
            // ==========================================
            // 步驟一：Check tc_psa_file (對齊舊系統單據與金額條件)
            // ==========================================
            if ("uncheck".equals(randomNo)) {
                _lBean = invoiceRepository.validateInvoiceUncheck(invoiceNo);
            } else {
                //  修正：舊系統在此直接將字串作為 tc_psa31 參數比對，此處改為符合實體型態或對齊 Repository 規格
                // 如果您的 Repository 參數宣告是 String，就直接傳 sRandomNo；如果是 Double，則轉型。
                // 這裡假設對齊您 Repository 之前的宣告傳入 Double
                Double dTotalPrice = Double.parseDouble(randomNo);
                _lBean = invoiceRepository.validateInvoiceCheck(invoiceNo, dTotalPrice);
            }

            // 發票不存在防線
            if (_lBean == null || _lBean.isEmpty()) { 
                _bean.setCode(ErrCodeConst.pos_not_found);
                _bean.setMessage(ErrCodeConst.pos_not_found_message);
                return _bean;
            }

            // 走訪明細，精準還原專櫃排除與退貨攔截
            TC_PSA_FILE _bean1 = new TC_PSA_FILE();
            for (TC_PSA_FILE record : _lBean) {
                _bean1 = record;
                
                //dc-
                /*
                // 風控 1：排除特定專櫃（00002）
                if ("00002".equals(_bean1.getTcPsa01())) {
                    _bean.setCode(ErrCodeConst.append_exclude_counter);
                    _bean.setMessage(ErrCodeConst.append_exclude_counter_message + "，專櫃代碼：" + _bean1.getTcPsa01());
                    return _bean;
                }
                */
                                
                // 風控 2：檢查是否為退貨/換貨單據 ("02" 或 "03")
                if ("02".equals(_bean1.getTcPsa06()) || "03".equals(_bean1.getTcPsa06())) {
                    _bean.setCode(ErrCodeConst.pos_refund);
                    _bean.setMessage(ErrCodeConst.pos_refund_message);
                    return _bean;
                }
            }

            // ==========================================
            //  重大修正：還原舊系統關於 tc_psa13 的 POS 綁定風控與 8 天過期規則
            // ==========================================
            if (_bean1.getTcPsa13() == null || _bean1.getTcPsa13().trim().isEmpty()) { 
                // 發票尚未被前台 POS 綁定過會員，進一步檢查是否已超過 8 天限制
                java.util.Date tcPsa04Date = _bean1.getTcPsa04();
                
                if (tcPsa04Date != null) {
                    // 1. 建立當前日期的 Calendar，並將時分秒歸零 (以便純日期比較)
                    Calendar todayCal = Calendar.getInstance();
                    todayCal.set(Calendar.HOUR_OF_DAY, 0);
                    todayCal.set(Calendar.MINUTE, 0);
                    todayCal.set(Calendar.SECOND, 0);
                    todayCal.set(Calendar.MILLISECOND, 0);

                    // 2. 建立發票日期的 Calendar，設定為發票日期
                    Calendar invoiceCal = Calendar.getInstance();
                    invoiceCal.setTime(tcPsa04Date);
                    invoiceCal.set(Calendar.HOUR_OF_DAY, 0);
                    invoiceCal.set(Calendar.MINUTE, 0);
                    invoiceCal.set(Calendar.SECOND, 0);
                    invoiceCal.set(Calendar.MILLISECOND, 0);

                    // 3. 發票日期 + 8 天
                    invoiceCal.add(Calendar.DAY_OF_YEAR, 8);

                    // 4. 比較：如果今天 > (發票日期 + 8天)
                    if (todayCal.after(invoiceCal)) {
                        _bean.setCode(ErrCodeConst.expired);
                        _bean.setMessage(ErrCodeConst.expired_message);
                        return _bean;
                    }
                } else {
                    _bean.setCode(ErrCodeConst.invalid_date);
                    _bean.setMessage("發票日期無效");
                    return _bean;
                }
            } else { 
                // tc_psa13 不為空，代表前台已經綁定過了
                _bean.setCode(ErrCodeConst.pos_used);
                _bean.setMessage(ErrCodeConst.pos_used_message);    
                return _bean;
            }

            // ==========================================
            // 步驟二：Check lsm_file (對齊舊系統 ta_lsm09 / lsm02 條件)
            // ==========================================
            //  修正：呼叫特化後的 LsmFileRepository 做中台/ERP 重複補登查驗
            boolean isUsedInErp = lsmFileRepository.isInvoiceUsedInErp(invoiceNo);
            if (isUsedInErp) { 
                _bean.setCode(ErrCodeConst.erp_used);
                _bean.setMessage(ErrCodeConst.erp_used_message + "(" + invoiceNo + ")");
            }       
            
            return _bean;

        } catch (NumberFormatException e) {
            log.error("金額格式轉換失敗 (sRandomNo 傳入非數字字串): {}", randomNo);
            _bean.setCode("4001");
            _bean.setMessage("金額格式不正確");
            return _bean;
        } catch (Exception e) {
            log.error("validate 執行期發生崩潰: ", e);
            throw new RuntimeException("發票驗證邏輯異常: " + e.getMessage());
        }
    }

    /**
     * 3. 核心商業邏輯：發票點數補登
     * 更名並加入 HttpServletRequest 參數以對接 Controller
     */
    @Transactional
    public synchronized AppendInvoiceBean appendInvoice(String memberId, String cardId, String invoiceNo, String randomNo) {                                                       

        log.info("開始執行發票補登: memberId={}, invoiceNo={}, randomNo={}", memberId, invoiceNo, randomNo);

        // ── Step 1. 查詢發票主檔 (TC_PSA_FILE) ──────────────────────────────
        List<TC_PSA_FILE> psaList = invoiceRepository.findInvoice(invoiceNo, randomNo);
        if (psaList == null || psaList.isEmpty()) {
            throw new RuntimeException("The invoice number(" + invoiceNo + ") cannot be found in POS");
        }
        TC_PSA_FILE psa = psaList.get(psaList.size() - 1); // 遵從舊系統取最後一筆

        // ── Step 2. 查詢發票商品明細 (TC_PSB_FILE) ───────────────────────────
        List<TcPsbProjection> projections = tcPsbRepository.findByInvoice(
                psa.getTcPsaplant(), psa.getTcPsa01(), psa.getTcPsa02(),
                psa.getTcPsa03(), psa.getTcPsa04());
        
        List<TC_PSB_FILE> psbList = projections.stream().map(p -> {
        	TC_PSB_FILE file = new TC_PSB_FILE();
            file.setTcPsbplant(p.getTcPsbplant());
            file.setTcPsb01(p.getTcPsb01());
            file.setTcPsb02(p.getTcPsb02());
            file.setTcPsb03(p.getTcPsb03());
            file.setTcPsb04(p.getTcPsb04());
            file.setTcPsb06(p.getTcPsb06());
            file.setTcPsb05(p.getTcPsb05());
            file.setTcPsb07(p.getTcPsb07());
            file.setTcPsb08(p.getTcPsb08());
            file.setTcPsb09(p.getTcPsb09());
            file.setTcPsb10(p.getTcPsb10());
            file.setTcPsb11(p.getTcPsb11());
            file.setTcPsb12(p.getTcPsb12());
            file.setTcPsb13(p.getTcPsb13());
            file.setTcPsb13A(p.getTcPsb13a());
            file.setTcPsb13B(p.getTcPsb13b());
            file.setTcPsb14(p.getTcPsb14());
            file.setTcPsb15(p.getTcPsb15());
            file.setTcPsb16(p.getTcPsb16());
            file.setTcPsb17(p.getTcPsb17());
            file.setTcPsb18(p.getTcPsb18());
            file.setTcPsb19(p.getTcPsb19());
            file.setTcPsb20(p.getTcPsb20());
            file.setTcPsb21(p.getTcPsb21());
            file.setTcPsb22(p.getTcPsb22());
            file.setTcPsb23(p.getTcPsb23());
            file.setIma25(p.getIma25()); 
            file.setLnt04(p.getLnt04()); 
            return file;
        }).collect(Collectors.toList());

        // ── Step 3. 查詢付款方式明細-信用卡 (TC_PSC_FILE) ────────────────────
        List<TcPscProjection> pscList = tcPscRepository.findCreditCard(
                psa.getTcPsaplant(), psa.getTcPsa01(), psa.getTcPsa02(),
                psa.getTcPsa03(), psa.getTcPsa04());

        TcPscProjection psc = pscList.stream().reduce((a, b) -> b).orElse(null);
        TC_PSC_FILE pscFile = new TC_PSC_FILE();
        if (psc != null) {
            pscFile.setTcPsc07(psc.getTcPsc07());
            pscFile.setTcPsc08(psc.getTcPsc08());
        }
        
        // ── Step 4. 查詢會員資料 (LPJ_FILE) ──────────────────────────────────
        List<LpjProjection> lpjList = lpjFileRepository.findActiveMember(memberId, "2");
        if (lpjList == null || lpjList.isEmpty()) {
            throw new RuntimeException("MemberID(" + memberId + ") cannot be found in LPJ");
        }
        LpjProjection lpj = lpjList.get(lpjList.size() - 1);

        // ── Step 5. 查詢積點換算基準率 (LRQ_FILE) ───────────────────────────
        int pointBase = 100; 
        List<LRQ_FILE> lrqList = lrqFileRepository.findActiveRate("601", psa.getTcPsaplant()); 
        if (lrqList != null && !lrqList.isEmpty()) {
            pointBase = lrqList.get(lrqList.size() - 1).getLrq03();
        }

        // ── Step 6. 計算本次發放點數 ──────────────────────────────────────────
        double consumeAmt = psa.getTcPsa40() != null ? psa.getTcPsa40() : 0.0;
        double taLpj01 = (lpj.getTaLpj01() != null) ? lpj.getTaLpj01() : 0.0;
        double earnedPoint = Math.floor(consumeAmt / pointBase) * taLpj01;

        // ── Step 7. 寫入 LSM_FILE (點數異動歷史明細) ──────────────────────────
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        Date cleanDate = (psa.getTcPsa04() != null) ? new Date(psa.getTcPsa04().getTime()) : new Date();

        Date expiryDate;
        try {
            expiryDate = sdf.parse("9999-12-31");
        } catch (Exception e) {
            expiryDate = now;
        }

        LSM_FILE lsm = LSM_FILE.builder()
                .lsm01(lpj.getLpj03())
                .lsm02("2")
                .lsm03(psa.getTcPsa02() + sdf1.format(psa.getTcPsa04()) + psa.getTcPsa03())
                .lsm04(earnedPoint)
                .lsm05(cleanDate)
                .lsm06(expiryDate)
                .lsm08(consumeAmt)
                .lsmlegal(psa.getTcPsaplant())
                .lsmplant(psa.getTcPsaplant())
                .lsm09(0).lsm10(0.0).lsm11(0.0).lsm12(0.0).lsm13(0.0)
                .lsm15("4") 
                .lsmstore(psa.getTcPsaplant())
                .taLsm09(psa.getTcPsa16() + psa.getTcPsa17())
                .taLsm10(psa.getTcPsa16() + psa.getTcPsa18())
                .taLsm01("補登")
                .taLsm02(psa.getTcPsa01())
                .taLsm03(psa.getTcPsa03())
                .taLsm04(GetDateTime.getTimeMilli())
                .taLsm05(now)
                .taLsm06(taLpj01)
                .taLsm07((lpj.getTaLpj02() != null) ? lpj.getTaLpj02() : 0.0)
                .taLsm08((lpj.getTaLpj03() != null) ? Double.valueOf(lpj.getTaLpj03()) : 0.0)
                .taLsm12(consumeAmt)
                .taLsm13(cardId)
                .build();
        lsmFileRepository.save(lsm);

        // ── Step 8. 更新 LPJ_FILE 會員主檔累計點數 ───────────────────────────
        lpjFileRepository.addPoints(memberId, "2", earnedPoint, consumeAmt, now);

        // ── Step 9. 寫入 LPL_FILE 會員商品明細 (記憶體序號優化，防 N+1 查詢) ──────
        Integer lastSeq = lplFileRepository.findNextSeq(lpj.getLpj03(), psa.getTcPsa04(), psa.getTcPsaplant());
        int currentSeq = (lastSeq == null) ? 1 : lastSeq;
    
        for (TC_PSB_FILE psb : psbList) {
        	LPL_FILE lpl = LPL_FILE.builder()
                     .lpl01(lpj.getLpj03())
                     .lpl02(psb.getTcPsb04())
                     .lpl03(psb.getTcPsb01())
                     .lpl04(psb.getLnt04())
                     .lpl05(psb.getTcPsb19() == null ? "0" : psb.getTcPsb19())
                     .lpl06(psb.getIma25() == null ? "PCS" : psb.getIma25())
                     .lpl07(psb.getTcPsb09() != null ? psb.getTcPsb09().doubleValue() : 0.0)
                     .lpl08(psb.getTcPsb13())
                     .lpl09(currentSeq++) 
                     .lpllegal(psa.getTcPsalegal())
                     .lplplant(psa.getTcPsaplant())
                     .taLpl10(psa.getTcPsa17())
                     .taLpl11(psa.getTcPsa18())
                     .taLpl01(psb.getTcPsb02())
                     .taLpl02(psb.getTcPsb03())
                     .build();
            lplFileRepository.save(lpl);
        }

        // ── Step 10. 回寫 發票主檔 綁定會員卡號 (TC_PSA_FILE) ─────────────────
        invoiceRepository.updatePsa13(
                lpj.getLpj03(), psa.getTcPsa04(), psa.getTcPsaplant(), invoiceNo,
                "uncheck".equals(randomNo) ? null : randomNo);
        
        // ── Step 11. 組裝回傳原本舊架構需要的 AppendInvoiceBean ────────────────
        AppendInvoiceBean retBean = new AppendInvoiceBean();
        retBean.setCard_id(lpj.getLpj03());
        retBean.setCounterID(psa.getTcPsa01());
        retBean.setCounterName(psa.getTqa02()); 
        retBean.setInvoiceSN(psa.getTcPsa03());
        retBean.setInvoiceDate(psa.getTcPsa04());
        retBean.setInvoiceTime(psa.getTcPsa05());
        retBean.setAmount(consumeAmt);
        retBean.setPoint(earnedPoint);
        retBean.setName(lpj.getLpk04()); 
        retBean.setPointBase(taLpj01);
        
        double currentLpj12 = (lpj.getLpj12() != null) ? lpj.getLpj12() : 0.0;
        double currentTaLpj02 = (lpj.getTaLpj02() != null) ? lpj.getTaLpj02() : 0.0;
        double currentTaLpj03 = (lpj.getTaLpj03() != null) ? Double.valueOf(lpj.getTaLpj03()) : 0.0;

        retBean.setTotalPoint(currentLpj12 + earnedPoint);
        retBean.setPrePoint(currentTaLpj02);
        retBean.setLastPoint(currentTaLpj03 + earnedPoint);
        
        retBean.setCreditCard(pscFile.getTcPsc07());
        retBean.setCreditCardAmt(pscFile.getTcPsc08());
        retBean.setInvAmt(psa.getTcPsa12());
        retBean.setPosId(psa.getTcPsa02());

        try {
            // 注意：這裡需要讓 executeAppendInvoice 把查詢到的 psa 傳出來，
            // 或者讓 retBean 攜帶所需資料，這裡以 retBean 為主組裝
            Map<String, Object> syncPayload = new HashMap<>();
            syncPayload.put("invoiceBean", retBean);
            // 如果 ChiefPay 真的需要完整的 psa 結構，可將 psa 暫存於 retBean 的擴充屬性中帶出來
            
            chiefPayClient.triggerChiefPayBonus(syncPayload, invoiceNo);
            log.info("Feign 點數同步成功: invoiceNo={}", invoiceNo);
        } catch (Exception e) {
            log.error("Feign 點數遠端同步發生異常（但不影響本地 DB 交易結果）: {}", e.getMessage(), e);
        }

        return retBean;
    }  

    
    
    
    
    @Transactional(rollbackFor = Exception.class)
    public AppendInvoiceDTO appendInvoice2(String sMemberID, String sCardID, String sInvoiceNo, String sRandomNo,
            jakarta.servlet.http.HttpServletRequest request) {

        log.info("Service 開始發票補登: memberId={}, invoiceNo={}, randomNo={}", sMemberID, sInvoiceNo, sRandomNo);

        // ── Step 1. 查詢發票主檔 (TC_PSA_FILE) ──────────────────────────────
        List<TC_PSA_FILE> psaList = invoiceRepository.findInvoice(sInvoiceNo, sRandomNo);
        if (psaList == null || psaList.isEmpty()) {
            throw new RuntimeException("The invoice number(" + sInvoiceNo + ") cannot be found in POS");
        }
        TC_PSA_FILE psa = psaList.get(psaList.size() - 1); // 舊系統迴圈最後一筆

        // ── Step 2. 查詢發票商品明細 (TC_PSB_FILE) ───────────────────────────
        List<TcPsbProjection> projections = tcPsbRepository.findByInvoice(
                psa.getTcPsaplant(), psa.getTcPsa01(), psa.getTcPsa02(),
                psa.getTcPsa03(), psa.getTcPsa04());
        
	            List<TC_PSB_FILE> psbList = projections.stream().map(p -> {
	            	TC_PSB_FILE file = new TC_PSB_FILE();
	            
	            // 基礎欄位對應
	            file.setTcPsbplant(p.getTcPsbplant());
	            file.setTcPsb01(p.getTcPsb01());
	            file.setTcPsb02(p.getTcPsb02());
	            file.setTcPsb03(p.getTcPsb03());
	            file.setTcPsb04(p.getTcPsb04());
	            file.setTcPsb06(p.getTcPsb06());
	            file.setTcPsb05(p.getTcPsb05());
	            file.setTcPsb07(p.getTcPsb07());
	            file.setTcPsb08(p.getTcPsb08());
	            file.setTcPsb09(p.getTcPsb09());
	            file.setTcPsb10(p.getTcPsb10());
	            file.setTcPsb11(p.getTcPsb11());
	            file.setTcPsb12(p.getTcPsb12());
	            file.setTcPsb13(p.getTcPsb13());
	            file.setTcPsb13A(p.getTcPsb13a());
	            file.setTcPsb13B(p.getTcPsb13b());
	            file.setTcPsb14(p.getTcPsb14());
	            file.setTcPsb15(p.getTcPsb15());
	            file.setTcPsb16(p.getTcPsb16());
	            file.setTcPsb17(p.getTcPsb17());
	            file.setTcPsb18(p.getTcPsb18());
	            file.setTcPsb19(p.getTcPsb19());
	            file.setTcPsb20(p.getTcPsb20());
	            file.setTcPsb21(p.getTcPsb21());
	            file.setTcPsb22(p.getTcPsb22());
	            file.setTcPsb23(p.getTcPsb23());
	            
	            // 額外 JOIN 的欄位
	            file.setIma25(p.getIma25());
	            file.setLnt04(p.getLnt04()); 
	            
	            return file;
	        }).collect(Collectors.toList());

            // ── Step 3. 查詢付款方式明細 (TC_PSC_FILE) ───────────────────────────
           List<TcPscProjection> pscList = tcPscRepository.findCreditCard(
                    psa.getTcPsaplant(), psa.getTcPsa01(), psa.getTcPsa02(),
                    psa.getTcPsa03(), psa.getTcPsa04());

            // 取最後一筆，若無資料則建立一個空的物件 (避免 NullPointerException)
            TcPscProjection psc = pscList.stream().reduce((a, b) -> b).orElse(null);

            // 如果需要轉回 TcPscFile 物件供後續業務邏輯使用：
            TC_PSC_FILE pscFile = new TC_PSC_FILE();
            if (psc != null) {
                pscFile.setTcPsc07(psc.getTcPsc07());
                pscFile.setTcPsc08(psc.getTcPsc08());
            }
            
            // ── Step 4. 查詢會員資料 (LPJ_FILE) ──────────────────────────────────
            List<LpjProjection> lpjList = lpjFileRepository.findActiveMember(sMemberID, "2");

            if (lpjList == null || lpjList.isEmpty()) {
                throw new RuntimeException("MemberID(" + sMemberID + ") cannot be found in LPJ");
            }

            // 取最後一筆
            LpjProjection lpj = lpjList.get(lpjList.size() - 1);

            // 如果後續業務邏輯需要使用物件屬性，直接透過 getter 取得
            // 例如：String memberName = lpj.getLpk04();

        // ── Step 5. 查詢積點換算率 (LRQ_FILE)，預設 100 ───────────────────────
        int pointBase = 100;
        List<LRQ_FILE> lrqList = lrqFileRepository.findActiveRate("601", psa.getTcPsaplant()); 
        if (lrqList != null && !lrqList.isEmpty()) {
            pointBase = lrqList.get(lrqList.size() - 1).getLrq03();
        }

        // ── Step 6. 計算本次累積點數 ──────────────────────────────────────────
        double consumeAmt = psa.getTcPsa40() != null ? psa.getTcPsa40() : 0.0;
        double taLpj01 = (lpj.getTaLpj01() != null) ? lpj.getTaLpj01() : 0.0;
        double earnedPoint = Math.floor(consumeAmt / pointBase) * taLpj01;

        // ── Step 7. 寫入 LSM_FILE (補登紀錄) ─────────────────────────────────
        SimpleDateFormat sdf   = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1  = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        Date cleanDate = (psa.getTcPsa04() != null) ? new Date(psa.getTcPsa04().getTime()) : new Date();

        LSM_FILE lsm = LSM_FILE.builder()
                .lsm01(lpj.getLpj03())
                .lsm02("2")
                .lsm03(psa.getTcPsa02() + sdf1.format(psa.getTcPsa04()) + psa.getTcPsa03())
                .lsm04(earnedPoint)
                .lsm05(cleanDate)
                //.lsm05(new java.util.Date(psa.getTcPsa04().getTime()))
                .lsm06(parseDate(sdf, "9999-12-31"))   // 有效期限
                .lsm08(consumeAmt)
                .lsmlegal(psa.getTcPsaplant())
                .lsmplant(psa.getTcPsaplant())
                .lsm09(0)
                .lsm10(0.0).lsm11(0.0).lsm12(0.0).lsm13(0.0)
                .lsm15("4")
                .lsmstore(psa.getTcPsaplant())
                .taLsm09(psa.getTcPsa16() + psa.getTcPsa17())
                .taLsm10(psa.getTcPsa16() + psa.getTcPsa18())
                .taLsm01("補登")
                .taLsm02(psa.getTcPsa01())
                .taLsm03(psa.getTcPsa03())
                .taLsm04(GetDateTime.getTimeMilli())
                .taLsm05(now)
                .taLsm06((lpj.getTaLpj01() != null) ? lpj.getTaLpj01() : 0.0)
                .taLsm07((lpj.getTaLpj02() != null) ? lpj.getTaLpj02() : 0.0)
                .taLsm08((lpj.getTaLpj03() != null) ? Double.valueOf(lpj.getTaLpj03()) : 0.0)
                .taLsm12(consumeAmt)
                .taLsm13(sCardID) //dc- 
                .build();
        lsmFileRepository.save(lsm);

        // ── Step 8. 更新 LPJ_FILE 會員累計點數 ───────────────────────────────
        lpjFileRepository.addPoints(sMemberID, "2", earnedPoint, consumeAmt, now);
        // SQL: lpj07+1, lpj08=now, lpj12+=point, lpj14+=point, lpj15+=amt, ta_lpj03+=point

	    // ── Step 9. 寫入 LPL_FILE 商品明細 (優化版) ──────────────────────────
	    // 1. 先在迴圈外取得初始序列號
        Integer lastSeq = lplFileRepository.findNextSeq(lpj.getLpj03(), psa.getTcPsa04(), psa.getTcPsaplant());
        int currentSeq = (lastSeq == null) ? 1 : lastSeq;
	
        for (TC_PSB_FILE psb : psbList) {
        	LPL_FILE lpl = LPL_FILE.builder()
	             .lpl01(lpj.getLpj03())
	             .lpl02(psb.getTcPsb04())
	             .lpl03(psb.getTcPsb01())
	             .lpl04(psb.getLnt04())
	             .lpl05(psb.getTcPsb19() == null ? "0" : psb.getTcPsb19())
	             .lpl06(psb.getIma25()   == null ? "PCS" : psb.getIma25())
	             .lpl07(psb.getTcPsb09() != null ? psb.getTcPsb09().doubleValue() : 0.0) // 增加 Null 安全檢查
	             .lpl08(psb.getTcPsb13())
	             .lpl09(currentSeq++) // 記憶體中遞增，減少 DB 查詢次數
	             .lpllegal(psa.getTcPsalegal())
	             .lplplant(psa.getTcPsaplant())
	             .taLpl10(psa.getTcPsa17())
	             .taLpl11(psa.getTcPsa18())
	             .taLpl01(psb.getTcPsb02())
	             .taLpl02(psb.getTcPsb03())
	             .build();
        	lplFileRepository.save(lpl);
        }

        // ── Step 10. 回寫 TC_PSA_FILE.TC_PSA13 (關聯會員卡號) ────────────────
        invoiceRepository.updatePsa13(
                lpj.getLpj03(), psa.getTcPsa04(), psa.getTcPsaplant(), sInvoiceNo,
                sRandomNo.equals("uncheck") ? null : sRandomNo);
        
        //dc-
        // ── Step 11. 組裝回傳 Bean (全面 Null 防禦) ──────────────────────────
        AppendInvoiceDTO result = new AppendInvoiceDTO();
        result.setCardId(lpj.getLpj03());
        result.setInvoiceSn(psa.getTcPsa03());
        result.setAmount(consumeAmt);
        result.setPoint(earnedPoint);
        
        // 嚴格 Null 防禦
        result.setPointBase((lpj.getTaLpj01() != null) ? lpj.getTaLpj01() : 0.0);
        result.setTotalPoint((lpj.getLpj12() != null ? lpj.getLpj12() : 0.0) + earnedPoint);
        result.setPrePoint((lpj.getTaLpj02() != null) ? lpj.getTaLpj02() : 0.0);
        result.setLastPoint((lpj.getTaLpj03() != null ? Double.valueOf(lpj.getTaLpj03()) : 0.0) + earnedPoint);
        
        // [Step 12. 自動觸發 API 同步]
        try {
            // 使用 Repository 補齊的 psa 物件與組裝好的 result 同步
            // chiefPayIntegrationService.triggerChiefPayBonus(result, sInvoiceNo, psa);
        	//dc-
        	hiefPayService.triggerChiefPayBonus(result, sInvoiceNo, psa);
        	
        	
        } catch (Exception e) {
            log.error("API 同步失敗: {}", e.getMessage());
            // 視需求決定是否 throw e 回滾補登
        }

        return result;
    }

    // ── 工具方法 ─────────────────────────────────────────────────────────────────
    private Date parseDate(SimpleDateFormat sdf, String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("日期解析失敗: " + dateStr, e);
        }
    }
    
    /**
     * 影城發票補登（標準風格重構版）
     */
    public AppendInvoiceBean theaterAppend(String memberID, String cardID, String invoiceNo, String totalPrice,
                                           String invoiceDate, String pointType) {
        
        log.info("Service 影城發票補登: memberId={}, invoiceNo={}, totalPrice={}", memberID, invoiceNo, totalPrice);
        
        try {
            // 1. 使用 Optional 取得資料庫執行的原始 Map 結果
            Optional<Map<String, Object>> rawResultOpt = invoiceRepository.theaterAppend(
                    memberID, cardID, invoiceNo, totalPrice, invoiceDate, pointType);
            
            // 2. 使用 orElseThrow 避免 null 檢查，失敗直接拋出 RuntimeException 觸發事務回滾
            Map<String, Object> rawResult = rawResultOpt.orElseThrow(() -> 
                new RuntimeException("影城補登失敗，請確認會員編號(" + memberID + ")或發票號碼(" + invoiceNo + ")是否正確")
            );
            
            // 3. 使用 Jackson ObjectMapper 將 Map 動態轉換為原本舊架構需要的 AppendInvoiceBean
            return objectMapper.convertValue(rawResult, AppendInvoiceBean.class);
            
        } catch (IllegalArgumentException e) {
            log.error("影城補登 Bean 屬性轉換失敗: ", e);
            throw new RuntimeException("影城補登資料格式轉換異常");
        } catch (Exception e) {
            log.error("theaterAppend 核心業務發生錯誤: ", e);
            // 向上拋出，讓上層 Controller 的 try-catch 或 ExceptionHandler 能統一捕捉
            throw new RuntimeException("影城補登異常: " + e.getMessage());
        }
    }    
        
    @Transactional(rollbackFor = Exception.class)
    public AppendInvoiceDTO theaterAppend2(String memberID, String cardID, String invoiceNo, String totalPrice,
            String invoiceDate, String pointType) {
        
        log.info("Service 影城發票補登: memberId={}, invoiceNo={}", memberID, totalPrice);
        
        try {
            // 1. 使用 Optional 取得結果
            Optional<Map<String, Object>> rawResultOpt = invoiceRepository.theaterAppend(
            		memberID, cardID, invoiceNo, totalPrice, invoiceDate, pointType);
            
            // 2. 使用 ifPresentOrElse 或 orElseThrow，避免直接對 Optional 物件做 null 檢查
            Map<String, Object> rawResult = rawResultOpt.orElseThrow(() -> 
                new RuntimeException("影城補登失敗，找不到對應的發票資料: " + invoiceNo)
            );
            
            // 3. 轉換為 Bean
            return objectMapper.convertValue(rawResult, AppendInvoiceDTO.class);
            
        } catch (IllegalArgumentException e) {
            log.error("JSON 轉換失敗: ", e);
            throw new RuntimeException("補登資料格式轉換異常");
        } catch (Exception e) {
            log.error("theaterAppend 發生錯誤: ", e);
            throw new RuntimeException("影城補登異常: " + e.getMessage());
        }
    }

    /**
     * 5. 查詢已使用發票
     */
    /**
     * 取得已核銷發票明細（標準小駝峰命名與 Repository 優化重構版）
     */
    @Transactional(readOnly = true)
    public List<InvoiceBean> getInvoiceUsed(String invoiceNo) throws Exception {
        
        log.info("Service 查詢已核銷發票明細: invoiceNo={}", invoiceNo);
        
        try {
            // 1. 解析傳入的逗號分隔發票字串，轉成 List 給 SQL IN 條件使用
            List<String> invoiceNos = java.util.Arrays.asList(invoiceNo.split(","));
            
            if (invoiceNos.isEmpty()) {
                return new java.util.ArrayList<>();
            }

            // 2. 透過 Repository 一口氣撈出所有發票的狀態對照 Map 列表
            List<Map<String, Object>> rawResults = invoiceRepository.findInvoiceUsedDetails(invoiceNos);
            
            // 3. 使用 objectMapper 批量將 List<Map> 轉換為 List<InvoiceBean>
            //    這會與 InvoiceBean 欄位上的 @JsonProperty("InvoiceNo") 完美對應
            List<InvoiceBean> invoiceList = objectMapper.convertValue(
                rawResults, 
                new com.fasterxml.jackson.core.type.TypeReference<List<InvoiceBean>>() {}
            );
            
            return invoiceList;
            
        } catch (IllegalArgumentException e) {
            log.error("發票明細 DTO 轉換異常: ", e);
            throw new RuntimeException("發票明細資料格式轉換異常");
        } catch (Exception e) {
            log.error("getInvoiceUsed 核心業務發生錯誤: ", e);
            throw new RuntimeException("查詢發票明細異常: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public AppendInvoiceBean appendReturn(String memberID, String invoiceNo, String totalPrice, String invoiceDate,
            double dPoint) {
        log.info("Service 執行退貨扣點: memberId={}, invoiceNo={}", memberID, invoiceNo);
        
        // 1. 檢查並刪除記錄
        int deleted = invoiceRepository.deleteLsmRecord(invoiceNo);
        if (deleted == 0) {
            // 使用特定的業務錯誤代碼，方便前端區分「找不到」與「系統錯誤」
            throw new RuntimeException("發票號碼 " + invoiceNo + " 無補登記錄，無法進行退貨。");
        }

        try {
            // 2. 執行扣點更新
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(invoiceDate);
            int updated = invoiceRepository.updateMemberPoints(memberID, date, dPoint);
            
            if (updated == 0) {
                throw new RuntimeException("會員點數更新失敗，請確認會員狀態是否異常。");
            }

            // 3. 查詢最新資訊
            Map<String, Object> memberData = invoiceRepository.findMemberPoints(memberID);
            
            // 4. 組裝回應
            AppendInvoiceBean bean = new AppendInvoiceBean();
            bean.setAmount(Double.parseDouble(totalPrice));
            bean.setPoint(-dPoint);
            if (memberData != null) {
                bean.setName((String) memberData.get("LPK04"));
                bean.setPointBase((Double) memberData.get("TA_LPJ01"));
                bean.setTotalPoint((Double) memberData.get("LPJ12"));
                bean.setPrePoint((Double) memberData.get("TA_LPJ02"));
                bean.setLastPoint((Double) memberData.get("TA_LPJ03"));
            }
            return bean;
        } catch (Exception e) {
            log.error("Service 扣點邏輯異常: ", e);
            throw new RuntimeException(e.getMessage()); // 拋出給 Controller 的 catch 處理
        }
    }

    /**
     * 專櫃補登/排除驗證
     */
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
    
    /**
     * 取得發票細項/核銷明細明細
     */
    @Transactional(readOnly = true)
    public List<InvoiceDTO> getInvoice(String sInvoiceNo) {
        log.info("Service 獲取發票明細: sInvoiceNo -> {}", sInvoiceNo);
        try {
            List<Map<String, Object>> rawList = invoiceRepository.getInvoiceUsed(sInvoiceNo);
            if (rawList == null || rawList.isEmpty()) {
                return new ArrayList<>();
            }
            
            return rawList.stream()
                    .map(map -> objectMapper.convertValue(map, InvoiceDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("獲取發票明細失敗: ", e);
            throw new RuntimeException("無法讀取發票明細資訊: " + e.getMessage());
        }
    }

}