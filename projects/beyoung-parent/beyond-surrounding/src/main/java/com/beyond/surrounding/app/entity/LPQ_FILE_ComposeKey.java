package com.beyond.surrounding.app.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LPQ_FILE_ComposeKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lpq01;
    private String lpq03;
    private String lpqplant;
    private String lpq00;
    private String lpq13;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LPQ_FILE_ComposeKey other = (LPQ_FILE_ComposeKey) o;
        return Objects.equals(lpq01, other.lpq01) &&
               Objects.equals(lpq03, other.lpq03) &&
               Objects.equals(lpqplant, other.lpqplant) &&
               Objects.equals(lpq00, other.lpq00) &&
               Objects.equals(lpq13, other.lpq13);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lpq01, lpq03, lpqplant, lpq00, lpq13);
    }
    
}