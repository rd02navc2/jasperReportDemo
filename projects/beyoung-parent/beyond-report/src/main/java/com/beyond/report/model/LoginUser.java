package com.beyond.report.model;

import java.io.Serializable;

/**
 * 登入後存放於 Session 中的使用者資訊
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Session 存放此物件的 key */
    public static final String SESSION_KEY = "LOGIN_USER";

    private String userid;
    private String username;
    private String role;

    public LoginUser() {
    }

    public LoginUser(String userid, String username, String role) {
        this.userid = userid;
        this.username = username;
        this.role = role;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
