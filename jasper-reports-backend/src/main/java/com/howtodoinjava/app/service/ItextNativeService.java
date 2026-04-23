package com.howtodoinjava.app.service;

import com.howtodoinjava.app.model.Item;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ItextNativeService {

    public byte[] generateReport(List<Item> data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 標題
        document.add(new Paragraph("Stock Item Report (iText Native)").setFontSize(18));

        // 建立表格 (3 欄)
        float[] columnWidths = {3, 1, 2};
        Table table = new Table(UnitValue.createPointArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // 表頭
        table.addHeaderCell("Item Name");
        table.addHeaderCell("Quantity");
        table.addHeaderCell("Location");

        // 填入數據
        for (Item item : data) {
            table.addCell(item.getItemName());
            table.addCell(String.valueOf(item.getStockQuantity()));
            table.addCell(item.getLocation());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }
}