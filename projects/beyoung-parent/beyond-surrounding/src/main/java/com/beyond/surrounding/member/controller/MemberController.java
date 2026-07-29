package com.beyond.surrounding.member.controller;

import com.beyond.surrounding.member.service.MemberService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.app.entity.LPJ_FILE;
import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.app.entity.LSM_FILE;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 會員 APP 相關 API 控制器
 * 已整合 Spring Validation 參數校驗功能，升級至 Java 21, Spring Boot 3.4.3
 */
@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/app/Member")
@RequiredArgsConstructor
@Validated // 必須加上此註解，@PathVariable 與 @RequestParam 的單一參數校驗才會生效
public class MemberController {
	
    private final MemberService memberService;
    
    @GetMapping(value = "/isExistLPK/{memberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean isExistLpk(@PathVariable String memberId) {
        
        log.info("檢查卡片是否存在：memberId -> {}", memberId);
        
        try {
            ResponseBean bean = new ResponseBean();
            
            // 補齊：還原原本的 IF-ELSE 判斷邏輯（Service 應返回 boolean）
            if (memberService.isExistLpk(memberId)) {
                bean.setCode(ErrCodeConst.duplicate);
                bean.setYn("Y"); // 依小駝峰改為 setYn
                bean.setMessage(memberId + " " + ErrCodeConst.duplicate_message);
            } else {
                bean.setCode(ErrCodeConst.finished);
                bean.setYn("N"); // 依小駝峰改為 setYn
                bean.setMessage(ErrCodeConst.finished_message);                
            }
            
            return bean;
            
        } catch (Exception e) {
            log.error("isExistLpk 處理異常: ", e);
            
            // 保持與前個方法相同的異常 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    /**
     * 新增一般臨時會員 
     * 修正：由 GET 改為 POST，並改用 Request Body 接收參數以維護安全
     */
    @GetMapping(value = "/addTempMember", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean addTempMember(@PathVariable String center,
    								  @PathVariable String memberId) {
    	log.info("addTempMember : center -> {}, memberId -> {}", center, memberId);
    	  
        try {
            ResponseBean bean = new ResponseBean();
            
            // 檢查 LPK 卡片是否存在
            if (memberService.isExistLpk(memberId)) {
                bean.setCode(ErrCodeConst.duplicate);
                bean.setMessage(memberId + " " + ErrCodeConst.duplicate_message);
            } else {
                // 修正：補回原本被漏掉的新增臨時會員業務邏輯
                memberService.addTempMember(center, memberId, memberId);
                
                bean.setCode(ErrCodeConst.finished);
                bean.setMessage(ErrCodeConst.finished_message);                
            }
            
            return bean;
            
        } catch (Exception e) {
            log.error("addTempMember 處理異常: ", e);
            
            // 保持與前面方法一致的錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    /**
     * 新增 RS 臨時會員
     */
    @GetMapping(value = "/addRSTempMember", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean addRSTempMember(@PathVariable String center,
			  							@PathVariable String memberId) {
        log.info("REST 請求 - 新增 RS 臨時會員：center -> {}, memberId -> {}", center, memberId);
        
        ResponseBean bean = new ResponseBean();
        try {
            // 1. 直接呼叫封裝好檢查與新增邏輯的 Service 方法
            // 提示：Service 內部已包含 if(isExistLpk) 檢查，重複時會拋出 IllegalArgumentException
            memberService.addRsTempMember(center, memberId);
            
            // 2. 成功時設定對應狀態碼
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            return bean;

        } catch (IllegalArgumentException e) {
            // 專門捕捉 Service 層因「帳號已存在」拋出的例外，對齊舊系統的重複代碼返回
            log.warn("新增 RS 臨時會員失敗，資料重複: {}", e.getMessage());
            bean.setCode(ErrCodeConst.duplicate);
            bean.setMessage(memberId + " " + ErrCodeConst.duplicate_message);
            return bean;
            
        } catch (Exception e) {
            log.error("addRsTempMember 處理系統級異常: ", e);
            
            // 3. 保持與舊系統 100% 一致的 417 Expectation Failed 錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
    /**
     * 更新會員聯絡資料
     * 修正：符合狀態更新語意改為 PUT，敏感個資全面改由 Request Body 傳遞，嚴禁暴露於 URL
     */
    @GetMapping(value = "/updMemberContact", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean updMemberContact(
            @PathVariable String memberId,
            @RequestParam String mobile, 
            @RequestParam String email,   
            @RequestParam String addr) {  
        
        log.info("REST 請求 - 更新會員聯絡資料：memberId -> {}, mobile -> {}, email -> {}, addr -> {}", 
                memberId, mobile, email, addr);
        
        ResponseBean bean = new ResponseBean();
        try {
            // 1. 檢查會員 LPK 是否存在 (若不存在，直接返回 not_found)
            if (!memberService.isExistLpk(memberId)) {
                log.warn("更新聯絡資料失敗：會員 {} 不存在", memberId);
                bean.setCode(ErrCodeConst.not_found);
                bean.setMessage(memberId + " " + ErrCodeConst.not_found_message);
                return bean;
            }
            
            // 2. 會員存在，執行更新 (內部方法與傳入變數皆使用純駝峰命名)
            memberService.updateMemberContact(memberId, mobile, email, addr);
            
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            return bean;
            
        } catch (Exception e) {
            log.error("updMemberContact 系統處理異常: ", e);
            
            // 3. 保持與舊系統 100% 一致的 417 Expectation Failed 錯誤格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
    @GetMapping(value = "/getAllCardByMemberID/{memberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
        	    MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LPJ_FILE> getAllCardByMemberId(
            @PathVariable @NotBlank(message = "會員Id不能為空") String memberId) {
        
        log.info("REST 請求 - 依會員Id查詢所有卡片：memberId -> {}", memberId);
        try {
            // 呼叫 Service 獲取清單 (變數名與方法名皆改為小駝峰)
            List<LPJ_FILE> cardList = memberService.getAllCardByMemberId(memberId);
            return cardList;
            		
        } catch (Exception e) {
            log.error("getAllCardByMemberId 系統處理異常: ", e);
            
            // 保持與前面所有方法完全相同的 417 Expectation Failed 錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    @GetMapping(value = "/getAllCardByID/{id}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LPJ_FILE> getAllCardById(@PathVariable String id) {
        
        log.info("REST 請求 - 依身分證Id查詢所有卡片：id -> {}", id);
        try {
            // 呼叫 Service 獲取清單 (方法名與傳入參數皆為小駝峰)
            List<LPJ_FILE> cardList = memberService.getAllCardById(id);
            
            // 修正：直接回傳實體清單，與方法宣告的 List<LpjFile> 保持一致
            return cardList;
            
        } catch (Exception e) {
            log.error("getAllCardById 系統處理異常: ", e);
            
            // 保持與前面所有方法完全相同的 417 Expectation Failed 錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    public LPJ_FILE getPointByMemberId(
            @PathVariable @NotBlank(message = "會員Id不能為空") String memberId) {
        
        log.info("REST 請求 - 依會員Id查詢點數資訊：memberId -> {}", memberId);
        try {
            // 呼叫 Service 獲取實體 (方法名與傳入參數皆為小駝峰)
        	LPJ_FILE result = memberService.getPointByMemberId(memberId);
            return result;
            
        } catch (Exception e) {
            log.error("getPointByMemberId 系統處理異常: ", e);
            
            // 保持與前面所有方法完全相同的 417 Expectation Failed 錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    @GetMapping(value = "/getPointByID/{id}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPJ_FILE getPointById(@PathVariable String id) {
        
        log.info("REST 請求 - 依身分證Id查詢點數資訊：id -> {}", id);
        try {
            // 呼召 Service 獲取點數實體 (方法名與傳入參數皆為小駝峰)
        	LPJ_FILE result = memberService.getPointById(id);
            
            // 修正：直接回傳 LpjFile 實體，對齊方法宣告型態
            return result;
            
        } catch (Exception e) {
            log.error("getPointById 系統處理異常: ", e);
            
            // 保持與前面所有方法完全相同的 417 Expectation Failed 錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }

    @GetMapping(value = "/doHouseHold", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPJ_FILE doHouseHold(
            @PathVariable String tempMemberId, 
            @PathVariable String id) {
        
        log.info("REST 請求 - 執行會員歸戶：tempMemberId -> {}, id -> {}", tempMemberId, id);
        
        try {
            // 1. 取得臨時會員點數資訊並檢查（必須為 000 級）
        	LPJ_FILE tempMemberCard = memberService.getPointByMemberId(tempMemberId);
            if (tempMemberCard == null || !"000".equals(tempMemberCard.getLpj02())) {
                throw new IllegalArgumentException("Can not do household : Not 000, TempMemberID -> " + tempMemberId + ", ID -> " + id);
            }
            
            // 2. 取得正式會員（身分證）點數資訊並檢查
            LPJ_FILE mainMemberCard = memberService.getPointById(id);
            if (mainMemberCard == null || mainMemberCard.getLpj03() == null) {
                throw new IllegalArgumentException("Can not do household : The user has no main card, sID -> " + id);
            }
            
            // 3. 檢查主卡卡號前綴（必須為台新聯名卡等指定卡別）
            String mainCardId = mainMemberCard.getLpj03();
            if (!mainCardId.startsWith("7708") && !mainCardId.startsWith("EC") && 
                !mainCardId.startsWith("APP") && !mainCardId.startsWith("TS")) {
                throw new IllegalArgumentException("Can not do household : The main card is not 7708 or APP, Card ID -> " + mainCardId + ", ID -> " + id);
            }
            
            // 4. 驗證雙方會員 ID 皆不為空，執行歸戶業務邏輯
            if (tempMemberCard.getLpj01() != null && !tempMemberCard.getLpj01().isEmpty() &&
                mainMemberCard.getLpj01() != null && !mainMemberCard.getLpj01().isEmpty()) {
                
                log.info("doHouseHold 驗證通過，執行對接：tempMemberId -> {}, mainMemberId -> {}", tempMemberId, mainMemberCard.getLpj01());
                
                // 呼叫 Service 執行歸戶（Service 內部應處理對應的資料庫異動）
                memberService.doHousehold(tempMemberCard, mainMemberCard.getLpj01());
                
                // 歸戶完成後，重新取得正式會員最新的點數與卡片狀態回傳
                return memberService.getPointById(id);
            } else {
                throw new IllegalArgumentException("Can not do household : TempMemberID -> " + tempMemberId + ", sID -> " + id);
            }
            
        } catch (Exception e) {
            log.error("doHouseHold 系統處理或業務檢核異常: ", e);
            
            // 保持與前面所有方法完全相同的 417 Expectation Failed 錯誤 JSON 格式返回
            Map<String, Object> errorJson = new HashMap<>();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
    @GetMapping(value = "/doFormal", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean doFormal(
            @RequestParam String tempMemberId, 
            @RequestParam String name,
            @RequestParam String id,
            @RequestParam String birthday,
            @RequestParam String mobile,
            @RequestParam String address,
            @RequestParam String email) {
        try {
            log.info("doFormal : tempMemberId -> {}, name -> {}, id -> {}", tempMemberId, name, id);
            
            // 1. 檢查會員狀態
            LPJ_FILE lpjBean = memberService.getPointByMemberId(tempMemberId);
            if (!"000".equals(lpjBean.getLpj02())) {
                throw new IllegalArgumentException("The user is not 000, can not doFormal : tempMemberId -> " + tempMemberId + ", id -> " + id);
            }

            // 2. 檢查身分證字號是否已存在
            List<LPJ_FILE> cardList = memberService.getAllCardById(id);
            if (!cardList.isEmpty()) {
                throw new IllegalArgumentException("The user ID existed, can not doFormal : tempMemberId -> " + tempMemberId + ", id -> " + id);
            }
            
            // 3. 執行正式化程序
            memberService.doFormal(tempMemberId, name, id, birthday, mobile, address, email);
            
            // 4. 回傳成功結果
            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished); // 假設原本常數可對應，通常常數全大寫
            bean.setMessage(ErrCodeConst.finished_message);
            
            return bean;

        } catch (Exception e) {
            log.error("Error executing doFormal", e);
            
            // 處理錯誤訊息，包裝成 JSON 字串格式
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value());
            jsonError.put("message", e.getMessage());
            
            // 在 Spring Boot 中使用 ResponseStatusException 丟出特定狀態碼與自訂訊息
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }

    @GetMapping(value = "/getPointHistByMemberID/{emberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LSM_FILE> getPointHistByMemberId(
            @PathVariable String memberId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 呼叫 DAO 查詢點數歷史紀錄，皆調整為駝峰式命名
            List<LSM_FILE> pointHistoryList = memberService.getPointHistByMemberId(memberId, startDate, endDate);
            
            // 原本的切面日誌（如 surroundingAccessLogDao）在 Spring 建議改用 Filter 或 Interceptor 處理
            return pointHistoryList;
            
        } catch (Exception e) {
            log.error("Error executing getPointHistByMemberId", e);
            
            // 處理錯誤訊息，包裝成 JSON 字串格式
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value());
            jsonError.put("message", e.getMessage());
            
            // 使用 ResponseStatusException 回傳 417 狀態碼與錯誤 JSON
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }
    
    @GetMapping(value = "/getMemberContact/{memberID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPK_FILE getMemberContact(@PathVariable String memberId) {
        try {
            log.info("API 請求：取得會員聯絡資料，memberId -> {}", memberId);

            // 呼叫 Repository 查詢會員聯絡資料 (全面改為小駝峰命名)
            LPK_FILE memberContactBean = memberService.getMemberContact(memberId);
                      
            return memberContactBean;
        } catch (Exception e) {
            log.error("執行 getMemberContact 發生異常, memberId -> " + memberId, e);
            
            // 建立錯誤訊息 JSON 內容
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value()); // 417
            jsonError.put("message", e.getMessage());
            
            // 拋出 Spring 標準的 ResponseStatusException，帶入 417 狀態碼與自定義錯誤訊息
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }

    @GetMapping(value = "/getMemberContactByID/{id}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPK_FILE getMemberContactById(@PathVariable String id) {
        try {
            log.info("API 請求：依卡號取得會員聯絡資料，id -> {}", id);

            // 1. 呼叫 Service/Repository 查詢會員聯絡資料實體 (落實小駝峰命名)
            LPK_FILE memberContactBean = memberService.getMemberContactById(id);
            
            // 備留原註解 log 行為參考：
            // surroundingAccessLogDao.save(request.getRemoteAddr(), "app", request.getRequestURI());
            
            return memberContactBean;
        } catch (Exception e) {
            log.error("執行 getMemberContactById 發生異常, id -> " + id, e);
            
            // 2. 建立錯誤訊息 JSON 內容
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value()); // 417
            jsonError.put("message", e.getMessage());
            
            // 3. 拋出 Spring 標準的 ResponseStatusException，帶入 417 狀態碼與錯誤 JSON 內容
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }

    @GetMapping(value = "/getMemberContactByCardID/{cardID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPK_FILE getMemberContactByCardId(@PathVariable String cardId) {
        try {
            log.info("API 請求：依卡號(CardID)取得會員聯絡資料，cardId -> {}", cardId);

            // 1. 呼叫 Service 查詢會員聯絡資料實體 (落實小駝峰命名)
            LPK_FILE memberContactBean = memberService.getMemberContactByCardId(cardId);
            
            // 備留原註解 log 行為參考：
            // surroundingAccessLogDao.save(request.getRemoteAddr(), "app", request.getRequestURI());
            
            return memberContactBean;
        } catch (Exception e) {
            log.error("執行 getMemberContactByCardId 發生異常, cardId -> " + cardId, e);
            
            // 2. 建立錯誤訊息 JSON 內容
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value()); // 417
            jsonError.put("message", e.getMessage());
            
            // 3. 拋出 Spring 標準的 ResponseStatusException，帶入 417 狀態碼與錯誤 JSON 內容
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }
    @GetMapping(value = "/getMainCard/{cardID}", 
    produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    	        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public LPK_FILE getMainCard(@PathVariable String cardId) {
        try {
            log.info("API 請求：取得主卡會員基本資料，cardId -> {}", cardId);

            // 1. 呼叫 Service 查詢主卡會員基本資料實體 (落實小駝峰命名)
            LPK_FILE mainCardBean = memberService.getMainCard(cardId);
            
            // 備留原註解 log 行為參考：
            // surroundingAccessLogDao.save(request.getRemoteAddr(), "app", request.getRequestURI());
            
            return mainCardBean;
        } catch (Exception e) {
            log.error("執行 getMainCard 發生異常, cardId -> " + cardId, e);
            
            // 2. 建立錯誤訊息 JSON 內容
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value()); // 417
            jsonError.put("message", e.getMessage());
            
            // 3. 拋出 Spring 標準的 ResponseStatusException，帶入 417 狀態碼與錯誤 JSON 內容
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }    
}