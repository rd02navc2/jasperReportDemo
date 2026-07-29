package com.beyond.report.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class CryptUtil {

    /**
     * 建立一個信任所有憑證的 SSLContext
     * 適用於測試環境或特定支付 API 的 SSL 握手
     */
    public static SSLContext getSSLContext() {
        try {
            // 建立一個信任所有憑證的 TrustManager
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc;
        } catch (Exception e) {
            throw new RuntimeException("初始化 SSLContext 失敗", e);
        }
    }

    /**
     * 1. 取得 MD5 加密 Hex 字串 (剛剛引發 NPE 的核心方法)
     */
    public static String getMd5(String string) {
        if (string == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(string.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    /**
     * 2. 取得 SHA-256 加密 Hex 字串
     */
    public static String toSHA256Encrypt(String string) {
        if (string == null) return null;
        byte[] hash = toSHA256(string);
        return hash != null ? bytesToHex(hash) : null;
    }

    /**
     * 3. 取得 SHA-256 原生 byte 陣列
     */
    public static byte[] toSHA256(String string) {
        if (string == null) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * 4. 取得 HmacSHA256 加密 Hex 字串 (方法一)
     */
    public static String toHmacSHA256(String sPlayLod, String property) {
        if (sPlayLod == null || property == null) return null;
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(property.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(sPlayLod.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HmacSHA256", e);
        }
    }

    /**
     * 5. 取得 HmacSHA256 加密 Hex 字串 (對應你宣告的另一個同功能方法)
     */
    public static String toHashHmacSHA256(String string, String property) {
        return toHmacSHA256(string, property);
    }

    /**
     *  輔助工具：將 byte 陣列轉換為 16 進位 (Hex) 小寫字串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
}
    
  