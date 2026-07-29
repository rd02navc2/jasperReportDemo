package com.beyoung.member.api.exception;

import com.beyoung.member.domain.dto.MemberDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MemberDTO.Response<Void> handleConstraintViolationException(ConstraintViolationException e) {
        // 串接所有的錯誤訊息
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        
        log.warn("API 參數校驗失敗: {}", errorMessage);
        
        // 回傳符合專案規格的客製化錯誤外殼
        return MemberDTO.Response.error("400", errorMessage);
    }
}