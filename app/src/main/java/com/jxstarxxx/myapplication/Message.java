package com.jxstarxxx.myapplication;

public class Message {

    private String message;
    private String sender;
    private String time;
    private long timestamp;

    public Message(String message, String sender, String time, long timestamp) {
        this.message = message;
        this.sender = sender;
        this.time = time;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
