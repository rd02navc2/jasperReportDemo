package com.howtodoinjava.app;

import com.howtodoinjava.app.model.Item;
import com.howtodoinjava.app.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.howtodoinjava.app.App;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.howtodoinjava.app.repository")
@EntityScan(basePackages = "com.howtodoinjava.app.model")
public class App implements CommandLineRunner {

  @Autowired
  ItemRepository itemRepository;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
	// TODO Auto-generated method stub
	
	// 正確的庫存資料模擬
	  itemRepository.save(new Item(
	      "Intel Core i9 處理器", // 品項名稱 (itemName)
	      150,                  // 庫存量 (stockQuantity - 應該是數字)
	      "倉庫 A-05-12"         // 儲位 (location)
	  ));
	  
	// 電子零件類
	  itemRepository.save(new Item("電容 100μF (CAP-100UF-25V)", 500, "區域 A 儲位 A-01"));
	  itemRepository.save(new Item("電阻 10kΩ (RES-10K-0.25W)", 1200, "區域 A 儲位 A-02"));
	  itemRepository.save(new Item("Arduino Uno R3", 8, "區域 A 儲位 A-03"));
	  itemRepository.save(new Item("Raspberry Pi 4B", 3, "區域 A 儲位 A-04"));

	  // 機械零件類
	  itemRepository.save(new Item("六角螺絲 M6×20", 3000, "區域 B 儲位 B-01"));
	  itemRepository.save(new Item("軸承 6205", 150, "區域 B 儲位 B-02"));
	  itemRepository.save(new Item("O型環 Ø50", 5, "區域 B 儲位 B-03"));

	  // 耗材類
	  itemRepository.save(new Item("無塵布", 200, "區域 C 儲位 C-01"));
	  itemRepository.save(new Item("酒精 75% 500ml", 80, "區域 C 儲位 C-02"));
	  itemRepository.save(new Item("標籤紙 A4", 9, "區域 C 儲位 C-03"));
	  
	  
	  /*
	  itemRepository.save(new Item(
		        "E-commerce Platform",
		        "Spring Boot, React",
		        "Reduced latency by 40%"
		    ));
		    */
  }
  
  public class DataSeeder {

	    public static void main(String[] args) {

	        ApplicationContext context = SpringApplication.run(App.class, args);

	        ItemRepository itemRepository = context.getBean(ItemRepository.class);

	        // 🔥 清空舊資料（可選）
	        itemRepository.deleteAll();

	        // 電子零件類
	        itemRepository.save(new Item("電容 100μF (CAP-100UF-25V)", 500, "區域 A 儲位 A-01"));
	        itemRepository.save(new Item("電阻 10kΩ (RES-10K-0.25W)", 1200, "區域 A 儲位 A-02"));
	        itemRepository.save(new Item("Arduino Uno R3", 8, "區域 A 儲位 A-03"));
	        itemRepository.save(new Item("Raspberry Pi 4B", 3, "區域 A 儲位 A-04"));

	        // 機械零件類
	        itemRepository.save(new Item("六角螺絲 M6×20", 3000, "區域 B 儲位 B-01"));
	        itemRepository.save(new Item("軸承 6205", 150, "區域 B 儲位 B-02"));
	        itemRepository.save(new Item("O型環 Ø50", 5, "區域 B 儲位 B-03"));

	        // 耗材類
	        itemRepository.save(new Item("無塵布", 200, "區域 C 儲位 C-01"));
	        itemRepository.save(new Item("酒精 75% 500ml", 80, "區域 C 儲位 C-02"));
	        itemRepository.save(new Item("標籤紙 A4", 9, "區域 C 儲位 C-03"));

	        System.out.println("✅ 測試資料建立完成！");
	    }
  }
}
