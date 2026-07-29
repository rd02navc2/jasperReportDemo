package com.beyoung.surrounding.spos.controller;

import com.beyoung.surrounding.bean.ResponseBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.beyoung.surrounding.spos.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("sPosMemberController")
@RequestMapping("/Surrounding/rest/spos/Member")
@RequiredArgsConstructor
public class MemberController {

	private final MembershipService membershipService;

	@GetMapping(value = "/getMemberByCardID",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean getMemberByCardID(
            // 修正：將 @PathParam 改為 Spring 標準的 @RequestParam
            @RequestParam String cardID) {
        
        ResponseBean response = new ResponseBean();
        
        // 防呆：如果前端沒傳 cardID
        if (cardID == null || cardID.trim().isEmpty()) {
            response.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value())); // "400"
            response.setMessage("Query parameter 'cardID' is required.");
            return response;
        }
            
        try {
            log.info("Receiving member query request for cardID: {}", cardID);
            
            // 執行核心舊邏輯改寫後的 Service 查詢
            var memberFile = membershipService.getMemberByCardID(cardID);
            
            // 封裝對齊舊系統的成功返回結構
            response.setCode("finished");
            response.setMessage("finished_message");
            
            if (memberFile != null) {
                // 1. lpj03 欄位：兩邊都是 String，直接賦值
                response.setLpj03(memberFile.getLPK04()); 
                
                // 2. ta_lpj01 欄位：ResponseBean 是 Double，必須將 String 安全轉型
                String lpk18Str = memberFile.getLPK18();
                if (lpk18Str != null && !lpk18Str.trim().isEmpty()) {
                    try {
                        response.setTa_lpj01(Double.parseDouble(lpk18Str.trim()));
                    } catch (NumberFormatException nfe) {
                        log.warn("無法將 LPK18 的字串值 [{}] 轉換為 Double 型態，暫不賦值。", lpk18Str);
                        // 如果 LPK18 存的其實是手機號碼或非數字字串，建議改塞到 response.setMobile() 或 setCard_id()
                        response.setCard_id(lpk18Str); 
                    }
                }
            }
            
            return response;
        
        } catch (Exception e) {
            log.error("查詢會員資料時發生錯誤: {}", e.getMessage(), e);
            
            // 建立標準的錯誤 Response 結構
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "查詢會員資料時發生錯誤: " + e.getMessage());
            
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }

}
			
			
			
			
			