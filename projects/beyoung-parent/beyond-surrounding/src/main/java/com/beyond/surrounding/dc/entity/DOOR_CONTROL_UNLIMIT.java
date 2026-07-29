package com.beyond.surrounding.dc.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DOOR_CONTROL_UNLIMIT")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class DOOR_CONTROL_UNLIMIT implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "user_name")
    private String userName;

    // 動補回無參建構子，防止 Hibernate 反射實例化失敗
    public DOOR_CONTROL_UNLIMIT() {
    }
    
}