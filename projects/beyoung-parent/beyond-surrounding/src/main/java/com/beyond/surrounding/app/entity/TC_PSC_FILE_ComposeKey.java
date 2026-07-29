package com.beyond.surrounding.app.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_PSC_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tcPscplant;
    private String tcPsc01;
    private String tcPsc02;
    private String tcPsc03;
    private Date tcPsc04;
    private String tcPsc05;
    private String tcPsc07;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TC_PSC_FILE_ComposeKey other = (TC_PSC_FILE_ComposeKey) o; // 已修正舊版誤植為 TC_PSB 的 Bug
        return Objects.equals(tcPscplant, other.tcPscplant) &&
               Objects.equals(tcPsc01, other.tcPsc01) &&
               Objects.equals(tcPsc02, other.tcPsc02) &&
               Objects.equals(tcPsc03, other.tcPsc03) &&
               Objects.equals(tcPsc04, other.tcPsc04) &&
               Objects.equals(tcPsc05, other.tcPsc05) &&
               Objects.equals(tcPsc07, other.tcPsc07);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcPscplant, tcPsc01, tcPsc02, tcPsc03, tcPsc04, tcPsc05, tcPsc07);
        
    }
}