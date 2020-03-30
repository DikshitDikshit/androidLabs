package com.example.androidlabs;



/**
 * Message representing class
 */
public class Message {
    private long id;
    private String message;
    private String type;

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
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", type=" + type +
                '}';
    }
}