package com.beyond.surrounding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@SuperBuilder // 讓子類別也能使用 Builder 繼承屬性
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 基本狀態欄位 ---
    private String code;
    private String yn;           // 原 yN
    private String message;

    // --- 會員與使用者資訊 ---
    private String userId;
    private String userName;
    private String mobile;
    private String cardId;
    private String identity;
    private String cardVip;      // 原 card_vip
    private String cardType;     // 原 card_type
    private String invoCarrier;  // 原 invoCarrier

    // --- 訊息與權限 ---
    private String uploadMessage;
    private String downloadMessage;
    private String hashKey;      // 原 sHashKey

    // --- 點數與金額 ---
    private Double baseBet;
    private Double totalPoint;
    private Double prePoint;
    private Double thisPoint;
    private Integer amt;

    // --- 其他特殊欄位 ---
    private String car;
    private String motor;
    private String lpj03;
    private Double taLpj01;      // 原 ta_lpj01
    private Double taLpj02;      // 原 ta_lpj02
    private Double taLpj03;      // 原 ta_lpj03
    private Double insIntegral;  // 原 ins_Integral

    private Object data;
}