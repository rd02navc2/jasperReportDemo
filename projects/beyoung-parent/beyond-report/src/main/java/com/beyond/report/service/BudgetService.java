package com.beyond.report.service;

import com.beyond.report.entity.*;
import com.beyond.report.repository.BudgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;

    // 建構子注入（官方推薦）
    // Spring 4.3+ 可以省略 @Autowired
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // ==================== 郵件參數 ====================

    public EMAIL_ADDRESS getBGParam() throws Exception {
        return budgetRepository.getBGParam("BUDGET_APPLY");
    }

    public EMAIL_ADDRESS getBGParam2() throws Exception {
        return budgetRepository.getBGParam("BUDGET_APPROVE");
    }

    @Transactional
    public void saveBGParam(String to, String cc) throws Exception {
        budgetRepository.updateBudgetApplyTo(to, "BUDGET_APPLY");
        budgetRepository.updateBudgetApproveCC(cc, "BUDGET_APPROVE");
    }

    // ==================== 預算 Header ====================

    public List<BUDGET_DAY_HEADER> getBudgetHeader() throws Exception {
        return budgetRepository.findAllHeaders();
    }

    public BUDGET_DAY_HEADER getStatusByMonth(String month) throws Exception {
        return budgetRepository.findHeaderByMonth(month);
    }

    @Transactional
    public void updateStatus(String month, String status) throws Exception {
        budgetRepository.updateStatus(month, status);
    }

    // ==================== 預算 Detail ====================

    public List<BUDGET_DAY_DETAIL> getBudgetDetail(String month, String sidx, String sord) throws Exception {
        return budgetRepository.findDetailsByMonth(month);
    }

    public Integer getBudgetDetailCnt(String month) throws Exception {
        return budgetRepository.countDetailsByMonth(month);
    }

    @Transactional
    public void insertCounterData(String month, List<OBA_FILE> counters) throws Exception {
        BUDGET_DAY_HEADER header = budgetRepository.findHeaderByMonth(month);
        if (header == null) {
            budgetRepository.insertHeader(month);
        }

        for (OBA_FILE counter : counters) {
            boolean exists = budgetRepository.existsDetailByMonthDeptCounter(
                    month, counter.getOBA01(), counter.getLNT06()
            );
            if (!exists) {
                // 使用 insertDetail 方法
                // 這裡需要根據您的 OBA_FILE 結構呼叫適當的 insert 方法
            }
        }
    }

    @Transactional
    public void updateFloor(String month, Map<String, String> floorMap) throws Exception {
        for (Map.Entry<String, String> entry : floorMap.entrySet()) {
            budgetRepository.updateFloorByCounter(month, entry.getValue().toUpperCase(), entry.getKey());
        }
    }

    @Transactional
    public void saveBudget(String month, JSONArray data, String loginId) throws Exception {
        budgetRepository.updateHeaderDraft(month, "draft", loginId, new Timestamp(System.currentTimeMillis()));

        for (int i = 0; i < data.length(); i++) {
            JSONObject jo = data.getJSONObject(i);
            String floor = jo.getString("floor").trim().toUpperCase();
            String deptId = jo.getString("dept_id").trim();
            String counterId = jo.getString("counter_id").trim();

            BUDGET_DAY_DETAIL existing = budgetRepository.findDetailByMonthFloorDeptCounter(
                    month, floor, deptId, counterId
            );

            if (existing == null) {
                budgetRepository.insertDetail(
                        jo.optString("b_01"), jo.optString("b_02"), jo.optString("b_03"),
                        jo.optString("b_04"), jo.optString("b_05"), jo.optString("b_06"),
                        jo.optString("b_07"), jo.optString("b_08"), jo.optString("b_09"),
                        jo.optString("b_10"), jo.optString("b_11"), jo.optString("b_12"),
                        jo.optString("b_13"), jo.optString("b_14"), jo.optString("b_15"),
                        jo.optString("b_16"), jo.optString("b_17"), jo.optString("b_18"),
                        jo.optString("b_19"), jo.optString("b_20"), jo.optString("b_21"),
                        jo.optString("b_22"), jo.optString("b_23"), jo.optString("b_24"),
                        jo.optString("b_25"), jo.optString("b_26"), jo.optString("b_27"),
                        jo.optString("b_28"), jo.optString("b_29"), jo.optString("b_30"),
                        jo.optString("b_31"),
                        month, floor, deptId, jo.getString("dept_name"),
                        counterId, jo.getString("counter_name"), jo.optString("org_name")
                );
            } else {
                budgetRepository.updateDetail(
                        jo.optString("b_01"), jo.optString("b_02"), jo.optString("b_03"),
                        jo.optString("b_04"), jo.optString("b_05"), jo.optString("b_06"),
                        jo.optString("b_07"), jo.optString("b_08"), jo.optString("b_09"),
                        jo.optString("b_10"), jo.optString("b_11"), jo.optString("b_12"),
                        jo.optString("b_13"), jo.optString("b_14"), jo.optString("b_15"),
                        jo.optString("b_16"), jo.optString("b_17"), jo.optString("b_18"),
                        jo.optString("b_19"), jo.optString("b_20"), jo.optString("b_21"),
                        jo.optString("b_22"), jo.optString("b_23"), jo.optString("b_24"),
                        jo.optString("b_25"), jo.optString("b_26"), jo.optString("b_27"),
                        jo.optString("b_28"), jo.optString("b_29"), jo.optString("b_30"),
                        jo.optString("b_31"), jo.optString("org_name"),
                        month, floor, deptId, counterId
                );
            }
        }
    }

    // ==================== 櫃位清單相關 (補齊) ====================

    /**
     * 取得所有櫃位清單
     * 對應原 BudgetDAO 中的 getCounterList
     */
    public List<OBA_FILE> getCounterList(String month) throws Exception {
        // 這裡需要從您的資料庫查詢櫃位清單
        // 由於原 BudgetDAO 中使用了 OERPSF 查詢 Oracle ERP 的 lnt_file 和 oba_file
        // 這裡需要根據您的實際資料來源實作
        
        // 範例：從 lnt_file 和 oba_file 查詢
        List<OBA_FILE> counterList = new ArrayList<>();
        
        // 實際實作需要查詢 Oracle ERP 的相關表格
        // 由於您現在使用 JPA，需要建立對應的 Repository 來查詢
        
        return counterList;
    }

    /**
     * 取得預算全部為 0 的櫃位清單
     * 對應原 BudgetDAO 中的 getNoneCounterList
     */
    public List<OBA_FILE> getNoneCounterList(String month, JSONArray dataArray) throws Exception {
        List<OBA_FILE> noneCounterList = new ArrayList<>();
        
        // 取得所有櫃位
        List<OBA_FILE> allCounters = getCounterList(month);
        
        // 檢查每個櫃位的預算是否全部為 0
        for (OBA_FILE counter : allCounters) {
            boolean allZero = true;
            
            // 從 dataArray 中找出該櫃位的資料
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jo = dataArray.getJSONObject(i);
                if (jo.getString("counter_id").equals(counter.getLNT06())) {
                    // 檢查 1-31 日是否全部為 0
                    for (int day = 1; day <= 31; day++) {
                        String key = String.format("b_%02d", day);
                        int value = jo.optInt(key, 0);
                        if (value != 0) {
                            allZero = false;
                            break;
                        }
                    }
                    break;
                }
            }
            
            if (allZero) {
                noneCounterList.add(counter);
            }
        }
        
        return noneCounterList;
    }

    // ==================== 例外櫃位 ====================

    public List<BUDGET_COUNTER_EXCEPT> getExceptCounter(JSONObject jsonObj) throws Exception {
        String counterId = jsonObj.optString("counter_id", "");
        return getExceptCounter(counterId);
    }

    public List<BUDGET_COUNTER_EXCEPT> getExceptCounter(String counterId) throws Exception {
        if (counterId != null && !counterId.isEmpty()) {
            BUDGET_COUNTER_EXCEPT entity = budgetRepository.findExceptCounterById(counterId);
            return entity != null ? List.of(entity) : List.of();
        }
        return budgetRepository.findAllExceptCounters();
    }

    @Transactional
    public void addExceptCounter(String counterId, String counterName) throws Exception {
        budgetRepository.insertExceptCounter(counterId, counterName);
    }

    @Transactional
    public void delExceptCounter(String counterId) throws Exception {
        budgetRepository.deleteExceptCounter(counterId);
    }

    // ==================== LNT_FILE (Oracle ERP) ====================

    public LNT_FILE getCounterById(JSONObject jsonObj) throws Exception {
        String counterId = jsonObj.getString("counter_id");
        return budgetRepository.findLntFileByCounterId(counterId);
    }

    // ==================== 刪除方法 ====================

    @Transactional
    public void delete(String month, String deptId, String counterId) throws Exception {
        budgetRepository.deleteDetailByMonthDeptCounter(month, deptId, counterId);
    }

    @Transactional
    public void delete0(String month) throws Exception {
        budgetRepository.deleteZeroDetails(month);
    }

    // ==================== 申請紀錄 ====================

    @Transactional
    public void saveApplyId(String month, String loginId) throws Exception {
        budgetRepository.deleteApply(month, loginId);
        budgetRepository.insertApply(month, loginId, new Timestamp(System.currentTimeMillis()));
    }

    public List<BUDGET_DAY_APPLY> getBudgetApply(String month) throws Exception {
        List<String> applyIds = budgetRepository.findApplyIdsByMonth(month);
        return applyIds.stream().map(id -> {
            BUDGET_DAY_APPLY apply = new BUDGET_DAY_APPLY();
            apply.setApply_id(id);
            // 這裡需要從 Users 表查詢 email，暫時留空
            return apply;
        }).collect(java.util.stream.Collectors.toList());
    }

    // ==================== 核准/退回 ====================

    @Transactional
    public void approve(String month, String loginId, boolean isRecalc) throws Exception {
        budgetRepository.approveHeader(month, "approved", loginId, new Timestamp(System.currentTimeMillis()));
        log.info("預算核准完成: month={}, loginId={}, isRecalc={}", month, loginId, isRecalc);
    }

    @Transactional
    public void reject(String month, String loginId, String rejectReason) throws Exception {
        budgetRepository.rejectHeader(month, "rejected", rejectReason, loginId, new Timestamp(System.currentTimeMillis()));
        log.info("預算退回完成: month={}, loginId={}, reason={}", month, loginId, rejectReason);
    }

    // ==================== 郵件排程 ====================

    @Transactional
    public void scheduleMail(String topic, String content, String function, String to, String cc, String bcc) throws Exception {
        EMAIL_ADDRESS emailParam = budgetRepository.getBGParam(function);
        
        String toMail = to;
        if (emailParam.getTO() != null && !emailParam.getTO().isEmpty()) {
            toMail = (toMail == null || toMail.isEmpty()) ? 
                    emailParam.getTO() : 
                    toMail + ";" + emailParam.getTO();
        }
        
        String ccMail = cc;
        if (emailParam.getCC() != null && !emailParam.getCC().isEmpty()) {
            ccMail = (ccMail == null || ccMail.isEmpty()) ? 
                    emailParam.getCC() : 
                    ccMail + ";" + emailParam.getCC();
        }

        log.info("排程郵件: topic={}, function={}, to={}, cc={}", topic, function, toMail, ccMail);
    }

    // ==================== 檢查是否完成 ====================

    public List<BUDGET_DAY_DETAIL> isCompletion(String month) throws Exception {
        return budgetRepository.findIncompleteDetails(month);
    }
}