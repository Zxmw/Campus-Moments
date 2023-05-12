package com.android.campusmoments.Fragment.data.model;

import android.util.Log;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private final int userId;
    private String username;
    private String bio;
    private String avatar;
    public int followedNum = 0;
    public int followingNum = 0;
    public LoggedInUser(int userId, String username, String bio, String avatar) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.avatar = avatar;
    }
    public LoggedInUser(LoggedInUser user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.avatar = user.getAvatar();
    }
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatar() {
        return avatar;
    }

}