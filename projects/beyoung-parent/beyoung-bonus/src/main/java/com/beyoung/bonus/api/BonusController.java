package com.beyoung.bonus.api;

import com.beyoung.bonus.domain.dto.BonusDTO;
import com.beyoung.bonus.domain.dto.BonusDTO.Response;
import com.beyoung.bonus.domain.dto.LsmHistoryDTO;
import com.beyoung.bonus.domain.dto.MemberStatsDTO;

import jakarta.validation.Valid;

import com.beyoung.bonus.domain.entity.BonusLog;
import com.beyoung.bonus.infrastructure.LsmFile;
import com.beyoung.bonus.application.BonusService;
import com.beyoung.bonus.domain.bean.PointResponseBean; 

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.beyoung.bonus.util.Constants;

/**
 * 點數相關 API 控制器
 * 已依據 BonusRes.java 完整重構升級至 Java 21, Spring Boot 3.4.3, Spring Cloud 2024.0.0
 */
@Slf4j
@RestController
@RequestMapping("/Surrounding/api/bonus/Point") 
@RequiredArgsConstructor
public class BonusController {
	
    @Value("${erp.ws.url}") 
    private String erpUrl;

    private final BonusService bonusService;
    
    /**
     * 由 Member MS 呼叫，處理會員歸戶後的卡號轉移
     */
    @PostMapping("/lsm/updateCardId")
    public BonusDTO.Response<String> updateLsmCardId(
            @RequestParam("oldCardId") String oldCardId, 
            @RequestParam("newCardId") String newCardId) {
        
        log.info("Bonus MS 接收卡號轉移請求：oldCardId={}, newCardId={}", oldCardId, newCardId);
        
        try {
            // 呼叫 Service 執行 LSM_FILE 的 update 操作
            bonusService.updateLsmCardId(oldCardId, newCardId);
            return BonusDTO.Response.success("卡號轉移成功");
        } catch (Exception e) {
            log.error("Bonus MS 卡號轉移失敗", e);
            return BonusDTO.Response.error("9999", "卡號轉移失敗: " + e.getMessage());
        }
    }
    
    @GetMapping("/stats/{memberId}")
    public ResponseEntity<MemberStatsDTO> getMemberStats(
            @PathVariable String memberId,
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(bonusService.getMemberStats(memberId, start, end));
    }
    
    @PostMapping("/point-history")
    public List<LsmHistoryDTO> getPointHistByMemberID(@RequestBody Map<String, String> params) {
        String memberId = params.get("memberId");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        
        return bonusService.getPointHistByMemberID(memberId, startDate, endDate);
    }
    
    @GetMapping("/excludeCounter/list")
    public BonusDTO.Response<List<BonusDTO.ExcludeCounterResponse>> getExcludeCounterList() {
        log.info("查詢排除專櫃清單");
        try {
            List<BonusDTO.ExcludeCounterResponse> list = bonusService.getExcludeCounterList();
            return BonusDTO.Response.success(list);
        } catch (Exception e) {
            log.error("getExcludeCounterList error: ", e);
            
            return BonusDTO.Response.error("9999", "系統發生錯誤：" + e.getMessage());
        }
    }
 
    
    @PostMapping("/excludeCounter/add")
    public BonusDTO.Response<String> addExcludeCounter(@Valid @RequestBody BonusDTO.Request request) {
        log.info("addExcludeCounter : sCounterID -> {}, sCounterName -> {}", 
                request.getCounterId(), request.getCounterName());
        try {
            bonusService.addExcludeCounter(request);
            return BonusDTO.Response.success("新增成功");
        } catch (Exception e) {
            log.error("addExcludeCounter error: ", e);
            return BonusDTO.Response.error("9999", "系統發生錯誤：" + e.getMessage());
        }
    } 
    
    @PostMapping("/excludeCounter/remove")
    public BonusDTO.Response<String> removeExcludeCounter(@Valid @RequestBody BonusDTO.Request request) {
        log.info("removeExcludeCounter : sCounterID -> {}", request.getCounterId());
        try {
            bonusService.removeExcludeCounter(request.getCounterId());
            
            return BonusDTO.Response.success("移除成功");
        } catch (Exception e) {
            log.error("removeExcludeCounter error: ", e);
            
            return BonusDTO.Response.error("9999", "系統發生錯誤：" + e.getMessage());
        }
    }
   
    
    
