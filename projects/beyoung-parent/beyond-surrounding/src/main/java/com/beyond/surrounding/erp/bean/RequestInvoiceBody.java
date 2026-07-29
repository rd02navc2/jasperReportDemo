package com.beyond.surrounding.erp.bean;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RequestInvoiceBody")
@XmlAccessorType(XmlAccessType.FIELD) // 讓 JAXB 直接讀取欄位上的註解，避免與 Getter 重複綁定
public class RequestInvoiceBody {

    @XmlElement(name = "sCenter")
    private String sCenter;

    @XmlElement(name = "sLoginID")
    private String sLoginID;

    @XmlElement(name = "sCounterID")
    private String sCounterID;

    @XmlElement(name = "sPOSID")
    private String sPOSID;

    @XmlElement(name = "sMonth")
    private String sMonth;

    @XmlElement(name = "sUserID")
    private String sUserID;

    @XmlElement(name = "sUserName")
    private String sUserName;

    @XmlElement(name = "sCardNO")
    private String sCardNO;

    @XmlElement(name = "iPoint")
    private Integer iPoint;

    // --- Getter & Setter (如果專案有裝 Lombok，可以直接在類別外掛 @Data 並拿掉以下程式碼) ---

    public String getsCenter() { return sCenter; }
    public void setsCenter(String sCenter) { this.sCenter = sCenter; }

    public String getsLoginID() { return sLoginID; }
    public void setsLoginID(String sLoginID) { this.sLoginID = sLoginID; }

    public String getsCounterID() { return sCounterID; }
    public void setsCounterID(String sCounterID) { this.sCounterID = sCounterID; }

    public String getsPOSID() { return sPOSID; }
    public void setsPOSID(String sPOSID) { this.sPOSID = sPOSID; }

    public String getsMonth() { return sMonth; }
    public void setsMonth(String sMonth) { this.sMonth = sMonth; }

    public String getsUserID() { return sUserID; }
    public void setsUserID(String sUserID) { this.sUserID = sUserID; }

    public String getsUserName() { return sUserName; }
    public void setsUserName(String sUserName) { this.sUserName = sUserName; }

    public String getsCardNO() { return sCardNO; }
    public void setsCardNO(String sCardNO) { this.sCardNO = sCardNO; }

    public Integer getiPoint() { return iPoint; }
    public void setiPoint(Integer iPoint) { this.iPoint = iPoint; }
    
}