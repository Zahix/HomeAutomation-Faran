package com.example.zahid.homeautomation.Model;

public class Account {
    private String name;
    private String email;
    private String devicemac;
    private String liverequest;
    private String gender;


    public Account() {
    }



    public Account(String email, String devicemac, String liverequest, String name, String gender) {
        this.email = email;
        this.devicemac = devicemac;
        this.liverequest = liverequest;
        this.name = name;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevicemac() {
        return devicemac;
    }

    public void setDevicemac(String devicemac) {
        this.devicemac = devicemac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLiverequest() {
        return liverequest;
    }

    public void setLiverequest(String liverequest) {
        this.liverequest = liverequest;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", devicemac='" + devicemac + '\'' +
                ", liverequest='" + liverequest + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
