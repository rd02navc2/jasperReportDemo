package com.beyoung.surrounding.app.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

// 動態讀取設定檔的 chiefpay.api.url，若找不到則預設為 localhost:8080，避免測試崩潰
@FeignClient(name = "chiefPayClient", url = "${chiefpay.api.url:http://localhost:8080}")
public interface ChiefPayFeignClient {

    /**
     * 呼叫 ChiefPay 的贈獎/消費回寫 API
     * 回傳格式若為 JSON，可直接用 Map<String, Object> 接收以方便檢查 code
     */
    @PostMapping(value = "/SRD/rest/hiefpay/purchase", consumes = "application/json")
    Map<String, Object> purchase(@RequestBody Map<String, Object> requestBody);
    
    /**
     * 同步點數至 ChiefPay 微服務
     * 【修正 2】將原本衝突的多個 @RequestBody 改為單一 Map 傳遞
     */
    @PostMapping(value = "/api/v1/bonus/sync", consumes = "application/json")
    void triggerChiefPayBonus(
        @RequestBody Map<String, Object> syncPayload,
        @RequestParam String invoiceNo
    );
}