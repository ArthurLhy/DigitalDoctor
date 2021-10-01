package com.jxstarxxx.myapplication.ui.adddoctor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdddoctorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdddoctorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
