package com.beyond.report.controller;

import com.beyond.report.entity.*;
import com.beyond.report.service.BudgetService;
import com.beyond.report.util.Constants;
import com.beyond.report.util.MyJSON;
import com.beyond.report.file.BudgetUploadExcel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/Report/rest/Budget")
public class BudgetController {

    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

    @Autowired
    private BudgetService budgetService;

    @Value("${upload.content.root}")
    private String uploadContentRoot;

    // ==================== 預算參數 ====================

	@GetMapping(value = "/getBGParam", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getBGParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(CONTENT_TYPE_JSON);
	    response.setCharacterEncoding("UTF-8");
	
	    JSONObject retObj = new JSONObject();
	
	    try {
	        // 1. 取得並防護 BudgetApply (entity)
	        EMAIL_ADDRESS entity = budgetService.getBGParam();
	        if (entity != null && entity.getTO() != null) {
	            retObj.put("BudgetApply", entity.getTO().replaceAll(";", "\n"));
	        } else {
	            retObj.put("BudgetApply", ""); // 查無資料時給予預設空字串，防止 NPE
	        }
	
	        // 2. 取得並防護 BudgetApprove (entity2)
	        EMAIL_ADDRESS entity2 = budgetService.getBGParam2();
	        if (entity2 != null && entity2.getCC() != null) {
	            retObj.put("BudgetApprove", entity2.getCC().replaceAll(";", "\n"));
	        } else {
	            retObj.put("BudgetApprove", ""); // 查無資料時給予預設空字串
	        }
	
	        retObj.put("Success", "Y");
	
	    } catch (Exception e) {
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", e.getMessage());
	        log.error("getBGParam error: {}", e.getMessage(), e);
	    } finally {
	        // 確保寫入 Response
	        response.getWriter().print(retObj.toString());
	        response.getWriter().flush();
	    }
	}

