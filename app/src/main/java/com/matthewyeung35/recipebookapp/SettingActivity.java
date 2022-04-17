package com.matthewyeung35.recipebookapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matthewyeung35.recipebookapp.database.DataBaseHelper;
import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeDetailsBinding;
import com.matthewyeung35.recipebookapp.databinding.ActivitySettingBinding;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private final static String TAG = "SettingActivity";
    private ActivitySettingBinding binding;
    private ActivityResultLauncher<String> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // back button
        binding.detailBar.barBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // dark mode button
        darkMode();

        // export txt
        exportTxt();

        // import txt
        importTxt();

    }

    // button to switch between dark and light mode
    private void darkMode() {
        // set initial button value
        SharedPreferences sharedPreferences = this.getSharedPreferences(UserSettings.PREFERENCE, Context.MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME,UserSettings.LIGHT_THEME);
        if (theme.equals(UserSettings.LIGHT_THEME)){
            binding.settingDarkMode.setChecked(false);
            binding.txtSetDarkModeDesc.setText("Light theme");
        }else{
            binding.settingDarkMode.setChecked(true);
            binding.txtSetDarkModeDesc.setText("Dark theme");
        }
        // on click listener for dark mode button
        binding.settingDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //dark
                    SharedPreferences.Editor editor = SettingActivity.this.getSharedPreferences(UserSettings.PREFERENCE, Context.MODE_PRIVATE).edit();
                    editor.putString(UserSettings.CUSTOM_THEME, UserSettings.DARK_THEME);
                    editor.apply();
                    binding.txtSetDarkModeDesc.setText("Dark theme");
                }else{
                    //light
                    SharedPreferences.Editor editor = SettingActivity.this.getSharedPreferences(UserSettings.PREFERENCE, Context.MODE_PRIVATE).edit();
                    editor.putString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
                    editor.apply();
                    binding.txtSetDarkModeDesc.setText("Light theme");
                }
                themeSetter();
            }
        });
    }

    // export all database recipes into a txt file of json
    private void exportTxt() {
        binding.btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(SettingActivity.this);
                ArrayList<Recipe> data = dataBaseHelper.getDb();
                String json_data = gson.toJson(data);
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File newtxt = new File(downloadDir, "recipeData.txt");
                try {
                    FileWriter writer = new FileWriter(newtxt);
                    writer.append(json_data);
                    writer.flush();
                    writer.close();
                    Toast.makeText(SettingActivity.this, "recipeData.txt saved in downloads", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SettingActivity.this, "Error; Cannot save file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // takes a txt file of json recipes and add them into the database
    private void importTxt() {
        binding.btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("*/*");
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                try {
                    // get the text from file
                    InputStream inputStream = getContentResolver().openInputStream(result);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }
                    String txtString = total.toString();
                    Log.d(TAG, txtString);

                    // turn the txt string into recipe data, and add them into the database as new entries
                    Gson gson = new Gson();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(SettingActivity.this);
                    ArrayList<Recipe> recipes = gson.fromJson(txtString, new TypeToken<ArrayList<Recipe>>(){}.getType());
                    for (Recipe r : recipes){
                        dataBaseHelper.addOne(r);
                    }
                    Toast.makeText(SettingActivity.this, recipes.size() + " recipes added succesfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SettingActivity.this, "Not a valid file", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

}

