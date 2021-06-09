package com.example.cookup.Logic;

import java.io.Serializable;

import lombok.Data;

@Data
public class Preparation implements Serializable {
    private String pass;

    public Preparation(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return pass;
    }
}
