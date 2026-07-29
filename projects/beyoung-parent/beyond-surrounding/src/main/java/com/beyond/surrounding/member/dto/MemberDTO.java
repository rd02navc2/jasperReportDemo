package com.beyond.surrounding.member.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 會員相關 DTO 集合
 */
public final class MemberDTO {

    private MemberDTO() {}

    // =========================================================================
    // 通用回應包裝
    // =========================================================================

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response<T> {
        private boolean success;
        private String  message;
        private T       data;

        public static <T> Response<T> success(T data) {
            return Response.<T>builder().success(true).message("成功").data(data).build();
        }

        public static <T> Response<T> success(String message, T data) {
            return Response.<T>builder().success(true).message(message).data(data).build();
        }

        public static <T> Response<T> fail(String message) {
            return Response.<T>builder().success(false).message(message).data(null).build();
        }

        public static Response<Void> error(String string, String errorMessage) {
            return null;
        }
    }

    
    @Data
    public static class DoFormalRequest {
        @NotBlank(message = "臨時會員ID不能為空")
        @JsonProperty("sTempMemberID")
        private String sTempMemberID;
        
        @NotBlank(message = "姓名不能為空")
        @JsonProperty("sName")
        private String sName;
        
        @NotBlank(message = "身分證字號不能為空")
        @JsonProperty("sID")
        private String sID;
        
        @JsonProperty("sBirthday")
        private String sBirthday;
        
        @JsonProperty("sMobile")
        private String sMobile;
        
        @JsonProperty("sAddress")
        private String sAddress;
        
        @JsonProperty("sEmail")
        private String sEmail;
    }
    // =========================================================================
    // 查詢回應 DTOs
    // =========================================================================

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExistResponse {
       
        private String yn;
        
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class CardDetailResponse {
    	
        private String lpj01;
        
        
        private String lpj02;
        
        
        private String lpj03;
        
        
        private LocalDate lpj04;
        
        
        private Double lpj06;
        
        
        private String lpj09;
        
        
        private Integer lpj07;
        
        
        @JsonIgnore
        private String lpj19;
        
        
        private Double lpj12;
        
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberContactResponse {
    	
    	
        private String lpk01;
        
        
        private String lpk03;
        
        private String lpk04;
        
        
        private String lpk05;        
               
        private String lpk14;
        
        private String lpk15;
        
        private String lpk18;
        
        private String lpk19;
        
        private String lpkUd02;
        
        private String vipLevel;
        
        private Double totalLsm08;
        
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CardItem {
    	
        private String lpj01;
        
        private String lpj02;
        
        private String lpj03;
        
        private String lpk03;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PointResponse {
    	
        private String lpj01;
        
        private String lpj02;
        
        private String lpj03;
        
        private String lpk03;
        
        private String lpk05;
        
        private Integer lpj07;
        
        private Double lpj12;
        
        private Double lpj14;
        
        private Double lpj15;
        
        private Double taLpj02;
        
        private Double taLpj03;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PointHistItem {
    	
        private String lsmStore;
        
        private String lsm01;
        
        private String lsm02;
        
        private Double lsm04;
        
        private String lsm05;
        
        private Double lsm08;
        
        private String taLsm02;
        
        private String taLsm09;
        
        private String tqa02;
        
        private String taLsm04;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberByCardResponse {
    	
        private String lpk01;
        
        private String lpk04;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberByCardEnterFullResponse {
    	
        private String lpk01;
        
        private String lpk04;
        
        private Double lpj12;
        
        private String vipLevel;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberByCardEnterResponse {
    	
        private String lpk01;
        
        private String lpk03;
        
        private String lpk04;
        
        private String lpk18;
        
        private String lpkUd02;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberByCardBossResponse {
    	
    	@JsonProperty("lpk01")
        private String lpk01;
        
        @JsonProperty("lpk01")
        private String lpk04;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MainCardResponse {
    	
    	@JsonProperty("lpk04")
        private String lpk04;
        
        @JsonProperty("lpj03")
        private String lpj03;
    }

    // =========================================================================
    // 請求參數 DTOs (保持不變)
    // =========================================================================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempMemberRequest {
        @NotBlank(message = "中心代碼不能為空")
        @JsonProperty("sCenter") // 明確指定對應 JSON 中的 key
        private String sCenter;

        @NotBlank(message = "會員ID不能為空")
        @JsonProperty("sMemberID") // 明確指定對應 JSON 中的 key
        private String sMemberID;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateContactRequest {
    	
        @NotBlank(message = "會員ID不能為空")
        @JsonProperty("sMemberID")
        private String sMemberID;
        
        @Pattern(regexp = "^$|^09\\d{8}$", message = "手機號碼格式不正確")
        @JsonProperty("sMobile")
        private String sMobile;
        
        @Email(message = "Email 格式不正確")
        @JsonProperty("sEmail")
        private String sEmail;
        
        @JsonProperty("sAddr")
        private String sAddr;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormalMemberRequest {
    	
        @NotBlank(message = "臨時會員ID不能為空")
        @JsonProperty("sTempMemberID")
        private String sTempMemberID;
        
        @NotBlank(message = "姓名不能為空")
        @JsonProperty("sName")
        private String sName;
        
        @NotBlank(message = "身分證ID不能為空")
        @JsonProperty("sID")
        private String sID;
        
        @NotBlank(message = "生日不能為空")
        @Size(min = 8, max = 8, message = "生日格式應為 YYYYMMDD 共 8 碼")
        @JsonProperty("sBirthday")
        private String sBirthday;
        
        @NotBlank(message = "手機號碼不能為空")
        @Pattern(regexp = "^09\\d{8}$", message = "手機號碼格式不正確")
        @JsonProperty("sMobile")
        private String sMobile;
        
        @NotBlank(message = "地址不能為空")
        @JsonProperty("sAddress")
        private String sAddress;
        
        @NotBlank(message = "Email 不能為空")
        @Email(message = "Email 格式不正確")
        @JsonProperty("sEmail")
        private String sEmail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HouseHoldRequest {
    	
        @NotBlank(message = "臨時會員ID不能為空")
        @JsonProperty("sTempMemberID")
        private String sTempMemberID;
        
        @NotBlank(message = "身分證ID不能為空")
        @JsonProperty("sID")
        private String sID;
    }
}