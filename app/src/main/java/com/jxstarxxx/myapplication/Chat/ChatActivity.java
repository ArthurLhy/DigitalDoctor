package com.jxstarxxx.myapplication.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxstarxxx.myapplication.R;

import java.sql.Timestamp;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/");

    private String chatID;
    private RecyclerView recyclerChat;

    private CardView message_card_view;
    private androidx.appcompat.widget.Toolbar toolbar_of_chat;


    private String message_will_be_send;
    Intent intent;

    private String sender_channel, receiver_channel;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        message_card_view = findViewById(R.id.card_view_of_send);
        toolbar_of_chat = findViewById(R.id.toolbar_for_chat);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        recyclerChat = findViewById(R.id.recycle_of_chat);
        recyclerChat.setHasFixedSize(true);
        recyclerChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));


        final ImageButton back = findViewById(R.id.chat_back_button);
        final ImageButton send_message = findViewById(R.id.send_button);

        final ImageView user_image = findViewById(R.id.chat_user_image);
        final TextView user_name = findViewById(R.id.name_of_chat_user);
        final EditText message = findViewById(R.id.input_message);

        // To do here
        final String senderID = auth.getUid();
        final String Name = getIntent().getStringExtra("Name");
        final String userImage = getIntent().getStringExtra("userImage");
        chatID = getIntent().getStringExtra("chatID");
        final String receiverID = getIntent().getStringExtra("userID");



        user_name.setText(Name);
        if(chatID.isEmpty()) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("chat")) {
                        chatID = String.valueOf (((int) snapshot.child("chat").getChildrenCount()) + 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message_send_done = message.getText().toString();
                final String timeStamp = String.valueOf(new Timestamp(System.currentTimeMillis()));

                databaseReference.child("chat").child(chatID).child("first_user").setValue(senderID);
                databaseReference.child("chat").child(chatID).child("second_user").setValue(receiverID);
                databaseReference.child("chat").child(chatID).child("messages").child(timeStamp).child("message").setValue(message_send_done);
                databaseReference.child("chat").child(chatID).child("messages").child(timeStamp).child("user").setValue(senderID);

                message.setText("");
            }
        });
        toolbar_of_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click toolbar", Toast.LENGTH_SHORT).show();;
            }
        });

    }
}