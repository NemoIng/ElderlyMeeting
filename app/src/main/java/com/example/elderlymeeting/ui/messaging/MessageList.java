package com.example.elderlymeeting.ui.messaging;

//values of an index in the messageList
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
