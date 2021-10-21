package com.jxstarxxx.myapplication.Chat;

public class Messages {

    private String message;
    private String senderID;
    private String name;
    private String time;
    private String date;

    public Messages(String message, String senderID, String name, String time, String date) {
        this.message = message;
        this.senderID = senderID;
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
