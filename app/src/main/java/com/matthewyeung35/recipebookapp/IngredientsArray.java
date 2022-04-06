package com.matthewyeung35.recipebookapp;

import java.util.ArrayList;

public class IngredientsArray {
    ArrayList<Ingredient> ingredientsArray;

    public IngredientsArray(ArrayList<Ingredient> ingredientsArray) {
        this.ingredientsArray = ingredientsArray;
    }

    public ArrayList<Ingredient> getIngredientsArray() {
        return ingredientsArray;
    }

    public void setIngredientsArray(ArrayList<Ingredient> ingredientsArray) {
        this.ingredientsArray = ingredientsArray;
    }
}
