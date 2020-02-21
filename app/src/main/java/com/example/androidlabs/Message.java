package com.example.androidlabs;

public class Message {
    private String message;
    private String type;
    private long id;
    Message(long id,String message, String type) {
        this.id = id;
        this.message = message;
        this.type = type;
    }

    String getMessage() {
        return message;
    }

    String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", type=" + type +
                '}';
    }
}