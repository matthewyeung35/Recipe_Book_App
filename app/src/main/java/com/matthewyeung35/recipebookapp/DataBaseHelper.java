package com.matthewyeung35.recipebookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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

    public DataBaseHelper(@Nullable Context context) {
        super(context, "recipe.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + RECIPE_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_IMAGE + " TEXT," + COLUMN_SERVING+ " INT, " + COLUMN_INGREDIENTS + " TEXT, "+ COLUMN_SHORTDESC+ " TEXT, " + COLUMN_STEPS + " TEXT, " + COLUMN_COMMENTS + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// empty
    }

    public boolean addOne(Recipe recipe){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, recipe.getName());
        cv.put(COLUMN_IMAGE, recipe.getImage());
        cv.put(COLUMN_SERVING, recipe.getServing());

        long insert = db.insert(RECIPE_TABLE, null, cv);
        if (insert == -1){
            return false;
        }else{
            return true;
        }
    }




}
