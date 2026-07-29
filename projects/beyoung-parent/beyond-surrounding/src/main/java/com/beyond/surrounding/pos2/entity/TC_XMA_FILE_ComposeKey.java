package com.beyond.surrounding.pos2.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TC_XMA_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 修正點：與 TC_XMA_FILE 內部的 @Id 欄位型態完全對齊
    private Integer TC_XMA05;
    private Integer TC_XMA06;
    private String TC_XMA07;
}