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
public class TC_PSA_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tcPsaplant;
    private String tcPsa01;
    private String tcPsa02;
    private String tcPsa03;
    private Date tcPsa04;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TC_PSA_FILE_ComposeKey other = (TC_PSA_FILE_ComposeKey) o;
        return Objects.equals(tcPsaplant, other.tcPsaplant) &&
               Objects.equals(tcPsa01, other.tcPsa01) &&
               Objects.equals(tcPsa02, other.tcPsa02) &&
               Objects.equals(tcPsa03, other.tcPsa03) &&
               Objects.equals(tcPsa04, other.tcPsa04);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tcPsaplant, tcPsa01, tcPsa02, tcPsa03, tcPsa04);
    }
    
}