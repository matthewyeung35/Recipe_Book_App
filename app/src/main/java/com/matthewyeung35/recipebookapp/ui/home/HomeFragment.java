package com.matthewyeung35.recipebookapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matthewyeung35.recipebookapp.DataBaseHelper;
import com.matthewyeung35.recipebookapp.MainViewAdapter;
import com.matthewyeung35.recipebookapp.databinding.FragmentHomeBinding;
import com.matthewyeung35.recipebookapp.objects.Ingredient;
import com.matthewyeung35.recipebookapp.objects.Recipe;


import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private DataBaseHelper dataBaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dataBaseHelper = new DataBaseHelper(getContext());

        //adapter
        final RecyclerView mainRecyclerView = binding.mainRecyclerView;
        MainViewAdapter adapter = new MainViewAdapter(getContext());
        mainRecyclerView.setAdapter(adapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //get data from database
        ArrayList<Recipe> recipes = dataBaseHelper.getDb();
        adapter.setRecipes(recipes);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}