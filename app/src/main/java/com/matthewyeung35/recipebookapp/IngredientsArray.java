package com.matthewyeung35.recipebookapp;

import com.google.gson.Gson;

import java.util.ArrayList;

public class IngredientsArray {
    private static IngredientsArray instance;
    private static ArrayList<Ingredient> allIngredients;

    private IngredientsArray() {
        if (allIngredients == null){
            allIngredients = new ArrayList<>();
            initData();
        }
    }

    private void initData() {
        allIngredients.add(new Ingredient(-1, ""));
    }

    public static IngredientsArray getInstance(){
        if (instance != null){
            return instance;
        }else {
            instance = new IngredientsArray();
            return instance;
        }
    }

    public static ArrayList<Ingredient> getAllIngredients() {
        return allIngredients;
    }

    public static boolean addIngredient(Ingredient ingredient){
        return allIngredients.add(ingredient);
    }

    public static boolean removeIngredient(Ingredient ingredient){
        return allIngredients.remove(ingredient);
    }

    public String dataToJson(){
        Gson gson = new Gson();
        return gson.toJson(allIngredients);
    }
}
