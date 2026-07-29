package com.beyoung.surrounding.spos.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TC_PSC_FILE_ComposeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String TC_PSCPLANT;
    private String TC_PSC01;
    private String TC_PSC02;
    private String TC_PSC03;
    private String TC_PSC04; // 保持與 Entity 型態一致
    private String TC_PSC05;
    private String TC_PSC07;
}