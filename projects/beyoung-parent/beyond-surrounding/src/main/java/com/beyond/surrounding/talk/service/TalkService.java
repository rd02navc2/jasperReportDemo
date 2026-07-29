package com.beyond.surrounding.talk.service;

import com.beyond.surrounding.talk.client.LineFeignClient;
import com.beyond.surrounding.talk.client.OpenAIFeignClient;
import com.beyond.surrounding.talk.dto.OpenAIRequest;
import com.beyond.surrounding.talk.dto.OpenAIResponse;
import feign.FeignException; // 💡 引入 Feign 異常類別
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TalkService {

    private final OpenAIFeignClient openaiFeignClient;
    private final LineFeignClient lineFeignClient;
    private final String channelAccessToken = "YOUR_CHANNEL_ACCESS_TOKEN";
    private final String targetUserId = "TARGET_USER_ID";

    @Transactional(rollbackFor = Exception.class)
    public String sendAIMessage(Logger log, Environment env, String message) {
        String result = "";
        try {
            OpenAIRequest.Message messageObj = new OpenAIRequest.Message("user", message);
            
            OpenAIRequest requestBody = new OpenAIRequest(
                env.getProperty("api-config.openai.model"),
                Collections.singletonList(messageObj),
                0.7
            );

            URI targetUri = new URI(env.getProperty("api-config.openai.url"));
            String authHeader = "Bearer " + env.getProperty("api-config.openai.access-token");

            OpenAIResponse response = openaiFeignClient.sendAIMessage(targetUri, authHeader, requestBody);
            log.info("OpenAI API Response DTO: {}", response);

            if (response != null) {
                if (response.getError() != null) {
                    result = response.getError().getMessage();
                } else if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                    result = response.getChoices().get(0).getMessage().getContent();
                }
            }

        } catch (FeignException e) {
            // 當外部 API 噴 401/429/500 時，把遠端的 Body 字串直接挖出來！
            String responseBody = e.contentUTF8();
            log.error("Feign 呼叫外部 API 失敗，狀態碼: {}, 回傳內容: {}", e.status(), responseBody);
            
            // 讓錯誤訊息更直觀，這樣會直接被 Controller 封裝進 417 回傳給前端或 Postman
            result = "OpenAI API Error [" + e.status() + "]: " + (responseBody.isEmpty() ? e.getMessage() : responseBody);
            
        } catch (Exception e) {
            log.error("Feign sendAIMessage 發生其他系統異常: ", e);
            result = "System Error: " + e.getMessage();
        }

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendLineMessage(String messageText) {
        try {
            // 1. 準備 Authorization Header (注意 Bearer 後面有空格)
            String authorization = "Bearer " + channelAccessToken;

            // 2. 建立訊息物件 (Message Object)
            Map<String, Object> message = new HashMap<>();
            message.put("type", "text");
            message.put("text", messageText);

            List<Map<String, Object>> messagesList = new ArrayList<>();
            messagesList.add(message);

            // 3. 建立完整的 Request Body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("to", targetUserId);
            requestBody.put("messages", messagesList);

            // 4. 透過 Feign 發送
            lineFeignClient.pushMessage(authorization, requestBody);
            
        } catch (Exception e) {
            // 這裡會對應到你 Controller 捕捉的異常處理
            throw new RuntimeException("LINE 服務通訊異常: " + e.getMessage(), e);
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
	public void sendRevenueImage(Logger log, Environment env, String sFrmeDate, String sFileName, String sReportType) throws Exception {
	        
	        // 1. 從整合後的 YAML 結構中取得萬用的 Channel Access Token
	        String accessToken = env.getProperty("api-config.line.channel-access-token");
	        if (accessToken == null || accessToken.isEmpty()) {
	            throw new Exception("未配置 api-config.line.channel-access-token");
	        }
	        
	        String targetId = "";
	        String reportTitle = "";
	        
	        // 2. 根據新的 YAML 階層結構，動態讀取對應的 target-id 配置
	        if ("A".equals(sReportType)) {
	            targetId = env.getProperty("api-config.line.target-id.pre-revenue");
	            reportTitle = "永和營業報表預派";
	        } else if ("B".equals(sReportType)) {
	            targetId = env.getProperty("api-config.line.target-id.real-revenue");
	            reportTitle = "永和營業報表";
	        } else if ("C".equals(sReportType)) {
	            targetId = env.getProperty("api-config.line.target-id.real-revenue");
	            reportTitle = "影城營業報表";
	        } else if ("D".equals(sReportType)) {
	            targetId = env.getProperty("api-config.line.target-id.sales");
	            reportTitle = "每日打烊業績快報";
	        } else {
	            throw new Exception("Wrong sReportType：" + sReportType);
	        }
	        
	        if (targetId == null || targetId.isEmpty()) {
	            throw new Exception("找不到對應 sReportType 的 LINE 接收端 ID 配置 (api-config.line.target-id)");
	        }
	        
	        String messageText = reportTitle + "\n營業日期：" + sFrmeDate;
	
	        // 3. 處理圖片對外公開網址
	        String baseImageUrl = env.getProperty("Public_Image_Base_Url");
	        if (baseImageUrl == null || baseImageUrl.isEmpty()) {
	            throw new Exception("未配置 Public_Image_Base_Url，無法建立 LINE 圖片網址");
	        }
	        String imageUrl = baseImageUrl + sFileName;
	
	        // 4. 包裝新版 Messaging API 規定的 JSON 格式 (Map 結構)
	        Map<String, Object> requestBody = new HashMap<>();
	        requestBody.put("to", targetId);
	
	        List<Map<String, Object>> messages = new ArrayList<>();
	
	        // (1) 文字訊息物件
	        Map<String, Object> textMessage = new HashMap<>();
	        textMessage.put("type", "text");
	        textMessage.put("text", messageText);
	        messages.add(textMessage);
	
	        // (2) 圖片訊息物件
	        Map<String, Object> imageMessage = new HashMap<>();
	        imageMessage.put("type", "image");
	        imageMessage.put("originalContentUrl", imageUrl);
	        imageMessage.put("previewImageUrl", imageUrl);
	        messages.add(imageMessage);
	
	        requestBody.put("messages", messages);
	
	        // 5. 【核心改變】直接使用 Feign Client 發送請求
	        try {
	            String authorizationHeader = "Bearer " + accessToken;
	            
	            // Feign 會自動將 Map 序列化為 JSON，並傳送過去
	            String responseResult = lineFeignClient.pushMessage(authorizationHeader, requestBody);
	            
	            log.info("Feign 發送 LINE 訊息成功，回應內容: " + responseResult);
	            
	        } catch (feign.FeignException e) {
	            // Feign 若收到非 2xx 的狀態碼，會自動拋出 FeignException
	            log.error("Feign 發送 LINE 訊息失敗，狀態碼: " + e.status() + ", 錯誤內容: " + e.contentUTF8());
	            throw new Exception("LINE API 服務連線或回應異常，狀態碼: " + e.status(), e);
	        }
	    }
	
    
}