package com.beyoung.parking.util;

import com.beyoung.parking.api.client.ErpFeignClient;
import com.beyoung.parking.util.GetDateTime;
import com.beyoung.parking.util.ParseUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ERPWebService {

    private final ErpFeignClient erpFeignClient;

    public JsonNode checkCoupon4ConnectTest(String sUrl, String sDate, String sCenter, String sCouponNO) throws Exception {
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetCouponRequest>"
                +"         <tip:request>"
                +"            &lt;Request>"
                + "							&lt;Access>"
                + "								&lt;Authentication user='tiptop' password='tiptop'/>"
                + "								&lt;Connection application='NaNa' source='192.168.1.2'/>"
                + "								&lt;Organization name='BY001'/>"
                + "								&lt;Locale language='zh_tw'/>"
                + "                             &lt;/Access>"
                + "							&lt;RequestContent>"
                + "								&lt;Parameter>"
                + "									&lt;Record>"
                + "										&lt;Field name='lqe01' value='"+sCouponNO+"'/>" 
                + "										&lt;Field name='shop' value='"+sCenter+"'/>" 
                + "										&lt;Field name='date' value='"+sDate+"'/>" 
                + "									&lt;/Record>"
                + "								&lt;/Parameter>"
                + "								&lt;Document/>"
                + "							&lt;/RequestContent>"
                + "						&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetCouponRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPResponse(response);
    }
    
    //dc-c
    /* 資安因素 靜止連線! 
    public JsonNode useMemberPoint(String sUrl, String branchId, String sCounterId, String cardId, int point, String sInvoiceB, String sInvoiceE, String sPosID, String sSerialNO) throws Exception {
        String serialNum = sPosID + GetDateTime.getTodayDateW("") + sSerialNO;
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetConsumerPointsRequest>"
                +"         <tip:request>"
                +"            &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>&lt;Field name='type' value='1'/>"
                + "&lt;Field name='condition' value='"+cardId+"'/>"
                + "&lt;Field name='shop' value='BY001'/>"
                + "&lt;Field name='saleno' value='"+serialNum+"'/>"
                + "&lt;Field name='date' value='"+GetDateTime.getTodayDateW("")+"'/>"
                + "&lt;Field name='amt' value='0'/>"
                + "&lt;Field name='Reduce_Points' value='0'/>"
                + "&lt;Field name='Rent_Booth' value='"+sCounterId+"'/>"
                + "&lt;Field name='invoice_b' value='"+sInvoiceB+"'/>"
                + "&lt;Field name='invoice_e' value='"+sInvoiceE+"'/>"
                + "&lt;Field name='rule' value=''/>"
                + "&lt;Field name='Reduce_Points2' value='"+point+"'/>"
                + "&lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetConsumerPointsRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPPoint(response);
    }
    */
    
    public JsonNode processPoint(String sUrl, String branchId, String sCounterId, String cardId, int iAmt, int point, String sRule, String sInvoiceB, String sInvoiceE, String sPosID, String sSerialNO) throws Exception {
        String serialNum = sPosID + GetDateTime.getTodayDateW("") + sSerialNO;
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetConsumerPointsRequest>"
                +"         <tip:request>"
                +"            &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>"
                + "&lt;Field name='type' value='1'/>"
                + "&lt;Field name='condition' value='"+cardId+"'/>"
                + "&lt;Field name='shop' value='"+branchId+"'/>"
                + "&lt;Field name='saleno' value='"+serialNum+"'/>"
                + "&lt;Field name='date' value='"+GetDateTime.getTodayDateW("")+"'/>"
                + "&lt;Field name='amt' value='"+iAmt+"'/>"
                + "&lt;Field name='Reduce_Points' value='"+point+"'/>"
                + "&lt;Field name='Rent_Booth' value='"+sCounterId+"'/>"
                + "&lt;Field name='invoice_b' value='"+sInvoiceB+"'/>"
                + "&lt;Field name='invoice_e' value='"+sInvoiceE+"'/>"
                + "&lt;Field name='rule' value='"+sRule+"'/>"
                + "&lt;Field name='Reduce_Points2' value='0'/>"
                + "&lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetConsumerPointsRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPPoint(response);
    }

    public JsonNode processPoint4EC(String sUrl, String branchId, String sCounterId, String cardId, int iAmt, int point, String sRule) throws Exception {
        String serialNum = "EC" + GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli("");
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetConsumerPointsRequest>"
                +"         <tip:request>"
                +"            &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>"
                + "&lt;Field name='type' value='1'/>"
                + "&lt;Field name='condition' value='"+cardId+"'/>"
                + "&lt;Field name='shop' value='"+branchId+"'/>"
                + "&lt;Field name='saleno' value='"+serialNum+"'/>"
                + "&lt;Field name='date' value='"+GetDateTime.getTodayDateW("")+"'/>"
                + "&lt;Field name='amt' value='"+iAmt+"'/>"
                + "&lt;Field name='Reduce_Points' value='"+point+"'/>"
                + "&lt;Field name='Rent_Booth' value='"+sCounterId+"'/>"
                + "&lt;Field name='invoice_b' value=''/>"
                + "&lt;Field name='invoice_e' value=''/>"
                + "&lt;Field name='rule' value='"+sRule+"'/>"
                + "&lt;Field name='Reduce_Points2' value='0'/>"
                + "&lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetConsumerPointsRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPPoint(response);
    }

    public JsonNode getInvoiceNO(String sUrl, String branchId, String sPOSID, String sMonth) throws Exception {
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetInvoiceNoRequest>"
                +"         <tip:request>"
                +"            &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>"
                + "&lt;Field name='type' value='"+sMonth+"'/>" 
                + "&lt;Field name='oom18' value='"+sPOSID+"'/>"
                + "&lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetInvoiceNoRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        log.info("ERP getInvoiceNO({}) Request：{}", sPOSID, strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPInvoice(response);
    }

    public JsonNode getChangeCoupon(String sUrl, String sDate, String sCenter, String sType, String sSaleNO, String sCouponNO, String sCounterID, String sPOSID, int iAmt) throws Exception {
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetChangeCouponRequest>"
                +"         <tip:request>"
                +"            &lt;Request>"
                + "							&lt;Access>"
                + "								&lt;Authentication user='tiptop' password='tiptop'/>"
                + "								&lt;Connection application='NaNa' source='192.168.1.2'/>"
                + "								&lt;Organization name='BY001'/>"
                + "								&lt;Locale language='zh_tw'/>"
                + "                             &lt;/Access>"
                + "							&lt;RequestContent>"
                + "								&lt;Parameter>"
                + "									&lt;Record>"
                + "										&lt;Field name='saleno' value='"+sSaleNO+"'/>" 
                + "										&lt;Field name='type' value='"+sType+"'/>" 
                + "										&lt;Field name='coupon_no' value='"+sCouponNO+"'/>"
                + "										&lt;Field name='amt' value='"+iAmt+"'/>"
                + "										&lt;Field name='date' value='"+sDate+"'/>" 
                + "										&lt;Field name='plant' value='"+sCenter+"'/>" 
                + "										&lt;Field name='pos_no' value='"+sPOSID+"'/>"
                + "										&lt;Field name='stand' value='"+sCounterID+"'/>" 
                + "									&lt;/Record>"
                + "								&lt;/Parameter>"
                + "								&lt;Document/>"
                + "							&lt;/RequestContent>"
                + "						&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetChangeCouponRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        log.info("Request：{}", strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPResponse(response);
    }

    public JsonNode exchangeCoupon(String sUrl, String sDate, String sCenter, String sUserID, String sCaseNO, String sCouponNO, String sCaseItem, int iQty, int iPoint) throws Exception {
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:CreateAppCouponRequest>"
                +"         <tip:request>"
                +"            &lt;Request>"
                + "							&lt;Access>"
                + "								&lt;Authentication user='tiptop' password='tiptop'/>"
                + "								&lt;Connection application='NaNa' source='192.168.1.2'/>"
                + "								&lt;Organization name='BY001'/>"
                + "								&lt;Locale language='zh_tw'/>"
                + "                             &lt;/Access>"
                + "							&lt;RequestContent>"
                + "								&lt;Parameter>"
                + "									&lt;Document>" 
                + "										&lt;RecordSet id='1'>"
                + "											&lt;Master name='lrl_file'>" 
                + "												&lt;Record>"
                + "													&lt;Field name='lrl00' value='"+sCenter+"'/>" 
                + "													&lt;Field name='lrl13' value='"+sDate+"'/>" 
                + "													&lt;Field name='lrl04' value='"+sUserID+"'/>"
                + "													&lt;Field name='lrl05' value='"+sCaseNO+"'/>"
                + "													&lt;Field name='lrg08' value='"+sCaseItem+"'/>"
                + "													&lt;Field name='lrg02' value='"+sCouponNO+"'/>" 
                + "													&lt;Field name='lrg04' value='"+iQty+"'/>"
                + "													&lt;Field name='lrg05' value='"+iPoint+"'/>"
                + "												&lt;/Record>"
                + "											&lt;/Master>" 
                + "										&lt;/RecordSet>"
                + "									&lt;/Document>"
                + "								&lt;/Parameter>" 
                + "							&lt;/RequestContent>"
                + "						&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:CreateAppCouponRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        log.info("Request：{}", strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPResponse(response);
    }

    public JsonNode checkCoupon(String sUrl, String sDate, String sCenter, List<String> lCouponNO) throws Exception {
        StringBuilder sbCouponNO = new StringBuilder();
        for (String sCouponNO : lCouponNO) {
            sbCouponNO.append(sCouponNO).append(",");
        }
        if (sbCouponNO.length() > 0 && sbCouponNO.charAt(sbCouponNO.length() - 1) == ',') {
            sbCouponNO.deleteCharAt(sbCouponNO.length() - 1);
        }

        log.info("ERP checkCoupon : 券號 -> {}", sbCouponNO);

        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetCouponRequest>"
                +"         <tip:request>"
                +"            &lt;Request>"
                + "							&lt;Access>"
                + "								&lt;Authentication user='tiptop' password='tiptop'/>"
                + "								&lt;Connection application='NaNa' source='192.168.1.2'/>"
                + "								&lt;Organization name='BY001'/>"
                + "								&lt;Locale language='zh_tw'/>"
                + "                             &lt;/Access>"
                + "							&lt;RequestContent>"
                + "								&lt;Parameter>"
                + "									&lt;Record>"
                + "										&lt;Field name='lqe01' value='"+sbCouponNO+"'/>" 
                + "										&lt;Field name='shop' value='"+sCenter+"'/>" 
                + "										&lt;Field name='date' value='"+sDate+"'/>" 
                + "									&lt;/Record>"
                + "								&lt;/Parameter>"
                + "								&lt;Document/>"
                + "							&lt;/RequestContent>"
                + "						&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:GetCouponRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        log.info("Request：{}", strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPResponse(response);
    }

    // 注意：已移除原參數中的不必要 Logger 傳遞，直接統一使用類別上的 @Slf4j
    public JsonNode useCoupon(String sUrl, String sDate, String sCenter, String sSaleNO, String sCounterID, String sPOSID, List<String> lCouponNO) throws Exception {
        StringBuilder sbCouponNO = new StringBuilder();
        for (String sCouponNO : lCouponNO) {
            sbCouponNO.append(sCouponNO).append(",");
        }
        if (sbCouponNO.length() > 0 && sbCouponNO.charAt(sbCouponNO.length() - 1) == ',') {
            sbCouponNO.deleteCharAt(sbCouponNO.length() - 1);
        }

        log.info("ERP useCoupon : 券號 -> {}", sbCouponNO);

        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:UpdateAppCouponRequest>"
                +"         <tip:request>"
                +"            &lt;Request>"
                + "							&lt;Access>"
                + "								&lt;Authentication user='tiptop' password='tiptop'/>"
                + "								&lt;Connection application='NaNa' source='192.168.1.2'/>"
                + "								&lt;Organization name='BY001'/>"
                + "								&lt;Locale language='zh_tw'/>"
                + "                             &lt;/Access>"
                + "							&lt;RequestContent>"
                + "								&lt;Parameter>"
                + "									&lt;Record>"
                + "										&lt;Field name='tc_psaplant' value='"+sCenter+"'/>" 
                + "										&lt;Field name='tc_psa01' value='"+sCounterID+"'/>" 
                + "										&lt;Field name='tc_psa02' value='"+sPOSID+"'/>"
                + "										&lt;Field name='tc_psa03' value='"+sSaleNO+"'/>"
                + "										&lt;Field name='tc_psa04' value='"+sDate+"'/>" 
                + "										&lt;Field name='lqe01' value='"+sbCouponNO+"'/>"
                + "									&lt;/Record>"
                + "								&lt;/Parameter>"
                + "								&lt;Document/>"
                + "							&lt;/RequestContent>"
                + "						&lt;/Request>"
                +"         </tip:request>"
                +"      </tip:UpdateAppCouponRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        log.info("Request：{}", strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPResponse(response);
    }

    public JsonNode getSaleCoupon(String sUrl, String sDate, String sCenter, String sType, String sSaleNO, String sHeadCode, int iPcs) throws Exception {
        String strXML = 
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>"
                +"   <soapenv:Header/>"
                +"   <soapenv:Body>"
                +"      <tip:GetSaleCouponRequest>"
                +"         <tip:request>"
                +"            &lt;Request>"
                + "							&lt;Access>"
                + "								&lt;Authentication user='tiptop' password='tiptop'/>"
                + "								&lt;Connection application='NaNa' source='192.168.1.2'/>"
                + "								&lt;Organization name='BY001'/>"
                + "								&lt;Locale language='zh_tw'/>"
                + "                             &lt;/Access>"
                + "							&lt;RequestContent>"
                + "								&lt;Parameter>"
                + "												<Record>"
                + "												&lt;Field name='type' value='1'/>" 
                + "												&lt;Field name='plant' value='"+sCenter+"'/>"
                + "												&lt;Field name='date' value='"+sDate+"'/>"
                + "												&lt;Field name='saleno' value='"+sSaleNO+"'/>" 
                + "												&lt;Field name='lpx23' value='"+sHeadCode+"'/>"
                + "												&lt;Field name='pcs' value='"+iPcs+"'/>"
                + "												</Record>"
                + "								</Parameter>" 
                + "							</RequestContent>"
                + "						</Request>"
                +"         </tip:request>"
                +"      </tip:GetSaleCouponRequest>"
                +"   </soapenv:Body>"
                +"</soapenv:Envelope>";

        log.info("Request：{}", strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
        String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        return ParseUtil.parserERPResponse(response);
    }
    
    //dc-
	public JsonNode useMemberPointSit(String erpUrl, String center, String counterId, String cardNo, int erpPoint,
			String invoice, String invoice2, String string, String timeMilli) {
		
		// String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        
		log.info("==== SIT ERP 原始回傳 XML ====\n{}");

        return ParseUtil.parserERPPoint(null);
	}
}