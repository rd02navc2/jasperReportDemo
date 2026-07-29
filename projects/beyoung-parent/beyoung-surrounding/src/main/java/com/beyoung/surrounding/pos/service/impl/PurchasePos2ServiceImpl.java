package com.beyoung.surrounding.pos.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Hex;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.beyoung.surrounding.pos2.client.LinePayPos2FeignClient;
import com.beyoung.surrounding.pos2.client.OnePayPos2FeignClient;
import com.beyoung.surrounding.pos2.client.PXPayPos2FeignClient;
import com.beyoung.surrounding.pos2.client.TaiwanPayPos2FeignClient;
import com.beyoung.surrounding.pos2.service.PurchasePos2Service;
import com.beyoung.surrounding.util.CryptUtil;
import com.beyoung.surrounding.util.GetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchasePos2ServiceImpl implements PurchasePos2Service {

	private final Environment env;
    private final OnePayPos2FeignClient onePayClient;
    private final TaiwanPayPos2FeignClient taiwanPayClient;
    private final PXPayPos2FeignClient pxPayClient;
    private final LinePayPos2FeignClient linePayClient;

    @Value("${linepay.channel-id:default-id}")
    private String channelId;

    @Value("${linepay.authorization-key:default-key}")
    private String auth;
    
    String nonce = UUID.randomUUID().toString();
    
    private String generateNonce() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public String linePayment(String orderId, String center, String counterID, String productName, Integer amt, String oneTimeKey) throws Exception {
        
    	// 使用 Jackson 建立 JSON
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jo = mapper.createObjectNode();
        jo.put("productName", productName);
        jo.put("currency", "TWD");
        jo.put("orderId", orderId);
        jo.put("oneTimeKey", oneTimeKey);
        jo.put("amount", amt);     

        // 呼叫 Client：現在參數個數與型別已與 Client 介面完全一致
        return linePayClient.payment(channelId, auth, generateNonce(), jo.toString());
    } 

    @Override
    public String linePaymentDetail(String transactionId) throws Exception {
        return linePayClient.paymentDetail(channelId, auth, generateNonce(), transactionId);
    }

    @Override
    public String linePayRefund(String orderId) throws Exception {
        return linePayClient.refund(channelId, auth, generateNonce(), orderId, "{}");
    }
       
    // --- OnePay 實作 ---
    @Override
    public String onepayPayment(String orderId, String posId, String posDateTime, Integer amt, String oneTimeKey) throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("amount", String.valueOf(amt));
        params.put("barcode1", URLEncoder.encode(oneTimeKey, "UTF-8"));
        params.put("merchantid", env.getProperty("Beyond-MerchantID"));
        params.put("merchanttradedate", posDateTime.substring(0, 8));
        params.put("merchanttradeno", orderId);
        params.put("merchanttradetime", posDateTime.substring(8));
        params.put("storeid", env.getProperty("Beyond-StoreID"));
        params.put("terminalid", posId);

        String sign = Hex.encodeHexString(CryptUtil.toSHA256(buildQueryString(params) + env.getProperty("Beyond-Key")));
        params.put("sign", sign);
        
        log.info("OnePay Payment Request: orderId={}", orderId);
        return onePayClient.payment(params);
    }

    @Override
    public String onepayQuery(String orderId, String posId, String posDateTime, String oneTimeKey, String tradeType) throws Exception {
        String merchantID = env.getProperty("Beyond-MerchantID");
        String storeID = env.getProperty("Beyond-StoreID");
        String key = env.getProperty("Beyond-Key");
        String queryDateTime = GetDateTime.getTodayDateW("") + GetDateTime.getTime("");
        String date = posDateTime.substring(0, 8);
        String time = posDateTime.substring(8);

        // 1. 組裝請求內容 (依據 API 文件順序與要求)
        // 注意：這裡的 key 必須與 OnePay API 文件要求的欄位名稱完全一致
        Map<String, String> params = new LinkedHashMap<>();
        params.put("barcode1", URLEncoder.encode(oneTimeKey, "UTF-8"));
        params.put("merchantid", merchantID);
        params.put("merchantquerydatetime", queryDateTime);
        params.put("merchanttradedate", date);
        params.put("merchanttradetime", time);
        params.put("querytype", "A");
        params.put("storeid", storeID);
        params.put("terminalid", posId);
        params.put("tradeno", orderId);
        params.put("tradetype", tradeType);

        // 2. 計算簽章
        // 將 Map 轉為原本拼接的字串格式用於計算 Hash
        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        String requestBody = sb.deleteCharAt(sb.length() - 1).toString();
        
        String sign = Hex.encodeHexString(CryptUtil.toSHA256(requestBody + key));
        
        // 3. 加入簽章到 Map，Feign 會自動將其轉為 URL 參數
        params.put("sign", sign);

        log.info("Up(query): params={}", params);
        
        // 4. 呼叫 Feign Client
        String response = onePayClient.query(params);
        
        log.info("Down(query): {}", response);
        return response;
    }
       
    @Override
    public String onepayRefund(String newOrderId, String posId, String posDateTime, Integer amt, String oneTimeKey, String transNo) throws Exception {
        // 1. 補齊環境變數防禦性檢查 (給予空字串預設值，避免 null 傳入後續邏輯)
        String merchantID = env.getProperty("Beyond-MerchantID", "");
        String storeID = env.getProperty("Beyond-StoreID", "");
        String storeName = env.getProperty("Beyond-StoreName", "");
        String key = env.getProperty("Beyond-Key", "");
        
        // 2. 日期格式保護 (假設 posDateTime 為必要參數，若可能為 null 需先判斷)
        String date = (posDateTime != null && posDateTime.length() >= 8) ? posDateTime.substring(0, 8) : "";
        String time = (posDateTime != null && posDateTime.length() >= 8) ? posDateTime.substring(8) : "";

        // 3. 組裝請求參數
        Map<String, String> params = new LinkedHashMap<>();
        params.put("amount", String.valueOf(amt != null ? amt : 0));
        params.put("barcode1", URLEncoder.encode(oneTimeKey != null ? oneTimeKey : "", "UTF-8"));
        params.put("barcode2", "");
        params.put("barcode3", "");
        params.put("barcode4", "");
        params.put("barcode5", "");
        params.put("merchantid", merchantID);
        params.put("merchanttradedate", date);
        params.put("merchanttradeno", newOrderId != null ? newOrderId : "");
        params.put("merchanttradetime", time);
        params.put("orderextrainfo1", "");
        params.put("orderextrainfo2", "");
        params.put("orderextrainfo3", "");
        params.put("orderitem", "");
        params.put("remark1", "");
        params.put("remark2", "");
        params.put("remark3", "");
        params.put("servicetradeno", transNo != null ? transNo : "");
        params.put("storeid", storeID);
        params.put("storename", storeName);
        params.put("terminalid", posId != null ? posId : "");

        // 2. 計算簽章
        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        // 移除最後一個 "&"
        String requestString = sb.substring(0, sb.length() - 1);
     // 先組裝字串
        String signBase = requestString + key;
        byte[] hashedBytes = CryptUtil.toSHA256(signBase);

        if (hashedBytes == null) {
            log.error("SHA256 hashing failed for input: {}", signBase);
            throw new RuntimeException("簽章產生失敗");
        }
        
        String sign = Hex.encodeHexString(hashedBytes);
        
        // 3. 加入簽章
        params.put("sign", sign);

        log.info("Up(refund): params={}", params);
        
        // 4. 呼叫 Feign Client
        String response = onePayClient.refund(params);
        
        log.info("Down(refund): {}", response);
        return response;
    }
    
	@Override
	public String taiwanpayPayment(String orderId, String posId, Integer amt, String oneTimeKey) throws Exception {
	    // 優先讀取並檢查是否為 null，避免簽章錯誤
	    String merchantID = env.getProperty("taiwanpay.merchant-id");
	    String terminalID = env.getProperty("taiwanpay.terminal-id");
	    String key = env.getProperty("taiwanpay.key");
	
	    if (merchantID == null || key == null) {
	        throw new IllegalStateException("配置參數缺失，請檢查 application.yml 設定");
	    }
	
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode jo = mapper.createObjectNode();
	    
	    jo.put("MerchantID", merchantID);
	    jo.put("TerminalID", terminalID);
	    jo.put("ShopOrderNo", orderId);
	    jo.put("PaymentType", "TaiwanPay");
	    jo.put("Amount", amt);
	    jo.put("ScanCode", oneTimeKey);
	    jo.put("Currency", "TWD");
	    jo.put("ProductInfo", "Beyond Product");
	    jo.put("DeviceSN", merchantID);
	    jo.put("VersionNo", "1.0.0");
	    
	    // 簽章計算使用與 JSON 一致的變數
	    String rawData = key + "&MerchantID=" + merchantID
	            + "&TerminalID=" + terminalID
	            + "&ShopOrderNo=" + orderId
	            + "&Amount=" + amt
	            + "&ScanCode=" + oneTimeKey
	            + "&VersionNo=1.0.0"
	            + "&" + key;
	            
	    String sign = CryptUtil.toSHA256Encrypt(rawData);
	    jo.put("Sign", sign);
	    
	    // 呼叫修正後的 Feign Client (僅傳入 JSON 字串)
	    return taiwanPayClient.payment(jo.toString());
	}
        
    @Override
    public String taiwanpayQuery(String orderId, String posId) throws Exception {
        String merchantID = env.getProperty("Taiwan-MerchantID");
        String terminalID = env.getProperty("Taiwan-TerminalID");
        String key = env.getProperty("Taiwan-Key");

        // 1. 組裝 JSON
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jo = mapper.createObjectNode();
        jo.put("MerchantID", merchantID);
        jo.put("TerminalID", terminalID);
        jo.put("ShopOrderNo", orderId);
        jo.put("PaymentType", "TaiwanPay");
        jo.put("DeviceSN", merchantID);
        jo.put("VersionNo", "1.0.0");

        // 2. 修正後的簽章邏輯 (移除查詢 API 可能不需要的欄位)
        // 請務必參考 API 文件，確認 Query 介面所需的欄位順序
        String rawSignString = key
                + "&MerchantID=" + merchantID
                + "&TerminalID=" + terminalID
                + "&ShopOrderNo=" + orderId
                + "&VersionNo=1.0.0"
                + "&" + key;

        log.info("DEBUG: 查詢 API 簽章原始字串 -> {}", rawSignString);
        String sign = CryptUtil.toSHA256Encrypt(rawSignString);
        jo.put("Sign", sign);

        return taiwanPayClient.payment(jo.toString());

    }   
    
    @Override
    public String taiwanpayRefund(Integer amt, String orderId) throws Exception {
    	String merchantID = env.getProperty("taiwanpay.merchant-id");
        String terminalID = env.getProperty("taiwanpay.terminal-id");
        String key = env.getProperty("taiwanpay.key");
        
        if (merchantID == null || "unknown".equals(merchantID) || key == null) {
            log.error("設定檔讀取錯誤: merchantID={}, key={}", merchantID, key);
            throw new IllegalStateException("請檢查 application.yml 中 taiwanpay 相關參數設定是否正確");
        }
        
        String version = "1.0.0";

        // 1. 組裝 JSON 資料
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jo = mapper.createObjectNode();
        jo.put("MerchantID", merchantID);
        jo.put("TerminalID", terminalID);
        jo.put("ShopOrderNo", orderId);
        jo.put("Amount", amt);
        jo.put("PaymentType", "TaiwanPay");
        jo.put("DeviceSN", merchantID);
        jo.put("VersionNo", version);

        // 2. 計算簽章 (依照 TaiwanPay 的簽章規則串接)
        String rawData = key + "&MerchantID=" + merchantID
                       + "&TerminalID=" + terminalID
                       + "&ShopOrderNo=" + orderId
                       + "&Amount=" + amt
                       + "&VersionNo=" + version
                       + "&" + key;
        log.info("DEBUG: 退款 API 簽章原始字串 -> {}", rawData);
        
        String sign = CryptUtil.toSHA256Encrypt(rawData);
        jo.put("Sign", sign);

        log.info("Up(refund): {}", jo.toString());

        // 3. 呼叫 Feign Client
        String response = taiwanPayClient.refund(env.getProperty("Content-type"));

        log.info("Down(refund): {}", response);
        return response;
    }   
    
    @Override
    public String pxpayPayment(String orderId, String center, String counterId, String posId, 
                               String posDateTime, String productName, Integer amt, String oneTimeKey) throws Exception {
        
        String reqTime = GetDateTime.getTodayDateW("") + GetDateTime.getTime("");
        
        // 1. 使用 Jackson 組裝 JSON
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jo = mapper.createObjectNode();
        
        jo.put("store_id", center);
        jo.put("store_name", "比漾廣場");
        jo.put("pos_id", posId);
        jo.put("pos_trade_time", posDateTime);
        jo.put("mer_trade_no", orderId);
        jo.put("pay_token", oneTimeKey);
        jo.put("amount", amt);
        jo.put("none_discount_amount", 0);
        jo.put("none_feedback_amount", 0);
        jo.put("req_time", reqTime);
        
        ArrayNode ja = mapper.createArrayNode();
        ObjectNode joProduct = mapper.createObjectNode();
        joProduct.put("name", productName);
        joProduct.put("amount", amt);
        joProduct.put("qty", 1);
        ja.add(joProduct);
        
        jo.set("products", ja);

        // 2. 計算 HMAC 簽章
        String sign = CryptUtil.toHashHmacSHA256(center + posId + posDateTime + orderId + oneTimeKey + amt + reqTime, env.getProperty("PX-Key"));
        
        // 3. 設定 Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", env.getProperty("Content-type"));
        headers.put("PX-MerCode", env.getProperty("PX-MerCode"));
        headers.put("PX-MerEnName", env.getProperty("PX-MerEnName"));
        headers.put("PX-SignValue", sign);

        // 4. 發送請求
        log.info("Up(PXPay Purchase): {}", jo.toString());
        return pxPayClient.payment(headers, jo.toString());
    }

    @Override
    public String pxpayQuery(String orderId) throws Exception {
        // 1. 準備時間戳記
        String reqTime = GetDateTime.getTodayDateW("") + GetDateTime.getTime("");
        
        // 2. 計算簽章 (保留原有的 CryptUtil 邏輯)
        String sign = CryptUtil.toHashHmacSHA256("1" + orderId + reqTime, env.getProperty("PX-Key"));
        
        // 3. 組裝 Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", env.getProperty("Content-type"));
        headers.put("PX-MerCode", env.getProperty("PX-MerCode"));
        headers.put("PX-MerEnName", env.getProperty("PX-MerEnName"));
        headers.put("PX-SignValue", sign);
        
        // 4. 呼叫 Feign Client
        log.info("Up(query): orderId={}, reqTime={}", orderId, reqTime);
        String response = pxPayClient.query(headers, orderId, reqTime);
        log.info("Down(query): {}", response);
        
        return response;
    }   
    
    @Override
    public String pxpayRefund(String transactionId, String orderId, String center, String posId, 
                              String productName, Integer amt, String newOrderId, String posDateTime) throws Exception {
        
        // 1. 組裝 JSON 資料 (使用 Jackson ObjectNode)
        String reqTime = GetDateTime.getTodayDateW("") + GetDateTime.getTime("");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jo = mapper.createObjectNode();
        
        jo.put("store_id", center);
        jo.put("store_name", "比漾廣場");
        jo.put("pos_id", posId);
        jo.put("pos_trade_time", posDateTime);
        jo.put("ori_mer_trade_no", orderId);
        jo.put("mer_trade_no", newOrderId);
        jo.put("ori_px_trade_no", transactionId);
        jo.put("amount", amt);
        jo.put("none_discount_amount", 0);
        jo.put("none_feedback_amount", 0);
        jo.put("req_time", reqTime);
        
        // 建立巢狀產品資訊
        ArrayNode ja = mapper.createArrayNode();
        ObjectNode joProduct = mapper.createObjectNode();
        joProduct.put("name", productName);
        joProduct.put("amount", amt);
        joProduct.put("qty", 1);
        ja.add(joProduct);
        
        jo.set("products", ja);

        // 2. 計算簽章 (維持原有 CryptUtil 邏輯)
        String sign = CryptUtil.toHashHmacSHA256(center + posId + posDateTime + orderId + newOrderId + amt + reqTime, env.getProperty("key"));

        // 3. 設定 Headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", env.getProperty("pxpay.config.content-type", "application/json")); 
        headers.put("PX-MerCode", env.getProperty("pxpay.config.mer-code", ""));
        headers.put("PX-MerEnName", env.getProperty("pxpay.config.mer-en-name", ""));
        headers.put("PX-SignValue", sign);
        
	    // 額外檢查：如果關鍵參數確實遺失，在此處拋出明確的自定義異常，而不是 NPE
	    if (sign == null) {
	        throw new IllegalArgumentException("簽章產生失敗");
	    }

        log.info("Up(refund): {}", jo.toString());
        
        // 4. 呼叫 Feign Client
        String response = pxPayClient.refund(headers, jo.toString());
        
        log.info("Down(refund): {}", response);
        return response;
    }    
    
    // 輔助方法：統一 Query String 生成
    private String buildQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

 

	}
	

