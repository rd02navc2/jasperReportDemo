package com.beyond.report.controller;

import com.beyond.report.file.AssetUploadExcel;
import com.beyond.report.bean.ResponseBean;
import com.beyond.report.entity.ASSET_INVENTORY;
import com.beyond.report.entity.ASSET_INVENTORY_HEADER;
import com.beyond.report.entity.ASSET_SCRAPPED;
import com.beyond.report.entity.ASSET_TYPE;
import com.beyond.report.entity.ATTENDANCEEMPRANK;
import com.beyond.report.entity.EMPLOYEE;
import com.beyond.report.entity.LOGDB;
import com.beyond.report.entity.Login;
import com.beyond.report.projection.AssetInventoryProjection;
import com.beyond.report.service.AssetService;
import com.beyond.report.service.BudgetService;
import com.beyond.report.service.DownlodSensitiveService;
import com.beyond.report.service.ReportService;
import com.beyond.report.util.GetDateTime;
import com.beyond.report.util.OffPunchExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/Report/rest/Asset")
public class AssetController {

    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

    // ==================== 使用建構子注入 ====================
    private final ReportService reportService;
    private final AssetService assetService;
    private final BudgetService budgetService;
    private final ObjectMapper objectMapper;
    private final DownlodSensitiveService downlodSensitiveService;

    @Value("${download.content.root}")
    private String downloadContentRoot;

    // 建構子注入（Spring 4.3+ 可省略 @Autowired）
    public AssetController(ReportService reportService,
                           AssetService assetService,
                           BudgetService budgetService,
                           ObjectMapper objectMapper,
                           DownlodSensitiveService downlodSensitiveService) {
        this.reportService = reportService;
        this.assetService = assetService;
        this.budgetService = budgetService;
        this.objectMapper = objectMapper;
        this.downlodSensitiveService = downlodSensitiveService;
    }

    // ==================== 1. 取得盤點日期列表 ====================
    @PostMapping(value = "/getInventoryDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getInventoryDate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> rows = new ArrayList<>();

            List<ASSET_INVENTORY_HEADER> list = assetService.getInventoryDate();

            if (list != null) {
                for (ASSET_INVENTORY_HEADER entity : list) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("inventory_date", entity.getInventory_date() != null ? entity.getInventory_date() : "");
                    String generateType = "1".equals(entity.getGenerate_type()) ? "匯入" : "發起盤點";
                    row.put("generate_type", generateType);
                    String closeDateStr = (entity.getClose_date() == null) ? "" : sdf.format(entity.getClose_date());
                    row.put("close_date", closeDateStr);
                    row.put("remark", entity.getRemark() != null ? entity.getRemark() : "");
                    rows.add(row);
                }
            }

