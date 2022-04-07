package com.matthewyeung35.recipebookapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.objects.IngredientsArray;
import com.matthewyeung35.recipebookapp.objects.Recipe;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO set photo on cardivew
        holder.txtCardName.setText(recipes.get(position).getName());
        holder.txtCardDesc.setText(recipes.get(position).getShotDesc());

        // on press on the card itself, move to details page
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "fixme", Toast.LENGTH_SHORT).show();
            }
        });

        // delete current recipe on click
        holder.btnDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete " + recipes.get(position).getName() + " ?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Recipe delete_recipe = recipes.get(position);
                        dataBaseHelper = new DataBaseHelper(context);
                        dataBaseHelper.deleteOne(delete_recipe);
                        recipes = dataBaseHelper.getDb();
                        notifyDataSetChanged();
                        Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();

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

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgCardPhoto;
        private TextView txtCardName, txtCardDesc;
        private CardView parent;
        private ImageView btnDeleteRecipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCardPhoto = itemView.findViewById(R.id.imgCardPhoto);
            txtCardName = itemView.findViewById(R.id.txtCardName);
            txtCardDesc = itemView.findViewById(R.id.txtCardDesc);
            parent = itemView.findViewById(R.id.mainRecParent);
            btnDeleteRecipe = itemView.findViewById(R.id.btnDeleteRecipe);

        }
    }
}
