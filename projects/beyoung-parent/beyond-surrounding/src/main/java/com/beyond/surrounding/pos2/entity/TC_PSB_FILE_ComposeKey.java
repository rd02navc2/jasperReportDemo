package com.beyond.surrounding.pos2.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TC_PSB_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String tcPsbPlant;
    private String tcPsb01;
    private String tcPsb02;
    private String tcPsb03;
    private String tcPsb04;
    private Integer tcPsb06;
}