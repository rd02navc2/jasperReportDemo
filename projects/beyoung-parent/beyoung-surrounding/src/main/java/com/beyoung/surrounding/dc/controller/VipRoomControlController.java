package com.beyoung.surrounding.dc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.dc.service.VipRoomControlService;
import com.beyoung.surrounding.member.service.MemberService;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.beyoung.surrounding.app.entity.LPK_FILE;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/dc/VipRoomControl") // 建議加上統一的根路徑
@RequiredArgsConstructor
public class VipRoomControlController {

	private final VipRoomControlService vipRoomControlService;
    private final MemberService memberService;
    
    @GetMapping(value = "/purchase", 
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean purchase(
            @RequestParam String center,         // 參數改用小駝峰 center
            @RequestParam String loginId,       // 參數改用小駝峰 loginId
            @RequestParam String cardId,         // 參數改用小駝峰 cardId
            @RequestParam int totalQty) throws JSONException { // 參數改用小駝峰 totalQty
        
        try {
            ResponseBean responseBean = new ResponseBean(); // _bean 改為 responseBean
            
            // 1. 呼叫新 Service 取得會員主檔 (變數 _entity 改為 memberEntity)
            LPK_FILE memberEntity = memberService.getMemberByCardID3(cardId);
            
            // 2. 防呆防空檢核
            if (memberEntity == null || memberEntity.getLpk01() == null) {
                responseBean.setCode(ErrCodeConst.vip_room_rs_not_found);
                responseBean.setMessage(ErrCodeConst.vip_room_rs_not_found_message);    
                log.info("VipRoom purchase : cardId -> {} {}", cardId, ErrCodeConst.vip_room_rs_not_found_message);
                return responseBean;
            }
            
            log.info("VipRoom purchase : cardId -> {}, Name -> {}", cardId, memberEntity.getLpk04());
            
            // 3. 呼叫商務邏輯 Service 層執行扣點/扣次購買
            responseBean = vipRoomControlService.purchase(center, loginId, cardId, totalQty, memberEntity);
            
            return responseBean;
            
        } catch (Exception e) {
            log.error("VipRoom purchase Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容 (變數改用小駝峰 errorJson)
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    @GetMapping(value = "/enter/{center}/{cardId}", 
    produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean enter(@PathVariable String center, 
	                          @PathVariable String cardId) throws JSONException {
	    try {
	        ResponseBean bean = new ResponseBean();
	        
	        // 呼叫先前修正好的 memberService 取得會員主檔
	        LPK_FILE entity = memberService.getMemberByCardID3(cardId);
	        
	        if (entity == null || entity.getLpk01() == null) {
	            bean.setCode(ErrCodeConst.vip_room_rs_not_found);
	            bean.setMessage(ErrCodeConst.vip_room_rs_not_found_message);
	            log.info("VipRoom enter : cardId -> {} {}", cardId, ErrCodeConst.vip_room_rs_not_found_message);
	            return bean;
	        }
	
	        log.info("VipRoom enter : cardId -> {}, Name -> {} 進入VIP室", cardId, entity.getLpk04());
	        return vipRoomControlService.enter(center, cardId, entity);
	    } catch (Exception e) {
            log.error("VipRoom enter Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容 (變數改用小駝峰 errorJson)
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    @GetMapping(value = "/exit/{center}/{cardId}", 
    produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean exit(@PathVariable String center, 
	                         @PathVariable String cardId) throws JSONException {
	    try {
	        ResponseBean bean = new ResponseBean();
	        
	        LPK_FILE entity = memberService.getMemberByCardID3(cardId);
	        
	        if (entity == null || entity.getLpk01() == null) {
	            bean.setCode(ErrCodeConst.vip_room_rs_not_found);
	            bean.setMessage(ErrCodeConst.vip_room_rs_not_found_message);
	            log.info("VipRoom exit : cardId -> {} {}", cardId, ErrCodeConst.vip_room_rs_not_found_message);
	            return bean;
	        }
	
	        log.info("VipRoom exit : cardId -> {}, Name -> {} 離開VIP室", cardId, entity.getLpk04());
	        return vipRoomControlService.exit(center, cardId, entity);
	
	    } catch (Exception e) {
            log.error("VipRoom exit Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容 (變數改用小駝峰 errorJson)
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    @GetMapping(value = "/refund", 
    produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
    		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean refund(@RequestParam String center,
	                           @RequestParam String loginId,
	                           @RequestParam String cardId) throws JSONException {
	    try {
	        ResponseBean bean = new ResponseBean();
	        
	        LPK_FILE entity = memberService.getMemberByCardID3(cardId);
	        
	        if (entity == null || entity.getLpk01() == null) {
	            bean.setCode(ErrCodeConst.vip_room_rs_not_found);
	            bean.setMessage(ErrCodeConst.vip_room_rs_not_found_message);
	            log.info("ReadingSpac refund : cardId -> {} {}", cardId, ErrCodeConst.vip_room_rs_not_found_message);
	            return bean;
	        }
	
	        log.info("VipRoom refund : cardId -> {}, Name -> {}", cardId, entity.getLpk04());
	        return vipRoomControlService.refund(center, loginId, cardId, entity);
	
	    } catch (Exception e) {
            log.error("VipRoom refund Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容 (變數改用小駝峰 errorJson)
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
}