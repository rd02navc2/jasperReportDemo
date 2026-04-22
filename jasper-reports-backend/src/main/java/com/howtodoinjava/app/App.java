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
	  
	  itemRepository.save(new Item(
		        "E-commerce Platform",
		        "Spring Boot, React",
		        "Reduced latency by 40%"
		    ));
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
