package com.beyond.permission.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "email_address")
public class EMAIL_ADDRESS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "function", length = 100)
    private String function;

    @Column(name = "from", length = 255)
    private String from;

    @Column(name = "from_name", length = 255)
    private String fromName;

    @Column(name = "to", length = 255)
    private String to;

    @Column(name = "cc", length = 500)
    private String cc;

    @Column(name = "bcc", length = 500)
    private String bcc;
}