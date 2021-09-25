package com.jxstarxxx.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jxstarxxx.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private EditText loginActivity_emailID, loginActivity_password;
    private Button loginActivity_loginBun;
    private TextView loginActivity_newUser;
    private FirebaseAuth loginActivity_firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        CastComponents();

        loginActivity_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity2.class));
            }
        });

        loginActivity_loginBun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = loginActivity_emailID.getText().toString().trim();
                String password = loginActivity_password.getText().toString();

                if(emailID.equals("") && password.equals("")){
                    Toast.makeText(MainActivity.this, "Email ID or password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    loginUserMethod(emailID, password);
                }
            }
        });


    }

    private void loginUserMethod(String userid, String password){
        loginActivity_firebaseAuth.signInWithEmailAndPassword(userid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(MainActivity.this, "logged in", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CastComponents(){
        loginActivity_emailID = (EditText) findViewById(R.id.text_email);
        loginActivity_password= (EditText) findViewById(R.id.text_password);
        loginActivity_loginBun = (Button) findViewById(R.id.button_first);
        loginActivity_newUser = (TextView) findViewById(R.id.textview_third);
        loginActivity_firebaseAuth = FirebaseAuth.getInstance();
    }

}