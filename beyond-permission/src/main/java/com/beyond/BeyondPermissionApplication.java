package com.beyond;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@ConfigurationPropertiesScan
@SpringBootApplication
@EntityScan(basePackages = "com.beyond.permission.entity")
public class BeyondPermissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeyondPermissionApplication.class, args);
	}

}
