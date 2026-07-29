package com.beyond.surrounding.bonus.controller;

import com.beyond.surrounding.bonus.dto.BonusDTO;
import com.beyond.surrounding.bonus.service.HiefPayService;
import com.beyond.surrounding.app.entity.WALLET;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.bonus.bean.CouponBean;
import com.beyond.surrounding.bonus.bean.PointBean;
import com.beyond.surrounding.bonus.bean.PrizeBean;
import com.beyond.surrounding.bonus.bean.RedeemBean;
import com.beyond.surrounding.bonus.bean.RefundBean;
import com.beyond.surrounding.bonus.bean.RequestPOSBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * ChiefPay 贈獎 API 入口 Controller
 * 對應原始路徑: /Surrounding/rest/hiefpay/purchase
 * @param <Wallet>
 */
@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/hiefpay")
@RequiredArgsConstructor
public class HiefPayController {

    private final HiefPayService hiefPayService;
    
    /**
     * 處理取得錢包資料請求
     */
    @PostMapping(
            value = "/getWallet", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public List<WALLET> getWallet(RequestBody requestBody) {
    	try {
            log.info("getWallet");
            
            // 呼叫你的 Service 取得資料
            List<WALLET> walletList = hiefPayService.getWalletList();
            
            return walletList;
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            
            // 1. 建立 Jackson 的 ObjectNode 來代替舊專案的 JSONObject json
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode jsonNode = mapper.createObjectNode();
            
            // 2. 對應原本的 EXPECTATION_FAILED (HTTP 417) 狀態碼與錯誤訊息
            jsonNode.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            jsonNode.put("message", e.getMessage());
            
            // 3. 拋出 Spring Boot 的狀態異常，並帶入 JSON 字串
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    jsonNode.toString()
            );
        }
    } 

