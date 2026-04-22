package com.howtodoinjava.app.strategy;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class ExcelReportStrategy implements ReportStrategy {

    @Override
    public byte[] export(JasperPrint jasperPrint) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setDetectCellType(true);
        config.setCollapseRowSpan(false);

        exporter.setConfiguration(config);
        exporter.exportReport();

        return out.toByteArray();
    }

    @Override
    public String getFormat() {
        return "excel";
    }
}
