package com.example.cookup.Logic;


import lombok.Data;

@Data
public class User {
    private String token;

    public User(String token){
        this.token = token;
    }
}
