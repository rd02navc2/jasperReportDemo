package com.beyond.surrounding.ec.service;

import com.beyond.surrounding.ec.client.TaishinFeignClient;
import com.beyond.surrounding.ts.bean.TSResponseBean;
import com.beyond.surrounding.util.ErrCodeConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinistryFinanceService {

    private final TaishinFeignClient taishinFeignClient;
    // private final MinistryFinanceRepository mfRepository; //  未來若要用 JPA 儲存，直接注入 Repository

    @Transactional(rollbackFor = Exception.class) // 若有 DB 搬移可加事務
    public TSResponseBean verifyLPProcess(String s_mid, String ret_code, String tx_type, 
                                          String order_no, String ret_msg, String auth_id_resp, String carrierId2) throws Exception {
        
        log.info("Ministry Finance VerifyLP Service 開始："
                + "s_mid -> {}, ret_code -> {}, tx_type -> {}, order_no -> {}, ret_msg -> {}, auth_id_resp -> {}, carrierId2 -> {}",
                s_mid, ret_code, tx_type, order_no, ret_msg, auth_id_resp, carrierId2);

        //  如果原本註解掉的 DB 更新要啟用，直接用 Spring Data JPA 操作：
        // mfRepository.updateOrderResult(order_no, "1", ret_code, ret_msg);

        // 1. 組裝 JSON Body
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("ts_order_no", order_no);
        jsonParam.put("ret_code", ret_code);
        jsonParam.put("ret_msg", ret_msg);

        log.info("Auth PostBack Api2EC : {}", order_no);

        // 2. 透過更乾淨的 Feign 呼叫遠端台新端點
        String responseBody = taishinFeignClient.postBackToTaishin(jsonParam.toString());
        log.info("Auth TS2Api : {}", responseBody);

        // 3. 解析台新回傳的結果
        JSONObject retJson = new JSONObject(responseBody);
        JSONObject params = retJson.optJSONObject("params");
        
        if (params == null || !"00".equals(params.optString("ret_code"))) {
            String errCode = params != null ? params.optString("ret_code") : "99";
            String errMsg = (params != null && params.has("ret_msg")) ? params.optString("ret_msg") : "台新API回覆錯誤";
            throw new Exception(errCode + " " + errMsg);
        }

        // 4. 回傳成功的 Bean
        TSResponseBean bean = new TSResponseBean();
        bean.setCode(ErrCodeConst.finished);
        bean.setMessage(ErrCodeConst.finished_message);
        return bean;
    }
}