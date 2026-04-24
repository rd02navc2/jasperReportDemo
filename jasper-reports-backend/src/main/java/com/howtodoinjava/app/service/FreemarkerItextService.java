package com.howtodoinjava.app.service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FreemarkerItextService {

    @Autowired
    private Configuration freemarkerConfig;

    /**
     * 產生 PDF 報表
     * @param data 報表資料列表 (例如 List<Item>)
     * @param format 檔案格式 (預留擴充用)
     * @return PDF 位元組陣列
     */
    public byte[] generateReport(List<?> data, String format) throws Exception {
        try {
            // 1. 準備數據模型 (Model)
            Map<String, Object> model = new HashMap<>();
            model.put("data", data);
            model.put("title", "庫存明細報表 (FreeMarker)");

            // 2. 解析 FreeMarker 模板產生 HTML 字串
            // 請確保模板檔案位於 src/main/resources/templates/item-report.ftl
            Template template = freemarkerConfig.getTemplate("item-report.ftl");
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            String htmlContent = writer.toString();

            // 3. 配置 iText 轉換屬性 (解決中文關鍵)
            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();
            
            // 註冊系統字型 (會掃描 Windows/Fonts 下的微軟正黑體等)
            // 如果部署在 Linux/Docker，請確保該環境已安裝中文字型
            fontProvider.addSystemFonts(); 
            properties.setFontProvider(fontProvider);

            // 4. 使用 iText 將 HTML 轉為 PDF 位元組流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(htmlContent, out, properties);
            
            return out.toByteArray();

        } catch (Exception e) {
            // 在 Console 印出詳細錯誤堆疊，幫助定位 FTL 變數不匹配或字型遺失問題
            System.err.println("報表產生失敗，錯誤原因: " + e.getMessage());
            e.printStackTrace(); 
            throw e;
        }
    }
}