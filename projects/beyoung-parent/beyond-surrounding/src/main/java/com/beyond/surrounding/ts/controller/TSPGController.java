package com.beyond.surrounding.ts.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.ts.bean.NCCCPaymentBean;
import com.beyond.surrounding.ts.bean.TSRequestBean;
import com.beyond.surrounding.ts.bean.TSResponseBean;
import com.beyond.surrounding.ts.client.NcccHppFeignClient;
import com.beyond.surrounding.ts.client.TspgFeignClient;
import com.beyond.surrounding.ts.entity.TS_EC_LOG;
import com.beyond.surrounding.ts.service.NCCCPGService;
import com.beyond.surrounding.ts.service.TSPGService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.beyond.surrounding.util.StringUtil;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/ts/PG")
@RequiredArgsConstructor
public class TSPGController {

	private final Environment env; 
	private final TSPGService tsPGService;
	private final NCCCPGService ncccPGService;
	private final TspgFeignClient tspgClient;
	private final NcccHppFeignClient ncccHppClient;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final Map<String, String> hStatus = new HashMap<>();
    
    static {
        hStatus.put("02", "已授權");
        hStatus.put("03", "已請款");
        hStatus.put("04", "請款已清算");
        hStatus.put("06", "已退貨");
        hStatus.put("08", "退貨已清算");
        hStatus.put("12", "訂單已取消");
        hStatus.put("ZP", "訂單處理中");
        hStatus.put("ZF", "授權失敗");
    }
	
