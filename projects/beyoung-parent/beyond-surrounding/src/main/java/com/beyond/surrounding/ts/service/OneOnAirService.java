package com.beyond.surrounding.ts.service;

import com.beyond.surrounding.ts.client.OpenaiFeignClient;
import com.beyond.surrounding.ts.entity.CUSTOMER_SERVICE;
import com.beyond.surrounding.ts.entity.TS_OOA_LOG;
import com.beyond.surrounding.ts.repository.CustomerServiceRepository;
import com.beyond.surrounding.ts.repository.OneOnAirRepository;
import com.beyond.surrounding.ts.repository.TsOoaLogRepository;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.ts.bean.OOARequestBean;
import com.beyond.surrounding.ts.bean.OOAResponseBean;
import com.beyond.surrounding.ts.client.LineReplyFeignClient;
import com.beyond.surrounding.ts.client.OneOnAirPaymentFeignClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneOnAirService {

    private final OpenaiFeignClient openaiFeignClient;
    private final LineReplyFeignClient lineReplyFeignClient;
    private final CustomerServiceRepository customerServiceRepository;
    private final TsOoaLogRepository tsOoaLogRepository;   
    private final OneOnAirRepository oneOnAirRepository;   

    @Value("${api-config.openai.access-token:}") // 冒號代表如果找不到，預設為空字串，不崩潰
    private String openaiToken;

    @Value("${api-config.openai.model:gpt-4o}") // 找不到時，預設用 gpt-4o
    private String openaiModel;

    @Value("${api-config.line.channel-access-token:}") // 這樣就算測試環境沒寫，也能順利啟動 Context
    private String lineAccessToken;
    
    @Value("${api-config.line.it-robot-token:}")
    private String itRobotToken;
    
    @Value("${api-config.line.work-robot-token:}")
    private String workRobotToken;
    
    @Value("${api-config.one-on-air.api-url}")
    private String paymentApiUrl;
    @Value("${api-config.one-on-air.merchant-id}")
    private String merchantId;
    @Value("${api-config.one-on-air.api-key}")
    private String apiKey;
    @Value("${api-config.one-on-air.return-url}")
    private String returnUrl;
    @Value("${api-config.one-on-air.cancel-url}")
    private String cancelUrl;
    @Value("${api-config.one-on-air.confirm-url}")
    private String confirmUrl;
    @Value("${api-config.one-on-air.notify-url}")
    private String notifyUrl;

    private final OneOnAirPaymentFeignClient oneOnAirPaymentClient;

    @Transactional(readOnly = true)
    public void processAiRobot(String requestBody) throws Exception {
        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        if (!jsonObject.has("events")) return;

        JsonArray eventArray = jsonObject.get("events").getAsJsonArray();
        for (int i = 0; i < eventArray.size(); i++) {
            JsonObject event = eventArray.get(i).getAsJsonObject();
            
            if ("message".equals(event.get("type").getAsString())) {
                String replyToken = event.get("replyToken").getAsString();
                JsonObject messageObject = event.get("message").getAsJsonObject();
                
                if ("text".equals(messageObject.get("type").getAsString())) {
                    String textContent = messageObject.get("text").getAsString();
                    ArrayList<String> replyList = new ArrayList<>();

                    if (textContent.length() < 2) {
                        replyList.add("至少需要輸入2個字以上");
                    } else {
                        JSONObject openaiBody = new JSONObject();
                        openaiBody.put("model", openaiModel);
                        openaiBody.put("temperature", 0.7);

                        JSONArray messagesArray = new JSONArray();
                        JSONObject messageObj = new JSONObject();
                        messageObj.put("role", "user");
                        messageObj.put("content", textContent);
                        messagesArray.put(messageObj);
                        openaiBody.put("messages", messagesArray);

                        try {
                            String openaiResponse = openaiFeignClient.askOpenai(
                                    "Bearer " + openaiToken, 
                                    openaiBody.toString()
                            );
                            log.info("OpenAI API 回應: {}", openaiResponse);

                            JsonObject responseJson = JsonParser.parseString(openaiResponse).getAsJsonObject();
                            if (responseJson.has("error")) {
                                replyList.add(responseJson.get("error").getAsJsonObject().get("message").getAsString());
                            } else {
                                String aiContent = responseJson.get("choices").getAsJsonArray().get(0)
                                        .getAsJsonObject().getAsJsonObject("message").get("content").getAsString();
                                replyList.add(aiContent);
                            }
                        } catch (Exception e) {
                            log.error("呼召 OpenAI Feign 失敗: ", e);
                            replyList.add("系統忙碌中，請稍後再試。");
                        }
                    }

                    StringBuilder replyMessageBuilder = new StringBuilder("{\"replyToken\":\"%s\",\"messages\":[");
                    for (String msg : replyList) {
                        String escapedMsg = msg.replace("\n", "\\n").replace("\"", "\\\"");
                        replyMessageBuilder.append(String.format("{\"type\":\"text\",\"text\":\"%s\"},", escapedMsg));
                    }
                    if (replyMessageBuilder.length() > 0 && replyMessageBuilder.charAt(replyMessageBuilder.length() - 1) == ',') {
                        replyMessageBuilder.deleteCharAt(replyMessageBuilder.length() - 1);
                    }
                    replyMessageBuilder.append("]}");
                    String lineJsonString = String.format(replyMessageBuilder.toString(), replyToken);

                    try {
                        String lineResult = lineReplyFeignClient.replyToLine("Bearer " + lineAccessToken, lineJsonString);
                        log.info("LINE 平台回應成功: {}", lineResult);
                    } catch (Exception e) {
                        log.error("呼叫 LINE Reply Feign 失敗: ", e);
                    }
                }
            }
        }
    }
    
    @Transactional(readOnly = true)
    public void processItRobot(String requestBody) throws Exception {
        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        if (!jsonObject.has("events")) return;

        JsonArray eventArray = jsonObject.get("events").getAsJsonArray();
        for (int i = 0; i < eventArray.size(); i++) {
            JsonObject event = eventArray.get(i).getAsJsonObject();
            
            if ("message".equals(event.get("type").getAsString())) {
                String replyToken = event.get("replyToken").getAsString();
                JsonObject messageObject = event.get("message").getAsJsonObject();
                
                if ("text".equals(messageObject.get("type").getAsString())) {
                    String textContent = messageObject.get("text").getAsString();
                    ArrayList<String> replyList = new ArrayList<>();

                    if (textContent.length() < 2) {
                        replyList.add("至少需要輸入2個字以上");
                    } else {
                        //  保持原有的 CUSTOMER_SERVICE 類別名稱不變
                        List<CUSTOMER_SERVICE> customerServiceList = getContent(textContent);
                        
                        if (customerServiceList == null || customerServiceList.isEmpty()) {
                            replyList.add("很抱歉，無法回答您的問題，請洽相關客服人員");
                        } else {
                            for (CUSTOMER_SERVICE bean : customerServiceList) {
                                replyList.add(bean.getContent());
                            }
                        }
                    }

                    // 組裝發送給 LINE Reply API 的 JSON 訊息
                    StringBuilder replyMessageBuilder = new StringBuilder("{\"replyToken\":\"%s\",\"messages\":[");
                    for (String msg : replyList) {
                        String escapedMsg = msg.replace("\n", "\\n").replace("\"", "\\\"");
                        replyMessageBuilder.append(String.format("{\"type\":\"text\",\"text\":\"%s\"},", escapedMsg));
                    }
                    if (replyMessageBuilder.length() > 0 && replyMessageBuilder.charAt(replyMessageBuilder.length() - 1) == ',') {
                        replyMessageBuilder.deleteCharAt(replyMessageBuilder.length() - 1);
                    }
                    replyMessageBuilder.append("]}");
                    String lineJsonString = String.format(replyMessageBuilder.toString(), replyToken);

                    // 透過 Feign 將罐頭訊息回傳給 LINE 平台
                    try {
                        String lineResult = lineReplyFeignClient.replyToLine("Bearer " + itRobotToken, lineJsonString);
                        log.info("ITRobot LINE 回應成功: {}", lineResult);
                    } catch (Exception e) {
                        log.error("ITRobot 呼叫 LINE Reply Feign 失敗: ", e);
                    }
                }
            }
        }
    }
    
    public List<CUSTOMER_SERVICE> getContent(String message) throws Exception {
        log.info("開始查詢客服罐頭訊息，關鍵字: {}", message);
        return customerServiceRepository.findByContentContaining(message);
    }
    
    @Transactional(rollbackFor = Exception.class) // 確保刪除與新增在同一個 Transaction 中
    public void save(OOARequestBean requestBody, String caseId, String tradeNo) throws Exception {
        log.info("開始執行防重複寫入，刪除舊有的訂單編號: {}", requestBody.getOrder_no());
        
        // 1. 先執行刪除舊資料
        tsOoaLogRepository.deleteByOrderNo(requestBody.getOrder_no());
        
        // 2. 建立新 Entity 並賦值 (全面改為駝峰式變數)
        TS_OOA_LOG ooaLog = TS_OOA_LOG.builder()
                .order_no(requestBody.getOrder_no())
                .payment_type(requestBody.getPayment_type())
                .amt(requestBody.getAmt() != null ? requestBody.getAmt().doubleValue() : null) // 轉為 Double
                .card_no(requestBody.getCard_no())
                .case_id(caseId)
                .trade_no(tradeNo)
                .create_date(new java.util.Date()) // 配合原本的 Date 型態
                .build();
        
        // 3. 執行 Insert
        tsOoaLogRepository.save(ooaLog);
        log.info("新訂單紀錄儲存成功: {}", requestBody.getOrder_no());
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void processEchoRobot(String requestBody) throws Exception {
        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        if (!jsonObject.has("events")) return;

        JsonArray eventArray = jsonObject.get("events").getAsJsonArray();
        for (int i = 0; i < eventArray.size(); i++) {
            JsonObject event = eventArray.get(i).getAsJsonObject();
            
            if ("message".equals(event.get("type").getAsString())) {
                String replyToken = event.get("replyToken").getAsString();
                JsonObject messageObject = event.get("message").getAsJsonObject();
                
                if ("text".equals(messageObject.get("type").getAsString())) {
                    String textContent = messageObject.get("text").getAsString();

                    // 跳過資料庫與 AI，直接把接收到的 textContent 字串拿來組裝
                    StringBuilder replyMessageBuilder = new StringBuilder("{\"replyToken\":\"%s\",\"messages\":[");
                    
                    // 特殊字元跳脫防呆
                    String escapedMsg = textContent.replace("\n", "\\n").replace("\"", "\\\"");
                    replyMessageBuilder.append(String.format("{\"type\":\"text\",\"text\":\"%s\"}", escapedMsg));
                    
                    replyMessageBuilder.append("]}");
                    String lineJsonString = String.format(replyMessageBuilder.toString(), replyToken);

                    // 透過 Feign 用 WorkRobot 的 Token 發送回 LINE 平台
                    try {
                        String lineResult = lineReplyFeignClient.replyToLine("Bearer " + workRobotToken, lineJsonString);
                        log.info("EchoRobot LINE 回應成功: {}", lineResult);
                    } catch (Exception e) {
                        log.error("EchoRobot 呼叫 LINE Reply Feign 失敗: ", e);
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public OOAResponseBean processGetPaymentUrl(OOARequestBean requestBody) throws Exception {
        // 1. 取得時間 (維持原有自訂工具類命名)
        String todayDate = GetDateTime.getTodayDateW("");
        String todayTime = GetDateTime.getTime("");

        // 2. 組裝主要發送的 JSON 資料結構
        JSONObject jsonOri = new JSONObject();
        jsonOri.put("amount", requestBody.getAmt());
        jsonOri.put("merchanttradeno", requestBody.getOrder_no());
        jsonOri.put("merchantid", merchantId);
        jsonOri.put("merchanttradedate", todayDate);
        jsonOri.put("merchanttradetime", todayTime);
        jsonOri.put("ordertitle", "SHOP");

        JSONObject redirectUrls = new JSONObject();
        redirectUrls.put("returnurl", returnUrl + "?order_no=" + requestBody.getOrder_no());
        redirectUrls.put("cancelurl", cancelUrl);
        redirectUrls.put("confirmurl", confirmUrl);
        redirectUrls.put("notifyurl", notifyUrl);
        jsonOri.put("redirecturl", redirectUrls);

        // 3. 組裝用於排序加簽的 JSON 資料結構
        JSONObject jsonSort = new JSONObject();
        jsonSort.put("amount", requestBody.getAmt());
        jsonSort.put("merchantid", merchantId);
        jsonSort.put("merchanttradedate", todayDate);
        jsonSort.put("merchanttradeno", requestBody.getOrder_no());
        jsonSort.put("merchanttradetime", todayTime);
        jsonSort.put("ordertitle", "SHOP");

        JSONObject redirectUrlsSort = new JSONObject();
        redirectUrlsSort.put("cancelurl", cancelUrl);
        redirectUrlsSort.put("confirmurl", confirmUrl);
        redirectUrlsSort.put("notifyurl", notifyUrl);
        redirectUrlsSort.put("returnurl", returnUrl + "?order_no=" + requestBody.getOrder_no());
        jsonSort.put("redirecturl", redirectUrlsSort);

        // 4. 計算 SHA-256 簽章 (Sign)
        String signRequestStr = "request=" + jsonSort.toString() + "&apikey=" + apiKey;
        byte[] bytes = signRequestStr.getBytes(StandardCharsets.UTF_8);
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(bytes);
        
        //  捨棄舊 java 內建的 DatatypeConverter，改用標準 Base64 處理
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        jsonOri.put("sign", sign);

        log.info("準備發送金流請求 API: {}", jsonOri.toString());

        // 5. 動態組裝完整請求網址並透過 Feign 發送
        String targetPath = String.format("%s/%s/v1/qr/request", paymentApiUrl, requestBody.getPayment_type());
        URI targetUri = new URI(targetPath);
        
        String responseBodyStr = oneOnAirPaymentClient.getPaymentUrl(targetUri, jsonOri.toString());
        log.info("金流平台回應結果: {}", responseBodyStr);

        // 6. 解析回傳結果
        JsonObject retJson = JsonParser.parseString(responseBodyStr).getAsJsonObject();
        if (!"000".equals(retJson.get("rtncode").getAsString())) {
            throw new Exception(retJson.get("rtncode").getAsString() + " " + retJson.get("rtnmsg").getAsString());
        }

        // 7. 呼叫剛剛與舊 Entity 同步優化好的 save 存入 MySQL
        this.save(requestBody, retJson.get("caseid").getAsString(), retJson.get("servicetradeno").getAsString());

        // 8. 封裝傳回前端的 Response Bean
        JsonObject paymentUrlObj = retJson.getAsJsonObject("paymenturl");
        OOAResponseBean responseBean = new OOAResponseBean();
        responseBean.setCode(ErrCodeConst.finished);
        responseBean.setMessage(ErrCodeConst.finished_message);
        responseBean.setsOrderNO(requestBody.getOrder_no());
        responseBean.setsWebUrl(paymentUrlObj.get("web").getAsString());
        responseBean.setsAndroidUrl(paymentUrlObj.get("android").getAsString());
        responseBean.setsIOSUrl(paymentUrlObj.get("ios").getAsString());

        return responseBean;
    }
       
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean processRefund(OOARequestBean requestBody) throws Exception {
        // 1. 撈取原交易紀錄，查無單號則噴錯
        TS_OOA_LOG entity = this.getTranLog(requestBody.getOrder_no());
        if (entity.getOrder_no() == null) {
            throw new Exception("查無訂單編號：" + requestBody.getOrder_no());
        }

        String todayDate = GetDateTime.getTodayDateW("");
        String todayTime = GetDateTime.getTime("");

        // 2. 組裝傳給外部金流的主要 JSON 資料結構
        JSONObject jsonOri = new JSONObject();
        jsonOri.put("refundamount", requestBody.getRefund_amt());
        jsonOri.put("refunddesc", "REFUND");
        jsonOri.put("caseid", entity.getCase_id());
        jsonOri.put("merchanttradeno", requestBody.getNew_order_no());
        jsonOri.put("merchantid", merchantId);
        jsonOri.put("merchanttradedate", todayDate);
        jsonOri.put("merchanttradetime", todayTime);
        jsonOri.put("executorid", "taishinbank");

        // 3. 組裝用於排序加簽的 JSON 資料結構
        JSONObject jsonSort = new JSONObject();
        jsonSort.put("caseid", entity.getCase_id());
        jsonSort.put("executorid", "taishinbank");
        jsonSort.put("merchantid", merchantId);
        jsonSort.put("merchanttradedate", todayDate);
        jsonSort.put("merchanttradeno", requestBody.getNew_order_no());
        jsonSort.put("merchanttradetime", todayTime);
        jsonSort.put("refundamount", requestBody.getRefund_amt());
        jsonSort.put("refunddesc", "REFUND");

        // 4. 計算加簽 (SHA-256)
        String signRequestStr = "request=" + jsonSort.toString() + "&apikey=" + apiKey;
        byte[] bytes = signRequestStr.getBytes(StandardCharsets.UTF_8);
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(bytes);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        jsonOri.put("sign", sign);

        log.info("準備發送金流退款請求: {}", jsonOri.toString());

        // 5. 動態組裝完整退款路徑並利用現有的 OneOnAirPaymentClient (Feign) 傳送
        String targetPath = String.format("%s/%s/v1/qr/refund", paymentApiUrl, entity.getPayment_type());
        URI targetUri = new URI(targetPath);
        
        String responseBodyStr = oneOnAirPaymentClient.getPaymentUrl(targetUri, jsonOri.toString());
        log.info("金流平台退款回應: {}", responseBodyStr);

        // 6. 解析回應
        JsonObject retJson = JsonParser.parseString(responseBodyStr).getAsJsonObject();
        if (!"000".equals(retJson.get("rtncode").getAsString())) {
            throw new Exception(retJson.get("rtncode").getAsString() + " " + retJson.get("rtnmsg").getAsString());
        }

        // 7. 更新資料庫內的退款狀態欄位
        this.updateRefund(
            requestBody.getOrder_no(), 
            requestBody.getNew_order_no(), 
            requestBody.getRefund_amt() != null ? requestBody.getRefund_amt().doubleValue() : null, 
            retJson.get("servicetradeno").getAsString(), 
            retJson.get("servicetradestatus").getAsString()
        );

        // 8. 返回完成 Bean
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(ErrCodeConst.finished);
        responseBean.setMessage(ErrCodeConst.finished_message);
        return responseBean;
    }
    
    public TS_OOA_LOG getTranLog(String orderNo) {
        log.info("getTranLog 查詢訂單紀錄，單號: {}", orderNo);
        // 如果查不到，傳回一個空的 Entity 避免 processRefund 拋出 NullPointerException
        return tsOoaLogRepository.findByOrderNo(orderNo).orElse(new TS_OOA_LOG());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRefund(String orderNo, String refundOrderNo, Double amtRefund, 
                             String refundTradeNo, String refundStatus) throws Exception {
        log.info("updateRefund 更新訂單退款狀態，原單號: {}, 退款單號: {}, 金額: {}", orderNo, refundOrderNo, amtRefund);
        
        // 呼叫先前在 Repository 中寫好的 Native SQL Text Block 更新語句
        tsOoaLogRepository.updateRefundInfo(
            orderNo, 
            refundOrderNo, 
            amtRefund, 
            refundTradeNo, 
            refundStatus, 
            new java.util.Date() // 設定目前時間為退款時間
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public OOAResponseBean processQuery(OOARequestBean requestBody) throws Exception {
        // 1. 撈取原交易紀錄，查無單號則噴錯
        TS_OOA_LOG entity = this.getTranLog(requestBody.getOrder_no());
        if (entity.getOrder_no() == null) {
            throw new Exception("查無訂單編號：" + requestBody.getOrder_no());
        }

        String todayDate = GetDateTime.getTodayDateW("");
        String todayTime = GetDateTime.getTime("");

        // 2. 組裝發送金流的原始 JSON 結構
        JSONObject jsonOri = new JSONObject();
        jsonOri.put("caseid", entity.getCase_id());
        jsonOri.put("merchanttradeno", requestBody.getOrder_no());
        jsonOri.put("merchantid", merchantId);
        jsonOri.put("merchanttradedate", todayDate);
        jsonOri.put("merchanttradetime", todayTime);

        // 3. 組裝用於排序加簽的 JSON 結構
        JSONObject jsonSort = new JSONObject();
        jsonSort.put("caseid", entity.getCase_id());
        jsonSort.put("merchantid", merchantId);
        jsonSort.put("merchanttradedate", todayDate);
        jsonSort.put("merchanttradeno", requestBody.getOrder_no());
        jsonSort.put("merchanttradetime", todayTime);

        // 4. 計算加簽 (SHA-256)
        String signRequestStr = "request=" + jsonSort.toString() + "&apikey=" + apiKey;
        byte[] bytes = signRequestStr.getBytes(StandardCharsets.UTF_8);
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(bytes);
        String sign = Base64.getEncoder().encodeToString(hash).toUpperCase();
        jsonOri.put("sign", sign);

        log.info("準備發送金流查詢請求: {}", jsonOri.toString());

        // 5. 動態組裝完整查詢路徑，沿用現有的 OneOnAirPaymentClient (Feign) 傳送
        String targetPath = String.format("%s/%s/v1/qr/paydetails", paymentApiUrl, entity.getPayment_type());
        URI targetUri = new URI(targetPath);
        
        String responseBodyStr = oneOnAirPaymentClient.getPaymentUrl(targetUri, jsonOri.toString());
        log.info("金流平台查詢回應: {}", responseBodyStr);

        // 6. 解析回應
        JsonObject retJson = JsonParser.parseString(responseBodyStr).getAsJsonObject();

        // 7. 解析支付狀態與時間
        JsonObject paymentInfoObj = retJson.getAsJsonObject("info");
        String paymentStatus = paymentInfoObj.get("servicetradestatus").getAsString();
        String rawPaymentDateTime = paymentInfoObj.get("servicetradedate").getAsString() + " " + paymentInfoObj.get("servicetradetime").getAsString();

        // 8. 解析退款狀態與時間 (如果有退款紀錄的話)
        String refundStatus = "";
        String rawRefundDateTime = "";
        JsonArray refundList = retJson.getAsJsonArray("refundlist");
        if (refundList != null && refundList.size() > 0) {
            JsonObject refundObj = refundList.get(0).getAsJsonObject();
            refundStatus = refundObj.get("refundtradetype").getAsString();
            rawRefundDateTime = refundObj.get("refundtradedate").getAsString() + " " + refundObj.get("refundtradetime").getAsString();
        }

        // 9. 使用新版 DateTimeFormatter 機制轉換時間格式
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedPaymentDateTime = formatDateTimeString(rawPaymentDateTime, inputFormatter, outputFormatter);
        String formattedRefundDateTime = formatDateTimeString(rawRefundDateTime, inputFormatter, outputFormatter);

        // 10. 封裝傳回前端的 Response Bean
        OOAResponseBean responseBean = new OOAResponseBean();
        responseBean.setCode(ErrCodeConst.finished);
        responseBean.setMessage(ErrCodeConst.finished_message);
        responseBean.setsPaymentStatus(paymentStatus);
        responseBean.setsPaymentDateTime(formattedPaymentDateTime);
        responseBean.setsRefundStatus(refundStatus);
        responseBean.setsRefundDateTime(formattedRefundDateTime);

        return responseBean;
    }

    private String formatDateTimeString(String rawDateTime, DateTimeFormatter inputFormatter, DateTimeFormatter outputFormatter) {
        if (rawDateTime == null || rawDateTime.trim().isEmpty() || "null".equalsIgnoreCase(rawDateTime.trim())) {
            return "";
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(rawDateTime.trim(), inputFormatter);
            return localDateTime.format(outputFormatter);
        } catch (Exception e) {
            log.warn("時間格式解析失敗 [{}], 傳回空字串", rawDateTime);
            return "";
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateConfirm(String orderNo) throws Exception {
        log.info("updateConfirm 更新訂單確認時間，單號: {}", orderNo);
        if (orderNo == null || orderNo.trim().isEmpty()) {
            throw new Exception("更新確認時間失敗：單號不能為空");
        }
        
        // 更新 confirm_date 為當下時間
        tsOoaLogRepository.updateConfirmDate(orderNo, new java.util.Date());
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateNotify(String orderNo, String retCode, String retMsg) throws Exception {
        log.info("updateNotify 更新通知狀態，單號: {}, 狀態碼: {}, 訊息: {}", orderNo, retCode, retMsg);
        if (orderNo == null || orderNo.trim().isEmpty()) {
            throw new Exception("更新通知失敗：單號不能為空");
        }
        
        // 更新狀態，並填入當下時間作為 notify_date
        tsOoaLogRepository.updateNotifyInfo(orderNo, retCode, retMsg, new java.util.Date());
    }

    @Transactional(readOnly = true)
    public TS_OOA_LOG getStatus(String sOrderNO, String sPaymentType) throws Exception {
        // 避免回傳 null 導致 API 報 NullPointerException，若查無資料則回傳一個空物件
        return oneOnAirRepository.findByOrderNoAndPaymentType(sOrderNO, sPaymentType)
                .orElseGet(TS_OOA_LOG::new);
    }
    
    
    
    
}