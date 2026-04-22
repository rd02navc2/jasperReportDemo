package com.howtodoinjava.app.strategy;

import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportStrategyManager {

    private final Map<String, ReportStrategy> strategyMap;

    public ReportStrategyManager(List<ReportStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ReportStrategy::getFormat,
                        s -> s
                ));
    }

    public byte[] export(String format, JasperPrint jasperPrint) throws Exception {

        ReportStrategy strategy = strategyMap.get(format.toLowerCase());

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported format: " + format);
        }

        return strategy.export(jasperPrint);
    }
}
