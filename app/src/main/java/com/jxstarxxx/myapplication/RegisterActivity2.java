package com.jxstarxxx.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity2 extends AppCompatActivity {

    private EditText registerActivity_emailID, registerActivity_password;
    private Button registerActivity_registered, registerActivity_registerButton;
    private ImageView registerActivity_profilePic;

    private FirebaseAuth registerActivity_firebaseAuth;
    private FirebaseDatabase registerActivity_firebaseDatabase;
    private FirebaseStorage registerActivity_firebaseStorage;

    private String profilePicStatus;
    private DatabaseReference myUserRef;
    private StorageReference myUserStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        getSupportActionBar().hide();

        CastComponents();
//        References();

        profilePicStatus = "nothing";

        registerActivity_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = registerActivity_password.getText().toString();

                if(CheckPassword(pass)){
                    String emailid = registerActivity_emailID.getText().toString().trim();
                    SignupUserMethod(emailid, pass);
                }else{
                    Toast.makeText(RegisterActivity2.this, "something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SignupUserMethod(String emailid, String pass) {
        registerActivity_firebaseAuth.createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity2.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    SignInMethod();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void SignInMethod() {
        String emailid = registerActivity_emailID.getText().toString().trim();
        String pass = registerActivity_password.getText().toString().trim();
        registerActivity_firebaseAuth.signInWithEmailAndPassword(emailid, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UploadUserData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadUserData() {
        String ProfilePicUrl = profilePicStatus;

        uploadUserDataJavaClass userData = new uploadUserDataJavaClass(ProfilePicUrl);
        myUserRef.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CastComponents(){
        registerActivity_emailID = (EditText) findViewById(R.id.register_user_box);
        registerActivity_password = (EditText) findViewById(R.id.register_pass_box);
        registerActivity_profilePic = (ImageView) findViewById(R.id.register_profilepic);
        registerActivity_registered = (Button) findViewById(R.id.register_back_button);
        registerActivity_registerButton = (Button) findViewById(R.id.register_button);

        registerActivity_firebaseAuth = FirebaseAuth.getInstance();
        registerActivity_firebaseDatabase = FirebaseDatabase.getInstance();
        registerActivity_firebaseStorage = FirebaseStorage.getInstance();

    }

    private void References(){
        myUserRef = registerActivity_firebaseDatabase.getReference("users")
                .child(registerActivity_firebaseAuth.getCurrentUser().getUid());
        myUserStorageRef = registerActivity_firebaseStorage.getReference("users")
                .child("profilepic");
    }

    private boolean CheckPassword(String password){
        return !password.equals("") && password.length() >= 8;
    }

    private boolean CheckProfilePicStatus(String status){
        return !profilePicStatus.equals("nothing");
    }
}