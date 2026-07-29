package com.beyoung.parking.api;

import com.beyoung.parking.domain.dto.ParkingDTO;

import jakarta.validation.Valid;

import com.beyoung.parking.domain.entity.ParkingLog;
import com.beyoung.parking.infrastructure.LsmFile;
import com.beyoung.parking.application.ParkingService;
import com.beyoung.parking.domain.bean.PointResponseBean; 

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * 點數相關 API 控制器
 * 已依據 parkingRes.java 完整重構升級至 Java 21, Spring Boot 3.4.3, Spring Cloud 2024.0.0
 */
@Slf4j
@RestController
@RequestMapping("/Surrounding/api/parking/Point") // 對應原 @Path("/parking/Point")
@RequiredArgsConstructor
public class ParkingController {
	
    @Value("${erp.ws.url}") 
    private String erpUrl;

    private final ParkingService parkingService;
    
    @PostMapping("/point-history")
    public List<LsmFile> getPointHistByMemberID(@RequestBody Map<String, String> params) {
        String memberId = params.get("memberId");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        
        return parkingService.getPointHistByMemberID(memberId, startDate, endDate);
    }
    
    @GetMapping("/excludeCounter/list")
    public ParkingDTO.Response<List<ParkingDTO.ExcludeCounterResponse>> getExcludeCounterList() {
        log.info("查詢排除專櫃清單");
        try {
            List<ParkingDTO.ExcludeCounterResponse> list = parkingService.getExcludeCounterList();
            return ParkingDTO.Response.success(list);
        } catch (Exception e) {
            log.error("getExcludeCounterList error: ", e);
            
            return ParkingDTO.Response.error("9999", "系統發生錯誤：" + e.getMessage());
        }
    }
 
    
    @PostMapping("/excludeCounter/add")
    public ParkingDTO.Response<String> addExcludeCounter(@Valid @RequestBody ParkingDTO.Request request) {
        log.info("addExcludeCounter : sCounterID -> {}, sCounterName -> {}", 
                request.getCounterId(), request.getCounterName());
        try {
            parkingService.addExcludeCounter(request);
            // 成功時，對應舊有 ErrCodeConst.finished (code "0", message "finished")
            return ParkingDTO.Response.success("新增成功");
        } catch (Exception e) {
            log.error("addExcludeCounter error: ", e);
            // 失敗時，對應舊有 ErrCodeConst.EC_error
            return ParkingDTO.Response.error("9999", "系統發生錯誤：" + e.getMessage());
        }
    } 
    
    @PostMapping("/excludeCounter/remove")
    public ParkingDTO.Response<String> removeExcludeCounter(@Valid @RequestBody ParkingDTO.Request request) {
        log.info("removeExcludeCounter : sCounterID -> {}", request.getCounterId());
        try {
            // 呼叫 Service 進行刪除，傳入前端帶過來的 counterId
            parkingService.removeExcludeCounter(request.getCounterId());
            
            // 成功時，對應舊有 ErrCodeConst.finished
            return ParkingDTO.Response.success("移除成功");
        } catch (Exception e) {
            log.error("removeExcludeCounter error: ", e);
            // 失敗時，回傳統一錯誤格式（可自訂錯誤碼如 "9999" 或舊制的狀態碼）
            return ParkingDTO.Response.error("9999", "系統發生錯誤：" + e.getMessage());
        }
    }
   
    
    
    @PostMapping(
            value = "/usePoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE // 移除非必要的 XML 宣告
    )    
    // 1. 補上 @Valid 確保欄位驗證生效
    public ParkingDTO.Response<Void> usePoint(@Valid @RequestBody ParkingDTO.Request requestBody) {
    	try {
            // 1. 進入點 Log 紀錄 (AOP 以外的標準業務日誌)
            log.info("parking：usePoint : sCenter -> {}, sCounterID -> {}, sCardNO -> {}, iPoint -> {}, sInvoiceB -> {}, sInvoiceE -> {}",
                    requestBody.getCenter(), 
                    requestBody.getCounterId(), 
                    requestBody.getCardNo(), 
                    requestBody.getPoint(), 
                    requestBody.getInvoice(), 
                    requestBody.getInvoice());
            
            // 2. 委派 Service 處理核心業務邏輯
            PointResponseBean serviceResult = parkingService.usePoint(requestBody);
            
            // 3. 將 Service 的結果封裝回前端所需的 DTO 格式
            return ParkingDTO.Response.<Void>builder()
                    .code(serviceResult.getCode())
                    .message(serviceResult.getMessage())
                    .build();
            
        } catch (Exception e) {
            log.error("usePoint 核心業務處理異常: ", e);
            // 保持原本的設計，自動組織 417 (EXPECTATION_FAILED) 回應給前端
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    } 
    
    //dc-
    @PostMapping(
            value = "/getPointHistByMemberID", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
        )
        public ParkingDTO.Response<List<LsmFile>> getPointHistByMemberID(@RequestBody ParkingDTO.Request requestBody) {
            log.info("parking：getPointHistByMemberID : sMemberID -> {}, sStartDate -> {}, sEndDate -> {}", 
                    requestBody.getLoginId(), requestBody.getInvoice(), requestBody.getCounterId()); 
            // 備註：上述 log 欄位映射請依據前端舊封裝欄位調整（例如舊系統常借用 loginId 當作 memberId 傳入）
            
            try {
                // 呼叫剛剛重構過、內含 Feign RPC 遠端卡號查詢的服務層方法
                List<LsmFile> histList = parkingService.getPointHistByMemberID(
                    requestBody.getLoginId(), // 傳入 MemberID
                    requestBody.getInvoice(), // 傳入 開始日期 (格式: YYYY-MM-DD)
                    requestBody.getCounterId() // 傳入 結束日期 (格式: YYYY-MM-DD)
                );
                
                return ParkingDTO.Response.success(histList);
                
            } catch (IllegalArgumentException e) {
                log.error("查詢點數歷程失敗：參數或業務邏輯錯誤", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                log.error("查詢點數歷程失敗：系統異常", e);
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
            }
        }
    
    @PostMapping(
            value = "/addPoint", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
        )
        public ParkingDTO.Response<Void> addPoint(@RequestBody ParkingDTO.Request requestBody) {
            log.info("parking：addPoint : sCenter -> {}, sCounterID -> {}, sCardNO -> {}, iPoint -> {}", 
                    requestBody.getCenter(), requestBody.getCounterId(), requestBody.getCardNo(), requestBody.getPoint());
            try {
                parkingService.addPoint(
                    requestBody.getCenter(), 
                    requestBody.getCounterId(), 
                    requestBody.getCardNo(), 
                    requestBody.getPoint()
                );
                return ParkingDTO.Response.success(null);
            } catch (IllegalArgumentException e) {
                log.error("加點點數操作失敗：參數錯誤", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (Exception e) {
                log.error("加點點數操作失敗：系統異常", e);
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, e.getMessage());
            }
        }
    
}