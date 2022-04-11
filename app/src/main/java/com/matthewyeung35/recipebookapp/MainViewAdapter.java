package com.matthewyeung35.recipebookapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.objects.Recipe;

import java.util.ArrayList;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private Context context;
    private DataBaseHelper dataBaseHelper;

    public MainViewAdapter(Context context) {
        this.context = context;
    }


    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        //Add image
        byte[] decodedString = Base64.decode(recipe.getImage(), Base64.DEFAULT);
        if (decodedString.length !=0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgCardPhoto.setImageBitmap(bitmap);
        }else{
            // show default image if no image
            holder.imgCardPhoto.setImageResource(R.mipmap.ic_launcher);
        }
        holder.txtCardName.setText(recipe.getName());
        holder.txtCardDesc.setText(recipe.getShortDesc());

        // on press on the card itself, move to details page
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.searchView.getQuery();
                Intent intent = new Intent(context, RecipeDetails.class);
                intent.putExtra("recipeId", recipe.getId());
                context.startActivity(intent);
            }
        });

        // favourite button
        //initialize the color of heart icon
        if (recipe.isFavourite()){
            holder.btnCardFavourite.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
            holder.btnCardFavourite.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
        }else{
            holder.btnCardFavourite.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.gray)));
            holder.btnCardFavourite.setImageDrawable(context.getDrawable(R.drawable.ic_heart_border));
        }

        //update favourite on click
        holder.btnCardFavourite.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // change condition of favourite on press
                //update database
                dataBaseHelper = new DataBaseHelper(context);
                dataBaseHelper.changeFavourite(recipe);
                // update local array
                if (recipe.isFavourite()){
                    holder.btnCardFavourite.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.gray)));
                    Toast.makeText(context, "Unfavourited", Toast.LENGTH_SHORT).show();
                    recipe.setFavourite(false);
                }else{
                    holder.btnCardFavourite.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
                    Toast.makeText(context, "Favourited", Toast.LENGTH_SHORT).show();
                    recipe.setFavourite(true);
                }
                notifyDataSetChanged();
            }
        });

        // delete current recipe on click
        holder.btnDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.confirm_delete) + " " + recipes.get(position).getName() + " ?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Recipe delete_recipe = recipes.get(position);
                        dataBaseHelper = new DataBaseHelper(context);
                        dataBaseHelper.deleteOne(delete_recipe);
                        recipes.remove(delete_recipe);
                        notifyDataSetChanged();
                        Toast.makeText(context, R.string.deleted, Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgCardPhoto;
        private TextView txtCardName, txtCardDesc;
        private CardView parent;
        private ImageView btnDeleteRecipe, btnCardFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCardPhoto = itemView.findViewById(R.id.imgCardPhoto);
            txtCardName = itemView.findViewById(R.id.txtCardName);
            txtCardDesc = itemView.findViewById(R.id.txtCardDesc);
            parent = itemView.findViewById(R.id.mainRecParent);
            btnDeleteRecipe = itemView.findViewById(R.id.btnDeleteRecipe);
            btnCardFavourite = itemView.findViewById(R.id.btnCardFavourite);

        }
    }
}
