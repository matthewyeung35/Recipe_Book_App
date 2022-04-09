package com.matthewyeung35.recipebookapp.objects;

import com.google.gson.Gson;

import java.util.ArrayList;

//global variable for temporary saving a ingredient list
public class IngredientsArray {
    private static IngredientsArray instance;
    private static ArrayList<Ingredient> allIngredients;

    private IngredientsArray() {
        if (allIngredients == null){
            allIngredients = new ArrayList<>();
            initData();
        }
    }

    public void initData() {
        allIngredients.add(new Ingredient(-1, ""));
    }

    public static IngredientsArray getInstance(){
        if (instance == null) {
            instance = new IngredientsArray();
        }
        return instance;
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

    public static void updateEntry(int position, float new_amount, String new_food){
        allIngredients.get(position).setAmount(new_amount);
        allIngredients.get(position).setFood(new_food);
    }

    public String dataToJson(){
        Gson gson = new Gson();
        return gson.toJson(allIngredients);
    }

    public void clearArray(){
        allIngredients.removeAll(allIngredients);

    }
}
