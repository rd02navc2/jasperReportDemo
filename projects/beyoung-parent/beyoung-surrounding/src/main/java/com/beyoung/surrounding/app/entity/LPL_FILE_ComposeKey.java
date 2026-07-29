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
public class LPL_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lpl01;
    private Date lpl02;
    private Integer lpl09;
    private String lplplant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LPL_FILE_ComposeKey that = (LPL_FILE_ComposeKey) o;
        return Objects.equals(lpl01, that.lpl01) &&
               Objects.equals(lpl02, that.lpl02) &&
               Objects.equals(lpl09, that.lpl09) &&
               Objects.equals(lplplant, that.lplplant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lpl01, lpl02, lpl09, lplplant);
    }
    
}