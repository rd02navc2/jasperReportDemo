package com.beyond.surrounding.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;

@Data
@SuperBuilder // 讓子類別能繼承建構器
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // 自動隱藏 null 欄位，減少傳輸量
public class ActionResponseDTO extends BaseResponseDTO implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private String actionType;
    private String moPayType;
    private String couponType;
    private Integer price;
    private String payType;
}
