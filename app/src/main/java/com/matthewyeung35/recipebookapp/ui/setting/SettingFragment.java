package com.matthewyeung35.recipebookapp.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.matthewyeung35.recipebookapp.MainActivity;
import com.matthewyeung35.recipebookapp.R;
import com.matthewyeung35.recipebookapp.UserSettings;
import com.matthewyeung35.recipebookapp.databinding.FragmentSettingBinding;

import java.util.Set;


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

        // dark mode button
        // set initial button value
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(UserSettings.PREFERENCE, Context.MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME,UserSettings.LIGHT_THEME);
        if (theme.equals(UserSettings.LIGHT_THEME)){
            binding.settingDarkMode.setChecked(false);
        }else{
            binding.settingDarkMode.setChecked(true);
        }
        // on click listener for dark mode button
        binding.settingDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(UserSettings.PREFERENCE, Context.MODE_PRIVATE).edit();
                    editor.putString(UserSettings.CUSTOM_THEME, UserSettings.DARK_THEME);
                    editor.apply();
                }else{
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(UserSettings.PREFERENCE, Context.MODE_PRIVATE).edit();
                    editor.putString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
                    editor.apply();
                }
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