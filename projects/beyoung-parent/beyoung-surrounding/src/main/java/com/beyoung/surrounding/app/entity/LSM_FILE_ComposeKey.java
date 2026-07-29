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
public class LSM_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lsm01;
    private String lsm02;
    private String lsm03;
    private Date lsm05;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LSM_FILE_ComposeKey that = (LSM_FILE_ComposeKey) o;
        return Objects.equals(lsm01, that.lsm01) &&
               Objects.equals(lsm02, that.lsm02) &&
               Objects.equals(lsm03, that.lsm03) &&
               Objects.equals(lsm05, that.lsm05);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lsm01, lsm02, lsm03, lsm05);
    }
}