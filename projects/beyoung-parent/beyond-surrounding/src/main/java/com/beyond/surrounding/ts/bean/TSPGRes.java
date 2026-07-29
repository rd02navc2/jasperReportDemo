package com.beyond.surrounding.ts.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TSPGRes {

    // 定義一個不可變（Immutable）的狀態對照 Map，效能更好且具備執行緒安全
    private static final Map<String, String> STATUS_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("02", "已授權");
        map.put("03", "已請款");
        map.put("04", "請款已清算");
        map.put("06", "已退貨");
        map.put("08", "退貨已清算");
        map.put("12", "訂單已取消");
        map.put("ZP", "訂單處理中");
        map.put("ZF", "授權失敗");
        STATUS_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * 原本的建構子（保留，若其他地方有呼叫到不會壞掉）
     */
    public TSPGRes() {
        // 保留預設建構子
    }

    /**
     * 獲取狀態代碼對應的中文名稱
     * 
     * @param code 狀態碼 (例如 "02")
     * @return 中文描述 (例如 "已授權")，若找不到則回傳 "未知狀態"
     */
    public static String getStatusName(String code) {
        return STATUS_MAP.getOrDefault(code, "未知狀態 (" + code + ")");
    }

    /**
     * 獲取完整的狀態對照表
     */
    public static Map<String, String> getStatusMap() {
        return STATUS_MAP;
    }
}
