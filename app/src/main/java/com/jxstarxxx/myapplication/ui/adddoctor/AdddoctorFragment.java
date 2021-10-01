package com.jxstarxxx.myapplication.ui.adddoctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jxstarxxx.myapplication.databinding.FragmentAdddoctorBinding;
import com.jxstarxxx.myapplication.ui.account.AccountViewModel;

public class AdddoctorFragment extends Fragment {

    private AdddoctorViewModel adddoctorViewModel;
    private FragmentAdddoctorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adddoctorViewModel =
                new ViewModelProvider(this).get(AdddoctorViewModel.class);

        binding = FragmentAdddoctorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textAccount;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
