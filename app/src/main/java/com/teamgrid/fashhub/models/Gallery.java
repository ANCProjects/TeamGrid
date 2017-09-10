package com.teamgrid.fashhub.models;

/**
 * Created by Johnbosco on 30-Aug-17.
 */

public class Gallery {

    public String username;
    public String email;
    public String role;
    public String downloadUrl;

    public Gallery(){

    }

    public Gallery(String downloadUrl){
        this.downloadUrl = downloadUrl;
    }
}
