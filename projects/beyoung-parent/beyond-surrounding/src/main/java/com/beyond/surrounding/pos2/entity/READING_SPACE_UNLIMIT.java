package com.beyond.surrounding.pos2.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "POS2_READING_SPACE_UNLIMIT")
@Table(name = "READING_SPACE_UNLIMIT")
public class READING_SPACE_UNLIMIT implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private String userId; 

    @Column(name = "user_name")
    private String userName; 
}