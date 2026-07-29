package com.beyoung.bonus.infrastructure;

import lombok.*;
import java.io.Serializable;

import jakarta.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode // JPA 規範：複合主鍵類別必須重寫 equals() 與 hashCode()，防範快取與關聯查詢失效
public class LrqFileId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lrq01; // 活動規則代碼
    
    private String lrq02; // 活動代號
    
    private String lrqplant; // 營運據點 / 店別代號
}