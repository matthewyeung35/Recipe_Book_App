package com.matthewyeung35.recipebookapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.Context;
import android.content.ContentResolver;

import com.matthewyeung35.recipebookapp.databinding.ActivityRecipeEditBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.IngredientsArray;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

public class RecipeEdit extends AppCompatActivity {
    private ActivityRecipeEditBinding binding;
    private IngredientsArray ingredients;
    private AddIngredientViewAdapter adapter = new AddIngredientViewAdapter(RecipeEdit.this);
    private String TAG = "RecipeEdit", currentPhotoPath;
    int recipeId;
    private Recipe old_recipe;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera;
    private ActivityResultLauncher<String> activityResultLauncherImage;
    private Bitmap bitmap;
    private static final int LOCATION_REQUEST = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize buttons
        btnPhoto();
        addIngredient();
        btnRecipe();
        binding.detailBar.barBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // if coming from edit recipe, apply existing data to the edit page
        Intent intent = getIntent();
        recipeId = intent.getIntExtra("recipeId", -1);
        if (recipeId != -1){
            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeEdit.this);
            old_recipe = dataBaseHelper.getOne(recipeId);
            binding.edtName.setText(old_recipe.getName());
            binding.edtDesc.setText(old_recipe.getShortDesc());
            binding.edtServing.setText(String.valueOf(old_recipe.getServing()));
            binding.edtSteps.setText(old_recipe.getSteps());
            binding.edtComments.setText(old_recipe.getComments());
            if (old_recipe.getIngredients().size() != 0){
                IngredientsArray.getInstance().clearArray();
            }
            for (Ingredient i: old_recipe.getIngredients()){
                IngredientsArray.getInstance().addIngredient(i);
            }
            //get image
            byte[] decodedString = Base64.decode(old_recipe.getImage(), Base64.DEFAULT);
            if (decodedString.length !=0){
                bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                binding.imgPhoto.setImageBitmap(bitmap);
            }
            Log.d(TAG, "decode" + old_recipe.getImage() + "length "+ old_recipe.getImage().length());

        }

        //initialize ingredients rec view
        adapter.setIngredients(ingredients);
        binding.ingredientRecylerView.setAdapter(adapter);
        binding.ingredientRecylerView.setLayoutManager(new LinearLayoutManager(RecipeEdit.this));

        //for accessing image
        activityResultLauncherImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imgPhoto.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        //for using camera
        activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    // get thumbnail method, low res
//                    Bundle bundle = result.getData().getExtras();
//                    bitmap = (Bitmap) bundle.get("data");
                    bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                    binding.imgPhoto.setImageBitmap(bitmap);
                    Log.d(TAG, "Camera bitmap" + bitmap);
                } catch (Exception e) {
                    Log.e(TAG, "can't get camera result");
                }
            }
        });
    }

    private void btnPhoto() {
        binding.btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for permission first
                String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(RecipeEdit.this, perms)) {
                    PopupMenu popupMenu = new PopupMenu(RecipeEdit.this, binding.btnAddPhoto);
                    popupMenu.getMenuInflater().inflate(R.menu.camera_popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.cameraExisting:
                                    activityResultLauncherImage.launch("image/*");
                                    return true;

                                case R.id.cameraPhoto:
                                    // get thumbnail method, low res
//                                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                if (camera_intent.resolveActivity(getPackageManager())!= null){
//                                    activityResultLauncherCamera.launch(camera_intent);
//                                } else{
//                                    Toast.makeText(RecipeEdit.this, "camera not working", Toast.LENGTH_SHORT).show();
//                             }

                                    Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (camera_intent.resolveActivity(getPackageManager())!= null) {
                                        File photoFile = createImageFile();
                                        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),"com.matthewyeung35.recipebookapp.fileprovider", photoFile);
                                        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                        Log.d(TAG, "PhotoUri " + String.valueOf(photoUri));
                                        activityResultLauncherCamera.launch(camera_intent);
                                    } else {
                                        Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();

                } else {
                    // Do not have permissions, request them now
                    EasyPermissions.requestPermissions(RecipeEdit.this,"Please grant permission",
                            LOCATION_REQUEST, perms);
                }

            }

            private File createImageFile() {
                String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
                String imageFileName = "IMAGE_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = null;
                try {
                    image = File.createTempFile(imageFileName, ".jpg", storageDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentPhotoPath = image.getAbsolutePath();
                Log.d(TAG, "currentPhotoPath " + currentPhotoPath);
                return image;
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


    private void btnRecipe() {
        binding.btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            private byte[] img;
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

                // for adding image, if there is a image then turn it to a byte[], if there is no image then use a empty byte[] instead
                if (bitmap != null){
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    img = bos.toByteArray();
                    Log.d(TAG, "bit map yes" + img.length);
                }else{
                    img = new byte[0];
                    Log.d(TAG, "bit map null" + img.length);
                }
                String encodedImage = Base64.encodeToString(img, Base64.DEFAULT);

                //continue
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

                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecipeEdit.this);
                // add new recipe to database
                Log.d(TAG, "Image bytearray" + img);
                // test if the image can fit into db or not, if not then request another image
                if (img.length > 1000000){
                    Toast.makeText(RecipeEdit.this, "Image size too big, choose another image", Toast.LENGTH_SHORT).show();
                }else{
                    if (recipeId == -1){
                        // back to main page
                        // clear ingredients for next entry
                        Recipe recipe = new Recipe(-1, name, encodedImage, serving, IngredientsArray.getAllIngredients(),desc,steps,comments,false);
                        dataBaseHelper.addOne(recipe);
                        dataBaseHelper.getDb();
                        IngredientsArray.getInstance().clearArray();
                        IngredientsArray.getInstance().initData();
                        adapter.notifyDataSetChanged();

                    }else{
                        Recipe recipe = new Recipe(old_recipe.getId(), name, encodedImage, serving, IngredientsArray.getAllIngredients(),desc,steps,comments,false);
                        // update recipe entry instead of creating a new one if coming from existing recipe
                        dataBaseHelper.updateOne(recipe);
                    }
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // also clear ingredients data on back click
        IngredientsArray.getInstance().clearArray();
        IngredientsArray.getInstance().initData();
    }

    //ask for accessing image perms
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}