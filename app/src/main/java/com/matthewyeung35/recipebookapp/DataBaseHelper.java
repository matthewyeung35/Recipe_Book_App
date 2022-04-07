package com.matthewyeung35.recipebookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String RECIPE_TABLE = "RECIPE_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String COLUMN_IMAGE = "COLUMN_IMAGE";
    public static final String COLUMN_SERVING = "COLUMN_SERVING";
    public static final String COLUMN_INGREDIENTS = "COLUMN_INGREDIENTS";
    public static final String COLUMN_SHORTDESC = "COLUMN_SHORTDESC";
    public static final String COLUMN_STEPS = "COLUMN_STEPS";
    public static final String COLUMN_COMMENTS = "COLUMN_COMMENTS";
    public static final String COLUMN_FAVOURITE = "COLUMN_FAVOURITE";

    public static final String TAG = "DataBaseHelper";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "recipe.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + RECIPE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_IMAGE + " TEXT," + COLUMN_SERVING+ " INT, " + COLUMN_INGREDIENTS + " TEXT, "+ COLUMN_SHORTDESC+ " TEXT, " + COLUMN_STEPS + " TEXT, " + COLUMN_COMMENTS + " TEXT, " + COLUMN_FAVOURITE+ " BOOL)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// empty
    }

    public boolean addOne(Recipe recipe){
        Gson gson = new Gson();
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, recipe.getName());
        cv.put(COLUMN_IMAGE, recipe.getImage());
        cv.put(COLUMN_SERVING, recipe.getServing());
        //store ingredients as json
        cv.put(COLUMN_INGREDIENTS, gson.toJson(recipe.getIngredients()));
        cv.put(COLUMN_SHORTDESC, recipe.getShotDesc());
        cv.put(COLUMN_STEPS, recipe.getSteps());
        cv.put(COLUMN_COMMENTS, recipe.getComments());
        cv.put(COLUMN_FAVOURITE, recipe.isFavourite());

        long insert = db.insert(RECIPE_TABLE, null, cv);
        if (insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteOne(Recipe recipe){
        //TODO delete from db
        return false;
    }


    // takes all data from db and return as an array of recipe object
    public ArrayList<Recipe> getDb(){
        Log.d(TAG,"Getting database...");

        ArrayList<Recipe> resultArray = new ArrayList<>();

        String queryString = "SELECT * FROM " + RECIPE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        // cursor is a large chunk of data
        if(cursor.moveToFirst()){
            // loop through the cursor and create new customer objects, and put them into the return list
            do{
                int customerID = cursor.getInt(0);
                String name = cursor.getString(1);
                String image = cursor.getString(2);
                int serving = cursor.getInt(3);
                String ingredients_str = cursor.getString(4);
                String desc = cursor.getString(5);
                String steps = cursor.getString(6);
                String comments = cursor.getString(7);
                boolean favourite = cursor.getInt(8) == 1 ? true:false;

                // ingredients from string to object
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Ingredient>>(){}.getType();
                ArrayList<Ingredient> ingredients = gson.fromJson(ingredients_str, type);
                // create a new CustomerModel class item and add it to the new array
                Recipe newRecipe = new Recipe(customerID, name, image, serving, ingredients, desc, steps, comments, favourite);
                resultArray.add(newRecipe);
            }while(cursor.moveToNext());
        }else{
            //don't add anything to the list if there is nothing in the database
        }
        // close both db or cursor when done
        db.close();
        cursor.close();
        // return a array of CustomerModel
        return resultArray;

    }




}
