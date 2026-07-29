package com.beyond.surrounding.erp.bean;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RequestCouponBody")
@XmlAccessorType(XmlAccessType.FIELD) // 讓 JAXB 直接讀取欄位上的註解，避免與 Getter 重複綁定
public class RequestCouponBody {

    private String sCenter;
    private String sSaleNO; 
    private String sType; 
    private String sTradeType;
    private String sClientSystem;
    private String sCardNO;
    private String sCouponNO;
    private String sPOSID;
    private String sCounterID;
    private String sDate;
    private Integer iAmt;
    private String sHeadCode; 
    private Integer iPieces;
    private List<String> lCouponNO;

    // ==========================================
    // Getter & Setter 方法
    // ==========================================

    public String getsCenter() {
        return sCenter;
    }

    public void setsCenter(String sCenter) {
        this.sCenter = sCenter;
    }

    public String getsSaleNO() {
        return sSaleNO;
    }

    public void setsSaleNO(String sSaleNO) {
        this.sSaleNO = sSaleNO;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getsTradeType() {
        return sTradeType;
    }

    public void setsTradeType(String sTradeType) {
        this.sTradeType = sTradeType;
    }

    public String getsClientSystem() {
        return sClientSystem;
    }

    public void setsClientSystem(String sClientSystem) {
        this.sClientSystem = sClientSystem;
    }

    public String getsCardNO() {
        return sCardNO;
    }

    public void setsCardNO(String sCardNO) {
        this.sCardNO = sCardNO;
    }

    public String getsCouponNO() {
        return sCouponNO;
    }

    public void setsCouponNO(String sCouponNO) {
        this.sCouponNO = sCouponNO;
    }

    public String getsPOSID() {
        return sPOSID;
    }

    public void setsPOSID(String sPOSID) {
        this.sPOSID = sPOSID;
    }

    public String getsCounterID() {
        return sCounterID;
    }

    public void setsCounterID(String sCounterID) {
        this.sCounterID = sCounterID;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public Integer getiAmt() {
        return iAmt;
    }

    public void setiAmt(Integer iAmt) {
        this.iAmt = iAmt;
    }

    public String getsHeadCode() {
        return sHeadCode;
    }

    public void setsHeadCode(String sHeadCode) {
        this.sHeadCode = sHeadCode;
    }

    public Integer getiPieces() {
        return iPieces;
    }

    public void setiPieces(Integer iPieces) {
        this.iPieces = iPieces;
    }

    public List<String> getlCouponNO() {
        return lCouponNO;
    }

    public void setlCouponNO(List<String> lCouponNO) {
        this.lCouponNO = lCouponNO;
    }
    
}
