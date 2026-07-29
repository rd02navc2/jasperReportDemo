package com.beyond.permission.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ListValue")
public class ListValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;
    private String label;
    @Column(name = "`desc`") // desc 在許多 SQL 資料庫中是關鍵字，建議加上轉義
    private String desc;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ListValue [" + (id != null ? "id=" + id + ", " : "") 
                + (value != null ? "value=" + value + ", " : "") 
                + (label != null ? "label=" + label : "") + "]";
    }
}