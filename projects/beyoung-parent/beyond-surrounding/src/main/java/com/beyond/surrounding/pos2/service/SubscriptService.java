package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.bean.ActionResponseBean;
import com.beyond.surrounding.pos2.client.SubscriptFeignClient;
import com.beyond.surrounding.util.CryptUtil;
import com.beyond.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptService {
	
	private final Environment env;
    private final ObjectMapper objectMapper;
    private final SubscriptFeignClient subscriptClient; // 注入 Feign Client

    @Transactional
    public ActionResponseBean search(String counterNo, String center, String posId, Integer quantity) {
        ActionResponseBean bean = new ActionResponseBean();
        
        try {
            log.info("Feign search 開始 : counterNo -> {}, center -> {}", counterNo, center);

            // 1. 組裝請求 JSON
            JSONObject jo1 = new JSONObject();
            jo1.put("brand_code", counterNo);
            jo1.put("store_code", center);
            jo1.put("source_uuid", posId);
            jo1.put("quantity", quantity);
            
            JSONObject jo = new JSONObject();
            jo.put("request_parameter", jo1);

            log.info("Up(search) 請求內容：{}", jo.toString());

            // 2. 簽章處理 (保持原有機制)
            String playLoad = URLEncoder.encode(jo.toString(), StandardCharsets.UTF_8.name());
            String signature = CryptUtil.toHashHmacSHA256(playLoad, env.getProperty("SubscriptKey"));
            
            JSONObject joAll = new JSONObject();
            joAll.put("sign", playLoad + "." + signature);
            
            // 取得目標網址 (注意路徑改為 subscript.target)
            String subscriptTargetUrl = env.getProperty("subscript.target");
            if (subscriptTargetUrl == null || subscriptTargetUrl.trim().isEmpty()) {
                throw new RuntimeException("環境變數配置錯誤：找不到 'subscript.target'");
            }
            URI targetUri = new URI(subscriptTargetUrl);

            // 🚀 4. 呼叫 Feign（已移除舊的 contentType 參數）
            String rawResult = subscriptClient.searchRemoteSystem(targetUri, joAll.toString());
            log.info("Down(search) Feign 回傳：{}", rawResult);

            // Jackson 解析
            JsonNode rootNode = objectMapper.readTree(rawResult);
            JsonNode rcrmNode = rootNode.path("rcrm");

            String rc = rcrmNode.path("RC").asText();
            if (!"C01".equals(rc)) {
                bean.setCode(rc);
                bean.setMessage(rcrmNode.path("RM").asText());    
                log.error("第三方查詢失敗 : RC -> {}, RM -> {}", rc, rcrmNode.path("RM").asText());
                return bean;
            }
            
            int leftQty = rootNode.path("left_qty").asInt(0); 
            log.info("Feign search 查詢成功數量 : {}", leftQty);
            
            bean.setiAmt(leftQty);
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            return bean;

        } catch (Exception e) {
            log.error("Feign search 呼叫遠端發生異常: ", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}