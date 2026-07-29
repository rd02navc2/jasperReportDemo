package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.pos2.bean.CouponPos2Bean;
import com.beyond.surrounding.pos2.bean.RequestPos2BodyBean;
import com.beyond.surrounding.pos2.client.GiftPos2ServiceFeignClient;
import com.beyond.surrounding.pos2.entity.LpxFile;
import com.beyond.surrounding.pos2.entity.LqeFile;
import com.beyond.surrounding.pos2.entity.TD;
import com.beyond.surrounding.pos2.service.CouponPos2Service;
import com.beyond.surrounding.pos2.service.Pos2DetailService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("pos2CouponPos2Controller")
@RequestMapping("/Surrounding/rest/pos2/Coupon")
@RequiredArgsConstructor
public class CouponPos2Controller {

	private final CouponPos2Service couponPos2Service;
	private final GiftPos2ServiceFeignClient giftWsClient;
	private final Pos2DetailService pos2DetailService;
	private final Environment env;

	@PostMapping(
            value = "/getCouponStatus2", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8" 
            } 
    )
    public List<LqeFile> getCouponStatus2(@RequestBody RequestPos2BodyBean requestBody) {
		
        try {
            // 參數健壯性檢查 (因為變數改名為 couponID，所以 getCouponID() 是完全合法的)
            if (requestBody == null || requestBody.getCouponId() == null || requestBody.getCouponId().isBlank()) {
                throw new IllegalArgumentException("請求參數 ID 為空");
            }
            
            log.info("查詢優惠券狀態2，ID: {}", requestBody.getCouponId());
            List<LqeFile> list = couponPos2Service.getCouponStatus(requestBody.getCouponId());
            
            return list;
        } catch (Exception e) {
            log.error("查詢優惠券狀態失敗: {}", e.getMessage(), e);
            
            // 對齊專案一致的 HTTP 417 例外包裝規格
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }

	@PostMapping(
            value = "/doCouponInvalid2", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8" 
            } 
    )
	public ResponseBean doCouponInvalid2(RequestPos2BodyBean requestBody) {
		
        try {
            // 1. 記錄 Log：將 sCenter 與 sCouponID 欄位全面改為駝峰式變數存取
            log.info("POS2 doCouponInvalid2 : center -> {}, couponId -> {}", requestBody.getCenter(), requestBody.getCouponId());
            
            ResponseBean responseBean = new ResponseBean();
            // 2023-10-21 店長、朱經理取消
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);    
            
            return responseBean;
            
        } catch (Exception e) {
			log.error("進行贈品券失效作業失敗: {}", e.getMessage(), e);
			
			// 3. 丟出符合新專案規範的 417 錯誤 JSON
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
			errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", "進行贈品券失效作業失敗: " + e.getMessage());
			
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
					errorJson.toString()
			);
		}
	}
	
	@GetMapping(value = "/getCouponByInvoiceNO/{center}/{invoiceNo}",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
					     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public List<CouponPos2Bean> getCouponByInvoiceNO(@PathVariable String center, @PathVariable String invoiceNo) {
		
        try {
            log.info("POS2 getCouponByInvoiceNO(New) : center -> {}, invoiceNo -> {}", center, invoiceNo);

            // 1. 取得發票 ERP 明細
            TD entity = pos2DetailService.getTDByInvoiceNO(env, center, invoiceNo);
            if (entity.getPosNo() == null) {
                throw new Exception("getCouponByInvoiceNO 執行 getTDByInvoiceNO 時，ERP 找不到 " + invoiceNo + " 這筆發票資料");
            }
            
            // 2. 使用 org.json 組裝 Feign 請求內容
            JSONObject joAll = new JSONObject();
            JSONObject joTrans = new JSONObject();
            joTrans.put("T0100", "0200");
            joTrans.put("T0200", entity.getVipNo() == null ? "" : entity.getVipNo());
            joTrans.put("T0300", "249000");
            joTrans.put("T1200", GetDateTime.getTime(""));
            joTrans.put("T1300", GetDateTime.getTodayDateW(""));
            joTrans.put("T4100", entity.getPosNo());
            joTrans.put("T4200", center);
            joTrans.put("T5507", invoiceNo);
            joTrans.put("T5509", "0");
            joTrans.put("T5583", entity.getSalDate() == null ? GetDateTime.getTodayDateW("") : entity.getSalDate());
            joAll.put("Trans", joTrans);
            
            log.info("getCouponByInvoiceNO(up)：{}", joAll.toString());

            // 3. 改用 OpenFeign 進行遠端呼叫
            String responseValue = giftWsClient.callGiftApi(joAll.toString());
            log.info("getCouponByInvoiceNO(down)：{}", responseValue);
            
            // 4. 解析遠端結果 (改用符合現代的 Gson/JsonParser 寫法)
            com.google.gson.JsonObject ret = com.google.gson.JsonParser.parseString(responseValue).getAsJsonObject();
            com.google.gson.JsonObject transObj = ret.getAsJsonObject("Trans");
            
            if (!transObj.get("T3900").getAsString().equals("00")) {
                throw new Exception("系統執行錯誤，錯誤代碼：" + transObj.get("T3900").getAsString());
            }
            
            ArrayList<CouponPos2Bean> couponList = new ArrayList<>();
            if (!transObj.has("T5579")) {
                return couponList;
            }

            // 5. 萃取遠端回傳的券號與型態
            StringBuilder sbCouponNo = new StringBuilder();
            HashMap<String, String> hCouponType = new HashMap<>();
            com.google.gson.JsonArray ja = transObj.getAsJsonArray("T5579");
            
            for (int i = 0; i < ja.size(); i++) {
                com.google.gson.JsonObject jo = ja.get(i).getAsJsonObject();
                if (!jo.has("T557907")) continue;

                String sCouponType = jo.get("T557910").getAsString(); 
                com.google.gson.JsonArray jaT557907 = jo.getAsJsonArray("T557907");
                
                for (int j = 0; j < jaT557907.size(); j++) {
                    String sCouponNo = jaT557907.get(j).getAsString().substring(0, 30).trim();
                    sbCouponNo.append(sCouponNo).append(",");
                    hCouponType.put(sCouponNo, sCouponType);
                }
            }
            
            if (sbCouponNo.length() > 0 && sbCouponNo.substring(sbCouponNo.length() - 1).equals(",")) {
                sbCouponNo.delete(sbCouponNo.length() - 1, sbCouponNo.length());
            }
            
            // 6. 撈取本地資料庫狀態
            HashMap<String, String> hUsedStatus = new HashMap<>();
            HashMap<String, Double> hPriceMap = new HashMap<>();
            
            log.info("getCouponByInvoiceNO.getCouponRealStatus : sbCouponNo -> {}", sbCouponNo.toString());
            List<LqeFile> l2 = couponPos2Service.getCouponRealStatus(sbCouponNo.toString());
            
            for (LqeFile data : l2) {
                // 精準對應你大寫實體屬性：getLQE01(), getLQE17(), getTA_LQE09(), getTA_LQE02()
                String isUsedValue = (data.getLQE17().equals("4") ? "Y" : (data.getTA_LQE09() != null && data.getTA_LQE09().equals("Y") ? "Y" : "N"));
                hUsedStatus.put(data.getLQE01(), isUsedValue);
                hPriceMap.put(data.getLQE01(), data.getTA_LQE02());
            }
            
            // 7. 封裝最終結果回傳 Bean (對應駝峰式方法)
            for (int i = 0; i < ja.size(); i++) {
                com.google.gson.JsonObject jo = ja.get(i).getAsJsonObject();
                if (!jo.has("T557907")) continue;

                com.google.gson.JsonArray jaT557907 = jo.getAsJsonArray("T557907");
                for (int j = 0; j < jaT557907.size(); j++) {
                    String sCouponNo = jaT557907.get(j).getAsString().substring(0, 30).trim();
                    
                    CouponPos2Bean bean = new CouponPos2Bean();
                    bean.setCouponNo(sCouponNo); // 配合重構後的駝峰式 setter
                    bean.setPrice(hPriceMap.containsKey(sCouponNo) ? hPriceMap.get(sCouponNo).intValue() : 0);
                    bean.setIsUsed(hUsedStatus.get(sCouponNo));
                    bean.setIsApp(hCouponType.get(sCouponNo).equalsIgnoreCase("V") ? "Y" : "N");
                    
                    couponList.add(bean);
                }
            }
                
            return couponList;

        } catch (Exception e) {
			log.error("進行贈品券失效作業失敗: {}", e.getMessage(), e);
			
			// 3. 丟出符合新專案規範的 417 錯誤 JSON
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
			errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", "進行贈品券失效作業失敗: " + e.getMessage());
			
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
					errorJson.toString()
			);
		}
	}
	
	@GetMapping(value = "/getCouponType",
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public List<LpxFile> getPOSData() {
	    try {
	        log.info("POS2 getCouponType 被呼叫");
	
	        // 1. 呼叫 Service/DAO 撈取券別資料
	        List<LpxFile> list = couponPos2Service.getCouponType(); // 建議改用 Service 層，若仍用 Dao 則維持 couponPos2Dao
	        
	        return list;
	
	    } catch (Exception e) {
	        log.error("取得券步/券型態資料失敗: {}", e.getMessage(), e);
	        
	        // 2. 封裝符合新專案規範的 417 (EXPECTATION_FAILED) 錯誤 JSON
	        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
	        com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
	        errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
	        errorJson.put("message", "取得券步/券型態資料失敗: " + e.getMessage());
	        
	        // 3. 拋出 Spring Boot 專用的狀態異常
	        throw new org.springframework.web.server.ResponseStatusException(
	                org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
	                errorJson.toString()
	        );
	    }
	}
    
}