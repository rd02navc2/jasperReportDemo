package com.beyond.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.beyond.report.entity.ATTENDANCEEMPRANK;
import com.beyond.report.entity.LOGDB;
import com.beyond.report.service.DownlodSensitiveService;
import com.beyond.report.service.ReportService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.beyond.report.util.GetDateTime;
import com.beyond.report.util.OffPunchExcel;
import com.beyond.report.entity.Login;

@Slf4j
@RestController
@RequestMapping("/Report/rest/Report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private DownlodSensitiveService downlodSensitiveService;

    // 建議將屬性注入改用 Spring 的 @Value (或 @ConfigurationProperties)
    @Value("${download.content.root}")
    private String downloadContentRoot;

    @PostMapping(value = "/genOffPunchExcel", 
    consumes = MediaType.APPLICATION_JSON_VALUE, 
    produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseEntity<Map<String, Object>> genOffPunchExcel(
            @RequestBody JSONObject jsonObj,
            @SessionAttribute(name = "login", required = false) Login loginUser) {

        Map<String, Object> retObj = new HashMap<>();

        // 1. 檢查 Session 登入狀態
        if (loginUser == null) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "使用者未登入或 Session 已過期");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(retObj);
        }

        String loginId = loginUser.getLoginId();

        try {
            // 2. 查詢排班與打卡資料
            List<ATTENDANCEEMPRANK> scheduleList = reportService.getSchedule(jsonObj);
            List<LOGDB> loginLogList = reportService.getLogin(jsonObj);
            JSONArray rows = reportService.processPunch(scheduleList, loginLogList);

            // 3. 建立目標資料夾路徑
            Path hrDir = Paths.get(downloadContentRoot, "HR");
            if (!Files.exists(hrDir)) {
                Files.createDirectories(hrDir);
            }

            // 4. 產生檔名與完整路徑
            String timeStamp = GetDateTime.getTodayDateW("") 
                    + GetDateTime.getTimeA2()[0] 
                    + GetDateTime.getTimeA2()[1];
            String fileName = "HR_OFF_PUNCH_" + timeStamp + ".xlsx";
            Path filePath = hrDir.resolve(fileName);

            // 5. 產生 Excel 並紀錄敏感資料下載 Log
            OffPunchExcel.genOffPunchExcel(rows, "出勤異常報表", filePath.toString(), jsonObj);
            downlodSensitiveService.saveDownloadSensitive(loginId, "OffPunch", scheduleList.size());

            // 6. 組裝成功回應 (回傳下載相對路徑或檔案名稱)
            retObj.put("Url", filePath.toString());
            retObj.put("FileName", fileName);
            retObj.put("Success", "Y");

            return ResponseEntity.ok(retObj);

        } catch (Exception e) {
            log.error("產生出勤異常報表時發生錯誤：", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "系統在處理時發生錯誤：" + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(retObj);
        }
    }
    
}