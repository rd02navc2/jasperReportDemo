package com.beyond.surrounding.pos2.controller;

import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.pos2.service.MemberPos2Service;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("pos2MemberController")
@RequestMapping("/Surrounding/rest/pos2/Member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberPos2Service memberPos2Service;

	@GetMapping(value = "/getMemberByCardID/{cardID}",
		    produces = { 
		        MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
		        MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8" 
		    })
		public ResponseBean getMemberByCardID(@PathVariable String cardID) {
		    try {
		        log.info(String.format("POS2 getMemberByCardID ：Card ID ->%s", cardID));
		        ResponseBean bean = new ResponseBean();
		        bean.setCode(ErrCodeConst.finished);
		        bean.setMessage(ErrCodeConst.finished_message);

		        // 1. 驗證特定卡號前綴
		        if (cardID.startsWith("7708")) {
		            boolean isValidate = memberPos2Service.isValidate(cardID);
		            if (!isValidate) {
		                log.info(String.format("POS2 isValidate Response：%s 找不到會員卡或者不是有效卡", cardID));
		                bean.setCode(ErrCodeConst.not_validate);
		                bean.setMessage(ErrCodeConst.not_validate_message);
		                return bean;
		            }
		        }

		        // 2. 獲取會員實體資料
		        LPK_FILE entity = memberPos2Service.getMemberByCardID(cardID);
		        if (entity == null || entity.getLpk04() == null) {
		            log.info(String.format("POS2 getMemberByCardID Response：%s 不存在", cardID));
		            bean.setCode(ErrCodeConst.not_found);
		            bean.setMessage(ErrCodeConst.not_found_message);
		            return bean;
		        }

		        // 3. 姓名去識別化處理 (遮罩)
		        StringBuilder myName = new StringBuilder(entity.getLpk04().trim());
		        if (myName.length() >= 2) {
		            myName.replace(1, 2, "*");
		        }

		        // 4. 資料映射至 ResponseBean
		        bean.setUser_id(entity.getLpk01());
		        bean.setUser_name(myName.toString());
		        bean.setIdentity(entity.getLpk03());
		        bean.setCard_id(entity.getLpj03());
		        bean.setMobile(entity.getLpk18());
		        bean.setTotal_point(entity.getLpj12());
		        bean.setBase_bet(entity.getTaLpj01());
		        bean.setPre_point(entity.getTaLpj02());
		        bean.setThis_point(entity.getTaLpj03());
		        bean.setCard_vip("62".equals(entity.getLpkud02()) ? "VIP" : ("66".equals(entity.getLpkud02()) ? "黑卡" : ""));
		        bean.setCard_type("A001".equals(entity.getLpk14()) ? "橘卡" : ("A002".equals(entity.getLpk14()) ? "兒童卡" : ""));
		        
		        log.info(String.format("POS2 getMemberByCardID Response：Card ID ->%s, UserName ->%s", cardID, myName.toString()));
		        
		        return bean;
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

		
}