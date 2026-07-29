package com.beyoung.surrounding.pos2.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TC_PSA_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String tcPsaPlant;
    private String tcPsa01;
    private String tcPsa02;
    private String tcPsa03;
    private String tcPsa04;
}