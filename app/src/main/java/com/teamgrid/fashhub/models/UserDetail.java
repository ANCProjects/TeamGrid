package com.teamgrid.fashhub.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class UserDetail implements Serializable {

    public String name;
    public String address;
    public String phone;
    public String email;
    public String gender;
    public String role;
    public String bio;
    public Boolean isVerified;
    public String avaterUrl;

    public UserDetail() {
        // Default constructor required for calls to DataSnapshot.getValue(UserDetail.class)
    }

    public UserDetail(String name, String address, String phone, String email,
                      String gender, String bio, Boolean isVerified, String role, String avaterUrl) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.gender = gender;
        this.bio = bio;
        this.email = email;
        this.isVerified = isVerified;
        this.role = role;
        this.avaterUrl = avaterUrl;
    }

}