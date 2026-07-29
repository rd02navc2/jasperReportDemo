package com.beyond.surrounding.ec.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.*;

@Data // 自動生成 Getter, Setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class TC_LRI_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String TC_LRI01;
    private String TC_LRI02;
    private Double TC_LRI03;
    private String TC_LRIPLANT;

    // 安全覆寫：確保多重主鍵在 JPA 託管時能精確比對
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TC_LRI_FILE_ComposeKey that = (TC_LRI_FILE_ComposeKey) o;
        return Objects.equals(TC_LRI01, that.TC_LRI01) && 
               Objects.equals(TC_LRI02, that.TC_LRI02) && 
               Objects.equals(TC_LRI03, that.TC_LRI03) && 
               Objects.equals(TC_LRIPLANT, that.TC_LRIPLANT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TC_LRI01, TC_LRI02, TC_LRI03, TC_LRIPLANT);
    }
    
}