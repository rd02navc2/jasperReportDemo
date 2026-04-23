package com.howtodoinjava.app.controller;

import com.howtodoinjava.app.model.Item;
import com.howtodoinjava.app.repository.ItemRepository;
import com.howtodoinjava.app.service.FreemarkerItextService;
import com.howtodoinjava.app.service.ItextNativeService;
import com.howtodoinjava.app.service.JasperReportService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired private JasperReportService jasperService;
    @Autowired private FreemarkerItextService freemarkerService;
    @Autowired private ItextNativeService itextService;
    @Autowired private ItemRepository itemRepository;

    @GetMapping("/{engine}/item-report")
    public ResponseEntity<Resource> getReport(
            @PathVariable String engine,
            @RequestParam String format) throws Exception {

        List<Item> data = itemRepository.findAll();
        byte[] content;

        switch (engine.toLowerCase()) {
            case "jasper":
                content = jasperService.generateReport(data, format);
                break;
            case "freemarker":
                content = freemarkerService.generateReport(data, format);
                break;
            case "itext":
                content = itextService.generateReport(data);
                break;
            default:
                throw new IllegalArgumentException("Unsupported engine: " + engine);
        }

        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + engine + ".pdf")
                .body(resource);
    }
}


/*
@RestController
@RequestMapping("/api/reports")
public class ReportController {

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  JasperReportService jasperReportService;

  @GetMapping("/item-report")
  public ResponseEntity<Resource> getItemReport(
		  @RequestParam String format
      )throws Exception { 

    byte[] reportContent =
        jasperReportService.generateReport(itemRepository.findAll(), format);
    // jasperReportService.getItemReport(itemRepository.findAll(), format);

    ByteArrayResource resource = new ByteArrayResource(reportContent);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .contentLength(resource.contentLength())
        .header(HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment()
                .filename("item-report." + format)
                .build()
                .toString())
        .body(resource);
  }
}
*/