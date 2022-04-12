package com.matthewyeung35.recipebookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.IngredientsArray;

import java.util.ArrayList;

public class ShopAddAdapter extends RecyclerView.Adapter<ShopAddAdapter.ViewHolder> {
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private IngredientsArray checkedList;
    private Context context;

    public ShopAddAdapter(Context context) {
        this.context = context;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_add_recycler, parent, false);
        ShopAddAdapter.ViewHolder holder = new ShopAddAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.txtShoppingName.setText(ingredient.getFood());
        holder.checkFood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
           @Override
           // if item is checked, add it to the Ingredients Array global var. Remove if unchecked
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (holder.checkFood.isChecked()){
                   checkedList.getInstance().addIngredient(ingredient);
               } else{
                   checkedList.getInstance().removeIngredient(ingredient);
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtShoppingName;
        CheckBox checkFood;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShoppingName = itemView.findViewById(R.id.txtShoppingName);
            checkFood = itemView.findViewById(R.id.checkFood);

        }
    }
}
