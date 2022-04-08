package com.matthewyeung35.recipebookapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeDetailsBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {
    private ActivityRecipeDetailsBinding binding;
    Recipe recipe;
    Dialog dialog;

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
        dialog = new Dialog(this);
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

        // calculator
        binding.btnDetailCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorOnClick();
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

    private void calculatorOnClick() {
        dialog.setContentView(R.layout.activity_pop_calculator);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnChangeServing = dialog.findViewById(R.id.btnChangeServing);
        EditText edtServingEditor = dialog.findViewById(R.id.edtServingEditor);
        ImageView imgAddbox = dialog.findViewById(R.id.imgAddBox);
        ImageView imgMinusBox = dialog.findViewById(R.id.imgMinusBox);
        edtServingEditor.setText(String.valueOf(recipe.getServing()));

        //pop up button onclicks
        imgAddbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtServingEditor.setText(String.valueOf(Integer.parseInt(edtServingEditor.getText().toString()) + 1));
            }
        });

        imgMinusBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(edtServingEditor.getText().toString()) > 1){
                    edtServingEditor.setText(String.valueOf(Integer.parseInt(edtServingEditor.getText().toString()) -1));
                }
            }
        });
        btnChangeServing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int new_amount = Integer.parseInt(edtServingEditor.getText().toString());
                double percentage_change = new_amount / (double)recipe.getServing();
                ArrayList<Ingredient> ingredients = recipe.getIngredients();
                ArrayList<Ingredient> new_ingredients = new ArrayList<>();
                for (Ingredient i: ingredients){
                    new_ingredients.add(new Ingredient((float) (i.getAmount()*percentage_change), i.getFood()));
                }
                recipe.setServing(new_amount);
                recipe.setIngredients(new_ingredients);
                dialog.dismiss();
                initView(recipe);
            }
        });
        dialog.show();
    }


}