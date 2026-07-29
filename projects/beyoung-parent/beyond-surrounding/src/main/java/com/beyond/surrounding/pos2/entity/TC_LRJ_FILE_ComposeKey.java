package com.beyond.surrounding.pos2.entity;

import java.io.Serializable;
import jakarta.persistence.Column; // 記得引入
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TC_LRJ_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "TC_LRJ01", length = 50) // 與 Entity 完全同步
    private String TC_LRJ01;
    
    @Column(name = "TC_LRJ02", length = 50) // 與 Entity 完全同步
    private String TC_LRJ02;
    
    @Column(name = "TC_LRJ09", length = 50) // 與 Entity 完全同步
    private String TC_LRJ09;
    
    @Column(name = "TC_LRJPLANT", length = 50) // 與 Entity 完全同步
    private String TC_LRJPLANT;
}