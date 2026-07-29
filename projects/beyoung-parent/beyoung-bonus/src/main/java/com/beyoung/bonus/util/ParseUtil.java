package com.beyoung.bonus.util;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

// import net.sf.json.JSONObject;
// import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Slf4j
public class ParseUtil {
	
	public static JsonNode parserERPPoint(String response) {
	    // 1. 初始化 Jackson ObjectMapper 與 ObjectNode
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectNode rootNode = mapper.createObjectNode();
	
	    // 處理轉義字元 (建議增加 &amp; 處理以防萬一)
	    response = response.replaceAll("&lt;", "<")
	                       .replaceAll("&gt;", ">")
	                       .replaceAll("&quot;", "\""); // 修正原代碼缺少分號的問題
	
	    try {
	        // 2. 解析 XML
	        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        InputSource is = new InputSource(new StringReader(response));
	        Document doc = db.parse(is);
	        doc.getDocumentElement().normalize();
	
	        // 3. 取得狀態資訊
	        NodeList nList = doc.getElementsByTagName("Status");
	        String erpCode = "";
	        String erpMsg = "";
	
	        if (nList.getLength() > 0) {
	            Node nNode = nList.item(0);
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                Element eElement = (Element) nNode;
	                erpCode = eElement.getAttribute("code");
	                erpMsg = eElement.getAttribute("description");
	            }
	        }
	
	        // 4. 根據 code 封裝 JsonNode
	        if ("0".equals(erpCode)) {
	            rootNode.put("code", "0");
	            rootNode.put("message", "0");
	            
	            NodeList nList1 = doc.getElementsByTagName("Field");
	            for (int temp = 0; temp < nList1.getLength(); ++temp) {
	                Node nNode = nList1.item(temp);
	                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                    Element eElement = (Element) nNode;
	                    String fieldName = eElement.getAttribute("name");
	                    String fieldValue = eElement.getAttribute("value");
	                    rootNode.put(fieldName, fieldValue);
	                }
	            }
	        } else {
	            rootNode.put("code", erpCode);
	            rootNode.put("message", erpMsg);
	        }
	
	    } catch (Exception e) {
	        // 5. 異常處理
	        rootNode.put("code", -1);
	        rootNode.put("message", "解析失敗: " + e.getMessage());
	    }
	
	    return rootNode; // ObjectNode 繼承自 JsonNode
	}

	private static JsonNode parseErpXmlToJson(String response) {
		ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        if (response == null || response.isEmpty()) {
            return rootNode.put("code", -1).put("message", "Empty response");
        }

        // 預處理轉義字元
        response = response.replaceAll("&lt;", "<")
                           .replaceAll("&gt;", ">")
                           .replaceAll("&quot;", "\"");

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(response)));
            doc.getDocumentElement().normalize();

            // 1. 取得 Status 資訊
            String erpCode = "";
            String erpMsg = "";
            NodeList statusList = doc.getElementsByTagName("Status");
            if (statusList.getLength() > 0) {
                Element statusElem = (Element) statusList.item(0);
                erpCode = statusElem.getAttribute("code");
                erpMsg = statusElem.getAttribute("description");
            }

            // 2. 根據 erpCode 封裝結果
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
            rootNode.put("code", -1);
            rootNode.put("message", "Parse Error: " + e.getMessage());
        }

        return rootNode;
    }

    public static JsonNode parserERPInvoice(String response) {
        return parseErpXmlToJson(response);
    }

    public static JsonNode parserERPResponse(String response) {
        return parseErpXmlToJson(response);
    }

	public static String getTagValue(String xml, String tagName, int i) {
		String _sValue = null;

		try {
			_sValue = xml.split("<" + tagName + ">")[1 + i].split("</" + tagName + ">")[0];
		} catch (ArrayIndexOutOfBoundsException var5) {
		}

		return _sValue;
	}

	public static void main(String[] args) {
        // 測試 getTagValue
        String xml = "<stackusers><name>Yash</name><age>30</age><age>29</age></stackusers>";
        System.out.println("Age at index 1: " + getTagValue(xml, "age", 1));
        
        // 測試 parser (範例 XML)
        String erpXml = "<Response><Status code=\"0\" description=\"Success\"/><Field name=\"InvNo\" value=\"AB12345678\"/></Response>";
        System.out.println("JSON Result: " + parserERPInvoice(erpXml).toString());
    }
	
}
