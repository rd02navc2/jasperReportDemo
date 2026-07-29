package com.beyond.report.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "Login")
public class Login implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "login_user") 
    private String loginUser;

    public Login() {
    }

    public Login(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser; 
    }

    public String getLoginId() {
        return loginUser;
    }

    public void setLoginId(String loginId) {
        this.loginUser = loginId;
    }
}