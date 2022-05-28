package com.javainstitute.androidcodingexamtest01.Model;

public class Friends {

    String userEmail;
    String friendEmail;
    String friendUsername;
    String lastMsgTime;

    public Friends(String userEmail, String friendEmail, String friendUsername, String lastMsgTime) {
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
        this.friendUsername = friendUsername;
        this.lastMsgTime = lastMsgTime;
    }

    public Friends() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