    @PostMapping(
            value = "/usePoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE 
    )    
    // @Valid 確保欄位驗證生效
    public BonusDTO.Response<Void> usePoint(@Valid @RequestBody BonusDTO.Request requestBody) {
    	try {
            // 進入點 Log 紀錄 (AOP 以外的標準業務日誌)
            log.info("Bonus：usePoint : sCenter -> {}, sCounterID -> {}, sCardNO -> {}, iPoint -> {}, sInvoiceB -> {}, sInvoiceE -> {}",
                    requestBody.getCenter(), 
                    requestBody.getCounterId(), 
                    requestBody.getCardNo(), 
                    requestBody.getPoint(), 
                    requestBody.getInvoice(), 
                    requestBody.getInvoice());
            
            // 委派 Service 處理核心業務邏輯
            PointResponseBean serviceResult = bonusService.usePoint(requestBody);
            
            // 將 Service 的結果封裝回前端所需的 DTO 格式
            return BonusDTO.Response.<Void>builder()
                    .code(serviceResult.getCode())
                    .message(serviceResult.getMessage())
                    .build();
            
        } catch (Exception e) {
            log.error("usePoint 核心業務處理異常: ", e);
            // 保持原本的設計，自動組織 417 (EXPECTATION_FAILED) 回應給前端
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    } 
    
    
    @PostMapping(
            value = "/getPointHistByMemberID", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
        )
        public Response<List<LsmHistoryDTO>> getPointHistByMemberID(@RequestBody BonusDTO.Request requestBody) {
            log.info("Bonus：getPointHistByMemberID : sMemberID -> {}, sStartDate -> {}, sEndDate -> {}", 
                    requestBody.getLoginId(), requestBody.getInvoice(), requestBody.getCounterId()); 
    
            
            try {
                // 呼叫剛剛重構過、內含 Feign RPC 遠端卡號查詢的服務層方法
                List<LsmHistoryDTO> histList = bonusService.getPointHistByMemberID(
                    requestBody.getLoginId(), // 傳入 MemberID
                    requestBody.getInvoice(), // 傳入 開始日期 (格式: YYYY-MM-DD)
                    requestBody.getCounterId() // 傳入 結束日期 (格式: YYYY-MM-DD)
                );
                
                return BonusDTO.Response.success(histList);
                
            } catch (IllegalArgumentException e) {
                log.error("查詢點數歷程失敗：參數或業務邏輯錯誤", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                log.error("查詢點數歷程失敗：系統異常", e);
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
            }
        }
    
    /*
		{
		  "center": "A101",
		  "counterId": "Wanhua_01",
		  "cardNo": "666888999",
		  "point": 50,
		  "lrq01": "603", 
		  "lrq02": "603"
		}
		
    */
    
    @PostMapping(
            value = "/addPoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
        )
        public BonusDTO.Response<Void> addPoint(@RequestBody @Valid BonusDTO.Request requestBody) {
		    	// 1. 取得當前訂單金額 (若為 null 則視為 0)
		        BigDecimal currentAmount = (requestBody.getAmount() != null) ? requestBody.getAmount() : BigDecimal.ZERO;
		        
		        // 2. 判斷是否符合 VIP 贈點門檻
		        // 邏輯：(有訂單號 且 金額達標) OR (無訂單號但總消費金額達 VIP 門檻)
		        boolean isVipBonus = (requestBody.getOrderNo() != null && !requestBody.getOrderNo().isEmpty() 
		                              && currentAmount.compareTo(Constants.VIP_AMOUNT_THRESHOLD) >= 0)
		                          || (currentAmount.compareTo(Constants.VIP_AMOUNT_THRESHOLD) >= 0);
		
		        // 3. 執行邏輯
		        if (isVipBonus) {
		            log.info("Bonus：偵測到 VIP 贈點條件，卡號: {}, 金額: {}", requestBody.getCardNo(), currentAmount);
		            
		            String accessId = (requestBody.getOrderNo() != null && !requestBody.getOrderNo().isEmpty()) 
		                            ? "VIP_GIFT_" + requestBody.getOrderNo() 
		                            : "POS_VIP_" + System.currentTimeMillis();
		                              
		            bonusService.addVipGiftPoint(
		                requestBody.getCenter(), 
		                requestBody.getCounterId(), 
		                requestBody.getCardNo(), 
		                requestBody.getPoint(), 
		                accessId
		            );
		            return BonusDTO.Response.success(null);
		            
		        } else {
            
	            // 防呆向下相容機制：如果舊前端沒傳 lrq01 或 lrq02，預設自動帶入原本的補點政策
	            String finalLrq01 = (requestBody.getLrq01() != null && !requestBody.getLrq01().isEmpty()) 
	                                ? requestBody.getLrq01() : "603"; // 假設原規則代碼
	            String finalLrq02 = (requestBody.getLrq02() != null && !requestBody.getLrq02().isEmpty()) 
	                                ? requestBody.getLrq02() : "603"; // 舊碼中固定查的 "603"
	
	            log.info("Bonus：addPoint : sCenter -> {}, sCounterID -> {}, sCardNO -> {}, iPoint -> {}, 專案活動 -> [{}-{}]", 
	                    requestBody.getCenter(), requestBody.getCounterId(), requestBody.getCardNo(), requestBody.getPoint(), 
	                    finalLrq01, finalLrq02);
	                    
	            try {
	                bonusService.addPoint(
	                    requestBody.getCenter(), 
	                    requestBody.getCounterId(), 
	                    requestBody.getCardNo(), 
	                    finalLrq01,      // 傳入動態規則
	                    finalLrq02,      // 傳入動態代號
	                    requestBody.getPoint(),
	                    requestBody.getOrderNo(),
	                    requestBody.getAmount()
	                );
	                return BonusDTO.Response.success(null);
	                
	            } catch (IllegalArgumentException e) {
	                // 專櫃黑名單、活動過期、或是點數超限，都會精準回傳 400 錯誤與明確原因給前台 POS
	                log.error("加點點數操作失敗：業務政策或參數錯誤", e);
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
	            } catch (IllegalStateException e) {
	                // 時效檢核未通過 (如專案未生效或已過期)
	                log.error("加點點數操作失敗：活動時效不符政策", e);
	                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
	            } catch (Exception e) {
	                log.error("加點點數操作失敗：系統嚴重異常（事務已全數回滾）", e);
	                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
	            }
	        }
	      
        }      	
}