package com.android.campusmoments.Service;

import java.util.Date;

public class Notification {
    private int mSenderId;
    private int mMomentId;
    private long mTime = new Date().getTime();
    private String mContent;
    private int mType; // 0 : like, 1 : comment, 2 : following user post, 3 : private message

    public Notification() {}

    public Notification(int senderId, String content, int type) {
        mSenderId = senderId;
        mContent = content;
        mType = type;
    }

    public Notification(int senderId, int momentId, String content, int type) {
        mSenderId = senderId;
        mMomentId = momentId;
        mContent = content;
        mType = type;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public int getMomentId() {
        return mMomentId;
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


    public void setSenderId(int senderId) {
        mSenderId = senderId;
    }

    public void setMomentId(int momentId) {
        mMomentId = momentId;
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

}
