package com.beyond.surrounding.ts.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyond.surrounding.ts.bean.PayPlusRequestBean;
import com.beyond.surrounding.ts.bean.PayPlusResponseBean;
import com.beyond.surrounding.ts.bean.PayPlusResultBean;
import com.beyond.surrounding.ts.bean.ResultData;
import com.beyond.surrounding.ts.client.PayPlusFeignClient;
import com.beyond.surrounding.ts.entity.CREDIT_CARD_BIND_LOG;
import com.beyond.surrounding.ts.entity.TS_PAYPLUS_LOG;
import com.beyond.surrounding.ts.repository.CreditCardBindRepository;
import com.beyond.surrounding.ts.repository.TsPayPlusLogRepository;
import com.beyond.surrounding.util.CryptUtil;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayPlusService {

    private final PayPlusFeignClient payPlusClient;

    // 透過 @Value 直接注入對應的 yml 設定值
    @Value("${api-config.one-on-air.merchant-id}")
    private String merchantId; // 對應原本的 ApposId

    @Value("${api-config.one-on-air.api-key}")
    private String apiKey;     // 對應原本的 ApiKey
    
    // 注入對應的跳轉/回傳 URL
    @Value("${api-config.one-on-air.result-url}")
    private String resultUrl;

    @Value("${api-config.one-on-air.success-url}")
    private String successUrl;

    @Value("${api-config.one-on-air.fail-url}")
    private String failUrl;

    private final CreditCardBindRepository creditCardBindRepository;
    private final TsPayPlusLogRepository tsPayPlusLogRepository;
    
    // Spring Boot 建議共用 ObjectMapper 提高效能
    private final ObjectMapper objectMapper = new ObjectMapper();
    

    @Transactional(readOnly = true)
    public ResultData processPaymentBarcode(PayPlusRequestBean requestBody) throws Exception {
        log.info("processPaymentBarcode 開始處理: CardToken -> {}, MemberId -> {}", 
                 requestBody.getCard_token(), requestBody.getMember_id());

        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 1. 組裝原始請求物件 (joOri)
        JSONObject joOri = new JSONObject();
        joOri.put("ApiVer", "1.0.1");
        joOri.put("ApposId", merchantId);
        joOri.put("TransNo", requestBody.getTrans_no());
        
        JSONObject joRequest = new JSONObject();
        joRequest.put("CardToken", requestBody.getCard_token());
        joRequest.put("MemberId", requestBody.getMember_id());
        joRequest.put("IsRedeem", "N");
        
        joOri.put("RequestParams", joRequest);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 2. 組裝排序用物件以計算 CheckSum (joSort)
        JSONObject joSort = new JSONObject();
        joSort.put("ApiVer", "1.0.1");
        joSort.put("ApposId", merchantId);
        joSort.put("Random", sRandom);
        
        JSONObject joRequestSort = new JSONObject();
        joRequestSort.put("CardToken", requestBody.getCard_token());
        joRequestSort.put("IsRedeem", "N");
        joRequestSort.put("MemberId", requestBody.getMember_id());
        
        joSort.put("RequestParams", joRequestSort);
        joSort.put("TimeStamp", String.valueOf(lTime));
        joSort.put("TransNo", requestBody.getTrans_no());

        // 3. 計算簽章 (CheckSum)
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        
        // 使用 Java 內建 Base64 API 代替舊版的 DatatypeConverter
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("getPaymentBarcode 發送 Feign Request : {}", joOri.toString());

        // 4. 透過 Feign Client 發送遠端 POST 請求
        String value = payPlusClient.getPaymentBarcode(joOri.toString());
        log.info("getPaymentBarcode 收到 Feign Response : {}", value);

        // 5. 解析遠端回傳結果 (使用 Jackson ObjectMapper)
        JsonNode ret = objectMapper.readTree(value);
        String rtnCode = ret.path("RtnCode").asText();
        
        // 如果遠端回傳狀態碼不是 1000，直接拋出例外
        if (!"1000".equals(rtnCode)) {
            String rtnMessage = ret.path("RtnMessage").asText();
            throw new Exception(rtnCode + " " + rtnMessage);
        }

        // 取得回傳結構中的 ResultData 節點
        JsonNode resultDataNode = ret.path("ResponseParams").path("ResultData");

        // 6. 封裝成要返回前端的 ResultData 物件
        ResultData bean = new ResultData();
        bean.setMemberId(requestBody.getMember_id());
        bean.setCardToken(requestBody.getCard_token());
        bean.setBarcode(resultDataNode.path("Barcode").asText());
        bean.setExpDate(resultDataNode.path("ExpDate").asText());
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultData getCreditCardStatus(PayPlusRequestBean requestBody) throws Exception {
        log.info("getCreditCardStatus 開始處理: OrderNo -> {}, MemberId -> {}", 
                 requestBody.getOrder_no(), requestBody.getMember_id());

        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 1. 組裝原始請求物件 (_joOri)
        JSONObject joOri = new JSONObject();
        joOri.put("ApiVer", "1.0.1");
        joOri.put("ApposId", merchantId);
        joOri.put("TransNo", requestBody.getTrans_no());
        
        JSONObject joRequest = new JSONObject();
        joRequest.put("OrderNo", requestBody.getOrder_no());
        joRequest.put("MemberId", requestBody.getMember_id());
        
        joOri.put("RequestParams", joRequest);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 2. 組裝排序用物件以計算 CheckSum (_joSort)
        JSONObject joSort = new JSONObject();
        joSort.put("ApiVer", "1.0.1");
        joSort.put("ApposId", merchantId);
        joSort.put("Random", sRandom);
        
        JSONObject joRequestSort = new JSONObject();
        joRequestSort.put("MemberId", requestBody.getMember_id());
        joRequestSort.put("OrderNo", requestBody.getOrder_no());
        
        joSort.put("RequestParams", joRequestSort);
        joSort.put("TimeStamp", String.valueOf(lTime));
        joSort.put("TransNo", requestBody.getTrans_no());

        // 3. 計算簽章 (CheckSum)
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("getCreditCardStatus 發送 Feign Request : {}", joOri.toString());

        // 4. 透過 Feign Client 發送遠端 POST 請求
        String value = payPlusClient.getCreditCardStatus(joOri.toString());
        log.info("getCreditCardStatus 收到 Feign Response : {}", value);

        // 5. 解析遠端回傳結果
        JsonNode ret = objectMapper.readTree(value);
        String rtnCode = ret.path("RtnCode").asText();
        
        if (!"1000".equals(rtnCode)) {
            throw new Exception(rtnCode + " " + ret.path("RtnMessage").asText());
        }

        JsonNode resultDataNode = ret.path("ResponseParams").path("ResultData");

        // 6. 將結果轉為 JSONObject，帶入 OrderNo 後執行資料庫更新邏輯
        JSONObject joForUpdate = new JSONObject(resultDataNode.toString());
        // 原本的 _jo 沒有 OrderNo，必須從 requestBody 拿進去放，更新時才找得到該筆資料
        joForUpdate.put("OrderNo", requestBody.getOrder_no());
        
        this.updateQuery(joForUpdate);

        // 7. 封裝前端所需的回傳 Bean
        ResultData bean = new ResultData();
        bean.setBankNo(resultDataNode.path("BankNo").asText());
        bean.setCardName(resultDataNode.path("CardName").asText());
        bean.setCardNumber(resultDataNode.path("CardNumber").asText());
        bean.setCardStatus(resultDataNode.path("CardStatus").asText());
        bean.setCardType(resultDataNode.path("CardType").asText());
        bean.setCardToken(resultDataNode.path("CardToken").asText());            
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateQuery(JSONObject jo) {
        log.info("開始更新信用卡狀態至資料庫, OrderNo: {}", jo.optString("OrderNo"));
        
        // 透過 JPA Repository Native Query 執行更新
        int updatedRows = creditCardBindRepository.updateCreditCardStatus(
            jo.optString("OrderNo"),
            jo.optString("BankNo"),
            jo.optString("CardName"),
            jo.optString("CardNumber"),
            jo.optString("CardStatus"),
            jo.optString("CardType"),
            jo.optString("CardToken")
        );
        
        log.info("資料庫更新完成，影響筆數: {}", updatedRows);
    }
  
    @Transactional(rollbackFor = Exception.class)
    public PayPlusResponseBean deleteCreditCardAuth(PayPlusRequestBean requestBody) throws Exception {
        log.info("deleteCreditCardAuth 開始處理: TransNo -> {}, MemberId -> {}, CardToken -> {}", 
                 requestBody.getTrans_no(), requestBody.getMember_id(), requestBody.getCard_token());

        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 1. 組裝原始請求 _joOri
        JSONObject joOri = new JSONObject();
        joOri.put("ApiVer", "1.0.0");
        joOri.put("ApposId", merchantId);
        joOri.put("TransNo", requestBody.getTrans_no());
        
        JSONObject joRequest = new JSONObject();
        joRequest.put("CardToken", requestBody.getCard_token());
        joRequest.put("MemberId", requestBody.getMember_id());
        
        joOri.put("RequestParams", joRequest);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 2. 組裝排序用 _joSort (用於算簽章)
        JSONObject joSort = new JSONObject();
        joSort.put("ApiVer", "1.0.0");
        joSort.put("ApposId", merchantId);
        joSort.put("Random", sRandom);
        
        JSONObject joRequestSort = new JSONObject();
        joRequestSort.put("CardToken", requestBody.getCard_token());
        joRequestSort.put("MemberId", requestBody.getMember_id());
        
        joSort.put("RequestParams", joRequestSort);
        joSort.put("TimeStamp", String.valueOf(lTime));
        joSort.put("TransNo", requestBody.getTrans_no());

        // 3. 計算簽章
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("deleteCreditCardAuth 發送 Feign Request : {}", joOri.toString());

        // 4. 透過 Feign 遠端呼叫
        String value = payPlusClient.deleteCreditCardAuth(joOri.toString());
        log.info("deleteCreditCardAuth 收到 Feign Response : {}", value);

        // 5. 解析回傳狀態
        JsonNode ret = objectMapper.readTree(value);
        String rtnCode = ret.path("RtnCode").asText();
        if (!"1000".equals(rtnCode)) {
            throw new Exception(rtnCode + " " + ret.path("RtnMessage").asText());
        }

        // 6. 異動資料庫狀態 (刪除註記)
        int updatedRows = creditCardBindRepository.updateDeleteMark(
            requestBody.getOrder_no(), 
            requestBody.getMember_id()
        );
        log.info("資料庫刪除註記更新完成，影響筆數: {}", updatedRows);

        // 7. 回傳結果 Bean
        PayPlusResponseBean bean = new PayPlusResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);
        
        return bean;
    }
    
    public PayPlusResponseBean getCreditCardList(PayPlusRequestBean requestBody) throws Exception {
        log.info("getCreditCardList 開始處理: TransNo -> {}, MemberId -> {}", 
                 requestBody.getTrans_no(), requestBody.getMember_id());

        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 1. 組裝原始傳入參數
        JSONObject joOri = new JSONObject();
        joOri.put("ApiVer", "1.0.1");
        joOri.put("ApposId", merchantId);
        joOri.put("TransNo", requestBody.getTrans_no());
        
        JSONObject joRequest = new JSONObject();
        joRequest.put("MemberId", requestBody.getMember_id());
        
        joOri.put("RequestParams", joRequest);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 2. 組裝排序用參數以取得簽章
        JSONObject joSort = new JSONObject();
        joSort.put("ApiVer", "1.0.1");
        joSort.put("ApposId", merchantId);
        joSort.put("Random", sRandom);
        
        JSONObject joRequestSort = new JSONObject();
        joRequestSort.put("MemberId", requestBody.getMember_id());
        
        joSort.put("RequestParams", joRequestSort);
        joSort.put("TimeStamp", String.valueOf(lTime));
        joSort.put("TransNo", requestBody.getTrans_no());

        // 3. 壓製 CheckSum
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("getCreditCardList 發送 Feign Request : {}", joOri.toString());

        // 4. 進行 Feign 連線
        String value = payPlusClient.getCreditCardList(joOri.toString());
        log.info("getCreditCardList 收到 Feign Response : {}", value);

        // 5. 解析與比對狀態
        JsonNode rootNode = objectMapper.readTree(value);
        String rtnCode = rootNode.path("RtnCode").asText();
        if (!"1000".equals(rtnCode)) {
            throw new Exception(rtnCode + " " + rootNode.path("RtnMessage").asText());
        }

        // 6. 提煉 ResultData 列表
        JsonNode resultDataNode = rootNode.path("ResponseParams").path("ResultData");
        List<ResultData> list = objectMapper.convertValue(resultDataNode, new TypeReference<List<ResultData>>() {});

        // 7. 封裝 Response
        PayPlusResponseBean bean = new PayPlusResponseBean();
        bean.setlResultData(list);
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);

        return bean;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public PayPlusResponseBean getCardPage(PayPlusRequestBean requestBody) throws Exception {
        log.info("getCardPage 開始處理: order_no -> {}, MemberId -> {}, PaymentType -> {}", 
                 requestBody.getOrder_no(), requestBody.getMember_id(), requestBody.getPayment_type());

        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 將回傳網址進行 URL 轉碼
        String encodedResultUrl = URLEncoder.encode(resultUrl, StandardCharsets.UTF_8);
        String encodedSuccessUrl = URLEncoder.encode(successUrl, StandardCharsets.UTF_8);
        String encodedFailUrl = URLEncoder.encode(failUrl, StandardCharsets.UTF_8);

        // 1. 組裝原始傳入參數 _joOri
        JSONObject joOri = new JSONObject();
        joOri.put("ApiVer", "1.0.2");
        joOri.put("ApposId", merchantId);
        
        JSONObject joRequest = new JSONObject();
        joRequest.put("OrderNo", requestBody.getOrder_no());
        joRequest.put("MemberId", requestBody.getMember_id());
        joRequest.put("ResultUrl", encodedResultUrl);
        joRequest.put("SuccessUrl", encodedSuccessUrl);
        joRequest.put("FailUrl", encodedFailUrl);
        joRequest.put("PaymentType", "04"); // 預設固定 04
        
        joOri.put("RequestParams", joRequest);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 2. 組裝排序用參數以取得簽章
        JSONObject joSort = new JSONObject();
        joSort.put("ApiVer", "1.0.2");
        joSort.put("ApposId", merchantId);
        joSort.put("Random", sRandom);
        
        JSONObject joRequestSort = new JSONObject();
        joRequestSort.put("FailUrl", encodedFailUrl);
        joRequestSort.put("MemberId", requestBody.getMember_id());
        joRequestSort.put("OrderNo", requestBody.getOrder_no());
        joRequestSort.put("PaymentType", "04");
        joRequestSort.put("ResultUrl", encodedResultUrl);
        joRequestSort.put("SuccessUrl", encodedSuccessUrl);
        
        joSort.put("RequestParams", joRequestSort);
        joSort.put("TimeStamp", String.valueOf(lTime));

        // 3. 壓製 CheckSum 簽章
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("getCardPage 發送 Feign Request : {}", joOri.toString());

        // 4. 進行 Feign 遠端呼叫
        String value = payPlusClient.getCardPage(joOri.toString());
        log.info("getCardPage 收到 Feign Response : {}", value);

        // 5. 解析與比對狀態
        JsonNode rootNode = objectMapper.readTree(value);
        String rtnCode = rootNode.path("RtnCode").asText();
        if (!"1000".equals(rtnCode)) {
            throw new Exception(rtnCode + " " + rootNode.path("RtnMessage").asText());
        }

        // 6. 保存當前交易/綁定紀錄
        // 這裡可視實作對 Entity 進行映射後存入 db。
        // 例如：creditCardBindRepository.save(new CreditCardBindLog(requestBody));
        // 此處對應原本的 save(requestBody)：
        this.save(requestBody); 
        log.info("交易資料庫紀錄保存成功。");

        // 7. 取得回傳的 3D 認證/綁卡跳轉頁面 URL
        String cardAuthUrl = rootNode.path("ResponseParams").path("ResultData").path("CardAuthUrl").asText();

        // 8. 封裝 Response
        PayPlusResponseBean bean = new PayPlusResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);
        bean.setsOrderNO(requestBody.getOrder_no());
        bean.setsCardAuthUrl(cardAuthUrl);

        return bean;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(PayPlusRequestBean requestBody) {
        // 1. 建立對應的 Entity 物件
        CREDIT_CARD_BIND_LOG entity = new CREDIT_CARD_BIND_LOG();
        
        // 2. 將 RequestBean 的欄位值 mapping 轉入 Entity
        entity.setOrderNo(requestBody.getOrder_no());
        entity.setMemberId(requestBody.getMember_id());
        entity.setCardStatus("INIT"); // 或是您定義的初始狀態，例如 "PENDING"
        // entity.set... 根據您資料表設計填入其他必要的預設欄位
        
        // 3. 呼叫 JpaRepository 自帶的 save 方法存入資料庫
        creditCardBindRepository.save(entity);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public String updateResult(PayPlusResultBean requestBody) throws Exception {
        var params = requestBody.getRequestParams();
        
        log.info("AddCard getResult : OrderNo -> {}, MemberId -> {}, Token -> {}", 
                 params.getOrderNo(), params.getMemberId(), params.getCardToken());

        // 1. 更新資料庫中的信用卡狀態 (使用先前在 Repository 定義的 Native SQL)
        int updatedRows = creditCardBindRepository.updateCreditCardStatus(
            params.getOrderNo(),
            params.getBankNo(),
            params.getCardName(),
            params.getCardNumber(),
            "ACTIVE", // 綁定成功狀態設定為 ACTIVE
            params.getCardType(),
            params.getCardToken()
        );
        log.info("updateResult 資料庫更新完成，影響筆數: {}", updatedRows);

        // 2. 準備回傳給 PayPlus 的確認 JSON 
        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 原始 JSON (_joOri)
        JSONObject joOri = new JSONObject();
        joOri.put("OrderNo", params.getOrderNo());
        joOri.put("IsSuccess", true);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 排序 JSON (_joSort) 用於簽章
        JSONObject joSort = new JSONObject();
        joSort.put("IsSuccess", true);
        joSort.put("OrderNo", params.getOrderNo());
        joSort.put("Random", sRandom);
        joSort.put("TimeStamp", String.valueOf(lTime));

        // 3. 計算簽章 (CheckSum)
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("getResult 回應 PayPlus 的確認 JSON: {}", joOri.toString());
        return joOri.toString();
    }
    
 // ==========================================
    // 1. 對外：負責解析 JSON、處理簽章、回傳結果
    // ==========================================
    @Transactional(rollbackFor = Exception.class)
    public String save3DResult(String requestBodyJson) throws Exception {
        log.info("BPPay get3DPage Result TS2Api：{}", requestBodyJson);

        // 使用 Jackson 解析傳入的 JSON 字串
        JsonNode rootNode = objectMapper.readTree(requestBodyJson);
        
        // 呼叫下方的「資料庫儲存邏輯」方法
        this.save3DResult(rootNode); 

        // 取得交易單號
        String merchantTradeNo = rootNode.path("RequestParams").path("MerchantTradeNo").asText();

        long lTime = System.currentTimeMillis() / 1000;
        String sRandom = StringUtil.getRandomID(16);

        // 組裝原始回應參數
        JSONObject joOri = new JSONObject();
        joOri.put("MerchantTradeNo", merchantTradeNo);
        joOri.put("IsSuccess", true);
        joOri.put("TimeStamp", String.valueOf(lTime));
        joOri.put("Random", sRandom);

        // 組裝簽章排序參數
        JSONObject joSort = new JSONObject();
        joSort.put("IsSuccess", true);
        joSort.put("MerchantTradeNo", merchantTradeNo);
        joSort.put("Random", sRandom);
        joSort.put("TimeStamp", String.valueOf(lTime));

        // 計算 CheckSum 簽章
        String sRequest = "request=" + joSort.toString() + "&apikey=" + apiKey;
        byte[] hash = CryptUtil.toSHA256(sRequest);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        joOri.put("CheckSum", sign);

        log.info("BPPay get3DPage Result Api2TS ：{}", joOri.toString());
        return joOri.toString();
    }

    // ==========================================
    // 2. 對內：負責將解析後的資料存入資料庫 (真正實作)
    // ==========================================
    @Transactional(rollbackFor = Exception.class)
    public void save3DResult(JsonNode rootNode) throws Exception {
        // 1. 從 JSON 中安全地解析出所需欄位
        JsonNode params = rootNode.path("RequestParams");
        
        // 取得交易單號 (對應 ORDER_NO)
        String merchantTradeNo = params.path("MerchantTradeNo").asText();
        
        // 取得交易狀態 (例如: "SUCCESS", "1" 代表成功，依據您的 PayPlus 規格書調整)
        String status = params.path("Status").asText(); 
        
        // 取得卡片 Token (若 3D 驗證成功有回傳的話)
        String cardToken = params.path("CardToken").isNull() ? null : params.path("CardToken").asText();

        log.info("【3D 驗證回調】開始更新資料庫 - 交易單號(OrderNo): {}, 原始狀態: {}, Token: {}", 
                 merchantTradeNo, status, cardToken);

        if (merchantTradeNo == null || merchantTradeNo.trim().isEmpty()) {
            throw new IllegalArgumentException("MerchantTradeNo 欄位為空，無法更新交易狀態！");
        }

        // 2. 根據 PayPlus 回傳的狀態，決定我們資料庫要寫入什麼 CARD_STATUS
        String dbStatus;
        if ("SUCCESS".equalsIgnoreCase(status) || "00".equals(status)) { 
            dbStatus = "ACTIVE"; // 驗證成功，啟用卡片
        } else {
            dbStatus = "FAILED"; // 驗證失敗
        }

        // 3. 呼叫 Repository 執行資料庫 UPDATE
        int updatedRows = creditCardBindRepository.update3DStatus(merchantTradeNo, dbStatus, cardToken);
        
        log.info("【3D 驗證回調】資料庫更新完成。更新狀態為: {}, 影響筆數: {} 筆", dbStatus, updatedRows);
        
        if (updatedRows == 0) {
            log.warn("【警告】找不到對應的訂單編號: {}，無法更新 3D 驗證狀態！", merchantTradeNo);
        }
    }

    @Transactional(readOnly = true)
    public TS_PAYPLUS_LOG get3DPage(String memberId, String barcode) {
        
        // 1. 呼叫 Repository 執行 Native Query 查詢
        return tsPayPlusLogRepository.find3DPageProjection(memberId, barcode)
                .map(projection -> {
                    // 2. 將 Projection 轉換為原本的 TS_PAYPLUS_LOG 物件 (類似原本的 aliasToBean)
                    TS_PAYPLUS_LOG bean = new TS_PAYPLUS_LOG();
                    bean.setMember_id(projection.getMemberId());
                    bean.setBarcode(projection.getBarcode());
                    bean.setOrder_no(projection.getOrderNo());
                    bean.setHpp_url(projection.getHppUrl());
                    return bean;
                })
                // 3. 若查無資料，回傳一個乾淨的空物件 (符合原本實作：if (_entity.getMember_id() == null) 的判斷)
                .orElseGet(TS_PAYPLUS_LOG::new);
    }
    
}


