package com.beyond.surrounding.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class UploadBean {

    @JsonProperty("transaction_id")
    private String transactionId; // 修正：改回 String 型態

    @JsonProperty("transaction_type")
    private String transactionType;

    @JsonProperty("member_identity")
    private MemberIdentityBean memberIdentity;

    @JsonProperty("brand_code")
    private String brandCode;

    @JsonProperty("store_code")
    private String storeCode;

    @JsonProperty("source_uuid")
    private String sourceUuid;

    @JsonProperty("invoice_number")
    private String invoiceNumber;

    @JsonProperty("invoice_random_number")
    private String invoiceRandomNumber;

    @JsonProperty("invoice_amount")
    private String invoiceAmount;

    @JsonProperty("level_apply_amount")
    private String levelApplyAmount;

    @JsonProperty("point_apply_amount")
    private String pointApplyAmount;

    @JsonProperty("transaction_datetime")
    private String transactionDatetime;
}