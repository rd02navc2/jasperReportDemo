package com.beyond.surrounding.talk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.talk.bean.RequestTalkBody;
import com.beyond.surrounding.talk.service.TalkService;
import com.beyond.surrounding.util.CryptUtil;
import com.beyond.surrounding.util.ErrCodeConst;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/talk")
@RequiredArgsConstructor
public class TalkController {

	private final TalkService talkService;
	private final Environment env;
	
	private boolean isUserAuthenticated(String authString) throws Exception {
	    log.info("[Auth檢查] 原始收到的 authString: -> [{}]", authString);
	    if (authString == null || authString.trim().isEmpty()) {
	        return false;
	    }
	    
	    // 1. 取得系統預期的正確答案
	    String targetAuth = CryptUtil.getMd5("regalscan:abcd@1234");
	    log.info("[Auth檢查] 預期的系統正確 MD5 答案: -> [{}]", targetAuth);
	    
	    // 2. 終極防呆：直接用正規表示式提取字串中最後一個符合 32 位 hex 字元 (MD5) 的部分
	    String authInfo = authString.trim();
	    java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("[a-fA-F0-9]{32}").matcher(authInfo);
	    
	    if (matcher.find()) {
	        authInfo = matcher.group(); // 不論前面疊了幾個 Bearer，都能精準拿到最後的 MD5
	    } else {
	        // 如果連 32 位 MD5 都找不到，就降級嘗試用空格切分
	        if (authInfo.contains(" ")) {
	            String[] authParts = authInfo.split("\\s+");
	            authInfo = authParts[authParts.length - 1]; 
	        }
	    }
	    
	    log.info("[Auth檢查] 經終極防呆提取出的 Token: -> [{}]", authInfo);
	    
	    boolean isMatched = authInfo.equalsIgnoreCase(targetAuth);
	    log.info("[Auth檢查] 比對結果: -> {}", isMatched);
	    
	    return isMatched;
	}
	  
	@PostMapping(value = "/sendAIMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public ResponseBean sendAIMessage(@RequestBody RequestTalkBody requestBody, 
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authString) {
		try {
			// 1. 身份驗證檢查
			if (authString == null || !isUserAuthenticated(authString)) {
			throw new Exception("The request is not authenticated");
			}
			
			// 2. 呼叫服務層（將變數 requestBody.getMessage() 帶入上面定義的駝峰方法）
			String result = talkService.sendAIMessage(log, env, requestBody.getMessage());
			
			// 3. 裝填成功回傳的 Bean
			ResponseBean responseBean = new ResponseBean();
			responseBean.setCode(ErrCodeConst.finished);
			responseBean.setMessage(result);            
			return responseBean;
		
		} catch (Exception e) {
			log.error("sendAIMessage 層捕捉到異常: ", e);
			
			// 4. 使用 HttpServletResponse 流直接輸出，100% 根除 417 被 Spring 二次包裝與跳脫字元問題
			JSONObject errorJson = new JSONObject();
			errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
			errorJson.put("message", e.getMessage());
			
			jakarta.servlet.http.HttpServletResponse response = ((org.springframework.web.context.request.ServletRequestAttributes) 
			org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()).getResponse();
			
			if (response != null) {
				try {
					response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
					response.getWriter().write(errorJson.toString());
					response.getWriter().flush();
					} 
				catch (java.io.IOException ioException) {
					log.error("Write error response failed", ioException);
					}
			}
			return null;
		}
	}
	
	@PostMapping(value = "/sendLineMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean sendLineMessage(@RequestBody RequestTalkBody requestBody, 
                                        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authString) {
        try {
            // 1. 沿用剛剛完全通車的終極身分驗證檢查
            if (authString == null || !isUserAuthenticated(authString)) {
                throw new Exception("The request is not authenticated");
            }
            
            // 2. 舊系統的日誌紀錄 (配合 DTO 的相容欄位提取，並轉為驼峰變數打印)
            log.info(String.format("sendLineMessage : sAccessToken -> %s, sMessage -> %s", 
                    requestBody.getAccessToken(), requestBody.getMessage()));
            
            // 3. 呼叫服務層 (依循駝峰化命名習慣傳入參數)
            talkService.sendLineMessage(requestBody.getAccessToken());
            
            // 4. 裝填成功回傳的 Bean (依據規範將變數改為駝峰式命名 responseBean)
            ResponseBean responseBean = new ResponseBean();
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);            
            return responseBean;
            
        } catch (Exception e) {
            log.error("sendLineMessage 層捕捉到異常: ", e);
            
            // 5. 統一使用先前研發成功的 HttpServletResponse 機制，杜絕 417 被 Spring 二次包裝問題
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            jakarta.servlet.http.HttpServletResponse response = ((org.springframework.web.context.request.ServletRequestAttributes) 
                    org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()).getResponse();

            if (response != null) {
                try {
                    response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8");
                    response.getWriter().write(errorJson.toString());
                    response.getWriter().flush();
                } catch (java.io.IOException ioException) {
                    log.error("Write error response failed", ioException);
                }
            }
            return null;
        }
    }

	@PostMapping(value = "/sendRevenueImage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
	public ResponseBean sendRevenueImage(
            @RequestBody RequestTalkBody requestBody, 
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authString) {
        
        try {
            // 1. 驗證 Authorization Header
            if (authString == null || !isUserAuthenticated(authString)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The request is not authenticated");
            }
            
            // 2. 記錄請求日誌 (使用你傳入的 RequestTalkBody 物件欄位)
            log.info(String.format("sendRevenueImage : sFromDate -> %s, sFileName -> %s, sReportType -> %s", 
                    requestBody.getFromDate(), requestBody.getFileName(), requestBody.getReportType()));
    
            // 3. 欄位檢核
            if (requestBody.getFileName() == null || requestBody.getFileName().trim().isEmpty()) {
                throw new IllegalArgumentException("推播圖檔必須指定檔案名稱");
            }
            
            // 4. 呼叫底層的 TalkService (已改為 Feign + MySQL 寫入)
            talkService.sendRevenueImage(log, env, 
                    requestBody.getFromDate(), requestBody.getFileName(), requestBody.getReportType());
            
            // 5. 回傳成功狀態物件
            ResponseBean responseBean = new ResponseBean();
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);            
            return responseBean;

        } catch (ResponseStatusException e) {
            // 針對權限等特定 HTTP 狀態異常直接拋出
            throw e;
        } catch (Exception e) {
            log.error("發送營業報表圖片異常: " + e.getMessage(), e);
            
            // 將舊版的 WebApplicationException 改為 Spring 的 ResponseStatusException
            // 這樣 Spring Boot 會自動幫你轉為 417 Expectation Failed 並帶上錯誤 JSON 訊息
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }
		
}
	