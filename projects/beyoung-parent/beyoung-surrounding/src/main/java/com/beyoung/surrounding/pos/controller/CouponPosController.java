package com.beyoung.surrounding.pos.controller;

import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.app.entity.LQE_FILE;
import com.beyoung.surrounding.pos.bean.CouponPosBean;
import com.beyoung.surrounding.pos.client.GiftServiceFeignClient;
import com.beyoung.surrounding.pos.entity.TD;
import com.beyoung.surrounding.pos.service.CouponPosService;
import com.beyoung.surrounding.pos.service.PosDetailService;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.beyoung.surrounding.util.GetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/pos/Coupon")
@RequiredArgsConstructor
public class CouponPosController {

	private final Environment env;
	private final PosDetailService posDetailService;
	private final CouponPosService couponPosService;
	private final GiftServiceFeignClient giftServiceClient;

	@GetMapping(value = "/getCouponByInvoiceNO/{invoiceNO}",
    produces = { 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
    public List<CouponPosBean> getCouponByInvoiceNO(@PathVariable String invoiceNO) {
        try {
            log.info("getCouponByInvoiceNO(New) : sInvoiceNO -> {}", invoiceNO);

            // 1. 取得 POS 交易明細
            TD entity = posDetailService.getTDByInvoiceNO(env, "BY001", invoiceNO);
            if (entity == null) {
                throw new IllegalArgumentException("找不到對應的發票交易明細");
            }

            // 2. 使用 Jackson ObjectMapper 建立請求體
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            com.fasterxml.jackson.databind.node.ObjectNode joTrans = mapper.createObjectNode();
            joTrans.put("T0100", "0200");
            joTrans.put("T0200", entity.getVipNo() == null ? "" : entity.getVipNo()); //  對齊上一題修正的小駝峰 getVipNo
            joTrans.put("T0300", "249000");
            joTrans.put("T1200", GetDateTime.getTime("")); 
            joTrans.put("T1300", GetDateTime.getTodayDateW(""));
            joTrans.put("T4100", entity.getPosNo() == null ? "BYSERVER" : entity.getPosNo()); //  對齊小駝峰 getPosNo
            joTrans.put("T4200", "BY001");
            joTrans.put("T5507", invoiceNO);
            joTrans.put("T5509", "0");
            joTrans.put("T5583", entity.getSalDate() == null ? GetDateTime.getTodayDateW("") : entity.getSalDate()); //  對齊小駝峰 getSalDate
            
            com.fasterxml.jackson.databind.node.ObjectNode joAll = mapper.createObjectNode();
            joAll.set("Trans", joTrans);
            
            log.info("getCouponByInvoiceNO(up)：{}", joAll.toString());

            // 3.  【改成 Feign 呼叫】直接調用介面，不再需要手動處理 HttpHeaders 與 RestTemplate
            com.fasterxml.jackson.databind.JsonNode ret = giftServiceClient.fetchCoupons(joAll);
            
            log.info("getCouponByInvoiceNO(down)：{}", ret.toString());

            // 4. 解析外部回應的 JSON
            com.fasterxml.jackson.databind.JsonNode transNode = ret.path("Trans");
            
            // 檢查回應狀態碼是否為 "00"
            if (!transNode.path("T3900").asText().equals("00")) {
                throw new RuntimeException("系統執行錯誤，錯誤代碼：" + transNode.path("T3900").asText());
            }

            ArrayList<CouponPosBean> al = new ArrayList<>();

            // 如果沒有 T5579 節點，直接回傳空陣列
            if (transNode.path("T5579").isMissingNode()) {
                return al;
            }

            // 5. 剖析複雜的券號陣列結構
            StringBuilder sbCouponNO = new StringBuilder();
            HashMap<String, String> hCouponType = new HashMap<>();
            
            com.fasterxml.jackson.databind.JsonNode ja = transNode.path("T5579");
            if (ja.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode jo : ja) {
                    if (!jo.has("T557907")) {
                        continue;
                    }

                    String sCouponType = jo.path("T557910").asText(); // 紙本 or 電子
                    com.fasterxml.jackson.databind.JsonNode jaT557907 = jo.path("T557907");
                    
                    if (jaT557907.isArray()) {
                        for (com.fasterxml.jackson.databind.JsonNode node : jaT557907) {
                            String sCouponNO = node.asText().substring(0, Math.min(node.asText().length(), 30)).trim();
                            sbCouponNO.append(sCouponNO).append(",");
                            hCouponType.put(sCouponNO, sCouponType);
                        }
                    }
                }
            }

            // 移除尾贅逗號
            if (sbCouponNO.length() > 0 && sbCouponNO.charAt(sbCouponNO.length() - 1) == ',') {
                sbCouponNO.deleteCharAt(sbCouponNO.length() - 1);
            }
            
            // 6. 批次查詢本地資料庫
            HashMap<String, String> h = new HashMap<>();
            HashMap<String, Double> h1 = new HashMap<>();
            
            List<LQE_FILE> l2 = couponPosService.getCouponStatus(sbCouponNO.toString());
            log.info("getCouponByInvoiceNO.getCouponStatus : sbCouponNO -> {}", sbCouponNO.toString());
            
            for (LQE_FILE data : l2) {
                String isUsed = ("4".equals(data.getLqe17()) || "Y".equals(data.getTaLqe09())) ? "Y" : "N";
                h.put(data.getLqe01(), isUsed);
                h1.put(data.getLqe01(), data.getTaLqe02() != null ? data.getTaLqe02().doubleValue() : 0.0);
            }
            
            // 7. 重新比對陣列組裝回傳物件
            if (ja.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode jo : ja) {
                    if (!jo.has("T557907")) {
                        continue;
                    }
                    com.fasterxml.jackson.databind.JsonNode jaT557907 = jo.path("T557907");
                    if (jaT557907.isArray()) {
                        for (com.fasterxml.jackson.databind.JsonNode node : jaT557907) {
                            String sCouponNO = node.asText().substring(0, Math.min(node.asText().length(), 30)).trim();
                            
                            CouponPosBean bean = new CouponPosBean();
                            bean.setCouponNO(sCouponNO);
                            bean.setPrice(h1.containsKey(sCouponNO) ? h1.get(sCouponNO).intValue() : 0);
                            bean.setIsUsed(h.get(sCouponNO));
                            bean.setIsAPP("V".equalsIgnoreCase(hCouponType.get(sCouponNO)) ? "Y" : "N");
                            al.add(bean);
                        }
                    }
                }
            }
            
            return al;

        } catch (Exception e) {
            log.error("取得贈品券號時發生錯誤: {}", e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "取得贈品券號時發生錯誤");
            
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
	}
	
    @GetMapping(value = "/doCouponInvalid/{center}",
    produces = { 
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" }) 
    public ResponseBean doCouponInvalid(
            @PathVariable String center,       
            @RequestParam String couponID    
    ) {
        try {
            log.info("doCouponInvalid : center -> {}, couponID -> {}", center, couponID);
            
            // 1. 執行防空檢查
            if (couponID == null || couponID.isBlank()) {
                throw new IllegalArgumentException("優惠券 ID 不能為空");
            }

            ResponseBean bean = new ResponseBean();
            
            // 2. 呼叫 Service 執行失效作業
            couponPosService.doCouponInvalid(center, couponID);
            
            // 3. 設定成功狀態碼 (對齊你原本的 ErrCodeConst 常量)
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);    
            
            return bean;
            
        } catch (Exception e) {
            log.error("進行贈品券失效作業失敗: {}", e.getMessage(), e);
            
            // 4. 改用 Jackson 建立錯誤回應，防範原本舊語法中未宣告的 json 變數崩潰
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", org.springframework.http.HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", "進行贈品券失效作業失敗 " + e.getMessage());
            
            // 5. 拋出 Spring Boot 標準的 ResponseStatusException (對齊 417 Expectation Failed)
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.EXPECTATION_FAILED, 
                    errorJson.toString()
            );
        }
    }

    
    
    
}