package com.matthewyeung35.recipebookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.objects.IngredientsArray;

public class AddIngredientViewAdapter extends RecyclerView.Adapter<AddIngredientViewAdapter.ViewHolder> {
    private IngredientsArray ingredients;
    private Context context;

    public AddIngredientViewAdapter(Context context) {
        this.context = context;
    }

    public void setIngredients(IngredientsArray ingredients){
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_ingredient_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        float ingredient_amount = ingredients.getInstance().getAllIngredients().get(position).getAmount();
        // if it's a new array, set amount to empty str
        if (ingredient_amount == -1){
            holder.edtIngredientAmount.setText("");
        } else{
            // if have amount value
            //remove .0 from amount if full number
            String [] arrayOfFloatInput = String.valueOf(ingredient_amount).split("\\.");
            if (arrayOfFloatInput[1].equals("0")){
                holder.edtIngredientAmount.setText(arrayOfFloatInput[0]);
            }else{
                holder.edtIngredientAmount.setText(String.valueOf(ingredient_amount));
            }
        }
        holder.edtIngredient.setText(ingredients.getInstance().getAllIngredients().get(position).getFood());

        // for delete one ingredient button: remove it from arraylist
        holder.btnDeleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IngredientsArray.getInstance().removeIngredient(ingredients.getInstance().getAllIngredients().get(position)) == true){
                    Toast.makeText(context, "deleted ingredient", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return ingredients.getInstance().getAllIngredients().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public EditText edtIngredientAmount, edtIngredient;
        private ImageView btnDeleteIngredient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edtIngredientAmount = itemView.findViewById(R.id.edtIngredientAmount);
            edtIngredient = itemView.findViewById(R.id.edtIngredient);
            btnDeleteIngredient = itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
