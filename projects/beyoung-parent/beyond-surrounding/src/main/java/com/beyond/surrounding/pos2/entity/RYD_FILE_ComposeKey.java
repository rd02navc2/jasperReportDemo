package com.beyond.surrounding.pos2.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RYD_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 屬性名稱必須與 RYD_FILE 中的 @Id 欄位完全一致
    private String RYD01;
    private String RYD10;
}