package com.beyoung;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootTest
@EnableFeignClients(basePackages = "com.beyoung.member") // 強制指定掃描整個 member 目錄
class BeyoungMemberApplicationTests {

	@Test
	void contextLoads() {
	}

}
