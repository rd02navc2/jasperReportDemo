package com.beyond.surrounding.spos.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TC_PSB_FILE_ComposeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String TC_PSBPLANT;
    private String TC_PSB01;
    private String TC_PSB02;
    private String TC_PSB03;
    private String TC_PSB04;
    private Integer TC_PSB06;
}