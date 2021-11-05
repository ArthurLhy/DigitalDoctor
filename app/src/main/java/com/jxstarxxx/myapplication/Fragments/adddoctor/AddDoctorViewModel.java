package com.jxstarxxx.myapplication.Fragments.adddoctor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddDoctorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddDoctorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
