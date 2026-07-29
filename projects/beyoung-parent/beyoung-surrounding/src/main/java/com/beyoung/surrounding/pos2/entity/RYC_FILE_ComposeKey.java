package com.beyoung.surrounding.pos2.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RYC_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 屬性名稱必須與 RYC_FILE 中的 @Id 欄位名稱、型態完全一致
    private String RYC00;
    private String RYC01;
}