package com.beyond.surrounding.pos.controller;

import com.beyond.surrounding.pos.client.PrivilegeServiceFeignClient;
import com.beyond.surrounding.pos.service.PrivilegeService;
import com.beyond.surrounding.util.CryptUtil;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyond.surrounding.bean.ResponseBean;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos/Privilege")
@RequiredArgsConstructor
public class PrivilegeController {
	
	private final Environment env;
	private final PrivilegeService privilegeService;
	private final PrivilegeServiceFeignClient privilegeServiceClient;

	@GetMapping(value = "/search",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean search(
			@RequestParam String brandCode,
			@RequestParam String storeCode,
			@RequestParam String sourceUuid,
			@RequestParam String type,
			@RequestParam String identity,
			@RequestParam String privilegeCode) {
		
		try {
			log.info("search : storeCode -> {}, brandCode -> {}, sourceUuid -> {}, identity -> {}, privilegeCode -> {}", 
					storeCode, brandCode, sourceUuid, identity, privilegeCode);
			
			ResponseBean bean = new ResponseBean();

			// 1. 呼叫特權服務取得 JSON 字串回應
			String response = privilegeService.search(env, type, identity, brandCode, storeCode, sourceUuid, privilegeCode);

			// 2. 使用 Spring Boot 內建的 Jackson ObjectMapper 解析 JSON
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.JsonNode responseObj = mapper.readTree(response);
			
			// 解析 rcrm 節點
			com.fasterxml.jackson.databind.JsonNode rcrm = responseObj.get("rcrm");
			String rc = rcrm.get("RC").asText();
			String rm = rcrm.get("RM").asText();

			// 3. 檢查業務狀態代碼是否為成功 (C01)
			if (!"C01".equals(rc)) {
				bean.setCode(rc);
				bean.setMessage(rm);
				log.error("brandCode -> {}, {} {}", brandCode, rc, rm);
				return bean;
			}

			// 4. 解析 results 節點並組合 Title 訊息
			com.fasterxml.jackson.databind.JsonNode results = responseObj.get("results");
			com.fasterxml.jackson.databind.JsonNode privilegeInformation = results.get("privilege_information");
			
			String title = privilegeInformation.get("title").asText() + ": 餘"
					+ privilegeInformation.get("redeemable_times").asInt() + "次";
			
			// 註：請確保你的 ResponseBean 內有定義 title 屬性與其 Getter/Setter
			bean.setCar(title); // 若新 ResponseBean 沒 title 欄位，依原 Bean 結構暫時塞入可用欄位(如 car)，或請在 ResponseBean 補上 title 屬性
			
			bean.setCode(ErrCodeConst.finished);
			bean.setMessage(ErrCodeConst.finished_message);
			return bean;
			
		} catch (Exception e) {
			log.error("特權查詢作業失敗: {}", e.getMessage(), e);
			
			// 完美對齊雙格式：直接回傳 errorBean，Spring Boot 會自動根據 Accept 標頭轉成 XML 或 JSON 錯誤外觀
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value())); // "417"
			errorBean.setMessage("特權查詢作業失敗: " + e.getMessage());
			return errorBean;
		}
	}
	
	@GetMapping(value = "/redeem",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
				 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public String redeem(String type, String identity, String transactionId, String transactionType,
		            String brandCode, String storeCode, String sourceUuid, String privilegeCode, Integer quantity) {
		        try {
		            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		            
		            // 建立 member_identity 節點
		            com.fasterxml.jackson.databind.node.ObjectNode memberIdentity = mapper.createObjectNode();
		            memberIdentity.put("type", type);
		            memberIdentity.put("identity", identity);

		            // 建立 request_parameter 節點
		            com.fasterxml.jackson.databind.node.ObjectNode requestParameter = mapper.createObjectNode();
		            requestParameter.put("transaction_id", transactionId);
		            requestParameter.put("transaction_type", transactionType); // normal, cancel
		            requestParameter.put("brand_code", brandCode);
		            requestParameter.put("store_code", storeCode);
		            requestParameter.put("source_uuid", sourceUuid);
		            requestParameter.put("privilege_code", privilegeCode);
		            requestParameter.put("quantity", quantity != null ? quantity : 1);
		            requestParameter.set("member_identity", memberIdentity);

		            // 建立最外層 jo 節點
		            com.fasterxml.jackson.databind.node.ObjectNode jo = mapper.createObjectNode();
		            jo.set("request_parameter", requestParameter);
		            
		            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		            jo.put("timestamp", sdf.format(new java.util.Date()));

		            String joJsonString = mapper.writeValueAsString(jo);
		            log.info("Up(redeem)：{}", joJsonString);

		            // Base64 編碼
		            String sPlayLod = java.util.Base64.getEncoder().encodeToString(
		                    joJsonString.getBytes(java.nio.charset.StandardCharsets.UTF_8)
		            );
		            
		            // HmacSHA256 簽章
		            String sSignature = CryptUtil.toHmacSHA256(sPlayLod, env.getProperty("SubscriptKey"));
		            
		            // 封裝成 sign 外殼
		            com.fasterxml.jackson.databind.node.ObjectNode joAll = mapper.createObjectNode();
		            joAll.put("sign", sPlayLod + "." + sSignature);

		            String finalRequestBody = mapper.writeValueAsString(joAll);
		            log.info("Encode(redeem)：{}", finalRequestBody);

		            // 發送 Feign 請求
		            String appId = env.getProperty("SubscriptAppId");
		            String response = privilegeServiceClient.executeRedeem(appId, finalRequestBody);

		            log.info("Down(redeem)：{}", response);
		            return response;

		        } catch (Exception e) {
		            log.error("特權扣除通訊異常: {}", e.getMessage(), e);
		            throw new RuntimeException("特權扣除服務異常: " + e.getMessage());
		        }
		    }
	
	@GetMapping(value = "/transaction_check",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
				 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean transaction_check(
			@RequestParam String brandCode,
			@RequestParam String type,
			@RequestParam String transactionId) {

		try {
			log.info("transaction_check : brandCode -> {}, type -> {}, transactionId -> {}", brandCode, type, transactionId);
			
			ResponseBean bean = new ResponseBean();
			
			// 1. 轉換查詢型態參數 (對齊舊系統商業邏輯)
			String convertedType = type;
			if ("tid".equals(type)) {
				convertedType = "transaction_id";
			} else if ("mmrm".equals(type)) {
				convertedType = "mmrm_tid";
			}

			// 2. 呼叫服務層取得中台 JSON 字串回應
			String response = privilegeService.transactionCheck(convertedType, transactionId);

			// 3. 使用 Jackson 內建解析器
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.JsonNode responseObj = mapper.readTree(response);
			
			// 檢查 rcrm 狀態
			com.fasterxml.jackson.databind.JsonNode rcrm = responseObj.get("rcrm");
			String rc = rcrm.get("RC").asText();
			String rm = rcrm.get("RM").asText();

			if (!"C01".equals(rc)) {
				bean.setCode(rc);
				bean.setMessage(rm);
				log.error("transaction_check 失敗: brandCode -> {}, {} {}", brandCode, rc, rm);
				return bean;
			}

			// 4. 解析 results 內部深層的交易節點資料
			com.fasterxml.jackson.databind.JsonNode transactionNode = responseObj
					.path("results")
					.path("data")
					.path("transaction");

			// 5. 將解析出的數值塞回 ResponseBean 
			// 註：請確保你的 ResponseBean 內有這些對應欄位，或依實際欄位結構調整
			bean.setUser_id(transactionNode.path("store_code").asText());        // 舊：setStoreCode
			bean.setCard_id(transactionNode.path("transaction_id").asText());    // 舊：setTransactionId
			bean.setCard_type(transactionNode.path("transaction_type").asText());// 舊：setTransactionType
			
			com.fasterxml.jackson.databind.JsonNode memberIdentityNode = transactionNode.path("member_identity");
			bean.setIdentity(memberIdentityNode.path("identity").asText());      // 舊：setIdentity
			
			bean.setiAmt(transactionNode.path("quantity").asInt());              // 舊：setQuantity

			// 6. 設定成功代碼
			bean.setCode(ErrCodeConst.finished);
			bean.setMessage(ErrCodeConst.finished_message);
			return bean;

		} catch (Exception e) {
			log.error("交易狀態檢查作業失敗: {}", e.getMessage(), e);
			
			// 完美支援雙格式：直接回傳 errorBean，Spring 會根據 Accept 標頭自動轉 XML 或 JSON
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value())); // "417"
			errorBean.setMessage("交易狀態檢查作業失敗: " + e.getMessage());
			return errorBean;
		}
	}
	
	@GetMapping(value = "/available_list",
			produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
						 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean available_list(
			@RequestParam String brandCode,
			@RequestParam String storeCode,
			@RequestParam String type,
			@RequestParam String identity) {

		try {
			log.info("availableList : storeCode -> {}, brandCode -> {}, identity -> {}", storeCode, brandCode, identity);
			
			ResponseBean bean = new ResponseBean();

			// 1. 呼叫服務層取得中台 JSON 字串回應
			String response = privilegeService.availableList(brandCode, storeCode, type, identity);

			// 2. 使用 Jackson 解析回應
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			com.fasterxml.jackson.databind.JsonNode responseObj = mapper.readTree(response);
			
			// 檢查 rcrm 狀態
			com.fasterxml.jackson.databind.JsonNode rcrm = responseObj.get("rcrm");
			String rc = rcrm.get("RC").asText();
			String rm = rcrm.get("RM").asText();

			if (!"C01".equals(rc)) {
				bean.setCode(rc);
				bean.setMessage(rm);
				log.error("availableList 失敗: brandCode -> {}, {} {}", brandCode, rc, rm);
				return bean;
			}

			// 3. 解析 results 內部的 privilege_information 陣列
			com.fasterxml.jackson.databind.JsonNode privilegeInfoArray = responseObj
					.path("results")
					.path("privilege_information");

			java.util.List<String> titleList = new java.util.ArrayList<>();

			if (privilegeInfoArray.isArray()) {
				for (com.fasterxml.jackson.databind.JsonNode node : privilegeInfoArray) {
					String title = node.path("title").asText();
					
					// 處理可兌換次數文字組合邏輯
					String info = title;
					if (!node.path("redeemable_times").isMissingNode() && !node.path("redeemable_times").isNull()) {
						info += ": 餘" + node.path("redeemable_times").asInt() + "次";
					}
					titleList.add(info);
				}
			}

			// 4. 將結果塞入新 ResponseBean
			// 註：因為 ResponseBean 是扁平化結構，如果沒有定義 List<AvailableInfo>，
			// 通常會將組合好的特權字串用逗號、或換行符號拼接後放入 message 或特定欄位(如 upload_message)。
			// 這裡提供兩種塞值策略，請依你的 ResponseBean 實際設計選擇：
			
			// 策略 A：若你的 ResponseBean 其實就是舊的 ResponseDataBean 複製品且有支援此欄位：
			// bean.setAvailableInfo(availableInfos); 
			
			// 策略 B：若新 Bean 沒有集合欄位，將文字清單串接起來放入現有欄位回傳：
			if (!titleList.isEmpty()) {
				bean.setUpload_message(String.join(", ", titleList)); 
			}

			// 5. 設定成功狀態
			bean.setCode(ErrCodeConst.finished);
			bean.setMessage(ErrCodeConst.finished_message);
			return bean;

		} catch (Exception e) {
			log.error("取得可用特權清單作業失敗: {}", e.getMessage(), e);
			
			// 完美支援雙格式：直接回傳 errorBean，Spring 會根據 Accept 標頭自動轉換外觀
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value())); // "417"
			errorBean.setMessage("取得可用特權清單作業失敗: " + e.getMessage());
			return errorBean;
		}
	}
	
}