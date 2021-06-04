package com.example.elderlymeeting.ui.users;

//all hobbies that a user has
public class Hobbies {

    private String hobby1, hobby2, hobby3, hobby4;

    public Hobbies(String hobby1, String hobby2, String hobby3, String hobby4){
        this.hobby1 = hobby1;
        this.hobby2 = hobby2;
        this.hobby3 = hobby3;
        this.hobby4 = hobby4;
    }

    public String getHobby1() {
        return hobby1;
    }
    public String getHobby2() {
        return hobby2;
    }
    public String getHobby3() {
        return hobby3;
    }
    public String getHobby4() {
        return hobby4;
    }

}
