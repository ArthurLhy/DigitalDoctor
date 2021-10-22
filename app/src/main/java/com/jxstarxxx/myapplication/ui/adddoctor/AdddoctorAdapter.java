package com.jxstarxxx.myapplication.ui.adddoctor;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jxstarxxx.myapplication.R;

import java.util.ArrayList;

public class AdddoctorAdapter extends RecyclerView.Adapter<AdddoctorAdapter.SearchViewHolder> {
    AdddoctorFragment context;
    ArrayList<String> fullNameList;
    ArrayList<String> userNameList;

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView full_name, user_name;

        public SearchViewHolder(View itemView) {
            super(itemView);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public AdddoctorAdapter(AdddoctorFragment context, ArrayList<String> fullNameList, ArrayList<String> userNameList) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.userNameList = userNameList;
    }

    @Override
    public AdddoctorAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getActivity()).inflate(R.layout.fragment_doctorlist, parent, false);
        return new AdddoctorAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.full_name.setText(fullNameList.get(position));
        holder.user_name.setText(userNameList.get(position));


        holder.full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getActivity(), "Full Name Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }
}