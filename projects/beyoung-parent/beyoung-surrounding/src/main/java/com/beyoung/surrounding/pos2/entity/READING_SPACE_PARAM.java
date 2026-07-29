package com.beyoung.surrounding.pos2.entity;

import jakarta.persistence.*; 
import lombok.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "POS2_READING_SPACE_PARAM")
@Table(name = "READING_SPACE_PARAM")
public class READING_SPACE_PARAM implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "param_name")
    private String paramName; // 建議遵循 Java 駝峰命名法，JPA 會自動對應或透過 @Column 指定

    @Column(name = "param_value")
    private String paramValue;
}