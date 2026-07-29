package com.beyond.surrounding.invoice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

public class InvoiceDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("code")
        private String code;

        @JsonProperty("yn")
        private String yn;

        @JsonProperty("message")
        private String message;

        @JsonProperty("data")
        private T data;

        // 成功回應 (不帶資料)
        public static <T> Response<T> success(String message) {
            return Response.<T>builder()
                    .code("0000")
                    .yn("Y")
                    .message(message)
                    .build();
        }

        // 成功回應 (攜帶核心資料) - 泛型設計，適用於所有類型
        public static <T> Response<T> success(T data, String message) {
            return Response.<T>builder()
                    .code("0000")
                    .yn("Y")
                    .message(message)
                    .data(data)
                    .build();
        }

        // 錯誤回應
        public static <T> Response<T> error(String code, String message) {
            return Response.<T>builder()
                    .code(code)
                    .yn("N")
                    .message(message)
                    .build();
        }

        // 泛型適配器 (適用於既有 Bean 的相容封裝)
        public static <T> Response<T> of(String code, String yn, String message, T data) {
            return Response.<T>builder()
                    .code(code)
                    .yn(yn)
                    .message(message)
                    .data(data)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempMemberRequest {
        
        @JsonProperty("center")
        @NotBlank(message = "sCenter 核心據點代碼不能為空")
        private String center;

        @JsonProperty("memberId")
        @NotBlank(message = "sMemberID 會員編號不能為空")
        private String memberId;
    }
}