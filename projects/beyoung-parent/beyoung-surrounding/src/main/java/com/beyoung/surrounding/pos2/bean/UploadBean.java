package com.beyoung.surrounding.pos2.bean;

import com.beyoung.surrounding.bean.MemberIdentityBean;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadBean {
    @JsonProperty("transaction_id") private String transactionId;
    @JsonProperty("transaction_type") private String transactionType;
    @JsonProperty("member_identity") private MemberIdentityBean memberIdentity;
    @JsonProperty("brand_code") private String brandCode;
    @JsonProperty("store_code") private String storeCode;
    @JsonProperty("source_uuid") private String sourceUuid;
    @JsonProperty("invoice_number") private String invoiceNumber;
    @JsonProperty("invoice_random_number") private String invoiceRandomNumber;
    @JsonProperty("invoice_amount") private Integer invoiceAmount;
    @JsonProperty("transaction_datetime") private String transactionDatetime;
}
