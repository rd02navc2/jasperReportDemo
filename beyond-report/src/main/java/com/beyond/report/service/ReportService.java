package com.beyond.report.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.report.entity.APP_COUNTER;
import com.beyond.report.entity.ATTENDANCEEMPRANK;
import com.beyond.report.entity.LOGDB;
import com.beyond.report.repository.AppCounterRepository;
import com.beyond.report.repository.AttendanceEmpRankRepository;
import com.beyond.report.repository.LogDbRepository;
import com.beyond.report.util.StatusCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

	private final LogDbRepository logDbRepository;
	private final AttendanceEmpRankRepository attendanceEmpRankRepository;
	private final AppCounterRepository appCounterRepository;

    /**
     * 根據傳入的 JSON 物件取得登入與打卡日誌
     */
	@Transactional(rollbackFor = Exception.class)
    public List<LOGDB> getLogin(JSONObject jsonObj) {
        if (jsonObj == null || !jsonObj.has("sale_s_date") || !jsonObj.has("sale_e_date")) {
            log.warn("傳入的 JSON 缺乏必要的日期參數 sale_s_date 或 sale_e_date");
            return Collections.emptyList();
        }

        String sFromDate = jsonObj.getString("sale_s_date");
        String sEndDate = jsonObj.getString("sale_e_date");

        return getLogin(sFromDate, sEndDate);
    }

    /**
     * 根據開始與結束日期查詢 Log
     */
	@Transactional(rollbackFor = Exception.class)
    public List<LOGDB> getLogin(String sFromDate, String sEndDate) {
        log.info("查詢打卡記錄，區間: {} ~ {}", sFromDate, sEndDate);
        return logDbRepository.findLoginLogsByDateRange(sFromDate, sEndDate);
    }

    /**
     * 比對班表與打卡記錄，產出異常名單
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONArray processPunch(List<ATTENDANCEEMPRANK> scheduleList, List<LOGDB> loginLogList) {
        JSONArray rows = new JSONArray();

        // 1. 整理打卡記錄 Map
        // Key: LOGTIME + "0100" + USERID (例如: "2026-07-240100EMP001")
        // Value: Map<WorkingStatus, LogTime2>
        Map<String, Map<String, String>> punchMap = new HashMap<>();

        if (loginLogList != null) {
            for (LOGDB bean : loginLogList) {
                if (bean.getLogTime() == null || bean.getUserID() == null) {
                    continue;
                }

                String mapKey = bean.getLogTime() + "0100" + bean.getUserID();
                Map<String, String> statusMap = punchMap.computeIfAbsent(mapKey, k -> new HashMap<>());

                String status = String.valueOf(bean.getWorkingStatus());
                String logTime2 = bean.getLogTime2();

                // WorkingStatus = "2" 表示上班打卡 (保留最早紀錄，若已有不覆蓋)
                if ("2".equals(status)) {
                    statusMap.putIfAbsent("2", logTime2);
                } 
                // WorkingStatus = "3" 表示下班打卡 (保留最新紀錄)
                else if ("3".equals(status)) {
                    statusMap.put("3", logTime2);
                } else {
                    statusMap.putIfAbsent(status, logTime2);
                }
            }
        }

        // 2. 比對班表與打卡狀況
        if (scheduleList != null) {
            for (ATTENDANCEEMPRANK schedule : scheduleList) {
                if (schedule.getDATE() == null || schedule.getCODE() == null) {
                    continue;
                }

                String scheduleKey = schedule.getDATE() + schedule.getCODE();
                JSONObject jsonObject = new JSONObject();

                // 情況 A：休假/請假，但卻有打卡記錄 -> 報表紀錄異常
                if ("休".equals(schedule.getSHORTNAME())) {
                    if (punchMap.containsKey(scheduleKey)) {
                        Map<String, String> statusMap = punchMap.get(scheduleKey);
                        String beginTime = statusMap.getOrDefault("2", "");
                        String endTime = statusMap.getOrDefault("3", "");

                        jsonObject.put("date", schedule.getDATE());
                        jsonObject.put("code", schedule.getCODE());
                        jsonObject.put("cnname", schedule.getCNNAME());
                        jsonObject.put("desc", StatusCodeConst.off_message);
                        jsonObject.put("begin_time", beginTime);
                        jsonObject.put("end_time", endTime);

                        rows.put(jsonObject); // 注意：org.json 使用 put() 而非 add()
                    }
                } 
                // 情況 B：正常上班日，但完全沒有打卡記錄 -> 未打卡
                else {
                    if (!punchMap.containsKey(scheduleKey)) {
                        jsonObject.put("date", schedule.getDATE());
                        jsonObject.put("code", schedule.getCODE());
                        jsonObject.put("cnname", schedule.getCNNAME());
                        jsonObject.put("desc", StatusCodeConst.none_message);

                        rows.put(jsonObject); // 注意：org.json 使用 put() 而非 add()
                    }
                }
            }
        }

        return rows;
    }

    /**
     * 根據 JSONObject 查詢排班資料
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ATTENDANCEEMPRANK> getSchedule(JSONObject jsonObj) {
        if (jsonObj == null) {
            return Collections.emptyList();
        }

        String sFromDate = jsonObj.optString("sale_s_date", "");
        String sEndDate = jsonObj.optString("sale_e_date", "");
        String sSidx = jsonObj.optString("sidx", "date");
        String sSord = jsonObj.optString("sord", "ASC");

        return getSchedule(sFromDate, sEndDate, sSidx, sSord);
    }

    /**
     * 核心排班查詢邏輯
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ATTENDANCEEMPRANK> getSchedule(String sFromDate, String sEndDate, String sSidx, String sSord) {
        // 安全處理排序方向 (ASC / DESC)
        String sortOrder = "DESC".equalsIgnoreCase(sSord) ? "DESC" : "ASC";

        log.info("查詢排班資料 - 區間: {} ~ {}, 排序依據: {}, 方向: {}", sFromDate, sEndDate, sSidx, sortOrder);

        if ("code".equalsIgnoreCase(sSidx)) {
            return attendanceEmpRankRepository.findScheduleOrderByCode(sFromDate, sEndDate, sortOrder);
        } else if ("name".equalsIgnoreCase(sSidx)) {
            return attendanceEmpRankRepository.findScheduleOrderByName(sFromDate, sEndDate, sortOrder);
        } else {
            return attendanceEmpRankRepository.findScheduleOrderByDate(sFromDate, sEndDate, sortOrder);
        }
    }

    /**
     * 根據 JSONObject 與分頁參數查詢 APP 統計資料
     */
    @Transactional(rollbackFor = Exception.class)
    public List<APP_COUNTER> getAppCounter(JSONObject jsonObj, int from, int recLimit) {
        if (jsonObj == null) {
            return Collections.emptyList();
        }

        String sFromDate = jsonObj.optString("sale_s_date", "");
        String sEndDate = jsonObj.optString("sale_e_date", "");

        return getAppCounter(sFromDate, sEndDate, from, recLimit);
    }

    /**
     * 核心查詢與分頁轉換邏輯
     */
    @Transactional(rollbackFor = Exception.class)
    public List<APP_COUNTER> getAppCounter(String sFromDate, String sEndDate, int from, int recLimit) {
        log.info("查詢 APP 統計數據 - 區間: {} ~ {}, Offset: {}, Limit: {}", sFromDate, sEndDate, from, recLimit);

        // 防禦性檢查：避免 recLimit 為 0 導致 PageRequest 拋出 Exception
        int pageSize = recLimit > 0 ? recLimit : 10;
        
        // 將 MySQL 的 offset (from) 轉為 Spring Data 的頁碼 pageNumber (從 0 開始)
        int pageNumber = from / pageSize;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return appCounterRepository.findAppCounterByDateRange(sFromDate, sEndDate, pageable);
    }


    /**
     * 根據 JSONObject 查詢總筆數
     */
    public int getTotCnt(JSONObject jsonObj) {
        if (jsonObj == null) {
            return 0;
        }

        String sFromDate = jsonObj.optString("sale_s_date", "");
        String sEndDate = jsonObj.optString("sale_e_date", "");

        return getTotCnt(sFromDate, sEndDate);
    }

    /**
     * 核心計算總筆數邏輯
     */
    public int getTotCnt(String sFromDate, String sEndDate) {
        log.info("查詢 APP 統計總筆數 - 區間: {} ~ {}", sFromDate, sEndDate);
        
        if (sFromDate.isBlank() || sEndDate.isBlank()) {
            return 0;
        }

        return appCounterRepository.countByDateRange(sFromDate, sEndDate);
    }
    

}
