package com.example.elderlymeeting.ui.messaging;

public class MessageList {
    private String name, date, message;

    public MessageList(String name, String date, String message) {
        this.name = name;
        this.date = date;
        this.message = message;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

    public String getMessage(){return message;}
}
