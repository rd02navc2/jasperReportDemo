package com.beyond.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginViewController {

    /**
     * 相容舊網址：存取 /Report/LoginServlet 時回傳 login.html 頁面
     */
    @GetMapping({"/Report/LoginServlet", "/login", "/"})
    public String showLoginPage() {
        // 回傳 templates/login.html (副檔名 .html 會由 Thymeleaf 自動補上)
        return "login";
    }
    
    @GetMapping({"/Report/demo1", "/demo1"})
    public String demo1() {
        // 對應 src/main/resources/templates/demo1.html
        return "demo1"; 
    }
    
    @PostMapping("/Report/api/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam(value = "userid", required = false) List<String> useridList,
            @RequestParam(value = "password", required = false) List<String> passwordList,
            @RequestParam(value = "process", required = false) String process,
            @RequestParam(value = "auto", required = false) String auto) {
        
        // 取陣列的第一個值，解決重複欄位問題
        String userid = (useridList != null && !useridList.isEmpty()) ? useridList.get(0) : "";
        String password = (passwordList != null && !passwordList.isEmpty()) ? passwordList.get(0) : "";

        System.out.println("收到登入 - 帳號: " + userid + ", 密碼: " + password + ", process: " + process);

        Map<String, Object> response = new HashMap<>();

        // TODO: 這裡換成你的實際資料庫驗證邏輯
        boolean isValidUser = "test".equals(userid) && "test".equals(password);

        if (isValidUser) {
            // 配合前端 successSubmit 的檢查: responseText.Success === 'Y'
            response.put("Success", "Y");
            response.put("success", true);
            response.put("message", "登入成功");
            return ResponseEntity.ok(response);
        } else {
            // 配合前端 successSubmit 的檢查: responseText.LoginMsg
            response.put("Success", "N");
            response.put("success", false);
            response.put("LoginMsg", "帳號或密碼錯誤，請重新確認");
            return ResponseEntity.status(401).body(response);
        }
    }

    
    
    
}
    
    
