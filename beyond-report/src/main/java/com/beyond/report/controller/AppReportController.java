package com.beyond.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.beyond.report.entity.APP_COUNTER;
import com.beyond.report.service.ReportService;
import com.beyond.report.util.MyJSON;
//Spring Boot 3.x+ (使用 jakarta)
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Report/rest/AppReport")
public class AppReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/getAppCounter") // 使用 @PostMapping，若前端為 GET 請改為 @GetMapping 或 @RequestMapping
    public void getAppCounter(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        // 設定正確的 Content-Type 與編碼
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        // 取得分頁參數並設置預設值，防止 NPE 或格式錯誤
        int page = getIntParameter(request, "page", 1);
        int recLimit = getIntParameter(request, "rows", 10);
        request.getParameter("sidx");
        request.getParameter("sord");

        int from = (page - 1) * recLimit;

        try {
            JSONArray rows = new JSONArray();
            List<APP_COUNTER> list = reportService.getAppCounter(jsonObj, from, recLimit);

            if (list != null) {
                for (APP_COUNTER bean : list) {
                    JSONObject jo = new JSONObject();
                    jo.put("lpj04", bean.getLPJ04() != null ? bean.getLPJ04().toString() : "");
                    jo.put("counter_all", bean.getCOUNTER_ALL());
                    jo.put("counter_000", bean.getCOUNTER_000());
                    jo.put("counter_app", bean.getCOUNTER_APP());
                    jo.put("counter_beyond", bean.getCOUNTER_BEYOND());
                    jo.put("counter_non_beyond", bean.getCOUNTER_NON_BEYOND());
                    
                    // org.json.JSONArray 需使用 put() 而非 add()
                    rows.put(jo); 
                }
            }

            int totCount = reportService.getTotCnt(jsonObj);
            int totPage = (int) Math.ceil((double) totCount / (double) recLimit);

            retObj.put("page", page);
            retObj.put("total", totPage);
            retObj.put("records", totCount);
            retObj.put("Success", "Y");
            retObj.put("rows", rows);

            // 寫回 Response 串流
            try (PrintWriter out = response.getWriter()) {
                out.print(retObj.toString());
                out.flush();
            }

        } catch (Exception e) {
            log.error("getAppCounter 執行失敗: ", e);
            
            // 寫回失敗資訊給前端 (jqGrid 相容)
            retObj.put("Success", "N");
            retObj.put("message", e.getMessage());
            try (PrintWriter out = response.getWriter()) {
                out.print(retObj.toString());
                out.flush();
            }
        }
    }

    /**
     * 安全取得整數參數的輔助工具方法
     */
    private int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String value = request.getParameter(paramName);
        if (value != null && !value.trim().isEmpty()) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                log.warn("數字參數解析失敗: {} = {}, 使用預設值: {}", paramName, value, defaultValue);
            }
        }
        return defaultValue;
    }
}