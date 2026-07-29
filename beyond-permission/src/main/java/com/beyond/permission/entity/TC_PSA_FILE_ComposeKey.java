package com.beyond.permission.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.*;

/**
 * 複合主鍵類別 (Composite Primary Key)
 * 依據 JPA 規範，必須為 public 且實作 Serializable
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TC_PSA_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String TC_PSAPLANT;
    private String TC_PSA01;
    private String TC_PSA02;
    private String TC_PSA03;
    private Date TC_PSA04;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TC_PSA_FILE_ComposeKey that = (TC_PSA_FILE_ComposeKey) o;
        return Objects.equals(TC_PSAPLANT, that.TC_PSAPLANT) &&
               Objects.equals(TC_PSA01, that.TC_PSA01) &&
               Objects.equals(TC_PSA02, that.TC_PSA02) &&
               Objects.equals(TC_PSA03, that.TC_PSA03) &&
               Objects.equals(TC_PSA04, that.TC_PSA04);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TC_PSAPLANT, TC_PSA01, TC_PSA02, TC_PSA03, TC_PSA04);
    }
}