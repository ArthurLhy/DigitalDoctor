package com.jxstarxxx.myapplication.ui.message;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxstarxxx.myapplication.DashboardActivity;
import com.jxstarxxx.myapplication.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageFragment extends Fragment {

    private final List<MessageList> messageLists = new ArrayList<>();
    private MessageViewModel homeViewModel;
    private RecyclerView message_cyc_view;
    private MessageAdapter adapter;
    private long newest_timestamp;
    private String last_message = "";
    private int message_unseen = 0;
    private String chatID = "0";
    private boolean gotdata = false;
    private boolean done = false;

    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    private FragmentMessageBinding binding;
    private final String this_user_id = auth.getUid();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(MessageViewModel.class);
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        message_cyc_view = binding.messageRecycler;
        message_cyc_view.setHasFixedSize(true);
        message_cyc_view.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new MessageAdapter(messageLists, this.getActivity());
        message_cyc_view.setAdapter(adapter);

        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Message List ...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();
                List<String> have_find = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.child("user").getChildren()) {
                    final String our_user_id = dataSnapshot.getKey();
                    Log.i("Message List", "finding user: " + our_user_id);

                    if (our_user_id.equals(this_user_id)) {
                        for (DataSnapshot dataSnapshot0: dataSnapshot.child("doctorList").getChildren()) {
                            final String user_id = dataSnapshot0.getKey();

                            if (!user_id.equals(this_user_id)) {
                                Log.i("Message List", "finding friend:  " + user_id);

                                final String user_name = dataSnapshot0.child("username").getValue(String.class);
                                final String user_image = dataSnapshot0.child("photoUrl").getValue(String.class);
                                last_message = "";
                                message_unseen = 0;
                                databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long chat_count = snapshot.getChildrenCount();

                                        if (chat_count > 0) {
                                            for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {

                                                if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                                    final String user1 = dataSnapshot1.child("user_1").getValue(String.class);
                                                    final String user2 = dataSnapshot1.child("user_2").getValue(String.class);

                                                    if ((user1.equals(user_id) && user2.equals(this_user_id)) || (user1.equals(this_user_id) && user2.equals(user_id))) {
                                                        chatID = dataSnapshot1.getKey();
                                                        gotdata = true;
                                                        newest_timestamp = 0;

                                                        Log.i("Message List", "find the chat" + chatID);
                                                        for (DataSnapshot dataSnapshot2: dataSnapshot1.child("messages").getChildren()) {
                                                            final long Timestamp =  Long.parseLong(dataSnapshot2.getKey());
                                                            if (MessageFragment.this.getActivity() != null) {
                                                                final long lastUnseenTime = Long.parseLong(LocalData.getLastMessage(chatID, MessageFragment.this.getActivity()));
                                                                if(Timestamp > lastUnseenTime) {
                                                                    message_unseen++;
                                                                }
                                                            }
                                                            if (Timestamp > newest_timestamp && dataSnapshot2.child("user").getValue(String.class).equals(user_id)) {
                                                                newest_timestamp = Timestamp;
                                                            }
                                                        }
                                                        last_message = dataSnapshot1.child("messages").child(String.valueOf(newest_timestamp)).child("message").getValue(String.class);
                                                    }
                                                }
                                            }
                                        }
                                        if (gotdata) {
                                            MessageList messageList = new MessageList(chatID, user_name, user_id, user_image,last_message,message_unseen);

                                            if (messageLists.isEmpty()) {
                                                Log.i("Message List", "add the chat to the list: " + chatID + user_name);
                                                messageLists.add(messageList);
                                                adapter.updateList(messageLists);
                                                gotdata = false;
                                            }

                                            for (MessageList i:messageLists) {
                                                if (i.getChatID().equals(messageList.getChatID())) {
                                                    gotdata = false;
                                                    break;
                                                }
                                            }
                                            if (gotdata) {
                                                Log.i("Message List", "add the chat to the list: " + chatID + user_name);
                                                messageLists.add(messageList);
                                                adapter.updateList(messageLists);
                                            }
                                            gotdata = false;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

//        final TextView textView = binding.textMessage;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}