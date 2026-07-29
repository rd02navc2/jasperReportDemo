package com.beyoung.surrounding.pss.service;

import java.util.List;
import com.beyoung.surrounding.pss.bean.DiscountDetailBean;
import com.beyoung.surrounding.pss.entity.ParkingDiscountExec;
import com.beyoung.surrounding.pss.entity.ParkingDiscountSet;
import com.beyoung.surrounding.pss.entity.ParkingRent;
import com.beyoung.surrounding.pss.entity.TcPsaFile;
import com.beyoung.surrounding.bean.ResponseBean;

/**
 * 停車場折抵業務邏輯核心介面
 * 重構說明：全面落實標準 Java 小駝峰命名法，移除舊式匈牙利命名法前綴
 */
public interface ParkingService {

    /**
     * 停車發票補登核心 API
     */
    ResponseBean appendInvoice4Parking(
            String invoiceDate, String invoiceNo, String randomNo, String invoiceTime, 
            String center, String channel, Integer tranXType, String counterId, 
            String cardNo, String carNo, Double promoteAmt) throws Exception;

    /**
     * 檢查特定發票於停車場域之可折抵狀態
     * * @param invoiceNo 發票號碼
     * @param randomNo  隨機碼 (或 "uncheck")
     * @return TcPsaFile 發票主要資料與狀態結構體
     */
    TcPsaFile check4Parking(String invoiceNo, String randomNo) throws Exception;

    /**
     * 針對無人繳費機 (APS_UN) 等異質管道進行發票日誌前置校驗
     * * @param invoiceNo 發票號碼
     * @param randomNo  隨機碼
     * @return ResponseBean 校驗結果載體
     */
    ResponseBean checklog4Parking(String invoiceNo, String randomNo) throws Exception;

    /**
     * 查詢車牌是否為月租車/長期承租車
     */
    ParkingRent getParkingRent(String carNo) throws Exception;

    /**
     * 取得目前該車牌可執行的折抵設定列表 (版本 1)
     */
    List<ParkingDiscountSet> getDiscExec(String carNo) throws Exception;

    /**
     * 取得目前該車牌可執行的折抵設定列表 (版本 2)
     */
    List<ParkingDiscountSet> getDiscExec2(String carNo) throws Exception;

    /**
     * 寫入卡片折抵/點數折抵扣減紀錄，並回傳產生的流水號 pNo
     */
    Integer insertCard(String loginId, String userId, String carNo, String cardNo, 
                       String discId, String discName, Double discHour) throws Exception;

    /**
     * 將中台折抵試算時數同步至外部不落地車牌辨識系統
     */
    void sync(DiscountDetailBean requestBody) throws Exception;

    /**
     * 檢查指定卡號今日是否已在該車牌使用過折抵
     */
    ParkingDiscountExec getCardUsed(String carNo, String cardNo) throws Exception;

    /**
     * 檢查指定會員今日是否已在該車牌使用過 VIP 免費時數折抵
     */
    ParkingDiscountExec getVIPUsed(String carNo, String memberId) throws Exception;

    /**
     * 刪除或取消已綁定的卡號折抵紀錄
     */
    void delCardNo(String center, String cardNo, Integer pNo) throws Exception;
    
}