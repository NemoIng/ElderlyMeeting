package com.example.elderlymeeting.ui.messaging;

import java.util.Date;


public class Messaging {
    private String text;
    private String user;
    private long time;

    public Messaging(String text, String user){
        this.text = text;
        this.user = user;

        time = new Date().getTime();
    }

    public String getText(){
        return  text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user =  user;
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time =  time;
    }
}
