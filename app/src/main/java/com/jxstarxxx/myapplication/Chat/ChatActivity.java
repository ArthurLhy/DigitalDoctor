package com.jxstarxxx.myapplication.Chat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxstarxxx.myapplication.HeartRateActivity;
import com.jxstarxxx.myapplication.R;
import com.jxstarxxx.myapplication.ui.message.LocalData;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    private String chatID;
    private final List<Message> messageList = new ArrayList<>();
    private RecyclerView recyclerChat;
    private ChatAdapter chatAdapter;
    private boolean first = true;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;
    private boolean addBtnClicked = false;

    private FloatingActionButton floatingAddBtn;
    private FloatingActionButton floatingAudioBtn;
//    private String heartRate;


    private String sender_channel, receiver_channel;

    private FirebaseUser auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance().getCurrentUser();


        chatAdapter = new ChatAdapter(messageList, ChatActivity.this);
        recyclerChat = findViewById(R.id.recycle_of_chat);
        recyclerChat.setHasFixedSize(true);
        recyclerChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        recyclerChat.setAdapter(chatAdapter);

        final ImageButton back = findViewById(R.id.chat_back_button);
        final ImageButton send_message = findViewById(R.id.send_button);
        final LottieAnimationView send_lottie = findViewById(R.id.send_lottie);


        final ImageView user_image = findViewById(R.id.chat_user_image);
        final TextView user_name = findViewById(R.id.name_of_chat_user);
        final EditText message = findViewById(R.id.input_message);

        // To do here
        final String senderID = auth.getUid();
        final String Name = getIntent().getStringExtra("username");
        final String userImage = getIntent().getStringExtra("userImage");
        Picasso.get().load(userImage).into(user_image);
        chatID = getIntent().getStringExtra("chatID");
        final String receiverID = getIntent().getStringExtra("userID");
        user_name.setText(Name);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotata_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        floatingAddBtn = findViewById(R.id.floatingActionAddButton);
        floatingAudioBtn = findViewById(R.id.floatingActionAudioButton);


        send_lottie.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!(send_lottie.getSpeed() == -1f)) {
                    send_lottie.setSpeed(-1f);
                    send_lottie.playAnimation();
                }
                super.onAnimationEnd(animation);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatID.isEmpty()) {
                    chatID = "1";
                    if (snapshot.hasChild("chat")) {
                        chatID = String.valueOf(((int) snapshot.child("chat").getChildrenCount()) + 1);
                    }
                }

                if (snapshot.hasChild("chat" ) && snapshot.child("chat").child(chatID).hasChild("messages")){
                    messageList.clear();
                    for (DataSnapshot messages_in_snapshot: snapshot.child("chat").child(chatID).child("messages").getChildren()) {
                        if(messages_in_snapshot.hasChild("message") && messages_in_snapshot.hasChild("user")) {
                            final String timestamp = messages_in_snapshot.getKey();
                            Timestamp timeStamp = new Timestamp(Long.parseLong(timestamp));
                            Date time = new Date(timeStamp.getTime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                            final String userID = messages_in_snapshot.child("user").getValue(String.class);
                            final String theMessage = messages_in_snapshot.child("message").getValue(String.class);
                            Message message1 = new Message(theMessage, userID, simpleDateFormat.format(time));
                            messageList.add(message1);
                            if (first || Long.parseLong(timestamp) > Long.parseLong(LocalData.getLastMessage(chatID, senderID,ChatActivity.this))) {
                                chatAdapter.update(messageList);
                                first = false;
                            }
                            recyclerChat.scrollToPosition(messageList.size()-1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String timeStamp = String.valueOf(System.currentTimeMillis());
                if (Long.parseLong(timeStamp) > Long.parseLong(LocalData.getLastMessage(chatID, senderID, ChatActivity.this))){
                    Log.i("time stamp", timeStamp);
                    LocalData.saveLastMessage(chatID, senderID, timeStamp, ChatActivity.this);
                    chatAdapter.update(messageList);
                }
                databaseReference.child("chat").child(chatID).child("user_1").setValue(senderID);
                databaseReference.child("chat").child(chatID).child("user_2").setValue(receiverID);
                finish();
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_lottie.setSpeed(1f);
                send_lottie.playAnimation();

                final String message_send_done = message.getText().toString();
                final String timeStamp = String.valueOf(System.currentTimeMillis());

                databaseReference.child("chat").child(chatID).child("user_1").setValue(senderID);
                databaseReference.child("chat").child(chatID).child("user_2").setValue(receiverID);
                databaseReference.child("chat").child(chatID).child("messages").child(timeStamp).child("message").setValue(message_send_done);
                databaseReference.child("chat").child(chatID).child("messages").child(timeStamp).child("user").setValue(senderID);
                LocalData.saveLastMessage(chatID, senderID, timeStamp, ChatActivity.this);
                message.setText("");
            }
        });

        floatingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddBtnClicked();

            }
        });

        floatingAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, HeartRateActivity.class);
//                intent.putExtra("receiverName", Name);
//                intent.putExtra("receiverImage", userImage);
                intent.putExtra("receiverID", receiverID);
                intent.putExtra("chatId", chatID);
                startActivity(intent);
            }
        });

    }

    private void onAddBtnClicked() {
        setVisibility(addBtnClicked);
        setAnimation(addBtnClicked);
        setClickable(addBtnClicked);
        addBtnClicked = !addBtnClicked;

    }

    private void setVisibility(boolean addBtnClicked) {
        if(!addBtnClicked){
            floatingAudioBtn.setVisibility(View.GONE);
        }else{
            floatingAudioBtn.setVisibility(View.VISIBLE);
        }
    }


    private void setAnimation(boolean addBtnClicked) {
        if(!addBtnClicked){
            floatingAudioBtn.startAnimation(fromBottom);
            floatingAddBtn.startAnimation(rotateOpen);
        }else{
            floatingAudioBtn.startAnimation(toBottom);
            floatingAddBtn.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean addBtnClicked){
        if(!addBtnClicked){
            floatingAudioBtn.setEnabled(true);
        }else{
            floatingAudioBtn.setEnabled(false);
        }
    }

}