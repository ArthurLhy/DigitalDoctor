package com.jxstarxxx.myapplication.ui.doctorlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DoctorlistViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DoctorlistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

