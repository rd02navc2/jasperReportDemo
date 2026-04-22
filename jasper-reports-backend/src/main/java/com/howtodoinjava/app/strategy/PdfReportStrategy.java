package com.howtodoinjava.app.strategy;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Component;

@Component
public class PdfReportStrategy implements ReportStrategy {

    @Override
    public byte[] export(JasperPrint jasperPrint) throws Exception {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    @Override
    public String getFormat() {
        return "pdf";
    }
}
