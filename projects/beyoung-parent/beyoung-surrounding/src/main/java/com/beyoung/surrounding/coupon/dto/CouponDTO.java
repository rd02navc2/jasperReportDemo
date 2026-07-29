package com.beyoung.surrounding.coupon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 統一的 Coupon DTO 封裝類
 */
@Data
public class CouponDTO {

    // 1. 請求物件：使用 @JsonProperty 指定 JSON 鍵名稱
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        
        // 前端傳送 {"couponID": "..."} 時會正確映射到此欄位
        @JsonProperty("couponID") 
        private String couponID;
    }

    // 2. 回應物件：統一 API 回傳格式
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response<T> {
        private String code;     // 狀態碼
        private String message;  // 訊息
        private T data;          // 泛型資料內容

        public static <T> Response<T> success(T data) {
            return new Response<>("0", "finished", data);
        }

        public static <T> Response<T> error(String code, String message) {
            return new Response<>(code, message, null);
        }
    }
}