package com.jxstarxxx.myapplication.ui.message;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

public class MessageFragment extends Fragment {

    private final List<MessageList> messageLists = new ArrayList<>();
    private MessageViewModel homeViewModel;
    private RecyclerView message_cyc_view;
    private MessageAdapter adapter;
    private long newest_timestamp;
    private String last_message = "";
    private String chatID = "";

    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    private FragmentMessageBinding binding;
    private String this_user_id = auth.getUid();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(MessageViewModel.class);
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        message_cyc_view = binding.messageRecycler;
        message_cyc_view.setHasFixedSize(true);
        message_cyc_view.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new MessageAdapter(messageLists, this.getActivity());
        message_cyc_view.setAdapter(adapter);

        ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("the message list is loading");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();

                for (DataSnapshot dataSnapshot: snapshot.child("user").getChildren()) {

                    final String user_id = dataSnapshot.getKey();
                    if (!user_id.equals(this_user_id)) {
                        final String user_name = dataSnapshot.child("username").getValue(String.class);
                        final String user_image = dataSnapshot.child("photoUrl").getValue(String.class);
                        last_message = "";
                        int message_unseen = 0;
                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long chat_count = snapshot.getChildrenCount();
                                if (chat_count > 0) {
                                    for (DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                                        chatID = dataSnapshot1.getKey();
                                        final String user1 = dataSnapshot1.child("user_1").getValue(String.class);
                                        final String user2 = dataSnapshot1.child("user_2").getValue(String.class);
                                        if ((user1.equals(user_id) && user2.equals(this_user_id)) || (user1.equals(this_user_id) && user2.equals(user_id))) {
                                            newest_timestamp = 0;
                                            for (DataSnapshot dataSnapshot2: dataSnapshot1.child("messages").getChildren()) {
                                                final long Timestamp =  Long.parseLong(dataSnapshot2.getKey());

                                                if (Timestamp > newest_timestamp && dataSnapshot2.child("user").getValue(String.class).equals(user_id)) {
                                                    newest_timestamp = Timestamp;
                                                }
                                            }
                                            last_message = dataSnapshot1.child("messages").child(String.valueOf(newest_timestamp)).child("message").getValue(String.class);
                                        }
                                    }
                                }
                                MessageList messageList = new MessageList(chatID, user_name, user_id, user_image,last_message,message_unseen);
                                messageLists.add(messageList);
                                adapter.updateList(messageLists);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                            }
                        });
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