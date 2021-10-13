package com.jxstarxxx.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jxstarxxx.myapplication.DoctorListActivity;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.VaccineFinderActivity;
import com.jxstarxxx.myapplication.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private Button chatWithDoc;
    private Button vaccineFinder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // button define
        chatWithDoc = (Button) root.findViewById(R.id.dashboard_chat);
        vaccineFinder = (Button) root.findViewById(R.id.dashboard_vaccine_finder);

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
                startActivity(new Intent(getActivity(), VaccineFinderActivity.class));
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