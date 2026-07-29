package com.beyond.surrounding.spos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import java.io.Serializable;

@Entity(name = "SPOS_RYD_FILE")
@Table(name = "RYD_FILE")
@IdClass(RYD_FILE_ComposeKey.class) //  綁定複合主鍵類別
@Getter
@Setter
@AllArgsConstructor
@Builder
public class RYD_FILE implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "RYD01")
    private String RYD01; //  複合主鍵之一

    @Column(name = "RYD02")
    private String RYD02;

    @Id
    @Column(name = "RYD10")
    private String RYD10; //  複合主鍵之二

    // 手動寫出無參建構子，徹底阻斷 Lombok 與 Hibernate 的反射衝突地雷
    public RYD_FILE() {
    }
}