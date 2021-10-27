package com.jxstarxxx.myapplication.ui.account;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jxstarxxx.myapplication.DashboardActivity;
import com.jxstarxxx.myapplication.LoginActivity;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.RegisterActivity;
import com.jxstarxxx.myapplication.User;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    private ImageView profilePic;
    private TextView tv_username, tv_gender;
    private LinearLayout ll_profilePic, ll_username, ll_gender;
    private Button backToAccount;

    private String uid;

    private ProgressDialog progressDialog1, progressDialog2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog1 = new ProgressDialog(ProfileActivity.this);
        progressDialog1.setCancelable(false);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();

        progressDialog2 = new ProgressDialog(ProfileActivity.this);
        progressDialog2.setCancelable(false);
        progressDialog2.setMessage("Uploading...");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/");
        firebaseStorage = FirebaseStorage.getInstance("gs://mobile-chat-demo-cacdf.appspot.com/");

        databaseRef = firebaseDatabase.getReference();
        storageRef = firebaseStorage.getReference();

        uid = firebaseAuth.getUid();
        if (uid == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            ProfileActivity.this.finish();
        }

        profilePic = findViewById(R.id.profile_pic);
        ll_profilePic = findViewById(R.id.profile_pic_ll);
        ll_username = findViewById(R.id.user_name_ll);
        ll_gender = findViewById(R.id.gender_ll);
        tv_username = findViewById(R.id.user_name);
        tv_gender = findViewById(R.id.gender);
        backToAccount = findViewById(R.id.back_account);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_username.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Warning", "loadPost:onCancelled", error.toException());
            }
        };
        databaseRef.child("user").child(uid).child("username").addValueEventListener(valueEventListener);

        databaseRef.child("user").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(ProfileActivity.this).load(user.getPhotoUrl()).into(profilePic);
                tv_username.setText(user.getUsername());
                if (user.getGender() == 1) {
                    tv_gender.setText("Male");
                } else if (user.getGender() == 2) {
                    tv_gender.setText("Female");
                } else {
                    tv_gender.setText("Unknown");
                }
                progressDialog1.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ll_profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 200);
            }
        });

        ll_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tv_username.getText().toString();
                usernameDialog(username);
            }
        });

        ll_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = tv_gender.getText().toString();
                genderDialog(gender);
            }
        });

        backToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 200) {
                Uri selectImageUri = data.getData();
                if (null != selectImageUri) {
                    progressDialog2.show();
                    profilePic.setImageURI(selectImageUri);
                    uploadProfilePhoto(uid);
                }
            }
        }
    }

    private void uploadProfilePhoto(String uid) {
        profilePic.setDrawingCacheEnabled(true);
        profilePic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] data = os.toByteArray();

        UploadTask uploadTask = storageRef.child("user/profilepic/"+uid+".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                Toast.makeText(ProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog2.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePic.setImageBitmap(bitmap);
                getPhotoUrl(uid);
            }
        });
    }

    private void getPhotoUrl(String uid) {
        storageRef.child("user/profilepic/"+uid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                databaseRef.child("user").child(uid).child("photoUrl").setValue(uri.toString());
                Toast.makeText(ProfileActivity.this, "Successfully change profile photo", Toast.LENGTH_SHORT).show();
                progressDialog2.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog2.dismiss();
            }
        });
    }

    private void genderDialog(String sex) {
        int sexFlag=0;
        if ("Male".equals(sex)) {
            sexFlag=0;
        } else if ("Female".equals(sex)) {
            sexFlag=1;
        }
        final String items[] = {"Male","Female"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("set gender");
        builder.setSingleChoiceItems(items,sexFlag,new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(ProfileActivity.this,items[which],
                        Toast.LENGTH_SHORT).show();
                setGender(items[which]);
            }
        });
        builder.create().show();
    }

    private void setGender(String gender){
        tv_gender.setText(gender);
        long genderId = 1;
        if (gender == "Female") {
            genderId = 2;
        }
        databaseRef.child("user").child(uid).child("gender").setValue(genderId);
    }

    private void usernameDialog(String username) {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        final EditText input = new EditText(ProfileActivity.this);
        builder.setTitle("User Name");
        builder.setView(input);
        builder.setButton(builder.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                progressDialog1.dismiss();
                progressDialog2.show();
                firebaseDatabase.getReference().child("user").child(uid).child("username")
                        .setValue(input.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileActivity.this, "Successfully update username", Toast.LENGTH_SHORT).show();
                        progressDialog2.dismiss();
                        //finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog2.dismiss();
                    }
                });

            }
        });

        builder.setButton(builder.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //finish();
            }
        });

        builder.show();

        Button btnPositive = builder.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }


}
