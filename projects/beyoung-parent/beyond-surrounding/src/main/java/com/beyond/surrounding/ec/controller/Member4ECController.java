package com.beyond.surrounding.ec.controller;

import com.beyond.surrounding.ec.bean.RequestBody;
import com.beyond.surrounding.ec.service.Member4ECService;
import com.beyond.surrounding.app.entity.LPJ_FILE;
import com.beyond.surrounding.app.entity.LPK_FILE;
import com.beyond.surrounding.app.entity.LSM_FILE;
import com.beyond.surrounding.bean.ResponseBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/ec/Member") // 依實際需求調整路徑
@RequiredArgsConstructor
public class Member4ECController {

    private final Member4ECService memberService;
    @GetMapping(value = "/getMemberByID/{id}", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public LPK_FILE getMemberById(@PathVariable String id) throws JSONException {
        try {
            return memberService.getMemberById(id);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @PostMapping(value = "/addTempMember", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean addTempMember(@org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) throws JSONException {
        try {
            log.info("EC：addTempMember : center -> {}, cardNo -> {}", requestBody.getCenter(), requestBody.getCardNo());
            return memberService.addTempMember(requestBody);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @PostMapping(value = "/updMemberByID", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean updateMemberById(@org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) throws JSONException {
        try {
            return memberService.updateMemberById(requestBody);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @GetMapping(value = "/getPointByID/{id}", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public LPJ_FILE getPointById(@PathVariable String id) throws JSONException {
        try {
            return memberService.getPointById(id);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @GetMapping(value = "/getPointHistByID/{id}", produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public List<LSM_FILE> getPointHistById(
            @PathVariable String id,
            @RequestParam String startDate,
            @RequestParam String endDate) throws JSONException {
        try {
            return memberService.getPointHistById(id, startDate, endDate);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @PostMapping(value = "/processPoint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean processPoint(@org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) throws JSONException {
        try {
            // 呼叫 Service 層處理所有的扣點、SOAP 遠端請求及 Log 紀錄
            return memberService.processPoint(requestBody);      	      	
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @PostMapping(value = "/doHouseHold", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public LPJ_FILE doHouseHold(@org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) throws JSONException {
        try {
            log.info("EC：doHouseHold : tempMemberId -> {}, id -> {}", requestBody.getCardNo(), requestBody.getId());
            return memberService.doHouseHold(requestBody);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    @PostMapping(value = "/doFormal", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"})
    public ResponseBean doFormal(@org.springframework.web.bind.annotation.RequestBody RequestBody requestBody) throws JSONException {
        try {
            log.info("EC：doFormal : tempMemberId -> {}, userName -> {}, id -> {}", requestBody.getCardNo(), requestBody.getUserName(), requestBody.getId());
            return memberService.doFormal(requestBody);
        } catch (Exception e) {
            throw createException(e);
        }
    }

    private ResponseStatusException createException(Exception e) throws JSONException {
        log.error(e.getMessage(), e);
        JSONObject json = new JSONObject();
        json.put("code", HttpStatus.EXPECTATION_FAILED.value());
        json.put("message", e.getMessage());
        return new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, json.toString());
    }

}