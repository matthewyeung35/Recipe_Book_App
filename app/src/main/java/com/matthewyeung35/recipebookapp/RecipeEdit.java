package com.matthewyeung35.recipebookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.databinding.ActivityMainBinding;
import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeEditBinding;

import java.util.ArrayList;

public class RecipeEdit extends AppCompatActivity {
    private ActivityRecipeEditBinding binding;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // for testing ingredients
        ingredients.add(new Ingredient(1, "egg"));

        resetIngredientsAdapter();

        addIngredient();
        addRecipe();
    }

    private void addIngredient() {
        binding.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.add(new Ingredient(-1, ""));
                resetIngredientsAdapter();
            }
        });
    }

    private void addRecipe() {
        binding.btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeEdit.this, "addrecipe", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetIngredientsAdapter(){
        AddIngredientViewAdapter adapter = new AddIngredientViewAdapter(RecipeEdit.this);
        adapter.setIngredients(ingredients);
        binding.ingredientRecylerView.setAdapter(adapter);
        binding.ingredientRecylerView.setLayoutManager(new LinearLayoutManager(RecipeEdit.this));
    }

}