package com.beyond.surrounding.pos2.service;

import com.beyond.surrounding.bean.ActionResponseBean;
import com.beyond.surrounding.util.ActiveDirectory;
import com.beyond.surrounding.util.ErrCodeConst;
import com.beyond.surrounding.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.naming.ldap.LdapContext;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActionPos2Service {
	
	private final Environment env;
       
    @Transactional(readOnly = true)
    public ActionResponseBean checkBarcode(String center, String counterId, String posId, String barcode) {
        try {
            // 1. 條碼進行 Hex 解碼，改用小駝峰命名 decrypted
            String decrypted = new String(Hex.decodeHex(barcode.toCharArray()));
            
            log.info("POS2 checkBarcode Service : center -> {}, counterId -> {}, barcode -> {}({})", 
                    center, counterId, decrypted, barcode);
            
            // 2. 宣告回傳 Bean，改用小駝峰命名 bean
            ActionResponseBean bean = new ActionResponseBean();

            // 3. 核心條碼規則判定 (採用轉化後的變數)
            if (decrypted.length() == 8 && decrypted.startsWith("/") && StringUtil.checkCarrier(decrypted)) {
                bean.setsActionType("invoice_carrier");
                
            } else if (decrypted.length() == 11 && (decrypted.startsWith("7708") || decrypted.startsWith("APP") || decrypted.startsWith("TS") || decrypted.startsWith("EC"))) {
                bean.setsActionType("member_card");
                
            } else if (decrypted.length() == 18 && (
                    decrypted.startsWith("31") || decrypted.startsWith("32") || decrypted.startsWith("33") ||
                    decrypted.startsWith("34") || decrypted.startsWith("35") || decrypted.startsWith("36") ||
                    decrypted.startsWith("37") || decrypted.startsWith("38") || decrypted.startsWith("39"))) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("LinePay");
                
            } else if (decrypted.length() == 18 && decrypted.startsWith("PI")) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("PiPay");
                
            } else if (decrypted.length() == 18 && decrypted.startsWith("P")) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("PXPay");
                
            } else if ((decrypted.length() == 22 && (decrypted.startsWith("95") || decrypted.startsWith("96"))) ||
                    (decrypted.length() == 144 && decrypted.startsWith("YWhPCKAAAAFylQABwVwyNTQx"))) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("TaiwanPay");
                
            } else if (decrypted.startsWith("BP")) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("BeyondPay");
                
            } else if (decrypted.length() == 20 && decrypted.startsWith("99")) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("EasyPay");
                
            } else if (decrypted.length() == 18 && decrypted.startsWith("22")) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("JkosPay");
                
            } else if (decrypted.length() == 18 && decrypted.startsWith("TS")) {
                bean.setsActionType("mobile_payment");
                bean.setsMoPayType("TSPay");
                
            } else {
                bean.setCode(ErrCodeConst.pos_action_check_barcode);
                bean.setMessage(ErrCodeConst.pos_action_check_barcode_message);    
                return bean;
            }

            // 4. 判定成功處理
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);    
            return bean;

        } catch (Exception e) {
            log.error("Service checkBarcode 處理異常: ", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Transactional(readOnly = true)
    public ActionResponseBean loginAd(String center, String userId, String password) {
        try {
            // 1. 密碼進行 Hex 解碼
            String decryptedPassword = new String(Hex.decodeHex(password.toCharArray()));
            
            log.info("POS2 loginAD Service : center -> {}, userId -> {}, password -> {}", center, userId, password);
            
            ActionResponseBean bean = new ActionResponseBean();
            bean.setCode(ErrCodeConst.finished);
            bean.setMessage(ErrCodeConst.finished_message);
            
            LdapContext ctx = null;
            try {
                // 2. 從環境變數 env 撈取 DomainName 與 LdapIP 進行驗證
                ctx = ActiveDirectory.getConnection(userId, decryptedPassword, env.getProperty("DomainName"), env.getProperty("LdapIP"));
                
                if (ctx == null) {
                    bean.setCode(ErrCodeConst.ad_connect_error);
                    bean.setMessage(ErrCodeConst.ad_connect_error_message);
                } else {
                    Object user = ActiveDirectory.getUser(userId, ctx); // 這裡 User 型態請對齊你原本的 AD 類別
                    if (user == null) {
                        bean.setCode(ErrCodeConst.ad_user_not_found);
                        bean.setMessage(ErrCodeConst.ad_user_not_found_message);
                    }
                }
            } catch (Exception e) {
                log.warn("AD 驗證期間發生異常（帳密錯誤或連線失敗）: {}", e.getMessage());
                bean.setCode(ErrCodeConst.ad_user_not_found);
                bean.setMessage(ErrCodeConst.ad_user_not_found_message);
            } finally {
                if (ctx != null) {
                    try {
                        ctx.close();
                    } catch (Exception e) {
                        log.error("關閉 LdapContext 失敗", e);
                    }
                }
            }
            return bean;

        } catch (Exception e) {
            log.error("Service loginAD 處理異常: ", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    
}