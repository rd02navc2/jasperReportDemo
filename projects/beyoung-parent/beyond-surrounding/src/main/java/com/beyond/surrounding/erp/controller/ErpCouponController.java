package com.beyond.surrounding.erp.controller;

import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.erp.bean.CouponBean;
import com.beyond.surrounding.erp.bean.RequestCouponBody;
import com.beyond.surrounding.erp.service.ErpCouponService;
import com.beyond.surrounding.util.ERPWebService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.GetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController("ErpCouponController")
@RequestMapping("/Surrounding/rest/erp/Coupon") // 依實際需求調整路徑
public class ErpCouponController {

	private final ERPWebService erpWebService;
	private final ErpCouponService erpCouponService;
	private final Environment env; // 1. 宣告 Environment 物件

    // 2. 透過建構子注入 ERPWebService 與 Environment
    public ErpCouponController(ERPWebService erpWebService, ErpCouponService erpCouponService, Environment env) {
        this.erpWebService = erpWebService;
        this.erpCouponService = erpCouponService;
        this.env = env;
    }
    
    private ResponseStatusException createLegacyException(Exception e) throws JSONException {
        JSONObject errorJson = new JSONObject();
        errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
        errorJson.put("message", e.getMessage());
        return new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
    }
	
	@GetMapping(value = "/connectTest/{couponNO}",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean connectTest(@PathVariable String couponNO) throws JSONException { 
        try {
            log.info("Connect Test 開始，券號: {}", couponNO);
            
            // 2. 補回舊版的日期與 URL 邏輯
            String erpUrl = env.getProperty("ERP_WS_URL");
            String todayDate = GetDateTime.getTodayDateW("/"); 
            String center = "BY001";

            // 3. 呼叫翻新後回傳 JSONObject 的方法
            JSONObject jsonResult = erpWebService.checkCoupon4ConnectTest(erpUrl, todayDate, center, couponNO);
            
            // 4. 修正狀態檢查：改用 JSONObject 的 optString 避免 NullPointerException 與 Type Mismatch
            if (jsonResult == null || !"Y".equals(jsonResult.optString("status"))) {
                String errorMsg = jsonResult != null ? jsonResult.optString("message", "未知錯誤") : "ERP 回傳內容為空";
                throw new Exception("ERP 驗證失敗: " + errorMsg);
            }
            
            // 5. 驗證成功，回傳正確的 Bean
            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            return bean;
	    } catch (Exception e) {
	        log.error("ERP Connect Test 異常: ", e);

	        throw createLegacyException(e);
	    }   
	}
	
	@PostMapping(value = "/checkCoupon", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public CouponBean checkCoupon(@RequestBody RequestCouponBody requestBody) throws JSONException {
        try {
            // 修正點：改用符合 Java 封裝規範的 Getter 方法取值
            log.info("ERP checkCoupon : sDate -> {}, sCenter -> {}", requestBody.getsDate(), requestBody.getsCenter());
            
            CouponBean bean = new CouponBean();
            
            // 修正點：傳參改用 getsDate(), getsCenter(), getlCouponNO()
            JSONObject jsonResult = erpWebService.checkCoupon(
                env.getProperty("ERP_WS_URL"), 
                requestBody.getsDate(), 
                requestBody.getsCenter(), 
                requestBody.getlCouponNO()
            );
            
            log.info("ERP checkCoupon Response：{}", jsonResult.toString());
            
            if (!"0".equals(jsonResult.optString("code"))) {
                bean.setCode(jsonResult.optString("code"));
                bean.setMessage(jsonResult.optString("message"));
                log.error("{}:{}", bean.getCode(), bean.getMessage());
                return bean;
            }
            
            bean.setStatus(jsonResult.optString("status"));
            bean.setCoupon(jsonResult.optString("coupon"));
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            
            return bean;
		
        } catch (Exception e) {
            log.error("ERP checkCoupon 系統異常: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
		
	@PostMapping(value = "/useCoupon", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public CouponBean useCoupon(@RequestBody RequestCouponBody requestBody) throws JSONException {
        try {
            // 修正點 1：使用 SLF4J 佔位符與 Getter 方法優化 Log
            log.info("ERP useCoupon : sDate -> {}, sCenter -> {}, sSaleNO -> {}, sPOSID -> {}, sCounterID -> {}", 
                    requestBody.getsDate(), requestBody.getsCenter(), requestBody.getsSaleNO(), requestBody.getsPOSID(), requestBody.getsCounterID());
            
            CouponBean bean = new CouponBean();
            
            // 修正點 2：呼叫已重構、回傳 JSONObject 的 Service 方法（移除末尾不必要的 log 傳參）
            JSONObject jsonResult = erpWebService.useCoupon(
                env.getProperty("ERP_WS_URL"), 
                requestBody.getsDate(), 
                requestBody.getsCenter(), 
                requestBody.getsSaleNO(), 
                requestBody.getsCounterID(), 
                requestBody.getsPOSID(), 
                requestBody.getlCouponNO()
            );
            
            log.info("ERP useCoupon Response：{}", jsonResult.toString());
            
            // 修正點 3：使用安全的 optString 檢查 ERP 回傳代碼
            if (!"0".equals(jsonResult.optString("code"))) {
                bean.setCode(jsonResult.optString("code"));
                bean.setMessage(jsonResult.optString("message"));
                log.error("{}:{}", bean.getCode(), bean.getMessage());
                return bean;
            }
            
            // 封裝成功代碼
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            
            return bean;
            
        } catch (Exception e) {
            log.error("ERP useCoupon 系統異常: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
	
	@PostMapping(value = "/getChangeCoupon", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public CouponBean getChangeCoupon(@RequestBody RequestCouponBody requestBody) throws JSONException {
        try {
            // 修正點 1：使用 SLF4J 佔位符與 Getter 方法優化日誌拼接
            log.info("ERP getChangeCoupon : sDate -> {}, sCenter -> {}, sType -> {}, sSaleNO -> {}, sCouponNO -> {}, sPOSID -> {}, sCounterID -> {}, iAmt -> {}", 
                    requestBody.getsDate(), requestBody.getsCenter(), requestBody.getsType(), requestBody.getsSaleNO(), 
                    requestBody.getsCouponNO(), requestBody.getsPOSID(), requestBody.getsCounterID(), requestBody.getiAmt());
            
            CouponBean bean = new CouponBean();

            // 修正點 2：呼叫已重構、回傳 JSONObject 的 Service 方法（移除末尾不必要的 log 傳參）
            JSONObject jsonResult = erpWebService.getChangeCoupon(
                env.getProperty("ERP_WS_URL"), 
                requestBody.getsDate(), 
                requestBody.getsCenter(), 
                requestBody.getsType(), 
                requestBody.getsSaleNO(), 
                requestBody.getsCouponNO(), 
                requestBody.getsCounterID(), 
                requestBody.getsPOSID(), 
                requestBody.getiAmt()
            );
            
            log.info("ERP getChangeCoupon Response ({} / {})：{}", 
                    requestBody.getsCenter(), requestBody.getsCounterID(), jsonResult.toString());
            
            // 修正點 3：使用安全的 optString 檢查 ERP 回傳代碼
            if (!"0".equals(jsonResult.optString("code"))) {
                bean.setCode(jsonResult.optString("code"));
                bean.setMessage(jsonResult.optString("message"));
                log.error("{}:{}", bean.getCode(), bean.getMessage());
                return bean;
            }
            
            // 修正點 4：封裝 ERP 特有回傳欄位 (lqe01, lqe20, lqe21 換券核心資料)
            bean.setSaleno(jsonResult.optString("saleno"));
            bean.setLqe01(jsonResult.optString("lqe01"));
            bean.setLqe20(jsonResult.optString("lqe20"));
            bean.setLqe21(jsonResult.optString("lqe21"));
            
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            
            // 修正點 5：移除舊有的 Gson().toJson 依賴，回歸乾淨的物件回傳
            log.info("ERP getChangeCoupon 處理完成，單號: {}", bean.getSaleno());
            return bean;
            
        } catch (Exception e) {
            log.error("ERP getChangeCoupon 系統異常: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
	
	@PostMapping(value = "/getSaleCoupon", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public CouponBean getSaleCoupon(@RequestBody RequestCouponBody requestBody) throws JSONException {
        try {
            // 1. 使用 SLF4J 佔位符與 Getter 方法優化 Log 記錄
            log.info("ERP getSaleCoupon : sDate -> {}, sCenter -> {}, sTradeType -> {}, sClientSystem -> {}, sCardNO -> {}, sSaleNO -> {}, sHeadCode -> {}, iPieces -> {}", 
                    requestBody.getsDate(), requestBody.getsCenter(), requestBody.getsTradeType(), requestBody.getsClientSystem(), 
                    requestBody.getsCardNO(), requestBody.getsSaleNO(), requestBody.getsHeadCode(), requestBody.getiPieces());
            
            // 2. 基本參數邏輯檢核
            if (requestBody.getsClientSystem() == null || requestBody.getsCardNO() == null) {
                throw new IllegalArgumentException("前置系統或會員卡號必須有值");
            }
            
            CouponBean bean = new CouponBean();
            
            // 3. 呼叫已重構、回傳 JSONObject 的 Service 方法（移除末尾不必要的 log 傳參）
            JSONObject jsonResult = erpWebService.getSaleCoupon(
                env.getProperty("ERP_WS_URL"), 
                requestBody.getsDate(), 
                requestBody.getsCenter(), 
                requestBody.getsTradeType(), 
                requestBody.getsSaleNO(), 
                requestBody.getsHeadCode(), 
                requestBody.getiPieces()
            );
            
            log.info("ERP getSaleCoupon Response：{}", jsonResult.toString());
            
            // 4. 檢查 ERP 回傳代碼
            if (!"0".equals(jsonResult.optString("code"))) {
                bean.setCode(jsonResult.optString("code"));
                bean.setMessage(jsonResult.optString("message"));
                log.error("{}:{}", bean.getCode(), bean.getMessage());
                return bean;
            }
            
            // 5. 封裝發券範圍的核心回傳資訊（lqe01_b 起始券號, lqe01_e 結束券號）
            bean.setSaleno(jsonResult.optString("saleno"));
            bean.setLqe01_b(jsonResult.optString("lqe01_b"));
            bean.setLqe01_e(jsonResult.optString("lqe01_e"));
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            
            log.info("ERP getSaleCoupon 處理完成，單號: {}, 券號範圍: {} ~ {}", 
                    bean.getSaleno(), bean.getLqe01_b(), bean.getLqe01_e());

            // 6. 將發券紀錄保存至本地資料庫（全面改用 Getter 方法傳參）
            erpCouponService.save(
                requestBody.getsCenter(), 
                requestBody.getsSaleNO(), 
                requestBody.getsTradeType(), 
                requestBody.getsClientSystem(), 
                requestBody.getsCardNO(), 
                requestBody.getiPieces(), 
                jsonResult.optString("lqe01_b"), 
                jsonResult.optString("lqe01_e")
            );
            
            return bean;
            
        } catch (Exception e) {
            log.error("ERP getSaleCoupon 系統異常: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
		
		
		
}