package com.matthewyeung35.recipebookapp.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matthewyeung35.recipebookapp.R;
import com.matthewyeung35.recipebookapp.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // search bar
        setHasOptionsMenu(true);



//        // hide fab button
//        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
//        fab.setVisibility(View.GONE);

        binding.textSetting.setText("Setting");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // for hiding search bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem search_item = menu.findItem(R.id.nav_search);
        if(search_item!=null){
            search_item.setVisible(false);
        }
        MenuItem add_item = menu.findItem(R.id.nav_add_shopping);
        if(add_item!=null){
            add_item.setVisible(false);
        }
    }
}