            retObj.put("Success", "Y");
            retObj.put("rows", rows);

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }

        } catch (Exception e) {
            log.error("getInventoryDate 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage() != null ? e.getMessage() : "系統發生未知錯誤");

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
        }
    }

    // ==================== 2. 取得資產盤點明細 ====================
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/getAssetInventory", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getAssetInventory(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        try {
            Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);

            String inventoryDate = reqMap.get("inventory_date") != null ? reqMap.get("inventory_date").toString() : "";
            String sidx = reqMap.get("sidx") != null ? reqMap.get("sidx").toString() : "self_no";
            String sord = reqMap.get("sord") != null ? reqMap.get("sord").toString() : "ASC";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> rows = new ArrayList<>();

            List<AssetInventoryProjection> list = assetService.getAssetInventory(inventoryDate, sidx, sord);

            if (list != null) {
                for (AssetInventoryProjection entity : list) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("inventory_date", entity.getInventory_date() != null ? entity.getInventory_date() : "");
                    row.put("self_no", entity.getSelf_no() != null ? entity.getSelf_no() : "");
                    row.put("model_no", entity.getModel_no() != null ? entity.getModel_no() : "");
                    String typeStr = (entity.getType() != null ? entity.getType() : "")
                            + " "
                            + (entity.getType_name() != null ? entity.getType_name() : "");
                    row.put("type", typeStr.trim());
                    row.put("prod_desc", entity.getProd_desc() != null ? entity.getProd_desc() : "");
                    row.put("owner_id", entity.getOwner_id() != null ? entity.getOwner_id() : "");
                    row.put("owner_name", entity.getOwner_name() != null ? entity.getOwner_name() : "");
                    row.put("title", entity.getTitle() != null ? entity.getTitle() : "");
                    row.put("dept_name", entity.getDept_name() != null ? entity.getDept_name() : "");
                    row.put("confirm_id", entity.getConfirm_id() != null ? entity.getConfirm_id() : "");
                    String confirmDateStr = (entity.getConfirm_date() == null) ? "" : sdf.format(entity.getConfirm_date());
                    row.put("confirm_date", confirmDateStr);
                    row.put("maintain_hist", entity.getMaintain_hist() != null ? entity.getMaintain_hist() : "");
                    row.put("memo_hist", entity.getMemo_hist() != null ? entity.getMemo_hist() : "");
                    rows.add(row);
                }
            }

            retObj.put("Success", "Y");
            retObj.put("rows", rows);

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }

        } catch (Exception e) {
            log.error("getAssetInventory 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage() != null ? e.getMessage() : "系統處理失敗");

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
        }
    }

    // ==================== 3. 取得資產類型下拉選單 (回傳 HTML) ====================
    @GetMapping(value = "/getAssetType", produces = MediaType.TEXT_HTML_VALUE)
    public void getAssetType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<select>");
            sb.append("<option value='choice'>- 請選擇 -</option>");

            List<ASSET_TYPE> typeList = assetService.getAssetType();
            for (ASSET_TYPE type : typeList) {
                sb.append("<option value='")
                        .append(type.getType_id())
                        .append("'>")
                        .append(type.getType_id())
                        .append(" ")
                        .append(type.getType_name())
                        .append("</option>");
            }

            sb.append("</select>");
            writer.println(sb.toString());
            writer.flush();

        } catch (Exception e) {
            log.error("getAssetType 執行失敗: ", e);
            try (PrintWriter writer = response.getWriter()) {
                writer.println("<select><option value=''>- 查詢失敗 -</option></select>");
                writer.flush();
            }
        }
    }

    // ==================== 4. 上傳 Excel 檔案 ====================
    @PostMapping(value = "/uploadExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadExcel(
            @RequestParam MultipartFile file,
            HttpServletRequest request, 
            HttpServletResponse response) throws Exception {

        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        // 1. 檢查 Session 登入狀態
        /*
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("login") == null) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "請先登入再上傳檔案");
            writeJsonResponse(response, retObj);
            return;
        }

        Login login = (Login) session.getAttribute("login");
        */
        
     // 1. 檢查 Session 登入狀態 (測試用：傳入 true 強制建立 Session 並跳過驗證)
        HttpSession session = request.getSession(true);

        Login login = (Login) session.getAttribute("login");

        // 若 session 中還沒有 login 物件，手動建立一個預設值塞入，確保後續程式碼運作正常
        if (login == null) {
            login = new Login();
            login.setLoginId("TEST_USER"); // 設定測試用的帳號名稱
            session.setAttribute("login", login);
        }

        try {
            // 2. 檢查檔案是否為空
            if (file == null || file.isEmpty()) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "上傳檔案不能為空");
                writeJsonResponse(response, retObj);
                return;
            }

            String fileName = file.getOriginalFilename();
            log.info("使用者 {} 上傳資產 Excel 檔：{} (大小: {} bytes)", login.getLoginId(), fileName, file.getSize());

            // 3. 建立伺服器儲存路徑 ({downloadContentRoot}/Asset/{loginId})
            Path uploadDir = Paths.get(downloadContentRoot, "Asset", login.getLoginId());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path savePath = uploadDir.resolve(fileName);
            // 將檔案傳輸儲存至指定檔案系統
            file.transferTo(savePath.toFile());

            // 4. 解析 Excel 檔 (使用專案內的 AssetUploadExcel 工具類)
            List<ASSET_INVENTORY> assetList = AssetUploadExcel.importAssetExcel(savePath.toAbsolutePath().toString());
            List<Map<String, Object>> rows = new ArrayList<>();

            if (assetList != null) {
                for (ASSET_INVENTORY entity : assetList) {
                    Map<String, Object> rowMap = new HashMap<>();

                    // 取得員工基本資料
                    com.beyond.report.entity.EMPLOYEE emp = assetService.getEmployeeByCode(entity.getOwner_id());
                    // 取得資產庫存檔
                    ASSET_INVENTORY inv = assetService.getAssetInventoryBySelfNo(entity.getSelf_no());

                    rowMap.put("self_no", entity.getSelf_no() != null ? entity.getSelf_no() : "");
                    rowMap.put("model_no", entity.getModel_no() != null ? entity.getModel_no() : "");
                    rowMap.put("type", entity.getType() != null ? entity.getType() : "");
                    rowMap.put("prod_desc", entity.getProd_desc() != null ? entity.getProd_desc() : "");
                    rowMap.put("owner_id", entity.getOwner_id() != null ? entity.getOwner_id() : "");

                    // 避免 emp 為 null 時引發 NullPointerException
                    rowMap.put("owner_name", emp != null && emp.getCNNAME() != null ? emp.getCNNAME() : "");
                    rowMap.put("title", emp != null && emp.getTITLE() != null ? emp.getTITLE() : "");
                    rowMap.put("dept_name", emp != null && emp.getDEPT_NAME() != null ? emp.getDEPT_NAME() : "");

                    // 避免 inv 為 null 時引發 NullPointerException
                    rowMap.put("maintain_hist", inv != null && inv.getMaintain_hist() != null ? inv.getMaintain_hist() : "");
                    rowMap.put("memo_hist", inv != null && inv.getMemo_hist() != null ? inv.getMemo_hist() : "");

                    rows.add(rowMap);
                }
            }

            // 5. 組合回傳資料
            retObj.put("Success", "Y");
            retObj.put("rows", rows);
            retObj.put("total", rows.size());
            retObj.put("FileName", fileName);

        } catch (Exception e) {
            log.error("uploadExcel 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "檔案讀取或解析失敗: " + e.getMessage());
        }

        // 輸出 JSON
        writeJsonResponse(response, retObj);
    }

    /**
     * 輔助方法：統一將物件轉為 JSON 並寫回 HttpServletResponse
     */
    private void writeJsonResponse(HttpServletResponse response, Object obj) throws Exception {
        try (PrintWriter out = response.getWriter()) {
            out.print(objectMapper.writeValueAsString(obj));
            out.flush();
        }
    }

    // ==================== 5. 儲存匯入的資產資料 ====================
    @PostMapping(value = "/saveImport", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveImport(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        // 1. 取得 Session 登入狀態 (測試階段設定為 true 自動建立，生產環境可改回 false)
        HttpSession session = request.getSession(true);
        Login login = (Login) session.getAttribute("login");

        // 若無 login 物件則給予測試用預設值 (避免 NPE)
        if (login == null) {
            login = new Login();
            login.setLoginId("TEST_USER");
            session.setAttribute("login", login);
        }

        try {
            // 2. 解析前端傳入的 JSON Request Body
            @SuppressWarnings("unchecked")
			Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);
            
            if (reqMap == null || !reqMap.containsKey("asset_inventory_data")) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "缺少必要參數 asset_inventory_data");
                writeJsonResponse(response, retObj);
                return;
            }

            // 3. 將物件轉為 JSONArray 帶入 Service 寫入資料庫
            Object dataObj = reqMap.get("asset_inventory_data");
            
            // 使用 Jackson 將 List/Object 轉為 org.json.JSONArray，確保相容 assetService
            String jsonArrayStr = objectMapper.writeValueAsString(dataObj);
            org.json.JSONArray jaData = new org.json.JSONArray(jsonArrayStr);

            // 4. 呼叫 Service 執行資料庫儲存/更新 (MySQL)
            assetService.saveImport(jaData, login.getLoginId());

            // 5. 成功回傳
            retObj.put("Success", "Y");
            retObj.put("Message", "匯入成功");

        } catch (Exception e) {
            log.error("saveImport 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "儲存失敗: " + e.getMessage());
        }

        // 6. 輸出 JSON 回傳
        writeJsonResponse(response, retObj);
    }

    // ==================== 6. 儲存資產盤點資料 ====================
    @PostMapping(value = "/saveAsset", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveAsset(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        // 1. 取得 Session 登入狀態 (測試階段設定為 true 自動建立，生產環境可改回 false)
        HttpSession session = request.getSession(true);
        Login login = (Login) session.getAttribute("login");

        // 若無 login 物件則給予測試用預設值 (避免 NPE)
        if (login == null) {
            login = new Login();
            login.setLoginId("TEST_USER");
            session.setAttribute("login", login);
        }

        try {
            // 2. 解析前端傳入的 JSON Request Body
            @SuppressWarnings("unchecked")
			Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);
            
            if (reqMap == null || !reqMap.containsKey("inventory_date") || !reqMap.containsKey("asset_inventory_data")) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "缺少必要參數：inventory_date 或 asset_inventory_data");
                writeJsonResponse(response, retObj);
                return;
            }

            // 3. 取得盤點日期與資產明細陣列
            String inventoryDate = String.valueOf(reqMap.get("inventory_date"));
            Object dataObj = reqMap.get("asset_inventory_data");

            // 使用 Jackson 將 List/Object 轉為 org.json.JSONArray，相容舊有 Service 規格
            String jsonArrayStr = objectMapper.writeValueAsString(dataObj);
            org.json.JSONArray jaData = new org.json.JSONArray(jsonArrayStr);

            // 4. 呼叫 Service 執行盤點資料儲存/更新
            assetService.saveAsset(inventoryDate, jaData, login.getLoginId());

            // 5. 成功回傳
            retObj.put("Success", "Y");
            retObj.put("Message", "盤點資料儲存成功");

        } catch (Exception e) {
            log.error("saveAsset 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "儲存失敗: " + e.getMessage());
        }

        // 6. 輸出 JSON 回傳
        writeJsonResponse(response, retObj);
    }

    // ==================== 7. 刪除所有資產 ====================
    @SuppressWarnings("unchecked")
	@PostMapping(value = "/deleteAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        try {
            Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);
            Object dateObj = reqMap.get("inventory_date");
            if (dateObj == null || dateObj.toString().trim().isEmpty()) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "缺少必要參數：inventory_date");
                writeJsonResponse(response, retObj);
                return;
            }
            String inventoryDate = dateObj.toString().trim();

            assetService.deleteAll(inventoryDate);

            retObj.put("Success", "Y");
            retObj.put("Message", "刪除成功");

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }

        } catch (Exception e) {
            log.error("deleteAll 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
        }
    }

    // ==================== 8. 刪除單一資產 ====================
	@PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(CONTENT_TYPE_JSON);
	    response.setCharacterEncoding("UTF-8");
	
	    Map<String, Object> retObj = new HashMap<>();
	
	    // 1. Session 檢查與預設測試值
	    HttpSession session = request.getSession(true);
	    Login login = (Login) session.getAttribute("login");
	    if (login == null) {
	        login = new Login();
	        login.setLoginId("TEST_USER");
	        session.setAttribute("login", login);
	    }
	
	    try {
	        // 2. 解析 JSON Request Body
	        @SuppressWarnings("unchecked")
	        Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);
	
	        // 3. 參數檢查
	        if (reqMap == null || !reqMap.containsKey("inventory_date") || !reqMap.containsKey("self_no")) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", "缺少必要參數：inventory_date 或 self_no");
	            writeJsonResponse(response, retObj);
	            return;
	        }
	
	        String inventoryDate = String.valueOf(reqMap.get("inventory_date"));
	        String selfNo = String.valueOf(reqMap.get("self_no"));
	
	        // 4. 執行刪除單筆資料
	        assetService.delete(inventoryDate, selfNo, login.getLoginId());
	
	        // 5. 回傳成功結果
	        retObj.put("Success", "Y");
	        retObj.put("Message", "單筆資產刪除成功");
	
	    } catch (Exception e) {
	        log.error("delete 執行失敗: ", e);
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", "刪除失敗: " + e.getMessage());
	    }
	
	    writeJsonResponse(response, retObj);
	}

    // ==================== 9. 報廢資產 ====================
	@PostMapping(value = "/scrapped", produces = MediaType.APPLICATION_JSON_VALUE)
	public void scrapped(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(CONTENT_TYPE_JSON);
	    response.setCharacterEncoding("UTF-8");
	
	    Map<String, Object> retObj = new HashMap<>();
	
	    // 1. Session 檢查與預設測試值 (修正大括號閉合)
	    HttpSession session = request.getSession(true);
	    Login login = (Login) session.getAttribute("login");
	    if (login == null) {
	        login = new Login();
	        login.setLoginId("TEST_USER");
	        session.setAttribute("login", login);
	    }
	
	    try {
	        // 2. 解析 JSON Request Body
	        @SuppressWarnings("unchecked")
	        Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);
	
	        // 3. 參數檢核 (避免 NPE)
	        if (reqMap == null || !reqMap.containsKey("inventory_date") || !reqMap.containsKey("self_no")) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", "缺少必要參數：inventory_date 或 self_no");
	            writeJsonResponse(response, retObj);
	            return;
	        }
	
	        String inventoryDate = String.valueOf(reqMap.get("inventory_date"));
	        String selfNo = String.valueOf(reqMap.get("self_no"));
	
	        // 4. 呼叫 Service 執行報廢
	        assetService.scrapped(inventoryDate, selfNo, login.getLoginId());
	
	        // 5. 設定成功訊息
	        retObj.put("Success", "Y");
	        retObj.put("Message", "報廢成功");
	
	    } catch (Exception e) {
	        log.error("scrapped 執行失敗: ", e);
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", "報廢失敗: " + e.getMessage());
	    }
	
	    // 6. 統一輸出 JSON 回傳
	    writeJsonResponse(response, retObj);
	}

    // ==================== 10. 發起新盤點 ====================
	@PostMapping(value = "/newAsset", produces = MediaType.APPLICATION_JSON_VALUE)
	public void newAsset(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(CONTENT_TYPE_JSON);
	    response.setCharacterEncoding("UTF-8");
	
	    Map<String, Object> retObj = new HashMap<>();
	
	    // 1. Session 檢查與預設測試值 (開發測試防護)
	    HttpSession session = request.getSession(true);
	    Login login = (Login) session.getAttribute("login");
	    if (login == null) {
	        login = new Login();
	        login.setLoginId("TEST_USER");
	        session.setAttribute("login", login);
	    }
	
	    try {
	        // 2. 驗證是否符合發起盤點條件
	        ResponseBean bean = assetService.isValidate();
	        if (!"0000".equals(bean.getCode())) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", bean.getMessage());
	        } else {
	            // 3. 執行發起資產盤點
	            assetService.newAsset(login.getLoginId());
	            retObj.put("Success", "Y");
	            retObj.put("Message", "盤點發起成功");
	
	            // 4. 排程寄發通知信
	            String content = "各位同事<p>個人設備資產盤點已經開始，請至報表管理系統進行盤點確認。<p>"
	                    + "請使用Google Chrome登入下列網址<br>"
	                    + "<a href='http://inner.beyondplaza.com.tw/Report'>http://inner.beyondplaza.com.tw/Report</a><br>"
	                    + "帳號、密碼與電腦登入之帳密相同<p>系統管理員";
	            budgetService.scheduleMail("個人設備資產盤點通知", content, "NewAsset", "", "", "");
	        }
	
	    } catch (Exception e) {
	        log.error("newAsset 執行失敗: ", e);
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", "發起失敗: " + e.getMessage());
	    }
	
	    // 5. 統一輸出 JSON 回傳
	    writeJsonResponse(response, retObj);
	}
	
    // ==================== 11. 取得員工資產 ====================
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/getStaffAsset", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getStaffAsset(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setCharacterEncoding("UTF-8");
	
	    Map<String, Object> retObj = new HashMap<>();
	
	    // 1. 取得 Session，如果不存在則建立一個新的 (true)
	    HttpSession session = request.getSession(true);
	    Login login = (Login) session.getAttribute("login");
	
	    // 2. 本地開發/測試用的 Mock 登入機制
	    if (login == null) {
	        login = new Login();
	        login.setLoginId("TEST_USER"); // 建議改成資料庫實際存在的員工帳號 (例如 EMP001)
	        session.setAttribute("login", login);
	        log.warn("目前使用測試帳號 [{}] 自動登入中...", login.getLoginId());
	    }
	
	    try {
	        // 讀取前端傳入的 JSON Payload
	        Map<String, Object> reqMap = new HashMap<>();
	        try {
	            reqMap = objectMapper.readValue(request.getInputStream(), Map.class);
	        } catch (Exception e) {
	            // 防止前端傳空 Body 導致 JSON 解析 failure
	            log.debug("Request body 為空，使用預設排序參數");
	        }
	
	        String sidx = (reqMap != null && reqMap.get("sidx") != null) ? reqMap.get("sidx").toString() : "self_no";
	        String sord = (reqMap != null && reqMap.get("sord") != null) ? reqMap.get("sord").toString() : "ASC";
	
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        List<Map<String, Object>> rows = new ArrayList<>();
	
	        // 3. 取得員工資訊
	        com.beyond.report.entity.EMPLOYEE emp = assetService.getEmployeeById(login.getLoginId());
	        
	        if (emp == null) {
	            retObj.put("Success", "N");
	            retObj.put("ErrorMessage", "查無員工資料，LoginId: " + login.getLoginId());
	            try (PrintWriter out = response.getWriter()) {
	                out.print(objectMapper.writeValueAsString(retObj));
	                out.flush();
	            }
	            return;
	        }
	
	        // 4. 查詢資產盤點資料
	        List<com.beyond.report.entity.ASSET_INVENTORY> list = assetService.getAssetInventoryByCode(
	                emp.getCODE(), sidx, sord);
	
	        retObj.put("AssetStatus", "Y");
	
	        for (com.beyond.report.entity.ASSET_INVENTORY entity : list) {
	            if (!"1".equals(entity.getGenerate_type()) && entity.getConfirm_date() == null) {
	                retObj.put("AssetStatus", "N");
	            }
	
	            Map<String, Object> row = new HashMap<>();
	            row.put("inventory_date", entity.getInventory_date() != null ? entity.getInventory_date() : "");
	            row.put("self_no", entity.getSelf_no() != null ? entity.getSelf_no() : "");
	            row.put("model_no", entity.getModel_no() != null ? entity.getModel_no() : "");
	            
	            String typeStr = (entity.getType() != null ? entity.getType() : "")
	                    + " "
	                    + (entity.getType_name() != null ? entity.getType_name() : "");
	            row.put("type", typeStr.trim());
	            
	            row.put("prod_desc", entity.getProd_desc() != null ? entity.getProd_desc() : "");
	            row.put("owner_id", entity.getOwner_id() != null ? entity.getOwner_id() : "");
	            row.put("owner_name", entity.getOwner_name() != null ? entity.getOwner_name() : "");
	            row.put("title", entity.getTitle() != null ? entity.getTitle() : "");
	            row.put("dept_name", entity.getDept_name() != null ? entity.getDept_name() : "");
	            row.put("memo_hist", entity.getMemo_hist() != null ? entity.getMemo_hist() : "");
	            row.put("confirm_id", entity.getConfirm_id() != null ? entity.getConfirm_id() : "");
	            
	            String confirmDateStr = (entity.getConfirm_date() == null) ? "" : sdf.format(entity.getConfirm_date());
	            row.put("confirm_date", confirmDateStr);
	            
	            rows.add(row);
	        }
	
	        retObj.put("Success", "Y");
	        retObj.put("rows", rows);
	
	    } catch (Exception e) {
	        log.error("getStaffAsset 執行失敗: ", e);
	        retObj.put("Success", "N");
	        retObj.put("ErrorMessage", e.getMessage());
	    }
	
	    // 統一由最後輸出 Response JSON
	    try (PrintWriter out = response.getWriter()) {
	        out.print(objectMapper.writeValueAsString(retObj));
	        out.flush();
	    }
	}
	
    // ==================== 12. 取得報廢資產 ====================
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/getAssetScrapped", produces = MediaType.APPLICATION_JSON_VALUE)
    public void getAssetScrapped(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        try {
            Map<String, Object> reqMap = objectMapper.readValue(request.getInputStream(), Map.class);

            String fromDate = reqMap.get("sale_s_date") != null ? reqMap.get("sale_s_date").toString() : "";
            String endDate = reqMap.get("sale_e_date") != null ? reqMap.get("sale_e_date").toString() : "";
            String selfNo = reqMap.get("self_no") != null ? reqMap.get("self_no").toString() : "";
            String sidx = reqMap.get("sidx") != null ? reqMap.get("sidx").toString() : "self_no";
            String sord = reqMap.get("sord") != null ? reqMap.get("sord").toString() : "ASC";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> rows = new ArrayList<>();

            List<ASSET_SCRAPPED> list = assetService.getAssetScrapped(
                    fromDate, endDate, selfNo, sidx, sord);

            for (ASSET_SCRAPPED entity : list) {
                Map<String, Object> row = new HashMap<>();
                row.put("self_no", entity.getSelf_no() != null ? entity.getSelf_no() : "");
                row.put("model_no", entity.getModel_no() != null ? entity.getModel_no() : "");
                String typeStr = (entity.getType() != null ? entity.getType() : "")
                        + " "
                        + (entity.getType_name() != null ? entity.getType_name() : "");
                row.put("type", typeStr.trim());
                row.put("prod_desc", entity.getProd_desc() != null ? entity.getProd_desc() : "");
                row.put("owner_id", entity.getOwner_id() != null ? entity.getOwner_id() : "");
                row.put("owner_name", entity.getOwner_name() != null ? entity.getOwner_name() : "");
                row.put("title", entity.getTitle() != null ? entity.getTitle() : "");
                row.put("dept_name", entity.getDept_name() != null ? entity.getDept_name() : "");
                row.put("scrapped_id", entity.getScrapped_id() != null ? entity.getScrapped_id() : "");
                String scrappedDateStr = (entity.getScrapped_date() == null) ? "" : sdf.format(entity.getScrapped_date());
                row.put("scrapped_date", scrappedDateStr);
                row.put("maintain_hist", entity.getMaintain_hist() != null ? entity.getMaintain_hist() : "");
                row.put("memo_hist", entity.getMemo_hist() != null ? entity.getMemo_hist() : "");
                rows.add(row);
            }

            retObj.put("Success", "Y");
            retObj.put("rows", rows);

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }

        } catch (Exception e) {
            log.error("getAssetScrapped 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
        }
    }

    // ==================== 13. 提交確認 ====================
    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public void submit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();
       
     // 1. 取得 Session，如果不存在則建立一個新的 (true)
	    HttpSession session = request.getSession(true);
	    Login login = (Login) session.getAttribute("login");
	
	    // 2. 本地開發/測試用的 Mock 登入機制
	    if (login == null) {
	        login = new Login();
	        login.setLoginId("TEST_USER"); // 建議改成資料庫實際存在的員工帳號 (例如 EMP001)
	        session.setAttribute("login", login);
	        log.warn("目前使用測試帳號 [{}] 自動登入中...", login.getLoginId());
	    }

       try {
            EMPLOYEE emp = assetService.getEmployeeById(login.getLoginId());
            if (emp == null) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "查無員工資料，LoginId: " + login.getLoginId());
                try (PrintWriter out = response.getWriter()) {
                    out.print(objectMapper.writeValueAsString(retObj));
                    out.flush();
                }
                return;
            }
            assetService.submit(emp.getCODE());

            retObj.put("Success", "Y");
            retObj.put("Message", "提交成功");

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }

        } catch (Exception e) {
            log.error("submit 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
        }
    }

    // ==================== 14. 發送通知 ====================
    @PostMapping(value = "/inform", produces = MediaType.APPLICATION_JSON_VALUE)
    public void inform(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE_JSON);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> retObj = new HashMap<>();

        HttpSession session = request.getSession(true);
        Login login = (Login) session.getAttribute("login");

        if (login == null) {
            login = new Login();
            login.setLoginId("TEST_USER");
            session.setAttribute("login", login);
            log.warn("目前使用測試帳號 [{}] 自動登入中...", login.getLoginId());
        }
        
        if (session == null || session.getAttribute("login") == null) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "請先登入");
            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
            return;
        }

        try {
            List<ASSET_INVENTORY> assetList = assetService.getInformStaff();

            if (assetList.size() <= 0) {
                retObj.put("Success", "N");
                retObj.put("ErrorMessage", "目前沒有需要通知的人員");
            } else {
                List<EMPLOYEE> empList = assetService.getStaffMail(assetList);

                if (empList.size() <= 0) {
                    retObj.put("Success", "N");
                    retObj.put("ErrorMessage", "沒有需要通知人員的電子郵件信箱");
                } else {
                    StringBuilder emailList = new StringBuilder();
                    for (EMPLOYEE emp : empList) {
                        if (emp.getEMAIL() != null && !"".equals(emp.getEMAIL())) {
                            emailList.append(emp.getEMAIL()).append(";");
                        }
                    }

                    retObj.put("Success", "Y");
                    retObj.put("Message", "通知已發送");

                    String content = "各位同事<p>個人設備資產盤點已經開始，請盡快至報表管理系統進行盤點確認。<p>"
                            + "請使用Google Chrome登入下列網址<br>"
                            + "<a href='http://inner.beyondplaza.com.tw/Report'>http://inner.beyondplaza.com.tw/Report</a><br>"
                            + "帳號、密碼與電腦登入之帳密相同<p>系統管理員";

                    budgetService.scheduleMail("個人設備資產盤點再次提醒通知", content,
                            "InformAsset", emailList.toString(), "", "");
                }
            }

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }

        } catch (Exception e) {
            log.error("inform 執行失敗: ", e);
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", e.getMessage());

            try (PrintWriter out = response.getWriter()) {
                out.print(objectMapper.writeValueAsString(retObj));
                out.flush();
            }
        }
    }

    // ==================== 15. 產生出勤異常 Excel ====================
    @PostMapping(value = "/genOffPunchExcel", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> genOffPunchExcel(
            @RequestBody Map<String, Object> bodyMap,
            @SessionAttribute(name = "login", required = false) Login loginUser) {

        Map<String, Object> retObj = new HashMap<>();

        if (loginUser == null) {
            retObj.put("Success", "N");
            retObj.put("ErrorMessage", "使用者未登入或 Session 已過期");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(retObj);
        }

        String loginId = loginUser.getLoginId();

        try {
            JSONObject jsonObj = new JSONObject(bodyMap != null ? bodyMap : new HashMap<>());

            List<ATTENDANCEEMPRANK> scheduleList = reportService.getSchedule(jsonObj);
            List<LOGDB> loginLogList = reportService.getLogin(jsonObj);
            JSONArray rows = reportService.processPunch(scheduleList, loginLogList);

            Path hrDir = Paths.get(downloadContentRoot, "HR");
            if (!Files.exists(hrDir)) {
                Files.createDirectories(hrDir);
            }

            String timeStamp = GetDateTime.getTodayDateW("")
                    + GetDateTime.getTimeA2()[0]
                    + GetDateTime.getTimeA2()[1];
            String fileName = "HR_OFF_PUNCH_" + timeStamp + ".xlsx";
            Path filePath = hrDir.resolve(fileName);

            OffPunchExcel.genOffPunchExcel(rows, "出勤異常報表", filePath.toString(), jsonObj);
            downlodSensitiveService.saveDownloadSensitive(loginId, "OffPunch",
                    scheduleList != null ? scheduleList.size() : 0);

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