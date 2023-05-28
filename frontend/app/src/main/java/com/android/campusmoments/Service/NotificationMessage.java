package com.android.campusmoments.Service;

import java.util.Date;

public class NotificationMessage {
    private int mSenderId;
    private int mMomentId;
    private String mUsername;
    private long mTime = new Date().getTime();
    private String mContent;
    private int mType; // 0 : like, 1 : comment, 2 : following user post, 3 : private message
    private boolean mIsRead = false;
    private boolean mIsNotified = false;

    public NotificationMessage() {}

    public NotificationMessage(int senderId, String username, String content, int type) {
        mSenderId = senderId;
        mUsername = username;
        mContent = content;
        mType = type;
    }

    public NotificationMessage(int senderId, int momentId, String username, String content, int type) {
        mSenderId = senderId;
        mMomentId = momentId;
        mUsername = username;
        mContent = content;
        mType = type;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public int getMomentId() {
        return mMomentId;
    }

    public String getUsername() {
        return mUsername;
    }

    public long getTime() {
        return mTime;
    }

    public String getContent() {
        return mContent;
    }

    public int getType() {
        return mType;
    }

    public boolean getIsRead() {
        return mIsRead;
    }

    public boolean getIsNotified() {
        return mIsNotified;
    }

    public void setSenderId(int senderId) {
        mSenderId = senderId;
    }

    public void setMomentId(int momentId) {
        mMomentId = momentId;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setType(int type) {
        mType = type;
    }

    public void setIsRead(boolean isRead) {
        mIsRead = isRead;
    }

    public void setIsNotified(boolean isNotified) {
        mIsNotified = isNotified;
    }

}
