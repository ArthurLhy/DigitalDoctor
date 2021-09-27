package com.example.medicalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText vPhoneNumber, vCode;
    private Button vSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vPhoneNumber = findViewById(R.id.phoneNumber);
        vCode = findViewById(R.id.verificationCode);
        vSendCode = findViewById(R.id.sendCode);
        
    }
}