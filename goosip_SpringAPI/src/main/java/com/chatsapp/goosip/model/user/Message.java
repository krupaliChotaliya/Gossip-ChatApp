package com.chatsapp.goosip.model.user;

public class Message {

    private String message,senderId;
    private int feeling;
    private Long timestamp;
    private String type="text";
    public Message(String message, String senderId, int feeling, Long timestamp, String type) {
        this.message = message;
        this.senderId = senderId;
        this.feeling = feeling;
        this.timestamp = timestamp;
        this.type = type;
    }
    public Message() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
