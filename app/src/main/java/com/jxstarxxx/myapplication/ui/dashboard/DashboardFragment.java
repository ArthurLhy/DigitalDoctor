package com.jxstarxxx.myapplication.ui.dashboard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxstarxxx.myapplication.CovidCasesTracker;
import com.jxstarxxx.myapplication.DoctorListActivity;
import com.jxstarxxx.myapplication.HeartRateActivity;
import com.jxstarxxx.myapplication.HeartRateGuideActivity;
import com.jxstarxxx.myapplication.PermissionActivity;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("user");
    private String user_id  = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private ImageView chatWithDoc, vaccineFinder, caseTracker, heartRate;
    private TextView usernameView;
    private String username = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        usernameView = binding.textViewUser;

        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Dashboard...");
        progressDialog.show();

        databaseReference.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = snapshot.child("username").getValue(String.class);
                if (snapshot.child("isDoctor").getValue(boolean.class)) {
                    binding.findDocCard.setVisibility(View.GONE);
                }
                usernameView.setText(username);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // button define
        chatWithDoc = (ImageView) root.findViewById(R.id.dashboard_doctor);
        vaccineFinder = (ImageView) root.findViewById(R.id.dashboard_vaccine_finder);
        caseTracker = (ImageView) root.findViewById(R.id.dashboard_tracker);
        heartRate = (ImageView) root.findViewById(R.id.dashboard_heart_rate);

        // click listener
        chatWithDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DoctorListActivity.class));
            }
        });

        vaccineFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PermissionActivity.class));
            }
        });

        caseTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CovidCasesTracker.class));
            }
        });

        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                }
                startActivity(new Intent(getActivity(), HeartRateGuideActivity.class));
            }
        });




//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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