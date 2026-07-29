package com.beyoung.surrounding.pos2.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TC_PSC_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String tcPscPlant;
    private String tcPsc01;
    private String tcPsc02;
    private String tcPsc03;
    private String tcPsc04; // 修正：型態由 Date 改回 String，與主實體類別完全對齊
    private String tcPsc05;
    private String tcPsc07;
}