package com.beyond.surrounding.card.dto;

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
public class CardDTO {

    /**
     * 統一 API 回傳包裝型態
     * @param <T> 實際業務資料型態
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL) // 欄位若為 null 則不輸出給前端，保持乾淨
    public static class Response<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        private String code;      // 狀態代碼 (例如: 0000 成功)
        private String yn;        // 狀態標記 (Y/N)
        private String message;   // 提示訊息
        private T data;           // 核心業務數據

        /**
         * 成功回應 (不帶資料)
         */
        public static <T> Response<T> success(String message) {
            return Response.<T>builder()
                    .code("0000")
                    .yn("Y")
                    .message(message)
                    .build();
        }

        /**
         * 成功回應 (攜帶核心資料)
         */
        public static <T> Response<T> success(T data, String message) {
            return Response.<T>builder()
                    .code("0000")
                    .yn("Y")
                    .message(message)
                    .data(data)
                    .build();
        }
        
        /**
         * 成功回應 (直接包裝舊系統既有的 Bean 物件)
         * 針對那些已經包含 code、message、yn 欄位的既有 Bean 結構做的相容封裝
         */
        public static <T> Response<T> of(String code, String yn, String message, T data) {
            return Response.<T>builder()
                    .code(code)
                    .yn(yn)
                    .message(message)
                    .data(data)
                    .build();
        }
    }

    /**
     * 新增臨時會員 請求參數結構
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempMemberRequest {
        @NotBlank(message = "sCenter 核心據點代碼不能為空")
        private String center;

        @NotBlank(message = "sMemberID 會員編號不能為空")
        private String memberID;
    }
}