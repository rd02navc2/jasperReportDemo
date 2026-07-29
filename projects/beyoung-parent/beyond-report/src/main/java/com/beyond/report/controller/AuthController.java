package com.beyond.report.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("userid") String userId, 
                                  @RequestParam String password) {
        
        // 驗證帳密
        if ("test".equals(userId) && "test".equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "登入成功");
            
            return ResponseEntity.ok(response); // 回傳 HTTP 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("帳號或密碼錯誤");
        }
    }
}