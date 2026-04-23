package com.howtodoinjava.app.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import com.itextpdf.html2pdf.HtmlConverter;
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

    public byte[] generateReport(List<?> data, String format) throws Exception {
        // 1. 準備數據
        Map<String, Object> model = new HashMap<>();
        model.put("items", data);
        model.put("title", "庫存明細報表 (FreeMarker)");

        // 2. 解析模板產生 HTML
        Template template = freemarkerConfig.getTemplate("item-report.ftl");
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        String htmlContent = writer.toString();

        // 3. 使用 iText 將 HTML 轉為 PDF
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(htmlContent, out);
        
        return out.toByteArray();
    }
}