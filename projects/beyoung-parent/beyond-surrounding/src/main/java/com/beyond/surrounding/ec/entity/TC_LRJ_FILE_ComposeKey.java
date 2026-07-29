package com.beyond.surrounding.ec.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.*;

@Data // 自動產生 Getter, Setter, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class TC_LRJ_FILE_ComposeKey implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String TC_LRJ01;
    private String TC_LRJ02; //  修正錯字：移除舊版錯誤的 'I'，與實體類別完全對齊
    private String TC_LRJ09;
    private String TC_LRJPLANT;

    // 覆寫 equals，改用穩健的 Objects.equals 防止記憶體位址比對失敗
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TC_LRJ_FILE_ComposeKey that = (TC_LRJ_FILE_ComposeKey) o;
        return Objects.equals(TC_LRJ01, that.TC_LRJ01) && 
               Objects.equals(TC_LRJ02, that.TC_LRJ02) && 
               Objects.equals(TC_LRJ09, that.TC_LRJ09) && 
               Objects.equals(TC_LRJPLANT, that.TC_LRJPLANT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TC_LRJ01, TC_LRJ02, TC_LRJ09, TC_LRJPLANT);
    }
    
}