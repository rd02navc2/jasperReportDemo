package com.beyond.surrounding.util;

import com.beyond.surrounding.ec.client.ErpEcInvoiceFeignClient;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor // 透過建構子自動注入 Feign 與 ParseUtil 元件
public class ERPWebService {

    private final ErpEcInvoiceFeignClient erpServiceClient;
    private final ParseUtil parseUtil;

    /**
     * 1. 取得 ERP 發票號碼
     */
    public JSONObject getInvoiceNo(String url, String branchId, String posId, String month) throws Exception {
        
        // 使用 Java 15+ Text Blocks (三引號開頭後直接換行)
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetInvoiceNoRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='type' value='%s'/>
                        &lt;Field name='oom18' value='%s'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetInvoiceNoRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(month, posId);

        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP getInvoiceNo({}) Request：\n{}", posId, logXml);
        }

        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 回傳 JSONObject 宣告
        return parseUtil.parserERPInvoice(xmlResponse);
    }

    /**
     * 2. 線上折價券連線測試
     */
    public JSONObject checkCoupon4ConnectTest(String url, String date, String center, String couponNo) throws Exception {
        
        // 修正點 2：改用 Java 15+ Text Blocks (三引號開頭後直接換行) 與 %s 佔位符
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetCouponRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='lqe01' value='%s'/>
                        &lt;Field name='shop' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetCouponRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(couponNo, center, date); // 按順序填入參數

        // 建議加上 Log 方便日後維護除錯
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP checkCoupon4ConnectTest({}) Request：\n{}", couponNo, logXml);
        }

        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 修正點 3：對接回傳 JSONObject 的解析方法
        return parseUtil.parserERPResponse(xmlResponse);
    }

    /**
     * 3. 扣點點數 (扣減會員點數)
     */
    public JSONObject useMemberPoint(String url, String branchId, String counterId, String cardId, int point, String invoiceB, String invoiceE, String posId, String serialNo) throws Exception {
        
        // 產生流水號
        String serialNum = posId + GetDateTime.getTodayDateW("") + serialNo;
        String todayDate = GetDateTime.getTodayDateW("");

        // 使用 Java 15+ Text Blocks (三引號開頭後直接換行)，內層標籤維持正確的 &lt; 轉義
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetConsumerPointsRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>&lt;Field name='type' value='1'/>
                        &lt;Field name='condition' value='%s'/>
                        &lt;Field name='shop' value='BY001'/>
                        &lt;Field name='saleno' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;Field name='amt' value='0'/>
                        &lt;Field name='Reduce_Points' value='0'/>
                        &lt;Field name='Rent_Booth' value='%s'/>
                        &lt;Field name='invoice_b' value='%s'/>
                        &lt;Field name='invoice_e' value='%s'/>
                        &lt;Field name='rule' value=''/>
                        &lt;Field name='Reduce_Points2' value='%d'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetConsumerPointsRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(cardId, serialNum, todayDate, counterId, invoiceB, invoiceE, point);

        // 加上 Log 方便生產環境除錯
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP useMemberPoint(Card: {}, Points: {}) Request：\n{}", cardId, point, logXml);
        }

        // 呼叫新版 Client
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 對接回傳 JSONObject 的解析方法 (如果結構一致，可在 ParseUtil 裡將 parserERPPoint 直接導向 parserERPResponse)
        return parseUtil.parserERPPoint(xmlResponse);
    }

    /**
     * 4. 處理消費點數 (累點與常規扣點)
     */
    public JSONObject processPoint(String url, String branchId, String counterId, String cardId, int amt, int point, String rule, String invoiceB, String invoiceE, String posId, String serialNo) throws Exception {
        
        // 產生流水號與當日日期
        String serialNum = posId + GetDateTime.getTodayDateW("") + serialNo;
        String todayDate = GetDateTime.getTodayDateW("");

        // 使用 Java 15+ Text Blocks (三引號開頭後直接換行)
        // 數字變數（amt, point）使用 %d 佔位符，字串變數使用 %s 佔位符
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetConsumerPointsRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='type' value='1'/>
                        &lt;Field name='condition' value='%s'/>
                        &lt;Field name='shop' value='%s'/>
                        &lt;Field name='saleno' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;Field name='amt' value='%d'/>
                        &lt;Field name='Reduce_Points' value='%d'/>
                        &lt;Field name='Rent_Booth' value='%s'/>
                        &lt;Field name='invoice_b' value='%s'/>
                        &lt;Field name='invoice_e' value='%s'/>
                        &lt;Field name='rule' value='%s'/>
                        &lt;Field name='Reduce_Points2' value='0'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetConsumerPointsRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(cardId, branchId, serialNum, todayDate, amt, point, counterId, invoiceB, invoiceE, rule);

        // 加上 Log 方便生產環境追蹤與對帳
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP processPoint(Card: {}, Amt: {}, Points: {}) Request：\n{}", cardId, amt, point, logXml);
        }

        // 呼叫 Client 發送請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 複用你 ParseUtil 裡的點數解析方法
        return parseUtil.parserERPPoint(xmlResponse);
    }

    /**
     * 5. 電商專用點數處理 (processPoint4EC)
     */
    public JSONObject processPoint4Ec(String url, String branchId, String counterId, String cardId, int amt, int point, String rule) throws Exception {
        
        // 生成電商專用的流水號與當日日期
        String serialNum = "EC" + GetDateTime.getTodayDateW("") + GetDateTime.getTimeMilli("");
        String todayDate = GetDateTime.getTodayDateW("");

        // 使用 Java 15+ Text Blocks (三引號開頭後直接換行)
        // 數字變數使用 %d，字串變數使用 %s
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetConsumerPointsRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='type' value='1'/>
                        &lt;Field name='condition' value='%s'/>
                        &lt;Field name='shop' value='%s'/>
                        &lt;Field name='saleno' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;Field name='amt' value='%d'/>
                        &lt;Field name='Reduce_Points' value='%d'/>
                        &lt;Field name='Rent_Booth' value='%s'/>
                        &lt;Field name='invoice_b' value=''/>
                        &lt;Field name='invoice_e' value=''/>
                        &lt;Field name='rule' value='%s'/>
                        &lt;Field name='Reduce_Points2' value='0'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetConsumerPointsRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(cardId, branchId, serialNum, todayDate, amt, point, counterId, rule);

        // 加上 Log 方便電商訂單與 ERP 對帳除錯
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP processPoint4Ec(Card: {}, Amt: {}, Points: {}) Request：\n{}", cardId, amt, point, logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 回傳 JSONObject 宣告，底層已無縫對接原生 DOM 解析
        return parseUtil.parserERPPoint(xmlResponse);
    }
    
    /**
     * 6. 換券處理 (getChangeCoupon)
     */
    public JSONObject getChangeCoupon(String url, String date, String center, String type, String saleNo, String couponNo, String counterId, String posId, int amt) throws Exception {
        
        // 改用 Java 15+ Text Blocks (三引號開頭後直接換行)
        // 數字變數使用 %d，字串變數使用 %s
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetChangeCouponRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='saleno' value='%s'/>
                        &lt;Field name='type' value='%s'/>
                        &lt;Field name='coupon_no' value='%s'/>
                        &lt;Field name='amt' value='%d'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;Field name='plant' value='%s'/>
                        &lt;Field name='pos_no' value='%s'/>
                        &lt;Field name='stand' value='%s'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetChangeCouponRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(saleNo, type, couponNo, amt, date, center, posId, counterId);

        // 加上 Log 追蹤傳入參數與發送的 XML
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP getChangeCoupon(Coupon: {}, Amt: {}) Request：\n{}", couponNo, amt, logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 對接已翻新為原生 DOM 並回傳 JSONObject 的 parserERPResponse
        return parseUtil.parserERPResponse(xmlResponse);
    }

    /**
     * 7. 點數換券 (exchangeCoupon)
     */
    public JSONObject exchangeCoupon(String url, String date, String center, String userId, String caseNo, String couponNo, String caseItem, int qty, int point) throws Exception {
        
        // 改用 Java 15+ Text Blocks (三引號開頭後直接換行)
        // 數字變數（qty, point）使用 %d 佔位符，其串變數使用 %s 佔位符
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:CreateAppCouponRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Document>&lt;RecordSet id='1'>&lt;Master name='lrl_file'>&lt;Record>
                        &lt;Field name='lrl00' value='%s'/>
                        &lt;Field name='lrl13' value='%s'/>
                        &lt;Field name='lrl04' value='%s'/>
                        &lt;Field name='lrl05' value='%s'/>
                        &lt;Field name='lrg08' value='%s'/>
                        &lt;Field name='lrg02' value='%s'/>
                        &lt;Field name='lrg04' value='%d'/>
                        &lt;Field name='lrg05' value='%d'/>
                        &lt;/Record>&lt;/Master>&lt;/RecordSet>&lt;/Document>&lt;/Parameter>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:CreateAppCouponRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(center, date, userId, caseNo, caseItem, couponNo, qty, point);

        // 加上 Log 方便生產環境除錯
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP exchangeCoupon(User: {}, Coupon: {}, Qty: {}) Request：\n{}", userId, couponNo, qty, logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 對接回傳 JSONObject 的原生 XML 解析方法
        return parseUtil.parserERPResponse(xmlResponse);
    }

    /**
     * 8. 批次核對折價券 (精簡 Java Stream 改良)
     */
    public JSONObject checkCoupon(String url, String date, String center, List<String> couponNoList) throws Exception {
        
        // 使用 Java Stream 優雅串接券號
        String joinedCouponNo = couponNoList.stream().collect(Collectors.joining(","));
        log.info("ERP checkCoupon : 券號 -> {}", joinedCouponNo);
        
        // 改用 Java 15+ Text Blocks (三引號開頭後直接換行)
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetCouponRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='lqe01' value='%s'/>
                        &lt;Field name='shop' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetCouponRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(joinedCouponNo, center, date);

        // 加上完整 XML 的 Log 輸出，方便排查轉義字元問題
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP checkCoupon 完整 Request：\n{}", logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 對接回傳 JSONObject 的原生 XML 解析方法
        return parseUtil.parserERPResponse(xmlResponse);
    }
    
    /**
     * 9. 使用折價券
     */
    public JSONObject useCoupon(String url, String date, String center, String saleNo, String counterId, String posId, List<String> couponNoList) throws Exception {
        
        // 使用 Java Stream 優雅串接券號
        String joinedCouponNo = couponNoList.stream().collect(Collectors.joining(","));
        log.info("ERP useCoupon : 券號 -> {}", joinedCouponNo);
        
        // 改用 Java 15+ Text Blocks (三引號開頭後直接換行)
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:UpdateAppCouponRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='tc_psaplant' value='%s'/>
                        &lt;Field name='tc_psa01' value='%s'/>
                        &lt;Field name='tc_psa02' value='%s'/>
                        &lt;Field name='tc_psa03' value='%s'/>
                        &lt;Field name='tc_psa04' value='%s'/>
                        &lt;Field name='lqe01' value='%s'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:UpdateAppCouponRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(center, counterId, posId, saleNo, date, joinedCouponNo);

        // 加上完整 XML 的 Log 輸出，方便排查
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP useCoupon 完整 Request：\n{}", logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 對接回傳 JSONObject 的原生 XML 解析方法
        return parseUtil.parserERPResponse(xmlResponse);
    }
    
    /**
     * 10. 銷售產生折價券 (getSaleCoupon)
     */
    public JSONObject getSaleCoupon(String url, String date, String center, String type, String saleNo, String headCode, int pcs) throws Exception {
        
        // 改用 Java 15+ Text Blocks (三引號開頭後直接換行)
        // 數字變數使用 %d，字串變數使用 %s
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetSaleCouponRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='type' value='1'/>
                        &lt;Field name='plant' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;Field name='saleno' value='%s'/>
                        &lt;Field name='lpx23' value='%s'/>
                        &lt;Field name='pcs' value='%d'/>
                        &lt;/Record>&lt;/Parameter>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetSaleCouponRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(center, date, saleNo, headCode, pcs);

        // 加上完整 XML 的 Log 輸出
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("ERP getSaleCoupon(SaleNo: {}, HeadCode: {}) Request：\n{}", saleNo, headCode, logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        
        // 對接回傳 JSONObject 的原生 XML 解析方法
        return parseUtil.parserERPResponse(xmlResponse);
    }

    /**
     * 11. SIT 測試環境專用扣點
     */
    public JSONObject useMemberPointSit(String erpUrl, String center, String counterId, String cardNo, int erpPoint, String invoice, String invoice2, String rule, String timeMilli) throws Exception {
        
        // 產生 SIT 專用流水號與當日日期
        String serialNum = "SIT" + GetDateTime.getTodayDateW("") + timeMilli;
        String todayDate = GetDateTime.getTodayDateW("");

        // 改用 Java 15+ Text Blocks (三引號開頭後直接換行)
        // 數字變數 erpPoint 使用 %d，其餘字串使用 %s
        String strXml = """
            <soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:tip='http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay'>
               <soapenv:Header/>
               <soapenv:Body>
                  <tip:GetConsumerPointsRequest>
                     <tip:request>
                        &lt;Request>&lt;Access>&lt;Authentication user='tiptop' password='tiptop'/>&lt;Connection application='NaNa' source='192.168.1.2'/>&lt;Organization name='BY001'/>&lt;Locale language='zh_tw'/>&lt;/Access>&lt;RequestContent>&lt;Parameter>&lt;Record>
                        &lt;Field name='type' value='1'/>
                        &lt;Field name='condition' value='%s'/>
                        &lt;Field name='shop' value='%s'/>
                        &lt;Field name='saleno' value='%s'/>
                        &lt;Field name='date' value='%s'/>
                        &lt;Field name='amt' value='0'/>
                        &lt;Field name='Reduce_Points' value='0'/>
                        &lt;Field name='Rent_Booth' value='%s'/>
                        &lt;Field name='invoice_b' value='%s'/>
                        &lt;Field name='invoice_e' value='%s'/>
                        &lt;Field name='rule' value='%s'/>
                        &lt;Field name='Reduce_Points2' value='%d'/>
                        &lt;/Record>&lt;/Parameter>&lt;Document/>&lt;/RequestContent>&lt;/Request>
                     </tip:request>
                  </tip:GetConsumerPointsRequest>
               </soapenv:Body>
            </soapenv:Envelope>
            """.formatted(cardNo, center, serialNum, todayDate, counterId, invoice, invoice2, rule, erpPoint);

        // 優化原本的 SIT Debug 日誌，將內層轉義字元還原方便開發人員肉眼比對
        if (log.isInfoEnabled()) {
            String logXml = strXml.replaceAll("&lt;", "<")
                                  .replaceAll("&gt;", ">")
                                  .replaceAll("&quot;", "\"");
            log.info("==== SIT ERP 請求 XML (已還原標籤) ====\n{}", logXml);
        }

        // 發送 SOAP 請求
        String xmlResponse = erpServiceClient.getInvoiceNoSoap(strXml);
        log.info("==== SIT ERP 原始回傳 XML ====\n{}", xmlResponse);
        
        // 完美對接原生 DOM 的 JSONObject 回傳
        return parseUtil.parserERPPoint(xmlResponse);
    }
    
}