package com.matthewyeung35.recipebookapp.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.R;
import com.matthewyeung35.recipebookapp.database.ShoppingDb;

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private ArrayList<String> foods = new ArrayList<>();
    private Context context;
    private ShoppingDb shoppingDb;

    public ShoppingAdapter(Context context) {
        this.context = context;
    }

    public void setFood(ArrayList<String> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_recycler, parent, false);
        ShoppingAdapter.ViewHolder holder = new ShoppingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtShoppingName.setText(foods.get(position));
        holder.imgRemoveShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingDb = new ShoppingDb(context);
                shoppingDb.deleteOne(foods.get(position));
                Toast.makeText(context, "Deleted " + foods.get(position), Toast.LENGTH_SHORT).show();
                foods.remove(foods.get(position));
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtShoppingName;
        private ImageView imgRemoveShopping;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShoppingName = itemView.findViewById(R.id.txtShoppingName);
            imgRemoveShopping = itemView.findViewById(R.id.imgRemoveShopping);
        }
    }
}
