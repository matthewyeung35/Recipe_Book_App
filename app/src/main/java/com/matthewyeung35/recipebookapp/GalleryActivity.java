package com.matthewyeung35.recipebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.matthewyeung35.recipebookapp.database.DataBaseHelper;
import com.matthewyeung35.recipebookapp.databinding.ActivityGalleryBinding;
import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeDetailsBinding;
import com.matthewyeung35.recipebookapp.objects.Recipe;

public class GalleryActivity extends AppCompatActivity {
    private ActivityGalleryBinding binding;
    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            int recipeId = intent.getIntExtra("recipeId", -1);
            if (recipeId != -1) {
                //set image
                DataBaseHelper dataBaseHelper = new DataBaseHelper(GalleryActivity.this);
                Recipe recipe = dataBaseHelper.getOne(recipeId);
                byte[] decodedString = Base64.decode(recipe.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image = binding.imageZoom;
                image.setImageBitmap(bitmap);
                SGD = new ScaleGestureDetector(this, new ScaleListener());

                //set back button
                binding.imageBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }else{
                finish();
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));
            matrix.setScale(scale, scale);
            image.setScaleX(scale);
            image.setScaleY(scale);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;
    }
}