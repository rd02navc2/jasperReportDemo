package com.beyond.surrounding.spos.bean;

import com.beyond.surrounding.spos.entity.TC_PSA_FILE;
import com.beyond.surrounding.spos.entity.TC_PSB_FILE;
import com.beyond.surrounding.spos.entity.TC_PSC_FILE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 加入這段註解，強制 Jackson 繞過 JavaBean 命名陷阱，完美映射純大寫的 List 欄位！
@com.fasterxml.jackson.annotation.JsonAutoDetect(
    fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY,
    getterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE,
    setterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE
)
public class SPOSBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // 單頭資料 (一筆)
    private TC_PSA_FILE TC_PSA_FILE;

    // 單身商品/料件明細 (多筆)
    private List<TC_PSB_FILE> TC_PSB_FILE;

    // 付款/促銷明細 (多筆)
    private List<TC_PSC_FILE> TC_PSC_FILE;
    
}