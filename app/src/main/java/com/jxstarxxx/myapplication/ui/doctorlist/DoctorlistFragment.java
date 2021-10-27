package com.jxstarxxx.myapplication.ui.doctorlist;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.databinding.FragmentDoctorlistBinding;



public class DoctorlistFragment extends Fragment {

    private DoctorlistViewModel doctorlistViewModel;
    private FragmentDoctorlistBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private RecyclerView doctorListRecycler;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doctorlistViewModel =
                new ViewModelProvider(this).get(DoctorlistViewModel.class);

        binding = FragmentDoctorlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        doctorListRecycler = (RecyclerView) root.findViewById(R.id.doctorlist_recycle);
        doctorListRecycler.hasFixedSize();
        doctorListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));






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
