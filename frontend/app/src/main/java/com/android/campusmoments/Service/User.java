package com.android.campusmoments.Service;

import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

public class User {
    public int id = 1;
    public String username = "name";
    public String bio = "bio";
    public String avatar;
    public List<Integer> followList;
    public List<Integer> blockList;

    public boolean isBlocked = false;

    public User(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("id");
            username = jsonObject.getString("username");
            bio = jsonObject.getString("bio");
            if (jsonObject.isNull("avatar"))
                avatar = null;
            else
                avatar = jsonObject.getString("avatar");
            followList = Services.jsonArrayToList(jsonObject.getJSONArray("follow_list"));
            blockList = Services.jsonArrayToList(jsonObject.getJSONArray("block_list"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
