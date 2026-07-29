package com.beyond.surrounding.pos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponPosDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 業務欄位
    private String couponNo;
    private Integer price;
    private String isUsed;
    private String isApp;

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
        private String status;
        private String message;
        private T data;
    }
}