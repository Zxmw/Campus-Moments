package com.android.campusmoments.Service;

import org.json.JSONObject;

import java.util.List;

public class Comment {
    private int id;
    private String avatarPath;
    private String username;
    private String time;
    private String content;

    public Comment(JSONObject obj) {
        try {
            username = Services.checkObjStr(obj, "usr_username");
            avatarPath = Services.checkObjStr(obj, "usr_avatar");
            time = Services.checkObjStr(obj, "created_at");
            content = Services.checkObjStr(obj, "content");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }
    public String getTime() {
        return time;
    }
    public String getContent() {
        return content;
    }
    public String getAvatarPath() {
        return avatarPath;
    }
}
