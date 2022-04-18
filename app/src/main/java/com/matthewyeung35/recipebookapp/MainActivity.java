package com.matthewyeung35.recipebookapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.matthewyeung35.recipebookapp.database.DataBaseHelper;
import com.matthewyeung35.recipebookapp.databinding.ActivityMainBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private DataBaseHelper dataBaseHelper;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeSetter();

        super.onCreate(savedInstanceState);

        //initialize database on create
        dataBaseHelper = new DataBaseHelper(this);
        ArrayList<Recipe> recipes = dataBaseHelper.getDb();

        //sample data on create app, create a sample recipe if there is nothing in database
        if (recipes.size() == 0){
            //sample data on create
            //for image
            byte[] img = new byte[0];
            String encodedImage = Base64.encodeToString(img, Base64.DEFAULT);
            ArrayList<Ingredient> testing_ingredient = new ArrayList<Ingredient>();
            testing_ingredient.add(new Ingredient(1,getString(R.string.egg)));
            testing_ingredient.add(new Ingredient(0.5F,getString(R.string.salt)));
            Recipe new_recipe = new Recipe(-1,getString(R.string.your_recipe_name), encodedImage, 1, testing_ingredient,getString(R.string.short_desc), getString(R.string.cooking_steps), getString(R.string.extra_comments), false);
            dataBaseHelper.addOne(new_recipe);
        }
        //initialize views
        initView();

    }

    private void themeSetter() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCE, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME,UserSettings.LIGHT_THEME);
        Log.d(TAG, "Preference " + theme);
        if (theme.equals(UserSettings.LIGHT_THEME)){
            AppCompatDelegate. setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate. setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }


    private void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        //fab button
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipeEdit.class);
                startActivity(intent);

            }
        });


        //nav bar
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favourite, R.id.nav_shopping, R.id.nav_setting, R.id.nav_add)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    //default option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    //default nav bar
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        binding.drawerLayout.closeDrawers();
        super.onPause();
    }
}