    @PostMapping(value = "/saveBGParam", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveBGParam(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject retObj = new JSONObject();

        try {
            String sTo = request.getParameter("budget_apply");
            String sCc = request.getParameter("budget_approve_cc");

            if (sTo == null || sCc == null) {
                StringBuilder sb = new StringBuilder();
                String line;
                try (java.io.BufferedReader reader = request.getReader()) {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                }
                String body = sb.toString();
                if (body != null && !body.isEmpty()) {
                    JSONObject jsonObj = new JSONObject(body);
                    if (sTo == null) sTo = jsonObj.optString("budget_apply");
                    if (sCc == null) sCc = jsonObj.optString("budget_approve_cc");
                }
            }

            if (sTo != null) {
                sTo = sTo.replaceAll("\n", ";");
            }
            if (sCc != null) {
                sCc = sCc.replaceAll("\n", ";");
            }

            budgetService.saveBGParam(sTo, sCc);

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("saveBGParam error: {}", e.getMessage(), e);
        }
    }

    // ==================== 預算資料查詢 ====================

    @GetMapping(value = "/getBudgetHeader", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getBudgetHeader(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject retObj = new JSONObject();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONArray rows = new JSONArray();
            List<BUDGET_DAY_HEADER> list = budgetService.getBudgetHeader();

            for (BUDGET_DAY_HEADER entity : list) {
                JSONObject jo = new JSONObject();
                jo.put("b_month", entity.getB_month());
                jo.put("status", Constants.getStatusName(entity.getStatus()));
                jo.put("access_id", entity.getAccess_id());
                jo.put("access_date", entity.getAccess_date() == null ? "" : sdf.format(entity.getAccess_date()));
                jo.put("approve_id", entity.getApprove_id());
                jo.put("approve_date", entity.getApprove_date() == null ? "" : sdf.format(entity.getApprove_date()));
                jo.put("reject_id", entity.getReject_id());
                jo.put("reject_date", entity.getReject_date() == null ? "" : sdf.format(entity.getReject_date()));
                jo.put("reason", entity.getReturnReason());
                rows.put(jo);
            }

            retObj.put("Success", "Y");
            retObj.put("rows", rows);
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("getBudgetHeader error: {}", e.getMessage(), e);
        }
    }

    @PostMapping(value = "/getBudgetDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getBudgetDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject retObj = new JSONObject();

        try {
            JSONObject jsonObj = MyJSON.readJson(request);

            String bMonth = jsonObj.getString("b_month");
            String sidx = jsonObj.optString("sidx", "");
            String sord = jsonObj.optString("sord", "");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONArray rows = new JSONArray();
            List<BUDGET_DAY_DETAIL> list = budgetService.getBudgetDetail(bMonth, sidx, sord);

            for (BUDGET_DAY_DETAIL entity : list) {
                JSONObject jo = new JSONObject();
                jo.put("b_month", entity.getB_month());
                jo.put("floor", entity.getFloor());
                jo.put("dept_id", entity.getDept_id());
                jo.put("dept_name", entity.getDept_name());
                jo.put("counter_id", entity.getCounter_id());
                jo.put("counter_name", entity.getCounter_name());
                jo.put("org_name", entity.getOrg_name());
                jo.put("b_01", entity.getB_01());
                jo.put("b_02", entity.getB_02());
                jo.put("b_03", entity.getB_03());
                jo.put("b_04", entity.getB_04());
                jo.put("b_05", entity.getB_05());
                jo.put("b_06", entity.getB_06());
                jo.put("b_07", entity.getB_07());
                jo.put("b_08", entity.getB_08());
                jo.put("b_09", entity.getB_09());
                jo.put("b_10", entity.getB_10());
                jo.put("b_11", entity.getB_11());
                jo.put("b_12", entity.getB_12());
                jo.put("b_13", entity.getB_13());
                jo.put("b_14", entity.getB_14());
                jo.put("b_15", entity.getB_15());
                jo.put("b_16", entity.getB_16());
                jo.put("b_17", entity.getB_17());
                jo.put("b_18", entity.getB_18());
                jo.put("b_19", entity.getB_19());
                jo.put("b_20", entity.getB_20());
                jo.put("b_21", entity.getB_21());
                jo.put("b_22", entity.getB_22());
                jo.put("b_23", entity.getB_23());
                jo.put("b_24", entity.getB_24());
                jo.put("b_25", entity.getB_25());
                jo.put("b_26", entity.getB_26());
                jo.put("b_27", entity.getB_27());
                jo.put("b_28", entity.getB_28());
                jo.put("b_29", entity.getB_29());
                jo.put("b_30", entity.getB_30());
                jo.put("b_31", entity.getB_31());
                rows.put(jo);
            }

            retObj.put("Success", "Y");
            retObj.put("total", list.size());
            retObj.put("rows", rows);
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("getBudgetDetail error: {}", e.getMessage(), e);
        }
    }

    // ==================== 檔案上傳 (使用 Spring MultipartFile) ====================

    @PostMapping(value = "/uploadExcel", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("b_month") String bMonth,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject retObj = new JSONObject();

        HttpSession session = request.getSession();
        if (session.getAttribute("login") == null) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "請先登入再上傳檔案");
            response.getWriter().print(retObj);
            return;
        }

        Login login = (Login) session.getAttribute("login");