    /**
     * 處理 ChiefPay 贈獎請求
     */
    @PostMapping(
            value = "/purchase", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public ResponseBean purchase(RequestPOSBody requestBody) {
        try {
            // 1. 呼叫你已經寫好的 Service 邏輯
            String result = hiefPayService.processPurchase(requestBody);
            log.info("ChiefPay purchase 處理結果: {}", result);
            
            // 2. 成功時，建構並回傳原本的 ResponseBean
            ResponseBean _bean = new ResponseBean();
            _bean.setCode(ErrCodeConst.finished);
            _bean.setMessage(ErrCodeConst.finished_message);
            
            return _bean;
            
        } catch (Exception e) {
            log.error("ChiefPay 贈獎處理異常", e);
            
            // 3. 異常時：維持原本回傳 HTTP 417 (EXPECTATION_FAILED) 與錯誤 JSON 的行為
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
            value = "/redeemable", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public RedeemBean redeemable(@org.springframework.web.bind.annotation.RequestBody RequestPOSBody requestBody) {
        try {
            log.info("ChiefPay redeemable : cardNo -> {}, deviceId -> {}", requestBody.getCardNo(), requestBody.getDeviceId());
            
            // 1. 呼叫 Service 取得底層整合與解析後的 DTO
            BonusDTO.RedeemResponseDTO data = hiefPayService.getRedeemable(requestBody);
            
            // 2. 實例化外部對接專用的回傳門面 Bean
            RedeemBean _bean = new RedeemBean();
            _bean.setCode(ErrCodeConst.finished);
            _bean.setMessage(ErrCodeConst.finished_message);
            
            // 3. 轉換並對接：將 List<PointDTO> 轉為 List<PointBean>
            if (data.getPointList() != null) {
                List<PointBean> pointBeans = data.getPointList().stream().map(dto -> {
                    PointBean pBean = new PointBean();
                    pBean.setPointCode(dto.getPointCode());
                    pBean.setPoint(dto.getPoint());
                    return pBean;
                }).collect(java.util.stream.Collectors.toList());
                _bean.setPointList(pointBeans); // 倒回對齊小駝峰重構後的屬性
            }

            // 4. 轉換並對接：將 List<PrizeDTO> 轉為 List<PrizeBean>
            if (data.getPrizeList() != null) {
                List<PrizeBean> prizeBeans = data.getPrizeList().stream().map(dto -> {
                    PrizeBean pBean = new PrizeBean();
                    pBean.setActivityCode(dto.getActivityCode());
                    pBean.setPrizeCode(dto.getPrizeCode());
                    pBean.setPrizeName(dto.getPrizeName());
                    pBean.setPointCode(dto.getPointCode());
                    pBean.setNeedPoint(dto.getNeedPoint());
                    pBean.setActivityName(dto.getActivityName());
                    pBean.setPrizeType(dto.getPrizeType());
                    pBean.setRedeemableQty(dto.getRedeemableQty());
                    return pBean;
                }).collect(java.util.stream.Collectors.toList());
                _bean.setPrizeList(prizeBeans); // 倒回對齊小駝峰重構後的屬性
            }
            
            return _bean;
                    
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            
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
            value = "/redeem", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public RedeemBean redeem(@org.springframework.web.bind.annotation.RequestBody RequestPOSBody requestBody) {
        try {
            log.info("ChiefPay redeem : cardNo -> {}, deviceId -> {}", requestBody.getCardNo(), requestBody.getDeviceId());

            // 1. 呼叫 Service 取得 DTO 結構的資料
            BonusDTO.RedeemResponseDTO data = hiefPayService.processRedeem(requestBody);
            
            // 2. 實例化要回傳的外部 Bean 門面
            RedeemBean bean = new RedeemBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            // 3. 將 List<CouponDTO> 轉換為 List<CouponBean>
            if (data.getCouponList() != null) {
                List<CouponBean> couponBeans = data.getCouponList().stream().map(dto -> {
                    CouponBean cBean = new CouponBean();
                    cBean.setCouponNo(dto.getCouponNO());
                    cBean.setStartDate(dto.getStartDate());
                    cBean.setEndDate(dto.getEndDate());
                    cBean.setValue(dto.getValue());
                    return cBean;
                }).collect(java.util.stream.Collectors.toList());
                bean.setCouponList(couponBeans); // 這裡對接的是你原始宣告的 List<CouponBean>
            }

            // 4. 將 List<PrizeDTO> 轉換為 List<PrizeBean>
            if (data.getPrizeList() != null) {
                List<PrizeBean> prizeBeans = data.getPrizeList().stream().map(dto -> {
                    PrizeBean pBean = new PrizeBean();
                    pBean.setActivityCode(dto.getActivityCode());
                    pBean.setActivityName(dto.getActivityName());
                    pBean.setPrizeType(dto.getPrizeType());
                    pBean.setPrizeCode(dto.getPrizeCode());
                    pBean.setPrizeName(dto.getPrizeName());
                    pBean.setPointCode(dto.getPointCode());
                    pBean.setNeedPoint(dto.getNeedPoint());
                    pBean.setRedeemableQty(dto.getRedeemableQty());
                    pBean.setPrice(dto.getPrice());
                    pBean.setCouponNo(dto.getCouponNO());
                    return pBean;
                }).collect(java.util.stream.Collectors.toList());
                bean.setPrizeList(prizeBeans); // 這裡對接的是你原始宣告的 List<PrizeBean>
            }
            
            return bean;
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            
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
            value = "/refund", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public RefundBean refund(RequestPOSBody requestBody
    ) {
        try {
            log.info("ChiefPay refund : cardNo -> {}, deviceId -> {}, invoiceNo -> {}", 
                    requestBody.getCardNo(), requestBody.getDeviceId(), requestBody.getInvoiceNO());
            
            // 1. 呼叫 Service 執行外部 API 退貨邏輯
            hiefPayService.processRefund(requestBody);
            
            // 2. 實例化回傳的 RefundBean，並設定對齊原本舊系統的成功代碼與訊息
            RefundBean bean = new RefundBean();
            bean.setInvoiceNo(requestBody.getInvoiceNO()); 
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            return bean;
            
        } catch (Exception e) {
            log.error("ChiefPay 退貨處理異常: " + e.getMessage(), e);
            
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
            value = "/checkInvoiceNO", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } 
    )
    public RedeemBean checkInvoiceNO(RequestPOSBody requestBody) {
        try {
            log.info("checkInvoiceNO : cardNo -> {}, deviceId -> {}", requestBody.getCardNo(), requestBody.getDeviceId());
            
            // 1. 呼叫 Service 取得解析後的 DTO 資料
            BonusDTO.RedeemResponseDTO data = hiefPayService.processCheckInvoice(requestBody);
            
            // 2. 實例化回傳物件並填入成功的代碼與訊息
            RedeemBean bean = new RedeemBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            // 3. 轉換並對接：將 List<PointDTO> 轉為 List<PointBean>
            if (data.getPointList() != null) {
                List<PointBean> pointBeans = data.getPointList().stream().map(dto -> {
                    PointBean pBean = new PointBean();
                    pBean.setPointCode(dto.getPointCode());
                    pBean.setPoint(dto.getPoint());
                    return pBean;
                }).collect(java.util.stream.Collectors.toList());
                bean.setPointList(pointBeans); // 填入重構後的小駝峰屬性
            }

            // 4. 轉換並對接：將 List<PrizeDTO> 轉為 List<PrizeBean>
            if (data.getPrizeList() != null) {
                List<PrizeBean> prizeBeans = data.getPrizeList().stream().map(dto -> {
                    PrizeBean pBean = new PrizeBean();
                    pBean.setActivityCode(dto.getActivityCode());
                    pBean.setPrizeCode(dto.getPrizeCode());
                    pBean.setPrizeName(dto.getPrizeName());
                    pBean.setPointCode(dto.getPointCode());
                    pBean.setNeedPoint(dto.getNeedPoint());
                    pBean.setActivityName(dto.getActivityName());
                    pBean.setPrizeType(dto.getPrizeType());
                    pBean.setRedeemableQty(dto.getRedeemableQty());
                    return pBean;
                }).collect(java.util.stream.Collectors.toList());
                bean.setPrizeList(prizeBeans); // 填入重構後的小駝峰屬性
            }
            
            return bean;
            
        } catch (Exception e) {
            log.error("checkInvoiceNO 處理異常: " + e.getMessage(), e);
            
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