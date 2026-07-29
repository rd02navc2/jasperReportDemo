package com.beyoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

//dc- @EnableScheduling
@SpringBootApplication(scanBasePackages = "com.beyoung")
@EnableFeignClients(basePackages = "com.beyoung.bonus") 
public class BeyoungBonusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeyoungBonusApplication.class, args);
	}

}
