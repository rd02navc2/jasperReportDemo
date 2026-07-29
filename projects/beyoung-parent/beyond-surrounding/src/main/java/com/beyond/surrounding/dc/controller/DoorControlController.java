package com.beyond.surrounding.dc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.beyond.surrounding.bean.ResponseBean;
import com.beyond.surrounding.coupon.service.DoorControlService;
import com.beyond.surrounding.dc.entity.EMPLOYEE;
import com.beyond.surrounding.member.service.MemberService;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.app.entity.LPK_FILE;


@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/dc/DoorControl") // 建議加上統一的根路徑
@RequiredArgsConstructor
public class DoorControlController {

    private final DoorControlService doorControlService;
    private final MemberService memberService;
    
    @GetMapping(
        value = "/enter/{center}/{cardNO}",
        produces = { MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
        		     MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean enter(
            @PathVariable String center, // 修正：Spring WebMvc 應使用 @PathVariable
            @PathVariable String cardNO) throws JSONException {
        
        try {
            ResponseBean bean = new ResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            StringBuilder myName = new StringBuilder();

            // 1. 條件判斷：卡號長度為 4 且排除特定卡號（走向員工通道）
            if (cardNO.length() == 4 && !"5291".equals(cardNO) && !"1106".equals(cardNO)) {
                
                EMPLOYEE employee = doorControlService.getEmployee(cardNO);
                if (employee == null || employee.getCnName() == null) {
                    bean.setCode(ErrCodeConst.dc_hr_not_found);
                    bean.setMessage(ErrCodeConst.dc_hr_not_found_message);    
                    log.info("DoorControl enter : cardNO -> {} {}", cardNO, ErrCodeConst.dc_hr_not_found_message);
                    return bean;
                }
                myName.append(employee.getCnName());
                
            } else { // 走向會員/一般卡通道
                
                LPK_FILE member = memberService.getMemberByCardID3(cardNO);
                if (member == null || member.getLpk01() == null) {
                    bean.setCode(ErrCodeConst.dc_not_found);
                    bean.setMessage(ErrCodeConst.dc_not_found_message);    
                    log.info("DoorControl enter : cardNO -> {} {}", cardNO, ErrCodeConst.dc_not_found_message);
                    return bean;
                }
                
                // 檢查是否擁有無限權限(特權)，若無特權，則必須檢核是否為在職員工
                if (!doorControlService.isUnlimit(center, cardNO, member)) {
                    if (!doorControlService.isEmployee(member.getLpk03())) {
                        bean.setCode(ErrCodeConst.dc_hr_not_found);
                        bean.setMessage(ErrCodeConst.dc_hr_not_found_message);    
                        log.info("DoorControl enter : cardNO -> {} {}", cardNO, ErrCodeConst.dc_hr_not_found_message);
                        return bean;
                    }
                }
                
                if (member.getLpk04() != null) {
                    myName.append(member.getLpk04().trim());
                }
            }

            bean.setUser_name(myName.toString());
            log.info("DoorControl enter : cardNO -> {}, Name -> {} 進入總辦", cardNO, bean.getUser_name());
            
            return bean;

        } catch (Exception e) {
            log.error("usePoint Exception: ", e);
            
            // 建立錯誤的 JSON 回傳內容
            JSONObject errorJson = new JSONObject();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            // 對應原本的 WebApplicationException (EXPECTATION_FAILED = 417)
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString(), e);
        }
    }	
    
    
}