package com.beyoung.surrounding.pos.service;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beyoung.surrounding.pos.client.PrivilegeServiceFeignClient;
import com.beyoung.surrounding.util.CryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class PrivilegeService {

	private final Environment env;
    private final PrivilegeServiceFeignClient privilegeServiceClient;

    @Transactional
    public String search(Environment env, String type, String identity, String brandCode, String storeCode,
            String sourceUuid, String privilegeCode) {
        try {
            // 1. 使用 Jackson 建立多層巢狀 JSON 結構
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            // 建立 member_identity 節點
            com.fasterxml.jackson.databind.node.ObjectNode memberIdentity = mapper.createObjectNode();
            memberIdentity.put("type", type);
            memberIdentity.put("identity", identity);

            // 建立 request_parameter 節點
            com.fasterxml.jackson.databind.node.ObjectNode requestParameter = mapper.createObjectNode();
            requestParameter.set("member_identity", memberIdentity);
            requestParameter.put("brand_code", brandCode);
            requestParameter.put("store_code", storeCode);
            requestParameter.put("source_uuid", sourceUuid);
            requestParameter.put("privilege_code", privilegeCode);
            requestParameter.put("quantity", 1);

            // 建立最外層 _jo 節點
            com.fasterxml.jackson.databind.node.ObjectNode jo = mapper.createObjectNode();
            jo.set("request_parameter", requestParameter);
            
            // 時間格式化
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            jo.put("timestamp", sdf.format(new java.util.Date()));

            String joJsonString = mapper.writeValueAsString(jo);
            log.info("Up(search)：{}", joJsonString);

            // 2. Base64 編碼 Payload
            String sPlayLod = java.util.Base64.getEncoder().encodeToString(
                    joJsonString.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
            
            // 3. HmacSHA256 簽章
            String sSignature = CryptUtil.toHmacSHA256(sPlayLod, env.getProperty("SubscriptKey"));
            
            // 4. 組裝最終送出的簽章大物件
            com.fasterxml.jackson.databind.node.ObjectNode joAll = mapper.createObjectNode();
            joAll.put("sign", sPlayLod + "." + sSignature);

            String finalRequestBody = mapper.writeValueAsString(joAll);
            log.info("Encode(search)：{}", finalRequestBody);

            // 5. 透過 Feign 帶入動態 Header 與 Body 發出請求
            String appId = env.getProperty("SubscriptAppId");
            String response = privilegeServiceClient.executeSearch(appId, finalRequestBody);

            log.info("Down(search)：{}", response);
            return response;

        } catch (Exception e) {
            log.error("特權服務內部查詢失敗: {}", e.getMessage(), e);
            // 拋出 RuntimeException 讓上層 Controller 的 catch 機制能夠統一捕捉並回傳 417
            throw new RuntimeException("特權服務通訊異常: " + e.getMessage());
        }
    }

    /**
     * 執行中台交易狀態檢查
     * * @param convertedType 已在 Controller 轉換完成的類型 (transaction_id 或 mmrm_tid)
     * @param transactionId 交易序號
     * @return 中台回傳的 JSON 字串
     */
    @Transactional
    public String transactionCheck(String convertedType, String transactionId) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            // 1. 建立 request_parameter 內部節點
            com.fasterxml.jackson.databind.node.ObjectNode requestParameter = mapper.createObjectNode();
            requestParameter.put("type", convertedType); // transaction_id 或 mmrm_tid
            requestParameter.put("id", transactionId);

            // 2. 建立最外層 jo 節點
            com.fasterxml.jackson.databind.node.ObjectNode jo = mapper.createObjectNode();
            jo.set("request_parameter", requestParameter);
            
            // 時間格式化 (yyyy/MM/dd HH:mm:ss)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            jo.put("timestamp", sdf.format(new java.util.Date()));

            String joJsonString = mapper.writeValueAsString(jo);
            log.info("Up(transactionCheck)：{}", joJsonString);

            // 3. 將 Payload 轉為 Base64 字串
            String sPlayLod = java.util.Base64.getEncoder().encodeToString(
                    joJsonString.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
            
            // 4. 利用驗證金鑰進行 HmacSHA256 簽章
            String sSignature = CryptUtil.toHmacSHA256(sPlayLod, env.getProperty("SubscriptKey"));
            
            // 5. 封裝至 final Body 外殼 ("sign": "Base64.Signature")
            com.fasterxml.jackson.databind.node.ObjectNode joAll = mapper.createObjectNode();
            joAll.put("sign", sPlayLod + "." + sSignature);

            String finalRequestBody = mapper.writeValueAsString(joAll);
            log.info("Encode(transactionCheck)：{}", finalRequestBody);

            // 6. 透過 FeignClient 動態傳遞 App-Id Header 並送出 POST 請求
            String appId = env.getProperty("SubscriptAppId");
            String response = privilegeServiceClient.executeTransactionCheck(appId, finalRequestBody);

            log.info("Down(transactionCheck)：{}", response);
            return response;

        } catch (Exception e) {
            log.error("交易狀態檢查中台連線異常: {}", e.getMessage(), e);
            // 拋出 RuntimeException，讓上層 Controller 捕捉並回傳標準 417 錯誤 Bean
            throw new RuntimeException("交易狀態檢查連線異常: " + e.getMessage());
        }
    }

    /**
     * 查詢會員當前可用的特權清單
     * * @param brandCode 品牌代碼
     * @param storeCode 門市代碼
     * @param type 認證類型 (cardno, mobile)
     * @param identity 會員識別碼
     * @return 中台回傳的 JSON 字串回應
     */
    @Transactional
    public String availableList(String brandCode, String storeCode, String type, String identity) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            // 1. 建立 member_identity 節點
            com.fasterxml.jackson.databind.node.ObjectNode memberIdentity = mapper.createObjectNode();
            memberIdentity.put("type", type); // cardno, mobile
            memberIdentity.put("identity", identity);

            // 2. 建立 request_parameter 內部節點
            com.fasterxml.jackson.databind.node.ObjectNode requestParameter = mapper.createObjectNode();
            requestParameter.put("brand_code", brandCode);
            requestParameter.put("store_code", storeCode);
            requestParameter.set("member_identity", memberIdentity);

            // 3. 建立最外層 jo 節點
            com.fasterxml.jackson.databind.node.ObjectNode jo = mapper.createObjectNode();
            jo.set("request_parameter", requestParameter);
            
            // 時間格式化 (yyyy/MM/dd HH:mm:ss)
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            jo.put("timestamp", sdf.format(new java.util.Date()));

            String joJsonString = mapper.writeValueAsString(jo);
            log.info("Up(availableList)：{}", joJsonString);

            // 4. 將 Payload 轉為 Base64 字串
            String sPlayLod = java.util.Base64.getEncoder().encodeToString(
                    joJsonString.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
            
            // 5. 利用驗證金鑰進行 HmacSHA256 簽章
            String sSignature = CryptUtil.toHmacSHA256(sPlayLod, env.getProperty("SubscriptKey"));
            
            // 6. 封裝至 final Body 外殼 ("sign": "Base64.Signature")
            com.fasterxml.jackson.databind.node.ObjectNode joAll = mapper.createObjectNode();
            joAll.put("sign", sPlayLod + "." + sSignature);

            String finalRequestBody = mapper.writeValueAsString(joAll);
            log.info("Encode(availableList)：{}", finalRequestBody);

            // 7. 透過 FeignClient 動態傳遞 App-Id Header 並發送 POST 請求
            String appId = env.getProperty("SubscriptAppId");
            String response = privilegeServiceClient.executeAvailableList(appId, finalRequestBody);

            log.info("Down(availableList)：{}", response);
            return response;

        } catch (Exception e) {
            log.error("取得可用特權清單中台連線異常: {}", e.getMessage(), e);
            // 拋出 RuntimeException 供 Controller 統一捕捉，輸出乾淨雙格式錯誤訊息
            throw new RuntimeException("可用特權清單通訊異常: " + e.getMessage());
        }
    }   

}
