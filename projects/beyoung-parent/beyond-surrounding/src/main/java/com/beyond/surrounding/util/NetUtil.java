package com.beyond.surrounding.util;

import java.util.HashMap;
import java.util.Map;

public class NetUtil {

    /**
     * 原舊系統中的 splitQuery 簡單實作
     */
    public static Map<String, String> splitQuery(String query) {
        Map<String, String> queryPairs = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return queryPairs;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                queryPairs.put(pair.substring(0, idx), pair.substring(idx + 1));
            } else {
                queryPairs.put(pair, "");
            }
        }
        return queryPairs;
    }
    
}