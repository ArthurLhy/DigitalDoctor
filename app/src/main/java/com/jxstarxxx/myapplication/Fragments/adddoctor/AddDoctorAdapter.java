package com.jxstarxxx.myapplication.Fragments.adddoctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jxstarxxx.myapplication.DTO.FriendListDoctor;
import com.jxstarxxx.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddDoctorAdapter extends RecyclerView.Adapter<AddDoctorAdapter.SearchViewHolder> {
    private final Context context;


    List<FriendListDoctor> friendListDoctors;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mobile-chat-demo-cacdf-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserUid = firebaseUser.getUid();

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView fullName, clinicName, departmentName;
        ImageView profile_pic;
        Button addButton;

        public SearchViewHolder(View itemView) {
            super(itemView);

            this.fullName = (TextView) itemView.findViewById(R.id.add_doctor_fullname);
            this.clinicName = (TextView) itemView.findViewById(R.id.add_doctor_clinic);
            this.departmentName = (TextView) itemView.findViewById(R.id.add_doctor_depart);
            this.profile_pic = (ImageView) itemView.findViewById(R.id.add_doctor_image);
            this.addButton = (Button) itemView.findViewById(R.id.add_doctor_button);
        }
    }


    public AddDoctorAdapter(Context context, List<FriendListDoctor> friendListDoctors) {
        this.context = context;
        this.friendListDoctors = friendListDoctors;
    }

    @NonNull
    @Override
    public AddDoctorAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_doctor_cardview, parent, false);
        return new AddDoctorAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddDoctorAdapter.SearchViewHolder holder, int position) {

        FriendListDoctor addDoctor = friendListDoctors.get(position);

        holder.fullName.setText(addDoctor.getFullName());
        holder.clinicName.setText(addDoctor.getClinicName());
        holder.departmentName.setText(addDoctor.getDepartmentName());
        if(!addDoctor.getImgUrl().isEmpty()){
            Picasso.get().load(addDoctor.getImgUrl()).into(holder.profile_pic);
        }

        //Remove the button if the doctor is already added
        if(addDoctor.isAdded()){
            holder.addButton.setVisibility(View.GONE);
        }else {
            holder.addButton.setVisibility(View.VISIBLE);
            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("user").child(currentUserUid).child("friendList").child(addDoctor.getUid()).child("chatted").setValue(false);
                    databaseReference.child("user").child(addDoctor.getUid()).child("friendList").child(currentUserUid).child("chatted").setValue(false);
                    holder.addButton.setVisibility(View.GONE);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return friendListDoctors.size();
    }
}