package com.howtodoinjava.app;

import com.howtodoinjava.app.dao.ItemRepository;
import com.howtodoinjava.app.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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

  /*
  @Override
  public void run(String... args) throws Exception {

	  
    itemRepository.save(new Item(
        "E-commerce Platform",
        "Spring Boot, React",
        "Reduced latency by 40%"
    ));

    itemRepository.save(new Item(
        "Jasper Reporting Tool",
        "JasperReports, Java",
        "Automated weekly business analytics"
    ));

    itemRepository.save(new Item(
        "Security Gateway",
        "OAuth2, JWT",
        "Implemented zero-trust architecture"
    ));

    itemRepository.save(new Item(
        "Distributed Order Management System",
        "Spring Boot, Kafka, Redis",
        "Handled 10k+ TPS with event-driven architecture"
    ));

    itemRepository.save(new Item(
        "Real-time Fraud Detection Platform",
        "Java, Flink, Kafka Streams",
        "Detected anomalies with <100ms latency"
    ));

    itemRepository.save(new Item(
        "Cloud-native Microservices Platform",
        "Spring Cloud, Docker, Kubernetes",
        "Deployed 50+ microservices with auto-scaling"
    ));

    itemRepository.save(new Item(
        "High-performance Payment Gateway",
        "Spring Boot, Netty, MySQL",
        "Processed millions of transactions daily"
    ));

    itemRepository.save(new Item(
        "Enterprise API Gateway",
        "Spring Cloud Gateway, OAuth2",
        "Centralized authentication for 100+ services"
    ));

    itemRepository.save(new Item(
        "IoT Data Ingestion Platform",
        "MQTT, Spring Boot, Cassandra",
        "Ingested millions of device messages per day"
    ));

    itemRepository.save(new Item(
        "Data Warehouse ETL Pipeline",
        "Spring Batch, PostgreSQL",
        "Processed TB-scale data nightly"
    ));

    itemRepository.save(new Item(
        "AI Recommendation Engine",
        "Python, TensorFlow, REST API",
        "Improved user engagement by 25%"
    ));

    itemRepository.save(new Item(
        "Legacy System Modernization",
        "Java, Spring Boot, RESTful API",
        "Migrated monolith to microservices architecture"
    ));

    itemRepository.save(new Item(
        "Banking Transaction System",
        "Spring Boot, Oracle DB",
        "Achieved ACID compliance with high concurrency"
    ));
  */
}
