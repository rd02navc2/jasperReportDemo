package com.beyoung.surrounding.bonus.service;

import com.beyoung.surrounding.app.entity.TC_PSA_FILE;
import com.beyoung.surrounding.app.entity.WALLET;
import com.beyoung.surrounding.util.GetDateTime;
import com.beyoung.surrounding.dto.AppendInvoiceDTO;
import com.beyoung.surrounding.bonus.bean.RequestPOSBody;
import com.beyoung.surrounding.bonus.dto.BonusDTO;
import com.beyoung.surrounding.bonus.dto.BonusDTO.PointDTO;
import com.beyoung.surrounding.bonus.dto.BonusDTO.PrizeDTO;
import com.beyoung.surrounding.bonus.repository.WALLETRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HiefPayService {

    private final WALLETRepository walletRepository;
 // 假設您已在類別中注入或實例化了 RestTemplate
    private final RestTemplate restTemplate = new RestTemplate();
    private final Environment env;

    @Transactional(readOnly = true)
    public List<WALLET> getWalletList() {
        log.info("HiefPayService: 使用 Repository 取得錢包列表");
        // 直接使用 JPA 提供的 findAll()
        return walletRepository.findAll();
    }

	public void triggerChiefPayBonus(AppendInvoiceDTO bean, String invoiceNo, TC_PSA_FILE psa) throws Exception {
	    log.info("開始呼叫 ChiefPay 贈獎 API, 發票號碼: {}", invoiceNo);
	
	    java.util.Date tcPsa04 = psa.getTcPsa04();
	    if (tcPsa04 == null) return;
	
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	    String dateStr = sdf.format(tcPsa04);
	
	    // 1. 組裝 DTO (確保欄位名稱與您最終決定的 JSON 格式一致)
	    BonusDTO.ChiefPayRequest request = BonusDTO.ChiefPayRequest.builder()
	            .cardNo(bean.getCardId())
	            .creditCard(bean.getCreditCard() != null ? bean.getCreditCard() : "")
	            .invoiceAmt(bean.getInvAmt())
	            .promoteAmt(bean.getAmount())
	            .creditAmt(bean.getCreditCardAmt() != null ? bean.getCreditCardAmt() : 0.0)
	            .deviceId(bean.getPosId())
	            .counterId(bean.getCounterId())
	            .invoiceSN(bean.getInvoiceSn())
	            .invoiceNO(invoiceNo)
	            .invoiceDate(dateStr)
	            .build();
	
	    // 2. 設定 HTTP Headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<BonusDTO.ChiefPayRequest> entity = new HttpEntity<>(request, headers);
	
	    // 3. 發送 POST 請求
	    String url = "http://your-target-api-url/api/purchase"; // 請填入實際 API URL
	    try {
	        log.info("發送請求內容: {}", request);
	        
	        // 使用 postForObject 發送請求並接收 Map
	        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
	        
	        // 4. 檢查業務邏輯狀態碼
	        if (response == null || !"0000".equals(String.valueOf(response.get("code")))) {
	            log.error("ChiefPay API 贈獎失敗，回應內容: {}", response);
	            throw new Exception("ChiefPay 贈獎失敗: " + (response != null ? response.get("message") : "無回應"));
	        }
	        
	        log.info("ChiefPay 贈獎同步成功");
		    } catch (Exception e) {
		        log.error("ChiefPay API 通訊異常", e);
		        throw e; // 拋出異常以觸發 @Transactional 回滾
		    }
		}

	public String processPurchase(RequestPOSBody requestBody) throws Exception {
        // 1. 組裝 JSON 結構 (使用 Map 對應舊系統的 JSONObject)
        Map<String, Object> trans = new HashMap<>();
        trans.put("T0100", "0200");
        trans.put("T0200", requestBody.getCardNo());
        trans.put("T0201", requestBody.getCreditCard());
        trans.put("T0300", "708070");
        trans.put("T0400", requestBody.getInvoiceAmt());
        trans.put("T0401", requestBody.getPromoteAmt());
        trans.put("T0402", requestBody.getCreditAmt());
        trans.put("T0405", requestBody.getInvoiceAmt());
        trans.put("T1200", GetDateTime.getTime(""));
        trans.put("T1300", GetDateTime.getTodayDateW(""));
        trans.put("T4100", requestBody.getDeviceId());
        trans.put("T4200", "BY001");
        trans.put("T5503", requestBody.getCounterId());
        trans.put("T5504", requestBody.getDeviceId());
        trans.put("T5505", requestBody.getInvoiceSN());
        trans.put("T5507", requestBody.getInvoiceNO());
        trans.put("T5509", "0");

        Map<String, Object> allData = new HashMap<>();
        allData.put("Trans", trans);

        // 2. 發送請求
        String url = env.getProperty("GIFT_WS_URL"); //dc- 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(allData, headers);

        log.info("ChiefPay 呼叫外部 API: {}", allData);
        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

        // 3. 解析與邏輯驗證
        if (response != null && response.containsKey("Trans")) {
            Map<String, Object> transRes = (Map<String, Object>) response.get("Trans");
            String t3900 = String.valueOf(transRes.getOrDefault("T3900", ""));
            
            if (!"00".equals(t3900)) {
                throw new Exception("系統執行錯誤，錯誤代碼：" + t3900);
            }
            return "Process Completed";
        } else {
            throw new Exception("外部系統無效回應");
        }
    }
	
	public BonusDTO.RedeemResponseDTO getRedeemable(RequestPOSBody requestBody) throws Exception {
	    // 1. 組裝請求 (維持原本結構)
	    Map<String, Object> trans = new HashMap<>();
	    trans.put("T0100", "0200");
	    trans.put("T0200", requestBody.getCardNo());
	    trans.put("T0300", "240000");
	    trans.put("T1200", GetDateTime.getTime(""));
	    trans.put("T1300", GetDateTime.getTodayDateW(""));
	    trans.put("T4100", requestBody.getDeviceId());
	    trans.put("T4200", "BY001");
	    trans.put("T4221", "APP");
	    trans.put("T5503", "APP");
	    trans.put("T5509", "A");

	    Map<String, Object> allData = Collections.singletonMap("Trans", trans);

	    // 2. 呼叫 API
	    Map<String, Object> response = restTemplate.postForObject(env.getProperty("GIFT_WS_URL"), allData, Map.class); //dc-

	    // 3. 檢查回應結構與 T3900 狀態碼
	    if (response == null || !response.containsKey("Trans")) throw new Exception("外部系統無效回應");
	    Map<String, Object> transRes = (Map<String, Object>) response.get("Trans");
	    if (!"00".equals(transRes.get("T3900"))) {
	        throw new Exception("系統執行錯誤，錯誤代碼：" + transRes.get("T3900"));
	    }

	    // 4. 使用 Builder 初始化回應物件，並透過 Stream 進行解析
	    return BonusDTO.RedeemResponseDTO.builder()
	            .pointList(parsePoints(transRes))
	            .prizeList(parsePrizes(transRes))
	            .build();
	}

	// 提取解析邏輯到 private 方法，提升程式碼可讀性
	private List<PointDTO> parsePoints(Map<String, Object> transRes) {
	    if (!transRes.containsKey("T5532")) return Collections.emptyList();
	    
	    // 顯式宣告 List 的泛型
	    List<Map<String, Object>> list = (List<Map<String, Object>>) transRes.get("T5532");
	    
	    return list.stream()
	            .map((Map<String, Object> m) -> {
	            return PointDTO.builder() // 在 lambda 中明確指定參數型別
	                    .pointCode((String) m.get("T553201"))
	                    .point(m.get("T553204") != null ? ((Number) m.get("T553204")).doubleValue() : 0.0)
	                    .build();
	            })
	            .collect(Collectors.toList());
	}

	private List<PrizeDTO> parsePrizes(Map<String, Object> transRes) {
	    if (!transRes.containsKey("T5578")) return Collections.emptyList();
	    List<Map<String, Object>> list = (List<Map<String, Object>>) transRes.get("T5578");
	    return list.stream().map(m -> PrizeDTO.builder()
	            .activityCode((String) m.get("T557801"))
	            .prizeCode((String) m.get("T557802"))
	            .prizeName((String) m.get("T557803"))
	            .pointCode((String) m.get("T557805"))
	            .needPoint(((Number) m.getOrDefault("T557806", 0)).doubleValue())
	            .activityName((String) m.get("T557810"))
	            .prizeType((String) m.get("T557811"))
	            .redeemableQty(((Number) m.getOrDefault("T557814", 0)).doubleValue())
	            .build())
	            .collect(Collectors.toList());
	}
	
	public BonusDTO.RedeemResponseDTO processRedeem(RequestPOSBody requestBody) throws Exception {
	    // 1. 組裝請求
	    Map<String, Object> trans = new HashMap<>();
	    String sTime = GetDateTime.getTime("");
	    trans.put("T0100", "0200");
	    trans.put("T0200", requestBody.getCardNo());
	    trans.put("T0300", "607090");
	    trans.put("T1200", sTime);
	    trans.put("T1300", GetDateTime.getTodayDateW(""));
	    trans.put("T4100", requestBody.getDeviceId());
	    trans.put("T4200", "BY001");
	    trans.put("T4221", "APP");
	    trans.put("T5503", "APP");
	    trans.put("T5505", sTime);
	    trans.put("T5509", "A");

	    // 轉換 PrizeBean 列表為外部系統需要的 JSON 結構
	    List<Map<String, Object>> prizeList = requestBody.getPrizeList().stream().map(p -> {
	        Map<String, Object> jo = new HashMap<>();
	        jo.put("T557901", p.getActivityCode());
	        jo.put("T557902", p.getActivityCode());
	        jo.put("T557903", String.valueOf(p.getRedeemableQty().intValue()));
	        jo.put("T557904", "");
	        jo.put("T557905", "");
	        jo.put("T557910", p.getPrizeType());
	        return jo;
	    }).collect(Collectors.toList());
	    
	    trans.put("T5579", prizeList);
	    Map<String, Object> allData = Collections.singletonMap("Trans", trans);

	    // 2. 發送請求
	    Map<String, Object> response = restTemplate.postForObject(env.getProperty("GIFT_WS_URL"), allData, Map.class);

	    // 3. 解析回應 (包含 Coupon 與 Prize)
	    if (response == null || !response.containsKey("Trans")) throw new Exception("無回應");
	    Map<String, Object> transRes = (Map<String, Object>) response.get("Trans");
	    if (!"00".equals(transRes.get("T3900"))) throw new Exception("錯誤代碼：" + transRes.get("T3900"));

	    return BonusDTO.RedeemResponseDTO.builder()
	            .couponList(parseCoupons(transRes))
	            .prizeList(parseRedeemPrizes(transRes))
	            .build();
	}

	private List<BonusDTO.PrizeDTO> parseRedeemPrizes(Map<String, Object> transRes) {
        if (!transRes.containsKey("T5579")) return Collections.emptyList();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) transRes.get("T5579");
        
        return list.stream().map(m -> BonusDTO.PrizeDTO.builder()
                .activityCode((String) m.get("T557901"))
                .prizeCode((String) m.get("T557902"))
                .redeemableQty(((Number) m.getOrDefault("T557903", 0)).doubleValue())
                .prizeName((String) m.get("T557904"))
                .activityName((String) m.get("T557905"))
                .prizeType((String) m.get("T557910"))
                .price(((Number) m.getOrDefault("T557911", 0)).doubleValue())
                .build())
                .collect(Collectors.toList());
    }

    private List<BonusDTO.CouponDTO> parseCoupons(Map<String, Object> transRes) {
        if (!transRes.containsKey("T5549")) return Collections.emptyList();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) transRes.get("T5549");
        
        return list.stream().map(m -> BonusDTO.CouponDTO.builder()
                .couponNO((String) m.get("T554901"))
                .startDate((String) m.get("T554902"))
                .value(((Number) m.getOrDefault("T554905", 0)).doubleValue())
                .endDate((String) m.get("T554918"))
                .build())
                .collect(Collectors.toList());
    }
    
    public void processRefund(RequestPOSBody requestBody) throws Exception {
        // 1. 組裝請求
        Map<String, Object> trans = new HashMap<>();
        String sTime = GetDateTime.getTime("");
        trans.put("T0100", "0200");
        trans.put("T0200", requestBody.getCardNo());
        trans.put("T0201", requestBody.getCreditCard()); // 新增信用卡資訊
        trans.put("T0300", "629070");
        trans.put("T1200", sTime);
        trans.put("T1300", GetDateTime.getTodayDateW(""));
        trans.put("T4100", requestBody.getDeviceId());
        trans.put("T4200", "BY001");
        trans.put("T5507", requestBody.getInvoiceNO());
        trans.put("T5509", "0");

        // 轉換 PrizeDTO 列表為外部系統需要的 JSON 結構
        List<Map<String, Object>> prizeList = requestBody.getPrizeList().stream().map(p -> {
            Map<String, Object> jo = new HashMap<>();
            jo.put("T557901", p.getActivityCode());
            jo.put("T557902", p.getPrizeCode());
            jo.put("T557903", String.valueOf(p.getRedeemableQty()));
            jo.put("T557904", "");
            jo.put("T557905", "");
            jo.put("T557906", "R");
            // 處理 Coupon 列表轉字串邏輯
            jo.put("T557907", p.getCouponNo() != null ? p.getCouponNo().toString() : "");
            jo.put("T557910", p.getPrizeType());
            jo.put("T557911", p.getPrice());
            return jo;
        }).collect(Collectors.toList());
        
        trans.put("T5579", prizeList);
        Map<String, Object> allData = Collections.singletonMap("Trans", trans);

        // 2. 發送請求
        Map<String, Object> response = restTemplate.postForObject(env.getProperty("GIFT_WS_URL"), allData, Map.class);

        // 3. 檢查回應
        if (response == null || !response.containsKey("Trans")) throw new Exception("無回應");
        Map<String, Object> transRes = (Map<String, Object>) response.get("Trans");
        if (!"00".equals(transRes.get("T3900"))) throw new Exception("系統執行錯誤，錯誤代碼：" + transRes.get("T3900"));
    }
    
    public BonusDTO.RedeemResponseDTO processCheckInvoice(RequestPOSBody requestBody) throws Exception {
        // 1. 組裝請求
        Map<String, Object> trans = new HashMap<>();
        String sTime = GetDateTime.getTime("");
        trans.put("T0100", "0200");
        trans.put("T0200", requestBody.getCardNo());
        trans.put("T0300", "240000");
        trans.put("T1200", sTime);
        trans.put("T1300", GetDateTime.getTodayDateW(""));
        trans.put("T4100", requestBody.getDeviceId());
        trans.put("T4200", "BY001");
        trans.put("T4221", "APP");
        trans.put("T5503", "APP");
        trans.put("T5509", "A");

        Map<String, Object> allData = Collections.singletonMap("Trans", trans);

        // 2. 呼叫 API
        Map<String, Object> response = restTemplate.postForObject(env.getProperty("GIFT_WS_URL"), allData, Map.class);

        // 3. 解析回應
        if (response == null || !response.containsKey("Trans")) throw new Exception("無回應");
        Map<String, Object> transRes = (Map<String, Object>) response.get("Trans");
        if (!"00".equals(transRes.get("T3900"))) throw new Exception("錯誤代碼：" + transRes.get("T3900"));

        return BonusDTO.RedeemResponseDTO.builder()
                .pointList(parsePoints(transRes))
                .prizeList(parseRedeemPrizes(transRes))
                .build();
    }
    

}