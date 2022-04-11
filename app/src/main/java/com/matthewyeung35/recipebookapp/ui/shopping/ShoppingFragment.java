package com.matthewyeung35.recipebookapp.ui.shopping;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matthewyeung35.recipebookapp.DataBaseHelper;
import com.matthewyeung35.recipebookapp.MainViewAdapter;
import com.matthewyeung35.recipebookapp.R;
import com.matthewyeung35.recipebookapp.ShoppingAdapter;
import com.matthewyeung35.recipebookapp.ShoppingDb;
import com.matthewyeung35.recipebookapp.databinding.FragmentShoppingBinding;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {
    private ArrayList<String> food_array;
    private FragmentShoppingBinding binding;
    private ShoppingDb shoppingDb;
    private ShoppingAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ShoppingViewModel shoppingViewModel =
                new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // search bar
        setHasOptionsMenu(true);

        // access database and show data
        shoppingDb = new ShoppingDb(getContext());
        food_array = shoppingDb.getDb();
        final RecyclerView shoppingRecyclerView = binding.shoppingRec;
        adapter = new ShoppingAdapter(getContext());
        shoppingRecyclerView.setAdapter(adapter);
        shoppingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setFood(food_array);

        // show a text when array empty
        if (food_array.size() == 0 ){
            binding.txtEmptyShopping.setVisibility(View.VISIBLE);
        }else{
            binding.txtEmptyShopping.setVisibility(View.GONE);
        }

        // remove all button
        binding.btnShoppingDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("You sure you want to clear the shopping cart?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shoppingDb.deleteAll();
                        food_array.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                builder.create().show();
            }
        });

        // hide fab button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // for action bar, + button
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        addShoppingCart(menu);

        // hiding search bar
        MenuItem search_item = menu.findItem(R.id.nav_search);
        if(search_item!=null){
            search_item.setVisible(false);
        }
    }

    private void addShoppingCart(Menu menu) {
        MenuItem btnAddShopping = menu.findItem(R.id.nav_add_shopping);
        btnAddShopping.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add to shopping cart");
                builder.setMessage("  ");
                EditText input = new EditText(getContext());
                builder.setView(input);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input_food = input.getText().toString();
                        boolean already_exist = false;
                        // check if input already in database, don't add if already in
                        for (String f: food_array){
                            if (input_food.equals(f)){
                                already_exist = true;
                            }
                        }
                        if (already_exist == false){
                            shoppingDb.addOne(input_food);
                            food_array.add(input_food);
                        }else{
                            Toast.makeText(getContext(), "Ingredient already exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ;
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
}