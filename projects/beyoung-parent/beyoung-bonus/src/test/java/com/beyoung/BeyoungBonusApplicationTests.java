package com.beyoung;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootTest
@EnableFeignClients(basePackages = "com.beyoung.bonus") // 強制指定掃描整個 bonus 目錄
class BeyoungBonusApplicationTests {

	@Test
	void contextLoads() {
	}

}
