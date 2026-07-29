package com.beyoung.surrounding.bonus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BonusDTO {
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PointDTO {
	    @JsonProperty("pointCode")
	    private String pointCode; // 內部使用更簡潔的命名
	    
	    @JsonProperty("point")
	    private Double point;

	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PrizeDTO {

	    @JsonProperty("activityCode")   private String activityCode;
	    @JsonProperty("activityName")   private String activityName;
	    @JsonProperty("prizeType")      private String prizeType;
	    @JsonProperty("prizeCode")      private String prizeCode;
	    @JsonProperty("prizeName")      private String prizeName;
	    @JsonProperty("pointCode")      private String pointCode;
	    @JsonProperty("needPoint")      private Double needPoint;
	    @JsonProperty("redeemableQty")  private Double redeemableQty;
	    @JsonProperty("price")          private Double price;
	    @JsonProperty("couponNO")       private ArrayList<String> couponNO;
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CouponDTO {

	    @JsonProperty("couponNO")
	    private String couponNO;

	    @JsonProperty("startDate")
	    private String startDate;

	    @JsonProperty("endDate")
	    private String endDate;

	    @JsonProperty("value")
	    private Double value;
	}
	
	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RedeemResponseDTO {
        @JsonProperty("pointList")
        private List<PointDTO> pointList;
        
        @JsonProperty("prizeList")
        private List<PrizeDTO> prizeList;
        
        @JsonProperty("couponList")
        private List<CouponDTO> couponList;
    }
	
	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JacksonXmlRootElement(localName = "Request")
    public static class Request {
        @JsonProperty("orderNo") private String orderNo;
        @JsonProperty("amount") private BigDecimal amount;
        @JsonProperty("lrq01") private String lrq01;
        @JsonProperty("lrq02") private String lrq02;
        @JsonProperty("counterId") private String counterId;
        @JsonProperty("counterName") private String counterName;
        @JsonProperty("center") private String center;
        @JsonProperty("loginId") private String loginId;
        @JsonProperty("createUserId") private String createUserId;
        @JsonProperty("cardNo") private String cardNo;
        @JsonProperty("point") private Integer point;
        @JsonProperty("invoice") private String invoice;
    }

	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChiefPayRequest {
        @JsonProperty("cardNo")     private String cardNo;
        @JsonProperty("creditCard") private String creditCard;
        @JsonProperty("invoiceAmt") private Double invoiceAmt;
        @JsonProperty("promoteAmt") private Double promoteAmt;
        @JsonProperty("creditAmt")  private Double creditAmt;
        @JsonProperty("deviceId")   private String deviceId;
        @JsonProperty("counterId")  private String counterId;
        @JsonProperty("invoiceSN")  private String invoiceSN;
        @JsonProperty("invoiceNO")  private String invoiceNO;
        @JsonProperty("invoiceDate") private String invoiceDate;
        @JsonProperty("prizeList")   private List<PrizeDTO> prizeList;
		
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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

        // 成功建立回應的簡捷方法
        public static <T> Response<T> success(T data) {
            return Response.<T>builder()
                    .code("0")
                    .message("finished")
                    .data(data)
                    .build();
        }

        // 失敗建立回應的簡捷方法
        public static <T> Response<T> error(String code, String message) {
            return Response.<T>builder()
                    .code(code)
                    .message(message)
                    .build();
        }
    }
}