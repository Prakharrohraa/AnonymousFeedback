package com.PrakharRohra.AnonymousFeedback.model.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    // Getters and Setters
    private String email;
    private String password;

    // Constructors
    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }
}
