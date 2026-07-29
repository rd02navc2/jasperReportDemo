package com.beyoung.surrounding.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ERPWebService {
	public static JSONObject useMemberPoint;

	public static synchronized JsonNode checkCoupon4ConnectTest(String sUrl, String sDate, String sCenter, String sCouponNO) throws Exception{
		HttpURLConnection con = null;
		try{			
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
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
							+ "             &lt;/Access>"
							+ "							&lt;RequestContent>"
							+ "								&lt;Parameter>"
							+ "									&lt;Record>"
							+ "										&lt;Field name='lqe01' value='"+sCouponNO+"'/>" 
							+ "										&lt;Field name='shop' value='"+sCenter+"'/>" 
							+ "										&lt;Field name='date' value='"+sDate+"'/>" //yyyy/MM/dd
							+ "									&lt;/Record>"
							+ "								&lt;/Parameter>"
							+ "								&lt;Document/>"
							+ "							&lt;/RequestContent>"
							+ "						&lt;/Request>"
							+"         </tip:request>"
							+"      </tip:GetCouponRequest>"
							+"   </soapenv:Body>"
							+"</soapenv:Envelope>";
	
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			//dc-
			return ParseUtil.parserERPResponse(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}			
	}

	public static synchronized JSONObject useMemberPoint(String sUrl, String branchId, String sCounterId, String cardId,int point, String sInvoiceB, String sInvoiceE, String sPosID, String sSerialNO) throws Exception{
		HttpURLConnection con = null;
		try{
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			String serialNum = sPosID+GetDateTime.getTodayDateW("")+sSerialNO;//new Timestamp(System.currentTimeMillis()).getTime()+"";
			
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

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));

			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPPoint(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}
	}

	public static synchronized JSONObject processPoint(String sUrl, String branchId, String sCounterId, String cardId, int iAmt, int point, String sRule, String sInvoiceB, String sInvoiceE, String sPosID, String sSerialNO) throws Exception{
		HttpURLConnection con = null;
		try{		
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			String serialNum = sPosID+GetDateTime.getTodayDateW("")+sSerialNO;//new Timestamp(System.currentTimeMillis()).getTime()+"";
			
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
	
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
	
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPPoint(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}
	}
	
	public static synchronized JSONObject processPoint4EC(String sUrl, String branchId, String sCounterId, String cardId, int iAmt, int point, String sRule) throws Exception{
		HttpURLConnection con = null;
		try{		
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			String serialNum = "EC"+GetDateTime.getTodayDateW("")+GetDateTime.getTimeMilli("");//new Timestamp(System.currentTimeMillis()).getTime()+"";
			
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
	
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPPoint(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}		
	}
	
	public static synchronized JsonNode getInvoiceNO(String sUrl, String branchId, String sPOSID, String sMonth) throws Exception{
		HttpURLConnection con = null;
		try{		
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
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
	
			log.info("ERP getInvoiceNO("+sPOSID+") Request："+strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot", "\""));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			log.info("ERP getInvoiceNO("+sPOSID+") Response Code : "+con.getResponseCode());
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPInvoice(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}		
	}
	
	public static synchronized JsonNode getChangeCoupon(String sUrl, String sDate, String sCenter, String sType, String sSaleNO, String sCouponNO, String sCounterID, String sPOSID, int iAmt) throws Exception{
		HttpURLConnection con = null;
		try{			
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
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
							+ "             &lt;/Access>"
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
	
			log.info("Request："+strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot", "\""));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			log.info("ERP getChangeCoupon("+sPOSID+") Response Code : "+con.getResponseCode());
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPResponse(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}			
	}	
	
	public static synchronized JsonNode exchangeCoupon(String sUrl, String sDate, String sCenter, String sUserID, String sCaseNO, String sCouponNO, String sCaseItem, int iQty, int iPoint) throws Exception{
		HttpURLConnection con = null;
		try{			
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
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
							+ "             &lt;/Access>"
							+ "							&lt;RequestContent>"
							+ "								&lt;Parameter>"
							+ "									&lt;Document>"						
							+ "										&lt;RecordSet id='1'>"
							+ "											&lt;Master name='lrl_file'>"						
							+ "												&lt;Record>"
							+ "													&lt;Field name='lrl00' value='"+sCenter+"'/>" //
							+ "													&lt;Field name='lrl13' value='"+sDate+"'/>" //
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
	
			log.info("Request："+strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot", "\""));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			log.info("ERP exchangeCoupon("+sUserID+") Response Code : "+con.getResponseCode());
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPResponse(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}			
	}	
	
	public static synchronized JsonNode checkCoupon(String sUrl, String sDate, String sCenter, List<String> lCouponNO) throws Exception{
		HttpURLConnection con = null;
		try{			
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
			StringBuffer _sbCouponNO = new StringBuffer();
			for (String sCouponNO : lCouponNO)
				_sbCouponNO.append(sCouponNO+",");
			
	    if (_sbCouponNO.length()>0 && _sbCouponNO.substring(_sbCouponNO.length()-1).equals(","))
	    	_sbCouponNO.delete(_sbCouponNO.length()-1, _sbCouponNO.length());
	
			log.info("ERP checkCoupon : 券號 -> "+_sbCouponNO.toString());
			
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
							+ "             &lt;/Access>"
							+ "							&lt;RequestContent>"
							+ "								&lt;Parameter>"
							+ "									&lt;Record>"
							+ "										&lt;Field name='lqe01' value='"+_sbCouponNO+"'/>" 
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
	
			log.info("Request："+strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot", "\""));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			log.info("ERP checkCoupon("+_sbCouponNO.toString()+") Response Code : "+con.getResponseCode());
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPResponse(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}			
	}
	
	public static synchronized JsonNode useCoupon(String sUrl, String sDate, String sCenter, String sSaleNO, String sCounterID, String sPOSID, List<String> lCouponNO, Logger log) throws Exception{
		HttpURLConnection con = null;
		try{			
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
			StringBuffer _sbCouponNO = new StringBuffer();
			for (String sCouponNO : lCouponNO)
				_sbCouponNO.append(sCouponNO+",");
			
	    if (_sbCouponNO.length()>0 && _sbCouponNO.substring(_sbCouponNO.length()-1).equals(","))
	    	_sbCouponNO.delete(_sbCouponNO.length()-1, _sbCouponNO.length());
	
			log.info("ERP useCoupon : 券號 -> "+_sbCouponNO.toString());
			
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
							+ "             &lt;/Access>"
							+ "							&lt;RequestContent>"
							+ "								&lt;Parameter>"
							+ "									&lt;Record>"
							+ "										&lt;Field name='tc_psaplant' value='"+sCenter+"'/>" 
							+ "										&lt;Field name='tc_psa01' value='"+sCounterID+"'/>"						
							+ "										&lt;Field name='tc_psa02' value='"+sPOSID+"'/>"
							+ "										&lt;Field name='tc_psa03' value='"+sSaleNO+"'/>"
							+ "										&lt;Field name='tc_psa04' value='"+sDate+"'/>"						
							+ "										&lt;Field name='lqe01' value='"+_sbCouponNO.toString()+"'/>"
							+ "									&lt;/Record>"
							+ "								&lt;/Parameter>"
							+ "								&lt;Document/>"
							+ "							&lt;/RequestContent>"
							+ "						&lt;/Request>"
							+"         </tip:request>"
							+"      </tip:UpdateAppCouponRequest>"
							+"   </soapenv:Body>"
							+"</soapenv:Envelope>";
	
			log.info("Request："+strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot", "\""));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			log.info("ERP useCoupon("+_sbCouponNO.toString()+") Response Code : "+con.getResponseCode());
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPResponse(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}				
	}
	
	public static synchronized JsonNode getSaleCoupon(String sUrl, String sDate, String sCenter, String sType, String sSaleNO, String sHeadCode, int iPcs, Logger log) throws Exception{
		HttpURLConnection con = null;
		try{			
			URL obj = new URL(sUrl);
			con = (HttpURLConnection) obj.openConnection();
			
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
							+ "             &lt;/Access>"
							+ "							&lt;RequestContent>"
							+ "								&lt;Parameter>"
	//						+ "									&lt;Document>"						
	//						+ "										&lt;RecordSet id='1'>"
	//						+ "											&lt;Master name='lqe_file'>"						
							+ "												&lt;Record>"
//							+ "												&lt;Field name='type' value='"+sType+"'/>" 
							+ "												&lt;Field name='type' value='1'/>"							
							+ "												&lt;Field name='plant' value='"+sCenter+"'/>"
							+ "												&lt;Field name='date' value='"+sDate+"'/>"
							+ "												&lt;Field name='saleno' value='"+sSaleNO+"'/>" 						
							+ "												&lt;Field name='lpx23' value='"+sHeadCode+"'/>"
							+ "												&lt;Field name='pcs' value='"+iPcs+"'/>"
							+ "												&lt;/Record>"
	//						+ "											&lt;/Master>"						
	//						+ "										&lt;/RecordSet>"
	//						+ "									&lt;/Document>"
							+ "								&lt;/Parameter>"						
							+ "							&lt;/RequestContent>"
							+ "						&lt;/Request>"
							+"         </tip:request>"
							+"      </tip:GetSaleCouponRequest>"
							+"   </soapenv:Body>"
							+"</soapenv:Envelope>";
	
			log.info("Request："+strXML.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot", "\""));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(strXML.getBytes().length));
	
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
		
			OutputStream out = con.getOutputStream();
			out.write(strXML.getBytes());
			out.close();
			con.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			con.disconnect();
			return ParseUtil.parserERPResponse(response.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if (con != null)
				con.disconnect();
		}			
	}
	
    //dc-
	public JSONObject useMemberPointSit(String erpUrl, String center, String counterId, String cardNo, int erpPoint,
			String invoice, String invoice2, String string, String timeMilli) throws JSONException {
		
		// String response = erpFeignClient.sendSoapRequest(URI.create(sUrl), strXML);
        
		log.info("==== SIT ERP 原始回傳 XML ====\n{}");

        return ParseUtil.parserERPPoint(null);
	}

		
}
