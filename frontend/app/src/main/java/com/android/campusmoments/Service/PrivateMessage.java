package com.android.campusmoments.Service;

import java.util.Date;

public class PrivateMessage {
    private int mSenderId;
    private int mReceiverId;
    private String mAvatar;
    private long mTime = new Date().getTime();
    private String mUsername;
    private String mContent;

    public PrivateMessage() {}

    public PrivateMessage(int senderId, int receiverId, String avatar, String username, String content) {
        mSenderId = senderId;
        mReceiverId = receiverId;
        mAvatar = avatar;
        mUsername = username;
        mContent = content;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public int getReceiverId() {
        return mReceiverId;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public long getTime() {
        return mTime;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getContent() {
        return mContent;
    }

    public void setSenderId(int senderId) {
        mSenderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        mReceiverId = receiverId;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setContent(String content) {
        mContent = content;
    }



}