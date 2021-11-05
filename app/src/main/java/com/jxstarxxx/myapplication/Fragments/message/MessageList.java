package com.jxstarxxx.myapplication.Fragments.message;

public class MessageList {

    private String username, userid, userImage, lastMessage, chatID;
    private int messageUnseen;

    public MessageList(String chatID, String username, String userid, String userImage, String lastMessage, int messageUnseen) {
        this.chatID = chatID;
        this.username = username;
        this.userid = userid;
        this.userImage = userImage;
        this.lastMessage = lastMessage;
        this.messageUnseen = messageUnseen;
    }

    public String getUsername() {
        return username;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getChatID() {
        return chatID;
    }

    public String getUserid() {
        return userid;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getMessageUnseen() {
        return messageUnseen;
    }
}
