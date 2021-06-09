package com.example.cookup.Logic;


import com.example.cookup.Logic.Enums.Type;

import java.io.Serializable;

import lombok.Data;

@Data
public class Ingredient implements Serializable {
    private String ingredient;
    private int amount;
    private Type type;

    public Ingredient(String ingredient, int amount, Type type) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public String toString() {
        return ingredient + " " + amount + " " + type;
    }
}
