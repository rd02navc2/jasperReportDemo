package com.beyoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.beyoung.member") // 強制指定掃描整個 member 目錄
public class BeyoungMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeyoungMemberApplication.class, args);
	}

}
