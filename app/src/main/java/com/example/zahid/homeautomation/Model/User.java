package com.example.zahid.homeautomation.Model;

public class User {
    private String userName;
    private String email;
    private String userGender;

    public User(String userName, String email, String userGender) {
        this.userName = userName;
        this.email = email;
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
