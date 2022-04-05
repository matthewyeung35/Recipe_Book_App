package com.matthewyeung35.recipebookapp.ui.testing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.matthewyeung35.recipebookapp.databinding.FragmentSlideshowBinding;
import com.matthewyeung35.recipebookapp.databinding.FragmentTestingBinding;
import com.matthewyeung35.recipebookapp.ui.slideshow.SlideshowViewModel;

public class TestingFragment extends Fragment {

    private FragmentTestingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.matthewyeung35.recipebookapp.ui.testing.TestingViewModel testingViewModel =
                new ViewModelProvider(this).get(TestingViewModel.class);

        binding = FragmentTestingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTesting;
        testingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}