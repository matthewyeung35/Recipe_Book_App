package com.matthewyeung35.recipebookapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.DataBaseHelper;
import com.matthewyeung35.recipebookapp.MainViewAdapter;
import com.matthewyeung35.recipebookapp.R;
import com.matthewyeung35.recipebookapp.databinding.FragmentHomeBinding;
import com.matthewyeung35.recipebookapp.objects.Recipe;


import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private DataBaseHelper dataBaseHelper;
    private static final String TAG = "HomeFragment";
    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dataBaseHelper = new DataBaseHelper(getContext());

        // search bar
        setHasOptionsMenu(true);

        //use adapter
        MainViewAdapter adapter = recViewAdapter();

        //get data from database
        ArrayList<Recipe> recipes = dataBaseHelper.getDb();
        adapter.setRecipes(recipes);

        return root;
    }

    //for initializing adapter
    private MainViewAdapter recViewAdapter() {
        final RecyclerView mainRecyclerView = binding.mainRecyclerView;
        MainViewAdapter adapter = new MainViewAdapter(getContext());
        mainRecyclerView.setAdapter(adapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return adapter;
    }

    // for search bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "In on create option menu" );
        searchView = (SearchView) menu.findItem(R.id.nav_search).getActionView();
        searchView.setQueryHint("Search by recipe name");

        // change the array of adapter when using search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecipe();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecipe();
                return true;
            }
            private void searchRecipe() {
                Log.d(TAG, "Searching for recipe with keyword: " + searchView.getQuery());
                MainViewAdapter adapter = recViewAdapter();
                ArrayList<Recipe> recipes = dataBaseHelper.getDb();
                ArrayList<Recipe> searching_recipes = new ArrayList<>();
                for (Recipe r: recipes){
                    if (r.getName().contains(searchView.getQuery())){
                        searching_recipes.add(r);
                    }
                }
                adapter.setRecipes(searching_recipes);
            }
        });

    }

    //clear focus of search bar when switching activities
    @Override
    public void onResume() {
        super.onResume();
        try {
            searchView.clearFocus();
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}