package com.howtodoinjava.app;

import com.howtodoinjava.app.model.Item;
import com.howtodoinjava.app.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 獨立的測試資料產生工具
 * 執行此 Main 方法會清空資料庫並重新填入測試資料
 */
public class DataSeeder {

    public static void main(String[] args) {
        // 啟動 Spring Context 但不啟動 Web Server (如果配置了 web-env)
        ApplicationContext context = SpringApplication.run(App.class, args);
        ItemRepository itemRepository = context.getBean(ItemRepository.class);

        System.out.println("⚠️ 正在清空舊資料...");
        itemRepository.deleteAll();

        List<Item> items = List.of(
            // 電子零件類
            new Item("電容 100μF (CAP-100UF-25V)", 500, "區域 A 儲位 A-01"),
            new Item("電阻 10kΩ (RES-10K-0.25W)", 1200, "區域 A 儲位 A-02"),
            new Item("Arduino Uno R3", 8, "區域 A 儲位 A-03"),
            new Item("Raspberry Pi 4B", 3, "區域 A 儲位 A-04"),

            // 機械零件類
            new Item("六角螺絲 M6×20", 3000, "區域 B 儲位 B-01"),
            new Item("軸承 6205", 150, "區域 B 儲位 B-02"),
            new Item("O型環 Ø50", 5, "區域 B 儲位 B-03"),

            // 耗材類
            new Item("無塵布", 200, "區域 C 儲位 C-01"),
            new Item("酒精 75% 500ml", 80, "區域 C 儲位 C-02"),
            new Item("標籤紙 A4", 9, "區域 C 儲位 C-03")
        );

        itemRepository.saveAll(items);
        System.out.println("✅ 測試資料重置完成！共插入 " + items.size() + " 筆資料。");
        
        // 執行完畢後關閉 Context
        System.exit(0);
    }
}