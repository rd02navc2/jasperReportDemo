package com.beyoung.surrounding.spos.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TC_PSA_FILE_ComposeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String TC_PSAPLANT;
    private String TC_PSA01;
    private String TC_PSA02;
    private String TC_PSA03;
    private String TC_PSA04;
}