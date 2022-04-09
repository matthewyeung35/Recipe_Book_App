package com.matthewyeung35.recipebookapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeDetailsBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {
    private ActivityRecipeDetailsBinding binding;
    Recipe recipe;
    Dialog dialog;
    private DataBaseHelper dataBaseHelper;
    int recipeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            recipeId = intent.getIntExtra("recipeId", -1);
            if (recipeId != -1) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeDetails.this);
                recipe = dataBaseHelper.getOne(recipeId);
                initView();
                initBar();
            }
        }
    }

    // setting button for header bar

    private void initBar() {
        if (recipe.isFavourite()){
            binding.detailBar.barHeart.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            binding.detailBar.barHeart.setImageDrawable(getDrawable(R.drawable.ic_heart));
        }else{
            binding.detailBar.barHeart.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
            binding.detailBar.barHeart.setImageDrawable(getDrawable(R.drawable.ic_heart_border));
        }

        binding.detailBar.barBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.detailBar.barHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseHelper = new DataBaseHelper(RecipeDetails.this);
                dataBaseHelper.changeFavourite(recipe);
                // update local array
                if (recipe.isFavourite()){
                    binding.detailBar.barHeart.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    binding.detailBar.barHeart.setImageDrawable(getDrawable(R.drawable.ic_heart_border));
                    Toast.makeText(RecipeDetails.this, "Unfavourited", Toast.LENGTH_SHORT).show();
                    recipe.setFavourite(false);
                }else{
                    binding.detailBar.barHeart.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    binding.detailBar.barHeart.setImageDrawable(getDrawable(R.drawable.ic_heart));
                    Toast.makeText(RecipeDetails.this, "Favourited", Toast.LENGTH_SHORT).show();
                    recipe.setFavourite(true);
                }


            }
        });

        binding.detailBar.barEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeDetails.this, RecipeEdit.class);
                intent.putExtra("recipeId", recipe.getId());
                startActivity(intent);


            }
        });

        binding.detailBar.barTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeDetails.this);
                builder.setMessage(getText(R.string.confirm_delete) + " " + recipe.getName() + " ?");
                builder.setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Recipe delete_recipe = recipe;
                        dataBaseHelper = new DataBaseHelper(RecipeDetails.this);
                        dataBaseHelper.deleteOne(recipe);
                        Toast.makeText(RecipeDetails.this, getText(R.string.deleted), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                builder.create().show();

            }
        });
    }

    private void initView() {
        dialog = new Dialog(this);
        binding.txtDetailName.setText(recipe.getName());
        binding.txtDetailDesc.setText(recipe.getShortDesc());
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
                initView();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update view after coming back from edit recipe
        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeDetails.this);
        recipe = dataBaseHelper.getOne(recipeId);
        initView();
    }
}