package com.matthewyeung35.recipebookapp;

import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.IngredientsArray;

import java.util.ArrayList;

public class GlobalVar {

    private static GlobalVar instance;
    private static int ingredient_amount;
    private static boolean pop_to_details;

    public GlobalVar() {
    }

    public static synchronized GlobalVar getInstance(){
        if (instance == null) {
            instance = new GlobalVar();
        }
        return instance;
    }

    public static int getIngredient_amount() {
        return ingredient_amount;
    }

    public static boolean isPop_to_details() {
        return pop_to_details;
    }

    public static void setPop_to_details(boolean pop_to_details) {
        GlobalVar.pop_to_details = pop_to_details;
    }

    public static void setIngredient_amount(int ingredient_amount) {
        GlobalVar.ingredient_amount = ingredient_amount;
    }
}
