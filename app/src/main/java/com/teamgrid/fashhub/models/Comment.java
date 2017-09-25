package com.teamgrid.fashhub.models;

/**
 * Created by inventor on 3/9/17.
 */
public class Comment {

    private UserDetail user, talking_to;
    private String message;
    private String timeStamp;
    private File file;

    public Comment() {
    }

    public Comment(UserDetail user, String message, File file, String timeStamp) {
        this.message = message;
        this.file = file;
        this.user = user;
        this.timeStamp = timeStamp;
    }

    public Comment(UserDetail user, UserDetail talking_to, String message, File file, String timeStamp) {
        this.message = message;
        this.user = user;
        this.file = file;
        this.timeStamp = timeStamp;
        this.talking_to = talking_to;
    }


    public UserDetail getUser() {
        return user;
    }
    public void setUser(UserDetail user) {
        this.user = user;
    }
    public UserDetail getTalking_to() {return talking_to;}
    public void setTalking_to(UserDetail talking_to) {this.talking_to = talking_to;}
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public File getFile() {return file;}
    public void setFile(File file) {this.file = file;}
}
