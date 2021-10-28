package com.jxstarxxx.myapplication.ui.doctorlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jxstarxxx.myapplication.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.doctorSearchViewHolder> {

    private final Context context;
    List<DoctorModel> doctorModels;

    public class doctorSearchViewHolder extends RecyclerView.ViewHolder{
        TextView fullName, clinicName, departmentName;
        ImageView profilePic;

        public doctorSearchViewHolder(View itemView){
            super(itemView);

            this.fullName = (TextView) itemView.findViewById(R.id.doctorlist_fullname);
            this.clinicName = (TextView) itemView.findViewById(R.id.doctorlist_clinic);
            this.departmentName = (TextView) itemView.findViewById(R.id.doctorlist_depart);
            this.profilePic = (ImageView) itemView.findViewById(R.id.doctorlist_image);

        }
    }

    public DoctorListAdapter(Context context, List<DoctorModel> doctorModels) {
        this.context = context;
        this.doctorModels = doctorModels;
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

        DoctorModel doctorModel = doctorModels.get(position);

        holder.fullName.setText(doctorModel.getFullName());
        holder.clinicName.setText(doctorModel.getClinicName());
        holder.departmentName.setText(doctorModel.getDepartmentName());

        if(!doctorModel.getProfilePic().isEmpty()){
            Picasso.get().load(doctorModel.getProfilePic()).into(holder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return doctorModels.size();
    }



}
