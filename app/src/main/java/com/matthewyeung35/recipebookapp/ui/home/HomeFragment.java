package com.matthewyeung35.recipebookapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.matthewyeung35.recipebookapp.R;
import com.matthewyeung35.recipebookapp.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textHome = binding.textHome;
        final TextView textView2 = binding.textView2;
        final TextView textView3 = binding. textView3;
        textView3.setText(R.string.hello_blank_fragment);

        homeViewModel.getText().observe(getViewLifecycleOwner(), textHome::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}