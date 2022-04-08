package com.matthewyeung35.recipebookapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeDetailsBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {
    private ActivityRecipeDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            int recipeId = intent.getIntExtra("recipeId", -1);
            if (recipeId != -1) {
                initView(recipeId);

            }
        }




    }

    private void initView(int recipeId) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeDetails.this);
        Recipe recipe = dataBaseHelper.getOne(recipeId);
        binding.txtDetailName.setText(recipe.getName());
        binding.txtDetailDesc.setText(recipe.getShotDesc());
        binding.txtDetailServing.setText(String.valueOf(recipe.getServing()));
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        String ingredients_string = "";
        for (Ingredient i: ingredients){
            ingredients_string += i.getAmountStr();
            ingredients_string += " ";
            ingredients_string += i.getFood();
            ingredients_string += "\n";
        }
        binding.txtDetailIngredients.setText(ingredients_string);
        binding.txtDetailSteps.setText(recipe.getSteps());
        binding.txtDetailComments.setText(recipe.getComments());

        binding.btnDetailCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // TODO shopping on click
        binding.btnDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeDetails.this, "TODO", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);


    }
}