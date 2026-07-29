package com.beyoung.surrounding.util;

public class StringUtil {

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
}