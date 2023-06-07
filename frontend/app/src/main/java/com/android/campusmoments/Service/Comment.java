package com.android.campusmoments.Service;

import org.json.JSONObject;

import java.util.List;

public class Comment implements Comparable<Comment>{
    private int id;
    private int by; // 评论者id
    private String avatarPath;
    private String username;
    private String time;
    private String content;

    public Comment(JSONObject obj) {
        try {
            id = obj.getInt("id");
            by = obj.getInt("by");
            username = Services.checkObjStr(obj, "usr_username");
            avatarPath = Services.checkObjStr(obj, "usr_avatar");
            time = Services.checkObjStr(obj, "created_at");
            content = Services.checkObjStr(obj, "content");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int compareTo(Comment comment) {
        return this.id - comment.id; // 按照 评论时间（id） 排序, 从小到大
    }
    public int getId() {
        return id;
    }
    public int getUserId() {
        return by;
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
