package com.example.cookup.Logic;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import lombok.Data;

@Data
public class Recipe implements Serializable {
    private String name;
    private String dishtype;
    private String foodtype;
    private String description;
    private int servings;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Preparation> preparations = new ArrayList<>();
    private Date date;

    public Recipe(String name) {
        this.name = name;
    }

    public Recipe(){
    }

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }

    public void addPreparation(Preparation preparation){
        this.preparations.add(preparation);
    }

    @Override
    public String toString() {
        return name;
    }
}
