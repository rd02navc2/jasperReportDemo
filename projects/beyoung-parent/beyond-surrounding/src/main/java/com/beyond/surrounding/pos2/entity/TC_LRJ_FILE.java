package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.Column; // 記得導入這個套件
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "POS2_TC_LRJ_FILE")
@Table(name = "TC_LRJ_FILE")
@IdClass(TC_LRJ_FILE_ComposeKey.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_LRJ_FILE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TC_LRJ01", length = 50) // 1. 限制長度為 50
    private String TC_LRJ01;
    
    @Id
    @Column(name = "TC_LRJ02", length = 50) // 2. 限制長度為 50
    private String TC_LRJ02;
    
    @Id
    @Column(name = "TC_LRJ09", length = 50) // 3. 限制長度為 50
    private String TC_LRJ09;
    
    private Double TC_LRJ03;
    
    @Id
    @Column(name = "TC_LRJPLANT", length = 50) // 4. 限制長度為 50
    private String TC_LRJPLANT;
    
    private Double TC_LRJ04;
    
    private Double TC_LRJ05;
    
    private String TC_LRJ06;
    
    private String TC_LRJ07;
    
    private String TC_LRJ08;
    
    private String TC_LRJACTI;
}