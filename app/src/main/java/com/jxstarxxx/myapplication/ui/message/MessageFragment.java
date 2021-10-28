package com.jxstarxxx.myapplication.ui.message;

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
import com.jxstarxxx.myapplication.databinding.FragmentMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private final List<MessageList> messageLists = new ArrayList<>();
    private MessageViewModel homeViewModel;
    private RecyclerView messageCycView;
    private MessageAdapter adapter;
    private long newestTimestamp;
    private String lastMessage = "";
    private int messageUnseen = 0;
    private String chatID = "0";
    private boolean gotData = false;
    private String userId, userName, userImage;

    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    private FragmentMessageBinding binding;
    private final String thisUserId = auth.getUid();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(MessageViewModel.class);
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        messageCycView = binding.messageRecycler;
        messageCycView.setHasFixedSize(true);
        messageCycView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new MessageAdapter(messageLists, this.getActivity());
        messageCycView.setAdapter(adapter);

        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Message List ...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();
                List<String> have_find = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.child("user").getChildren()) {
                    final String our_user_id = dataSnapshot.getKey();
                    Log.i("Message List", "finding user: " + our_user_id);

                    if (our_user_id.equals(thisUserId)) {
                        List<String> chatList = new ArrayList<>();

                        for (DataSnapshot dataSnapshot0 : dataSnapshot.child("friendList").getChildren()) {
                            userId = dataSnapshot0.getKey();
                            final boolean chatted = (boolean) dataSnapshot0.child("chatted").getValue();

                            if ((!userId.equals(thisUserId)) && chatted) {
                                Log.i("Message List", "finding friend:  " + userId);
                                chatID = dataSnapshot0.child("chatID").getValue(String.class);
                                chatList.add(chatID);
                                lastMessage = "";
                                messageUnseen = 0;

                                databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {

                                        for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {

                                            if (chatList.contains(dataSnapshot1.getKey()) && dataSnapshot1.hasChild("messages")) {
                                                chatID = dataSnapshot1.getKey();
                                                final String user1 = dataSnapshot1.child("user_1").getValue(String.class);
                                                final String user2 = dataSnapshot1.child("user_2").getValue(String.class);
                                                gotData = true;
                                                newestTimestamp = 0;

                                                if (user1.equals(thisUserId)) {
                                                    userId = user2;
                                                }
                                                else {
                                                    userId = user1;
                                                }
                                                userName = snapshot.child("user").child(userId).child("username").getValue(String.class);
                                                userImage = snapshot.child("user").child(userId).child("photoUrl").getValue(String.class);

                                                Log.i("Message List", "find the chat" + chatID);
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("messages").getChildren()) {
                                                    final long Timestamp = Long.parseLong(dataSnapshot2.getKey());
                                                    if (MessageFragment.this.getActivity() != null) {
                                                        final long lastUnseenTime = Long.parseLong(LocalData.getLastMessage(chatID, MessageFragment.this.getActivity()));
                                                        if (Timestamp > lastUnseenTime) {
                                                            messageUnseen++;
                                                        }
                                                    }
                                                    if (Timestamp > newestTimestamp && dataSnapshot2.child("user").getValue(String.class).equals(userId)) {
                                                        newestTimestamp = Timestamp;
                                                    }
                                                }
                                                lastMessage = dataSnapshot1.child("messages").child(String.valueOf(newestTimestamp)).child("message").getValue(String.class);

                                            }

                                            if (gotData) {
                                                MessageList messageList = new MessageList(chatID, userName, userId, userImage, lastMessage, messageUnseen);

                                                if (messageLists.isEmpty()) {
                                                    Log.i("Message List", "add the chat to the list: " + chatID + userName);
                                                    messageLists.add(messageList);
                                                    adapter.updateList(messageLists);
                                                    gotData = false;
                                                }

                                                for (MessageList i : messageLists) {
                                                    if (i.getChatID().equals(messageList.getChatID())) {
                                                        gotData = false;
                                                        break;
                                                    }
                                                }
                                                if (gotData) {
                                                    Log.i("Message List", "add the chat to the list: " + chatID + userName);
                                                    messageLists.add(messageList);
                                                    adapter.updateList(messageLists);
                                                }
                                                gotData = false;
                                            }
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