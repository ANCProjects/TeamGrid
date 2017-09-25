package com.teamgrid.fashhub.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class UserDetail implements Serializable {

    private String name, phone, email, uid, avaterUrl;
    private Boolean isVerified;
    private String gender, role, address, bio;

    public UserDetail() {
    }

    public UserDetail(String name, String phone, String email, String uid, String avaterUrl, Boolean isVerified,
                      String gender, String role, String address, String bio) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.uid = uid;
        this.avaterUrl = avaterUrl;
        this.isVerified = isVerified;
        this.gender = gender;
        this.role = role;
        this.address = address;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getAvaterUrl() {
        return avaterUrl;
    }
    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }
    public Boolean getIsVerified() {
        return isVerified;
    }
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getBio() {return bio;}
    public void setBio(String bio) {this.bio = bio;}
}