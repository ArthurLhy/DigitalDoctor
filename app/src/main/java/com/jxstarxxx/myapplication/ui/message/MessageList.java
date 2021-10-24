package com.jxstarxxx.myapplication.ui.message;

public class MessageList {

    private String username, userid, userImage, lastMessage;
    private int messageUnseen;

    public MessageList(String username, String userid, String userImage, String lastMessage, int messageUnseen) {
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
