package com.example.elderlymeeting.ui.Users;

public class Users {

    public String fullName, age, email, bio, picture;
    public String[] hobby;

    public Users(){

    }

    public Users(String fullName, String age, String email, String bio, String[] hobby, String picture){
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.bio = bio;
        this.hobby = hobby;
        this.picture = picture;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String[] getHobby() {
        return hobby;
    }

    public void setHobby(String[] hobby) {
        this.hobby = hobby;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
