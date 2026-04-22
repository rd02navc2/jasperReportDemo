package com.howtodoinjava.app.strategy;

import net.sf.jasperreports.engine.JasperPrint;

public interface ReportStrategy {
    byte[] export(JasperPrint jasperPrint) throws Exception;
    String getFormat();
}
