package com.android.campusmoments.Service;

import com.android.campusmoments.Fragment.data.model.LoggedInUser;

public class User {
    public int id = 1;
    public String username = "name";
    public String bio = "bio";
    public String avatar = null;

    public int followedNum = 0;
    public int followingNum = 0;

    public boolean isBlocked = false;

    public User(LoggedInUser user) {
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.avatar = user.getAvatar();
    }
}
