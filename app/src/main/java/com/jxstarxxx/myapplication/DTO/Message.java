package com.jxstarxxx.myapplication.DTO;

public class Message {

    private String message;
    private String senderID;
    private String time;

    public Message(String message, String senderID, String time) {
        this.message = message;
        this.senderID = senderID;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getTime() {
        return time;
    }
}
