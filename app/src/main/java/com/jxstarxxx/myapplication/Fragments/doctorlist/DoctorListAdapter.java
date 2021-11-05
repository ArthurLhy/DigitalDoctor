package com.jxstarxxx.myapplication.Fragments.doctorlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jxstarxxx.myapplication.Chat.ChatActivity;
import com.jxstarxxx.myapplication.DTO.ChatListDoctor;
import com.jxstarxxx.myapplication.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.doctorSearchViewHolder> {

    private final Context context;
    List<ChatListDoctor> chatListDoctors;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserUid = firebaseUser.getUid();

    public class doctorSearchViewHolder extends RecyclerView.ViewHolder{
        TextView fullName, clinicName, departmentName;
        ImageView profilePic;
        LinearLayout root;

        public doctorSearchViewHolder(View itemView){
            super(itemView);

            this.fullName = (TextView) itemView.findViewById(R.id.doctorlist_fullname);
            this.clinicName = (TextView) itemView.findViewById(R.id.doctorlist_clinic);
            this.departmentName = (TextView) itemView.findViewById(R.id.doctorlist_depart);
            this.profilePic = (ImageView) itemView.findViewById(R.id.doctorlist_image);
            this.root = (LinearLayout) itemView.findViewById(R.id.doctor_list_card_root);

        }
    }

    public DoctorListAdapter(Context context, List<ChatListDoctor> chatListDoctors) {
        this.context = context;
        this.chatListDoctors = chatListDoctors;
    }

    @NonNull
    @NotNull
    @Override
    public doctorSearchViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctorlist_cardview, parent, false);
        return new DoctorListAdapter.doctorSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull doctorSearchViewHolder holder, int position) {

        ChatListDoctor chatListDoctor = chatListDoctors.get(position);

        holder.fullName.setText(chatListDoctor.getFullName());
        holder.clinicName.setText(chatListDoctor.getClinicName());
        holder.departmentName.setText(chatListDoctor.getDepartmentName());

        if(!chatListDoctor.getProfilePic().isEmpty()){
            Picasso.get().load(chatListDoctor.getProfilePic()).into(holder.profilePic);
        }

        final String[] chatId = new String[1];

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (chatListDoctor.isChatted()){
                    chatId[0] = snapshot.child("user").child(currentUserUid).child("friendList").child(chatListDoctor.getUid()).child("chatID").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatListDoctor.isChatted()){
                    chatId[0] = databaseReference.child("chat").push().getKey();
                    databaseReference.child("user").child(currentUserUid).child("friendList").child(chatListDoctor.getUid()).child("chatted").setValue(true);
                    databaseReference.child("user").child(currentUserUid).child("friendList").child(chatListDoctor.getUid()).child("chatID").setValue(chatId[0]);
                    databaseReference.child("user").child(chatListDoctor.getUid()).child("friendList").child(currentUserUid).child("chatted").setValue(true);
                    chatListDoctor.setChatted(true);
                    databaseReference.child("user").child(chatListDoctor.getUid()).child("friendList").child(currentUserUid).child("chatID").setValue(chatId[0]);
                    databaseReference.child("chat").child(chatId[0]).child("user_1").setValue(currentUserUid);
                    databaseReference.child("chat").child(chatId[0]).child("user_2").setValue(chatListDoctor.getUid());
                }

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("username", chatListDoctor.getUsername());
                intent.putExtra("userImage", chatListDoctor.getProfilePic());
                intent.putExtra("userID", chatListDoctor.getUid());
                intent.putExtra("chatID", chatId[0]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatListDoctors.size();
    }



}
