package com.beyond.surrounding.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception e) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", 417); // 你的 Expectation Failed
        body.put("message", e.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.EXPECTATION_FAILED);
    }
}