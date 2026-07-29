package com.beyoung.surrounding.pos2.controller;

import com.beyoung.surrounding.pos.service.ReadingSpacePosService;
import com.beyoung.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.app.entity.LPK_FILE;
import com.beyoung.surrounding.member.repository.MemberRepository;

@Slf4j 
@RestController
@RequestMapping("/Surrounding/rest/pos2/ReadingSpace")
@RequiredArgsConstructor
public class ReadingSpacePos2Controller {

	private final ReadingSpacePosService readingSpacePosService;
	private final MemberRepository memberRepository;
	
	@GetMapping(value = "/purchase",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean purchase(
            @RequestParam String center,
            @RequestParam String counterID,
            @RequestParam String posID,
            @RequestParam String cardNO,
            @RequestParam Integer price) throws JSONException {

        try {
            return readingSpacePosService.purchase(
            		center,
            		counterID,
            		posID,
            		cardNO,
            		price
            );

		} catch (Exception e) {
			log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
			
			// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
			errorBean.setMessage("Reading Space 連線測試作業失敗 " + e.getMessage());
			return errorBean;	
			
		}
	}


	@GetMapping(value = "/enter/{center}/{cardNO}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
            	 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean enter(
            @PathVariable String center,
            @PathVariable String cardNO) {
        
        try {
            ResponseBean bean = new ResponseBean();
            
            // 1. 查會員 (複用你之前的 memberRepository 或是成員 DAO)
            // 這裡注意對應你專案中實際的 Entity 名稱（LpkFile 或 LPK_FILE）
            LPK_FILE entity = memberRepository.getMemberByCardId(cardNO);
            
            if (entity.getLpk01() == null) {
                bean.setCode(ErrCodeConst.pos_rs_not_found);
                bean.setMessage(ErrCodeConst.pos_rs_not_found_message);    
                log.info("ReadingSpace enter : cardNO -> " + cardNO + " " + ErrCodeConst.pos_rs_not_found_message);
                return bean;
            }
            
            log.info("ReadingSpace enter : cardNO -> " + cardNO + ", Name -> " + entity.getLpk02() + " 進入4步書房");
            
            // 2. 呼叫服務層處理進入邏輯
            bean = readingSpacePosService.enter(center, cardNO, entity);
            
            return bean;
            
		} catch (Exception e) {
			log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
			
			// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
			errorBean.setMessage("Reading Space 連線測試作業失敗 " + e.getMessage());
			return errorBean;	
			
		}
	}

			
	@GetMapping(value = "/exit/{center}/{cardNO}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean exit(@PathVariable String center,
            				 @PathVariable String cardNO) {
        
        try {
            ResponseBean bean = new ResponseBean();
            
            // 1. 透過卡號查詢會員
            LPK_FILE entity = memberRepository.getMemberByCardId(cardNO);
            
            if (entity.getLpk01() == null) {
                bean.setCode(ErrCodeConst.pos_rs_not_found);
                bean.setMessage(ErrCodeConst.pos_rs_not_found_message);    
                log.info("ReadingSpace exit : cardNO -> " + cardNO + " " + ErrCodeConst.pos_rs_not_found_message);
                return bean;
            }
            
            log.info("ReadingSpace exit : cardNO -> " + cardNO + ", Name -> " + entity.getLpk02());
            
            // 2. 呼叫服務層處理出場邏輯
            bean = readingSpacePosService.exit(center, cardNO, entity);
            
            return bean;
		} catch (Exception e) {
			log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
			
			// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
			errorBean.setMessage("Reading Space 連線測試作業失敗 " + e.getMessage());
			return errorBean;	
			
		}
	}

    		
	@GetMapping(value = "/refund/{center}/{invoiceNO}/{cardNO}/{refundDate}",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		         MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean refund(
			@PathVariable String center,
			@PathVariable String invoiceNO,
			@PathVariable String cardNO,
			@PathVariable String refundDate) {
		
		try {
			ResponseBean bean = new ResponseBean();
			
			// 防錯處理：將傳入的 center 參數進行數字化過濾（清除 C 等英文字母）
			if (center != null) {
				center = center.replaceAll("[^0-9]", ""); // 確保 "C001" 轉為 "001"
			}
			
			// 1. 透過卡號查詢會員
			LPK_FILE entity = memberRepository.getMemberByCardId(cardNO);
			
			if (entity.getLpk01() == null) {
				bean.setCode(ErrCodeConst.pos_rs_not_found);
				bean.setMessage(ErrCodeConst.pos_rs_not_found_message);	
				log.info("ReadingSpace refund : cardNO -> " + cardNO + " " + ErrCodeConst.pos_rs_not_found_message);
				return bean;
			}
			
			log.info("ReadingSpace refund : cardNO -> " + cardNO + ", Name -> " + entity.getLpk02() + " refundDate -> " + refundDate);
			
			// 2. 呼叫服務層處理退款邏輯
			bean = readingSpacePosService.refund(center, invoiceNO, cardNO, refundDate, entity);
			
			return bean;
		} catch (Exception e) {
			log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
			
			// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
			errorBean.setMessage("Reading Space 連線測試作業失敗 " + e.getMessage());
			return errorBean;	
			
		}
	}
		
	@GetMapping(value = "/connectTest",
	produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
				 MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" })
	public ResponseBean connectTest() {
		try {
			// log.info("Connect Test");
			ResponseBean bean = new ResponseBean();
			bean.setCode(ErrCodeConst.finished);
			bean.setMessage(ErrCodeConst.finished_message);
			return bean;
		} catch (Exception e) {
			log.error("Reading Space 連線測試作業失敗: {}", e.getMessage(), e);
			
			// 優化點：直接回傳 ResponseBean，由 Spring Boot 自動決定轉成 XML 或 JSON 錯誤訊息
			ResponseBean errorBean = new ResponseBean();
			errorBean.setCode(String.valueOf(org.springframework.http.HttpStatus.EXPECTATION_FAILED.value()));
			errorBean.setMessage("Reading Space 連線測試作業失敗 " + e.getMessage());
			return errorBean;	
			
		}
	}
	
}