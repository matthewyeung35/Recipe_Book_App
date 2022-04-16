package com.matthewyeung35.recipebookapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.matthewyeung35.recipebookapp.RecyclerAdapters.ShopAddAdapter;
import com.matthewyeung35.recipebookapp.database.DataBaseHelper;
import com.matthewyeung35.recipebookapp.database.ShoppingDb;
import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeDetailsBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.IngredientsArray;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {
    private ActivityRecipeDetailsBinding binding;
    Recipe recipe;
    Dialog dialog;
    private DataBaseHelper dataBaseHelper;
    int recipeId;
    private String TAG = "RecipeDetails";

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
        //image
        byte[] decodedString = Base64.decode(recipe.getImage(), Base64.DEFAULT);
        if (decodedString.length !=0){
            binding.imgDetail.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.imgDetail.setImageBitmap(bitmap);
        }else{
            // hide imageview if no image
            binding.imgDetail.setVisibility(View.GONE);
        }


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
                shoppingOnClick();
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

    private void shoppingOnClick() {
        // set up ingredients array global var
        IngredientsArray ingredients = null;
        ingredients.getInstance().clearArray();

        // set up dialog box on click
        dialog.setContentView(R.layout.activity_pop_shopping);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set up recview with ingredients list
        RecyclerView shoppingAddRec = dialog.findViewById(R.id.ingredientShoppingRec);
        ShopAddAdapter adapter = new ShopAddAdapter(RecipeDetails.this);
        shoppingAddRec.setAdapter(adapter);
        shoppingAddRec.setLayoutManager(new LinearLayoutManager(RecipeDetails.this));
        adapter.setIngredients(recipe.getIngredients());

        // confirm add button, add selected ingredients to shopping list db
        Button btnAddToShopping = dialog.findViewById(R.id.btnAddToShopping);
        btnAddToShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int new_shopping_item = 0;
                // get the listed of checked item from ShopAddAdapter
                for (Ingredient i: ingredients.getInstance().getAll()){
                    Log.d(TAG, "i " + i.getFood());
                    ShoppingDb shoppingDb = new ShoppingDb(RecipeDetails.this);
                    ArrayList<String> old_shopping_list = shoppingDb.getDb();
                    Boolean already_exist = false;
                    // compare current food item with existing shopping database, if not in there, add it
                    for (String food: old_shopping_list){
                        if (food.equals(i.getFood())){
                            already_exist = true;
                        }
                    }
                    if (already_exist == false){
                        shoppingDb.addOne(i.getFood());
                        new_shopping_item += 1;
                    }
                }
                // return a toast depends if there is anything added
                if (new_shopping_item > 0){
                    Toast.makeText(RecipeDetails.this, "Added " + new_shopping_item + " items into the shopping cart", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RecipeDetails.this, "No items were added", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
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