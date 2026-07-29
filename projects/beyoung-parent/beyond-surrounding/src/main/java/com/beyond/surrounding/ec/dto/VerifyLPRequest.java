package com.beyond.surrounding.ec.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyLPRequest {
    @JsonProperty("s_mid")
    private String sMid;

    @JsonProperty("ret_code")
    private String retCode;

    @JsonProperty("tx_type")
    private String txType;

    @JsonProperty("order_no")
    private String orderNo;

    @JsonProperty("ret_msg")
    private String retMsg;

    @JsonProperty("auth_id_resp")
    private String authIdResp;

    private String carrierId2; // 若 JSON 欄位就是 carrierId2，則不需特別指定 JsonProperty
    
}