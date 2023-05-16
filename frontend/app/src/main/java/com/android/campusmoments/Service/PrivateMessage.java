package com.android.campusmoments.Service;

public class PrivateMessage {
    public int id;
    public String avatar;
    public String time;
    public String username;
    public String content;
    public boolean isSentByMe; // Added property to indicate if the message was sent by the user

    public PrivateMessage(int id, String avatar, String time, String username, String content, boolean isSentByMe) {
        this.id = id;
        this.avatar = avatar;
        this.time = time;
        this.username = username;
        this.content = content;
        this.isSentByMe = isSentByMe;
    }
}