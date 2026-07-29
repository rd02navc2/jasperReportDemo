package com.beyond.surrounding.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException; // 強制拋出 Checked Exception
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Slf4j
@Component
public class ParseUtil {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * 核心邏輯：注意因應 configurationprocessor 規範，方法簽章加上了 throws JSONException
     */
    private JSONObject parseErpXmlToJson(String response) throws JSONException {
        JSONObject rootNode = new JSONObject();

        if (response == null || response.isEmpty()) {
            rootNode.put("code", "-1");
            rootNode.put("message", "Empty response");
            return rootNode;
        }

        String processedResponse = response.replaceAll("&lt;", "<")
                                           .replaceAll("&gt;", ">")
                                           .replaceAll("&quot;", "\"");

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(processedResponse)));
            doc.getDocumentElement().normalize();

            String erpCode = "";
            String erpMsg = "";
            NodeList statusList = doc.getElementsByTagName("Status");
            if (statusList.getLength() > 0) {
                Element statusElem = (Element) statusList.item(0);
                erpCode = statusElem.getAttribute("code");
                erpMsg = statusElem.getAttribute("description");
            }

            if ("0".equals(erpCode)) {
                rootNode.put("code", "0");
                rootNode.put("message", "0");

                NodeList fieldList = doc.getElementsByTagName("Field");
                for (int i = 0; i < fieldList.getLength(); i++) {
                    Node node = fieldList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;
                        rootNode.put(e.getAttribute("name"), e.getAttribute("value"));
                    }
                }
            } else {
                rootNode.put("code", erpCode);
                rootNode.put("message", erpMsg);
            }

        } catch (Exception e) {
            log.error("ERP XML 解析失敗: ", e);
            rootNode.put("code", "-1");
            rootNode.put("message", "Parse Error: " + e.getMessage());
        }

        return rootNode;
    }

    /**
     * 完全使用 Java 原生 DOM 解析 ERP 回傳，並包裝成 JSONObject
     * @throws JSONException 
     */
    public JSONObject parserERPInvoice(String xmlResponse) throws JSONException {
        JSONObject jsonResult = new JSONObject();
        try {
            if (xmlResponse == null || xmlResponse.isBlank()) {
                return createErrorResult("999", "ERP 回傳內容為空");
            }

            // 1. 初始化 Java 原生 DOM 解析器
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 關閉外部實體引入，防止 XML 注入攻擊 (XXE)
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 2. 解析外層 SOAP XML
            Document soapDoc = builder.parse(new InputSource(new StringReader(xmlResponse)));
            
            // 3. 尋找鼎新 ERP 的回傳節點 (可能叫 return 或 response)
            NodeList returnNodes = soapDoc.getElementsByTagName("return");
            if (returnNodes.getLength() == 0) {
                returnNodes = soapDoc.getElementsByTagName("response");
            }

            if (returnNodes.getLength() == 0) {
                log.error("無法在 SOAP 回傳中找到 'return' 或 'response' 節點。");
                return createErrorResult("999", "無法解析 ERP 回傳結構");
            }

            // 4. 取得內層被轉義的 XML 字串 (例如 &lt;Response&gt;...)
            String innerXml = returnNodes.item(0).getTextContent();
            if (innerXml == null || innerXml.isBlank()) {
                return createErrorResult("999", "ERP 內層資料為空");
            }

            // 5. 解析內層的 XML 字串
            Document responseDoc = builder.parse(new InputSource(new StringReader(innerXml)));
            Element responseRoot = responseDoc.getDocumentElement();

            // 6. 提取 <Execution status="..." code="..." description="..."/>
            NodeList executionNodes = responseRoot.getElementsByTagName("Execution");
            if (executionNodes.getLength() > 0) {
                Element execution = (Element) executionNodes.item(0);
                jsonResult.put("status", execution.getAttribute("status"));
                jsonResult.put("code", execution.getAttribute("code"));
                jsonResult.put("message", execution.getAttribute("description"));
            } else {
                jsonResult.put("status", "N");
                jsonResult.put("code", "-1");
                jsonResult.put("message", "找不到 Execution 節點");
            }

            // 7. 提取所有 <Field name="xxx" value="yyy"/>
            NodeList fieldNodes = responseRoot.getElementsByTagName("Field");
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Element field = (Element) fieldNodes.item(i);
                String name = field.getAttribute("name");
                String value = field.getAttribute("value");
                if (name != null && !name.isBlank()) {
                    jsonResult.put(name, value);
                }
            }

        } catch (Exception e) {
            log.error("原生解析 ERP XML 發生異常: ", e);
            return createErrorResult("500", "解析 XML 異常: " + e.getMessage());
        }

        return jsonResult;
    }

    private JSONObject createErrorResult(String code, String message) throws JSONException {
        JSONObject errorJson = new JSONObject();
        errorJson.put("status", "N");
        errorJson.put("code", code);
        errorJson.put("message", message);
        return errorJson;
    }


    /**
     * 補全成員點數接口：同樣直接調用核心轉換邏輯，消滅重複程式碼（DRY）
     */
    public JSONObject parserERPPoint(String response) {
       
        return parserERPResponse(response);
    }

    /**
     * 跨框架轉換：處理新模組需要的 Jackson JsonNode
     */
    public JSONObject parserERPResponse(String response) {
        JSONObject jsonResult = new JSONObject();
        try {
            if (response == null || response.isBlank()) {
                jsonResult.put("status", "N");
                jsonResult.put("code", "999");
                jsonResult.put("message", "ERP 回傳內容為空");
                return jsonResult;
            }

            // 1. 初始化 Java 原生 DOM 解析器並防止 XXE 攻擊
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 2. 解析外層 SOAP XML
            Document soapDoc = builder.parse(new InputSource(new StringReader(response)));
            
            // 3. 尋找鼎新 ERP 的回傳節點（可能是 return 或 response）
            NodeList returnNodes = soapDoc.getElementsByTagName("return");
            if (returnNodes.getLength() == 0) {
                returnNodes = soapDoc.getElementsByTagName("response");
            }

            if (returnNodes.getLength() == 0) {
                log.error("無法在 SOAP 回傳中找到 'return' 或 'response' 節點。");
                jsonResult.put("status", "N");
                jsonResult.put("code", "999");
                jsonResult.put("message", "無法解析 ERP 回傳結構");
                return jsonResult;
            }

            // 4. 取得內層被轉義的 XML 字串
            String innerXml = returnNodes.item(0).getTextContent();
            if (innerXml == null || innerXml.isBlank()) {
                jsonResult.put("status", "N");
                jsonResult.put("code", "999");
                jsonResult.put("message", "ERP 內層資料為空");
                return jsonResult;
            }

            // 5. 解析內層的 XML 字串
            Document responseDoc = builder.parse(new InputSource(new StringReader(innerXml)));
            Element responseRoot = responseDoc.getDocumentElement();

            // 6. 提取 <Execution status="..." code="..." description="..."/>
            NodeList executionNodes = responseRoot.getElementsByTagName("Execution");
            if (executionNodes.getLength() > 0) {
                Element execution = (Element) executionNodes.item(0);
                jsonResult.put("status", execution.getAttribute("status"));
                jsonResult.put("code", execution.getAttribute("code"));
                jsonResult.put("message", execution.getAttribute("description"));
            } else {
                jsonResult.put("status", "N");
                jsonResult.put("code", "-1");
                jsonResult.put("message", "找不到 Execution 節點");
            }

            // 7. 提取所有 <Field name="xxx" value="yyy"/> 并塞入 JSONObject
            NodeList fieldNodes = responseRoot.getElementsByTagName("Field");
            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Element field = (Element) fieldNodes.item(i);
                String name = field.getAttribute("name");
                String value = field.getAttribute("value");
                if (name != null && !name.isBlank()) {
                    jsonResult.put(name, value);
                }
            }

        } catch (Exception e) {
            log.error("XML 解析成 JSONObject 失敗: ", e);
            try {
                jsonResult.put("status", "N");
                jsonResult.put("code", "-1");
                jsonResult.put("message", e.getMessage());
            } catch (Exception ex) {
                // 防禦性空捕捉
            }
        }
        
        return jsonResult;
    }
    
    public static String getTagValue(String xml, String tagName, int i) {
		String _sValue = null;

		try {
			_sValue = xml.split("<" + tagName + ">")[1 + i].split("</" + tagName + ">")[0];
		} catch (ArrayIndexOutOfBoundsException var5) {
		}

		return _sValue;
	}
}