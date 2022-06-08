package com.example.cookbook.api.models.api_classes;

public class User {
    public String email;
    public String username;
    public String password;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
