package com.beyond.surrounding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@Data
@SuperBuilder // 讓子類別能繼承建構器
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 自動隱藏 null 欄位，減少傳輸量
public class ResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 系統基礎欄位 ---
    private String code;
    private String yn;
    private String message;
    private T data;

    // --- 使用者與身分資訊 ---
    private String userId;
    private String userName;
    private String mobile;
    private String cardId;
    private String identity;
    private String cardVip;
    private String cardType;
    private String invoCarrier;

    // --- 訊息與權限 ---
    private String uploadMessage;
    private String downloadMessage;
    private String hashKey;

    // --- 點數與金額 ---
    private Double baseBet;
    private Double totalPoint;
    private Double prePoint;
    private Double thisPoint;
    private Integer amt;

    // --- 其他業務欄位 ---
    private String car;
    private String motor;
    private String lpj03;
    private Double taLpj01;
    private Double taLpj02;
    private Double taLpj03;
    private Double insIntegral;

    // --- 資料載體 ---
    // private Object data;
    
    public static ResponseDTO<?> success(String code, String message) {
        return ResponseDTO.builder()
                .code(code)
                .message(message)
                .build();
    }
    
 // 修改 ResponseDTO 中的 error 方法
    public static ResponseDTO<?> error(String code, String message) {
        return ResponseDTO.builder()
                .code(code)
                .message(message)
                .build();
    }

}