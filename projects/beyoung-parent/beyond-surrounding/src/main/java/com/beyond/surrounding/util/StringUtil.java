package com.beyond.surrounding.util;

import java.security.SecureRandom;

public class StringUtil {
    
    // 使用 SecureRandom 確保生成的隨機數符合安全加密規格
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 檢查是否符合財政部手機載具條碼規格
     * 規則：共8碼，由 / 開頭，後面 7 碼為 [0-9][A-Z].+-
     */
    public static boolean checkCarrier(String carrier) {
        if (carrier == null || carrier.length() != 8) {
            return false;
        }
        // 財政部官方標準雲端發票載具 Regex 驗證
        String regex = "^/[0-9A-Z.+ -]{7}$";
        return carrier.matches(regex);
    }

    /**
     * 生成指定長度的隨機英數字串
     * @param length 字串長度
     * @return 隨機字串
     */
    public static String getRandomID(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int characterIndex = secureRandom.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(characterIndex));
        }
        return builder.toString();
    }

    /**
     * 向右靠齊，不足指定長度時向右填補指定字串 (通常用於文字補空白)
     * 
     * @param str        原始文字
     * @param len        指定總長度
     * @param padChar    用來填補的字元/字串 (若為 null 或空則預設補空白)
     * @return 填補後的字串
     */
    public static String padR(String str, int len, String padChar) {
        String baseStr = (str == null) ? "" : str;
        String pChar = (padChar == null || padChar.isEmpty()) ? " " : padChar;
        
        if (baseStr.length() >= len) {
            return baseStr.substring(0, len); // 避免資料過長超出媒體檔欄位規格
        }
        
        StringBuilder sb = new StringBuilder(baseStr);
        while (sb.length() < len) {
            sb.append(pChar);
        }
        return sb.toString();
    }

    /**
     * 向左靠齊補零，不足指定長度時向左補 "0" (通常用於金額與流水號)
     * 
     * @param str 原始數字字串
     * @param len 指定總長度
     * @return 填補後的數字字串
     */
    public static String fillzero(String str, int len) {
        String baseStr = (str == null) ? "" : str;
        if (baseStr.length() >= len) {
            return baseStr.substring(0, len);
        }
        
        // 使用 String.format 的 %0Nd 語法（如果是純數字），或直接用 StringBuilder 最穩健
        StringBuilder sb = new StringBuilder();
        int padLength = len - baseStr.length();
        for (int i = 0; i < padLength; i++) {
            sb.append("0");
        }
        sb.append(baseStr);
        return sb.toString();
    }
    
    /**
     * 補齊遺失的方法：將 XML 字串轉換為 W3C Document 物件
     * 
     * @param xmlStr XML 格式的字串
     * @return org.w3c.dom.Document
     * @throws Exception 當 XML 格式錯誤或無法解析時拋出
     */
    public static org.w3c.dom.Document convertStringToXMLDocument(String xmlStr) throws Exception {
        if (xmlStr == null || xmlStr.trim().isEmpty()) {
            throw new IllegalArgumentException("XML 內容不可為空");
        }
        
        // 建立 DocumentBuilderFactory
        javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        
        //  安全防禦：防止 XXE (XML External Entity) 漏洞攻擊
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        
        javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
        
        // 將字串包裝成 InputSource 並進行解析
        try (java.io.StringReader reader = new java.io.StringReader(xmlStr)) {
            org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(reader);
            return builder.parse(inputSource);
        }
    }
    
    
}