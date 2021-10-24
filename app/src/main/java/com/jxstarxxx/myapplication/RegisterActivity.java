package com.jxstarxxx.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerActivity_emailID, registerActivity_password;
    private Button registerActivity_uploadPhoto, registerActivity_registerButton;
    private ImageView registerActivity_profilePic;
    private EditText registerActivity_username;
    private EditText registerActivity_firstName;
    private EditText registerActivity_lastName;
    private TextView registerActivity_dob;
    private DatePickerDialog.OnDateSetListener dobSetListener;

    private FirebaseAuth registerActivity_firebaseAuth;
    private FirebaseDatabase registerActivity_firebaseDatabase;
    private FirebaseStorage registerActivity_firebaseStorage;

    private DatabaseReference myUserRef;
    private StorageReference myUserStorageRef;

    private String photoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        castComponents();
        references();

        registerActivity_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dobSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                datePickerDialog.show();
            }
        });

        registerActivity_uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 200);
            }
        });

        dobSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dob = day + "/" + month + "/" +year;
                registerActivity_dob.setText(dob);
            }
        };


        registerActivity_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = registerActivity_password.getText().toString();

                if(checkPassword(pass)){
                    String emailid = registerActivity_emailID.getText().toString().trim();
                    signupUserMethod(emailid, pass);
                }else{
                    Toast.makeText(RegisterActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupUserMethod(String emailid, String pass) {
        registerActivity_firebaseAuth.createUserWithEmailAndPassword(emailid, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    String uid = registerActivity_firebaseAuth.getCurrentUser().getUid();
                    uploadProfilePhoto(uid);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadProfilePhoto(String uid) {
        registerActivity_profilePic.setDrawingCacheEnabled(true);
        registerActivity_profilePic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) registerActivity_profilePic.getDrawable()).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] data = os.toByteArray();

        UploadTask uploadTask = myUserStorageRef.child(uid+".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                Toast.makeText(RegisterActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                uploadUserData(uid);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getPhotoUrl(uid);
            }
        });
    }

    private void getPhotoUrl(String uid) {
        myUserStorageRef.child(uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoUrl = uri.toString();
                uploadUserData(uid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                uploadUserData(uid);
            }
        });
    }

    private void uploadUserData(String uid) {
        User user = new User();
        user.setEmail(registerActivity_emailID.getText().toString().trim());
        user.setFirstName(registerActivity_firstName.getText().toString().trim());
        user.setLastName(registerActivity_lastName.getText().toString().trim());
        user.setDob(registerActivity_dob.getText().toString());
        user.setUsername(registerActivity_username.getText().toString().trim());
        user.setIsDoctor(false);
        user.setPhotoUrl(photoUrl);

        myUserRef.child(uid).setValue(user);
        registerActivity_firebaseAuth.signOut();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

        RegisterActivity.this.finish();

    }

    private void castComponents(){
        registerActivity_emailID = (EditText) findViewById(R.id.register_user_box);
        registerActivity_password = (EditText) findViewById(R.id.register_pass_box);
        registerActivity_profilePic = (ImageView) findViewById(R.id.register_profilepic);
        registerActivity_username = (EditText) findViewById(R.id.register_un_box);
        registerActivity_firstName = (EditText) findViewById(R.id.register_fn_box);
        registerActivity_lastName = (EditText) findViewById(R.id.register_ln_box);
        registerActivity_registerButton = (Button) findViewById(R.id.register_button);
        registerActivity_uploadPhoto = (Button) findViewById(R.id.upload_photo_btn);
        registerActivity_dob = (TextView) findViewById(R.id.register_select_dob);

        registerActivity_firebaseAuth = FirebaseAuth.getInstance();
        registerActivity_firebaseDatabase = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/");
        registerActivity_firebaseStorage = FirebaseStorage.getInstance("gs://mobile-chat-demo-cacdf.appspot.com/");

    }

    private void references(){
        myUserRef = registerActivity_firebaseDatabase.getReference("user");
        myUserStorageRef = registerActivity_firebaseStorage.getReference("user")
                .child("profilepic");
    }

    private boolean checkPassword(String password){
        return !password.equals("") && password.length() >= 8;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 200) {
                Uri selectImageUri = data.getData();
                if (null != selectImageUri) {
                    registerActivity_profilePic.setImageURI(selectImageUri);
                }
            }
        }
    }

}