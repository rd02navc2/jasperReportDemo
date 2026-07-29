package com.beyoung.surrounding.exchange.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 發票與點數模組 DTO 定義
 */
@Data
public class ExchangeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 統一 API 回傳包裝型態
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        private String code;     // 狀態代碼
        private String yn;       // 狀態標記 (Y/N)
        private String message;  // 提示訊息
        private T data;          // 核心業務數據

        // 1. 成功：只帶訊息
        public static <T> Response<T> success(String message) {
            return Response.<T>builder().code("0000").yn("Y").message(message).build();
        }

        // 2. 成功：帶資料
        public static <T> Response<T> success(T data, String message) {
            return Response.<T>builder().code("0000").yn("Y").message(message).data(data).build();
        }

        // 3. 錯誤：標準失敗回應
        public static <T> Response<T> error(String code, String message) {
            return Response.<T>builder().code(code).yn("N").message(message).build();
        }

        // 4. 相容舊系統的封裝
        public static <T> Response<T> of(String code, String yn, String message, T data) {
            return Response.<T>builder().code(code).yn(yn).message(message).data(data).build();
        }
    }

    /**
     * 會員請求參數結構
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempMemberRequest {
        @NotBlank(message = "center 核心據點代碼不能為空")
        private String center;

        @NotBlank(message = "memberID 會員編號不能為空")
        private String memberID;
    }
}