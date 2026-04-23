package com.howtodoinjava.app.controller;

import com.howtodoinjava.app.repository.ItemRepository;
import com.howtodoinjava.app.service.JasperReportService;

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
