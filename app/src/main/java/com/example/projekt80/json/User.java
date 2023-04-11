package com.example.projekt80.json;

import java.io.Serializable;

public class User implements Serializable {

    private String accessToken;

    private String name;

    private String password;

    public User(String name, String password, String accessToken) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
