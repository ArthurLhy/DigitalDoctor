package com.jxstarxxx.myapplication.ui.message;

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

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageView> {

    private final List<MessageList> messageLists;
    private final Context content;

    public MessageAdapter(List<MessageList> messageLists, Context content) {
        this.messageLists = messageLists;
        this.content = content;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageView(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageView holder, int position) {
        MessageList message_list = messageLists.get(position);

        if(!message_list.getUserImage().isEmpty()){
            Picasso.get().load(message_list.getUserImage()).into(holder.userImage);
        }
        holder.userName.setText(message_list.getUsername());
        holder.lastMessage.setText(message_list.getLastMessage());

        if(message_list.getMessageUnseen() == 0) {
            holder.messageNumber.setVisibility(View.GONE);
        }
        else{
            holder.messageNumber.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    static class MessageView extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;
        private TextView lastMessage;
        private TextView messageNumber;

        public MessageView(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.message_user_image);
            userName = itemView.findViewById(R.id.message_username);
            lastMessage = itemView.findViewById(R.id.last_message);
            messageNumber = itemView.findViewById(R.id.message_number);

        }
    }
}
