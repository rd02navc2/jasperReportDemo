package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "POS2_RYD_FILE")
@Table(name = "RYD_FILE")
@IdClass(RYD_FILE_ComposeKey.class) //  修正：精準綁定 RYD 專屬的複合主鍵
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RYD_FILE implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RYD01")
    @JsonProperty("RYD01") //  強制 XML/JSON 標籤維持大寫
    private String RYD01;

    @Column(name = "RYD02")
    @JsonProperty("RYD02")
    private String RYD02;

    @Id
    @Column(name = "RYD10")
    @JsonProperty("RYD10")
    private String RYD10;
}