package com.beyond.report.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJSON {

    /**
     * 從 HttpServletRequest 的 Request Body 中讀取字串並轉為 org.json.JSONObject
     */
    public static JSONObject readJson(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // 若請求體為空，回傳空的 JSONObject 防止 NPE
            if (sb.length() == 0) {
                return new JSONObject();
            }

            return new JSONObject(sb.toString());

        } catch (Exception e) {
            log.error("解析 HttpServletRequest Body 為 JSONObject 失敗", e);
            // 當發生解析錯誤時，回傳空物件避免後續 NPE 崩潰
            return new JSONObject();
        }
    }
}