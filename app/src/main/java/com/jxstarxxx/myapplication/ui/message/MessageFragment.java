package com.jxstarxxx.myapplication.ui.message;

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
import androidx.recyclerview.widget.RecyclerView;

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

    private FragmentMessageBinding binding;
    private String this_user_id;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(MessageViewModel.class);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.child("users").getChildren()) {

                    final String user_id = dataSnapshot.getKey();
                    if (!user_id.equals(this_user_id)) {
                        final String user_name = dataSnapshot.child("name").getValue(String.class);
                        MessageList messageList = new MessageList(user_name, user_id, "",0);
                        messageLists.add(messageList);
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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