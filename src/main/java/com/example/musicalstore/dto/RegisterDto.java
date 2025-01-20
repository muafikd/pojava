package com.example.musicalstore.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class RegisterDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
