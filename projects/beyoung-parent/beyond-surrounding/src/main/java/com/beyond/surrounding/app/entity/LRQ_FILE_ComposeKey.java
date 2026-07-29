package com.beyond.surrounding.app.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LRQ_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lrq12;
    private String lrq13;
    private String lrq02;
    private String lrqplant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRQ_FILE_ComposeKey other = (LRQ_FILE_ComposeKey) o;
        return Objects.equals(lrq12, other.lrq12) &&
               Objects.equals(lrq13, other.lrq13) &&
               Objects.equals(lrq02, other.lrq02) &&
               Objects.equals(lrqplant, other.lrqplant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lrq12, lrq13, lrq02, lrqplant);
    }
    
}