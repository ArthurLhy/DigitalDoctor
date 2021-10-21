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

import java.util.List;

public class MessageListView extends RecyclerView.Adapter<MessageListView.MessageView> {

    private final List<MessageList> messageLists;
    private final Context content;

    public MessageListView(List<MessageList> messageLists, Context content) {
        this.messageLists = messageLists;
        this.content = content;
    }

    @NonNull
    @Override
    public MessageListView.MessageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageView(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListView.MessageView holder, int position) {

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
