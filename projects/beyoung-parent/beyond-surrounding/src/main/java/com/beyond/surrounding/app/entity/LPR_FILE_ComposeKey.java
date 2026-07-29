package com.beyond.surrounding.app.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LPR_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lpr01;
    private String lprplant;
    private Integer lpr06;
    private String lpr08;
    private String lpr00;
    private String lpr09;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LPR_FILE_ComposeKey other = (LPR_FILE_ComposeKey) o;
        return Objects.equals(lpr01, other.lpr01) &&
               Objects.equals(lprplant, other.lprplant) &&
               Objects.equals(lpr06, other.lpr06) &&
               Objects.equals(lpr08, other.lpr08) &&
               Objects.equals(lpr00, other.lpr00) &&
               Objects.equals(lpr09, other.lpr09);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lpr01, lprplant, lpr06, lpr08, lpr00, lpr09);
    }
    
}