package com.matthewyeung35.recipebookapp.ui.testing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TestingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is testing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}