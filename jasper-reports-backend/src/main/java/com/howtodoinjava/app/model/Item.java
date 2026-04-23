package com.howtodoinjava.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 庫存品項明細 Model
 */
@Entity
public class Item {
	
	@Id // ✨ 關鍵：必須定義主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String itemName;      // 品項名稱
    private Integer stockQuantity; // 庫存量
    private String location;      // 儲位

    public Item() {}

    public Item(String itemName, Integer stockQuantity, String location) {
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.location = location;
    }

    // Getter 與 Setter (必須存在，JasperReports 依賴 Getter 讀取資料)
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


/*
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String technology;
    private String achievement;

    // ⭐ business constructor（重點）
    public Item(String projectName, String technology, String achievement) {
        this.projectName = projectName;
        this.technology = technology;
        this.achievement = achievement;
    }
}

*/
