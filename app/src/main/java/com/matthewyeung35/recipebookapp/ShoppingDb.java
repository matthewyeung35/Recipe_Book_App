package com.matthewyeung35.recipebookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ShoppingDb extends SQLiteOpenHelper {
    public static final String FOOD_TABLE = "FOOD_TABLE";
    public static final String COLUMN_ID = "COLUMN_ID";
    public static final String COLUMN_FOOD = "COLUMN_FOOD";

    public ShoppingDb(@Nullable Context context) {
        super(context, "shopping.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + FOOD_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(String food){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FOOD, food);

        long insert = db.insert(FOOD_TABLE, null, cv);
        if (insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteOne(String food){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            String queryString = "DELETE FROM " + FOOD_TABLE + " WHERE " + COLUMN_FOOD + " = '" + food + "'";
            db.execSQL(queryString);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ArrayList<String> getDb(){
        ArrayList<String> resultArray = new ArrayList<>();

        String queryString = "SELECT * FROM " + FOOD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        // cursor is a large chunk of data
        if(cursor.moveToFirst()){
            // loop through the cursor and create new customer objects, and put them into the return list
            do{
                String food = cursor.getString(1);
                resultArray.add(food);
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

    public boolean deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            String queryString = "DELETE FROM " + FOOD_TABLE;
            db.execSQL(queryString);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
