package com.jxstarxxx.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private EditText message;
    private ImageButton send_message;
    private CardView message_card_view;
    private androidx.appcompat.widget.Toolbar toolbar_of_chat;

    private ImageView user_image;
    private TextView user_name;

    private String message_will_be_send;
    Intent intent;

    private String receiver_name, receiver_id;
    private String sender_name, sender_id;
    private String sender_channel, receiver_channel;

    private FirebaseAuth auth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        message = findViewById(R.id.input_message);
        message_card_view = findViewById(R.id.card_view_of_send);
        send_message = findViewById(R.id.send_button);

        toolbar_of_chat = findViewById(R.id.toolbar_for_chat);
        user_image = findViewById(R.id.chat_user_image);
        user_name = findViewById(R.id.name_of_chat_user);

        intent = getIntent();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        sender_id = auth.getUid();

        //To do the intent to give this value
        receiver_id = getIntent().getStringExtra("receiver_uid");
        receiver_name = getIntent().getStringExtra("receiver_name");

        sender_channel = sender_id + receiver_id;
        receiver_channel = receiver_id + sender_id;

        toolbar_of_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click toolbar", Toast.LENGTH_SHORT).show();;
            }
        });

    }
}