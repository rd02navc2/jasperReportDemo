package com.beyond.report.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    
    public static final Map<String, String> hReportName = new HashMap<>();
    public static final Map<String, String> hStatusName = new HashMap<>();
    public static final Map<String, String> hFloor = new HashMap<>();
    
    static {
        hReportName.put("A_Test1", "報表1");
        hReportName.put("A_Test2", "報表2");
        hReportName.put("A_Test3", "報表3");
        hReportName.put("A_Test4", "報表4");
        hReportName.put("A_Test5", "報表5");
        
        hStatusName.put("draft", "匯入中");
        hStatusName.put("approving", "審核中");
        hStatusName.put("approved", "已審核");
        hStatusName.put("rejected", "已退回");
        
        hFloor.put("1F", "1F");
        hFloor.put("2F", "2F");
        hFloor.put("3F", "3F");
        hFloor.put("4F", "4F");
        hFloor.put("5F", "5F");
        hFloor.put("9F", "9F");
        hFloor.put("B1", "B1");
        hFloor.put("B2", "B2");
        hFloor.put("TN", "TN");
    }

    /**
     * 取得狀態顯示名稱
     */
    public static String getStatusName(String status) {
        if (status == null) {
            return "未編輯";
        }
        return hStatusName.getOrDefault(status, "未編輯");
    }

    /**
     * 取得樓層顯示名稱
     */
    public static String getFloorName(String floor) {
        if (floor == null) {
            return "";
        }
        return hFloor.getOrDefault(floor, floor);
    }

    /**
     * 取得報表名稱
     */
    public static String getReportName(String reportId) {
        if (reportId == null) {
            return "";
        }
        return hReportName.getOrDefault(reportId, reportId);
    }
}