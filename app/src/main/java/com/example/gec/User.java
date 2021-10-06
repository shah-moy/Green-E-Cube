package com.example.gec;

public class User {

    public String fullName, email, contact;

    public User() {

    }
    public User (String fullName, String email, String contact){
        this.fullName = fullName;
        this.email = email;
        this.contact = contact;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
