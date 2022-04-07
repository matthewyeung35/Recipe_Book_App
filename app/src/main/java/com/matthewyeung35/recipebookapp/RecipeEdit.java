package com.matthewyeung35.recipebookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.databinding.ActivityMainBinding;
import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeEditBinding;

import java.util.ArrayList;

public class RecipeEdit extends AppCompatActivity {
    private ActivityRecipeEditBinding binding;
    private IngredientsArray ingredients;
    AddIngredientViewAdapter adapter = new AddIngredientViewAdapter(RecipeEdit.this);
    String TAG = "RecipeEdit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        resetIngredientsAdapter();

        addIngredient();
        addRecipe();
    }

    private void addIngredient() {
        binding.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add a new empty Ingredient to array list on click
                ingredients.getInstance().addIngredient(new Ingredient(-1,""));
                Log.d(TAG, "adapter item count" + adapter.getItemCount());
                //check all existing cardview entries, store their current value into the array list
                for (int i=0; i<adapter.getItemCount()-1; i++){
                    Log.d(TAG, "i: " + i );
                    AddIngredientViewAdapter.ViewHolder holder = (AddIngredientViewAdapter.ViewHolder) binding.ingredientRecylerView.findViewHolderForLayoutPosition(i);
                    IngredientsArray.getInstance().updateEntry(i, Float.parseFloat(holder.edtIngredientAmount.getText().toString()), holder.edtIngredient.getText().toString());
                }
                // update cardview
                adapter.notifyDataSetChanged();
            }
        });
    }

    //TODO current part
    private void addRecipe() {
        binding.btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            // on clicking the add recipe button, store all data into the recipe database
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeEdit.this, "ingredients:" + ingredients.getInstance().dataToJson() , Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void resetIngredientsAdapter(){
        adapter.setIngredients(ingredients);
        binding.ingredientRecylerView.setAdapter(adapter);
        binding.ingredientRecylerView.setLayoutManager(new LinearLayoutManager(RecipeEdit.this));
    }

}