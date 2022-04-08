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
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            int recipeId = intent.getIntExtra("recipeId", -1);
            if (recipeId != -1) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeDetails.this);
                recipe = dataBaseHelper.getOne(recipeId);
                initView(recipe);

            }
        }




    }

    private void initView(Recipe recipe) {
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
                Intent intent = new Intent(RecipeDetails.this,PopCalculator.class);
                intent.putExtra("amount",recipe.getServing());
                startActivity(intent);

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

    @Override
    protected void onResume() {
        super.onResume();

        //after user accessing the pop up calculator, update the ingredients amount accordingly
        if (GlobalVar.getInstance().isPop_to_details()){
            GlobalVar.getInstance().setPop_to_details(false);
            int new_amount = GlobalVar.getInstance().getIngredient_amount();

            double percentage_change = new_amount / (double)recipe.getServing();
            Toast.makeText(this, String.valueOf(percentage_change), Toast.LENGTH_SHORT).show();
            ArrayList<Ingredient> ingredients = recipe.getIngredients();
            ArrayList<Ingredient> new_ingredients = new ArrayList<>();
            for (Ingredient i: ingredients){
                new_ingredients.add(new Ingredient((float) (i.getAmount()*percentage_change), i.getFood()));
            }
            recipe.setServing(new_amount);
            recipe.setIngredients(new_ingredients);
            initView(recipe);
        }

    }
}