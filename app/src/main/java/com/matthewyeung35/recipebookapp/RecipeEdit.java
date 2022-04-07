package com.matthewyeung35.recipebookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeEditBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.IngredientsArray;
import com.matthewyeung35.recipebookapp.objects.Recipe;

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


        //initView
        resetIngredientsAdapter();
        addPhoto();
        addIngredient();
        addRecipe();

    }

    private void addPhoto() {
        binding.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeEdit.this, "TODO", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIngredient() {
        binding.btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add a new empty Ingredient to array list on click
                IngredientsArray.getInstance().addIngredient(new Ingredient(-1,""));
                Log.d(TAG, "adapter item count" + adapter.getItemCount());
                //check all existing cardview entries, store their current value into the array list
                for (int i=0; i<adapter.getItemCount()-1; i++){
                    Log.d(TAG, "i: " + i );
                    AddIngredientViewAdapter.ViewHolder holder = (AddIngredientViewAdapter.ViewHolder) binding.ingredientRecylerView.findViewHolderForLayoutPosition(i);
                    // make sure we getting a holder
                    if (holder != null){
                        String input_amount = holder.edtIngredientAmount.getText().toString();

                        if (input_amount.equals("")){
                            IngredientsArray.getInstance().updateEntry(i, 0, holder.edtIngredient.getText().toString());
                        }else{
                            IngredientsArray.getInstance().updateEntry(i, Float.parseFloat(input_amount), holder.edtIngredient.getText().toString());
                        }
                    } else{
                        Log.d(TAG, "NO HOLDER, HOLDER == NULL");
                    }
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
                String name = binding.edtName.getText().toString();
                String desc = binding.edtDesc.getText().toString();
                int serving = 1;
                try {
                    serving = Integer.parseInt(binding.edtServing.getText().toString());
                }catch(Exception e){
                    Log.e(TAG, "no serving input");
                }

                // TODO Image function
                String image  = "dummy text";
                String steps = binding.edtSteps.getText().toString();
                String comments = binding.edtComments.getText().toString();

                // part to turn ingredients array into a json
                //update the arraylist by getting data from views
                for (int i=0; i<adapter.getItemCount(); i++){
                    AddIngredientViewAdapter.ViewHolder holder = (AddIngredientViewAdapter.ViewHolder) binding.ingredientRecylerView.findViewHolderForLayoutPosition(i);
                    // make sure we getting a holder
                    if (holder != null){
                        String input_amount = holder.edtIngredientAmount.getText().toString();
                        if (input_amount.equals("")){
                            IngredientsArray.getInstance().updateEntry(i, 0, holder.edtIngredient.getText().toString());
                        }else{
                            IngredientsArray.getInstance().updateEntry(i, Float.parseFloat(input_amount), holder.edtIngredient.getText().toString());
                        }
                    } else{
                        Log.d(TAG, "NO HOLDER, HOLDER == NULL");
                    }
                }
                // remove ingredients with empty food name
                for (Ingredient i: IngredientsArray.getAllIngredients()){
                    Log.d(TAG, i.toString());
                    if (i.getAmount() == -1 || i.getFood().equals("")){
                        IngredientsArray.getAllIngredients().remove(i);
                    }
                }

                Log.d(TAG, "JSON ingredients" + IngredientsArray.getInstance().dataToJson());

                Recipe recipe = new Recipe(-1, name, image, serving, IngredientsArray.getAllIngredients(),desc,steps,comments,false);
                // add new recipe to database
                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeEdit.this);
                dataBaseHelper.addOne(recipe);


                // back to main page
                // clear ingredients for next entry
                IngredientsArray.getInstance().clearArray();
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(RecipeEdit.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void resetIngredientsAdapter(){
        adapter.setIngredients(ingredients);
        binding.ingredientRecylerView.setAdapter(adapter);
        binding.ingredientRecylerView.setLayoutManager(new LinearLayoutManager(RecipeEdit.this));
    }

}