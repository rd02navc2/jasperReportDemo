package com.beyoung.surrounding.pos2.controller;

import com.beyoung.surrounding.bean.APPResponseBean;
import com.beyoung.surrounding.bean.ResponseBean;
import com.beyoung.surrounding.bean.TransactionInResponseBean;
import com.beyoung.surrounding.util.ErrCodeConst;
import com.google.gson.JsonObject;
import com.beyoung.surrounding.pos2.service.UploadTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/Surrounding/rest/pos2/transaction")
@RequiredArgsConstructor
public class UploadTransactionController {

    private final UploadTransactionService transactionService;
    private final Environment env;

    @GetMapping(
            value = "/upload",
            produces = {
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"
            }
    )
    public ResponseBean upload(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String identity,
            @RequestParam(required = false) String storeCode,
            @RequestParam(required = false) String sourceUuid,
            @RequestParam(required = false) String invoiceNumber,
            @RequestParam(required = false) String invoiceRandomNumber,
            @RequestParam(required = false) Integer invoiceAmount,
            @RequestParam(required = false) String transactionDatetime,
            @RequestParam(required = false) String type) {

        try {
            // 1. 記錄 Log
            log.info(String.format(
                    "transactionId => %s, identity => %s, storeCode => %s, "
                            + "sourceUuid => %s, invoiceNumber => %s, invoiceRandomNumber => %s, "
                            + "invoiceAmount => %d, transactionDatetime => %s, type => %s",
                    transactionId, identity, storeCode, sourceUuid, invoiceNumber, 
                    invoiceRandomNumber, invoiceAmount, transactionDatetime, type));

            ResponseBean bean = new ResponseBean();
            
            // 2. 呼叫服務層
            APPResponseBean<String> appResponseBean = transactionService.upload(
                    transactionId, 
                    identity, 
                    storeCode, 
                    sourceUuid, 
                    invoiceNumber, 
                    invoiceRandomNumber, 
                    invoiceAmount,         // 傳入 Integer
                    transactionDatetime,   // 傳入 String
                    type,                 
                    env                   
            );
            
            // 3. 檢查回傳代碼是否成功
            if (appResponseBean.getRcrm() != null && !"C01".equalsIgnoreCase(appResponseBean.getRcrm().getRC())) {
                bean.setCode(appResponseBean.getRcrm().getRC());
                bean.setMessage(appResponseBean.getRcrm().getRM());
                
                log.error(String.format("上傳交易 失敗 transactionId -> %s , error_code -> %s , error_msg -> %s", 
                        transactionId, appResponseBean.getRcrm().getRC(), appResponseBean.getRcrm().getRM()));
                return bean;
            }

            // 4. 成功回傳
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            return bean;

        } catch (Exception e) {
            log.error("上傳交易發生系統異常: " + e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
    @GetMapping(
            value = "/upload/returned",
            produces = {
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"
            }
    )
    public ResponseBean returned(
            @RequestParam String transactionId,
            @RequestParam String sourceTransactionId, 
            @RequestParam String identity,
            @RequestParam String storeCode, 
            @RequestParam String invoiceAmount,
            @RequestParam String transactionDatetime,
            @RequestParam String type) {

        try {
            log.info(String.format(
                    "transactionId => %s,identity => %s,storeCode => %s,"
                            + "sourceTransactionId => %s ,invoiceAmount => %s,transactionDatetime => %s,"
                            + " type => %s",
                    transactionId, identity, storeCode, sourceTransactionId, invoiceAmount, transactionDatetime, type));

            ResponseBean bean = new ResponseBean();
            APPResponseBean<String> aPPResponseBean = transactionService.returned(env, transactionId, identity,
                    storeCode, invoiceAmount, transactionDatetime, sourceTransactionId, type);

            if (!aPPResponseBean.getRcrm().getRC().equalsIgnoreCase("C01")) {
            	bean.setCode(aPPResponseBean.getRcrm().getRC());
            	bean.setMessage(aPPResponseBean.getRcrm().getRM());
                log.error(String.format("退貨交易 失敗 transactionId -> %s , error_code -> %s ,error_msg -> %s", transactionId,
                        aPPResponseBean.getRcrm().getRC(), aPPResponseBean.getRcrm().getRM()));
                return bean;
            }

            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            return bean;
            
        } catch (Exception e) {
            log.error("上傳交易發生系統異常: " + e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
    @GetMapping(
            value = "/transactionCheck",
            produces = {
                MediaType.APPLICATION_XML_VALUE + ";charset=utf-8", 
                MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8"
            }
    )
    public TransactionInResponseBean transactionCheck(
            @RequestParam String type,
            @RequestParam String id) {
            
        List<String> typeList = Arrays.asList("tid", "mmrm");
        
        try {
            // 使用 Slf4j 預留位置寫法，比 String.format 更省記憶體
            log.info("type => {} , id => {}", type, id);
            
            TransactionInResponseBean bean = new TransactionInResponseBean();
            
            // 1. 驗證型態
            if (!typeList.contains(type)) {
                bean.setCode(ErrCodeConst.not_found);
                bean.setMessage("型態錯誤");
                log.error("查詢交易 失敗 型態錯誤 type -> {} , id -> {}", type, id);
                return bean;
            }

            // 2. 呼叫 Service (配合先前重構，移除了 method 中的 log 參數，改用 class 內建 log)
            APPResponseBean<JsonObject> aPPResponseBean = transactionService.transactionCheck(env, type, id);
            
            // 3. 檢查 API 回傳狀態
            if (!aPPResponseBean.getRcrm().getRC().equalsIgnoreCase("C01")) {
                bean.setCode(aPPResponseBean.getRcrm().getRC());
                bean.setMessage(aPPResponseBean.getRcrm().getRM());
                log.error("查詢交易 失敗 type -> {} , id -> {}, error_code -> {} , error_msg -> {}", 
                        type, id, aPPResponseBean.getRcrm().getRC(), aPPResponseBean.getRcrm().getRM());
                return bean;
            }
                
            // 4. 設定成功狀態
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            // 5. 解析 JSON 邏輯
            JsonObject results = aPPResponseBean.getResults();
            bean.setMmrmTid(results.get("mmrm_tid").getAsString());
            
            JsonObject txObject = results.get("transaction").getAsJsonObject();
            bean.setTransactionId(txObject.get("transaction_id").getAsString());
            bean.setTransactionType(txObject.get("transaction_type").getAsString());
            bean.setMemberType(txObject.get("member_identity").getAsJsonObject().get("type").getAsString());
            bean.setCardNumber(txObject.get("member_identity").getAsJsonObject().get("identity").getAsString());
            bean.setBrandCode(txObject.get("brand_code").getAsString());
            bean.setStoreCode(txObject.get("store_code").getAsString());
            bean.setSourceUuid(txObject.get("source_uuid").getAsString());
            bean.setInvoiceNumber(txObject.get("invoice_number").getAsString());
            bean.setInvoiceRandomNumber(txObject.get("invoice_random_number").getAsString());
            bean.setInvoiceAmount(txObject.get("invoice_amount").getAsInt());
            bean.setTransactionDatetime(txObject.get("transaction_datetime").getAsString());

            return bean;
            
        } catch (Exception e) {
            log.error("上傳交易發生系統異常: " + e.getMessage(), e);
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.node.ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("code", HttpStatus.EXPECTATION_FAILED.value());
            errorJson.put("message", e.getMessage());
            
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, errorJson.toString());
        }
    }
    
}