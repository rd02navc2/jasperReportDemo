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
