package com.beyond.surrounding.pos.client;

import com.beyond.surrounding.pos.bean.RefundBean;
import com.beyond.surrounding.pos2.bean.PaceResponseBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(
    name = "pacePayFeignClient", 
    // 對齊 YAML: pacepay.payment-target-base
    url = "${pacepay.payment-target-base:https://playground.api.pacepay.com}", 
    configuration = PaceFeignConfig.class
)
public interface PacePayFeignClient {

    @PostMapping("/v1/transactions")
    PaceResponseBean pacepayPayment(@RequestBody Map<String, Object> requestBody);

    @PostMapping("/v1/transactions/{transactionId}/verify_barcode")
    PaceResponseBean pacepayVerifyBarcode(
        @PathVariable("transactionId") String transactionId, 
        @RequestBody Map<String, String> requestBody
    );

    @GetMapping("/v1/transactions/{transactionId}")
    PaceResponseBean pacepayQuery(@PathVariable("transactionId") String transactionId);

    /**
     * 退款介面 (使用強型別 RefundBean)
     * @param authHeader 認證標頭
     * @param refundBean 退款資料實體
     * * 註：請確保設定檔中的 ${PACE-Refund-Path} 為相對路徑，例如: /v1/refunds
     * 如果設定檔不好改，也可以直接寫死改為 @PostMapping("/v1/refunds")
     */
    @PostMapping("${PACE-Refund-Path:/v1/refunds}")
    PaceResponseBean refund(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody RefundBean refundBean
    );

}