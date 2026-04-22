package com.howtodojava.strategy;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class CsvReportStrategy implements ReportStrategy {

    @Override
    public byte[] export(JasperPrint jasperPrint) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JRCsvExporter exporter = new JRCsvExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(out));

        exporter.exportReport();

        return out.toByteArray();
    }

    @Override
    public String getFormat() {
        return "csv";
    }
}
