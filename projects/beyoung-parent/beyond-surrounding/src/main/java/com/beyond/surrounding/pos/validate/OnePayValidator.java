package com.beyond.surrounding.pos.validate;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import com.beyond.surrounding.pos.exception.BusinessException;
import com.beyond.surrounding.util.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j 
@Component
public class OnePayValidator {

    /**
     * 校驗支付類型並解碼 Key
     * 將原本 Controller 的校驗邏輯與解碼邏輯合二為一
     */
    public String validateAndDecode(String moPayType, String oneTimeKey, String orderId) {
    	
    	
        // 1. 檢查支付類型是否支援
        MoPayType type = MoPayType.fromCode(moPayType);
        if (type == null) {
            throw new BusinessException(Constants.BEYONDPAY_UNSUPPORTED_CODE, Constants.BEYONDPAY_UNSUPPORTED_MSG);
        }

        // 2. Hex 解碼 (包裝在 try-catch 中以處理格式錯誤)
        String decodedKey;
        try {
            decodedKey = new String(Hex.decodeHex(oneTimeKey.toCharArray()));
            log.info("解碼後的 Key 為: {}, 長度為: {}", decodedKey, decodedKey.length());
        } catch (Exception e) {
            throw new BusinessException(Constants.BEYONDPAY_CODE_MISMATCH, "OneTimeKey 解碼失敗");
        }

        // 3. 檢查條碼規則
        if (!type.isValid(decodedKey)) {
            String errorMsg = Constants.BEYONDPAY_CODE_MISMATCH_MSG + "，此條碼並非 " + moPayType;
            throw new BusinessException(Constants.BEYONDPAY_CODE_MISMATCH, errorMsg);
        }

        return decodedKey;
    }
    
    // 枚舉保持不變
    public enum MoPayType {
        BEYOND_PAY("BeyondPay", 18, "BP"),
        EASY_PAY("EasyPay", 20, "99"),
        JKOS_PAY("JkosPay", 18, "22"),
        PI_PAY("PiPay", 18, "PI"),
        TS_PAY("TSPay", 18, "TS");

        private final String code;
        private final int length;
        private final String prefix;

        MoPayType(String code, int length, String prefix) {
            this.code = code;
            this.length = length;
            this.prefix = prefix;
        }

        public static MoPayType fromCode(String code) {
            for (MoPayType type : values()) {
                if (type.code.equals(code)) return type;
            }
            return null;
        }

        public boolean isValid(String decodedKey) {
            return decodedKey != null && 
                   decodedKey.length() == this.length && 
                   decodedKey.startsWith(this.prefix);
        }
    }

}