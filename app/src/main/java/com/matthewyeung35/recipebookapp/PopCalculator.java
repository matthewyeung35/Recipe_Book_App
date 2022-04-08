package com.matthewyeung35.recipebookapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class PopCalculator extends Activity {
    private Button btnChangeServing;
    private EditText edtServingEditor;
    private ImageView imgAddbox, imgMinusBox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing window
        setContentView(R.layout.activity_pop_calculator);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.4));

        //initializing data
        int amount = getIntent().getIntExtra("amount", -1);
        btnChangeServing = findViewById(R.id.btnChangeServing);
        edtServingEditor = findViewById(R.id.edtServingEditor);
        imgAddbox = findViewById(R.id.imgAddBox);
        imgMinusBox = findViewById(R.id.imgMinusBox);
        edtServingEditor.setText(String.valueOf(amount));

        //button onclicks
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
                GlobalVar.getInstance().setIngredient_amount(Integer.parseInt(edtServingEditor.getText().toString()));
                GlobalVar.getInstance().setPop_to_details(true);
                finish();
            }
        });

    }
}
