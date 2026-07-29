package com.beyoung.surrounding.coupon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyoung.surrounding.coupon.bean.RequestCouponBody;
import com.beyoung.surrounding.coupon.service.CouponService;
import com.beyoung.surrounding.app.entity.LQE_FILE;
import com.beyoung.surrounding.app.entity.TC_PSC_FILE;
import com.beyoung.surrounding.app.entity.LPX_FILE;
import java.util.List;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/app/Coupon")
@RequiredArgsConstructor
public class CouponController {
	
	private final CouponService couponService;

	@GetMapping(value = "/getCouponStatus",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LQE_FILE> getCouponStatus(@RequestParam String couponID) {
        try {
            // 參數健壯性檢查
            if (couponID == null || couponID.isBlank()) {
                throw new IllegalArgumentException("優惠券 ID 參數缺失");
            }
            
            log.info("查詢優惠券狀態，ID: {}", couponID);
            
            // 修正 2：呼叫 Service 取得結果後，直接回傳對齊方法簽章的 List<LqeFile>
            List<LQE_FILE> list = couponService.getCouponStatus(couponID);
            return list;
            
        } catch (Exception e) {
            log.error("查詢優惠券狀態失敗: {}", e.getMessage(), e);
            
            // 修正 3：對齊專案一致的 HTTP 417 例外包裝規格
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
            value = "/getCouponStatus2", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8" 
            } 
    )
    public List<LQE_FILE> getCouponStatus2(@RequestBody RequestCouponBody requestBody) {
        try {
            // 參數健壯性檢查 (因為變數改名為 couponID，所以 getCouponID() 是完全合法的)
            if (requestBody == null || requestBody.getCouponID() == null || requestBody.getCouponID().isBlank()) {
                throw new IllegalArgumentException("請求參數 ID 為空");
            }
            
            log.info("查詢優惠券狀態2，ID: {}", requestBody.getCouponID());
            List<LQE_FILE> list = couponService.getCouponStatus(requestBody.getCouponID());
            
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

    // 2. 轉換為 GET 請求，處理路徑與參數
	@GetMapping(
            value = "/getCouponHistByMemberID/{sMemberID}", // 修正 1：路徑變數名稱維持舊系統的 {sMemberID}
            produces = { 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" 
            }
    )
    public List<TC_PSC_FILE> getCouponHistByMemberID(
            @PathVariable String memberID,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // 修正 3：不可 return DTO，改為拋出異常讓底下的 catch 區塊統一包裝回傳
            if (startDate == null || startDate.isBlank() || endDate == null || endDate.isBlank()) {
                throw new IllegalArgumentException("日期參數(startDate, endDate)為必填");
            }

            log.info("查詢會員 {} 優惠券歷史，區間: {} 至 {}", memberID, startDate, endDate);
            
            List<TC_PSC_FILE> list = couponService.getCouponHistByMemberID(memberID, startDate, endDate);
            return list;
            
        } catch (Exception e) {
            log.error("查詢優惠券歷史失敗: {}", e.getMessage(), e);
            
            // 建立 Jackson 的 ObjectNode 來代替舊專案的 JSONObject json
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode jsonNode = mapper.createObjectNode();
            
            // 對應原本的 EXPECTATION_FAILED (HTTP 417) 狀態碼與錯誤訊息
            jsonNode.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            jsonNode.put("message", e.getMessage());
            
            // 拋出 Spring Boot 的狀態異常，並帶入 JSON 字串
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    jsonNode.toString()
            );
        }
    } 

    // 3. 簡單的 GET 列表查詢
    @GetMapping(value = "/getCouponType",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<LPX_FILE> getCouponType() {
        try {
            List<LPX_FILE> list = couponService.getCouponType();
            return list;
        } catch (Exception e) {
            log.error("查詢優惠券狀態失敗: {}", e.getMessage(), e);
            
            // 修正 3：對齊專案一致的 HTTP 417 例外包裝規格
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
	

}