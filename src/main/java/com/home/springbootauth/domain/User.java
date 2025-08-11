package com.home.springbootauth.domain;

import lombok.Getter;

@Getter
public class User {
    private String name;
    private String password;
    private String passwordHash;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
