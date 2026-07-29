package com.beyoung.surrounding.app.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TC_PSB_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tcPsbplant;
    private String tcPsb01;
    private String tcPsb02;
    private String tcPsb03;
    private Date tcPsb04;
    private Integer tcPsb06;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TC_PSB_FILE_ComposeKey other = (TC_PSB_FILE_ComposeKey) o;
        return Objects.equals(tcPsbplant, other.tcPsbplant) &&
               Objects.equals(tcPsb01, other.tcPsb01) &&
               Objects.equals(tcPsb02, other.tcPsb02) &&
               Objects.equals(tcPsb03, other.tcPsb03) &&
               Objects.equals(tcPsb04, other.tcPsb04) &&
               Objects.equals(tcPsb06, other.tcPsb06);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcPsbplant, tcPsb01, tcPsb02, tcPsb03, tcPsb04, tcPsb06);
    }
    
}