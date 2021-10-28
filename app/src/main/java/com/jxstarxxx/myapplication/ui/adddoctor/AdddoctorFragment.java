package com.jxstarxxx.myapplication.ui.adddoctor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import com.jxstarxxx.myapplication.Doctor;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.databinding.FragmentAdddoctorBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class AdddoctorFragment extends Fragment {

    private AdddoctorViewModel adddoctorViewModel;
    private FragmentAdddoctorBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserUid = firebaseUser.getUid();
    private FirebaseDatabase database;

    private RecyclerView addDoctorRecyclerView;
    private String[] clinicList;
    private String[] departmentList;

    private List<addDoctorModel> addDoctorModels = new ArrayList<>();

    private Spinner clinicSpin;
    private Spinner departmentSpin;

    private Button searchButton;
    private AdddoctorAdapter adddoctorAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adddoctorViewModel = new ViewModelProvider(this).get(AdddoctorViewModel.class);
        binding = FragmentAdddoctorBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        addDoctorRecyclerView = (RecyclerView) root.findViewById(R.id.add_doctor_recyle);
        addDoctorRecyclerView.setHasFixedSize(true);
        addDoctorRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        clinicSpin = (Spinner) root.findViewById(R.id.spinner_clinic);
        departmentSpin = (Spinner) root.findViewById(R.id.spinner_depart);

        clinicList = new String[] {"Melbourne North Medical Clinic", "Midtown Medical Clinic", "Southbank Medical Clinic"};
        departmentList = new String[] {"surgery", "cardiology"};

        ArrayAdapter<String> clinicAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clinicList);
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, departmentList);

        clinicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clinicSpin.setAdapter(clinicAdapter);
        departmentSpin.setAdapter(departmentAdapter);

        adddoctorAdapter = new AdddoctorAdapter(this.getActivity(), addDoctorModels);
        addDoctorRecyclerView.setAdapter(adddoctorAdapter);

        searchButton = (Button) root.findViewById(R.id.add_doctor_search_button);

        List<Doctor> totalDoctorList = new ArrayList<>();

        List<String> addedDoctorList = new ArrayList<>();

        databaseReference.child("user").child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.hasChild("friendList")){
                    for (DataSnapshot dataSnapshot : snapshot.child("friendList").getChildren()){
                        addedDoctorList.add(dataSnapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.i("Issue", "snapshot does not exist");
                }
                Log.i("Status", "Start iterating...");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //Check if the user is current user.
                    if (dataSnapshot.getKey().equals(currentUserUid)){
                        continue;
                    }
                    boolean isDoctor = (Boolean) dataSnapshot.child("isDoctor").getValue();
                    Log.i(dataSnapshot.child("firstName").getValue(String.class), String.valueOf(isDoctor));
                    if (!isDoctor){
                        continue;
                    }
                    String doctorClinic = dataSnapshot.child("clinic").getValue(String.class);
                    Log.i("Current Clinic Name is", doctorClinic);
                    String doctorDepartment = dataSnapshot.child("department").getValue(String.class);
                    Log.i("Current Depart Name is", doctorDepartment);
                    String doctorFullName = dataSnapshot.child("firstName").getValue(String.class) + " " + dataSnapshot.child("lastName").getValue(String.class);
                    Log.i("Current Doctor Name is", doctorFullName);
                    String doctorUid = dataSnapshot.getKey();
                    Log.i("Current Doctor uid is", doctorUid);
                    String doctorImageUrl = dataSnapshot.child("photoUrl").getValue(String.class);
                    Log.i("Current Doctor image is", doctorImageUrl);
                    Doctor newDoctor = new Doctor(doctorFullName, doctorClinic, doctorDepartment, doctorUid, doctorImageUrl);
                    totalDoctorList.add(newDoctor);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.i("Status", "Cancelled");
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Search Button", "Clicked");

                String clinicName = clinicSpin.getSelectedItem().toString();
                Log.i("Chosen Clinic Name is", clinicName);
                String departmentName = departmentSpin.getSelectedItem().toString();
                Log.i("Chosen Department is", departmentName);

                List<Doctor> doctor_list = retrieveDoctors(clinicName, departmentName, totalDoctorList);

                Log.i("find doctors", doctor_list.get(0).getFullName());

                if (doctor_list.size() != 0){
                    addDoctorModels.clear();
                    for (int i = 0; i < doctor_list.size(); i++ ){
                        Log.i("current doctors", doctor_list.get(i).getFullName());
                        Doctor currentDoctor = doctor_list.get(i);

                        boolean isAdded = false;

                        if (!addedDoctorList.isEmpty()){
                            if (addedDoctorList.contains(currentDoctor.getUid())){
                                isAdded = true;
                            }
                        }

                        addDoctorModel addDoctorModel = new addDoctorModel(currentDoctor.getUid(),
                                currentDoctor.getFullName(),
                                currentDoctor.getClinicName(),
                                currentDoctor.getDepartmentName(),
                                currentDoctor.getImageUrl(), isAdded);


                        addDoctorModels.add(addDoctorModel);
                    }
                    adddoctorAdapter.notifyDataSetChanged();
                }

            }
        });

        return root;
    }

    private List<Doctor> retrieveDoctors(String clinicName, String departmentName, List<Doctor> doctorList) {

        Log.i("Status", "Started Retrieving Doctors");

        List<Doctor> searchedDoctors = new ArrayList<>();

        Log.i("Status", "list created");

        for (Doctor doctor : doctorList){
            if (doctor.getClinicName().equals(clinicName) && doctor.getDepartmentName().equals(departmentName)){
                searchedDoctors.add(doctor);
            }
        }

        return searchedDoctors;

    }

}