        try {
            if (file == null || file.isEmpty()) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "請選擇要上傳的檔案");
                response.getWriter().print(retObj);
                return;
            }

            String fileName = file.getOriginalFilename();
            log.info("Upload file: {}, size: {}", fileName, file.getSize());

            // 建立上傳目錄
            String uploadPath = uploadContentRoot + "/Budget/" + login.getLoginId();
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 儲存檔案
            File targetFile = new File(uploadPath, fileName);
            file.transferTo(targetFile);

            // 讀取 Excel
            JSONArray rows = new JSONArray();
            List<BUDGET_DAY_DETAIL> detailList = BudgetUploadExcel.importBudgetExcel(
                    uploadPath + "/" + fileName
            );

            for (BUDGET_DAY_DETAIL entity : detailList) {
                JSONObject jo = new JSONObject();
                jo.put("b_month", bMonth);
                jo.put("floor", entity.getFloor());
                jo.put("dept_id", entity.getDept_id());
                jo.put("dept_name", entity.getDept_name());
                jo.put("counter_id", entity.getCounter_id());
                jo.put("counter_name", entity.getCounter_name());
                jo.put("org_name", entity.getOrg_name());
                jo.put("b_01", entity.getB_01());
                jo.put("b_02", entity.getB_02());
                jo.put("b_03", entity.getB_03());
                jo.put("b_04", entity.getB_04());
                jo.put("b_05", entity.getB_05());
                jo.put("b_06", entity.getB_06());
                jo.put("b_07", entity.getB_07());
                jo.put("b_08", entity.getB_08());
                jo.put("b_09", entity.getB_09());
                jo.put("b_10", entity.getB_10());
                jo.put("b_11", entity.getB_11());
                jo.put("b_12", entity.getB_12());
                jo.put("b_13", entity.getB_13());
                jo.put("b_14", entity.getB_14());
                jo.put("b_15", entity.getB_15());
                jo.put("b_16", entity.getB_16());
                jo.put("b_17", entity.getB_17());
                jo.put("b_18", entity.getB_18());
                jo.put("b_19", entity.getB_19());
                jo.put("b_20", entity.getB_20());
                jo.put("b_21", entity.getB_21());
                jo.put("b_22", entity.getB_22());
                jo.put("b_23", entity.getB_23());
                jo.put("b_24", entity.getB_24());
                jo.put("b_25", entity.getB_25());
                jo.put("b_26", entity.getB_26());
                jo.put("b_27", entity.getB_27());
                jo.put("b_28", entity.getB_28());
                jo.put("b_29", entity.getB_29());
                jo.put("b_30", entity.getB_30());
                jo.put("b_31", entity.getB_31());
                rows.put(jo);
            }

            retObj.put("Success", "Y");
            retObj.put("rows", rows);
            retObj.put("total", detailList.size());
            retObj.put("FileName", fileName);
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("uploadExcel error: {}", e.getMessage(), e);
        }
    }

    // ==================== 預算儲存與提交 ====================

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("login");

        try {
            String bMonth = jsonObj.getString("b_month");

            BUDGET_DAY_HEADER header = budgetService.getStatusByMonth(bMonth);
            if (header.getStatus() != null &&
                    ("approved".equals(header.getStatus()) || "approving".equals(header.getStatus()))) {
                throw new Exception(bMonth + " 資料已在 \"審核中\" 或 \"已審核\" 無法儲存資料");
            }

            synchronized (this) {
                JSONArray dataArray = jsonObj.getJSONArray("budget_data");
                List<OBA_FILE> noneCounterList = budgetService.getNoneCounterList(bMonth, dataArray);

                if (noneCounterList != null && !noneCounterList.isEmpty()) {
                    JSONArray rows = new JSONArray();
                    for (OBA_FILE bean : noneCounterList) {
                        JSONObject jo = new JSONObject();
                        jo.put("excel_floor", bean.getEXCEL_LNT09());
                        jo.put("excel_dept_id", bean.getEXCEL_OBA01());
                        jo.put("floor", bean.getLNT09());
                        jo.put("dept_id", bean.getOBA01());
                        jo.put("dept_name", bean.getOBA02());
                        jo.put("counter_id", bean.getLNT06());
                        jo.put("counter_name", bean.getTQA02());
                        rows.put(jo);
                    }

                    retObj.put("Success", "N");
                    retObj.put("rows", rows);
                    retObj.put("ErrorMessage", bMonth + " 預算資料尚未處理完成，尚有部分櫃位的預算全部為0");
                    response.getWriter().print(retObj);
                    return;
                }

                List<OBA_FILE> counterList = budgetService.getCounterList(bMonth);
                budgetService.insertCounterData(bMonth, counterList);
                budgetService.saveBudget(bMonth, dataArray, login.getLoginId());
                budgetService.saveApplyId(bMonth, login.getLoginId());

                List<BUDGET_DAY_DETAIL> completionList = budgetService.isCompletion(bMonth);
                if (completionList.size() <= 0) {
                    String content = bMonth + " 預算已提出申請，請至報表管理系統進行審核。<br>" +
                            "<a href='http://inner.beyondplaza.com.tw/Report'>http://inner.beyondplaza.com.tw/Report</a><p>系統管理員";
                    budgetService.scheduleMail("預算請求核准通知", content, "BudgetApply", "", "", "");
                    budgetService.updateStatus(bMonth, "approving");
                }
            }

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("save error: {}", e.getMessage(), e);
        }
    }

    @PostMapping(value = "/updateFloor", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateFloor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        try {
            String bMonth = jsonObj.getString("b_month");

            List<OBA_FILE> counterList = budgetService.getCounterList(bMonth);
            Map<String, String> floorMap = new HashMap<>();
            for (OBA_FILE record : counterList) {
                floorMap.put(record.getLNT06(), record.getLNT09());
            }

            budgetService.updateFloor(bMonth, floorMap);

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("updateFloor error: {}", e.getMessage(), e);
        }
    }

    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public void submit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("login");

        List<BUDGET_DAY_DETAIL> completionList = null;

        try {
            String bMonth = jsonObj.getString("b_month");
            String action = jsonObj.getString("action");

            if ("Yes".equalsIgnoreCase(action)) {
                completionList = budgetService.isCompletion(bMonth);
                if (completionList.size() > 0) {
                    throw new Exception("有部分櫃位的預算全部為0，請檢查後再行核准");
                }

                budgetService.approve(bMonth, login.getLoginId(), false);

                String content = bMonth + " 預算已核准。<br>" +
                        "<a href='http://inner.beyondplaza.com.tw/Report'>http://inner.beyondplaza.com.tw/Report</a><p>系統管理員";
                List<BUDGET_DAY_APPLY> applyList = budgetService.getBudgetApply(bMonth);
                StringBuilder emailSb = new StringBuilder();
                for (BUDGET_DAY_APPLY entity : applyList) {
                    emailSb.append(entity.getEmail()).append(";");
                }
                budgetService.scheduleMail("日預算核准通知", content, "BudgetApprove", emailSb.toString(), "", "");

            } else if ("No".equalsIgnoreCase(action)) {
                budgetService.reject(bMonth, login.getLoginId(), jsonObj.getString("returnReason"));

                String content = bMonth + " 預算已退回，退回原因：<p>" +
                        jsonObj.getString("returnReason") +
                        "<p><a href='http://inner.beyondplaza.com.tw/Report'>http://inner.beyondplaza.com.tw/Report</a><p>系統管理員";
                List<BUDGET_DAY_APPLY> applyList = budgetService.getBudgetApply(bMonth);
                StringBuilder emailSb = new StringBuilder();
                for (BUDGET_DAY_APPLY entity : applyList) {
                    emailSb.append(entity.getEmail()).append(";");
                }
                budgetService.scheduleMail("預算退回通知", content, "BudgetApprove", emailSb.toString(), "", "");
            }

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            JSONArray rows = new JSONArray();
            if (completionList != null && !completionList.isEmpty()) {
                for (BUDGET_DAY_DETAIL bean : completionList) {
                    JSONObject jo = new JSONObject();
                    jo.put("counter_id", bean.getCounter_id());
                    jo.put("counter_name", bean.getCounter_name());
                    rows.put(jo);
                }
            }

            retObj.put("Success", "N");
            retObj.put("rows", rows);
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("submit error: {}", e.getMessage(), e);
        }
    }

    // ==================== 刪除功能 ====================

    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        try {
            budgetService.delete(
                    jsonObj.getString("b_month"),
                    jsonObj.getString("dept_id"),
                    jsonObj.getString("counter_id")
            );

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("delete error: {}", e.getMessage(), e);
        }
    }

    @PostMapping(value = "/delete0", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete0(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        try {
            budgetService.delete0(jsonObj.getString("b_month"));

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("delete0 error: {}", e.getMessage(), e);
        }
    }

    // ==================== 例外專櫃管理 ====================

    @PostMapping(value = "/getExceptCounter", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getExceptCounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        try {
            JSONArray rows = new JSONArray();
            List<BUDGET_COUNTER_EXCEPT> list = budgetService.getExceptCounter(jsonObj);

            for (BUDGET_COUNTER_EXCEPT bean : list) {
                JSONObject jo = new JSONObject();
                jo.put("counter_id", bean.getCounter_id());
                jo.put("counter_name", bean.getCounter_name());
                rows.put(jo);
            }

            retObj.put("Success", "Y");
            retObj.put("rows", rows);
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("getExceptCounter error: {}", e.getMessage(), e);
        }
    }

	@PostMapping(value = "/addExceptCounter", produces = MediaType.APPLICATION_JSON_VALUE)
	public void addExceptCounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(CONTENT_TYPE_JSON);
	
	    JSONObject jsonObj = MyJSON.readJson(request);
	    JSONObject retObj = new JSONObject();
	
	    try {
	        String counterId = jsonObj.getString("counter_id");
	
	        // 檢查是否已存在
	        List<BUDGET_COUNTER_EXCEPT> existList = budgetService.getExceptCounter(jsonObj);
	        if (!existList.isEmpty()) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", counterId + " 專櫃資料已經存在");
	            response.getWriter().print(retObj);
	            return;
	        }
	
	        // 取得櫃位資訊
	        LNT_FILE counterEntity = budgetService.getCounterById(jsonObj);
	        
	        // 檢查是否找到櫃位資訊
	        if (counterEntity == null) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", "找不到櫃位編號：" + counterId);
	            response.getWriter().print(retObj);
	            return;
	        }
	
	        // 檢查 TQA02 是否為空
	        String counterName = counterEntity.getTQA02();
	        if (counterName == null || counterName.isEmpty()) {
	            counterName = counterId; // 使用櫃位 ID 作為名稱
	        }
	
	        budgetService.addExceptCounter(counterId, counterName);
	
	        retObj.put("counter_id", counterId);
	        retObj.put("Success", "Y");
	        response.getWriter().print(retObj);
	
	    } catch (Exception e) {
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", e.getMessage());
	        response.getWriter().print(retObj);
	        log.error("addExceptCounter error: {}", e.getMessage(), e);
	    }
	}

	@PostMapping(value = "/delExceptCounter", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delExceptCounter(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(CONTENT_TYPE_JSON);
	
	    JSONObject jsonObj = MyJSON.readJson(request);
	    JSONObject retObj = new JSONObject();
	
	    try {
	        String counterId = jsonObj.getString("counter_id");
	
	        // 檢查是否存在
	        List<BUDGET_COUNTER_EXCEPT> existList = budgetService.getExceptCounter(jsonObj);
	        if (existList.isEmpty()) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", counterId + " 專櫃資料不存在");
	            response.getWriter().print(retObj);
	            return;
	        }
	
	        budgetService.delExceptCounter(counterId);
	
	        retObj.put("counter_id", counterId);
	        retObj.put("Success", "Y");
	        response.getWriter().print(retObj);
	
	    } catch (Exception e) {
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", e.getMessage());
	        response.getWriter().print(retObj);
	        log.error("delExceptCounter error: {}", e.getMessage(), e);
	    }
	}

    // ==================== 檢查與重算 ====================

    @PostMapping(value = "/isCompletion", produces = MediaType.APPLICATION_JSON_VALUE)
    public void isCompletion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        try {
            String bMonth = jsonObj.getString("b_month");

            BUDGET_DAY_HEADER header = budgetService.getStatusByMonth(bMonth);
            if (header.getStatus() == null) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "查無 " + bMonth + " 資料");
            } else if (!"draft".equals(header.getStatus()) && !"approving".equals(header.getStatus())) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "只有狀態在 \"匯入中\" 或 \"審核中\" 才可進行檢查");
            } else {
                synchronized (this) {
                    List<BUDGET_DAY_DETAIL> completionList = budgetService.isCompletion(bMonth);
                    if (completionList.size() <= 0) {
                        String content = bMonth + " 預算已提出申請，請至報表管理系統進行審核。<br>" +
                                "<a href='http://inner.beyondplaza.com.tw/Report'>http://inner.beyondplaza.com.tw/Report</a><p>系統管理員";
                        budgetService.scheduleMail("預算請求核准通知", content, "BudgetApply", "", "", "");
                        budgetService.updateStatus(bMonth, "approving");
                        retObj.put("Success", "Y");
                    } else {
                        JSONArray rows = new JSONArray();
                        for (BUDGET_DAY_DETAIL bean : completionList) {
                            JSONObject jo = new JSONObject();
                            jo.put("counter_id", bean.getCounter_id());
                            jo.put("counter_name", bean.getCounter_name());
                            rows.put(jo);
                        }
                        retObj.put("Success", "N");
                        retObj.put("rows", rows);
                        retObj.put("ErrorMessage", bMonth + " 預算資料尚未處理完成，尚有部分櫃位的預算全部為0");
                    }
                }
            }
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("isCompletion error: {}", e.getMessage(), e);
        }
    }

    @PostMapping(value = "/reCalc", produces = MediaType.APPLICATION_JSON_VALUE)
    public void reCalc(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);

        JSONObject jsonObj = MyJSON.readJson(request);
        JSONObject retObj = new JSONObject();

        HttpSession session = request.getSession();
        Login login = (Login) session.getAttribute("login");

        try {
            budgetService.approve(jsonObj.getString("b_month"), login.getLoginId(), true);

            retObj.put("Success", "Y");
            response.getWriter().print(retObj);

        } catch (Exception e) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());
            response.getWriter().print(retObj);
            log.error("reCalc error: {}", e.getMessage(), e);
        }
    }
}