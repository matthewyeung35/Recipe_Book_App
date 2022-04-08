package com.matthewyeung35.recipebookapp;

import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.IngredientsArray;

import java.util.ArrayList;

public class GlobalVar {

    private static GlobalVar instance;


    public GlobalVar() {
    }

    public static synchronized GlobalVar getInstance(){
        if (instance == null) {
            instance = new GlobalVar();
        }
        return instance;
    }


}
