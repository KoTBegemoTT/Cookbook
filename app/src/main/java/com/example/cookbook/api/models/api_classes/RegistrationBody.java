package com.example.cookbook.api.models.api_classes;

public class RegistrationBody {
    public String email;
    public String username;
    public String password;

    public RegistrationBody(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
