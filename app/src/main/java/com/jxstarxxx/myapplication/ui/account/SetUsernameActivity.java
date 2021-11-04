package com.jxstarxxx.myapplication.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.jxstarxxx.myapplication.LoginActivity;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.User;

public class SetUsernameActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private EditText set_username;
    private Button btn_save, btn_back;
    private String uid;
    private ProgressDialog progressDialog1, progressDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);

        progressDialog1 = new ProgressDialog(SetUsernameActivity.this);
        progressDialog1.setCancelable(false);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();

        progressDialog2 = new ProgressDialog(SetUsernameActivity.this);
        progressDialog2.setCancelable(false);
        progressDialog2.setMessage("Saving...");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/");

        uid = firebaseAuth.getUid();
        if (uid == null) {
            startActivity(new Intent(SetUsernameActivity.this, LoginActivity.class));
            SetUsernameActivity.this.finish();
        }

        set_username = findViewById(R.id.set_username);
        btn_save = findViewById(R.id.save_btn);
        btn_back = findViewById(R.id.back_btn);



        firebaseDatabase.getReference().child("user").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                set_username.setText(user.getUsername());
                progressDialog1.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(SetUsernameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog1.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog2.show();
                firebaseDatabase.getReference().child("user").child(uid).child("username")
                        .setValue(set_username.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SetUsernameActivity.this, "Successfully update username", Toast.LENGTH_SHORT).show();
                        progressDialog2.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetUsernameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog2.dismiss();
                    }
                });

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}