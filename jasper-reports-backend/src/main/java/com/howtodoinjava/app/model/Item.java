package com.howtodoinjava.app.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 庫存品項明細 Model
 */
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	
	@Id // 關鍵：必須定義主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
    private String itemName;      // 品項名稱
	
	@Column(nullable = false)
	@Min(0)
    private Integer stockQuantity; // 庫存量
	
	@Column(nullable = false)
    private String location;      // 儲位


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
    public Item(String itemName, Integer stockQuantity, String location) {
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.location = location;
    }

    /*
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
    */
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
