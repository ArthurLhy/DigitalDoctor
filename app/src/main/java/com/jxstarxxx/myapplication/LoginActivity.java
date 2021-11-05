package com.jxstarxxx.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jxstarxxx.myapplication.MyUtils.LoginTextClear;
//import com.jxstarxxx.myapplication.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    //private ActivityMainBinding binding;

    private EditText loginActivity_emailID, loginActivity_password;
    private Button loginActivity_loginBun;
    private TextView loginActivity_newUser;
    private FirebaseAuth loginActivity_firebaseAuth;
    private ImageView loginActivity_emailID_del, loginActivity_password_del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();

        castComponents();

        loginActivity_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginActivity_loginBun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = loginActivity_emailID.getText().toString().trim();
                String password = loginActivity_password.getText().toString();

                if(emailID.equals("") && password.equals("")){
                    Toast.makeText(LoginActivity.this, "Account or password cannot be empty", Toast.LENGTH_SHORT).show();
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
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void castComponents(){
        float v = 0;

        loginActivity_emailID = (EditText) findViewById(R.id.account_edit);
        loginActivity_password= (EditText) findViewById(R.id.psd_edit);
        loginActivity_loginBun = (Button) findViewById(R.id.btn_login);
        loginActivity_newUser = (TextView) findViewById(R.id.btn_register);
        loginActivity_firebaseAuth = FirebaseAuth.getInstance();
        loginActivity_emailID_del = (ImageView) findViewById(R.id.account_edit_del);
        loginActivity_password_del = (ImageView) findViewById(R.id.psd_edit_del);

        loginActivity_emailID.setTranslationX(1500);
        loginActivity_password.setTranslationX(1500);
        loginActivity_loginBun.setTranslationX(1500);
        loginActivity_newUser.setTranslationX(1500);

        loginActivity_emailID.setAlpha(v);
        loginActivity_password.setAlpha(v);
        loginActivity_loginBun.setAlpha(v);
        loginActivity_newUser.setAlpha(v);

        loginActivity_emailID.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        loginActivity_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginActivity_loginBun.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        loginActivity_newUser.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        LoginTextClear.addClearListener(loginActivity_emailID,loginActivity_emailID_del);
        LoginTextClear.addClearListener(loginActivity_password,loginActivity_password_del);
    }
}