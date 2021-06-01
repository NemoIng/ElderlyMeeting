package com.example.elderlymeeting.ui.friends;

public class FriendsList {

    private String imageUrl, fullName, age;

    public FriendsList(String imageUrl, String fullName, String age) {
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.age = age;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getFullName(){
        return fullName;
    }

    public String getAge(){
        return age;
    }
}
