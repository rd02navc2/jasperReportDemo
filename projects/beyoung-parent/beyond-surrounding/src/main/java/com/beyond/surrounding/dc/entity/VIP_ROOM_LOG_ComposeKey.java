package com.beyond.surrounding.dc.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Data // 自動生成安全正確的 equals()、hashCode()、Getter、Setter
@NoArgsConstructor
@AllArgsConstructor
public class VIP_ROOM_LOG_ComposeKey implements Serializable {
    private static final long serialVersionUID = 1L;

    // 變數名稱與主 Entity 完全保持一致
    private String center;
    private Date transactionDate; 
    private String userId;
}