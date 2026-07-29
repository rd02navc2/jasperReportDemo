package com.beyond.surrounding.counter.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 點數相關資料傳輸物件集合（兼顧 JSON 與 XML 序列化規格）
 */
public class CounterDTO {
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InvoiceInfo {
	    private Date tcPsa04;
	    private String tcPsa05;
	    private String lnt06;
	    private String lnt09;
	    private String tqa02;
	    private Double tcPsa12;
	    private Double tcPsa40;
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CounterListInfo {
	    private String lnt06;
	    private String lnt09;
	    private String oba01;
	    private String oba02;
	    private String tqa02;
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DeptInfo {
	    @JsonProperty("lnt09") @JacksonXmlProperty(localName = "lnt09")
	    private String lnt09;
	    
	    @JsonProperty("oba01") @JacksonXmlProperty(localName = "oba01")
	    private String oba01;
	    
	    @JsonProperty("oba02") @JacksonXmlProperty(localName = "oba02")
	    private String oba02;
	}
	
	/**
     * 新增：對應 getAllCounter 查詢結果的資料結構
     * 欄位對應: lntplant, lnt06, lnt09, lnt30, tqa02
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CounterInfo {
        @JsonProperty("lntPlant") @JacksonXmlProperty(localName = "lntPlant")
        private String lntPlant;
        
        @JsonProperty("lnt06") @JacksonXmlProperty(localName = "lnt06")
        private String lnt06;
        
        @JsonProperty("lnt09") @JacksonXmlProperty(localName = "lnt09")
        private String lnt09;
        
        @JsonProperty("lnt30") @JacksonXmlProperty(localName = "lnt30")
        private String lnt30;
        
        @JsonProperty("tqa02") @JacksonXmlProperty(localName = "tqa02")
        private String tqa02;
    }

    /**
     * API 請求物件：對應原 BonusRes.RequestBody
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JacksonXmlRootElement(localName = "Request") // 支援 XML 格式請求根節點
    public static class Request {
    	
    	//dc-
    	@JsonProperty("orderNo") // 客製化自動贈點
    	private String orderNo;
    	
    	@JsonProperty("amount") // VIP消費總額
    	private BigDecimal amount;
    	
    	// ==========================================
        // 新增：動態行銷專案對接欄位
        // ==========================================
    	
    	@JsonProperty("lrq01")
    	private String lrq01; // 活動規則代碼 (例如: "701")
        
    	@JsonProperty("lrq02")
        private String lrq02; // 活動代號/客製規則 (預設傳 "603")
        
        @JsonProperty("counterId")
        @JacksonXmlProperty(localName = "counterId")
        private String counterId; 
        
        @JsonProperty("counterName")
        @JacksonXmlProperty(localName = "counterName")
        private String counterName; 
        
        @JsonProperty("center")
        @JacksonXmlProperty(localName = "center")
        private String center;
        
        @JsonProperty("loginId")
        @JacksonXmlProperty(localName = "loginId")
        private String loginId; 
        
        @JsonProperty("createUserId")
        @JacksonXmlProperty(localName = "createUserId")
        private String createUserId; 
        
        @JsonProperty("cardNo")
        @JacksonXmlProperty(localName = "cardNo")
        private String cardNo; 
        
        @JsonProperty("point")
        @JacksonXmlProperty(localName = "point")
        private Integer point;
        
        @JsonProperty("invoice")
        @JacksonXmlProperty(localName = "invoice")
        private String invoice;
    }

    /**
     * API 回應物件：對應原 BonusRes.ResponseBody
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JacksonXmlRootElement(localName = "ExcludeCounterResponse")
    public static class ExcludeCounterResponse {
        
        @JsonProperty("counterId")
        @JacksonXmlProperty(localName = "counterId")
        private String counterId;
        
        @JsonProperty("counterName")
        @JacksonXmlProperty(localName = "counterName")
        private String counterName;
        
        @JsonProperty("center")
        @JacksonXmlProperty(localName = "center")
        private String center;
        
        @JsonProperty("loginId")
        @JacksonXmlProperty(localName = "loginId")
        private String loginId;
        
        @JsonProperty("createUserId")
        @JacksonXmlProperty(localName = "createUserId")
        private String createUserId;
        
        @JsonProperty("cardNo")
        @JacksonXmlProperty(localName = "cardNo")
        private String cardNo;
        
        @JsonProperty("point")
        @JacksonXmlProperty(localName = "point")
        private Integer point;
        
        @JsonProperty("transactionId")
        @JacksonXmlProperty(localName = "transactionId")
        private String transactionId;
        
        @JsonProperty("transactionTime")
        @JacksonXmlProperty(localName = "transactionTime")
        private String transactionTime;
        
        @JsonProperty("status")
        @JacksonXmlProperty(localName = "status")
        private String status; 
    }

    /**
     * API 統一回應格式：對應原 ResponseBean 邏輯
     * 加上 @JacksonXmlRootElement 確保舊系統接收 XML 時，根節點為 <Response> 
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JacksonXmlRootElement(localName = "Response") 
    public static class Response<T> {
        
        @JsonProperty("code")
        @JacksonXmlProperty(localName = "code")
        private String code;
        
        @JsonProperty("message")
        @JacksonXmlProperty(localName = "message")
        private String message;
        
        @JsonProperty("data")
        @JacksonXmlProperty(localName = "data")
        private T data;

        /**
         * 快速生成成功回應
         * 預設 code "0" 與 message "finished" 對應 ErrCodeConst 邏輯
         */
        public static <T> Response<T> success(T data) {
            return Response.<T>builder()
                    .code("0")
                    .message("finished")
                    .data(data)
                    .build();
        }

        /**
         * 快速生成失敗回應
                 */
        public static <T> Response<T> error(String code, String message) {
            return Response.<T>builder()
                    .code(code)
                    .message(message)
                    .build();
        }
    }
}