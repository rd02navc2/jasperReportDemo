package com.beyond.surrounding.ts.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.ts.bean.OOARequestBean;
import com.beyond.surrounding.ts.bean.OOAResponseBean;
import com.beyond.surrounding.ts.entity.TS_OOA_LOG;
import com.beyond.surrounding.ts.service.OneOnAirService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.NetUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/ts/OOA")
@RequiredArgsConstructor
public class OneOnAirController {

	private final OneOnAirService oneOnAirService;
	
	@Value("${OneOnAir_EC_Notify_URL:http://localhost:8095/defaultCallback}") 
    private String ecNotifyUrl;
	
	@PostMapping(value = "/AIRobot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public void AIRobot(@RequestBody String requestBody) {
        try {
            log.info("OneOnAirController 收到 LINE Webhook JSON 請求: {}", requestBody);
            
            // 呼叫重構後的 Service 執行遠端 Feign (OpenAI + LINE API)
            oneOnAirService.processAiRobot(requestBody);
            
        } catch (Exception e) {
            log.error("OneOnAirController 層捕捉到異常: ", e);
            
            // 封裝原本 legacy 架構需要的 417 錯誤 JSON 格式
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 丟出 417 異常狀態
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }

	@PostMapping(value = "/ITRobot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public void ITRobot(@RequestBody String requestBody) {
        try {
            log.info("OneOnAirController 收到 ITRobot 請求: {}", requestBody);
            oneOnAirService.processItRobot(requestBody); // 呼叫處理 ITRobot 邏輯
        } catch (Exception e) {
            log.error("ITRobot 異常: ", e);
            throw createLegacyException(e);
        }
    }
		
    private ResponseStatusException createLegacyException(Exception e) {
        JSONObject errorJson = new JSONObject();
        errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
        errorJson.put("message", e.getMessage());
        return new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
    }
    
    @PostMapping(
            value = "/echo", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"}
        )
    public void echo(@RequestBody String requestBody) {
        try {
            log.info("OneOnAirController 收到 echo 請求: {}", requestBody);
            oneOnAirService.processEchoRobot(requestBody); // 呼叫處理 Echo 邏輯
        } catch (Exception e) {
            log.error("echo 異常: ", e);
            throw createLegacyException(e); // 沿用之前寫好的 417 包裝格式
        }
    }
    
    @PostMapping(
            value = "/getPaymentUrl",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"}
        )
        public OOAResponseBean getPaymentUrl(@RequestBody OOARequestBean requestBody) {
            try {
                log.info("收到 getPaymentUrl 請求: order_no -> {}, payment_type -> {}", 
                        requestBody.getOrder_no(), requestBody.getPayment_type());
                return oneOnAirService.processGetPaymentUrl(requestBody);
            } catch (Exception e) {
                log.error("getPaymentUrl 異常: ", e);
                throw createLegacyException(e);
            }
        }
    
    @PostMapping(
            value = "/refund",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"}
        )
        public ResponseBean refund(@RequestBody OOARequestBean requestBody) {
            try {
                log.info("收到退款請求: order_no -> {}, new_order_no -> {}, refund_amt -> {}", 
                        requestBody.getOrder_no(), requestBody.getNew_order_no(), requestBody.getRefund_amt());
                return oneOnAirService.processRefund(requestBody);
            } catch (Exception e) {
                log.error("refund 異常: ", e);
                throw createLegacyException(e);
            }
        }
    
    @PostMapping(
            value = "/query",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"}
        )
        public OOAResponseBean query(@RequestBody OOARequestBean requestBody) {
            try {
                log.info("收到交易狀態查詢請求: order_no -> {}", requestBody.getOrder_no());
                return oneOnAirService.processQuery(requestBody);
            } catch (Exception e) {
                log.error("query 異常: ", e);
                throw createLegacyException(e);
            }
        }   
    
    @GetMapping(
            value = "/getConfirm",
            produces = {MediaType.TEXT_HTML_VALUE + ";charset=utf-8", MediaType.APPLICATION_XHTML_XML_VALUE + ";charset=utf-8"}
        )
        public String getConfirm(HttpServletRequest request) {
            try {
                String queryString = request.getQueryString();
                log.info("confirm PostBack URL：{}?{}", request.getRequestURL(), queryString);
                
                if (queryString == null || queryString.isEmpty()) {
                    return "<html><body>Query string is empty</body></html>";
                }

                // 1. 解碼 URL
                String decodedQuery = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
                log.info("confirm Query String 解碼後：{}", decodedQuery);
                
                // 2. 利用舊有的 NetUtil.splitQuery 或 Spring 的工具拆解參數
                Map<String, String> paramMap = NetUtil.splitQuery(decodedQuery);
                
                // 優化原本的迴圈：直接用 map.get 獲取，不需跑 for 迴圈
                String orderNo = paramMap.get("merchanttradeno");
                
                // 3. 呼叫 Service 更新 confirm_date
                oneOnAirService.updateConfirm(orderNo);

                // 4. 回傳網頁
                return "<html><body>ok</body></html>";
                
            } catch (Exception e) {
                log.error("getConfirm 異常: ", e);
                // 由於此端點要求回傳 HTML，若噴錯且想維持舊 legacy 417 JSON 錯誤：
                throw createLegacyException(e);
            }
        }
    
    @GetMapping(
            value = "/getNotify",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE
        )
        public ResponseEntity<Void> getNotify(HttpServletRequest request) {
            try {
                String queryString = request.getQueryString();
                log.info("notify PostBack URL：{}?{}", request.getRequestURL(), queryString);
                
                if (queryString == null || queryString.isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                // 1. 解析 Query String 參數
                var queryParams = UriComponentsBuilder.fromUriString("?" + queryString).build().getQueryParams();
                String orderNo = queryParams.getFirst("merchanttradeno");
                String rtnCode = queryParams.getFirst("rtncode");
                String rtnMsg = queryParams.getFirst("rtnmsg");

                // 2. 呼叫 Service 更新資料庫狀態
                oneOnAirService.updateNotify(orderNo, rtnCode, rtnMsg);

                // 3. 動態組裝重新導向到電商前台的 URL (自動處理 UTF-8 編碼)
                String redirectUrl = UriComponentsBuilder.fromUriString(ecNotifyUrl) 
                        .queryParam("order_no", orderNo)
                        .queryParam("ret_code", rtnCode)
                        .queryParam("ret_msg", rtnMsg)
                        .build()
                        .toUriString();
                
                log.info("Redirect 到電商前台網址: {}", redirectUrl);

                // 4. 回傳 HTTP 303 (See Other) 狀態碼與 Location 標頭實現重導向
                return ResponseEntity.status(HttpStatus.SEE_OTHER)
                        .location(URI.create(redirectUrl))
                        .build();
                
            } catch (Exception e) {
                log.error("getNotify 異常: ", e);
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
            }
        }
    
    @PostMapping(
            value = "/getStatus",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"}
        )
    public ResponseEntity<OOAResponseBean> getStatus(@RequestBody OOARequestBean requestBody) {
        try {
            log.info("getStatus EC2Api : order_no -> {}, payment_type -> {}", 
                     requestBody.getOrder_no(), requestBody.getPayment_type());

            OOAResponseBean bean = new OOAResponseBean();
            TS_OOA_LOG entity = oneOnAirService.getStatus(requestBody.getOrder_no(), requestBody.getPayment_type());

            if (entity.getOrder_no() == null) {
                bean.setCode("9999");
                bean.setMessage("查無訂單交易資料");
            } else if (entity.getRefund_date() != null) {
                bean.setCode("0001");
                bean.setMessage("交易退貨");
            } else if (entity.getCreate_date() != null && entity.getRet_code() == null) {
                bean.setCode("0002");
                bean.setMessage("已取得QRCode，尚未完成交易");
            } else if (entity.getConfirm_date() != null && entity.getNotify_date() == null) {
                bean.setCode("0003");
                bean.setMessage("使用者已確認，尚未取得台新通知完成交易");
            } else if ("000".equals(entity.getRet_code()) && entity.getNotify_date() != null) {
                bean.setCode(ErrCodeConst.finished);
                bean.setMessage(ErrCodeConst.finished_message);
            } else {
                bean.setCode(entity.getRet_code());
                bean.setMessage(entity.getRet_msg());
            }

            return ResponseEntity.ok(bean);      
	    } catch (Exception e) {
	    	log.error("查詢狀態失敗", e);
	        throw createLegacyException(e);
	    }
    }
    
}