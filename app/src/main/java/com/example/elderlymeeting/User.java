package com.example.elderlymeeting;

public class User {

    public String fullName, age, email, bio, picture;
    public String[] hobby;

    public User (String fullName, String age, String email, String bio, String[] hobby, String picture){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.bio = bio;
        this.hobby = hobby;
        this.picture = picture;
    }
}
