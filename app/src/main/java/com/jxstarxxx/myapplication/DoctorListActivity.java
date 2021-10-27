package com.jxstarxxx.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jxstarxxx.myapplication.databinding.ActivityDoctorListBinding;

public class DoctorListActivity extends AppCompatActivity {

    private ActivityDoctorListBinding binding2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding2 = ActivityDoctorListBinding.inflate(getLayoutInflater());
        setContentView(binding2.getRoot());

        BottomNavigationView navView2 = findViewById(R.id.nav_view2);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration2 = new AppBarConfiguration.Builder(
                R.id.navigation_doctor_list, R.id.navigation_add_doctor)
                .build();
        NavController navController2 = Navigation.findNavController(this, R.id.nav_host_fragment_activity_doctor_list);
        NavigationUI.setupActionBarWithNavController(this, navController2, appBarConfiguration2);
        NavigationUI.setupWithNavController(binding2.navView2, navController2);

    }
}