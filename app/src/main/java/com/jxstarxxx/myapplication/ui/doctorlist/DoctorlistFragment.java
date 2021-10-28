package com.jxstarxxx.myapplication.ui.doctorlist;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.databinding.FragmentDoctorlistBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DoctorlistFragment extends Fragment {

    private DoctorlistViewModel doctorlistViewModel;
    private FragmentDoctorlistBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserUid = firebaseUser.getUid();
    private FirebaseDatabase database;
    private RecyclerView doctorListRecycler;
    private DoctorListAdapter doctorListAdapter;

    private List<DoctorModel> doctorModels = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doctorlistViewModel =
                new ViewModelProvider(this).get(DoctorlistViewModel.class);

        binding = FragmentDoctorlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        doctorListRecycler = (RecyclerView) root.findViewById(R.id.doctorlist_recycle);
        doctorListRecycler.hasFixedSize();
        doctorListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorListAdapter = new DoctorListAdapter(this.getActivity(), doctorModels);
        doctorListRecycler.setAdapter(doctorListAdapter);

        List<String> friendListUid = new ArrayList<>();

        Log.i("Status", "friend list created");
        Log.i("userid", currentUserUid);

        doctorModels.clear();

        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child(currentUserUid).hasChild("friendList")){
                    Log.i("has friend", "yes");
                    for (DataSnapshot dataSnapshot : snapshot.child(currentUserUid).child("friendList").getChildren()){
                        Log.i("friendiud", dataSnapshot.getKey());
                        String friendUid = dataSnapshot.getKey();
                        String fullName = snapshot.child(friendUid).child("firstName").getValue(String.class) + " " + snapshot.child(friendUid).child("lastName").getValue(String.class);
                        String clinicName = snapshot.child(friendUid).child("clinic").getValue(String.class);
                        String departmentName = snapshot.child(friendUid).child("department").getValue(String.class);
                        String profilePic = snapshot.child(friendUid).child("photoUrl").getValue(String.class);
                        boolean chatted = (Boolean) dataSnapshot.child("chatted").getValue();
                        String userName = snapshot.child(friendUid).child("username").getValue(String.class);
                        DoctorModel doctorModel = new DoctorModel(fullName, clinicName, departmentName, profilePic, friendUid, chatted, userName);
                        doctorModels.add(doctorModel);
                    }
                    doctorListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