	private ResponseStatusException createLegacyException(Exception e) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
        errorJson.put("message", e.getMessage());
        return new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
    }
	
	@GetMapping(value = "/connectTest",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean connectTest() {
        try {
            log.info("Connect Test");
            
            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            return bean;
		} catch (Exception e) {
			log.error("Connect Test 異常: ", e);
		    throw createLegacyException(e);
		}	
	}
	
	@PostMapping(value = "/auth", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public TSResponseBean auth(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("Auth EC2Api : order_no -> {}, ec_order_no -> {}, pay_type -> {}, amt -> {}, order_desc -> {}, card_no -> {}, install_period -> {}, use_redeem -> {}",
                    requestBody.getOrder_no(), requestBody.getEc_order_no(), requestBody.getPay_type(), requestBody.getAmt(), 
                    requestBody.getOrder_desc(), requestBody.getCard_no(), requestBody.getInstall_period(), requestBody.getUse_redeem());

            // 使用 Jackson (ObjectMapper) 取代舊的 JSONObject
            Map<String, Object> params = new HashMap<>();
            params.put("layout", "1");
            params.put("order_no", requestBody.getOrder_no());
            params.put("amt", requestBody.getAmt() + "00"); 
            params.put("cur", "NTD");
            params.put("order_dsc", requestBody.getOrder_desc());
            params.put("capt_flag", "0");
            params.put("result_flag", "0");
            params.put("post_back_url", env.getProperty("post_back_url"));
            params.put("install_period", String.valueOf(requestBody.getInstall_period()));
            params.put("use_redeem", requestBody.getUse_redeem());
            params.put("city", "");
            params.put("start_date", "");
            params.put("end_date", "");
            params.put("cust_id", "");
            params.put("b_day", "");
            params.put("cell_phone_no", "");
            params.put("home_tel_no", "");
            params.put("office_tel_no", "");

            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", "rest");
            payload.put("ver", "1.0.0");
            payload.put("mid", env.getProperty("mid"));
            payload.put("s_mid", "");
            payload.put("tid", env.getProperty("tid"));
            payload.put("pay_type", requestBody.getPay_type());
            payload.put("tx_type", 1);
            payload.put("params", params);

            String jsonPayload = objectMapper.writeValueAsString(payload);
            log.info("Auth Api2TS : {}", jsonPayload);

            // 透過 Feign 發送發送請求
            String responseValue = tspgClient.auth(jsonPayload);
            log.info("Auth TS2Api : {}", responseValue);

            // 解析回傳內容
            JsonNode rootNode = objectMapper.readTree(responseValue);
            JsonNode paramsNode = rootNode.path("params");
            String retCode = paramsNode.path("ret_code").asText();

            if (!"00".equals(retCode)) {
                String retMsg = paramsNode.has("ret_msg") ? paramsNode.path("ret_msg").asText() : "台新API回覆錯誤";
                throw new Exception(retCode + " " + retMsg);
            }

            // 寫入交易紀錄
            tsPGService.save(requestBody);

            // 組裝回傳模型
            TSResponseBean responseBean = new TSResponseBean();
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);
            responseBean.setsOrderNO(requestBody.getOrder_no());
            responseBean.setsHppUrl(paramsNode.path("hpp_url").asText());
            
            return responseBean;
        } catch (Exception e) {
            log.error("台新授權處理失敗: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
	
	@PostMapping(value = "/authCancel", 
	        consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
	        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public TSResponseBean authCancel(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("AuthCancel EC2Api : order_no -> {}, pay_type -> {}, amt -> {}", 
                    requestBody.getOrder_no(), requestBody.getPay_type(), requestBody.getAmt());

            // 使用 Map 組裝 JSON Payload
            Map<String, Object> params = new HashMap<>();
            params.put("order_no", requestBody.getOrder_no());
            params.put("amt", requestBody.getAmt() + "00"); 

            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", "rest");
            payload.put("ver", "1.0.0");
            payload.put("mid", env.getProperty("mid"));
            payload.put("s_mid", "");
            payload.put("tid", env.getProperty("tid"));
            payload.put("pay_type", requestBody.getPay_type());
            payload.put("tx_type", 8); // 取消授權代碼
            payload.put("params", params);

            String jsonPayload = objectMapper.writeValueAsString(payload);
            log.info("AuthCancel Api2TS : {}", jsonPayload);

            // 透過 Feign 呼叫台新 other.ashx
            String responseValue = tspgClient.other(jsonPayload);
            log.info("AuthCancel TS2Api : {}", responseValue);

            // 解析回傳
            JsonNode rootNode = objectMapper.readTree(responseValue);
            JsonNode paramsNode = rootNode.path("params");
            String retCode = paramsNode.path("ret_code").asText();
            String retMsg = paramsNode.has("ret_msg") ? paramsNode.path("ret_msg").asText() : "台新API回覆錯誤";

            if (!"00".equals(retCode)) {
                // 失敗：更新狀態為台新回傳的錯誤碼，隨後拋出異常
                tsPGService.update(requestBody.getOrder_no(), "8", retCode, retMsg, requestBody.getAmt());
                throw new Exception(retCode + " " + retMsg);
            }
            
            // 成功：更新狀態為 "00"
            tsPGService.update(requestBody.getOrder_no(), "8", "00", "", requestBody.getAmt());

            TSResponseBean responseBean = new TSResponseBean();
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);
            return responseBean;
        } catch (Exception e) {
            log.error("台新取消授權處理失敗: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
	
	@PostMapping(value = "/refund", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public TSResponseBean refund(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("refund EC2Api : order_no -> {}, pay_type -> {}, amt -> {}", 
                    requestBody.getOrder_no(), requestBody.getPay_type(), requestBody.getAmt());

            // 使用 Map 組裝參數
            Map<String, Object> params = new HashMap<>();
            params.put("order_no", requestBody.getOrder_no());
            params.put("amt", requestBody.getAmt() + "00"); 

            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", "rest");
            payload.put("ver", "1.0.0");
            payload.put("mid", env.getProperty("mid"));
            payload.put("s_mid", "");
            payload.put("tid", env.getProperty("tid"));
            payload.put("pay_type", requestBody.getPay_type());
            payload.put("tx_type", 5); // 退款交易代碼
            payload.put("params", params);

            String jsonPayload = objectMapper.writeValueAsString(payload);
            log.info("refund Api2TS : {}", jsonPayload);

            // 透過 Feign 呼叫台新 other.ashx
            String responseValue = tspgClient.other(jsonPayload);
            log.info("refund TS2Api : {}", responseValue);

            // 解析回傳
            JsonNode rootNode = objectMapper.readTree(responseValue);
            JsonNode paramsNode = rootNode.path("params");
            String retCode = paramsNode.path("ret_code").asText();
            String retMsg = paramsNode.has("ret_msg") ? paramsNode.path("ret_msg").asText() : "台新API回覆錯誤";

            if (!"00".equals(retCode)) {
                // 失敗：更新狀態為退款失敗錯誤碼，隨後拋出異常
                tsPGService.update(requestBody.getOrder_no(), "5", retCode, retMsg, requestBody.getAmt());
                throw new Exception(retCode + " " + retMsg);
            }
            
            // 成功：更新狀態為 "00"
            tsPGService.update(requestBody.getOrder_no(), "5", "00", "", requestBody.getAmt());

            TSResponseBean responseBean = new TSResponseBean();
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);
            return responseBean;
        } catch (Exception e) {
        	log.error("台新退款處理失敗: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }

	@PostMapping(value = "/refundCancel", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public TSResponseBean refundCancel(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("refundCancel EC2Api : order_no -> {}, pay_type -> {}, amt -> {}", 
                    requestBody.getOrder_no(), requestBody.getPay_type(), requestBody.getAmt());

            // 1. 使用 Map 組裝請求 parameters
            Map<String, Object> params = new HashMap<>();
            params.put("order_no", requestBody.getOrder_no());
            params.put("amt", requestBody.getAmt() + "00"); 

            // 2. 組裝主要 Payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", "rest");
            payload.put("ver", "1.0.0");
            payload.put("mid", env.getProperty("mid"));
            payload.put("s_mid", "");
            payload.put("tid", env.getProperty("tid"));
            payload.put("pay_type", requestBody.getPay_type());
            payload.put("tx_type", 6); // 取消退款交易代碼
            payload.put("params", params);

            // 3. 轉為 JSON 字串並透過 OpenFeign 呼叫遠端 API
            String jsonPayload = objectMapper.writeValueAsString(payload);
            log.info("refundCancel Api2TS : {}", jsonPayload);

            String responseValue = tspgClient.other(jsonPayload);
            log.info("refundCancel TS2Api : {}", responseValue);

            // 4. 使用 Jackson ObjectMapper 解析回應
            JsonNode rootNode = objectMapper.readTree(responseValue);
            JsonNode paramsNode = rootNode.path("params");
            String retCode = paramsNode.path("ret_code").asText();
            String retMsg = paramsNode.has("ret_msg") ? paramsNode.path("ret_msg").asText() : "台新API回覆錯誤";

            // 5. 檢核回傳狀態碼
            if (!"00".equals(retCode)) {
                // 失敗：更新資料庫狀態，隨後拋出異常
                tsPGService.update(requestBody.getOrder_no(), "6", retCode, retMsg);
                throw new Exception(retCode + " " + retMsg);
            }
            
            // 成功：更新狀態為 "00"
            tsPGService.update(requestBody.getOrder_no(), "6", "00", "");

            // 6. 回傳標準成功回應組件
            TSResponseBean responseBean = new TSResponseBean();
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);
            return responseBean;
        } catch (Exception e) {
            log.error("台新取消退款處理失敗: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
       	
	@PostMapping(value = "/query", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public TSResponseBean query(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("query EC2Api : order_no -> {}", requestBody.getOrder_no());

            // 1. 使用 Map 組裝 params
            Map<String, Object> params = new HashMap<>();
            params.put("order_no", requestBody.getOrder_no());
            params.put("result_flag", "1");

            // 2. 組裝主要 Payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("sender", "rest");
            payload.put("ver", "1.0.0");
            payload.put("mid", env.getProperty("mid"));
            payload.put("s_mid", "");
            payload.put("tid", env.getProperty("tid"));
            payload.put("pay_type", requestBody.getPay_type());
            payload.put("tx_type", 7); // 查詢交易代碼
            payload.put("params", params);

            String jsonPayload = objectMapper.writeValueAsString(payload);
            log.info("query Api2TS : {}", jsonPayload);

            // 3. 透過 Feign 呼叫台新
            String responseValue = tspgClient.other(jsonPayload);
            log.info("query TS2Api : {}", responseValue);

            // 4. 解析回傳資料
            JsonNode rootNode = objectMapper.readTree(responseValue);
            JsonNode paramsNode = rootNode.path("params");
            String retCode = paramsNode.path("ret_code").asText();
            
            if (!"00".equals(retCode)) {
                String retMsg = paramsNode.has("ret_msg") ? paramsNode.path("ret_msg").asText() : "台新API回覆錯誤";
                throw new Exception(retCode + " " + retMsg);
            }

            // 5. 組裝詳細的交易狀態回傳對象
            TSResponseBean responseBean = new TSResponseBean();
            responseBean.setsRetCode(paramsNode.path("ret_code").asText(""));
            responseBean.setsRetMsg(paramsNode.path("ret_msg").asText(""));
            
            String orderStatus = paramsNode.path("order_status").asText();
            responseBean.setsOrderStatus(orderStatus);
            //  hStatus 是您原本用來查找狀態說明的對照 Map
            responseBean.setsOrderStatusDesc(hStatus != null ? hStatus.get(orderStatus) : "");
            
            responseBean.setsAuthType(paramsNode.path("auth_type").asText(""));
            responseBean.setsPurchaseDate(paramsNode.path("purchase_date").asText(""));
            responseBean.setsTxAmt(paramsNode.path("tx_amt").asText(""));
            responseBean.setsSettleAmt(paramsNode.path("settle_amt").asText(""));
            responseBean.setsSettleDate(paramsNode.path("settle_date").asText(""));
            responseBean.setsRefundTransAmt(paramsNode.path("refund_trans_amt").asText(""));
            responseBean.setsRefundDate(paramsNode.path("refund_date").asText(""));
            responseBean.setsRedeemPt(paramsNode.path("redeem_pt").asText(""));
            responseBean.setsRedeemAmt(paramsNode.path("redeem_amt").asText(""));
            responseBean.setsPostRedeemPt(paramsNode.path("post_redeem_pt").asText(""));
            responseBean.setsPostRedeemAmt(paramsNode.path("post_redeem_amt").asText(""));
            responseBean.setsInstallPeriod(paramsNode.path("install_period").asText(""));
            responseBean.setsInstallDownPay(paramsNode.path("install_down_pay").asText(""));
            responseBean.setsInstallPay(paramsNode.path("install_pay").asText(""));
            responseBean.setsInstallDownPayFee(paramsNode.path("install_down_pay_fee").asText(""));
            responseBean.setsInstallPayFee(paramsNode.path("install_pay_fee").asText(""));
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);
            return responseBean;
        } catch (Exception e) {
            log.error("台新交易查詢失敗: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
      
	@PostMapping(value = "/getPostBack", 
	            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE) //  1. 修正為接收表單 URLENCODED 格式
	    public org.springframework.http.ResponseEntity<?> getPostBack(
	            @RequestParam("s_mid") String sMid,
	            @RequestParam("ret_code") String retCode,
	            @RequestParam("tx_type") String txType,
	            @RequestParam("order_no") String orderNo,
	            @RequestParam("ret_msg") String retMsg,
	            @RequestParam(value = "auth_id_resp", required = false) String authIdResp,
	            @RequestParam(required = false) String carrierId2) {
	        
	        try {
	            log.info("Auth TS PostBack：s_mid -> {}, ret_code -> {}, tx_type -> {}, order_no -> {}, ret_msg -> {}, auth_id_resp -> {}, carrierId2 -> {}", 
	                    sMid, retCode, txType, orderNo, retMsg, authIdResp, carrierId2);
	
	            // 舊代碼邏輯寫死更新交易型態為 "1" (授權)
	            tsPGService.update(orderNo, "1", retCode, retMsg);
	
	            // 組裝重導向 URL
	            String ecBackUrl = env.getProperty("ec_back_url");
	            String redirectUrl = ecBackUrl + "?order_no=" + orderNo 
	                    + "&ret_code=" + retCode 
	                    + "&ret_msg=" + java.net.URLEncoder.encode(retMsg, java.nio.charset.StandardCharsets.UTF_8);
	            
	            log.info("【TSPG】PostBack 重導向目標網址: {}", redirectUrl);
	
	            // 回傳 303 See Other 狀態碼進行瀏覽器重新導向
	            return org.springframework.http.ResponseEntity
	                    .status(org.springframework.http.HttpStatus.SEE_OTHER)
	                    .location(java.net.URI.create(redirectUrl))
	                    .build();
	
	        } catch (Exception e) {
	            log.error("台新 PostBack 處理失敗: ", e);
	            
	            // 2. 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式，並直接透過 ResponseEntity 拋出以避免轉型錯誤
	            Map<String, Object> errMap = new HashMap<>();
	            errMap.put("code", HttpStatus.EXPECTATION_FAILED.value());
	            errMap.put("message", e.getMessage());
	            
	            String errJson;
	            try {
	                errJson = objectMapper.writeValueAsString(errMap);
	            } catch (Exception ex) {
	                errJson = "{\"code\":417,\"message\":\"" + e.getMessage() + "\"}";
	            }
	            
	            return org.springframework.http.ResponseEntity
	                    .status(org.springframework.http.HttpStatus.EXPECTATION_FAILED)
	                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
	                    .body(errJson);
	        }
	    }
	
	/**
     * 查詢內部訂單與交易狀態端點
     */
    @PostMapping(value = "/getStatus", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public org.springframework.http.ResponseEntity<?> getStatus(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("getStatus EC2Api : order_no -> {}, card_type -> {}", 
                    requestBody.getOrder_no(), requestBody.getCard_type());
            
            TSResponseBean bean = new TSResponseBean();
            
            // 從服務層撈取 TS_EC_LOG 資料
            TS_EC_LOG entity = tsPGService.getStatus(requestBody.getOrder_no(), requestBody.getCard_type());
            
            // 完全沿用舊系統的交易狀態判斷矩陣
            if (entity == null || entity.getOrder_no() == null) {
                bean.setCode("9999");
                bean.setMessage("查無訂單交易資料");
            } else if (entity.getAuth_cancel_date() != null) {
                bean.setCode("0001");
                bean.setMessage("交易取消");
            } else if (entity.getRefund_date() != null) {
                bean.setCode("0002");
                bean.setMessage("交易退貨");
            } else if (entity.getAuth_date() != null && entity.getRet_code() == null) {
                bean.setCode("0003");
                bean.setMessage("已取得授權，尚未完成交易");
            } else if ("00".equals(entity.getRet_code())) {
                //  對應舊專案的常數設定 (請確保 ErrCodeConst 類別有被正常載入)
                bean.setCode(ErrCodeConst.finished);
                bean.setMessage(ErrCodeConst.finished_message);
            } else {
                bean.setCode(entity.getRet_code());
                bean.setMessage(entity.getRet_msg());
            }

            return org.springframework.http.ResponseEntity.ok(bean);

        } catch (Exception e) {
            log.error("getStatus 處理失敗: ", e);
            // 呼叫先前抽出的共用 417 例外回傳方法，防止 Content-Type 衝突
            return build417Response(e.getMessage());
        }
    }
	
	
    @PostMapping(value = "/ncccauth",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public TSResponseBean ncccauth(@RequestBody TSRequestBean requestBody) {
        try {
            //  修正點：對接 TSRequestBean 實際定義的蛇形 Getter 命名
            log.info("Auth NCCC EC2Api : orderNo -> {}, ecOrderNo -> {}, amt -> {}, orderDesc -> {}, cardNo -> {}, installPeriod -> {}, useRedeem -> {}",
                    requestBody.getOrder_no(), requestBody.getEc_order_no(), requestBody.getAmt(),
                    requestBody.getOrder_desc(), requestBody.getCard_no(), requestBody.getInstall_period(), requestBody.getUse_redeem());

            TSResponseBean bean = new TSResponseBean();
            bean.setCode("00"); // 模擬 ErrCodeConst.finished
            bean.setMessage("SUCCESS");
            bean.setsOrderNO(requestBody.getOrder_no()); //  改回 getOrder_no()

            // 封裝 Feign Form 表單參數
            Map<String, Object> formParams = new HashMap<>();
            formParams.put("MERCHANTID", env.getProperty("nccc_mid"));
            formParams.put("TERMINALID", env.getProperty("nccc_tid"));
            formParams.put("ORDERID", requestBody.getOrder_no()); //  改回 getOrder_no()

            String transMode = "0";
            if (requestBody.getInstall_period() != null && requestBody.getInstall_period() != 0) { //  改回 getInstall_period()
                transMode = "1";
            } else if ("1".equals(requestBody.getUse_redeem())) { //  改回 getUse_redeem()
                transMode = "2";
            }
            formParams.put("TRANSMODE", transMode);
            formParams.put("INSTALLMENT", String.valueOf(requestBody.getInstall_period())); //  改回 getInstall_period()
            formParams.put("TRANSAMT", String.valueOf(requestBody.getAmt())); //  改回 getAmt()
            formParams.put("NotifyURL", env.getProperty("nccc_post_back_url"));

            // OpenFeign 宣告式遠端調用
            Map<String, String> feignResult = ncccHppClient.postTransaction(formParams);
            String responseCode = feignResult.get("RESPONSECODE");

            if ("00".equals(responseCode)) {
                String key = feignResult.get("KEY");
                String retUrl = "https://" + env.getProperty("NCCC_API_URL") + "/merchant/HPPRequest?KEY=" 
                        + URLEncoder.encode(key, StandardCharsets.UTF_8);
                bean.setsHppUrl(retUrl);
                
                // 呼叫 JPA 儲存至 NCCC_EC_LOG
                ncccPGService.save(requestBody);
            } else {
                bean.setCode(responseCode);
                bean.setMessage(feignResult.get("RESPONSEMSG"));
            }

            return bean;
        } catch (Exception e) {
            log.error("NCCC Auth Exception: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    @PostMapping(value = "/ncccauthCancel",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            		produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public TSResponseBean ncccauthCancel(@RequestBody TSRequestBean requestBody) {
        try {
            log.info("AuthCancel NCCC EC2Api : order_no -> {}, pay_type -> {}, amt -> {}",
                    requestBody.getOrder_no(), requestBody.getPay_type(), requestBody.getAmt());

            TSResponseBean bean = new TSResponseBean();
            bean.setCode("00"); 
            bean.setMessage("SUCCESS"); 

            // 準備封裝發送給 NCCC 的 Form 表單取消參數
            Map<String, Object> formParams = new HashMap<>();
            formParams.put("MERCHANTID", env.getProperty("nccc_mid"));
            formParams.put("ORDERID", requestBody.getOrder_no());

            // 透過全新的 NcccHppFeignClient 發送連線
            Map<String, String> feignResult = ncccHppClient.postCancel(formParams);
            
            String responseCode = feignResult.get("RESPONSECODE");
            String responseMsg = feignResult.get("RESPONSEMSG");

            if (responseCode != null) {
                if ("00".equals(responseCode)) {
                    // 更新退款取消狀態（傳入 "8", null, "00", "", amt）
                    ncccPGService.update(requestBody.getOrder_no(), "8", null, "00", "", requestBody.getAmt());
                } else {
                    ncccPGService.update(requestBody.getOrder_no(), "8", null, responseCode, responseMsg, requestBody.getAmt());
                    bean.setCode(responseCode);
                    bean.setMessage(responseMsg);
                }
            } else {
                bean.setCode("2001"); 
                bean.setMessage("postCancel 發生錯誤，未取得回應狀態碼");
            }

            return bean;
        } catch (Exception e) {
            log.error("NCCC AuthCancel Exception: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    @GetMapping(value = "/getNCCCPostBack",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public jakarta.ws.rs.core.Response getNCCCPostBack(@jakarta.ws.rs.QueryParam("KEY") String sKey) {
        try {
            log.info("Auth NCCC PostBack：sKey -> {}", sKey);

            // 1. 封裝查詢條件
            Map<String, Object> formParams = new java.util.HashMap<>();
            formParams.put("KEY", sKey);

            // 2. 換掉舊的 HppApiClient，直接使用您的 NcccHppFeignClient 進行表單回查
            Map<String, String> feignResult = ncccHppClient.postQuery(formParams);
            
            // 3. 將得到的結果全面轉換為駝峰式命名
            String orderId = feignResult.get("ORDERID");
            String approveCode = feignResult.get("APPROVECODE");
            String responseCode = feignResult.get("RESPONSECODE");
            String responseMsg = feignResult.get("RESPONSEMSG");
            
            if (responseMsg == null) {
                responseMsg = "";
            }

            // 4. 呼叫您已重構為 Spring Data JPA Native Query 的 Service
            ncccPGService.update(orderId, "1", approveCode, responseCode, responseMsg);

            // 5. 組裝重新導向的完整 URL
            String redirectUrl = env.getProperty("nccc_ec_back_url") 
                    + "?order_no=" + orderId 
                    + "&sApproveCode=" + approveCode 
                    + "&ret_code=" + responseCode 
                    + "&ret_msg=" + java.net.URLEncoder.encode(responseMsg, java.nio.charset.StandardCharsets.UTF_8);

            log.info("NCCC PostBack Feign 回查成功，即將執行 303 Redirect 至: {}", redirectUrl);
            
            // 6. 維持原本的 Response 機制回傳
            return jakarta.ws.rs.core.Response.seeOther(new java.net.URI(redirectUrl)).build();
        } catch (Exception e) {
            log.error("NCCC getNCCCPostBack Exception: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }   
    
    @PostMapping(value = "/ncccupload",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            		produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean ncccupload(@RequestBody NCCCPaymentBean requestBody) {
        try {
            log.info("Upload NCCC EC2Api Head : send_date -> {}, serial_no -> {}, amt -> {}, total_cnt -> {}",
                    requestBody.getSend_date(), requestBody.getSerial_no(), requestBody.getAmt(), requestBody.getTotal_cnt());
            
            // 1. 產製本地 .dat 檔案
            String paymentDir = env.getProperty("nccc_payment_dir");
            ncccPGService.genDat(requestBody, paymentDir);

            ResponseBean bean = new ResponseBean();
            bean.setCode("00"); 
            bean.setMessage("SUCCESS"); 

            // 2. 尋找剛剛產出的實體檔案
            File datFile = new File(paymentDir + "/" + requestBody.getMid() + ".dat");
            if (!datFile.exists()) {
                throw new java.io.FileNotFoundException("批次媒體檔產製失敗，實體路徑找不到檔案");
            }

            // 3. 免依賴 spring-test！直接透過匿名內部類別實作不佔空間的 MultipartFile
            MultipartFile multipartFile = new MultipartFile() {
                @Override
                public String getName() { return "file"; }

                @Override
                public String getOriginalFilename() { return datFile.getName(); }

                @Override
                public String getContentType() { return "text/plain"; }

                @Override
                public boolean isEmpty() { return datFile.length() == 0; }

                @Override
                public long getSize() { return datFile.length(); }

                @Override
                public byte[] getBytes() throws IOException {
                    return Files.readAllBytes(datFile.toPath());
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return new FileInputStream(datFile);
                }

                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {
                    Files.copy(datFile.toPath(), dest.toPath());
                }
            };

            log.info("【Feign 上傳】開始透過 Feign 將媒體檔 {} 送往 NCCC 收銀台...", datFile.getName());

            // 4. 呼叫 Feign Client 發送傳輸
            Map<String, String> feignResult = ncccHppClient.uploadPaymentDat(
                multipartFile, 
                "Internet", 
                env.getProperty("nccc_mid")
            );

            // 5. 獲取 NCCC 對應的上傳回應訊息
            String uploadResponse = feignResult.get("RESPONSEMSG");
            if (uploadResponse != null) {
                log.info("Upload NCCC NCCC2Api via Feign : {}", uploadResponse);
                bean.setUpload_message(uploadResponse);
            } else {
                bean.setUpload_message("檔案上傳成功 (Feign 通訊完畢)");
            }

            return bean;
        } catch (Exception e) {
            log.error("NCCC ncccupload Exception: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }   
    
    @PostMapping(value = "/ncccdownload",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public NCCCPaymentBean ncccdownload() {
        try {
            log.info("Download NCCC EC2Api");

            NCCCPaymentBean bean = new NCCCPaymentBean();
            bean.setCode("00"); // 模擬 ErrCodeConst.finished
            bean.setMessage("SUCCESS"); // 模擬 ErrCodeConst.finished_message

            // 1.  改用 Feign Client 遠端呼叫 NCCC，直接取得 XML 結果字串
            log.info("【Feign 下載】發送下載請求至 NCCC...");
            String xmlResponse = ncccHppClient.downloadPaymentResponse("Internet", env.getProperty("nccc_mid"));
            
            log.info("NCCC Download Response: {}", xmlResponse);
            if (xmlResponse == null || xmlResponse.trim().isEmpty()) {
                throw new Exception("Run download no response");
            }

            // 2. 解析 XML 文件內容
            Document doc = StringUtil.convertStringToXMLDocument(xmlResponse);
            
            // 提取下載訊息 (MESSAGE)
            NodeList headList = doc.getElementsByTagName("xmlhead");
            if (headList.getLength() > 0) {
                Node headNode = headList.item(0);
                if (headNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element headElement = (Element) headNode;
                    String downloadMessage = headElement.getAttribute("MESSAGE");
                    bean.setDownload_message(downloadMessage);
                }
            }

            // 提取待解析的檔名 (FILENAME)
            String fileName = "";
            NodeList itemList = doc.getElementsByTagName("item");
            if (itemList.getLength() > 0) {
                Node itemNode = itemList.item(0);
                if (itemNode != null && itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    fileName = itemElement.getAttribute("FILENAME");
                }
            }

            log.info("FileName：{}", fileName);

            // 3. 呼叫現代化 Service：讀取並解析已下載至本地目錄的請款結果媒體檔
            if (!fileName.isEmpty()) {
                // 移除了舊的 log 傳參，改用 Service 內部的強大 @Slf4j
                ncccPGService.readRsp(bean, fileName, env.getProperty("nccc_payment_dir"));
            }

            log.info("Download NCCC Api2EC 完成");
            return bean;
        } catch (Exception e) {
            log.error("NCCC ncccdownload Exception: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    
	
	/**
     * 1. 測試環境背景回傳端點 (重導向至 test_ec_back_url)
     */
    @PostMapping(value = "/getPostBackTest", 
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public org.springframework.http.ResponseEntity<?> getPostBackTest(
            @RequestParam("s_mid") String sMid,
            @RequestParam("ret_code") String retCode,
            @RequestParam("tx_type") String txType,
            @RequestParam("order_no") String orderNo,
            @RequestParam("ret_msg") String retMsg,
            @RequestParam(value = "auth_id_resp", required = false) String authIdResp,
            @RequestParam(required = false) String carrierId2) {
        
        try {
            log.info("Auth TS PostBackTest：s_mid -> {}, ret_code -> {}, tx_type -> {}, order_no -> {}, ret_msg -> {}, auth_id_resp -> {}, carrierId2 -> {}", 
                    sMid, retCode, txType, orderNo, retMsg, authIdResp, carrierId2);

            // 舊代碼邏輯固定更新交易型態為 "1"
            tsPGService.update(orderNo, "1", retCode, retMsg);

            // 注意：這裡讀取的是測試環境專屬的 url 變數
            String testEcBackUrl = env.getProperty("test_ec_back_url");
            String redirectUrl = testEcBackUrl + "?order_no=" + orderNo 
                    + "&ret_code=" + retCode 
                    + "&ret_msg=" + java.net.URLEncoder.encode(retMsg, java.nio.charset.StandardCharsets.UTF_8);
            
            log.info("【TSPG】PostBackTest 重導向目標網址: {}", redirectUrl);

            return org.springframework.http.ResponseEntity
                    .status(org.springframework.http.HttpStatus.SEE_OTHER)
                    .location(java.net.URI.create(redirectUrl))
                    .build();

        } catch (Exception e) {
            log.error("台新 PostBackTest 處理失敗: ", e);
            return build417Response(e.getMessage());
        }
    }

    /**
     * 2. 獲取交易結果端點 (接收表單，回傳 ResponseBean 支援 XML/JSON)
     */
    @PostMapping(value = "/getResult", 
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public org.springframework.http.ResponseEntity<?> getResult(
            @RequestParam("s_mid") String sMid,
            @RequestParam("ret_code") String retCode,
            @RequestParam("tx_type") String txType,
            @RequestParam("order_no") String orderNo,
            @RequestParam("ret_msg") String retMsg,
            @RequestParam(value = "auth_id_resp", required = false) String authIdResp,
            @RequestParam(required = false) String carrierId2) {
        
        try {
            log.info("Auth TS Result：s_mid -> {}, ret_code -> {}, tx_type -> {}, order_no -> {}, ret_msg -> {}, auth_id_resp -> {}, carrierId2 -> {}", 
                    sMid, retCode, txType, orderNo, retMsg, authIdResp, carrierId2);

            // 舊代碼邏輯固定更新交易型態為 "1"
            tsPGService.update(orderNo, "1", retCode, retMsg);
            log.info("Auth Result TS2BeyondAPi : {}", orderNo);

            // 封裝成原本的 ResponseBean 物件回傳
            ResponseBean bean = new ResponseBean();
            bean.setCode(retCode);
            bean.setMessage(retMsg);
            
            return org.springframework.http.ResponseEntity.ok(bean);

        } catch (Exception e) {
            log.error("台新 Result 處理失敗: ", e);
            return build417Response(e.getMessage());
        }
    }

    /**
     * 提取共用的 417 錯誤封裝方法，確保強制作為 application/json 回傳
     */
    private org.springframework.http.ResponseEntity<String> build417Response(String errorMessage) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
        errMap.put("message", errorMessage);
        
        String errJson;
        try {
            errJson = objectMapper.writeValueAsString(errMap);
        } catch (Exception ex) {
            errJson = "{\"code\":417,\"message\":\"" + errorMessage + "\"}";
        }
        
        return org.springframework.http.ResponseEntity
                .status(org.springframework.http.HttpStatus.EXPECTATION_FAILED)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(errJson);
    }
	
	
    
    
    
	
}