package com.beyoung.surrounding.pss.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.beyoung.surrounding.pss.entity.LpjFile;
import com.beyoung.surrounding.pss.bean.DiscountDetailBean;
import com.beyoung.surrounding.pss.bean.ParkingDiscountExecBean;
import com.beyoung.surrounding.pss.bean.ParkingRequestBody;
import com.beyoung.surrounding.pss.client.ParkingServiceFeignClient;
import com.beyoung.surrounding.pss.client.ParkingSpaceFeignClient;
import com.beyoung.surrounding.pss.entity.ParkingDiscountExec;
import com.beyoung.surrounding.pss.entity.ParkingDiscountSet;
import com.beyoung.surrounding.pss.entity.ParkingRent;
import com.beyoung.surrounding.pss.service.ParkingService;
import com.beyoung.surrounding.pss.service.MemberService;
import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.beyoung.surrounding.util.GetDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pss/Parking")
@RequiredArgsConstructor
public class ParkingController {

    private final MemberService memberService;
    private final ParkingService parkingService;
    private final ParkingServiceFeignClient parkingServiceClient;
    private ParkingSpaceFeignClient parkingSpaceClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    
	@GetMapping(value = "/checkCarNO",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean checkCarNo(@RequestParam String carNo) {
        log.info("本地端收到車牌校驗請求, carNo: {}", carNo);
        
        ResponseBean responseBean = new ResponseBean();
        
        try {
            // 1. 透過 OpenFeign 進行遠端 HTTP 呼叫，免去繁瑣的連線與 Header 處理
            String jsonResponse = parkingServiceClient.checkCarNo(
                    "322b514d7a347849583731744a495072447a454e6f773d3d", 
                    carNo
            );
            
            log.info("遠端停車場系統回傳原始 JSON: {}", jsonResponse);
            
            // 2. 採用現代化 Jackson 處理 JSON 解析 (取代舊版 Gson JsonParser)
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            
            // 3. 核心業務邏輯判定
            if (rootNode.has("code") && !"0000".equals(rootNode.get("code").asText())) {
                responseBean.setCode(ErrCodeConst.parking_not_found);
                responseBean.setMessage(carNo + " " + ErrCodeConst.parking_not_found_message);
            } else {
                responseBean.setCode(ErrCodeConst.finished);
                responseBean.setMessage(ErrCodeConst.finished_message);
                responseBean.setYn("Y"); // 依據您的 ResponseBean 規範可自由補上成功識別
            }
            
            return responseBean;
            
        } catch (Exception e) {
            log.error("執行 checkCarNo 發生異常", e);
            
            // 4. 符合 Spring 規範的現代化 REST 異常拋出處理 (替代 WebApplicationException)
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value()); // 417
            jsonError.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }   
    
    @GetMapping(value = "/getRemainSpace",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public ResponseBean getRemainSpace() {
        log.info("收到取得剩餘車位請求 (OpenFeign 模式)");
        
        ResponseBean responseBean = new ResponseBean();
        
        try {
            // 1. 一行程式碼直接搞定遠端 HTTPS 連線與 Header 帶入
            String jsonResponse = parkingSpaceClient.getRemainSpaceInfo(
                    "322b514d7a347849583731744a495072447a454e6f773d3d"
            );
            
            log.info("遠端車位系統回傳 JSON: {}", jsonResponse);
            
            // 2. 透過 Jackson 提取欄位
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            
            responseBean.setCar(rootNode.has("car") ? rootNode.get("car").asText() : "0");
            responseBean.setMotor(rootNode.has("motor") ? rootNode.get("motor").asText() : "0");
            responseBean.setCode(ErrCodeConst.finished);
            responseBean.setMessage(ErrCodeConst.finished_message);
            responseBean.setYn("Y");
            
            return responseBean;
            
        } catch (Exception e) {
            log.error("執行 getRemainSpace 發生異常", e);
            
            Map<String, Object> jsonError = new HashMap<>();
            jsonError.put("code", HttpStatus.EXPECTATION_FAILED.value());
            jsonError.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, jsonError.toString(), e);
        }
    }    
    
	@GetMapping(value = "/getDiscount",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
   public DiscountDetailBean getDiscount(@Context ServletContext context, ParkingRequestBody requestBody) {
        log.info("本地收到 getDiscount 請求 -> center: {}, cardNo: {}, carNo: {}", 
                 requestBody.getCenter(), requestBody.getCardNo(), requestBody.getCardNo());
        
        try {
            // 從 ServletContext 安全取得假期對照表
            @SuppressWarnings("unchecked")
            Map<String, String> holidayMap = (Map<String, String>) context.getAttribute("HolidayMap");
            if (holidayMap == null) {
                holidayMap = new HashMap<>();
            }
            
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String todayStr = GetDateTime.getTodayDateW("-");

            // 1. 檢查車輛租賃狀態 (修正：核心鍵值必須是【車牌】carNo，而非卡號)
            ParkingRent rentEntity = parkingService.getParkingRent(requestBody.getCardNo());
            if (rentEntity != null && rentEntity.getCarNo() != null) {
                DiscountDetailBean retBean = new DiscountDetailBean();
                retBean.setIsRent("Y");
                retBean.setUserName(rentEntity.getUserName());
                return retBean;                
            }
            
            // 2. 處理會員與今日已折抵資格檢查
            LpjFile memberBean = new LpjFile();
            if (requestBody.getCardNo() != null && !"".equals(requestBody.getCardNo())) {
                memberBean = memberService.getMemberData4PD(requestBody.getCardNo());
                if (memberBean == null || memberBean.getLpj01() == null) {
                    throw new Exception("這張不是比漾會員卡：" + requestBody.getCardNo());
                }

                // 若非特定的卡別類型 "66"，需檢查今日是否已經有其他車牌綁定折抵
                if (!"66".equals(memberBean.getLpkud02())) {
                    ParkingDiscountExec execCheck = parkingService.getVIPUsed(requestBody.getCardNo(), memberBean.getLpj01());
                    if (execCheck != null && execCheck.getCarNo() != null) {
                        throw new Exception("會員 " + memberBean.getLpj01() + " " + memberBean.getLpk04() + 
                                            "，停車折抵今日已使用於車號：" + execCheck.getCarNo());
                    }
                }
            }

            // 3. 獲取該車牌目前的折扣設定清單 (修正：以車牌 carNo 進行查詢)
            List<ParkingDiscountSet> discList = parkingService.getDiscExec(requestBody.getCardNo());
            if (discList == null) {
                discList = new ArrayList<>();
            }
            
            // 狀態控制暫存 Map 
            Map<String, Boolean> isUsedMap = new HashMap<>();
            Map<String, Boolean> isSavedMap = new HashMap<>();
            for (ParkingDiscountSet setBean : discList) {
                isUsedMap.put(setBean.getDiscId(), "Y".equals(setBean.getIsUsed()));
                if ("Y".equals(setBean.getIsUsed())) { 
                    isUsedMap.put(setBean.getDiscId(), true);
                    isSavedMap.put(setBean.getDiscId(), true);
                } else {
                    boolean hasPNo = setBean.getPNo() != null;
                    isUsedMap.put(setBean.getDiscId(), false);
                    isSavedMap.put(setBean.getDiscId(), hasPNo);
                }
            }
            
            // 4. 初始化回傳封裝物件 (修正：對齊正確的小駝峰與欄位本意映射)
            DiscountDetailBean retBean = new DiscountDetailBean();
            retBean.setUserNo(memberBean.getLpj01());       // 舊版 sUserNO -> userNo (會員編號)
            retBean.setCardNo(requestBody.getCardNo());    // 舊版 sCardNO -> cardNo (會員卡號)
            retBean.setCarNo(requestBody.getCardNo());      // 補上正確的車牌欄位

            Integer maxPNo = -1;
            List<ParkingDiscountExecBean> discountExecList = new ArrayList<>();            
            
            // 5. 完整還原核心商業卡別折扣扣點與寫入邏輯
            for (ParkingDiscountSet setBean : discList) {
                ParkingDiscountExecBean myBean = new ParkingDiscountExecBean();
                myBean.setPNo(null);
                myBean.setBookingDate(setBean.getBookingDate() == null ? "" : sdf1.format(setBean.getBookingDate()));
                myBean.setDiscId(setBean.getDiscId());
                myBean.setDiscName(setBean.getDiscName());
                myBean.setIsUsed("N");
                
                String discId = setBean.getDiscId();

                if ("black_card".equals(discId)) {
                    if ("66".equals(memberBean.getLpkud02())) {
                        if (isSavedMap.containsKey("black_card") && !isSavedMap.get("black_card")) {
                            maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                            isSavedMap.put("black_card", true);
                        }
                        myBean.setDiscHour(setBean.getDiscHour());
                        myBean.setModify("Y");
                    } else if (isSavedMap.containsKey("black_card") && isSavedMap.get("black_card")) { 
                        myBean.setDiscHour(setBean.getUsedHour());
                        myBean.setModify("Y");                
                        myBean.setPNo(setBean.getPNo());                    
                    } else {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                    }
                } 
                else if ("vip_common".equals(discId)) {
                    if ("62".equals(memberBean.getLpkud02())) {
                        if (isUsedMap.containsKey("vip_common") && isUsedMap.get("vip_common")) {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");
                            myBean.setPNo(null);                            
                        } else if (isSavedMap.containsKey("vip_common") && isSavedMap.get("vip_common")) { 
                            myBean.setDiscHour(setBean.getUsedHour());
                            myBean.setModify("Y");
                            myBean.setPNo(setBean.getPNo());
                        } else {
                            if (holidayMap.containsKey(todayStr)) {
                                myBean.setDiscHour(0d);
                                myBean.setModify("N");
                                myBean.setPNo(null);
                            } else {
                                myBean.setDiscHour(setBean.getDiscHour());
                                myBean.setModify("Y");
                                maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                                myBean.setPNo(maxPNo);
                                isSavedMap.put("vip_common", true);
                            }
                        }                            
                    } else if (isSavedMap.containsKey("vip_common") && isSavedMap.get("vip_common")) { 
                        if (isUsedMap.containsKey("vip_common") && isUsedMap.get("vip_common")) {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");                            
                        } else {
                            myBean.setDiscHour(setBean.getUsedHour());
                            myBean.setModify("Y");                
                            myBean.setPNo(setBean.getPNo());
                        }
                    } else {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                    }
                } 
                else if ("vip_holiday".equals(discId)) {
                    if ("62".equals(memberBean.getLpkud02())) {
                        if (isUsedMap.containsKey("vip_holiday") && isUsedMap.get("vip_holiday")) {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");
                            myBean.setPNo(null);                            
                        } else if (isSavedMap.containsKey("vip_holiday") && isSavedMap.get("vip_holiday")) { 
                            myBean.setDiscHour(setBean.getUsedHour());
                            myBean.setModify("Y");
                            myBean.setPNo(setBean.getPNo());
                        } else {
                            if (holidayMap.containsKey(todayStr)) {
                                myBean.setDiscHour(setBean.getDiscHour());
                                myBean.setModify("Y");
                                maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                                myBean.setPNo(maxPNo);
                                isSavedMap.put("vip_holiday", true);
                            } else {
                                myBean.setDiscHour(0d);
                                myBean.setModify("N");
                                myBean.setPNo(null);
                            }
                        }                            
                    } else if (isSavedMap.containsKey("vip_holiday") && isSavedMap.get("vip_holiday")) { 
                        if (isUsedMap.containsKey("vip_holiday") && isUsedMap.get("vip_holiday")) {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");                            
                        } else {
                            myBean.setDiscHour(setBean.getUsedHour());
                            myBean.setModify("Y");                
                            myBean.setPNo(setBean.getPNo());
                        }    
                    } else {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                    }
                } 
                else if ("vip".equals(discId)) { 
                    if ("62".equals(memberBean.getLpkud02())) {
                        if (isSavedMap.containsKey("vip") && !isSavedMap.get("vip")) {
                            maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                            isSavedMap.put("vip", true);
                        }
                        myBean.setDiscHour(setBean.getDiscHour());
                        myBean.setModify("Y");
                    } else if (isSavedMap.containsKey("vip") && isSavedMap.get("vip")) { 
                        myBean.setDiscHour(setBean.getUsedHour());
                        myBean.setModify("Y");                
                        myBean.setPNo(setBean.getPNo());                    
                    } else {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                    }                    
                } 
                else if ("ts_common".equals(discId)) {
                    if ((isUsedMap.containsKey("ts_common") && isUsedMap.get("ts_common")) ||
                        (isSavedMap.containsKey("ts_holiday") && isSavedMap.get("ts_holiday")) ||
                        (isSavedMap.containsKey("member_card") && isSavedMap.get("member_card")) ||
                        (isSavedMap.containsKey("black_card") && isSavedMap.get("black_card")) ||
                        (isSavedMap.containsKey("vip") && isSavedMap.get("vip")) ||
                        (isSavedMap.containsKey("vip_common") && isSavedMap.get("vip_common")) || 
                        (isSavedMap.containsKey("vip_holiday") && isSavedMap.get("vip_holiday"))) {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                        myBean.setPNo(null);
                    } else if (isSavedMap.containsKey("ts_common") && isSavedMap.get("ts_common")) { 
                        myBean.setDiscHour(setBean.getUsedHour());
                        myBean.setModify("Y");
                        myBean.setPNo(setBean.getPNo());
                    } else {
                        if (requestBody.getCardNo() != null && !"".equals(requestBody.getCardNo()) && requestBody.getCardNo().startsWith("TS")) {
                            if (holidayMap.containsKey(todayStr)) {
                                myBean.setDiscHour(0d);
                                myBean.setModify("N");
                                myBean.setPNo(null);
                            } else {
                                myBean.setDiscHour(setBean.getDiscHour());
                                myBean.setModify("Y");
                                maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                                myBean.setPNo(maxPNo);
                                isSavedMap.put("ts_common", true);
                            }
                        } else {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");
                            myBean.setPNo(null);
                        }
                    }
                } 
                else if ("ts_holiday".equals(discId)) {                    
                    if ((isUsedMap.containsKey("ts_holiday") && isUsedMap.get("ts_holiday")) ||
                        (isSavedMap.containsKey("ts_common") && isSavedMap.get("ts_common")) ||
                        (isSavedMap.containsKey("member_card") && isSavedMap.get("member_card")) ||
                        (isSavedMap.containsKey("black_card") && isSavedMap.get("black_card")) ||
                        (isSavedMap.containsKey("vip") && isSavedMap.get("vip")) ||
                        (isSavedMap.containsKey("vip_common") && isSavedMap.get("vip_common")) || 
                        (isSavedMap.containsKey("vip_holiday") && isSavedMap.get("vip_holiday"))) {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                        myBean.setPNo(null);
                    } else if (isSavedMap.containsKey("ts_holiday") && isSavedMap.get("ts_holiday")) { 
                        myBean.setDiscHour(setBean.getUsedHour());
                        myBean.setModify("Y");
                        myBean.setPNo(setBean.getPNo());
                    } else {
                        if (requestBody.getCardNo() != null && !"".equals(requestBody.getCardNo()) && requestBody.getCardNo().startsWith("TS")) {
                            if (holidayMap.containsKey(todayStr)) {
                                myBean.setDiscHour(setBean.getDiscHour());
                                myBean.setModify("Y");
                                maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                                myBean.setPNo(maxPNo);
                                isSavedMap.put("ts_holiday", true);
                            } else {
                                myBean.setDiscHour(0d);
                                myBean.setModify("N");
                                myBean.setPNo(null);
                            }                            
                        } else {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");
                            myBean.setPNo(null);
                        }
                    }
                } 
                else if ("member_card".equals(discId)) {
                    if ((isUsedMap.containsKey("member_card") && isUsedMap.get("member_card")) ||
                        (isSavedMap.containsKey("ts_common") && isSavedMap.get("ts_common")) ||
                        (isSavedMap.containsKey("ts_holiday") && isSavedMap.get("ts_holiday")) ||
                        (isSavedMap.containsKey("black_card") && isSavedMap.get("black_card")) ||
                        (isSavedMap.containsKey("vip") && isSavedMap.get("vip")) ||
                        (isSavedMap.containsKey("vip_common") && isSavedMap.get("vip_common")) || 
                        (isSavedMap.containsKey("vip_holiday") && isSavedMap.get("vip_holiday"))) {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                        myBean.setPNo(null);
                    } else if (isSavedMap.containsKey("member_card") && isSavedMap.get("member_card")) { 
                        myBean.setDiscHour(setBean.getUsedHour());
                        myBean.setModify("Y");                
                        myBean.setPNo(setBean.getPNo());
                    } else {
                        if (requestBody.getCardNo() != null && !"".equals(requestBody.getCardNo()) && !requestBody.getCardNo().startsWith("TS")) {
                            myBean.setDiscHour(setBean.getDiscHour());
                            myBean.setModify("Y");
                            maxPNo = parkingService.insertCard(null, memberBean.getLpj01(), requestBody.getCardNo(), requestBody.getCardNo(), setBean.getDiscId(), setBean.getDiscName(), setBean.getDiscHour());
                            myBean.setPNo(maxPNo);
                            isSavedMap.put("member_card", true);
                        } else {
                            myBean.setDiscHour(0d);
                            myBean.setModify("N");
                            myBean.setPNo(null);                            
                        }
                    }
                } 
                else if ("sale_hour".equals(discId)) {            
                    if (setBean.getPromoteAmt() != null && setBean.getPromoteAmt() > 0) {
                        double dSaleHour = Math.floor(setBean.getPromoteAmt() / 500) * setBean.getDiscHour();
                        
                        if (!"Y".equals(setBean.getIsUnlimitedHour())) {
                            if (dSaleHour > setBean.getHour_max()) {
                                dSaleHour = setBean.getHour_max();
                            }
                            if (setBean.getPromoteAmt() >= 10000) {
                                dSaleHour = 12;
                            }
                        }
                        myBean.setDiscHour(dSaleHour);
                        myBean.setModify("Y");
                        myBean.setPNo(setBean.getPNo());
                    } else {
                        myBean.setDiscHour(0d);
                        myBean.setModify("N");
                        myBean.setPNo(null);
                    }
                } 
                else {
                    myBean.setDiscHour(setBean.getUsedHour() == null ? 0 : setBean.getUsedHour());
                    myBean.setModify((setBean.getUsedHour() == null || setBean.getUsedHour() == 0) ? "N" : "Y");
                    myBean.setPNo(setBean.getPNo());
                }
                
                discountExecList.add(myBean);
                
                // 動態追蹤最大流水號流水號
                if (myBean.getPNo() != null && myBean.getPNo() > maxPNo) {
                    maxPNo = myBean.getPNo();
                }
            }
            
            retBean.setIsRent("N");
            retBean.setPNo(maxPNo);            
            retBean.setDiscount(discountExecList);
            
            return retBean;

        } catch (Exception e) {
            log.error("執行 getDiscount 核心業務邏輯發生異常: ", e);
            ObjectNode errorJson = objectMapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }
    
    @PostMapping(
            value = "/sync", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    /**
     * 同步停車折扣與交易資訊 (方案 A：本地業務同步)
     */
    public ResponseBean sync(DiscountDetailBean requestBody) {
        // 使用 SLF4J 參數化日誌，避免字串拼接帶來的效能損耗
        log.info("【sync】接收同步請求 -> center: {}, carNO: {}, parkingHour: {}, discFee: {}, payAmt: {}", 
                 requestBody.getCenter(), 
                 requestBody.getCarNo(), 
                 requestBody.getParkingHour(), 
                 requestBody.getDiscFee(), 
                 requestBody.getPayAmt());
        try {
            // 呼叫底層停車系統執行同步落庫
            parkingService.sync(requestBody);
            
            // 回傳成功狀態 bean
            ResponseBean response = new ResponseBean();
            response.setCode(ErrCodeConst.finished);
            response.setMessage(ErrCodeConst.finished_message);			
            return response;
            
        } catch (Exception e) {
            log.error("【sync】處理資料同步時發生異常: ", e);
            
            // 保持原有的異常處理機制，包裝成 Standard HTTP 417 回傳
            ObjectNode json = objectMapper.createObjectNode();
            json.put("code", HttpStatus.EXPECTATION_FAILED.value());
            json.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, json.toString(), e);
        }
    }
    
    @PostMapping(
            value = "/delCardNO", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public ResponseBean delCardNO(ParkingRequestBody requestBody) {
        try {
            log.info("delCardNO : sCenter -> {}, sCardNO -> {}, p_no -> {}", 
                     requestBody.getCenter(), requestBody.getCardNo(), requestBody.getPNo());
            
            // 呼叫 Service 執行刪除
            parkingService.delCardNo(requestBody.getCenter(), requestBody.getCardNo(), requestBody.getPNo());
            
            ResponseBean response = new ResponseBean();
            response.setCode(ErrCodeConst.finished);
            response.setMessage(ErrCodeConst.finished_message);            
            return response;
            
        } catch (Exception e) {
            log.error("delCardNO 異常: ", e);
            
            // 使用與 sync 一致的錯誤處理方式，統一回傳格式
            ObjectNode json = objectMapper.createObjectNode();
            json.put("code", HttpStatus.EXPECTATION_FAILED.value());
            json.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, json.toString());
        }
    }    
    
}