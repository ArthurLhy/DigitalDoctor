package com.jxstarxxx.myapplication.Fragments.adddoctor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import com.jxstarxxx.myapplication.DTO.FriendListDoctor;
import com.jxstarxxx.myapplication.DTO.Doctor;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.databinding.FragmentAdddoctorBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class AddDoctorFragment extends Fragment {

    private AddDoctorViewModel adddoctorViewModel;
    private FragmentAdddoctorBinding binding;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserUid = firebaseUser.getUid();
    private FirebaseDatabase database;

    private RecyclerView addDoctorRecyclerView;
    private List<String> clinicList;
    private String[] departmentList;

    private List<FriendListDoctor> friendListDoctors = new ArrayList<>();

    private AutoCompleteTextView clinicText;
    private AutoCompleteTextView departmentText;
    //private Spinner departmentSpin;

    private Button searchButton;
    private AddDoctorAdapter adddoctorAdapter;

    private ProgressDialog progressDialog;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adddoctorViewModel = new ViewModelProvider(this).get(AddDoctorViewModel.class);
        binding = FragmentAdddoctorBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        addDoctorRecyclerView = (RecyclerView) root.findViewById(R.id.add_doctor_recyle);
        addDoctorRecyclerView.setHasFixedSize(true);
        addDoctorRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        clinicText = root.findViewById(R.id.text_clinic);
        departmentText = root.findViewById(R.id.text_department);

        clinicList = new ArrayList<>();

        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Doctor Information ...");
        progressDialog.show();

        //Go through the clinics in database to get a list of clinic names
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.child("clinic").getChildren()){
                    String clinicName = dataSnapshot.getKey();
                    clinicList.add(clinicName);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        departmentList = getResources().getStringArray(R.array.departmentList);

        ArrayAdapter<String> clinicAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item_style_add_doctor, clinicList);
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item_style_add_doctor, departmentList);

        clinicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clinicText.setAdapter(clinicAdapter);
        departmentText.setAdapter(departmentAdapter);

        adddoctorAdapter = new AddDoctorAdapter(this.getActivity(), friendListDoctors);
        addDoctorRecyclerView.setAdapter(adddoctorAdapter);

        searchButton = (Button) root.findViewById(R.id.add_doctor_search_button);

        List<Doctor> totalDoctorList = new ArrayList<>();

        List<String> addedDoctorList = new ArrayList<>();

        // Retrieve doctors in user's friend list
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

        // Retrieve all the doctor
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.i("Issue", "snapshot does not exist");
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //Check if the user is current user.
                    if (dataSnapshot.getKey().equals(currentUserUid)){
                        continue;
                    }
                    boolean isDoctor = (Boolean) dataSnapshot.child("isDoctor").getValue();

                    if (!isDoctor){
                        continue;
                    }

                    String doctorClinic = dataSnapshot.child("clinic").getValue(String.class);
                    String doctorDepartment = dataSnapshot.child("department").getValue(String.class);
                    String doctorFullName = dataSnapshot.child("firstName").getValue(String.class) + " " + dataSnapshot.child("lastName").getValue(String.class);
                    String doctorUid = dataSnapshot.getKey();
                    String doctorImageUrl = dataSnapshot.child("photoUrl").getValue(String.class);

                    Doctor newDoctor = new Doctor(doctorFullName, doctorClinic, doctorDepartment, doctorUid, doctorImageUrl);
                    totalDoctorList.add(newDoctor);
                }
                progressDialog.dismiss();
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.i("Status", "Cancelled");
            }


        });




        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clinicName = clinicText.getText().toString();
                String departmentName = departmentText.getText().toString();
                friendListDoctors.clear();

                // filter doctors using clinic and department
                List<Doctor> doctorList = retrieveDoctors(clinicName, departmentName, totalDoctorList);
                if (doctorList.size() != 0){
                    for (int i = 0; i < doctorList.size(); i++ ){
                        Doctor currentDoctor = doctorList.get(i);
                        boolean isAdded = false;
                        if (!addedDoctorList.isEmpty()){
                            if (addedDoctorList.contains(currentDoctor.getUid())){
                                isAdded = true;
                            }
                        }

                        FriendListDoctor friendListDoctor = new FriendListDoctor(currentDoctor.getUid(),
                                currentDoctor.getFullName(),
                                currentDoctor.getClinicName(),
                                currentDoctor.getDepartmentName(),
                                currentDoctor.getImageUrl(), isAdded);

                        friendListDoctors.add(friendListDoctor);
                    }
                }
                adddoctorAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    private List<Doctor> retrieveDoctors(String clinicName, String departmentName, List<Doctor> doctorList) {
        List<Doctor> searchedDoctors = new ArrayList<>();
        for (Doctor doctor : doctorList){
            if (doctor.getClinicName().equals(clinicName) && doctor.getDepartmentName().equals(departmentName)){
                searchedDoctors.add(doctor);
            }
        }
        return searchedDoctors;
    }

}
