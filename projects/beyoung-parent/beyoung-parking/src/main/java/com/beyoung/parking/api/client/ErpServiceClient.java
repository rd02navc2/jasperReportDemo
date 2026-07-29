package com.beyoung.parking.api.client;

//dc-
// import com.beyoung.invoice.domain.bean.AppendInvoiceBean; // 確保有共享或引入此 DTO
import com.beyoung.parking.domain.bean.AppendInvoiceBean;
import com.beyoung.parking.domain.dto.ParkingDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// name 填入 Invoice 微服務在 Eureka/Consul/Nacos 註冊的服務名稱 (例如: invoice-service)
@FeignClient(
	    name = "erp-service", 
	    url = "${app.invoice-service.url:http://localhost:8097}", 
	    contextId = "erpServiceClient"
	)
public interface ErpServiceClient {


    
}

