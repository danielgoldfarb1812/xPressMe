package com.example.xpressme;

import java.util.ArrayList;
import java.util.List;

public class User {
    String uId;
    String firstName;
    String lastName;
    String email;
    String phone;
    List<CommunicationBoard> communicationBoards;

    // בנאי ללא טלפון
    public User (String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = "";
        this.communicationBoards = new ArrayList<CommunicationBoard>();
    }
    // בנאי עם טלפון
    public User (String firstName, String lastName, String email, String phone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.communicationBoards = new ArrayList<CommunicationBoard>();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<CommunicationBoard> getCommunicationBoards() {
        return communicationBoards;
    }

    public void setCommunicationBoards(List<CommunicationBoard> communicationBoards) {
        this.communicationBoards = communicationBoards;
    }
}
