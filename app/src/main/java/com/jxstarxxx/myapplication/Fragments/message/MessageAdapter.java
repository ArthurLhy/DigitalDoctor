package com.jxstarxxx.myapplication.Fragments.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jxstarxxx.myapplication.Chat.ChatActivity;
import com.jxstarxxx.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageView> {

    private List<MessageList> messageLists;
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
            holder.lastMessage.setTextColor(Color.parseColor("#019688"));
        }
        else{
            holder.messageNumber.setVisibility(View.VISIBLE);
            holder.messageNumber.setText(String.valueOf(message_list.getMessageUnseen()));
            holder.lastMessage.setTextColor(Color.parseColor("#009688"));
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(content, ChatActivity.class);
                intent.putExtra("username", message_list.getUsername());
                intent.putExtra("userImage", message_list.getUserImage());
                intent.putExtra("userID", message_list.getUserid());
                intent.putExtra("chatID", message_list.getChatID());
                Log.i("open the chat of:", message_list.getChatID());
                content.startActivity(intent);
            }
        });
    }

    public void updateList(List<MessageList> messageLists) {
        this.messageLists = messageLists;
        notifyDataSetChanged();
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
        private CardView root;

        public MessageView(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.message_user_image);
            userName = itemView.findViewById(R.id.message_username);
            lastMessage = itemView.findViewById(R.id.last_message);
            messageNumber = itemView.findViewById(R.id.message_number);
            root =itemView.findViewById(R.id.root);

        }
    }
}
