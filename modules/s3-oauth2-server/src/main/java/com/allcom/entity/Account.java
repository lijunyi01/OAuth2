package com.allcom.entity;

import java.io.Serializable;

/**
 * Created by ljy on 2018/5/2.
 * ok
 */
@SuppressWarnings("serial")
public class Account implements Serializable {

    private Integer id;
    private String userName;
    private String email;
    private String password;
    private String roleString;

    public Account() {
        this.id = -1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleString() {
        return roleString;
    }

    public void setRoleString(String roleString) {
        this.roleString = roleString;
    }
}
