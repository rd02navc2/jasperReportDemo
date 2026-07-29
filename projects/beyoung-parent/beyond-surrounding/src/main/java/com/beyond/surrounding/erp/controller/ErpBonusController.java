package com.beyond.surrounding.erp.controller;

import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.erp.bean.CouponBean;
import com.beyond.surrounding.erp.bean.RequestBonusBody;
import com.beyond.surrounding.erp.entity.TC_LRJ_FILE;
import com.beyond.surrounding.erp.service.ErpBonusService;
import com.beyond.surrounding.util.ERPWebService;
import com.beyond.surrounding.util.ErrCodeConst;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController("ErpBonusController")
@RequestMapping("/Surrounding/rest/erp/Bonus") // 依實際需求調整路徑
public class ErpBonusController {

	private final ERPWebService erpWebService;
	private final ErpBonusService erpBonusService;
	private final Environment env; // 1. 宣告 Environment 物件

    // 2. 透過建構子注入 ERPWebService 與 Environment
    public ErpBonusController(ERPWebService erpWebService, ErpBonusService erpBonusService, Environment env) {
        this.erpWebService = erpWebService;
        this.erpBonusService = erpBonusService;
        this.env = env;
    }
    
    @PostMapping(value = "/usePoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean usePoint(@RequestBody RequestBonusBody requestBody) throws JSONException {
        try {
            // 1. 紀錄請求 Log，全面改為 Getter 取值
            log.info("ERP usePoint : sCenter -> {}, sCounterID -> {}, sCardNO -> {}, iPoint -> {}, sInvoiceB -> {}, sInvoiceE -> {}, sPosID -> {}, sSerialNO -> {}", 
                    requestBody.getSCenter(), requestBody.getSCounterID(), requestBody.getSCardNO(), requestBody.getSPosID(), 
                    requestBody.getSInvoiceB(), requestBody.getSInvoiceE(), requestBody.getSPosID(), requestBody.getSSerialNO());
            
            ResponseBean bean = new ResponseBean();

            if (requestBody.getIPoint() == null || requestBody.getSCardNO() == null) {
                throw new IllegalArgumentException("會員卡號 (sCardNO) 與 點數 (iPoint) 不可為空");
            }
            
            // 2. 呼叫 ERP WebService (注意：依原業務邏輯，點數扣減需代入負值)
            JSONObject jsonResult = erpWebService.useMemberPoint(
                    env.getProperty("ERP_WS_URL"), 
                    requestBody.getSCenter(), 
                    requestBody.getSCounterID(), 
                    requestBody.getSCardNO(), 
                    requestBody.getIPoint(), 
                    requestBody.getSInvoiceB(), 
                    requestBody.getSInvoiceE(), 
                    requestBody.getSPosID(), 
                    requestBody.getSSerialNO()
                );
            
            log.info("ERP usePoint Response：{}", jsonResult.toString());
            
            // 3. 檢查 ERP 回傳結果
            if (!"0".equals(jsonResult.optString("code"))) {
                bean.setCode(ErrCodeConst.pos_rs_erp_ws);
                bean.setMessage(ErrCodeConst.pos_rs_erp_ws_message);
                log.error("卡號：{}，{}:{}", requestBody.getSCardNO(), jsonResult.optString("code"), jsonResult.optString("message"));
                return bean;
            }
            
            // 4. 呼叫本地點數日誌 Service 進行持久化儲存
            erpBonusService.saveLog(
                    requestBody.getSCenter(), 
                    requestBody.getSCounterID(), 
                    requestBody.getSUserID(), 
                    requestBody.getSUserName(), 
                    requestBody.getSCardNO(), 
                    requestBody.getIPoint(), 
                    requestBody.getSLoginID()
                );
            
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            return bean;
            
        } catch (Exception e) {
            log.error("ERP usePoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	

    @PostMapping(value = "/processPoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean processPoint(@RequestBody RequestBonusBody requestBody) throws JSONException {
        try {
            // 1. 防呆參數檢核（防範 NPE）
            if (requestBody.getIPoint() == null || requestBody.getIAmt() == null) {
                throw new IllegalArgumentException("異動點数(iPoint)與金額(iAmt)不可為空");
            }

            // 2. 輸出請求日誌 (使用剛才修正的 Jackson 屬性對應 Getter)
            log.info("ERP processPoint : sCenter -> {}, sCounterID -> {}, sCardNO -> {}, sCardType -> {}, iAmt -> {}, iPoint -> {}, sInvoiceB -> {}, sInvoiceE -> {}, sPosID -> {}, sSerialNO -> {}", 
                    requestBody.getSCenter(), requestBody.getSCounterID(), requestBody.getSCardNO(), requestBody.getSCardType(), 
                    requestBody.getIAmt(), requestBody.getIPoint(), requestBody.getSInvoiceB(), requestBody.getSInvoiceE(), 
                    requestBody.getSPosID(), requestBody.getSSerialNO());

            ResponseBean bean = new ResponseBean();
            
            // 3. 獲取點數核銷規則（後續可將 redeemDao 升級為 Spring Data JPA Repository）
            TC_LRJ_FILE ruleEntity = erpBonusService.getRule(requestBody.getSCenter(), requestBody.getSCardType());
            if (ruleEntity == null) {
                throw new IllegalStateException("無法取得該商場與卡別對應的點數核銷規則(TC_LRJ_FILE)");
            }
            
            // 4. 呼叫 ERP 點數計算服務 (點數同樣依原業務規則帶入負值)
            JSONObject jsonResult = erpWebService.processPoint(
                env.getProperty("ERP_WS_URL"), 
                requestBody.getSCenter(), 
                requestBody.getSCounterID(), 
                requestBody.getSCardNO(), 
                requestBody.getIAmt(), 
                -requestBody.getIPoint(), 
                ruleEntity.getTC_LRJ01(), 
                requestBody.getSInvoiceB(), 
                requestBody.getSInvoiceE(), 
                requestBody.getSPosID(), 
                requestBody.getSSerialNO()
            );
            
            log.info("ERP processPoint Response ({} / {} / {})：{}", 
                    requestBody.getSCenter(), requestBody.getSCounterID(), requestBody.getSCardNO(), jsonResult.toString());
            
            // 5. 驗證 ERP 狀態碼
            if (!"0".equals(jsonResult.optString("code"))) {
                bean.setCode(ErrCodeConst.pos_rs_erp_ws);
                bean.setMessage(ErrCodeConst.pos_rs_erp_ws_message);
                log.error("卡號：{}，{}:{}", requestBody.getSCardNO(), jsonResult.optString("code"), jsonResult.optString("message"));
                return bean;
            }
            
            // 6. 條件式寫入點數異動日誌 (特定櫃位來源才記錄)
            String counterId = requestBody.getSCounterID();
            if ("GIFTCENTER".equalsIgnoreCase(counterId) || "EC".equalsIgnoreCase(counterId) || "APP".equalsIgnoreCase(counterId)) {
                erpBonusService.saveLog(
                    requestBody.getSCenter(), 
                    requestBody.getSCounterID(), 
                    requestBody.getSUserID(), 
                    requestBody.getSUserName(), 
                    requestBody.getSCardNO(), 
                    requestBody.getIPoint(), 
                    requestBody.getSLoginID()
                );
            }
            
            // 7. 安全解析 ERP 回傳數據並組裝 ResponseBean
            bean.setLpj03(jsonResult.optString("lpj03"));
            
            // 使用 optDouble(key, 0.0) 自動將空字串、缺失欄位安全轉換成 0.0，省去繁瑣的判斷式
            bean.setTa_lpj01(jsonResult.optDouble("ta_lpj01", 0.0));
            bean.setTa_lpj02(jsonResult.optDouble("ta_lpj02", 0.0));
            bean.setTa_lpj03(jsonResult.optDouble("ta_lpj03", 0.0));
            bean.setIns_Integral(jsonResult.optDouble("Ins_Integral", 0.0));
            
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);                
            return bean;
            
        } catch (Exception e) {
            log.error("ERP processPoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
    
    @PostMapping(
            value = "/exchangeCoupon",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
        )
    public CouponBean exchangeCoupon(@RequestBody RequestBonusBody requestBody) throws JSONException {
        try {
            log.info("ERP exchangeCoupon : sDate -> {}, sCenter -> {}, sUserID -> {}, sCaseNO -> {}, sCouponNO -> {}, sCaseItem -> {}, iCount -> {}, iPoint -> {}",
                    requestBody.getSDate(), requestBody.getSCenter(), requestBody.getSUserID(), 
                    requestBody.getSCaseNO(), requestBody.getSCouponNO(), requestBody.getSCaseItem(), 
                    requestBody.getIQty(), requestBody.getIPoint());
            
            // 呼叫 Service 處理核心邏輯 (包含 WebService 呼叫與資料庫 Insert)
            return erpBonusService.processCouponExchange(requestBody);

        } catch (Exception e) {
            log.error("ERP processPoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 透過 Spring 容器取得當前請求的 HttpServletResponse
            HttpServletResponse response = ((org.springframework.web.context.request.ServletRequestAttributes) 
                    org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()).getResponse();
            
            if (response != null) {
                try {
                    response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
                    response.getWriter().write(errorJson.toString());
                    response.getWriter().flush();
                } catch (IOException ioException) {
                    log.error("Write error response failed", ioException);
                }
            }
            
            // 因為方法簽章必須回傳物件或拋出異常，這裡回傳 null（因 response 已 flush，Spring 預設不會再處理回傳值）
            return null;
        }
    }

}