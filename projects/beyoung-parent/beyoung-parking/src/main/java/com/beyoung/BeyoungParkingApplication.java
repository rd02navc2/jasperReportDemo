package com.beyoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.beyoung.parking") // 強制指定掃描整個 parking 目錄
public class BeyoungParkingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeyoungParkingApplication.class, args);
	}

}
