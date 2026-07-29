package com.beyond.report.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginViewController {

    /** Session 裡存放登入帳號的 key */
    private static final String SESSION_KEY_USER = "LOGIN_USER";

    // ===================== 頁面路由 (GET) =====================

    @GetMapping({"/login", "/", "/Report/"})
    public String showLoginPage() {
        return "login"; // WEB-INF/jsp/login.jsp
    }

    @GetMapping({"/demo1", "/Report/demo1"})
    public String demo1() {
        return "demo1";
    }

    // ===================== 認證 API (POST) =====================

    /**
     * 對應前端 demo1.jsp / loginPage.jsp 的
     * $.ajax({ url:"LoginServlet", data:{process:...} }) 呼叫方式。
     * 依 process 參數分流三種動作：login / getUserFunction / logout。
     *
     * 同時相容舊路徑前綴 /Report/LoginServlet。
     */
    @PostMapping({"/LoginServlet", "/Report/LoginServlet"})
    @ResponseBody
    public Map<String, Object> handleLoginServlet(
            @RequestParam(value = "process", required = false) String process,
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request) {

        if (process == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("Success", "N");
            error.put("LoginMsg", "缺少 process 參數");
            return error;
        }

        switch (process) {
            case "login":
                return doLogin(userid, password, request);
            case "getUserFunction":
                return doGetUserFunction(request);
            case "logout":
                return doLogout(request);
            default:
                Map<String, Object> error = new HashMap<>();
                error.put("Success", "N");
                error.put("LoginMsg", "不支援的操作：" + process);
                return error;
        }
    }

    /**
     * 保留原本的 /Report/api/login（相容用陣列參數解決重複欄位問題的舊寫法）。
     * 內部邏輯改呼叫共用的 doLogin()，確保驗證規則跟 /LoginServlet 一致，不會兩邊邏輯兜不起來。
     */
    @PostMapping("/Report/api/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam(value = "userid", required = false) List<String> useridList,
            @RequestParam(value = "password", required = false) List<String> passwordList,
            @RequestParam(value = "process", required = false) String process,
            @RequestParam(value = "auto", required = false) String auto,
            HttpServletRequest request) {

        String userid = (useridList != null && !useridList.isEmpty()) ? useridList.get(0) : "";
        String password = (passwordList != null && !passwordList.isEmpty()) ? passwordList.get(0) : "";

        Map<String, Object> response = doLogin(userid, password, request);
        boolean success = "Y".equals(response.get("Success"));
        return success ? ResponseEntity.ok(response) : ResponseEntity.status(401).body(response);
    }

    // ===================== 內部邏輯 =====================

	private Map<String, Object> doLogin(String userid, String password, HttpServletRequest request) {
	    // 1. 打印原值调试（你刚才已经做了）
	    System.out.println("DEBUG: Received userid = [" + userid + "]");
	    System.out.println("DEBUG: Received password = [" + password + "]");
	
	    // 2. 【核心修复】处理可能拼接了双份的逗号字符串，只取第一个有效值
	    if (userid != null && userid.contains(",")) {
	        userid = userid.split(",")[0]; // 取逗号前面的值
	    }
	    if (password != null && password.contains(",")) {
	        password = password.split(",")[0]; // 取逗号前面的值
	    }
	
	    // 打印处理后的值，检查是否正常
	    System.out.println("DEBUG: Fixed userid = [" + userid + "]");
	    System.out.println("DEBUG: Fixed password = [" + password + "]");
	
	    Map<String, Object> response = new HashMap<>();
	
	    // TODO: 這裡換成你的實際資料庫驗證邏輯
	    boolean isValidUser = "test".equals(userid) && "test".equals(password);
	
	    if (isValidUser) {
	       
	        HttpSession oldSession = request.getSession(false);
	        if (oldSession != null) {
	            oldSession.invalidate();
	        }
	        HttpSession session = request.getSession(true);
	        session.setAttribute(SESSION_KEY_USER, userid);
	
	        response.put("Success", "Y");
	        response.put("success", true);
	        response.put("message", "登入成功");
	    } else {
	        response.put("Success", "N");
	        response.put("success", false);
	        response.put("LoginMsg", "帳號或密碼錯誤，請重新確認");
	    }
	    return response;
	}

    private Map<String, Object> doGetUserFunction(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        Object loginUser = (session != null) ? session.getAttribute(SESSION_KEY_USER) : null;

        if (loginUser == null) {
            response.put("Success", "N");
            response.put("LoginMsg", "尚未登入或登入逾時，請重新登入");
            return response;
        }

        // TODO: 正式環境應依登入者角色/群組從資料庫查詢實際可用選單與權限
        response.put("Success", "Y");
        response.put("UserFunction", buildMockFunctionTree());
        return response;
    }

    private Map<String, Object> doLogout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.put("Success", "Y");
        return response;
    }

    /**
     * 對應 demo1.jsp 裡 zTree 需要的 simpleData 格式：
     * { id, pId, name, program_name, canRead, canInsert, canSave, canDelete, canPrint }
     */
    private List<Map<String, Object>> buildMockFunctionTree() {
        List<Map<String, Object>> nodes = new ArrayList<>();

        nodes.add(category(1, 0, "資產盤點作業"));
        nodes.add(function(11, 1, "個人資產設定", "pages/asset/personalAsset.jsp", true, true, true, false, true));

        nodes.add(category(2, 0, "表單相關作業"));
        nodes.add(function(21, 2, "資訊需求單查詢作業", "pages/form/itDemandQuery.jsp", true, false, false, false, true));
        nodes.add(function(22, 2, "資訊需求單新增作業", "pages/form/itDemandCreate.jsp", true, true, true, false, true));
        nodes.add(function(23, 2, "IT維運統計查詢", "pages/form/itOpsStat.jsp", true, false, false, false, true));
        nodes.add(function(24, 2, "申請名條推播查詢作業", "pages/form/nameTagPushQuery.jsp", true, false, false, false, true));
        nodes.add(function(25, 2, "申請名條推播新增作業", "pages/form/nameTagPushCreate.jsp", true, true, true, false, true));
        nodes.add(function(26, 2, "商品編號維護作業", "pages/form/productCodeMaint.jsp", true, true, true, true, true));

        nodes.add(category(3, 0, "權限設定"));
        nodes.add(function(31, 3, "密碼變更作業", "pages/auth/changePassword.jsp", true, false, true, false, false));

        return nodes;
    }

    private Map<String, Object> category(int id, int pId, String name) {
        Map<String, Object> n = new HashMap<>();
        n.put("id", id);
        n.put("pId", pId);
        n.put("name", name);
        n.put("program_name", "");
        return n;
    }

    private Map<String, Object> function(int id, int pId, String name, String programName,
                                          boolean canRead, boolean canInsert, boolean canSave,
                                          boolean canDelete, boolean canPrint) {
        Map<String, Object> n = new HashMap<>();
        n.put("id", id);
        n.put("pId", pId);
        n.put("name", name);
        n.put("program_name", programName);
        n.put("canRead", canRead ? "Y" : "N");
        n.put("canInsert", canInsert ? "Y" : "N");
        n.put("canSave", canSave ? "Y" : "N");
        n.put("canDelete", canDelete ? "Y" : "N");
        n.put("canPrint", canPrint ? "Y" : "N");
        return n;
    }
}
