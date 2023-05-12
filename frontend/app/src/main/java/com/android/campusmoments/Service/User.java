package com.android.campusmoments.Service;

import org.json.JSONObject;

public class User {
    public int id = 1;
    public String username = "name";
    public String bio = "bio";
    public String avatar = null;

    public int followingNum = 0;

    public boolean isBlocked = false;

    public User(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            username = jsonObject.getString("username");
            bio = jsonObject.getString("bio");
            avatar = jsonObject.getString("avatar");
//            followingNum = jsonObject.getInt("followingNum");
//            isBlocked = jsonObject.getBoolean("isBlocked");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
