package com.teamgrid.fashhub.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String name;
    public String address;
    public String phone;
    public String email;
    public String gender;
    public String role;
    public String bio;
    public Boolean isVerified;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String address, String phone, String email,
                String gender, String bio, Boolean isVerified, String role) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.bio = bio;
        this.email = email;
        this.isVerified = isVerified;
        this.role = role;
    }

}