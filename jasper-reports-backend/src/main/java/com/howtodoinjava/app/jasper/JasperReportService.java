package com.howtodoinjava.app.jasper;

import com.howtodoinjava.app.model.Item;
import com.howtodoinjava.app.strategy.ReportStrategyManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class JasperReportService {

    @Autowired
    private ReportStrategyManager strategyManager;

    public byte[] generateReport(List<?> data, String format)
            throws Exception {

        JRBeanCollectionDataSource ds =
                new JRBeanCollectionDataSource(data);

        InputStream template =
                this.getClass().getResourceAsStream("/reports/item-report.jrxml");

        JasperReport report =
                JasperCompileManager.compileReport(template);

        JasperPrint print =
                JasperFillManager.fillReport(report, new HashMap<>(), ds);

        return strategyManager.export(format, print);
    }
}



/*
@Service
public class JasperReportService {

    public byte[] getItemReport(List<Item> items, String format) {
        try {
            // 1. Load the JRXML from the classpath
            InputStream reportStream = getClass().getResourceAsStream("/item-report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 2. Map data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);

            // 3. Set Parameters (Must match 'ReportTitle' in JRXML)
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "David's Project Report");

            // 4. Fill and Export
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return switch (format.toLowerCase()) {
                case "pdf" -> JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new IllegalArgumentException("Unsupported format: " + format);
            };
        } catch (JRException e) {
            throw new RuntimeException("Error generating Jasper report", e);
        }
    }
}
*/
